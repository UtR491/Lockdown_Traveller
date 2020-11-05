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
    // Different buttons, for different features. Name explains what they do.
    @FXML
    public Button findTrainsButton, travelsButton, notificationButton, cancelBookingButton;
    // Logout link.
    @FXML
    public Hyperlink logoutLink;
    // Different labels to be set when logged in.
    @FXML
    public Label phoneLabel, emailLabel, nameLabel, usernameLabel, userIdLabel;
    private String userId, name, username, email, phone;
    private Scene homeScene;

    /**
     * Data recieved from the login screen after a successful login attempt. Here we set the labels too.
     * @param homeScene Home scene without the details set.
     * @param userId Identifier of the logged in user.
     * @param name Name of the logged in user.
     * @param username Username of the logged in user.
     * @param email Email of the logged in user.
     * @param phone Phone number of the logged in user.
     */
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

    /**
     * Triggered on clicking the logout button.
     * @param actionEvent
     */
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

    /**
     * Triggered on clicking the find trains button.
     * @param actionEvent
     */
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

    /**
     * Triggered on clicking the notifications button.
     * @param actionEvent
     */
    public void notifications(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Notifications.fxml"));
        Scene scene = null;
        Stage stage = (Stage) cancelBookingButton.getScene().getWindow();
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Notifications.");
        stage.setScene(scene);
        NotificationsController notificationsController = loader.getController();
        NotificationRequest notificationRequest = new NotificationRequest(userId);
        Main.SendRequest(notificationRequest);
        NotificationResponse notificationResponse = (NotificationResponse) Main.ReceiveResponse();
        notificationsController.initData(homeScene, userId, notificationResponse);
    }

    /**
     * Triggered on clicking the Cancel button.
     * @param actionEvent
     */
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

    /**
     * Triggered on clicking the travels button.
     * @param actionEvent
     */
    public void travels(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingHistory.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) logoutLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Your Travels");
        BookingHistoryRequest bookingHistoryRequest = new BookingHistoryRequest(userId);
        Main.SendRequest(bookingHistoryRequest);
        BookingHistoryResponse bookingHistoryResponse = (BookingHistoryResponse) Main.ReceiveResponse();
        BookingHistoryController bookingHistoryController = loader.getController();
        bookingHistoryController.initData(homeScene, bookingHistoryResponse);
    }
}
