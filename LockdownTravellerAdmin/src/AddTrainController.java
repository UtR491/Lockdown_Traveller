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

public class AddTrainController {
    @FXML
    public TextField AC1CoachesTextfield, AC2CoachesTextfield, AC3CoachesTextfield, sleeperCoachesTextfield,
            numAC1SeatsTextfield, numAC2SeatsTextfield, numAC3SeatsTextfield, numSleeperSeatsTextfield,
            AC1RateTextfield, AC2RateTextfield, AC3RateTextfield, sleeperRateTextfield,
            trainIDTextfield, trainNameTextfield, daysRunningTextfield;
    @FXML
    public DatePicker addedTillDatepicker;
    @FXML
    public Hyperlink finalizeButton;
    @FXML
    public Button addStationButton;
    @FXML
    public Hyperlink homeLink;

    String trainId, trainName, daysRunning, AC1Coaches, AC2Coaches, AC3Coaches, sleeperCoaches, numAC1Seats, numAC2Seats,
            numAC3Seats, numSleeperSeats, AC1Rate, AC2Rate, AC3Rate, sleeperRate, date;

    public void goToStationScene(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTrainStation.fxml"));
        Stage stage = (Stage) addStationButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene =  new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Add Stations");

        AddTrainStationController addTrainStationController = loader.getController();
        addTrainStationController.initData(trainId, trainName, daysRunning, AC1Coaches, AC2Coaches, AC3Coaches,
                sleeperCoaches, numAC1Seats, numAC2Seats, numAC3Seats, numSleeperSeats, AC1Rate, AC2Rate, AC3Rate,
                sleeperRate, date);
    }

    public void assignInfo(ActionEvent actionEvent) {
        trainId = trainIDTextfield.getText();
        System.out.println("Train id is " + trainId);
        trainName = trainNameTextfield.getText();
        AC1Coaches = AC1CoachesTextfield.getText();
        AC2Coaches = AC2CoachesTextfield.getText();
        AC3Coaches = AC3CoachesTextfield.getText();
        sleeperCoaches = sleeperCoachesTextfield.getText();
        numAC1Seats = numAC1SeatsTextfield.getText();
        numAC2Seats = numAC2SeatsTextfield.getText();
        numAC3Seats = numAC3SeatsTextfield.getText();
        numSleeperSeats = numSleeperSeatsTextfield.getText();
        AC1Rate = AC1RateTextfield.getText();
        AC2Rate = AC2RateTextfield.getText();
        AC3Rate = AC3RateTextfield.getText();
        sleeperRate = sleeperRateTextfield.getText();
        daysRunning = daysRunningTextfield.getText();
        if(addedTillDatepicker.getValue() != null)
            date = addedTillDatepicker.getValue().toString();
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
