/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.model;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

/**
 *
 * @author tommib
 */
@Service
public class ExcelReader {

    private FileChooser chooser;
    private File selectedFile;

    public ExcelReader() {
        this.chooser = new FileChooser();
    }

    public File selectFile() {
        this.selectedFile = this.chooser.showOpenDialog(new Stage());
        return this.selectedFile;
    }
}
