package com.cinemaapp.server.main;

import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.ShowTimeModel;
import com.cinemaapp.server.interfaces.ICinema;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class LotteCinema implements ICinema{
    
    private HashMap<String,JSONObject> jsonObjects;
    private HashMap<String,String> movies;
    private HashMap<String,String> posters;
    private LinkedHashMap<String,String> cinemas;
    private ArrayList<ShowTimeModel> playDate;
    public LotteCinema() {
        jsonObjects = new HashMap<>();
        movies = new HashMap<>();
        posters = new HashMap<>();
        cinemas = new LinkedHashMap<>();
        playDate = new ArrayList<>();
        loadCinemaAndPlayDate();
    }
    private boolean getJSONObject(String id){
        try {
            String rawData = "paramList={\"MethodName\":\"GetMovieDetail\",\"channelType\":\"HO\",\"osType\":\"Chrome\",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36\""
                    + ",\"multiLanguageID\":\"LL\",\"representationMovieCode\":\""+id+"\"}";
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCWS/Movie/MovieData.aspx")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody(rawData)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = con.parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            jsonObjects.put(id, jsonObject);
            return true;
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    private CinemaShowTimeModel loadCinemaShowTime(String movieId,String cinemaId){
        CinemaShowTimeModel cinemaShowTimeModel = new CinemaShowTimeModel(cinemas.get(cinemaId));
        for (ShowTimeModel date : playDate) {
            ShowTimeModel showTime = loadShowTime(movieId, cinemaId, date);
            if(showTime.getTimes().isEmpty()) continue;
            cinemaShowTimeModel.getShowTimes().add(showTime);
        }
        return cinemaShowTimeModel;
    }
    private ShowTimeModel loadShowTime(String movieId,String cinemaId,ShowTimeModel date){
        ShowTimeModel result = new ShowTimeModel(date);
        try {
            String rawData = "paramList={\"MethodName\":\"GetPlaySequence\",\"channelType\":\"HO\",\"osType\":\"Chrome\",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36\""
                    + ",\"playDate\":\""+date.getYear()+date.getMonth()+date.getDay()+"\""
                    + ",\"cinemaID\":\"1|0001|"+cinemaId+"\",\"representationMovieCode\":\""+movieId+"\"}";
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCWS/Ticketing/TicketingData.aspx")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody(rawData)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = con.parse();
            JSONObject jSONObject = new JSONObject(doc.text());
            JSONArray jsonArray = jSONObject.getJSONObject("PlaySeqs").getJSONArray("Items");
            for (int i = 0; i < jsonArray.length(); i++) {
                String time = jsonArray.getJSONObject(i).getString("StartTime");
                result.getTimes().add(time);
            }
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    private void loadCinemaAndPlayDate(){
        try {
            String rawData = "paramList={\"MethodName\":\"GetTicketingPage\",\"channelType\":\"HO\",\"osType\":\"Chrome\""
                + ",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36\",\"memberOnNo\":\"\"}";
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCWS/Ticketing/TicketingData.aspx")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody(rawData)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = con.parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            JSONArray jsonCinemaArray = jsonObject.getJSONObject("Cinemas").getJSONObject("Cinemas").getJSONArray("Items");
            for (int i = 0; i < jsonCinemaArray.length(); i++) {
               String detailDivisionCode = jsonCinemaArray.getJSONObject(i).getString("DetailDivisionCode");
               String cinemaID = String.valueOf(jsonCinemaArray.getJSONObject(i).getInt("CinemaID"));
               String cinemaName ="Lotte "+ jsonCinemaArray.getJSONObject(i).getString("CinemaName");
               if(detailDivisionCode.equals("0001")){
                   cinemas.put(cinemaID, cinemaName);
               }
            }
            JSONArray jsonDateArray = jsonObject.getJSONObject("MoviePlayDates").getJSONObject("Items").getJSONArray("Items");
            for (int i = 0; i < jsonDateArray.length(); i++) {
                if(i==4) break;
                String day = String.valueOf(jsonDateArray.getJSONObject(i).getInt("Day"));
                String month = String.valueOf(jsonDateArray.getJSONObject(i).getInt("Month"));
                String year = String.valueOf(jsonDateArray.getJSONObject(i).getInt("Year"));
                playDate.add(new ShowTimeModel(day,month,year));
            }
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @Override
    public ArrayList<String> getPlayingMoviesId() {
        ArrayList<String> ids = new ArrayList<>();
        try {
            String rawData = "paramList={\"MethodName\":\"GetMainMovies\",\"channelType\":\"HO\",\"osType\":\"Chrome\""
                    + ",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36\",\"multiLanguageID\":\"LL\"}";
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCWS/Movie/MovieData.aspx")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody(rawData)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = con.parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            JSONArray jsonArray = jsonObject.getJSONObject("Movies").getJSONArray("Items");
            for (int i = 0; i < jsonArray.length(); i++) {
               String id = String.valueOf(jsonArray.getJSONObject(i).getInt("RepresentationMovieCode"));
               String name = jsonArray.getJSONObject(i).getString("MovieName");
               String imageUrl = jsonArray.getJSONObject(i).getString("PosterURL");
               ids.add(id);
               movies.put(id, name);
               posters.put(id, imageUrl);
            }
            
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    @Override
    public String getName(String id){
        if(movies.containsKey(id)){
            return movies.get(id);
        }
        return "";
    }
    @Override
    public String getImageUrl(String id)
    {
        if(posters.containsKey(id)){
            return posters.get(id);
        }
        return "";
    }
    @Override
    public String getTrailerUrl(String id){
        if(!jsonObjects.containsKey(id)){
            if(!getJSONObject(id)){
                return "";
            }
        }
        JSONObject json = jsonObjects.get(id);
        return json.getJSONObject("Trailer").getJSONArray("Items").getJSONObject(0).getString("MediaURL"); 
    }
        @Override
    public String getDescription(String id) {
        if(!jsonObjects.containsKey(id)){
            if(!getJSONObject(id)){
                return "";
            }
        }
        JSONObject json = jsonObjects.get(id);
        return json.getJSONObject("Movie").getString("Synopsis");
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
        if(!jsonObjects.containsKey(id)){
            if(!getJSONObject(id)){
                return "";
            }
        }
        JSONObject json = jsonObjects.get(id);
        return json.getJSONObject("Movie").getString("MovieGenreName");
    }

    @Override
    public String getDuration(String id) {
        if(!jsonObjects.containsKey(id)){
            if(!getJSONObject(id)){
                return "";
            }
        }
        JSONObject json = jsonObjects.get(id);
        return String.valueOf(json.getJSONObject("Movie").getInt("PlayTime"));
    }

    @Override
    public ArrayList<CinemaShowTimeModel> getShowTime(String id) {
        ArrayList<CinemaShowTimeModel> result = new ArrayList<>();
        for (String cinemaId : cinemas.keySet()) {
            CinemaShowTimeModel cinemaShowTimeModel= loadCinemaShowTime(id, cinemaId);
            if(cinemaShowTimeModel.getShowTimes().isEmpty()) continue;
            result.add(cinemaShowTimeModel);
        }
        return result;
    }

    
}
