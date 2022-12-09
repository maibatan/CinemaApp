package com.cinemaapp.server.main;

import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.Message;
import com.cinemaapp.model.MovieDetailModel;
import com.cinemaapp.model.MovieModel;
import com.cinemaapp.server.interfaces.ICinema;
import com.cinemaapp.utils.RSAUtil;
import com.cinemaapp.utils.TripleDES;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author New
 */
public class ServerService implements Runnable{
    
    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private boolean running;
    private TripleDES security = null;
    
    public ServerService(Socket socket){
        try {
            this.socket = socket;
            this.running = true;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            configSercurity();
        } catch (IOException e) { 
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e); 
        }     
    }
    private void configSercurity(){
        try {  
            String publickey = Base64.getEncoder().encodeToString(Server.keyPair.getPublicKey().getEncoded());
            out.write(publickey);
            out.newLine();
            out.flush();
            String key = RSAUtil.Decrypt(in.readLine(),Server.keyPair.getPrivateKey());
            security = new TripleDES(key);
        } catch (IOException e) {
           Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
   
    @Override
    public void run() {
        while (running) {     
                String msg = receive();
                JSONObject jsonobject = new JSONObject(msg);
                if(jsonobject.getBoolean("empty")){                   
                    if(jsonobject.getString("msg").equals("close")){ 
                        close();
                        break;
                    }
                    if(jsonobject.getString("msg").equals("playing movies")){
                       sendPlayingMovies();
                    }
                    continue;
                }
                Gson gson = new Gson();
                String jsonData = jsonobject.getJSONObject("data").toString();
                if(jsonobject.getString("msg").equals("detail movie")){
                   MovieModel movie = gson.fromJson(jsonData, MovieModel.class);
                   sendDetail(movie);
                }    
                if(jsonobject.getString("msg").equals("cinema show time")){
                   MovieModel movie = gson.fromJson(jsonData, MovieModel.class);
                   sendShowTime(movie);
                }    
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

    private void send(Message data){
        try {
            Gson gson = new Gson();
            String msg = security.Encrypt(gson.toJson(data));
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
        for (int i =0; i < Server.cinemas.size(); i++) {
            ICinema cinema = Server.cinemas.get(i);
            ArrayList<String> ids = cinema.getPlayingMoviesId();
            for (String id : ids) {
                String name = cinema.getName(id);
                String imgUrl = cinema.getImageUrl(id);
                MovieModel movieModel = new MovieModel(name);
                movieModel.addId(cinema.getClass().getName(), id);
                movieModel.addImageUrl(cinema.getClass().getName(), imgUrl);
                send(new Message("playing movies",movieModel));
            }
        }
        send(new Message("playing movies"));
    }
    private void sendDetail(MovieModel movie){
        MovieDetailModel detailModel = new MovieDetailModel();
        for (ICinema cinema : Server.cinemas) {
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
        send(new Message("detail movie",detailModel));
    }
    private void sendShowTime(MovieModel movie) {
        for (ICinema cinema : Server.cinemas) {
            String id = movie.getId(cinema.getClass().getName());
            if(id.equals("")) break;
            ArrayList<CinemaShowTimeModel> cinemaShowTimes = cinema.getShowTime(id);
            for (CinemaShowTimeModel cinemaShowTime : cinemaShowTimes) {
                send(new Message("cinema show time",cinemaShowTime));
            }
        }
        send(new Message("cinema show time"));
    }
    
    public boolean isRunning() {
        return running;
    }
    
}
