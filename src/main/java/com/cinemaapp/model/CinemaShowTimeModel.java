package com.cinemaapp.model;

import java.util.ArrayList;

public class CinemaShowTimeModel{
        private String cinemaName;
        private ArrayList<ShowTimeModel> showTimes; 

        public CinemaShowTimeModel(String cinemaName) {
            this.cinemaName = cinemaName;
            this.showTimes = new ArrayList<>();
        }
        
        public String getCinemaName() {
            return cinemaName;
        }
        
        public ArrayList<ShowTimeModel> getShowTimes() {
            return showTimes;
        }
        
    }
