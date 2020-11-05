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
    // Fields to enter the destination and source respectively.
    @FXML
    public TextField destinationTextfield, sourceTextfield;
    // Choose the date of journey.
    @FXML
    public DatePicker dateDatePicker;
    // The button that triggers the function to send the request and get the response
    @FXML
    public Button findTrainsButton;
    // Go to home link.
    @FXML
    public Hyperlink homeLink;
    // Home scene.
    private Scene homeScene;
    // User id of the logged in user.
    private String userID;

    /**
     * Triggered on clicking the find trains button.
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
     * Gets the home screen and user id from the previous screen.
     * @param homeScene Home screen state.
     * @param userID Identifier of the logged in user.
     */
    public void initData (Scene homeScene, String userID){
        this.homeScene = homeScene;
        this.userID = userID;
    }
}

