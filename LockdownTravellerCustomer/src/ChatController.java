import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatController {
    @FXML
    public TextArea messageField;
    @FXML
    public Button sendButton;
    @FXML
    public Hyperlink homeLink;
    @FXML
    public ListView<String> messageHolder;

    private Scene homeScene;

    public void initData(Scene homeScene, String userId) {
        this.homeScene = homeScene;
    }

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

}
