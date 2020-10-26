import java.io.Serializable;
import java.sql.ResultSet;

public class ViewCancelledTrainsResponse extends Response implements Serializable {
    ResultSet resultSet;

    public ViewCancelledTrainsResponse(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
}
