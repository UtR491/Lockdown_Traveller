import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles the admin login request.
 */
public class AdminLoginRequestHandler extends Handler{

    final private AdminLoginRequest adminLoginRequest;
    final private ObjectOutputStream oos;
    final private Connection connection;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param adminLoginRequest The admin login request object with relevant information.
     * @param oos The output stream to send the object.
     */
    public AdminLoginRequestHandler(AdminLoginRequest adminLoginRequest, ObjectOutputStream oos, Connection connection) {
        this.adminLoginRequest = adminLoginRequest;
        this.oos = oos;
        this.connection = connection;
    }

    /**
     * This is the first function called inside the request identifier.
     */
    @Override
    public void sendQuery() {
        // Get the details about the admin with the specified info.
        String query = "select * from Admin where Username = ? and Password = ?;";
        AdminLoginResponse adminLoginResponse = adminLoginRequest(query);
        Server.SendResponse(oos, adminLoginResponse);
    }

    /**
     * Execute the query and send the relevant response.
     * @params Queries.
     * @return Response to the admin login query.
     */
    public @Nullable AdminLoginResponse adminLoginRequest(String query) {
        try {
            System.out.println("Prepared statement");
            PreparedStatement validateLogin = connection.prepareStatement(query);
            validateLogin.setString(1, adminLoginRequest.getUsername());
            validateLogin.setString(2, adminLoginRequest.getPassword());
            System.out.println("going to execute");
            @Initialized @NonNull ResultSet adminCredentials = validateLogin.executeQuery(); // This @NonNull here is
            // not redundant because without it, there is a dereference.of.nullable warning in the while clause in the
            // else block below. Same with the @Initialized annotation.

            // Check if the result set is empty. If empty send failure as object response.
            if (!adminCredentials.next()) {
                System.out.println("fail");
                return new AdminLoginResponse("failure", "");
            } else {
                do {
                    System.out.println("success");
                    @SuppressWarnings("assignment.type.incompatible") // Admin_ID cannot be null. Refer the README.
                    @NonNull String s = adminCredentials.getString("Admin_ID");
                    return new AdminLoginResponse("success", s);
                } while (adminCredentials.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
}
