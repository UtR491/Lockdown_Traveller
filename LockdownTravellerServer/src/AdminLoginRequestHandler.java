import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Properties;

public class AdminLoginRequestHandler extends Handler{
    AdminLoginRequest adminLoginRequest;
    ObjectOutputStream oos;
    public AdminLoginRequestHandler(AdminLoginRequest adminLoginRequest, ObjectOutputStream oos) {
        this.adminLoginRequest = adminLoginRequest;
        this.oos = oos;
    }
    public void formQuery() {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream("./src/db.properties")));
            if(properties.getProperty("admin").equals(adminLoginRequest.getUsername())
            && properties.getProperty("adminPass").equals(adminLoginRequest.getPassword())) {
                Server.SendResponse(oos, new AdminLoginResponse("success"));
            }
            else {
                Server.SendResponse(oos, new AdminLoginResponse("failure"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
