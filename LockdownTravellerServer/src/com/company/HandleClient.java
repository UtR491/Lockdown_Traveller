package com.company;

import java.io.*;
import java.net.Socket;


public class HandleClient implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public HandleClient(Socket socket) {
        this.socket = socket;
        try {
             this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String username=null;
        try {
            username= (String) this.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Disconnected");
            Server.map.remove(username,socket);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Server.map.put(username,socket);
        while (true) {
            Message message=null;
            try {
                message = (Message) this.objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Disconnected");
                Server.map.remove(username,socket);
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String to;
            to= message.getTo();
            Socket receiver;
            receiver=   Server.map.get(to);
            if(receiver==null){
                try {
                    Message errorMessage= new Message("Receiver not active", "server", username);
                    objectOutputStream.writeObject(errorMessage);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Disconnected");
                    Server.map.remove(username,socket);
                    return;
                }
                continue;
            }
            try {
                ObjectOutputStream receiverStream = new ObjectOutputStream(receiver.getOutputStream());
                receiverStream.writeObject(message);
                receiverStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
