import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LandingPageController {

    @FXML
    public Button findTrainsButton;
    private String userId;
    public void initData(String userId) {
        this.userId = userId;
    }

    public void findTrains(ActionEvent actionEvent) {

    }
}
