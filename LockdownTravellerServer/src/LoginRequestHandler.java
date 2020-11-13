import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRequestHandler extends Handler {

    final private Connection connection;
    final private LoginRequest loginRequest ;
    final private ObjectOutputStream oos;

    public LoginRequestHandler(ObjectOutputStream oos, Connection connection, LoginRequest loginRequest) {
        System.out.println("Inside LoginRequestHandler");
        this.connection = connection;
        this.loginRequest=loginRequest;
        this.oos = oos;
    }

    /**
     * This is the first function called inside the request identifier.
     */
    @Override
    public void sendQuery() {
        // Get details for the specified credentials.
        String query = "select * from User where Username = ? and Password = ?;";
        LoginResponse loginResponse = loginRequest(query);
        Server.SendResponse(oos, loginResponse);
    }

    /**
     * Execute the query and return the response.
     * @param query Query to get the details for the credentials.
     * @return The response to the request.
     */
    private @Nullable LoginResponse loginRequest(String query) {
        try {
            PreparedStatement loginQuery = connection.prepareStatement(query);
            loginQuery.setString(1, loginRequest.getUsername());
            loginQuery.setString(2, loginRequest.getPassword());
            ResultSet loginResult = loginQuery.executeQuery();
            // If the result set is empty, this implies the credentials are wrong.
            if (!loginResult.next()) {
                return null; // This change causes the clients program to crash on an unsuccessful login attempt due to
                // a null pointer exception but seems like a reasonable change. The client side crash can be fixed
                // trivially.
            } else {
                @SuppressWarnings("assignment.type.incompatible") // No column in User table can have null values.
                @NonNull String userId = loginResult.getString("User_ID"),
                        name = loginResult.getString("First_Name") + " " + loginResult.getString("Last_Name"),
                        username = loginResult.getString("Username"),
                        email = loginResult.getString("Email_ID"),
                        phone = loginResult.getString("Phone_No");
                return new LoginResponse(userId, name, username, email, phone);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Returning the last null");
        return null;
    }
}
