/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import com.mycompany.quartercaster.codes.Validator;
import com.mycompany.quartercaster.codes.deliveries.Shipment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author tommib
 */
public class Controller {

    @FXML
    private Label inputFile;
    @FXML
    private Label total;
    @FXML
    private ListView<String> log;
    private ObservableList<String> logLine;
    @FXML
    private ListView<Shipment> productList;
    private ObservableList<Shipment> Shipments;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BarChart<Number, Number> forecastVisual;

    private Validator validator;
    private FileChooser fileChooser;
    private ArrayList<Shipment> shipments;
    private int codesTotal;

    // These are used to check if BarChart should be cleared (Click on a new item -> clears the screen)
    // Could have been done better .. perhaps this will get refactored later :)
    private int newClick;
    private int lastClick;

    public Controller() {
        this.log = new ListView<>();
        this.logLine = FXCollections.observableArrayList();
        this.productList = new ListView<>();
        this.Shipments = FXCollections.observableArrayList();

        this.validator = new Validator(); // Olio koodien lukemiseen koodit.txt tiedostosta.
        this.fileChooser = new FileChooser();
        this.shipments = new ArrayList<>();
        this.codesTotal = 0;

        this.newClick = 0;
        this.lastClick = 0;
    }

    @FXML
    public void initialize() {
        /*
        // Lukee koodit.txt tiedostosta löytyvät tuotekoodit. Näitä käytetään InputFilestä löytyvien koodien validiuden tarkistamiseen.
         */
        String fileReading = this.validator.readCodes();
        this.logLine.add(fileReading);
        this.log.setItems(logLine);
    }

    @PostConstruct
    public void init() {

    }

    @FXML
    public void selectInput() throws IOException, InvalidFormatException {

        // Choose the file -popup
        File selectedFile = this.fileChooser.showOpenDialog(new Stage());
        this.inputFile.setText("Sales from " + selectedFile.getName() + " found with:");
        // Print the chosen file to log
        this.logLine.add("-> Selected file: " + selectedFile.getName());

        try {
            Workbook workbook = WorkbookFactory.create(selectedFile);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.rowIterator();

            // Iteroi koko exceliä läpi
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    // Jos "Nimiketunnus" sarake löytyy, niin iteroi siitä alespäin nimiketunnukset läpi
                    if (dataFormatter.formatCellValue(cell).equals("Nimiketunnus")) {
                        for (int i = cell.getRowIndex(); i < sheet.getLastRowNum(); i++) {
                            String currentCell = dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex()));
                            if (this.validator.foundFromList(currentCell) == true) {
                                String nameCell = dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex() + 1));
                                String deliveryCell = dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex() + 3));
                                double deliveryQuantity = Double.parseDouble(dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex() + 5)).replaceAll(",", "."));
                                if (this.Shipments.contains(new Shipment(currentCell, nameCell))) {
                                    for (Shipment shipment : this.Shipments) {
                                        if (shipment.equals(new Shipment(currentCell, nameCell))) {
                                            shipment.addDelivery(deliveryCell, deliveryQuantity);
                                        }
                                    }
                                } else {
                                    Shipment shipment = new Shipment(currentCell, nameCell);
                                    shipment.addDelivery(deliveryCell, deliveryQuantity);
                                    this.Shipments.add(shipment);
                                    this.codesTotal++;
                                }
                            }
                        }
                    }

                }
            }
            this.total.setText(this.codesTotal + " products");
            System.out.println(this.Shipments);
            this.logLine.add("-> Found " + this.codesTotal + " different products");
            this.log.setItems(this.logLine);
            this.productList.setItems(this.Shipments);
        } catch (IOException ioe) {
            this.logLine.add("ERROR IOException: " + ioe.getMessage());
            this.log.setItems(this.logLine);
        } catch (InvalidFormatException ife) {
            this.logLine.add("ERROR InvalidFormatException error: " + ife.getMessage());
            this.log.setItems(this.logLine);
        }
    }

    @FXML
    public void showData() {

        if (this.newClick > this.lastClick) {
            this.forecastVisual.getData().clear();
        }
        Shipment shipment = this.productList.getSelectionModel().getSelectedItem();
        System.out.println("Clicked on: " + shipment);
        this.xAxis.setLabel("Time");
        this.yAxis.setLabel("Kg");
        XYChart.Series series = new XYChart.Series();
        series.setName(shipment.getProductName());
        shipment.getDelivery().forEach((key, value) -> series.getData().add(new XYChart.Data(key, value)));
        this.forecastVisual.getData().add(series);
        this.forecastVisual.setTitle(this.productList.getSelectionModel().getSelectedItems() + " Forecast");
        this.lastClick = this.newClick;
        this.newClick++;
    }

}
