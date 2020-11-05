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
    public AdminLoginResponse adminLoginRequest(String query) {
        try {
            System.out.println("Prepared statement");
            PreparedStatement validateLogin = connection.prepareStatement(query);
            validateLogin.setString(1, adminLoginRequest.getUsername());
            validateLogin.setString(2, adminLoginRequest.getPassword());
            System.out.println("going to execute");
            ResultSet adminCredentials = validateLogin.executeQuery();
            // Check if the result set is empty. If empty send failure as object response.
            if (!adminCredentials.next()) {
                System.out.println("fail");
                return new AdminLoginResponse("failure", "");
            } else {
                do {
                    System.out.println("success");
                    return new AdminLoginResponse("success", adminCredentials.getString("Admin_ID"));
                } while (adminCredentials.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
}
