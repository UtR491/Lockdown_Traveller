import java.io.ObjectOutputStream;

public class LoginRequestHandler {
    DatabaseConnector db = null;
    LoginRequest loginRequest = null;
    ObjectOutputStream oos = null;

    public LoginRequestHandler(DatabaseConnector db, LoginRequest loginRequest, ObjectOutputStream oos) {
        System.out.println("Inside LoginRequestHandler");
        this.db=db;
        this.loginRequest=loginRequest;
        this.oos=oos;
    }

    public void sendQuery() {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("The inputs given were " + username + " and " + password);
        String query = "select User_ID from User where Username = '" + username + "' and Password='" + password +"';";
        System.out.println("Sending the loginquery to database connector");
        LoginResponse loginResponse = db.loginRequest(query);
        System.out.println("Sending the login response to client");
        Server.SendResponse(oos, loginResponse);
    }
}
