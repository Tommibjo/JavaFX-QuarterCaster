/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.codes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Tommi
 */
public class CodeReader {

    private ArrayList<String> codes;

    public CodeReader() {
        this.codes = new ArrayList<>();
    }

    public String readCodes() {
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

    public boolean checkCode(String code) {
        return this.codes.contains(code);
    }

    public ArrayList<String> getCodes() {
        return codes;
    }
}
