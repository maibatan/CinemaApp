package com.cinemaapp.server.main;

import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.MovieDetailModel;
import com.cinemaapp.model.MovieModel;
import com.cinemaapp.server.interfaces.ICinema;
import com.cinemaapp.utils.RSAUtil;
import com.cinemaapp.utils.Security;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author New
 */
public class ServerService implements Runnable{
    
    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private boolean running;
    private Security security = null;
    private ArrayList<ICinema> cinemas = new ArrayList<>();
    
    public ServerService(Socket socket){
        try {
            this.socket = socket;
            this.running = true;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            configSercurity();
            cinemas.add(new BHDCinema());
            cinemas.add(new LotteCinema());
        } catch (IOException e) { 
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e); 
        }     
    }

    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void run() {
        try {
            while (running) {     
                String data = receive();
                if(data.equals("close")){
                    close();
                    break;
                }
                if(data.equals("playingMovies")){
                   send("start");
                   sendPlayingMovies();
                   send("end");
                }
                if(data.equals("detailMovie")){
                   String json = receive();
                   Gson gson = new Gson();
                   MovieModel movie = gson.fromJson(json, MovieModel.class);
                   send("startGetDetail");
                   sendDetail(movie);
                   send("endGetDetail");
                }    
                if(data.equals("cinemaShowTime")){
                   String json = receive();
                   Gson gson = new Gson();
                   MovieModel movie = gson.fromJson(json, MovieModel.class);
                   send("startGetTime");
                   sendShowTime(movie);
                   send("endGetTime");
                }    
            }
        } catch (Exception e) { 
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e); 
        }
    }
    public void close(){
        try {
            running = false;
            in.close();
            out.close();
            System.out.println("Client "+socket.getInetAddress().getHostAddress()+" closed connection...");
            socket.close();
            
        } catch (IOException e) { System.err.println(e); }
    }
    private void configSercurity(){
        try {  
            String publickey = Base64.getEncoder().encodeToString(Server.keyPair.getPublicKey().getEncoded());
            out.write(publickey);
            out.newLine();
            out.flush();
            String key = RSAUtil.Decrypt(in.readLine(),Server.keyPair.getPrivateKey());
            security = new Security(key);
        } catch (IOException e) {
           Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void send(String data){
        try {
            String msg = security.Encrypt(data);
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }  
    }
    private String receive() {
        try {
            String msg = in.readLine();
            String data = security.Decrypt(msg);
            return data;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
    private void sendPlayingMovies(){
        
        for (int i =0; i < cinemas.size(); i++) {
            ICinema cinema = cinemas.get(i);
            ArrayList<String> ids = cinema.getPlayingMoviesId();
            for (String id : ids) {
                String name = cinema.getName(id);
                String imgUrl = cinema.getImageUrl(id);
                MovieModel movieModel = new MovieModel(name);
                movieModel.addId(cinema.getClass().getName(), id);
                movieModel.addImageUrl(cinema.getClass().getName(), imgUrl);
                Gson gson = new Gson();
                String json = gson.toJson(movieModel);
                send(json);
            }
        }
    }
    private void sendDetail(MovieModel movie){
        MovieDetailModel detailModel = new MovieDetailModel();
        for (ICinema cinema : cinemas) {
            String id = movie.getId(cinema.getClass().getName());
            if(id.equals("")) continue;
            String description = cinema.getDescription(id);
            String direction = cinema.getDirection(id);
            String actors = cinema.getActors(id);
            String categogy = cinema.getCategory(id);
            String duration = cinema.getDuration(id);
            if(detailModel.getDescription().toCharArray().length < description.toCharArray().length){
                detailModel.setDescription(description);
            }
            if(detailModel.getDirection().toCharArray().length < direction.toCharArray().length){
                detailModel.setDirection(direction);
            }
            if(detailModel.getActors().toCharArray().length < actors.toCharArray().length){
                detailModel.setActors(actors);
            }
            if(detailModel.getCategory().equals("")){
                detailModel.setCategory(categogy);
            }
            if(detailModel.getDuration().equals("")){
                detailModel.setDuration(duration);
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(detailModel);
        send(json);
    }
    private void sendShowTime(MovieModel movie) {
        for (ICinema cinema : cinemas) {
            String id = movie.getId(cinema.getClass().getName());
            ArrayList<CinemaShowTimeModel> cinemaShowTimes = cinema.getShowTime(id);
            for (CinemaShowTimeModel cinemaShowTime : cinemaShowTimes) {
                Gson gson = new Gson();
                String json = gson.toJson(cinemaShowTime);
                send(json);
            }
        }    
    }
}
