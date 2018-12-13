/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.model;

import com.mycompany.quartercaster.model.deliveries.Shipment;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tommib
 */
@Service
public class ExcelReader {

    @Autowired
    private Validator validate;
    private ArrayList<String> weeks;
    private FileChooser chooser;
    private File selectedFile;
    private int codesTotal;
    private ObservableList<Shipment> Shipments;

    public ExcelReader() {
        this.chooser = new FileChooser();
        this.weeks = new ArrayList<>();
        this.codesTotal = 0;
        this.Shipments = FXCollections.observableArrayList();
    }

    public File selectFile() {
        this.selectedFile = this.chooser.showOpenDialog(new Stage());
        return this.selectedFile;
    }

    public ObservableList iterateSelectedFile() throws ParseException {

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

                    if (dataFormatter.formatCellValue(cell).equals("Toimituspvm")) {
                        String timeFrame = dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(row.getRowNum()).getCell(cell.getColumnIndex() + 1));
                        String[] timeFrameSplitted = timeFrame.split("-");
                        String start = timeFrameSplitted[0].replaceAll("\\s+", "").replaceAll("\\.", "/");
                        String end = timeFrameSplitted[1].replaceAll("\\s+", "").replaceAll("\\.", "/");;

                        String format = "dd/MM/yyyy";
                        SimpleDateFormat df = new SimpleDateFormat(format);
                        Date startDate = df.parse(start);
                        Date endDate = df.parse(end);

                        Calendar cal = Calendar.getInstance();
                        int startWeek = 0;
                        int endWeek = 0;
                        cal.setTime(startDate);
                        startWeek = cal.get(Calendar.WEEK_OF_YEAR);
                        cal.setTime(endDate);
                        endWeek = cal.get(Calendar.WEEK_OF_YEAR);
                        System.out.println("Start week: " + startWeek);
                        System.out.println("End week: " + endWeek);

                        while (startWeek <= endWeek) {
                            this.weeks.add(Integer.toString(startWeek));
                            startWeek++;
                        }

                    }
                    // Jos "Nimiketunnus" sarake löytyy, niin iteroi siitä alespäin nimiketunnukset läpi
                    if (dataFormatter.formatCellValue(cell).equals("Nimiketunnus")) {
                        for (int i = cell.getRowIndex(); i < sheet.getLastRowNum(); i++) {
                            String currentCell = dataFormatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(cell.getColumnIndex()));
                            if (this.validate.foundFromList(currentCell) == true) {
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
            //        this.total.setText(this.codesTotal + " products");
            System.out.println(this.Shipments);
            //         this.logLine.add("-> Found total " + this.codesTotal + " products which match the codes within 'koodit.txt'");
            //         this.log.setItems(this.logLine);
            //         this.productList.setItems(this.Shipments);
        } catch (IOException ioe) {
            //          this.logLine.add("ERROR IOException: " + ioe.getMessage());
            //          this.log.setItems(this.logLine);
        } catch (InvalidFormatException ife) {
            //           this.logLine.add("ERROR InvalidFormatException error: " + ife.getMessage());
            //           this.log.setItems(this.logLine);
        }
        return this.Shipments;
    }

    public ArrayList<String> getWeeks() {
        return weeks;
    }

    public void setWeeks(ArrayList<String> weeks) {
        this.weeks = weeks;
    }

    public FileChooser getChooser() {
        return chooser;
    }

    public void setChooser(FileChooser chooser) {
        this.chooser = chooser;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public int getCodesTotal() {
        return codesTotal;
    }

    public void setCodesTotal(int codesTotal) {
        this.codesTotal = codesTotal;
    }

    public ObservableList<Shipment> getShipments() {
        return Shipments;
    }

    public void setShipments(ObservableList<Shipment> Shipments) {
        this.Shipments = Shipments;
    }

}
