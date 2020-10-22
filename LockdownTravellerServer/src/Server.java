
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static ObjectOutputStream objectOutputStream = null;
    static ObjectInputStream objectInputStream = null;
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
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("customer connected now creating database connection");
                System.out.println("database connected now going to request identifier.");
                Thread t = new Thread(new RequestIdentifier(socket, objectOutputStream, objectInputStream, db, db.getConnection()));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static void SendResponse(Response response) {
        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SendResponse (ObjectOutputStream oos, Response response) {
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object ReceiveRequest() {
        try {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
