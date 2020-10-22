import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {
    @FXML
    public Hyperlink maintainCustomerHyperlink,
            maintainTrainsHyperlink,
            maintainSeatsHyperlink,
            addTrainsHyperlink,
            removeTrainsHyperlink,
            modifySeatsHyperlink,
            cancelTrainsHyperlink,
            rerouteTrainHyperlink;

    public void maintainCustomer(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MaintainCustomer.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Customer Information");
    }
    public void maintainTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MaintainTrains.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Train Information");
    }
    public void maintainSeats(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MaintainSeats.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Seat Information");
    }
    public void addTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTrain.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Add Train");
    }
    public void removeTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoveTrain.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Remove Train");
    }
    public void modifySeats(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifySeats.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Modify Seat");
    }
    public void cancelTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelTrain.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Cancel Train");
    }

    public void rerouteTrain(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RerouteTrain.fxml"));
        Stage stage = (Stage) maintainCustomerHyperlink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Reroute Train");
    }
}
