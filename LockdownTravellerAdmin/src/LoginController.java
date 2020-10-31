import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label noAccountLabel, signinLabel;
    @FXML
    private AnchorPane signinPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login(ActionEvent actionEvent) {
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(usernameField.getText(), passwordField.getText());
        System.out.println("Send request");
        Main.SendRequest(adminLoginRequest);
        System.out.println("Wait for response");
        AdminLoginResponse adminLoginResponse = (AdminLoginResponse) Main.ReceiveResponse();
        assert adminLoginResponse != null;
        System.out.println("Wait over Response is " +adminLoginResponse.getStatus());
        if(adminLoginResponse.getStatus().equals("success")) {
            FXMLLoader landingPage = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            try {
                Scene landingPageScene = new Scene(landingPage.load());
                currentStage.setScene(landingPageScene);
                currentStage.setTitle("Welcome Admin");
            } catch (IOException e) {
                e.printStackTrace();
            }
            LandingPageController landingPageController = landingPage.getController();
            landingPageController.initData(adminLoginResponse.getAdminID());
        }
    }
}