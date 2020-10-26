import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Random;

public class Main extends Application {
    Socket socket=null;
    static ObjectOutputStream outputStream=null;
    static ObjectInputStream inputStream = null;
    final private static char base36Char[] = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
    final private static Random random = new Random();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Login.fxml"));

        try {
            System.out.println("Creating a connection in thread " + Thread.currentThread());
            socket=new Socket("localhost", 12000);
            outputStream=new ObjectOutputStream(socket.getOutputStream());
            inputStream =new ObjectInputStream(socket.getInputStream());

        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.setTitle("Sign In");
        try{
            primaryStage.setScene(new Scene(loader.load()));
            LoginController login=loader.getController();
            System.out.println("Sending ois and oos in thread" + Thread.currentThread());
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.show();
    }

    public static void SendRequest(Request o) {
        try {
            outputStream.writeObject(o);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Response ReceiveResponse() {
        try {
            System.out.println("Inside receive response to read the object");
            Object response = inputStream.readObject();
            System.out.println("Object read");
            if(response == null) {
                System.out.println("The response is null");
            }
            else
                System.out.println("The response is NOT null");
            return (Response) response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Returning the last null after receive response in Main of admin");
        return null;
    }
    public static String MACaddress (){
        InetAddress ip = null;
        {
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        NetworkInterface network=null;
        {
            try {
                network = NetworkInterface.getByInetAddress(ip);
            } catch (SocketException socketException) {
                socketException.printStackTrace();
            }
            byte[] mac = new byte[0];
            try {
                mac = network.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            for (int num : mac) {
                sb.append(num);
            }
            return sb.toString();
        }
    }

    public static String randomIDGenerator() {
        String id = "";
        for(int i = 0; i < 7; i++) {
            id += base36Char[random.nextInt(36)];
        }
        return id;
    }
}
