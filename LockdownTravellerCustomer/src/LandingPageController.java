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
    public Button findTrainsButton, travelsButton, notificationButton, reroutedTrainsButton, cancelBookingButton,
            cancelledTrainsButton;
    @FXML
    public Hyperlink logoutLink, homeLink;
    @FXML
    public Label phoneLabel, emailLabel, nameLabel, usernameLabel, userIdLabel;
    public Hyperlink chatLink;
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
    public void reroutedTrains(ActionEvent actionEvent) {
        ViewReroutedTrainsRequest viewReroutedTrainsRequest = new ViewReroutedTrainsRequest();
        Main.SendRequest(viewReroutedTrainsRequest);
        ViewReroutedTrainsResponse viewReroutedTrainsResponse = (ViewReroutedTrainsResponse) Main.ReceiveResponse();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewReroutedTrains.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) logoutLink.getScene().getWindow();
        stage.setScene(scene);
        ViewReroutedTrainsController viewReroutedTrainsController = loader.getController();
        viewReroutedTrainsController.initData(homeScene, viewReroutedTrainsResponse);
    }

    public void cancelledTrains(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCancelledTrains.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) logoutLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("View Cancelled Trains");
        ViewCancelledTrainsController viewCancelledTrainsController = loader.getController();
        viewCancelledTrainsController.initData(homeScene);
    }

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

    public void chat(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) logoutLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Contact Us");
        ChatController chatController = loader.getController();
        chatController.initData(homeScene, userId);
    }
}
