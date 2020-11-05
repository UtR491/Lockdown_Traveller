import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelBookingController {
    // Button to send the request.
    @FXML
    public Button cancelBookingButton;
    // Go to home link.
    @FXML
    public Hyperlink homeLink;
    // Field to enter the pnr of the booking that has to be cancelled.
    @FXML
    public TextField pnrTextfield;
    // Home scene.
    private Scene homeScene = null;
    // User id of the logged in user.
    private String userId = null;
    // Popup to notify the user whether cancellation was successful or not.
    private Alert dialog = new Alert(Alert.AlertType.WARNING);

    /**
     * Triggered on clicking the Go to home link.
     * @param actionEvent
     */
    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    /**
     * Kind of a constructor.
     * @param homeScene Home scene.
     * @param userID Identifier of the logged in user.
     */
    public void initData(Scene homeScene, String userID) {
        this.homeScene = homeScene;
        this.userId = userID;
    }

    /**
     * Triggered on clicking the cancel booking button. Sends the request object, waits for the response and shows a
     * popup accordingly.
     * @param actionEvent
     */
    public void cancelBooking(ActionEvent actionEvent) {
        CancelBookingRequest cancelBookingRequest = new CancelBookingRequest(pnrTextfield.getText(), userId);
        Main.SendRequest(cancelBookingRequest);
        CancelBookingResponse cancelBookingResponse = (CancelBookingResponse) Main.ReceiveResponse();
        if(cancelBookingResponse.getResponse().equals("success")) {
            dialog.setAlertType(Alert.AlertType.CONFIRMATION);
            dialog.setContentText("Successfully cancelled the tickets.");
            dialog.setHeaderText("Success");
        } else {
            dialog.setAlertType(Alert.AlertType.ERROR);
            dialog.setContentText("Could not cancel the tickets. Either the PNR entered is wrong or the ticket is" +
                    "already cancelled");
            dialog.setHeaderText("Error");
        }
        dialog.show();
    }
}
