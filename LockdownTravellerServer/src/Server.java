import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Server side of the application which assigns a thread for every client that connects to it. Every client has it's
 * own socket connection and it's own input and output streams.
 */
public class Server {

    private static Connection connection;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket;
        try {
            serverSocket = new ServerSocket(12000);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Infinite loop. After a client connects to the server, the server client interaction is assigned a thread and
        // the server loops back to the serverSocket.accept() line to listen for other connection attempts.
        while (true) {
            try {
                assert serverSocket != null;
                System.out.println("Waiting for a customer");
                socket = serverSocket.accept();
                System.out.println("customer connected now creating database connection");
                System.out.println("database connected now going to request identifier.");
                Thread t = new Thread(new RequestIdentifier(socket));

                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Connects the server side to the database to make queries.
     * @return Connection to the database.
     */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/lockdown_traveller";
            connection = DriverManager.getConnection(url, "utkarsh", "Hello@123");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Send the response object from the relevant handler.
     * @param oos Reference to the output stream of the thread that called the function.
     * @param response The object to be sent via tha output stream.
     */
    public static void SendResponse(ObjectOutputStream oos, Response response) {
        try {
            System.out.println("Sending the object now " + response);
            if (response == null)
                System.out.println("The object is null");
            else
                System.out.println("The object is NOT null");
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the received object from the client.
     * @param objectInputStream The input stream in the thread.
     * @return The object read from the input stream.
     */
    public static Object ReceiveRequest(ObjectInputStream objectInputStream)
    {
        try {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}