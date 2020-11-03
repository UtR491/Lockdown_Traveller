
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RequestIdentifier implements Runnable {
    Socket socket;
    Socket adminSocket;
    Map<String,Socket>customerSocket= new HashMap<>();
    public RequestIdentifier(Socket socket) {
        this.socket = socket;
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
        while (socket.isConnected()) {
            assert ois != null;
            Object request = Server.ReceiveRequest(ois);
            if (request == null)
                break;

            if (request instanceof LoginRequest) {
                System.out.println("Login Request");
                LoginRequestHandler loginRequestHandler = new LoginRequestHandler(oos, Server.getConnection(), (LoginRequest) request);
                loginRequestHandler.sendQuery();
            } else if (request instanceof BookingRequest) {
                System.out.println("Booking Request");
                BookingHandler brh = new BookingHandler(Server.getConnection(), (BookingRequest) request, oos);
                brh.sendQuery();
            } else if (request instanceof DisplayTrainsRequest) {
                DisplayTrainsRequestHandler dtrh = new DisplayTrainsRequestHandler(Server.getConnection(), (DisplayTrainsRequest) request, oos);
                try {
                    dtrh.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else if (request instanceof CancelBookingRequest) {
                CancelBookingRequestHandler c = new CancelBookingRequestHandler(Server.getConnection(), (CancelBookingRequest) request, oos);
                try {
                    c.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof RegisterRequest) {
                System.out.println("Register Request");
                RegisterRequestHandler registerRequestHandler = new RegisterRequestHandler(Server.getConnection(), (RegisterRequest) request, oos);
                registerRequestHandler.sendQuery();
            } else if (request instanceof AdminLoginRequest) {
                System.out.println("Admin login request");
                AdminLoginRequestHandler adminLoginRequestHandler = new AdminLoginRequestHandler((AdminLoginRequest) request, oos, Server.getConnection());
                adminLoginRequestHandler.sendQuery();
                adminSocket=socket;
            } else if (request instanceof MaintainTrainsRequest) {
                System.out.println("Maintain trains request");
                MaintainTrainsRequestHandler maintainTrainsRequestHandler = new MaintainTrainsRequestHandler(oos, Server.getConnection(), (MaintainTrainsRequest) request);
                maintainTrainsRequestHandler.sendQuery();
            } else if (request instanceof MaintainCustomerRequest) {
                System.out.println("Maintain customer request");
                MaintainCustomerRequestHandler maintainCustomerRequestHandler = new MaintainCustomerRequestHandler(oos, Server.getConnection(), (MaintainCustomerRequest) request);
                maintainCustomerRequestHandler.sendQuery();
            } else if (request instanceof MaintainSeatsRequest) {
                System.out.println("Maintain seats request");
                MaintainSeatsRequestHandler maintainSeatsRequestHandler = new MaintainSeatsRequestHandler(oos, Server.getConnection(), (MaintainSeatsRequest) request);
                maintainSeatsRequestHandler.sendQuery();
            } else if (request instanceof NotificationRequest) {
                NotificationRequestHandler notificationRequestHandler = new NotificationRequestHandler(Server.getConnection(), (NotificationRequest) request, oos);
                try {
                    notificationRequestHandler.sendQuery();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof BookingHistoryRequest) {
                BookingHistoryHandler bookingHistoryHandler = new BookingHistoryHandler(Server.getConnection(), oos, (BookingHistoryRequest) request);
                bookingHistoryHandler.sendQuery();
            }
        }
    }
}
