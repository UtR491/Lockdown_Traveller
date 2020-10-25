import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Main extends Application {
    Socket socket=null;
    ObjectOutputStream outputStream=null;
    ObjectInputStream inputStream = null;
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
            login.initData(outputStream,inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.show();
    }

    public static void SendRequest(ObjectOutputStream oos, Request o) {
        try {
            oos.writeObject(o);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void MACaddress (){
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
            int finalmac = Integer.parseInt(sb.toString());
            int BookindID;
            BookindID = finalmac;
        }
    }

}
