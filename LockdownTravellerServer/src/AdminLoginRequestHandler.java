import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginRequestHandler extends Handler{
    AdminLoginRequest adminLoginRequest;
    ObjectOutputStream oos;
    Connection connection;
    public AdminLoginRequestHandler(AdminLoginRequest adminLoginRequest, ObjectOutputStream oos, Connection connection) {
        this.adminLoginRequest = adminLoginRequest;
        this.oos = oos;
        this.connection = connection;
    }

    @Override
    public void sendQuery() {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();
        String query = "select * from Admin where Username = '" + username + "' and Password = '" + password + "';";
        AdminLoginResponse adminLoginResponse = adminLoginRequest(query);
        Server.SendResponse(oos, adminLoginResponse);
    }

    public AdminLoginResponse adminLoginRequest(String query) {
        try {
            System.out.println("Prepared statement");
            PreparedStatement validateLogin = connection.prepareStatement(query);
            System.out.println("going to execute");
            ResultSet adminCredentials = validateLogin.executeQuery();
            if (!adminCredentials.next()) {
                System.out.println("fail");
                return new AdminLoginResponse("failure");
            } else {
                do {
                    System.out.println("success");
                    return new AdminLoginResponse("success");
                } while (adminCredentials.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
}
