package com.cinemaapp.client.main;

import com.cinemaapp.client.form.FormMain;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ClientApp {

    public static Client client = null;
    public static void main(String args[]) {
        connectServer();
        java.awt.EventQueue.invokeLater(() -> {
            FormMain main = new FormMain();
            main.setVisible(true);
            main.loadMovies();
        });
    }
    private static void connectServer(){
        try {
            String api = "https://api-generator.retool.com/uLEVQv/data/1";
            Document doc = Jsoup.connect(api)
                    .ignoreContentType(true).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.GET).execute().parse();
            JSONObject jSONObject = new JSONObject(doc.text());
            String ipServer = jSONObject.getString("ip");
            client = new Client(ipServer, 5000);
        } catch (IOException e) {
            Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
