import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {

    @FXML
    public Button findTrainsButton, historyButton, notificationButton, upcomingTravelsButton, cancelBookingButton;
    @FXML
    public Hyperlink logoutLink, homeLink;
    @FXML
    public Label phoneLabel, emailLabel, nameLabel, usernameLabel, userIdLabel;
    private String userId, name, username, email, phone;
    private Scene homeScene;
    public void initData(Scene homeScene, String userId, String name, String username, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.homeScene = homeScene;
        userIdLabel.setText(this.userId);
        usernameLabel.setText(this.username);
        nameLabel.setText(this.name + "!");
        phoneLabel.setText(this.phone);
        emailLabel.setText(this.email);
    }

    public void logout(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = null;
        Stage stage = (Stage) findTrainsButton.getScene().getWindow();
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Sign in");
    }

    public void displayTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayTrains.fxml"));
        Scene scene = null;
        Stage stage = (Stage) cancelBookingButton.getScene().getWindow();
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Find Trains");
        stage.setScene(scene);
        DisplayTrainsController displayTrainsController = loader.getController();
        displayTrainsController.initData(homeScene, userId);
    }
    public void history(ActionEvent actionEvent) {
    }
    public void notifications(ActionEvent actionEvent) {
    }
    public void cancelBooking(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CancelBooking.fxml"));
        Scene scene = null;
        Stage stage = (Stage) cancelBookingButton.getScene().getWindow();
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Cancel Booking");
        stage.setScene(scene);
        CancelBookingController cancelBookingController = loader.getController();
        cancelBookingController.initData(homeScene, userId);
    }
    public void upcomingTravels(ActionEvent actionEvent) {
    }
}
