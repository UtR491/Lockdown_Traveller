import java.io.ObjectOutputStream;

public class RegisterRequestHandler extends Handler {
    DatabaseConnector db = null;
    RegisterRequest registerRequest = null;
    ObjectOutputStream oos = null;

    public RegisterRequestHandler(DatabaseConnector db, RegisterRequest registerRequest, ObjectOutputStream oos) {
        this.db=db;
        this.registerRequest=registerRequest;
        this.oos=oos;
    }
    @Override
    public void sendQuery(){
    }

}
