/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.quartercaster.model;

import com.mycompany.quartercaster.model.deliveries.Shipment;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Service;

/**
 *
 * @author tommib
 */
@Service
public class ForecastGenerator {

    private int forecastSpan;

    public ForecastGenerator() {
        this.forecastSpan = 0;
    }

    public int getForecastSpan() {
        return forecastSpan;
    }

    public void setForecastSpan(int forecastSpan) {
        this.forecastSpan = forecastSpan;
    }

    public XYChart.Series populateChart(ArrayList<String> weeks, Shipment shipment) {
        XYChart.Series series = new XYChart.Series();
        System.out.println(shipment.getProductName());
        series.setName(shipment.getProductName());

        for(int i = 0; i < this.forecastSpan; i++){
            weeks.add(Integer.toString(weeks.size() + i));
        }
        System.out.println("WEEKS SIZE: " + weeks.size());
        for (int i = 0; i < weeks.size() + this.forecastSpan; i++) {
            System.out.println(i);
            if (shipment.getDelivery().containsKey(weeks.get(i))) {
                series.getData().add(new XYChart.Data(weeks.get(i), shipment.getDelivery().get(weeks.get(i))));
            } else {
                series.getData().add(new XYChart.Data(weeks .get(i), 0));
            }
        }
        return series;
    }
}
