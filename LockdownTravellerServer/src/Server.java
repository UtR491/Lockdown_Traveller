
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
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("customer connected now creating database connection");
                System.out.println("database connected now going to request identifier.");
                Thread t = new Thread(new RequestIdentifier(socket, oos, ois, db));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static void SendResponse(ObjectOutputStream oos, Response response) {
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SendRequest(ObjectOutputStream oos, Request o) {
        try {
            oos.writeObject(o);
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

}
