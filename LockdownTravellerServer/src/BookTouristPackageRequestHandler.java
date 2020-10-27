

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class BookTouristPackageRequestHandler extends Handler {
    Connection connection;
    ObjectOutputStream oos;
    BookTouristPackageRequest bookTouristPackageRequest;

    public BookTouristPackageRequestHandler(Connection connection, ObjectOutputStream oos, BookTouristPackageRequest bookTouristPackageRequest) {
        this.connection = connection;
        this.oos = oos;
        this.bookTouristPackageRequest = bookTouristPackageRequest;
    }

    @Override
    void sendQuery() throws IOException, SQLException {

    }
}
