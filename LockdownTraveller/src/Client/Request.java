package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Request implements Serializable {

    public static void SendRequest(ObjectOutputStream oos, Request o) {
        try {
            oos.writeObject(o);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetInfo() throws Exception {
        /*
        Override this method.
         */
    }
}
