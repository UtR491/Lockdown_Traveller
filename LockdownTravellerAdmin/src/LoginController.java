import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // Username field for login.
    @FXML
    private TextField usernameField;
    // Password field for login.
    @FXML
    private PasswordField passwordField;
    // Button to trigger initialization and sending of the login request object.
    @FXML
    private Button loginButton;

    /**
     * Triggered on clicking the login button.
     * @param actionEvent
     */
    public void login(ActionEvent actionEvent) {
        // Login request.
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(usernameField.getText(), passwordField.getText());
        // Send the login request object.
        System.out.println("Send request");
        Main.SendRequest(adminLoginRequest);
        // Read the login response.
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