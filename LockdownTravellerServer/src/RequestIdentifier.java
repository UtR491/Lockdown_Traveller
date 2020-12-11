
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * RequestIdentifier class. Listens for different types of requests and directs them to the relevant handler. There is
 * an instance of this class for every server-client thread.
 */
public class RequestIdentifier implements Runnable {

    final private Socket socket;

    /**
     * Initializes the RequestIdentifier object with the socket that was used to connect to the server.
     * @param socket The server side end-point.
     */
    public RequestIdentifier(Socket socket) {
        this.socket = socket;
    }

    /**
     * Overloaded function. This is the point from which the thread forks from the main thread to handle the
     * server-client interaction.
     */
    @Override
    public void run() {

        @MonotonicNonNull ObjectInputStream ois = null;
        @MonotonicNonNull ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (socket.isConnected()) {
            Object request = Server.ReceiveRequest(ois);
            if (request == null)
                break;

            // Finding what kind of request has been sent and then sending the request and database connection to the
            // relevant handler.
            if (request instanceof LoginRequest) {
                System.out.println("Login Request");
                LoginRequestHandler loginRequestHandler = new LoginRequestHandler(oos, Server.getConnection(), (LoginRequest) request);
                loginRequestHandler.sendQuery();
            } else if (request instanceof BookingRequest) {
                System.out.println("Booking Request");
                BookingRequestHandler brh = new BookingRequestHandler(Server.getConnection(), (BookingRequest) request, oos);
                brh.sendQuery();
            } else if (request instanceof DisplayTrainsRequest) {
                DisplayTrainsRequestHandler dtrh = new DisplayTrainsRequestHandler(Server.getConnection(), (DisplayTrainsRequest) request, oos);
                dtrh.sendQuery();
            } else if (request instanceof CancelBookingRequest) {
                CancelBookingRequestHandler c = new CancelBookingRequestHandler(Server.getConnection(), (CancelBookingRequest) request, oos);
                c.sendQuery();
            } else if (request instanceof RegisterRequest) {
                System.out.println("Register Request");
                RegisterRequestHandler registerRequestHandler = new RegisterRequestHandler(Server.getConnection(), (RegisterRequest) request, oos);
                registerRequestHandler.sendQuery();
            } else if (request instanceof AdminLoginRequest) {
                System.out.println("Admin login request");
                AdminLoginRequestHandler adminLoginRequestHandler = new AdminLoginRequestHandler((AdminLoginRequest) request, oos, Server.getConnection());
                adminLoginRequestHandler.sendQuery();
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
                    notificationRequestHandler.sendQuery();
            } else if (request instanceof BookingHistoryRequest) {
                BookingHistoryRequestHandler bookingHistoryRequestHandler = new BookingHistoryRequestHandler(Server.getConnection(), oos, (BookingHistoryRequest) request);
                bookingHistoryRequestHandler.sendQuery();
            }
        }
    }
}
