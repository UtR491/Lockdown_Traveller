import java.io.Serializable;
import java.util.ArrayList;

public class MaintainCustomerResponse extends Response implements Serializable {

    private final ArrayList<Customer> customers;

    /**
     * Constructor to initialize the Response object.
     * @param customers Stores details of all the customers.
     */
    public MaintainCustomerResponse(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    /**
     * Getter for the customer information.
     * @return Customers array.
     */
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}

/**
 * Customer class. Holds all the information about a customer. Customer is someone who has signed up using the
 * application.
 */
class Customer implements Serializable{

    final private String name, gender, emailId, phone, username, userId;
    final private int age;

    /**
     * Constructor use to initialize the root node of the TreeTableView.
     */
    public Customer() {
        this.name = "Customers";
        this.userId = "";
        this.emailId = "";
        this.phone = "";
        this.gender = "";
        this.username = "";
        this.age = 0;
    }

    /**
     * Constructor to initialize the leaf nodes of the TreeTableView.
     * @param name
     * @param gender
     * @param emailId
     * @param phone
     * @param username
     * @param userId
     * @param age
     */
    public Customer(String name, String gender, String emailId, String phone, String username, String userId, int age) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.emailId = emailId;
        this.phone = phone;
        this.username = username;
        this.userId = userId;
    }

    /**
     * Getter for customer phone number.
     * @return Phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for the customer username.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the customer name.
     * @return Customer name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for customer email id.
     * @return Email ID.
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Getter for the customer gender.
     * @return Gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Getter for the customer User ID.
     * @return User ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Getter for the customer age.
     * @return Age.
     */
    public int getAge() {
        return age;
    }
}
