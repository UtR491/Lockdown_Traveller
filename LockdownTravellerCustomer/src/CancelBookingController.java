import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelBookingController {
    @FXML
    public Button cancelBookingButton;
    @FXML
    public Hyperlink homeLink;
    @FXML
    public TextField pnrTextfield;
    private Scene homeScene = null;
    private String userId = null;
    private Alert dialog = new Alert(Alert.AlertType.WARNING);

    public void goToHome(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    public void initData(Scene homeScene, String userID) {
        this.homeScene = homeScene;
        this.userId = userID;
    }

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
