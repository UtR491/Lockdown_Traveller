package Client.src.com.company;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 11000);

        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Client created.");
        System.out.println("Enter username.");
        String name = null;
        try {
            name = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ReceiveMsg receiveMsg = new ReceiveMsg(socket);
            Thread thread=new Thread(receiveMsg);
            thread.start();
            System.out.println("receive message thread started");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(name);
            while (true){
                String message;
                System.out.println("Enter message");
                message=bufferedReader.readLine();
                String to;
                to =bufferedReader.readLine();
                Message sendMsg =new Message(message,name,to);
                outputStream.writeObject(sendMsg);
                outputStream.flush();
                System.out.println("message sent to: "+to);
            }

        } catch (IOException e){
            e.printStackTrace();
        }


    }
}

