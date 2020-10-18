package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LandingPageController {

    @FXML
    public Button findTrainsButton;
    private String userId;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    public void initData(String userId, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.userId = userId;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public void findTrains(ActionEvent actionEvent) {

    }
}
