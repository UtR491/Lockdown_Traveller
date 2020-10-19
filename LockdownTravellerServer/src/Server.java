
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
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
}
