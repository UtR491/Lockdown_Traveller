package Server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public abstract class Response implements Serializable {
    public static void SendResponse(ObjectOutputStream oos, Response server) {
        try {
            oos.writeObject(server);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
