import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RemoveTrainController {
    @FXML
    public TextField trainIDTextfield;
    @FXML
    public Button removeButton;
    @FXML
    public Hyperlink homeLink;

    public Alert resultDialog = new Alert(Alert.AlertType.WARNING);

    public void removeTrain(ActionEvent actionEvent) {
        RemoveTrainsRequest removeTrainsRequest = new RemoveTrainsRequest(trainIDTextfield.getText());
        Main.SendRequest(removeTrainsRequest);
        RemoveTrainsResponse removeTrainsResponse = (RemoveTrainsResponse) Main.ReceiveResponse();
        if(removeTrainsResponse.getResponse().equals("success")) {
            resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
            resultDialog.setContentText("Successfully removed the train permanently.");
            resultDialog.setHeaderText("Success");
        }
        else {
            resultDialog.setAlertType(Alert.AlertType.WARNING);
            resultDialog.setContentText("Could not remove the train. Make sure your input is correct.");
            resultDialog.setHeaderText("Failure");
        }
        resultDialog.show();
    }

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
}
