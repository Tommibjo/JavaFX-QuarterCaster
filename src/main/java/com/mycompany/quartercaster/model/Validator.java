/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tommi
 */
@Configuration
@Service
public class Validator {

    private ArrayList<String> codes;

    public Validator() {
        this.codes = new ArrayList<>();
    }

    /*
    Uploads wanted codes to softwares memory.
    */  
    public String uploadCodes() {
        try (Scanner scanner = new Scanner(new File("koodit.txt"))) {
            while (scanner.hasNextLine()) {
                this.codes.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return "ERROR Exception: " + e;
        }
        return "-> koodit.txt loaded to QuarterCaster";
    }

    public boolean foundFromList(String code) {
        return this.codes.contains(code);
    }

    public ArrayList<String> getCodes() {
        return codes;
    }
}
