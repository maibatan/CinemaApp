package com.cinemaapp.client.form;

import com.cinemaapp.client.main.ClientApp;
import com.cinemaapp.model.CinemaShowTimeModel;
import com.cinemaapp.model.Message;
import com.cinemaapp.model.MovieModel;
import com.google.gson.Gson;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import org.json.JSONObject;

public class FormShowTime extends javax.swing.JFrame {

    private FormScrollCinemaTime list;

    public FormShowTime() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        init();
    }

    private void init() {
        list = new FormScrollCinemaTime();
        winButton.addCloseEvent((ActionEvent ae) -> {
            dispose();
        });
        winButton.addMinimizeEvent((ActionEvent ae) -> {
                setState(JFrame.ICONIFIED);
        });
        winButton.addResizeEvent((ActionEvent ae) -> {
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    background.setRound(20);
                    setExtendedState(JFrame.NORMAL);
                } else {
                    background.setRound(0);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
        });
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(list);
       
    }
    public void loadShowTime(MovieModel movie){ 
        Thread thread;
        thread = new Thread(() -> {
            System.out.println("Loading....");
            ClientApp.client.send(new Message("cinema show time",movie));
            while (true) {                
                String msg = ClientApp.client.receive();
                JSONObject jsonobject = new JSONObject(msg);
                if(!jsonobject.getString("msg").equals("cinema show time")) continue;
                if(jsonobject.getBoolean("empty")) break;
                Gson gson = new Gson();
                CinemaShowTimeModel cinemaShowTime = gson.fromJson(jsonobject.getJSONObject("data").toString(), CinemaShowTimeModel.class);
                list.addItem(cinemaShowTime);
            }
            System.out.println("Done....");
        });
        thread.start();
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new com.cinemaapp.client.swing.Background();
        header = new javax.swing.JPanel();
        winButton = new com.cinemaapp.client.swing.win_button.WinButton();
        mainPanel = new com.cinemaapp.client.swing.MainPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        background.setPreferredSize(new java.awt.Dimension(1080, 700));

        header.setOpaque(false);
        header.setPreferredSize(new java.awt.Dimension(1080, 100));
        header.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(1028, Short.MAX_VALUE)
                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        mainPanel.setPreferredSize(new java.awt.Dimension(1080, 600));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1088, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 606, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 1088, Short.MAX_VALUE)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1088, Short.MAX_VALUE))
                .addContainerGap())
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.cinemaapp.client.swing.Background background;
    private javax.swing.JPanel header;
    private com.cinemaapp.client.swing.MainPanel mainPanel;
    private com.cinemaapp.client.swing.win_button.WinButton winButton;
    // End of variables declaration//GEN-END:variables
}
