import java.io.Serializable;

/**
 * Request class to register new user.
 */
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

    /**
     * Constructor to initialize the request object with the details entered by the user.
     * @param firstName First name of the user.
     * @param emailID Email ID of the user.
     * @param lastName Last name of the user.
     * @param phone_number Phone number of the user.
     * @param gender Gender of the user.
     * @param age Age of the user.
     * @param username Username of choice.
     * @param password Password of choice.
     * @param userID Randomly generated user id.
     */
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

    /**
     * Getter for the first name entered by the user.
     * @return First name.
     */
    public String getFirstName () {
        return this.firstName;
    }

    /**
     * Getter for the last name entered by the user.
     * @return Last name.
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     * Getter for the email id entered by the user.
     * @return Email ID.
     */
    public String getEmailID (){
        return this.emailID;
    }

    /**
     * Getter for the phone number entered by the user.
     * @return Phone number.
     */
    public String getPhone_number (){
        return this.phone_number;
    }

    /**
     * Getter for the gender chosen by the user.
     * @return Gender.
     */
    public String getGender (){
        return this.gender;
    }

    /**
     * Getter for the username entered by the user.
     * @return Username.
     */
    public String getUsername (){
        return this.username;
    }

    /**
     * Getter for the password entered by the user.
     * @return Password.
     */
    public String getPassword (){
        return this.password;
    }

    /**
     * Getter for the user id generated for the user.
     * User id is generated at the client side.
     * @return User ID.
     */
    public String getUserID(){
        return this.userID;
    }

    /**
     * Getter for the age entered by the user.
     * @return Age.
     */
    public int getAge(){
        return this.age;
    }
}
