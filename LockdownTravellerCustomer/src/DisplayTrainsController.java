import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DisplayTrainsController {
    @FXML
    public TextField destinationTextfield, sourceTextfield;
    @FXML
    public DatePicker dateDatePicker;
    @FXML
    public Button findTrainsButton;
    @FXML
    public Hyperlink homeLink;

    private Scene homeScene;
        public void sendRequest (ActionEvent actionEvent){
            DisplayTrainsRequest displayTrainsRequest = new DisplayTrainsRequest(sourceTextfield.getText(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateDatePicker.getValue()),
                    destinationTextfield.getText());
            Main.SendRequest(displayTrainsRequest);
            DisplayTrainsResponse displayTrainsResponse = (DisplayTrainsResponse) Main.ReceiveResponse();
            for (String trainID : displayTrainsResponse.getTrain_ID())
                System.out.println("Train ID - " + trainID);
        }

        public void goToHome (ActionEvent actionEvent){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
            Stage stage = (Stage) homeLink.getScene().getWindow();
            stage.setScene(homeScene);
            stage.setTitle("Welcome");
        }


        public void initData (Scene homeScene){
            this.homeScene = homeScene;
        }
    }

