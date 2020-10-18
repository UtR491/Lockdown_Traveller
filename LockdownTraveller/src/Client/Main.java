package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {
    Socket socket=null;
    ObjectOutputStream outputStream=null;
    ObjectInputStream inputStream = null;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            socket=new Socket("localhost", 12000);
            outputStream=new ObjectOutputStream(socket.getOutputStream());
            inputStream =new ObjectInputStream(socket.getInputStream());

        } catch (IOException e){
            e.printStackTrace();
        }


        FXMLLoader loader=null;

        loader=new FXMLLoader(getClass().getResource("Login.fxml"));

        primaryStage.setTitle("Sign In");
        try{
            assert loader!=null;
            primaryStage.setScene(new Scene(loader.load()));
            LoginController login=loader.getController();
            login.initData(outputStream,inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.show();
    }
}
