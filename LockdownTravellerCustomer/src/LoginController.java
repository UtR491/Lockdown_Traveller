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
    private Button loginButton, signupButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login(ActionEvent actionEvent) {
        System.out.println("Creating a login request object in thread " + Thread.currentThread());
        LoginRequest loginRequest=new LoginRequest(usernameField.getText(), passwordField.getText());
        System.out.println("Sending the object in thread " + Thread.currentThread());
        Main.SendRequest(loginRequest);

        try {
            System.out.println("Waiting for response");
            LoginResponse loginResponse = (LoginResponse) Main.ReceiveResponse();
            System.out.println("Wait over");
            if(loginResponse.getUserId() == null) {
                System.out.println("Wrong info given");
                //Prompt the user that the input is wrong.
            } else {
                String userId = loginResponse.getUserId();
                System.out.println("User id " + userId);
                FXMLLoader landingPageLoader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                Scene landingPageScene = new Scene(landingPageLoader.load());
                currentStage.setScene(landingPageScene);
                currentStage.setTitle("Welcome");
                LandingPageController landingPage = landingPageLoader.getController();
                landingPage.initData(landingPageScene, userId, loginResponse.getName(), loginResponse.getUsername(), loginResponse.getEmail(),
                        loginResponse.getPhone());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToSignup(ActionEvent actionEvent) {
        FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Stage stage = (Stage) signupButton.getScene().getWindow();
        Scene signupScene = null;
        try {
            signupScene = new Scene(signupLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(signupScene);
        RegisterController registerController = signupLoader.getController();
        registerController.executeFirst();
    }
}


