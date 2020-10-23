
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
            Object request;
                request = Server.ReceiveRequest();

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
                DisplayTrainsRequestHandler dtrh = new DisplayTrainsRequestHandler(db.getConnection(), (DisplayTrainsRequest) request);
                dtrh.sendQuery();

            } else if (request instanceof CancelBookingRequest) {
                CancelBookingRequestHandler c = new CancelBookingRequestHandler(db.getConnection(), (CancelBookingRequest) request);
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
            else if(request instanceof RemoveTrainsRequest)
            {
                RemoveTrainsRequestHandler removeTrainsRequestHandler=new RemoveTrainsRequestHandler(db.getConnection(),(RemoveTrainsRequest)request);
                removeTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof CancelTrainsRequest)
            {
                CancelTrainsRequestHandler cancelTrainsRequestHandler=new CancelTrainsRequestHandler(db.getConnection(),(CancelTrainsRequest)request);
                cancelTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof AddTrainsRequest)
            {
                AddTrainsRequestHandler addTrainsRequestHandler=new AddTrainsRequestHandler(db.getConnection(),(AddTrainsRequest)request);
                addTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof AddSeatsRequest)
            {
                AddSeatsRequestHandler addSeatsRequestHandler=new AddSeatsRequestHandler(db.getConnection(),(AddSeatsRequest)request);
                addSeatsRequestHandler.sendQuery();
            }
            else if(request instanceof RemoveSeatsRequest)
            {
                RemoveSeatsRequestHandler removeSeatsRequestHandler=new RemoveSeatsRequestHandler(db.getConnection(),(RemoveSeatsRequest)request);
                removeSeatsRequestHandler.sendQuery();
            }
            else if(request instanceof AddCoachesRequest)
            {
                AddCoachesRequestHandler addCoachesRequestHandler=new AddCoachesRequestHandler(db.getConnection(),(AddCoachesRequest)request);
                addCoachesRequestHandler.sendQuery();
            }
            else if(request instanceof RemoveCoachesRequest)
            {
                RemoveCoachesRequestHandler removeCoachesRequestHandler=new RemoveCoachesRequestHandler(db.getConnection(),(RemoveCoachesRequest)request);
                removeCoachesRequestHandler.sendQuery();
            }
        }
    }
}
