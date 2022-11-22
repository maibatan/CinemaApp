package com.cinemaapp.client.form;

import com.cinemaapp.client.main.ClientApp;
import com.cinemaapp.client.swing.ScrollBar;
import com.cinemaapp.model.MovieDetailModel;
import com.cinemaapp.model.MovieModel;
import com.cinemaapp.utils.ImageUtil;
import com.google.gson.Gson;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFrame;

public class FormDetail extends javax.swing.JFrame {

    private MovieModel movie;
    private MovieDetailModel detail;
    private FormScrollTime cinenaShowtime;
    
    public FormDetail() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        init();
    }
    private void init() {
        scroll.setVerticalScrollBar(new ScrollBar());
        cinenaShowtime = new FormScrollTime();
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
        mainPanel.add(cinenaShowtime);
    }
    private void setData(MovieModel movie) {
        this.movie = movie;
        lbItemName.setText(movie.getName());
        ImageUtil util = new ImageUtil();
        for (String cinema : movie.getCinemaHasFilm()) {
            if(util.download(movie.getImageUrl(cinema), movie.getId(cinema)))
            {
                Icon icon = util.load(movie.getId(cinema));
                picture.setImage(icon);
                break;
            }
        }    
    }
    private void setData(MovieDetailModel detail){
        this.detail = detail;
        txtDescription.setText(detail.getDescription());
        txtDirection.setText(detail.getDirection());
        txtActors.setText(detail.getActors());
        txtCategory.setText(detail.getCategory());
        txtDuration.setText(detail.getDuration());
    }
    public void loadDetail(MovieModel movie){
        setData(movie);
        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("loading....");
                ClientApp.client.send("detailMovie");
                Gson gson = new Gson();
                String json = gson.toJson(movie);
                ClientApp.client.send(json);
                while (true) {
                    String start = ClientApp.client.receive();
                    if(start.equals("startGetDetail")){
                        while (true) {
                            String msg = ClientApp.client.receive();
                            if(msg.equals("endGetDetail")){
                                break;
                            }
                            MovieDetailModel model = gson.fromJson(msg, MovieDetailModel.class);
                            setData(model);
                        }
                        break;
                    }
                }
                System.out.println("Done....");
            }
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
        lbItemName = new javax.swing.JLabel();
        picture = new com.cinemaapp.client.swing.PictureBox();
        showTimeBtn = new javax.swing.JButton();
        trailerBtn = new javax.swing.JButton();
        lbDirection = new javax.swing.JLabel();
        lbActor = new javax.swing.JLabel();
        lbCategory = new javax.swing.JLabel();
        lbDuration = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();
        txtDirection = new javax.swing.JLabel();
        txtActors = new javax.swing.JLabel();
        txtCategory = new javax.swing.JLabel();
        txtDuration = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        background.setPreferredSize(new java.awt.Dimension(1080, 750));

        header.setOpaque(false);
        header.setPreferredSize(new java.awt.Dimension(1080, 100));
        header.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        mainPanel.setPreferredSize(new java.awt.Dimension(1080, 650));

        lbItemName.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        lbItemName.setForeground(new java.awt.Color(76, 76, 76));
        lbItemName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbItemName.setText("Tên Phim");
        lbItemName.setPreferredSize(new java.awt.Dimension(500, 25));

        showTimeBtn.setBackground(new java.awt.Color(102, 255, 102));
        showTimeBtn.setLabel("Xem lịch chiếu");
        showTimeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTimeBtnActionPerformed(evt);
            }
        });

        trailerBtn.setBackground(new java.awt.Color(255, 51, 51));
        trailerBtn.setActionCommand("Trailer");
        trailerBtn.setLabel("Trailer");
        trailerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trailerBtnActionPerformed(evt);
            }
        });

        lbDirection.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lbDirection.setForeground(new java.awt.Color(76, 76, 76));
        lbDirection.setText("Đạo diễn:");
        lbDirection.setPreferredSize(new java.awt.Dimension(100, 25));

        lbActor.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lbActor.setForeground(new java.awt.Color(76, 76, 76));
        lbActor.setText("Diễn viên:");
        lbActor.setPreferredSize(new java.awt.Dimension(100, 25));

        lbCategory.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lbCategory.setForeground(new java.awt.Color(76, 76, 76));
        lbCategory.setText("Thể loại:");
        lbCategory.setPreferredSize(new java.awt.Dimension(100, 25));

        lbDuration.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lbDuration.setForeground(new java.awt.Color(76, 76, 76));
        lbDuration.setText("Thời lượng");
        lbDuration.setPreferredSize(new java.awt.Dimension(100, 25));

        lbDescription.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lbDescription.setForeground(new java.awt.Color(76, 76, 76));
        lbDescription.setText("Giới thiệu:");
        lbDescription.setPreferredSize(new java.awt.Dimension(100, 25));

        txtDirection.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txtDirection.setForeground(new java.awt.Color(76, 76, 76));
        txtDirection.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtDirection.setText("Loading...");
        txtDirection.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        txtDirection.setPreferredSize(new java.awt.Dimension(100, 25));

        txtActors.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txtActors.setForeground(new java.awt.Color(76, 76, 76));
        txtActors.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtActors.setText("Loading...");
        txtActors.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        txtActors.setPreferredSize(new java.awt.Dimension(100, 25));

        txtCategory.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txtCategory.setForeground(new java.awt.Color(76, 76, 76));
        txtCategory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtCategory.setText("Loading...");
        txtCategory.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        txtCategory.setPreferredSize(new java.awt.Dimension(100, 25));

        txtDuration.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txtDuration.setForeground(new java.awt.Color(76, 76, 76));
        txtDuration.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtDuration.setText("Loading...");
        txtDuration.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        txtDuration.setPreferredSize(new java.awt.Dimension(100, 25));

        scroll.setBackground(new java.awt.Color(255, 255, 255));
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtDescription.setEditable(false);
        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtDescription.setForeground(new java.awt.Color(76, 76, 76));
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        txtDescription.setText("Loading...\n");
        txtDescription.setBorder(null);
        txtDescription.setCaretColor(new java.awt.Color(255, 255, 255));
        txtDescription.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtDescription.setDoubleBuffered(true);
        txtDescription.setSelectionColor(new java.awt.Color(255, 255, 255));
        txtDescription.setVerifyInputWhenFocusTarget(false);
        scroll.setViewportView(txtDescription);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(trailerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151)
                .addComponent(showTimeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(picture, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(lbDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDirection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtActors, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCategory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDirection, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbActor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCategory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDescription, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(scroll)))
                .addGap(77, 77, 77))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(lbItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbDirection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDirection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbActor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(picture, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trailerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showTimeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81))
        );

        trailerBtn.getAccessibleContext().setAccessibleName("trailerBtn");

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
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void trailerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trailerBtnActionPerformed

        try {
            for (String cinema : movie.getCinemaHasFilm()) {
                String trailerUrl = detail.getTrailerUrl(cinema);
                if(trailerUrl.equals("")) continue;
                Desktop.getDesktop().browse(new URL(trailerUrl).toURI());
            }
        } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(FormDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_trailerBtnActionPerformed

    private void showTimeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTimeBtnActionPerformed
        FormShowTime form = new FormShowTime();
        form.loadShowTime(movie);
        form.setVisible(true);
    }//GEN-LAST:event_showTimeBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.cinemaapp.client.swing.Background background;
    private javax.swing.JPanel header;
    private javax.swing.JLabel lbActor;
    private javax.swing.JLabel lbCategory;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbDirection;
    private javax.swing.JLabel lbDuration;
    private javax.swing.JLabel lbItemName;
    private com.cinemaapp.client.swing.MainPanel mainPanel;
    private com.cinemaapp.client.swing.PictureBox picture;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JButton showTimeBtn;
    private javax.swing.JButton trailerBtn;
    private javax.swing.JLabel txtActors;
    private javax.swing.JLabel txtCategory;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JLabel txtDirection;
    private javax.swing.JLabel txtDuration;
    private com.cinemaapp.client.swing.win_button.WinButton winButton;
    // End of variables declaration//GEN-END:variables
}
