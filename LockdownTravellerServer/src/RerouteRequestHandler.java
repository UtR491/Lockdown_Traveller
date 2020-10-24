import java.io.ObjectOutputStream;
import java.sql.Connection;

public class RerouteRequestHandler extends Handler {
    private ObjectOutputStream objectOutputStream;
    private RerouteRequest rerouteRequest;
    private  Connection connection;
    RerouteRequestHandler(Connection connection,RerouteRequest rerouteRequest,ObjectOutputStream objectOutputStream)
    {
        this.connection=connection;
        this.objectOutputStream=objectOutputStream;
        this.rerouteRequest=rerouteRequest;
    }

    @Override
    void sendQuery()  {

    }
    public RerouteResponse rerouteTrains()
    {
        String response = null;
        return new RerouteResponse(response);
    }

}
