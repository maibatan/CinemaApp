
package com.cinemaapp.model;

import java.util.HashMap;

public class MovieDetailModel {
    
    private HashMap<String,String> trailerUrls;
    private String description;
    private String direction;
    private String actors;
    private String category;
    private String duration;
    
    public MovieDetailModel() {
        this.trailerUrls = new HashMap<>();
        this.description = "";
        this.direction = "";
        this.actors = "";
        this.category = "";
        this.duration = "";
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void addTrailerUrl(String cinemaName, String url){
        trailerUrls.put(cinemaName, url);
    }
    public String getTrailerUrl(String cinemaName){
        return trailerUrls.getOrDefault(cinemaName, "");
    }
}
