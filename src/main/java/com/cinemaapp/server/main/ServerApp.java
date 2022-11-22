package com.cinemaapp.server.main;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ServerApp {

    public static void main(String[] args){
        uploadIPAddress();
        Server server = new Server(5000);
        server.run();
    }
    private static void uploadIPAddress(){
        try {
            Socket socket = new Socket("sgu.edu.vn",443);
            String localIP = socket.getLocalAddress().toString().substring(1);
            String api = "https://api-generator.retool.com/uLEVQv/data/1";
            String jsonData = "{\"ip\":\""+localIP+"\"}";
            Jsoup.connect(api)
                    .ignoreContentType(true).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .requestBody(jsonData)
                    .method(Connection.Method.PUT).execute();
        } catch (IOException e) {
            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
