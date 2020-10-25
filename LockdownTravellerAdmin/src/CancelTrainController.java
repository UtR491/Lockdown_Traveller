import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CancelTrainController {
    @FXML
    public DatePicker cancelTillDatePicker;
    @FXML
    public TextField trainIdTextfield;
    @FXML
    public Button cancelButton;
    @FXML
    public Hyperlink homeLink;

    public Alert resultDialog = new Alert(Alert.AlertType.CONFIRMATION);

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

    public void cancelTrain(ActionEvent actionEvent) {
        CancelTrainsRequest cancelTrainsRequest = new CancelTrainsRequest(trainIdTextfield.getText(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(cancelTillDatePicker.getValue()));
        Main.SendRequest(cancelTrainsRequest);
        CancelTrainsResponse cancelTrainsResponse = (CancelTrainsResponse) Main.ReceiveResponse();
        if(cancelTrainsResponse.getResponse().equals("success")) {
            resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
            resultDialog.setContentText("Successfully cancelled train till " + cancelTillDatePicker.getValue().toString());
            resultDialog.setHeaderText("Success");
        } else {
            resultDialog.setAlertType(Alert.AlertType.WARNING);
            resultDialog.setContentText("Could not cancel the train. Make sure your input is correct.");
            resultDialog.setHeaderText("Failure");
        }
        resultDialog.show();
    }
}
