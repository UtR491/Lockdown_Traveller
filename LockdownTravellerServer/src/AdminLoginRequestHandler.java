import java.io.ObjectOutputStream;

public class AdminLoginRequestHandler extends Handler{
    AdminLoginRequest adminLoginRequest;
    ObjectOutputStream oos;
    DatabaseConnector db;
    public AdminLoginRequestHandler(AdminLoginRequest adminLoginRequest, ObjectOutputStream oos, DatabaseConnector db) {
        this.adminLoginRequest = adminLoginRequest;
        this.oos = oos;
        this.db = db;
    }

    @Override
    public void sendQuery() {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();
        String query = "select * from Admin where Username = '" + username + "' and Password = '" + password + "';";
        AdminLoginResponse adminLoginResponse = db.adminLoginRequest(query);
        Server.SendResponse(oos, adminLoginResponse);
    }
}
