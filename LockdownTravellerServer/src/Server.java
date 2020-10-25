import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Server {
    public static Connection connection;


    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket;
        DatabaseConnector db = null;
        try {
            serverSocket = new ServerSocket(12000);
            db = new DatabaseConnector();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                assert serverSocket != null;
                System.out.println("Waiting for a customer");
                socket = serverSocket.accept();
                System.out.println("customer connected now creating database connection");
                System.out.println("database connected now going to request identifier.");
                Thread t = new Thread(new RequestIdentifier(socket, db, db.getConnection()));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void SendResponse(Response response) {
        try {
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SendResponse (ObjectOutputStream oos, Response response) {
        try {
            System.out.println("Sending the object now " + response);
            if(response == null)
                System.out.println("The object is null");
            else
                System.out.println("The object is NOT null");
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if(connection !=null){
            return connection;
        }
        try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/lockdown_traveller";
                connection = DriverManager.getConnection(url, "root", "Bgr^&Bhu");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static Object ReceiveRequest(ObjectInputStream objectInputStream) {
        try {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
