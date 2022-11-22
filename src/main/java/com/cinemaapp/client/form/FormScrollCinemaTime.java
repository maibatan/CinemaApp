package com.cinemaapp.client.form;

import com.cinemaapp.client.component.CinemaShowTimeItem;
import com.cinemaapp.client.swing.ScrollBar;
import com.cinemaapp.model.CinemaShowTimeModel;


public class FormScrollCinemaTime extends javax.swing.JPanel {

    public FormScrollCinemaTime() {
        initComponents();
        scroll.setVerticalScrollBar(new ScrollBar());
    }

    public void addItem(CinemaShowTimeModel data) {
        CinemaShowTimeItem item = new CinemaShowTimeItem();
        item.setData(data);
        panelItem.add(item);
        panelItem.repaint();
        panelItem.revalidate();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        panelItem = new com.cinemaapp.client.swing.PanelItem();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1000, 600));

        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setViewportView(panelItem);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.cinemaapp.client.swing.PanelItem panelItem;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
}
