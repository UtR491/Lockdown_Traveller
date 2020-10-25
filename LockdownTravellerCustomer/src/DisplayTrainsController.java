import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;

public class DisplayTrainsController {
    @FXML
    public TextField sourceTextField;
    @FXML
    public TextField destTextField;
    @FXML
    public DatePicker dateDatePicker;
ObjectOutputStream oos;
ObjectInputStream ois;
    @FXML
    public Button Display;
    public void initData(ObjectOutputStream oos,ObjectInputStream ois)
    {
        this.oos=oos;
        this.ois=ois;
    }


    public void displayTrains(ActionEvent actionEvent) {
        String source=sourceTextField.getText();
        String dest=destTextField.getText();
        String date = dateDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/mm/yyyy"));
        DisplayTrainsRequest displayTrainsRequest=new DisplayTrainsRequest(source,dest,date);
        Main.SendRequest(oos,displayTrainsRequest);
        try {
            DisplayTrainsResponse displayTrainsResponse=(DisplayTrainsResponse)ois.readObject();
            FXMLLoader display=new FXMLLoader(getClass().getResource("DisplayTrainsUI.fxml"));
            Stage currentStage=(Stage) Display.getScene().getWindow();
            Scene displayTrains=null;
            try {
                displayTrains=new Scene(display.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentStage.setScene(displayTrains);
            DisplayTrainsLandingPageController displayTrainsLandingPageController=display.getController();
            displayTrainsLandingPageController.init(displayTrainsResponse.getTrain_ID(),displayTrainsResponse.getTrain_Name(),displayTrainsResponse.getDeparture(),displayTrainsResponse.getArrival(),displayTrainsResponse.getThird_AC(),displayTrainsResponse.getSecond_AC(),displayTrainsResponse.getFirst_AC(),displayTrainsResponse.getSleeper(),displayTrainsResponse.getSource(),displayTrainsResponse.getDestination(),displayTrainsResponse.getsDate(),displayTrainsResponse.getI());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
