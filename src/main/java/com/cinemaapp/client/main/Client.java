/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinemaapp.client.main;

import com.cinemaapp.utils.RSAUtil;
import com.cinemaapp.utils.Security;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author New
 */
public class Client {
    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private Security security = null;
    
    public Client(String host, int port)
    {
        try{
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            configSecurity();
        } catch (IOException e) { System.err.println(e); }
    }
    private void configSecurity()
    {
        try{
            String publicKey = in.readLine();      
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey PK = factory.generatePublic(spec);
            String charlList = "12345567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random randomGenerator = new Random();
            StringBuilder generatedKey = new StringBuilder();
            for(int i=0; i<24; i++){
                int randomInt = Math.abs(randomGenerator.nextInt()%charlList.length());
                char ch = charlList.charAt(randomInt);
                generatedKey.append(ch);
            }
            String msg = RSAUtil.Encrypt(generatedKey.toString(), PK);
            out.write(msg);
            out.newLine();
            out.flush();
            security = new Security(generatedKey.toString());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) { 
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        } 
    }
    public void send(String data) {
        try {
            String msg = security.Encrypt(data);
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public String receive() {
        try {
            String msg = in.readLine();
            String data = security.Decrypt(msg);
            return data;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
    public void close() {     
        try {
            send("close");
            in.close();
            out.close();
            socket.close();
            System.out.println("Client closed connection...");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
