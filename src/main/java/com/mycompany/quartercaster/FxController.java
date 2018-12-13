/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import com.mycompany.quartercaster.model.ExcelReader;

import com.mycompany.quartercaster.model.deliveries.Shipment;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tommib
 */
public class FxController {

    @Autowired
    private ExcelReader excelReader;

    @FXML
    private Label inputFile;
    @FXML
    private ListView<String> log;
    private ObservableList<String> logLine;
    @FXML
    private ListView<Shipment> shipment;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BarChart<Number, Number> forecastVisual;
    @FXML
    private Label total;
    private int newClick;
    private int lastClick;

    public FxController() {
        this.log = new ListView<>();
        this.logLine = FXCollections.observableArrayList();
        this.shipment = new ListView<>();
        this.newClick = 0;
        this.lastClick = 0;

    }

    @FXML
    public void selectInput() throws IOException, InvalidFormatException, ParseException {
        File selectedFile = this.excelReader.selectFile();
        ObservableList<Shipment> shipments = this.excelReader.iterateSelectedFile();
        this.shipment.setItems(shipments);
        this.inputFile.setText("Sales from " + selectedFile.getName() + " found with:");
        this.logLine.add("-> Selected file: " + selectedFile.getName());
        this.total.setText("with " + this.excelReader.getCodesTotal() + " products");
        this.log.setItems(logLine);
    }

    @FXML
    public void showData() {

        if (this.newClick > this.lastClick) {
            this.forecastVisual.getData().clear();
        }
        Shipment shipment = this.shipment.getSelectionModel().getSelectedItem();
        System.out.println("Clicked on: " + shipment);
        this.xAxis.setLabel("Week");
        this.yAxis.setLabel("Kg");
        XYChart.Series series = new XYChart.Series();
        series.setName(shipment.getProductName());
        for (int i = 0; i < this.excelReader.getWeeks().size(); i++) {
            if (shipment.getDelivery().containsKey(this.excelReader.getWeeks().get(i))) {
                series.getData().add(new XYChart.Data(this.excelReader.getWeeks().get(i), shipment.getDelivery().get(this.excelReader.getWeeks().get(i))));
            } else {
                series.getData().add(new XYChart.Data(this.excelReader.getWeeks().get(i), 0));
            }
        }
        this.forecastVisual.getData().add(series);
        this.forecastVisual.setTitle(this.shipment.getSelectionModel().getSelectedItems() + " Forecast");
        this.lastClick = this.newClick;
        this.newClick++;
    }
}
