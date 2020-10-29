import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ViewCancelledTrainsController {

    @FXML
    public DatePicker dateDatePicker;
    @FXML
    public Button viewTrainsButton;
    @FXML
    public ListView<HBox> cancelledTrainsHolder;
    @FXML
    Hyperlink homeLink;

    private Scene homeScene;
    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    public void initData(Scene homeScene) {
        this.homeScene = homeScene;
    }

    public void cancelledTrains(ActionEvent actionEvent) {
        String date = dateDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ViewCancelledTrainsRequest viewCancelledTrainsRequest = new ViewCancelledTrainsRequest(date);
        Main.SendRequest(viewCancelledTrainsRequest);
        ViewCancelledTrainsResponse viewCancelledTrainsResponse = (ViewCancelledTrainsResponse) Main.ReceiveResponse();

        for(int i = 0; i < viewCancelledTrainsResponse.getTrain_Name().size(); i++) {
            HBox train = new HBox();
            train.setSpacing(20);
            train.getChildren().addAll(new Label(viewCancelledTrainsResponse.getTrain_ID().get(i)),
                    new Label(viewCancelledTrainsResponse.getTrain_Name().get(i)),
                    new Label(viewCancelledTrainsResponse.getCancelled_Till().get(i)));
            cancelledTrainsHolder.getItems().add(train);
        }
    }
}
