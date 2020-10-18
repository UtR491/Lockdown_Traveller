package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Client.BookingRequest;

public class RequestIdentifier implements Runnable{
    Socket socket;
    DatabaseConnector db;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public RequestIdentifier(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, DatabaseConnector db) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.db = db;
    }

    @Override
    public void run() {
        while(socket.isConnected()) {
            Object request = null;
            try {
                request = ois.readObject();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(request instanceof BookingRequest) {
                BookingRequestHandler brh = new BookingRequestHandler(db, (BookingRequest) request, oos);
                brh.gottaDoWhatYouGottaDo();
            }
        }
    }
}
