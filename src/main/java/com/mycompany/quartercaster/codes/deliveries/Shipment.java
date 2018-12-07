/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.codes.deliveries;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Tommi
 */
public class Shipment {

    private String productCode;
    private HashMap<String, Double> delivery;

    public Shipment(String productCode) {
        this.productCode = productCode;
        this.delivery = new HashMap<>();
    }
    
    public void addDelivery(String date, double quantity ){
        this.delivery.put(date, quantity);
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public HashMap<String, Double> getDelivery() {
        return delivery;
    }

    public void setDelivery(HashMap<String, Double> delivery) {
        this.delivery = delivery;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.productCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Shipment other = (Shipment) obj;
        if (!Objects.equals(this.productCode, other.productCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return productCode;
    }
}
