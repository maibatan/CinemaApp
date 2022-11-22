/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinemaapp.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author New
 */
public class ImageUtil {
   
    public boolean download(String imageUrl,String name){
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            FileOutputStream fo = new FileOutputStream("images/"+name+".jpg");
            int b = 0;
            while ((b=is.read())!= -1) {
                fo.write(b);         
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public ImageIcon load(String name) {
       return new ImageIcon("images/"+name+".jpg");
    }
}
