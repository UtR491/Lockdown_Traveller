import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NotificationsController {
    @FXML
    public Hyperlink homeLink;
    @FXML
    public ListView<String> newNotificationHolder, seenNotificationHolder;
    private Scene homeScene;
    private String userId;
    private NotificationResponse notificationResponse;

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    public void initData(Scene homeScene, String userId, NotificationResponse notificationResponse) {
        this.homeScene = homeScene;
        this.userId = userId;
        this.notificationResponse = notificationResponse;

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
