import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login(ActionEvent actionEvent) {
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(usernameField.getText(), passwordField.getText());
    }

    public void initData (ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.objectOutputStream=objectOutputStream;
        this.objectInputStream=objectInputStream;
    }

}


