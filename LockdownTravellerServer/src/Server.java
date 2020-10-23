import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
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
    public static Object ReceiveRequest(ObjectInputStream objectInputStream) {
        try {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
