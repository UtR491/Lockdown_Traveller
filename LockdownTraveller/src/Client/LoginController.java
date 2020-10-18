package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    ObjectInputStream objectInputStream=null;
    ObjectOutputStream objectOutputStream=null;

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
        LoginRequest loginRequest=new LoginRequest(usernameField.getText(), passwordField.getText());
        LoginRequest.SendRequest(objectOutputStream,loginRequest);


    }

    public void switchToSignup(ActionEvent actionEvent) {
        FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        Stage stage = (Stage) signupButton.getScene().getWindow();
        Scene signupScene = null;
        try {
            signupScene = new Scene(signupLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(signupScene);

    }

    public void initData (ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.objectOutputStream=objectOutputStream;
        this.objectInputStream=objectInputStream;
    }

    }


