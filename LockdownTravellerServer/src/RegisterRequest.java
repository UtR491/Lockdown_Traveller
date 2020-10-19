

import java.io.Serializable;

public class RegisterRequest extends Request implements Serializable {


    private final String firstName;
    private final String lastName;
    private final String emailID;
    private final String gender;
    private final String phone_number;
    private final int age;
    private final String username;
    private final String password;
    private final int userID;




    public RegisterRequest(String firstName, String emailID, String lastName, String phone_number, String gender, int age,
                           String username, String password, int userID) {
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

    public String getFirstName (){
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
    public int getUserID(){
        return this.userID;
    }
    public int getAge(){
        return this.age;
    }
}
