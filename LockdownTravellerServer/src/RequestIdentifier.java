
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class RequestIdentifier implements Runnable{
    Socket socket;
    DatabaseConnector db;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public RequestIdentifier(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, DatabaseConnector db, Connection connection) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.db = db;
    }

    @Override
    public void run() {
        while(socket.isConnected()) {
            Object request = Server.ReceiveRequest();
            if(request == null)
                break;

            if(request instanceof LoginRequest) {
                System.out.println("Login Request");
                LoginRequestHandler loginRequestHandler = new LoginRequestHandler(db.getConnection(), (LoginRequest) request);
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
                registerRequestHandler.sendQuery();
            }
            else if (request instanceof AdminLoginRequest) {
                System.out.println("Admin login request");
                AdminLoginRequestHandler adminLoginRequestHandler = new AdminLoginRequestHandler((AdminLoginRequest) request, oos, db);
                adminLoginRequestHandler.sendQuery();
            }
            else if (request instanceof MaintainTrainsRequest) {
                System.out.println("Maintain trains request");
                MaintainTrainsRequestHandler maintainTrainsRequestHandler = new MaintainTrainsRequestHandler(db.getConnection(), (MaintainTrainsRequest) request);
                maintainTrainsRequestHandler.sendQuery();
            }
        }
    }
}
