package Server;

import Client.BookingRequest;
import Client.RegisterRequest;

import java.io.ObjectOutputStream;

public class RegisterRequestHandler {
    DatabaseConnector db = null;
    RegisterRequest registerRequest = null;
    ObjectOutputStream oos = null;

    public RegisterRequestHandler(DatabaseConnector db, RegisterRequest registerRequest, ObjectOutputStream oos) {
        this.db=db;
        this.registerRequest=registerRequest;
        this.oos=oos;
    }
    public void formQuery(){
    }

}
