import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayTrainsRunningTodayController {
    @FXML
    public Hyperlink homeLink;
    @FXML
    public TreeTableView<TrainsToday> trainHolder;
    private Scene homeScene;
    private DisplayTrainsRunningTodayResponse displayTrainsRunningTodayResponse;

    public void goToHome(ActionEvent actionEvent) {
        Scene scene = homeScene;
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Set Platform");
    }

//    public void setPlatform(ActionEvent actionEvent) {
//        ArrayList<String> trainID = new ArrayList<>();
//        trainID.add(trainIDTextfield.getText());
//        ArrayList<ArrayList<Integer>> stationNo = new ArrayList<>(), platform = new ArrayList<>();
//        stationNo.add(new ArrayList<>());
//        platform.add(new ArrayList<>());
//        for(int i = 0; i < stationNo.size(); i++) {
//            stationNo.get(0).add(Integer.parseInt(((TextField)((HBox)stationPlatformHolder.getItems().get(i)).getChildren().get(0)).getText()));
//            platform.get(0).add(Integer.parseInt(((TextField)((HBox)stationPlatformHolder.getItems().get(i)).getChildren().get(1)).getText()));
//        }
//        SetPlatformRequest setPlatformRequest = new SetPlatformRequest(trainID, stationNo, platform);
//        Main.SendRequest(setPlatformRequest);
//        SetPlatformResponse setPlatformResponse = (SetPlatformResponse) Main.ReceiveResponse();
//        if(setPlatformResponse.getResponse().equals("success")) {
//            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
//            dialog.setContentText("Platforms set successfully");
//            dialog.show();
//        } else {
//            Alert dialog = new Alert(Alert.AlertType.WARNING);
//            dialog.setContentText("Could not set platforms");
//            dialog.show();
//        }
//    }

    public void initData(Scene homeScene, String adminID, DisplayTrainsRunningTodayResponse displayTrainsRunningTodayResponse) {
        this.homeScene = homeScene;
        this.displayTrainsRunningTodayResponse = displayTrainsRunningTodayResponse;
        TreeTableColumn<TrainsToday, Void> setPlatform = new TreeTableColumn<>("Set Platform");
        TreeTableColumn<TrainsToday, String> trainstoday = new TreeTableColumn<>("Trains Running");
        trainstoday.setCellValueFactory((TreeTableColumn.CellDataFeatures<TrainsToday, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getTrain()));
        Callback<TreeTableColumn<TrainsToday, Void>, TreeTableCell<TrainsToday, Void>> platformButton =
                new Callback<TreeTableColumn<TrainsToday, Void>, TreeTableCell<TrainsToday, Void>>() {
                    @Override
                    public TreeTableCell<TrainsToday, Void> call(TreeTableColumn<TrainsToday, Void> param) {
                        final TreeTableCell<TrainsToday, Void> cell = new TreeTableCell<TrainsToday, Void>() {
                            private final Button button = new Button("Add Platform");
                            {
                                button.setOnAction((ActionEvent event) -> {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SetPlatform.fxml"));
                                    Scene scene = null;
                                    try {
                                        scene = new Scene(loader.load());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Stage stage = (Stage) homeLink.getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.setTitle("Add Platform");
                                    SetPlatformController setPlatformController = loader.getController();
                                    setPlatformController.initData(getTreeTableView().getTreeItem(getTreeTableRow().getIndex()).getValue(), homeScene);
                                });
                            }
                            @Override
                            public void updateItem(Void item, boolean empty) {
                                boolean isNotLeaf = false;
                                try {
                                    getTreeTableView().getTreeItem(getTreeTableRow().getIndex()).getParent().getParent();
                                } catch (NullPointerException e) {
                                    isNotLeaf = true;
                                }
                                super.updateItem(item, empty || isNotLeaf);
                                if (empty || isNotLeaf) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(button);
                                }
                            }
                        };
                        return cell;
                    }
                };
        setPlatform.setCellFactory(platformButton);
        trainHolder.getColumns().addAll(trainstoday, setPlatform);
        TreeItem<TrainsToday> rootNode = new TreeItem<>(new TrainsToday("Trains Running Today", "", null, null, null, null));
        rootNode.setExpanded(true);
        trainHolder.setRoot(rootNode);
        final int n = displayTrainsRunningTodayResponse.getTrainID().size();
        for(int i = 0; i < n; i++) {
            ArrayList<String> stations, arrival, departure;
            ArrayList<Integer> stationNo;
            stations = displayTrainsRunningTodayResponse.getStation().get(i);
            arrival = displayTrainsRunningTodayResponse.getArrival().get(i);
            departure = displayTrainsRunningTodayResponse.getDeparture().get(i);
            stationNo = displayTrainsRunningTodayResponse.getStationNo().get(i);
            TreeItem<TrainsToday> leaf = new TreeItem<>(new TrainsToday(displayTrainsRunningTodayResponse.getTrainID().get(i),
                    displayTrainsRunningTodayResponse.getTrainName().get(i), stations, arrival, departure, stationNo));
            rootNode.getChildren().add(leaf);
        }
    }
//
//    public void addPlatform(ActionEvent actionEvent) {
//        HBox stationPlatform = new HBox();
//        TextField stationNumber = new TextField();
//        stationNumber.setPromptText("Enter Station number");
//        TextField platform = new TextField();
//        platform.setPromptText("Halting Platform");
//        stationPlatform.getChildren().addAll(stationNumber, platform);
//        stationPlatform.setSpacing(50);
//        stationPlatform.setPadding(new Insets(0, 50, 0, 50));
//        stationPlatformHolder.getItems().add(stationPlatform);
//    }
}
