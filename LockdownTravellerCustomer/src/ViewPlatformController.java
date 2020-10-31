import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ViewPlatformController {
    @FXML
    public TextField trainIdTextfield;
    @FXML
    public Button checkPlatform;
    @FXML
    public Hyperlink homeLink;
    @FXML
    public ListView<Label> holder;
    private Scene homeScene;
    private String userId;
    public void initData(Scene homeScene, String userId) {
        this.homeScene = homeScene;
        this.userId = userId;
    }

    public void sendRequest(ActionEvent actionEvent) {
        ViewPlatformRequest viewPlatformRequest = new ViewPlatformRequest(trainIdTextfield.getText());
        Main.SendRequest(viewPlatformRequest);
        ViewPlatformResponse viewPlatformResponse = (ViewPlatformResponse) Main.ReceiveResponse();
        final int n = viewPlatformResponse.getStation().size();
        for(int i = 0; i < n; i++) {
            Label label = new Label("Station : " + viewPlatformResponse.getStation().get(i) +
                    " Platform Number : " + viewPlatformResponse.getPlatformNo().get(i));
            holder.getItems().add(label);
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }
}
