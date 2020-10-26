import java.io.ObjectOutputStream;
import java.sql.Connection;

public class RegisterRequestHandler extends Handler {
    RegisterRequest registerRequest = null;
    Connection connection;
    ObjectOutputStream oos = null;

    public RegisterRequestHandler(Connection connection, RegisterRequest registerRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.registerRequest=registerRequest;
        this.oos=oos;
    }
    @Override
    public void sendQuery(){
    }

}
