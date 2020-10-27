import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRequestHandler {
    Connection connection;
    LoginRequest loginRequest ;
    ObjectOutputStream oos;

    public LoginRequestHandler(ObjectOutputStream oos, Connection connection, LoginRequest loginRequest) {
        System.out.println("Inside LoginRequestHandler");
        this.connection = connection;
        this.loginRequest=loginRequest;
        this.oos = oos;
    }

    public void sendQuery() {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("The inputs given were " + username + " and " + password);
        String query = "select * from User where Username = '" + username + "' and Password='" + password +"';";
        System.out.println("Sending the loginquery to database connector");
        LoginResponse loginResponse = loginRequest(query);
        System.out.println("Sending the login response to client");
        Server.SendResponse(oos, loginResponse);
        System.out.println("Response sent");
    }

    private LoginResponse loginRequest(String query) {
        try {
            PreparedStatement loginQuery = connection.prepareStatement(query);
            ResultSet loginResult = loginQuery.executeQuery();
            if (!loginResult.next()) {
                return new LoginResponse(null, null, null, null, null);
            } else {
                do {
                    return new LoginResponse(loginResult.getString("User_ID"),
                            loginResult.getString("First_Name") + " " + loginResult.getString("Last_Name"),
                            loginResult.getString("Username"), loginResult.getString("Email_ID"),
                            loginResult.getString("Phone_No"));
                } while (loginResult.next());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Returning the last null");
        return null;
    }
}
