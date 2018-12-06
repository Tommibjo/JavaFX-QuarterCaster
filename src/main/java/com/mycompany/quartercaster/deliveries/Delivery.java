/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.deliveries;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author tommib
 */
@Service
@Scope(value="prototype")
public class Delivery {
    private int deliveryNr;
    private int orderNr;
    private int clientNr;
    private String clientName;
    private String productCode;
    private String innerDeliveryDate;
    private String deliveryDate;
    private String unit;
    private int orderedQuantity;
    private int deliveredQuantity;
    private int deliveredOverall;
    private String JOT;

    public int getDeliveryNr() {
        return deliveryNr;
    }

    public void setDeliveryNr(int deliveryNr) {
        this.deliveryNr = deliveryNr;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(int orderNr) {
        this.orderNr = orderNr;
    }

    public int getClientNr() {
        return clientNr;
    }

    public void setClientNr(int clientNr) {
        this.clientNr = clientNr;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInnerDeliveryDate() {
        return innerDeliveryDate;
    }

    public void setInnerDeliveryDate(String innerDeliveryDate) {
        this.innerDeliveryDate = innerDeliveryDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public int getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(int deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public int getDeliveredOverall() {
        return deliveredOverall;
    }

    public void setDeliveredOverall(int deliveredOverall) {
        this.deliveredOverall = deliveredOverall;
    }

    public String getJOT() {
        return JOT;
    }

    public void setJOT(String JOT) {
        this.JOT = JOT;
    }
    
    
}
