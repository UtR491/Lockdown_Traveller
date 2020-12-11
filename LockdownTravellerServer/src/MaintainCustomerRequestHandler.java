import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainCustomerRequestHandler extends Handler{

    final private Connection connection;
    final private ObjectOutputStream oos;
    final private MaintainCustomerRequest request;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param request The booking request object with relevant information.
     * @param oos The output stream to send the object.
     */
    public MaintainCustomerRequestHandler(ObjectOutputStream oos, Connection connection, MaintainCustomerRequest request) {
        System.out.println("Constructor of Maintain Customer Request Handler");
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    /**
     * This is the first function that is called inside the request identifier. In this function, we form the relevant
     * sql queries and pass it to another function to execute.
     */
    @Override
    public void sendQuery() {
        // Get all details about all users.
        String query = "select * from User;";
        MaintainCustomerResponse maintainCustomerResponse = maintainCustomerRequest(query);
        Server.SendResponse(oos, maintainCustomerResponse);
    }

    /**
     * Execute the query.
     * @param query Query to get all user info.
     * @return Response to maintain customer request.
     */
    private @Nullable MaintainCustomerResponse maintainCustomerRequest(String query) {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement customerQuery = connection.prepareStatement(query);
            ResultSet customerInfo = customerQuery.executeQuery();
            while(customerInfo.next()) {
                @SuppressWarnings("assignment.type.incompatible") // these columns cannot be null in the table.
                @NonNull String name = customerInfo.getString("First_Name") + " " +
                        customerInfo.getString("Last_Name"),
                        gender = customerInfo.getString("Gender"),
                        email = customerInfo.getString("Email_ID"),
                        phone = customerInfo.getString("Phone_No"),
                        username = customerInfo.getString("Username"),
                        userId = customerInfo.getString("User_ID");

                customers.add(new Customer(
                        name, gender, email, phone, username, userId, customerInfo.getInt("Age")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new MaintainCustomerResponse(customers);
    }
}
