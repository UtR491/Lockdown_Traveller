
import java.io.IOException;
import java.sql.SQLException;

abstract class Handler {
    void sendQuery() throws IOException, SQLException {}
}