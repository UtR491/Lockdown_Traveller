import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DisplayTrainsController {

    // TextFields for source and destination.
    @FXML
    public TextField destinationTextfield, sourceTextfield;

    // DatePicker to choose date of journey.
    @FXML
    public DatePicker dateDatePicker;

    // Button send find trains request.
    @FXML
    public Button findTrainsButton;

    // Hyperlink to go to home.
    @FXML
    public Hyperlink homeLink;

    // Stores home screen state because it contains some information about the user.
    private Scene homeScene;

    private String userID;

    /**
     * Triggered on clicking the Find Trains button.
     * @param actionEvent
     */
    public void sendRequest (ActionEvent actionEvent){

        DisplayTrainsRequest displayTrainsRequest = new DisplayTrainsRequest(sourceTextfield.getText(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateDatePicker.getValue()),
                destinationTextfield.getText());
        Main.SendRequest(displayTrainsRequest);

        DisplayTrainsResponse displayTrainsResponse = (DisplayTrainsResponse) Main.ReceiveResponse();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TrainsList.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Available Trains");
        TrainsListController trainsListController = loader.getController();
        trainsListController.initData(homeScene, displayTrainsResponse, userID, sourceTextfield.getText(),
                destinationTextfield.getText(), dateDatePicker.getValue().toString());

    }

    /**
     * Triggered on clicking the Go to home link.
     * @param actionEvent
     */
    public void goToHome (ActionEvent actionEvent){
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    /**
     * Can be called a pseudo-constructor maybe?
     * @param homeScene Home scene.
     * @param userID Unique identifier for the user logged in.
     */
    public void initData (Scene homeScene, String userID){
        this.homeScene = homeScene;
        this.userID = userID;
    }
}

