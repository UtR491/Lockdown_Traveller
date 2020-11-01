import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RerouteTrainController {
    @FXML
    public TextField trianIdTextfield, newStationTextfield, arrivalTextfield, departureTextfield, dayTextfield,
            cityCodeTextfield, currentDistanceTextfield, previousDistanceTextfield, nextDistanceTextfield,
            currentStationTextfield, previousStationTextfield, nextStationTextfield, nextStationArrival,
            currentStationDeparture;
    @FXML
    public ComboBox<String> typeComboBox;
    @FXML
    public Button rerouteButton;
    @FXML
    public DatePicker dateDatePicker;
    @FXML
    public Hyperlink homeLink;

    Alert resultDialog = new Alert(Alert.AlertType.CONFIRMATION);

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

    public void sendRequest(ActionEvent actionEvent) {
//        RerouteRequest rerouteRequest = new RerouteRequest(
//                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateDatePicker.getValue()),
//                trianIdTextfield.getText(), newStationTextfield.getText(), arrivalTextfield.getText(),
//                departureTextfield.getText(), cityCodeTextfield.getText(), Integer.parseInt(dayTextfield.getText()),
//                Integer.parseInt(previousDistanceTextfield.getText()),
//                Integer.parseInt(currentDistanceTextfield.getText()), Integer.parseInt(nextDistanceTextfield.getText()),
//                currentStationTextfield.getText(), previousStationTextfield.getText(), nextStationTextfield.getText(),
//                typeComboBox.getValue().equals("Replace the current station with new station"));
//
        RerouteRequest rerouteRequest = new RerouteRequest(
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateDatePicker.getValue()),
                trianIdTextfield.getText(), newStationTextfield.getText(), cityCodeTextfield.getText(),
                Integer.parseInt(previousDistanceTextfield.getText()), Integer.parseInt(currentDistanceTextfield.getText()),
                Integer.parseInt(nextDistanceTextfield.getText()), currentStationTextfield.getText(),
                previousStationTextfield.getText(), nextStationTextfield.getText(),
                typeComboBox.getValue().equals("Replace the current station with new station"), arrivalTextfield.getText(),
                departureTextfield.getText(), nextStationArrival.getText(), currentStationDeparture.getText(),
                Integer.parseInt(dayTextfield.getText()));
        System.out.println(dateDatePicker.getValue().toString());
        Main.SendRequest(rerouteRequest);
        RerouteResponse rerouteResponse = (RerouteResponse) Main.ReceiveResponse();

            if(rerouteResponse.getResponse().equals("SUCCESS")) {
            resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
            resultDialog.setContentText("Rerouted the train successfully.");
            resultDialog.setHeaderText("Success");
        }
        else {
            resultDialog.setAlertType(Alert.AlertType.WARNING);
            resultDialog.setContentText("Could not reroute the train.");
            resultDialog.setHeaderText("Failure");
        }
        resultDialog.show();

    }
}
