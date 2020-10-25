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
        System.out.println("maintain customer info clicked");
        MaintainCustomerRequest maintainCustomerRequest = new MaintainCustomerRequest();
        System.out.println("maintain customer rquest sending");
        Main.SendRequest(maintainCustomerRequest);
        System.out.println("waiting for maintain customer response");
        MaintainCustomerResponse maintainCustomerResponse = (MaintainCustomerResponse) Main.ReceiveResponse();
        System.out.println("wait for maintain customer response over ");
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
        MaintainCustomerController maintainCustomerController = loader.getController();
        maintainCustomerController.initData(maintainCustomerResponse);
    }
    public void maintainTrains(ActionEvent actionEvent) {
        System.out.println("Maintain trains pushed");
        MaintainTrainsRequest maintainTrainsRequest = new MaintainTrainsRequest();
        System.out.println("Maintain trains request being sent");
        Main.SendRequest(maintainTrainsRequest);
        System.out.println("Waiting for maintain trains response");
        MaintainTrainsResponse maintainTrainsResponse = (MaintainTrainsResponse) Main.ReceiveResponse();
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
        MaintainTrainsController maintainTrainsController = loader.getController();
        maintainTrainsController.initData(maintainTrainsResponse);
    }
    public void maintainSeats(ActionEvent actionEvent) {
        MaintainSeatsRequest maintainSeatsRequest = new MaintainSeatsRequest();
        Main.SendRequest(maintainSeatsRequest);
        MaintainSeatsResponse maintainSeatsResponse = (MaintainSeatsResponse) Main.ReceiveResponse();

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

        MaintainSeatsController maintainSeatsController = loader.getController();
        maintainSeatsController.initData(maintainSeatsResponse);
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
