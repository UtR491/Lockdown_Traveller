import java.io.Serializable;
import java.util.ArrayList;

public class MaintainCustomerResponse extends Response implements Serializable {
    private final ArrayList<Customer> customers;
    public MaintainCustomerResponse(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}

class Customer implements Serializable{
    final private String name, gender, emailId, phone, username, userId;
    final private int age;
    public Customer() {
        this.name = "Customers";
        this.userId = "";
        this.emailId = "";
        this.phone = "";
        this.gender = "";
        this.username = "";
        this.age = 0;
    }
    public Customer(String name, String gender, String emailId, String phone, String username, String userId, int age) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.emailId = emailId;
        this.phone = phone;
        this.username = username;
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getEmailId() {
        return emailId;
    }
    public String getGender() {
        return gender;
    }
    public String getUserId() {
        return userId;
    }
    public int getAge() {
        return age;
    }
}
