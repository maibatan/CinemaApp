package com.cinemaapp.server.main;

import com.cinemaapp.server.interfaces.ICinema;
import com.cinemaapp.utils.RSAKeyPairGenerator;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public static RSAKeyPairGenerator keyPair;
    public static ArrayList<ICinema> cinemas;
    private ServerSocket server = null;
    private ExecutorService executorService = null;
    private Vector<ServerService> clientList = null;
    private boolean running;
    
    public Server(int port)
    {  
        try {
            server = new ServerSocket(port);
            System.out.println("Server opened...");
            executorService = Executors.newCachedThreadPool();
            running = true;
            clientList = new Vector<>();
            keyPair = new RSAKeyPairGenerator();
            cinemas = new ArrayList<>();
            cinemas.add(new BHDCinema());
            cinemas.add(new LotteCinema());
        } catch (IOException e) { 
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e); 
        }           
    }
    public void run(){
        try {           
            while(running) {
                RemoveNotRunningService();
                Socket socket = server.accept();
                System.out.println("Client Ip "+ socket.getInetAddress().getHostAddress() +" connected...");
                ServerService service = new ServerService(socket);
                clientList.add(service);
                executorService.submit(service);
            }   
        } catch (IOException e) { 
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void shutdown(){
        try {
            running = false;
            server.close();
            for (ServerService serverService : clientList) {
                serverService.close();
            }
            executorService.shutdown();
            System.out.println("Server closed connection...");
        } catch (IOException e) { System.err.println(e); }
    }
    private void RemoveNotRunningService(){
        for (ServerService serverService : clientList) {
            if(!serverService.isRunning()){
                clientList.remove(serverService);
            }
        }
    }
}
