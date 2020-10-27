import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterRequestHandler extends Handler {
    RegisterRequest registerRequest;
    Connection connection;
    ObjectOutputStream oos;

    public RegisterRequestHandler(Connection connection, RegisterRequest registerRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.registerRequest=registerRequest;
        this.oos=oos;
    }
    @Override
    public void sendQuery(){
        String query1 = "insert into User(User_ID, First_Name, Last_Name, Gender, Age, Email_ID, Phone_No, Username," +
                "Password, Total_Spend) values (?, ?, ?, ?, ?, ?, ?, ?, ?, 0);";
        String query2 = "select count(Username) from User where Username = ?;";
        RegisterResponse registerResponse = registerRequest(query1, query2);
        Server.SendResponse(oos, registerResponse);
    }

    private RegisterResponse registerRequest(String query1, String query2) {
        try {
            PreparedStatement usernameAvailability = connection.prepareStatement(query2);
            usernameAvailability.setString(1, registerRequest.getUsername());
            ResultSet usernameCount = usernameAvailability.executeQuery();
            while(usernameCount.next()) {
                if(usernameCount.getInt(1) == 1) {
                    return new RegisterResponse("Username taken");
                }
            }
            PreparedStatement addUser = connection.prepareStatement(query1);
            addUser.setString(1,registerRequest.getUserID());
            addUser.setString(2, registerRequest.getFirstName());
            addUser.setString(3, registerRequest.getLastName());
            addUser.setString(4, String.valueOf(registerRequest.getGender().charAt(0)));
            addUser.setInt(5, registerRequest.getAge());
            addUser.setString(6, registerRequest.getEmailID());
            addUser.setString(7, registerRequest.getPhone_number());
            addUser.setString(8, registerRequest.getUsername());
            addUser.setString(9, registerRequest.getPassword());
            int c = addUser.executeUpdate();
            if(c != 0) {
                return new RegisterResponse("success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new RegisterResponse("failure");
    }
}
