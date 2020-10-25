import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AddTrainStationController {
    @FXML
    public GridPane root;
    @FXML
    public Hyperlink homeLink;
    @FXML
    public Button addTrainButton;
    Alert dialog = new Alert(Alert.AlertType.WARNING);
    @FXML
    public Hyperlink addStationLink;

    ArrayList<RouteRow> stations = new ArrayList<>();

    String trainId, trainName, daysRunning, AC1Coaches, AC2Coaches, AC3Coaches, sleeperCoaches, numAC1Seats, numAC2Seats,
    numAC3Seats, numSleeperSeats, AC1Rate, AC2Rate, AC3Rate, sleeperRate, date;
/*
    trainIDTextfield.getText(), trainNameTextfield.getText(),
            daysRunningTextfield.getText(), AC1CoachesTextfield.getText(), AC2CoachesTextfield.getText(),
            AC3CoachesTextfield.getText(), sleeperCoachesTextfield.getText(), numAC1SeatsTextfield.getText(),
            numAC2SeatsTextfield.getText(), numAC3SeatsTextfield.getText(), numSleeperSeatsTextfield.getText(),
            AC1RateTextfield.getText(), AC2RateTextfield.getText(), AC3CoachesTextfield.getText(),
            sleeperRateTextfield.getText(), addedTillDatepicker.getValue().toString(),
*/

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

    public void addStation(ActionEvent actionEvent) {
        root.setVgap(20);
        stations.add(new RouteRow());
        root.add(stations.get(stations.size()-1).getHBox(), 0, stations.size()+1);
    }

    public void sendRequestButton(ActionEvent actionEvent) {
        System.out.println("Send Request button clicked");
        ArrayList<String> station = new ArrayList<>(),
                cityCode = new ArrayList<>(),
                arrival = new ArrayList<>(),
                departure = new ArrayList<>(),
                dayNo = new ArrayList<>(),
                distanceCovered = new ArrayList<>();
        for(RouteRow element : stations) {
            station.add(element.stationName.getText());
            cityCode.add(element.cityCode.getText());
            arrival.add(element.arrival.getText());
            departure.add(element.departure.getText());
            dayNo.add(element.day.getText());
            distanceCovered.add(element.distanceCovered.getText());
        }
        System.out.println("Train id before passing to add trains " + trainId);
        AddTrainsRequest addTrainsRequest = new AddTrainsRequest(trainId, trainName, daysRunning, AC1Coaches, AC2Coaches,
                AC3Coaches, sleeperCoaches, numAC1Seats, numAC2Seats, numAC3Seats, numSleeperSeats, AC1Rate, AC2Rate,
                AC3Rate, sleeperRate, date, station, cityCode, arrival, departure, dayNo, distanceCovered);
        //Printing the reuqest object.
        System.out.println("Details being sent on the server side");
        System.out.println(addTrainsRequest.getTrain_Name()+" "+addTrainsRequest.getTrain_ID()+" "+addTrainsRequest.getFirstAC_Coaches()
        + " " + addTrainsRequest.getStation().get(0));
        //Done
        Main.SendRequest(addTrainsRequest);
        AddTrainsResponse addTrainsResponse = (AddTrainsResponse) Main.ReceiveResponse();
        if(addTrainsResponse.getResponse().equals("success")) {
            System.out.println("Train was added!!");
            dialog.setAlertType(Alert.AlertType.CONFIRMATION);
            dialog.setContentText("The train was added successfully.");
            dialog.setHeaderText("Success");
        } else {
            dialog.setAlertType(Alert.AlertType.ERROR);
            dialog.setContentText("Could not add the train");
            dialog.setHeaderText("Error");
        }
        dialog.show();
    }

    public void initData(String trainId, String trainName, String daysRunning, String ac1Coaches, String ac2Coaches, String ac3Coaches, String sleeperCoaches, String numAC1Seats, String numAC2Seats, String numAC3Seats, String numSleeperSeats, String ac1Rate, String ac2Rate, String ac3Rate, String sleeperRate, String date) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.daysRunning = daysRunning;
        this.AC1Coaches = ac1Coaches;
        this.AC2Coaches = ac2Coaches;
        this.AC3Coaches = ac3Coaches;
        this.sleeperCoaches = sleeperCoaches;
        this.numAC1Seats = numAC1Seats;
        this.numAC2Seats = numAC2Seats;
        this.numAC3Seats = numAC3Seats;
        this.numSleeperSeats = numSleeperSeats;
        this.AC1Rate = ac1Rate;
        this.AC2Rate = ac2Rate;
        this.AC3Rate = ac3Rate;
        this.sleeperRate = sleeperRate;
        this.date = date;
    }
}

class RouteRow {
    TextField stationName, stationNumber, cityCode, arrival, departure, day, distanceCovered;
    public RouteRow() {
        stationName = new TextField();
        stationName.setPromptText("Station Name");
        stationName.setMinWidth(180);

        stationNumber = new TextField();
        stationNumber.setPromptText("Order");
        stationNumber.setMinWidth(45);

        cityCode = new TextField();
        cityCode.setPromptText("Code");
        cityCode.setMinWidth(70);

        arrival = new TextField();
        arrival.setPromptText("Arr.");
        arrival.setMinWidth(45);

        departure = new TextField();
        departure.setPromptText("Dep.");
        departure.setMinWidth(45);

        day = new TextField();
        day.setPromptText("Day");
        day.setMinWidth(45);

        distanceCovered = new TextField();
        distanceCovered.setPromptText("Dist.");
        distanceCovered.setMinWidth(45);
    }

    public HBox getHBox() {
        HBox unit = new HBox();
        unit.setSpacing(3);
        unit.setPadding(new Insets(0, 10, 0, 10));
        unit.getChildren().addAll(stationName, cityCode, stationNumber, arrival, departure, day, distanceCovered);
        return unit;
    }
}
