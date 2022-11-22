package com.cinemaapp.model;

import java.util.ArrayList;

 public class ShowTimeModel {
    private String day;
    private String month;
    private String year;
    private ArrayList<String> times;

    public ShowTimeModel(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
         this.times = new ArrayList<>();
    }
    
    public ArrayList<String> getTimes() {
        return times;
    }   

    @Override
    public String toString() {
        return day+"/"+month+"/"+year;
    }
    
}
