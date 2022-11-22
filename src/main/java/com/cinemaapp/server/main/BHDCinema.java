package com.cinemaapp.server.main;

import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.MovieDetailModel;
import com.cinemaapp.model.MovieModel;
import com.cinemaapp.model.ShowTimeModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.StringTokenizer;
import com.cinemaapp.server.interfaces.ICinema;
import java.util.HashMap;

public class BHDCinema implements ICinema{

    private HashMap<String,Document> docs;
    private HashMap<String,String> movies;
    private HashMap<String,String> posters;
    public BHDCinema() {
        docs = new HashMap<>();
        movies = new HashMap<>();
        posters = new HashMap<>();
    }
    
    @Override
    public ArrayList<String> getPlayingMoviesId(){
        ArrayList<String> result = new ArrayList<>();
        try {
            String url = "https://www.bhdstar.vn";
            Connection.Response con = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = con.parse();
            Elements elements = doc.select("div.container-search-header.container-search-header-now ul li");
            for (Element element : elements) {
                String id = element.select("a").attr("data-id");
                String name = element.text();
                String imageUrl = "http:"+element.select("a img").attr("src");
                result.add(id);
                movies.put(id, name);
                posters.put(id, imageUrl);
            }
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return  result;
    }
    @Override
    public String getName(String id){
        if(movies.containsKey(id)){
            return movies.get(id);
        }
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.select("div.product--name").text();
    }
    @Override
    public String getImageUrl(String id)
    {
        if(posters.containsKey(id)){
            return posters.get(id);
        }
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.select("img.movie-full").attr("src");
    }
    @Override
    public String getTrailerUrl(String id){
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.select("a.btn--green bhd-trailer").attr("href"); 
    }
        @Override
    public String getDescription(String id) {
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.select("div.film--detail").text();
    }

    @Override
    public String getDirection(String id) {
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.getElementsByClass("col-right")
                    .get(1).text();
    }

    @Override
    public String getActors(String id) {
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.getElementsByClass("col-right")
                    .get(2).text();
    }

    @Override
    public String getCategory(String id) {
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.getElementsByClass("col-right")
                    .get(3).text();
    }

    @Override
    public String getDuration(String id) {
        if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return "";
            }
        }
        Document doc = docs.get(id);
        return doc.getElementsByClass("col-right")
                    .get(5).text();
    }

    @Override
    public ArrayList<CinemaShowTimeModel> getShowTime(String id) {
         if(!docs.containsKey(id)){
            if(!getDocument(id)){
                return new ArrayList<>();
            }
        }
        ArrayList<CinemaShowTimeModel> list = new ArrayList<>();
        Document doc = docs.get(id);
        for (Element element : doc.select("ul.list--showtimes-cinema li")) {
            String address = element.select("div.inside p").text();
            if(address.contains("TPHCM")|| address.contains("Tp.HCM") || address.contains("TP.HCM")){
                String cinemaName = element.select("div.inside h4").text();
                CinemaShowTimeModel cinemaShowTime = new CinemaShowTimeModel(cinemaName);
                list.add(cinemaShowTime);
                for (Element dateShowTime : element.select("ul.list--film-type")) {
                    String date = dateShowTime.select("li.item--film-type ul").attr("class").replaceAll("[a-z]|_|-", " ").trim();
                    StringTokenizer tokenizer = new StringTokenizer(date," ");
                    String year = tokenizer.nextToken();
                    String month = tokenizer.nextToken();
                    String day = tokenizer.nextToken();
                    ShowTimeModel showTime = new ShowTimeModel(day, month, year);
                    cinemaShowTime.getShowTimes().add(showTime);
                    for (Element time : dateShowTime.select("li.item--film-type ul li")) {
                        showTime.getTimes().add(time.text());
                    }                   
                }
            }
        }
        return list;
    }
    private boolean getDocument(String id){
        try {
            String url = "https://www.bhdstar.vn/movie/"+id;
            Connection.Response con = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = con.parse();
            docs.put(id, doc);
            return true;
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

 
}
