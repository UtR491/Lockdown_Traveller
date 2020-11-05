import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Execution begins here.
 */
public class Main extends Application {

    // Socket for connection to the server.
    Socket socket=null;
    // OutputStream to send the request object.
    static ObjectOutputStream outputStream=null;
    // InputStream to receive response object.
    static ObjectInputStream inputStream = null;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start of the UI thread.
     * @param primaryStage The window in which the application will run.
     */
    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader=new FXMLLoader(getClass().getResource("Login.fxml"));

        try {
            // Connecting to the ServerSocket.
            System.out.println("Creating a connection in thread " + Thread.currentThread());
            socket=new Socket("localhost", 12000);
            // Initializing the input and output streams.
            outputStream=new ObjectOutputStream(socket.getOutputStream());
            inputStream =new ObjectInputStream(socket.getInputStream());

        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.setTitle("Sign In");
        try{
            primaryStage.setScene(new Scene(loader.load()));
            System.out.println("Sending ois and oos in thread" + Thread.currentThread());
        } catch (IOException e){
            e.printStackTrace();
        }

        primaryStage.show();
    }

    /**
     * To send the request object to the server side.
     * @param o Request object.
     */
    public static void SendRequest(Request o) {
        try {
            outputStream.writeObject(o);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To receive the response object from the server side. Called just after sending a request object.
     * @return Response object from the server.
     */
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
}