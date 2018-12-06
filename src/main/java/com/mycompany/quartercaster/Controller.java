/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import com.mycompany.quartercaster.deliveries.Delivery;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Hyperlink;
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
    private Button dataInput;
    @FXML
    private Label inputFile;
    @FXML
    private ListView<String> log;
    private ObservableList<String> items;
    private Text logText;
    private String cellValue;

    public Controller() {
        this.logText = new Text();
        this.log = new ListView<String>();
        this.items = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {

    }

    @PostConstruct
    public void init() {

    }

    @FXML
    public void selectInput() throws IOException, InvalidFormatException {

        // Choose the file
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(new Stage());
        this.inputFile.setText(inputFile.getName());
        this.items.add(inputFile.getName());
        // Try reading the excel via Workbook
        try {

            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (dataFormatter.formatCellValue(cell).equals("Nimiketunnus")) {
                        int nimikeTunnusRow = cell.getColumnIndex();
                        System.out.println("Found: " + dataFormatter.formatCellValue(cell) + " at " + cell.getColumnIndex() + " " + cell.getRowIndex());

                        for (int i = cell.getRowIndex(); i < sheet.getLastRowNum(); i++) {
                            this.items.add(dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(nimikeTunnusRow)));
                            if (workbook.getSheetAt(0).getRow(i).getCell(nimikeTunnusRow) != null) {
                                Hyperlink link = workbook.getSheetAt(0).getRow(i).getCell(nimikeTunnusRow).getHyperlink();
                                if (link != null && getCodeStatus(dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(nimikeTunnusRow))) == true) {
                                    this.items.add(dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(nimikeTunnusRow)));
                                }
                            }
                        }
                        System.out.println("ColumnIndex" + cell.getColumnIndex());
                        System.out.println("Last row:" + sheet.getLastRowNum());
                    }
                }
            }

            this.log.setItems(items);
        } catch (IOException ioe) {
            this.items.add("IOException: " + ioe.getMessage());
            this.log.setItems(items);
        } catch (InvalidFormatException ife) {
            this.items.add("InvalidFormatException error: " + ife.getMessage());
            this.log.setItems(items);
        }
    }
    
    /* returns true if cell contains product code. False if not.*/
    public boolean getCodeStatus(String cell){
        int numbers = 0;
        for(int i = 0; i < cell.length(); i++){
            if(Character.isDigit(cell.charAt(i))){
                numbers++;
            }
        }
        if(numbers > 4){
            return true;
        }
        return false;
    }
}
