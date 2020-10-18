package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import Client.BookingRequest;
import Client.LoginRequest;
import Client.RegisterRequest;
import Client.DisplayTrainsRequest;
import Client.CancelBookingRequest;

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
            if(request instanceof LoginRequest) {
                System.out.println("Login Request");
                LoginRequestHandler loginRequestHandler = new LoginRequestHandler(db, (LoginRequest) request, oos);
                loginRequestHandler.sendQuery();
            }
            else if(request instanceof BookingRequest) {
                System.out.println("Booking Request");
                BookingRequestHandler brh = new BookingRequestHandler(db, (BookingRequest) request, oos);
                brh.sendQuery();
            }
           else if (request instanceof DisplayTrainsRequest) {
                DisplayTrainsRequestHandler dtrh = new DisplayTrainsRequestHandler(db, (DisplayTrainsRequest) request, oos);
                try {
                    dtrh.sendQuery();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof CancelBookingRequest) {
                CancelBookingRequestHandler c = new CancelBookingRequestHandler(db, (CancelBookingRequest) request, oos);
                try {
                    c.sendQuery();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
            else if (request instanceof RegisterRequest){
                System.out.println("Register Request");
                RegisterRequestHandler registerRequestHandler= new RegisterRequestHandler (db, (RegisterRequest) request, oos);
            }
        }
    }
}
