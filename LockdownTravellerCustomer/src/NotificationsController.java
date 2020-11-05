import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NotificationsController {
    // Go to home link.
    @FXML
    public Hyperlink homeLink;
    // Holders for new and read notifications respectively.
    @FXML
    public ListView<String> newNotificationHolder, seenNotificationHolder;
    // Instance of the loaded home screen during login time, this is needed because the home screen shows some details
    // related to the user it got from the server in the form of login response.
    private Scene homeScene;
    // To request the notification.
    private String userId;

    /**
     * Triggered on clicking the go to home button.
     * @param actionEvent
     */
    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    /**
     * Called just after the screen is loaded.
     * @param homeScene The home screen.
     * @param userId Identifier of the user logged in currently.
     * @param notificationResponse Response from the server for the particular user's notifications.
     */
    public void initData(Scene homeScene, String userId, NotificationResponse notificationResponse) {

        this.homeScene = homeScene;
        this.userId = userId;

        ArrayList<String> notifications = notificationResponse.getMessage();
        ArrayList<Integer> pendingStatus = notificationResponse.getPendingStatus();
        newNotificationHolder.setPadding(new Insets(10, 10, 10, 10));
        seenNotificationHolder.setPadding(new Insets(10, 10, 10, 10));
        int counter = 0;
        for(String notification : notifications) {
            if(pendingStatus.get(counter++) == 1) {
                newNotificationHolder.getItems().add(notification);
            } else {
                seenNotificationHolder.getItems().add(notification);
            }
        }
    }
}
