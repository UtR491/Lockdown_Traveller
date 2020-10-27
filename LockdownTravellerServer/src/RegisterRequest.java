import java.io.Serializable;

public class RegisterRequest extends Request implements Serializable {

    final private String firstName;
    final private String lastName;
    final private String emailID;
    final private String gender;
    final private String phone_number;
    final private int age;
    final private String username;
    final private String password;
    final private String userID;

    public RegisterRequest(String firstName, String emailID, String lastName, String phone_number, String gender, int age,
                           String username, String password, String userID) {
        this.age=age;
        this.userID=userID;
        this.emailID=emailID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.phone_number=phone_number;
        this.username=username;
        this.gender=gender;
    }

    public String getFirstName () {
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmailID (){
        return this.emailID;
    }
    public String getPhone_number (){
        return this.phone_number;
    }
    public String getGender (){
        return this.gender;
    }
    public String getUsername (){
        return this.username;
    }
    public String getPassword (){
        return this.password;
    }
    public String getUserID(){
        return this.userID;
    }
    public int getAge(){
        return this.age;
    }
}
