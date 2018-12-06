/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import com.mycompany.quartercaster.codes.CodeReader;
import com.mycompany.quartercaster.codes.deliveries.Shipment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
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
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tommib
 */
public class Controller {

    @Autowired
    BeanFactory beanFactory;
    @FXML
    private Label inputFile;
    @FXML
    private Label total;
    @FXML
    private ListView<String> log;
    private ObservableList<String> items;
    @FXML
    private ListView<String> productList;
    private ObservableList<String> codeItems;

    private Text logText;
    private CodeReader codeReader;
    private FileChooser fileChooser;
    private ArrayList<Shipment> shipments;
    private int codesTotal;

    public Controller() {
        this.log = new ListView<>();
        this.items = FXCollections.observableArrayList();
        this.productList = new ListView<>();
        this.codeItems = FXCollections.observableArrayList();
        this.logText = new Text();
        this.codeReader = new CodeReader(); // Olio koodien lukemiseen koodit.txt tiedostosta.
        this.fileChooser = new FileChooser();
        this.shipments = new ArrayList<>();
        this.codesTotal = 0;
    }

    @FXML
    public void initialize() {
        /*
        // Lukee koodit.txt tiedostosta löytyvät tuotekoodit. Näitä käytetään InputFilestä löytyvien koodien validiuden tarkistamiseen.
         */
        String fileReading = this.codeReader.readCodes();
        this.items.add(fileReading);
        this.log.setItems(items);
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
        this.items.add("-> Selected file: " + selectedFile.getName());

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
                            Cell currentCell = workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex());
                            Cell deliveryCell = workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex() + 3);
                            Cell deliveredQuantity = workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex() + 5);
                            // Jos solu löytyy codeReaderin ArrayListista, niin tällöin solun koodi on ennustamiseen validi 
                            if (this.codeReader.checkCode(dataFormatter.formatCellValue(currentCell)) == true) {
                                String date = dataFormatter.formatCellValue(deliveryCell);
                                double quantity = Double.parseDouble(dataFormatter.formatCellValue(deliveredQuantity).replaceAll(",", "."));
                                if (this.shipments.contains(new Shipment(dataFormatter.formatCellValue(currentCell)))) {
                                    for (Shipment shipment : this.shipments) {
                                        if (shipment.equals(new Shipment(dataFormatter.formatCellValue(currentCell)))) {
                                            shipment.addDelivery(date, quantity);
                                        }
                                    }
                                } else {
                                    Shipment shipment = new Shipment(dataFormatter.formatCellValue(currentCell));
                                    shipment.addDelivery(date, quantity);
                                    this.shipments.add(shipment);
                                    this.codeItems.add(dataFormatter.formatCellValue(currentCell));
                                    this.codesTotal++;
                                }
                            }
                        }
                    }

                }
            }
            this.total.setText(this.codesTotal + " products");
            System.out.println(this.shipments);
            this.items.add("-> Found " + this.codesTotal + " different products");
            this.log.setItems(this.items);
            this.productList.setItems(this.codeItems);
        } catch (IOException ioe) {
            this.items.add("ERROR IOException: " + ioe.getMessage());
            this.log.setItems(this.items);
        } catch (InvalidFormatException ife) {
            this.items.add("ERROR InvalidFormatException error: " + ife.getMessage());
            this.log.setItems(this.items);
        }
    }
}
