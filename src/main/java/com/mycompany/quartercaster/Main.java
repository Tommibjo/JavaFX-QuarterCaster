/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author tommib
 */
@Lazy
@SpringBootApplication
public class Main extends AbstractJavaFXAppSupport {

    @Autowired
    private ControllersConfig.View view;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
       primaryStage.setTitle("Forecasts");
       primaryStage.setScene(new Scene(view.getView()));
       primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launchApp(Main.class, args);
    }

}
