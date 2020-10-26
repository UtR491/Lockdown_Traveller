
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class RequestIdentifier implements Runnable{
    Socket socket;
    DatabaseConnector db;
    public RequestIdentifier(Socket socket, DatabaseConnector db, Connection connection) {
        this.socket = socket;
        this.db = db;
    }

    @Override
    public void run() {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(socket.isConnected()) {
            Object request = Server.ReceiveRequest(ois);
            if(request == null)
                break;

            if(request instanceof LoginRequest) {
                System.out.println("Login Request");
                LoginRequestHandler loginRequestHandler = new LoginRequestHandler(oos, db.getConnection(), (LoginRequest) request);
                loginRequestHandler.sendQuery();
            }

            else if(request instanceof BookingRequest) {
                System.out.println("Booking Request");
                BookingRequestHandler brh = new BookingRequestHandler(db, (BookingRequest) request, oos);
                brh.sendQuery();
            }
           else if (request instanceof DisplayTrainsRequest) {
                DisplayTrainsRequestHandler dtrh = new DisplayTrainsRequestHandler(db.getConnection(), (DisplayTrainsRequest) request,oos);
                dtrh.sendQuery();

            } else if (request instanceof CancelBookingRequest) {
                CancelBookingRequestHandler c = new CancelBookingRequestHandler(db.getConnection(), (CancelBookingRequest) request,oos);
                try {
                    c.sendQuery();
                } catch (SQLException e) {
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
                MaintainTrainsRequestHandler maintainTrainsRequestHandler = new MaintainTrainsRequestHandler(oos, db.getConnection(), (MaintainTrainsRequest) request);
                maintainTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof RemoveTrainsRequest)
            {
                RemoveTrainsRequestHandler removeTrainsRequestHandler=new RemoveTrainsRequestHandler(db.getConnection(),(RemoveTrainsRequest)request,oos);
                removeTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof CancelTrainsRequest)
            {
                CancelTrainsRequestHandler cancelTrainsRequestHandler=new CancelTrainsRequestHandler(db.getConnection(),(CancelTrainsRequest)request,oos);
                cancelTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof AddTrainsRequest)
            {
                AddTrainsRequestHandler addTrainsRequestHandler=new AddTrainsRequestHandler(db.getConnection(),(AddTrainsRequest)request,oos);
                addTrainsRequestHandler.sendQuery();
            }
            else if(request instanceof AddSeatsRequest)
            {
                AddSeatsRequestHandler addSeatsRequestHandler=new AddSeatsRequestHandler(db.getConnection(),(AddSeatsRequest)request,oos);
                addSeatsRequestHandler.sendQuery();
            }
            else if(request instanceof RemoveSeatsRequest)
            {
                RemoveSeatsRequestHandler removeSeatsRequestHandler=new RemoveSeatsRequestHandler(db.getConnection(),(RemoveSeatsRequest)request,oos);
                removeSeatsRequestHandler.sendQuery();
            }
            else if(request instanceof AddCoachesRequest)
            {
                AddCoachesRequestHandler addCoachesRequestHandler=new AddCoachesRequestHandler(db.getConnection(),(AddCoachesRequest)request,oos);
                addCoachesRequestHandler.sendQuery();
            }
            else if(request instanceof RemoveCoachesRequest)
            {
                RemoveCoachesRequestHandler removeCoachesRequestHandler=new RemoveCoachesRequestHandler(db.getConnection(),(RemoveCoachesRequest)request,oos);
                removeCoachesRequestHandler.sendQuery();
            }
            else if(request instanceof MaintainCustomerRequest) {
                System.out.println("Maintain customer request");
                MaintainCustomerRequestHandler maintainCustomerRequestHandler = new MaintainCustomerRequestHandler(oos, db.getConnection(), (MaintainCustomerRequest) request);
                maintainCustomerRequestHandler.sendQuery();
            }
            else if(request instanceof MaintainSeatsRequest) {
                System.out.println("Maintain seats request");
                MaintainSeatsRequestHandler maintainSeatsRequestHandler = new MaintainSeatsRequestHandler(oos, db.getConnection(), (MaintainSeatsRequest) request);
                maintainSeatsRequestHandler.sendQuery();
            }
            else if(request instanceof RerouteRequest)
            {
                RerouteRequestHandler rerouteRequestHandler=new RerouteRequestHandler(db.getConnection(),(RerouteRequest)request,oos);
                rerouteRequestHandler.sendQuery();
            }
            else if(request instanceof ViewCancelledTrainsRequest)
            {
                ViewCancelledTrainsRequestHandler viewCancelledTrainsRequestHandler=new ViewCancelledTrainsRequestHandler(Server.getConnection(),(ViewCancelledTrainsRequest)request,oos);
                viewCancelledTrainsRequestHandler.sendQuery();
            }
        }
    }
}
