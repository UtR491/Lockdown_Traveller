import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SetPlatformController {
    @FXML
    public ListView<HBox> holder;
    @FXML
    public Button setButton;
    @FXML
    public Hyperlink homeLink;

    private int nc;
    private TrainsToday value;
    private Scene homeScene;
    public void initData(TrainsToday value, Scene homeScene) {
        this.value = value;
        this.homeScene = homeScene;
        final int n = value.getStations().size();
        nc = n;
        for(int i = 0; i < n; i++) {
            HBox hBox = new HBox();
            Label label = new Label("Station : " + value.getStations().get(i) +
                    " Station No. : " + value.getStationNo().get(i) +
                    " Arrival : " + value.getArrival().get(i) +
                    " Departure : " + value.getDeparture().get(i));
            TextField textField = new TextField();
            textField.setPromptText("Platform Number");
            hBox.getChildren().addAll(label, textField);
            holder.getItems().add(hBox);
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        Scene scene = homeScene;
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Welcome Admin");
    }

    public void setPlatform(ActionEvent actionEvent) {
        ArrayList<ArrayList<Integer>> stationNo = new ArrayList<>();
        stationNo.add(value.getStationNo());
        ArrayList<ArrayList<Integer>> platformNo = new ArrayList<>();
        platformNo.add(new ArrayList<>());
        ArrayList<String> trainIDs = new ArrayList<>();
        trainIDs.add(value.getTrain().substring(0, 5));
        for(int i = 0; i < nc; i++) {
            platformNo.get(0).add(Integer.parseInt(((TextField)holder.getItems().get(i).getChildren().get(1)).getText()));
        }
        SetPlatformRequest setPlatformRequest = new SetPlatformRequest(trainIDs, stationNo, platformNo);
        Main.SendRequest(setPlatformRequest);
        SetPlatformResponse setPlatformResponse = (SetPlatformResponse) Main.ReceiveResponse();
        if(setPlatformResponse.getResponse().equals("success")) {
            goToHome(null);
        } else {
            goToHome(null);
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setContentText("Could not set platform");
            dialog.show();
        }
    }
}
