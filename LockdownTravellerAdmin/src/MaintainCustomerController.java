import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MaintainCustomerController {
    public TreeTableView<Customer> customerList;
    public Hyperlink homeLink;

    public void goToHome(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
        Stage stage = (Stage) homeLink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Welcome Admin");
    }

    public void initData(MaintainCustomerResponse maintainCustomerResponse) {
        System.out.println("Inside init of maintain customer");
        if(maintainCustomerResponse == null) {
            System.out.println("The response is null");
        }
        TreeTableColumn<Customer, String> userID = new TreeTableColumn<>("User ID");
        TreeTableColumn<Customer, String> name = new TreeTableColumn<>("Name");
        TreeTableColumn<Customer, String> username = new TreeTableColumn<>("Username");
        TreeTableColumn<Customer, String> email = new TreeTableColumn<>("Email");
        TreeTableColumn<Customer, String> phone = new TreeTableColumn<>("Phone No.");
        TreeTableColumn<Customer, String> gender = new TreeTableColumn<>("Gender");
        TreeTableColumn<Customer, Number> age = new TreeTableColumn<>("Age");

        userID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getUserId()));
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        username.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getUsername()));
        email.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getEmailId()));
        phone.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getPhone()));
        gender.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getGender()));
        age.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, Number> p) ->
                new ReadOnlyIntegerWrapper(p.getValue().getValue().getAge()));

        customerList.getColumns().add(userID);
        customerList.getColumns().add(name);
        customerList.getColumns().add(username);
        customerList.getColumns().add(email);
        customerList.getColumns().add(phone);
        customerList.getColumns().add(gender);
        customerList.getColumns().add(age);
        TreeItem<Customer> rootNode = new TreeItem<>(new Customer());
        rootNode.setExpanded(true);
        ArrayList<Customer> customers = maintainCustomerResponse.getCustomers();
        for(Customer customer : customers) {
            TreeItem<Customer> customerLeaf = new TreeItem<>(customer);
            rootNode.getChildren().add(customerLeaf);
        }
        customerList.setRoot(rootNode);
    }
}
