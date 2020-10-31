import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class RegisterController {
    @FXML
    private TextField lastNameField, firstNameField, emailField, mobileField, usernameField, stationTextfield;
    @FXML
    private AnchorPane signupPane;
    @FXML
    private Label signupLabel, orLabel, mobileInvalidLabel, passwordMismatchLabel;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private PasswordField confirmPasswordField, passwordField;
    @FXML
    private Button signupButton, signinButton;

    Alert dialog = new Alert(Alert.AlertType.WARNING);

    public void signup(ActionEvent actionEvent) {
        LocalDate today = LocalDate.now();
        String userId = Main.randomIDGenerator();
        int age = Period.between(dobDatePicker.getValue(), today).getYears();
        RegisterRequest registerRequest=new RegisterRequest(firstNameField.getText(), emailField.getText(),
                lastNameField.getText(), mobileField.getText(), genderComboBox.getValue(), age, usernameField.getText(),
                EncryptPassword.getHash(passwordField.getText()), userId);
        Main.SendRequest(registerRequest);
        RegisterResponse registerResponse = (RegisterResponse) Main.ReceiveResponse();
        if(registerResponse.getResponse().equals("success")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
            Stage stage = (Stage) signupButton.getScene().getWindow();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Welcome");
            stage.setScene(scene);
            LandingPageController landingPageController = loader.getController();
            landingPageController.initData(scene, userId, firstNameField.getText() + " " + lastNameField.getText(),
                    usernameField.getText(), emailField.getText(), mobileField.getText());
        } else if(registerResponse.getResponse().equals("Username taken")) {
            dialog.setAlertType(Alert.AlertType.WARNING);
            dialog.setHeaderText("Username Taken");
            dialog.setContentText("Username has already been taken.");
            dialog.show();
        } else {
            dialog.setAlertType(Alert.AlertType.ERROR);
            dialog.setHeaderText("Unexpected Error");
            dialog.setContentText("Some unexpected error occurred");
            dialog.show();
        }
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

    public void executeFirst() {
        genderComboBox.getItems().addAll("Male", "Female", "Other");
    }
}
