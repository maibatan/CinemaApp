package com.cinemaapp.server.main;

import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.ShowTimeModel;
import com.cinemaapp.server.interfaces.ICinema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GalaxyCinema implements ICinema{
    private HashMap<String,JSONObject> jsonObjects;
    private HashMap<String,String> movies;
    private HashMap<String,String> posters;
    private HashMap<String,String> trailers;
    private HashMap<String,String> descriptions;
    private HashMap<String,String> durations;
    public GalaxyCinema() {
        jsonObjects = new HashMap<>();
        movies = new HashMap<>();
        posters = new HashMap<>();
        trailers = new HashMap<>();
        descriptions = new HashMap<>();
        durations = new HashMap<>();
    }
    
    @Override
    public ArrayList<String> getPlayingMoviesId() {
        ArrayList<String> ids = new ArrayList<>();
        try {
            
            Connection.Response con = Jsoup.connect("https://www.galaxycine.vn/api/movie/showAndComming")
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = con.parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            JSONArray jsonArray = jsonObject.getJSONArray("movieShowing");
            for (int i = 0; i < jsonArray.length(); i++) {
               String id = jsonArray.getJSONObject(i).getString("id");
               String name = jsonArray.getJSONObject(i).getString("name");
               String imageUrl ="https://cdn.galaxycine.vn"+ jsonArray.getJSONObject(i).getString("imagePortrait");
               String trailerUrl = jsonArray.getJSONObject(i).getString("trailer");
               String duration = jsonArray.getJSONObject(i).getString("duration");
               String description = Jsoup.parse(jsonArray.getJSONObject(i).getString("description")).text();
               ids.add(id);
               movies.put(id, name);
               posters.put(id, imageUrl);
               trailers.put(id, trailerUrl);
               durations.put(id, duration);
               descriptions.put(id, description);
            }
            
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    @Override
    public String getName(String id) {
        if(movies.containsKey(id)){
            return movies.get(id);
        }
        return "";
    }

    @Override
    public String getImageUrl(String id) {
        if(posters.containsKey(id)){
            return posters.get(id);
        }
        return "";
    }

    @Override
    public String getTrailerUrl(String id) {
        if(trailers.containsKey(id)){
            return trailers.get(id);
        }
        return "";
    }

    @Override
    public String getDescription(String id) {
        if(descriptions.containsKey(id)){
            return descriptions.get(id);
        }
        return "";
    }

    @Override
    public String getDirection(String id) {
        return "";
    }

    @Override
    public String getActors(String id) {
        return "";
    }

    @Override
    public String getCategory(String id) {
        return "";
    }

    @Override
    public String getDuration(String id) {
        if(durations.containsKey(id)){
            durations.get(id);
        }
        return "";
    }

    @Override
    public ArrayList<CinemaShowTimeModel> getShowTime(String id) {
        ArrayList<CinemaShowTimeModel> result = new ArrayList<>();
        try {
            
            Connection.Response con = Jsoup.connect("https://www.galaxycine.vn/api/session/movie/"+id)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = con.parse();
            JSONArray cinemas = new JSONArray(doc.text());
            for (int i = 0; i < cinemas.length(); i++) {
                String name = cinemas.getJSONObject(i).getString("name");
                String address = cinemas.getJSONObject(i).getString("address");
                if(address.toUpperCase().contains("TPHCM")|| address.toUpperCase().contains("TP.HCM")){
                    CinemaShowTimeModel cstm = new CinemaShowTimeModel(name);
                    JSONArray dates = cinemas.getJSONObject(i).getJSONArray("dates");
                    for (int j = 0; j < dates.length(); j++) {
                        String date = dates.getJSONObject(j).getString("showDate");
                        StringTokenizer tokenizer = new StringTokenizer(date, "/");
                        ShowTimeModel stm = new ShowTimeModel(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
                        JSONArray bundles = dates.getJSONObject(j).getJSONArray("bundles");
                        for (int k = 0; k < bundles.length(); k++) {
                            JSONArray times = bundles.getJSONObject(k).getJSONArray("sessions");
                            for (int l = 0; l < times.length(); l++) {
                                String time = times.getJSONObject(i).getString("showTime");
                                stm.getTimes().add(time);
                            }
                        }
                        cstm.getShowTimes().add(stm);
                    }
                }
            }
            
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
}
