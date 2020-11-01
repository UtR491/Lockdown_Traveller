
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
            } else if (request instanceof RemoveTrainsRequest) {
                RemoveTrainsRequestHandler removeTrainsRequestHandler = new RemoveTrainsRequestHandler(Server.getConnection(), (RemoveTrainsRequest) request, oos);
                removeTrainsRequestHandler.sendQuery();
            } else if (request instanceof CancelTrainsRequest) {
                CancelTrainsRequestHandler cancelTrainsRequestHandler = new CancelTrainsRequestHandler(Server.getConnection(), (CancelTrainsRequest) request, oos);
                cancelTrainsRequestHandler.sendQuery();
            } else if (request instanceof AddTrainsRequest) {
                AddTrainsRequestHandler addTrainsRequestHandler = new AddTrainsRequestHandler(Server.getConnection(), (AddTrainsRequest) request, oos);
                addTrainsRequestHandler.sendQuery();
            } else if (request instanceof AddSeatsRequest) {
                AddSeatsRequestHandler addSeatsRequestHandler = new AddSeatsRequestHandler(Server.getConnection(), (AddSeatsRequest) request, oos);
                addSeatsRequestHandler.sendQuery();
            } else if (request instanceof RemoveSeatsRequest) {
                RemoveSeatsRequestHandler removeSeatsRequestHandler = new RemoveSeatsRequestHandler(Server.getConnection(), (RemoveSeatsRequest) request, oos);
                removeSeatsRequestHandler.sendQuery();
            } else if (request instanceof AddCoachesRequest) {
                AddCoachesRequestHandler addCoachesRequestHandler = new AddCoachesRequestHandler(Server.getConnection(), (AddCoachesRequest) request, oos);
                addCoachesRequestHandler.sendQuery();
            } else if (request instanceof RemoveCoachesRequest) {
                RemoveCoachesRequestHandler removeCoachesRequestHandler = new RemoveCoachesRequestHandler(Server.getConnection(), (RemoveCoachesRequest) request, oos);
                removeCoachesRequestHandler.sendQuery();
            } else if (request instanceof MaintainCustomerRequest) {
                System.out.println("Maintain customer request");
                MaintainCustomerRequestHandler maintainCustomerRequestHandler = new MaintainCustomerRequestHandler(oos, Server.getConnection(), (MaintainCustomerRequest) request);
                maintainCustomerRequestHandler.sendQuery();
            } else if (request instanceof MaintainSeatsRequest) {
                System.out.println("Maintain seats request");
                MaintainSeatsRequestHandler maintainSeatsRequestHandler = new MaintainSeatsRequestHandler(oos, Server.getConnection(), (MaintainSeatsRequest) request);
                maintainSeatsRequestHandler.sendQuery();
            } else if (request instanceof RerouteRequest) {
                RerouteRequestHandler rerouteRequestHandler = new RerouteRequestHandler(Server.getConnection(), (RerouteRequest) request, oos);
                try {
                    rerouteRequestHandler.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof ViewCancelledTrainsRequest) {
                ViewCancelledTrainsRequestHandler viewCancelledTrainsRequestHandler = new ViewCancelledTrainsRequestHandler(Server.getConnection(), (ViewCancelledTrainsRequest) request, oos);
                viewCancelledTrainsRequestHandler.sendQuery();
            } else if (request instanceof DisplayTouristPackageRequest) {
                DisplayTouristPackageRequestHandler displayTouristPackageRequestHandler = new DisplayTouristPackageRequestHandler(oos, Server.getConnection(), (DisplayTouristPackageRequest) request);
                try {
                    displayTouristPackageRequestHandler.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof BookTouristPackageRequest) {
                BookTouristPackageRequestHandler bookTouristPackageRequestHandler = new BookTouristPackageRequestHandler(Server.getConnection(), oos, (BookTouristPackageRequest) request);
                bookTouristPackageRequestHandler.sendQuery();
            } else if (request instanceof NotificationRequest) {
                NotificationRequestHandler notificationRequestHandler = new NotificationRequestHandler(Server.getConnection(), (NotificationRequest) request, oos);
                try {
                    notificationRequestHandler.sendQuery();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof ViewReroutedTrainsRequest) {
                ViewReroutedTrainsRequestHandler viewReroutedTrainsRequestHandler = new ViewReroutedTrainsRequestHandler(Server.getConnection(), oos, (ViewReroutedTrainsRequest) request);
                try {
                    viewReroutedTrainsRequestHandler.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof DisplayTrainsRunningTodayRequest) {
                DisplayTrainsRunningTodayRequestHandler displayTrainsRunningTodayRequestHandler = new DisplayTrainsRunningTodayRequestHandler(Server.getConnection(), oos, (DisplayTrainsRunningTodayRequest) request);
                try {
                    displayTrainsRunningTodayRequestHandler.sendQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (request instanceof BookingHistoryRequest) {
                BookingHistoryHandler bookingHistoryHandler = new BookingHistoryHandler(Server.getConnection(), oos, (BookingHistoryRequest) request);
                bookingHistoryHandler.sendQuery();
            } else if (request instanceof SetPlatformRequest) {
                SetPlatformRequestHandler setPlatformRequestHandler = new SetPlatformRequestHandler(Server.getConnection(), oos, (SetPlatformRequest) request);
                setPlatformRequestHandler.sendQuery();
            } else if (request instanceof ViewPlatformRequest) {
                ViewPlatformRequestHandler viewPlatformRequestHandler = new ViewPlatformRequestHandler(Server.getConnection(), oos, (ViewPlatformRequest) request);
                viewPlatformRequestHandler.sendQuery();
            }
            else if(request instanceof SetPlatformRequest)
            {
                SetPlatformRequestHandler setPlatformRequestHandler=new SetPlatformRequestHandler(Server.getConnection(),oos,(SetPlatformRequest)request);
                setPlatformRequestHandler.sendQuery();
            }
            else if(request instanceof ViewPlatformRequest)
            {
                ViewPlatformRequestHandler viewPlatformRequestHandler=new ViewPlatformRequestHandler(Server.getConnection(),oos,(ViewPlatformRequest)request);
                viewPlatformRequestHandler.sendQuery();
            }
            else if(request instanceof  ChatMessageRequest)
            {
                //checking if the map already contains a socket for that ID(user)
                if(customerSocket.containsKey(((ChatMessageRequest) request).getID()))//if yes then we will replace it with the current socket
                {
                   customerSocket.replace(((ChatMessageRequest) request).getID(),socket);
                }
                else //else we will add the key value pair
                    customerSocket.put(((ChatMessageRequest) request).getID(),socket);

                //now from the request object we will get the "to" field and get the corresponding socket
                //if yes then it means the admin is sending the message TO a user
                if(customerSocket.containsKey(((ChatMessageRequest) request).getTo()))
                {
                    ChatMessageHandler chatMessageHandler=new ChatMessageHandler(customerSocket.get(((ChatMessageRequest) request).getTo()),(ChatMessageRequest)request,customerSocket.get(((ChatMessageRequest) request).getID()));
                }
                else //if it is the first connection
                {
                    ChatMessageHandler chatMessageHandler=new ChatMessageHandler(adminSocket,(ChatMessageRequest)request,customerSocket.get(((ChatMessageRequest) request).getID()));
                }



            }
        }
    }
}
