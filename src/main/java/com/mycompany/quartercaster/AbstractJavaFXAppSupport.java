/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author tommib
 */
public abstract class AbstractJavaFXAppSupport extends Application {

    private static String[] args;
    protected ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(getClass(), args);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }
    
    @Override
    public void stop() throws Exception{
        super.stop();
        context.close();
    }
    
    protected static void launchApp(Class<? extends AbstractJavaFXAppSupport> clazz, String[] args){
        AbstractJavaFXAppSupport.args = args;
        Application.launch(clazz, args);
    }

}
