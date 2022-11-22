package com.cinemaapp.model;

import java.util.HashMap;
import java.util.Set;

public class MovieModel {
    private HashMap<String,String> cinemaIds;
    private HashMap<String,String> imgUrls;
    private String name;

    public String getName() {
        return name;
    }
    public MovieModel(String name) {
        this.name = name;
        this.cinemaIds = new HashMap<>();
        this.imgUrls = new HashMap<>();
    }

    public void addImageUrl(String cinemaName, String url){
        imgUrls.put(cinemaName, url);
    }
    public String getImageUrl(String cinemaName){
        return imgUrls.getOrDefault(cinemaName, "");
    }
    public void addId(String cinemaName, String id){
        cinemaIds.put(cinemaName, id);
    }
    public String getId(String cinamaName){
        return cinemaIds.getOrDefault(cinamaName,"");
    }
    public Set<String> getCinemaHasFilm(){
        return cinemaIds.keySet();
    }
   
}

