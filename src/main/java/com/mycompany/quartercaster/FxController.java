/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import com.mycompany.quartercaster.model.ExcelReader;
import com.mycompany.quartercaster.model.ForecastGenerator;

import com.mycompany.quartercaster.model.deliveries.Shipment;
import java.io.File;
import java.text.ParseException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tommib
 */
public class FxController {

    @Autowired
    private ExcelReader excelReader;

    @Autowired
    private ForecastGenerator forecastGenerator;

    @FXML
    private Label selectedFile;
    @FXML
    private Label total;
    @FXML
    private TextField forecastSpan;
    @FXML
    private ListView<String> log;
    private ObservableList<String> logLine;
    @FXML
    private ListView<Shipment> shipments;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BarChart<Number, Number> forecastVisual;

    private int newClick;
    private int lastClick;

    public FxController() {
        this.log = new ListView<>();
        this.logLine = FXCollections.observableArrayList();
        this.shipments = new ListView<>();
        this.newClick = 0;
        this.lastClick = 0;

    }

    @FXML
    public void selectInput() throws ParseException {

        File file = this.excelReader.selectFile();
        ObservableList<Shipment> shipmentList = this.excelReader.iterateSelectedFile();
        this.shipments.setItems(shipmentList);
        this.selectedFile.setText("Sales from " + file.getName() + " found with:");
        this.logLine.add("Selected file: " + file.getName());
        this.total.setText("with " + this.excelReader.getCodesTotal() + " products");
        this.log.setItems(logLine);
    }

    @FXML
    public void showData() {

        if (this.newClick > this.lastClick) {
            this.forecastVisual.getData().clear();
        }
        this.xAxis.setLabel("Week");
        this.yAxis.setLabel("Kg");
        XYChart.Series series = this.forecastGenerator.populateChart(this.excelReader.getWeeks(), this.shipments.getSelectionModel().getSelectedItem());
        this.forecastVisual.getData().add(series);
        this.forecastVisual.setTitle(this.shipments.getSelectionModel().getSelectedItems() + " Forecast");
        this.lastClick = this.newClick;
        this.newClick++;
    }

    public ListView<String> getLog() {
        return log;
    }

    public void setLog(ListView<String> log) {
        this.log = log;
    }

    public ObservableList<String> getLogLine() {
        return logLine;
    }

    public void setLogLine(ObservableList<String> logLine) {
        this.logLine = logLine;
    }
    
    public void updateTimeSpan(){
        if(this.forecastSpan.getText().matches("^[0-9]*$")){
            System.out.println("Numbero!");
            this.forecastGenerator.setForecastSpan(Integer.parseInt(this.forecastSpan.getText()));
        }else{
            System.out.println("Ei ole numbero");
            this.forecastSpan.clear();
        }
    }
}
