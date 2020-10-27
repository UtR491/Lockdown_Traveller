package com.company;

import java.awt.font.TextHitInfo;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Server {
    public static Map<String,Socket> map;

    public static void main(String[] args) {
        map= new HashMap<>();
        ServerSocket serverSocket=null;

        try {
            serverSocket = new ServerSocket(11000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server Started");
        Socket socket;

        while (true) {
            try {
                socket = serverSocket.accept();

                HandleClient handleClient = new HandleClient(socket);
                Thread thread = new Thread(handleClient);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}





