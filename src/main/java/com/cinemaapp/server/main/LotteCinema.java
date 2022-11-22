package com.cinemaapp.server.main;

import com.cinemaapp.model.MovieModel;
import com.cinemaapp.server.interfaces.ICinema;
import com.cinemaapp.utils.ImageUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LotteCinema {

    public ArrayList<MovieModel> getPlayingMovies() {
        
        return null;
    }
    public MovieModel getMovieDetail(String id){
        MovieModel result = null;
        ImageUtil imageUtil = new ImageUtil();
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
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    public void getCinemaShowTime(){
                try {
            String rawData = "paramList={\"MethodName\":\"GetPlaySequence\",\"channelType\":\"HO\",\"osType\":\"Chrome\",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36\""
                    + ",\"playDate\":\"20221119\",\"cinemaID\":\"1|0001|8027\",\"representationMovieCode\":\"10945\"}";
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCWS/Ticketing/TicketingData.aspx")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody(rawData)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = con.parse();
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void getCinemaID(){
           try {
            Connection.Response con = Jsoup.connect("https://www.lottecinemavn.com/LCHS/Contents/Cinema/Cinema-Detail.aspx")
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = con.parse();
               for (Element element : doc.select("div.depth_03 ul li a")) {
                  if(element.attr("href").contains("detailDivisionCode=1")){
                      System.out.println(element.text());
                  }                 
               }
        } catch (IOException e) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    
}
