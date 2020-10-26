import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifySeatsController {
    @FXML
    public ComboBox<String> modifyOptionComboBox, coachComboBox, typeComboBox;
    @FXML
    public TextField trainIdTextField, newNumberTextfield;
    @FXML
    public Button modifySeatsButton;
    @FXML
    public Hyperlink homeLink;

    Alert resultDialog = new Alert(Alert.AlertType.CONFIRMATION);

    public void executeFirst() {
        modifyOptionComboBox.getItems().addAll("Coaches", "Seats");
        coachComboBox.getItems().addAll("FirstAC", "SecondAC", "ThirdAC", "Sleeper");
        typeComboBox.getItems().addAll("Add", "Remove");
    }

    public void modifySeatCoach(ActionEvent actionEvent) {
        if(modifyOptionComboBox.getValue().equals("Coaches") && typeComboBox.getValue().equals("Remove")) {
            RemoveCoachesRequest removeCoachesRequest = new RemoveCoachesRequest(trainIdTextField.getText(),
                    coachComboBox.getValue(), Integer.parseInt(newNumberTextfield.getText()));
            Main.SendRequest(removeCoachesRequest);
            RemoveCoachesResponse removeCoachesResponse = (RemoveCoachesResponse) Main.ReceiveResponse();
            if(removeCoachesResponse.getResponse().equals("success")) {
                resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
                resultDialog.setContentText("Changed the number of coaches successfully.");
                resultDialog.setHeaderText("Success");
            }
            else {
                resultDialog.setAlertType(Alert.AlertType.WARNING);
                resultDialog.setContentText("Could not change the number of coaches.");
                resultDialog.setHeaderText("Failure");
            }
            resultDialog.show();
        } else if(modifyOptionComboBox.getValue().equals("Seats") && typeComboBox.getValue().equals("Remove")){
            RemoveSeatsRequest removeSeatsRequest = new RemoveSeatsRequest(trainIdTextField.getText(),
                    coachComboBox.getValue(), Integer.parseInt(newNumberTextfield.getText()));
            Main.SendRequest(removeSeatsRequest);
            RemoveSeatsResponse removeSeatsResponse = (RemoveSeatsResponse) Main.ReceiveResponse();
            if(removeSeatsResponse.getResponse().equals("success")) {
                resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
                resultDialog.setContentText("Changed the number of seats successfully.");
                resultDialog.setHeaderText("Success");
            }
            else {
                resultDialog.setAlertType(Alert.AlertType.WARNING);
                resultDialog.setContentText("Could not change the number of seats.");
                resultDialog.setHeaderText("Failure");
            }
            resultDialog.show();
        } else if(modifyOptionComboBox.getValue().equals("Coaches") && typeComboBox.getValue().equals("Add")) {
            AddCoachesRequest addCoachesRequest = new AddCoachesRequest(trainIdTextField.getText(), coachComboBox.getValue(),
                    Integer.parseInt(newNumberTextfield.getText()));
            Main.SendRequest(addCoachesRequest);
            AddCoachesResponse addCoachesResponse = (AddCoachesResponse) Main.ReceiveResponse();
            if(addCoachesResponse.getResponse().equals("success")) {
                resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
                resultDialog.setContentText("Changed the number of seats successfully.");
                resultDialog.setHeaderText("Success");
            }
            else {
                resultDialog.setAlertType(Alert.AlertType.WARNING);
                resultDialog.setContentText("Could not change the number of seats.");
                resultDialog.setHeaderText("Failure");
            }
            resultDialog.show();
        } else {
            AddSeatsRequest addSeatsRequest = new AddSeatsRequest(trainIdTextField.getText(), coachComboBox.getValue(),
                    Integer.parseInt(newNumberTextfield.getText()));
            Main.SendRequest(addSeatsRequest);
            AddSeatsResponse addSeatsResponse = (AddSeatsResponse) Main.ReceiveResponse();
            if(addSeatsResponse.getResponse().equals("success")) {
                resultDialog.setAlertType(Alert.AlertType.CONFIRMATION);
                resultDialog.setContentText("Changed the number of seats successfully.");
                resultDialog.setHeaderText("Success");
            }
            else {
                resultDialog.setAlertType(Alert.AlertType.WARNING);
                resultDialog.setContentText("Could not change the number of seats.");
                resultDialog.setHeaderText("Failure");
            }
            resultDialog.show();
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
        Stage stage = (Stage) homeLink.getScene().getWindow();
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(newScene);
        stage.setTitle("Welcome Admin");
    }
}
