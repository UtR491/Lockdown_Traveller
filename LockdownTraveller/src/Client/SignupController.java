package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField lastNameField, firstNameField, emailField, mobileField, usernameField;
    @FXML
    private AnchorPane signupPane;
    @FXML
    private Label signupLabel, orLabel, mobileInvalidLabel, passwordMismatchLabel;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private ComboBox<String> stationComboBox, genderComboBox;
    @FXML
    private PasswordField confirmPasswordField, passwordField;
    @FXML
    private Button signupButton, signinButton;

    public void signup(ActionEvent actionEvent) {

    }

    public void switchToSignin(ActionEvent actionEvent) {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) signinButton.getScene().getWindow();
        Scene loginScene = null;
        try {
            loginScene = new Scene(loginLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(loginScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void validateMobile(javafx.scene.input.KeyEvent keyEvent) {
        if(mobileField.getText().length()==10 && mobileField.getText().matches("[0-9]+"))
            mobileInvalidLabel.setText("");
        else
            mobileInvalidLabel.setText("Invalid mobile number.");
    }

    public void validatePassword(javafx.scene.input.KeyEvent keyEvent) {
        if(null != passwordField.getText() && confirmPasswordField.getText() != null && passwordField.getText().equals(confirmPasswordField.getText()))
            passwordMismatchLabel.setText("");
        else
            passwordMismatchLabel.setText("Passwords don't match!");
    }
}
