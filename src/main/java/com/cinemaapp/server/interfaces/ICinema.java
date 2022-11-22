package com.cinemaapp.server.interfaces;


import com.cinemaapp.model.CinemaShowTimeModel;
import java.util.ArrayList;

public interface ICinema {
    public ArrayList<String> getPlayingMoviesId();
    public String getName(String id);
    public String getImageUrl(String id);
    public String getTrailerUrl(String id);
    public String getDescription(String id);
    public String getDirection(String id);
    public String getActors(String id);
    public String getCategory(String id);
    public String getDuration(String id);
    public ArrayList<CinemaShowTimeModel> getShowTime(String id);
}
