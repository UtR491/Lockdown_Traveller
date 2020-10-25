import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MaintainTrainsController {
    @FXML
    public TreeTableView<Station> trainsList;
    @FXML
    public Hyperlink homeLink;

    MaintainTrainsResponse maintainTrainsResponse = null;

    public void initData(MaintainTrainsResponse maintainTrainsResponse) {

        TreeTableColumn<Station, String> station = new TreeTableColumn<>("Station");
        TreeTableColumn<Station, String> arrival = new TreeTableColumn<>("Arrival");
        TreeTableColumn<Station, String> departure = new TreeTableColumn<>("Departure");
        TreeTableColumn<Station, String> stationNumber = new TreeTableColumn<>("Station Number");
        TreeTableColumn<Station, String> day = new TreeTableColumn<>("Day");

        station.setCellValueFactory((TreeTableColumn.CellDataFeatures<Station, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getStation()));
        arrival.setCellValueFactory((TreeTableColumn.CellDataFeatures<Station, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getArrival()));
        departure.setCellValueFactory((TreeTableColumn.CellDataFeatures<Station, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDeparture()));
        stationNumber.setCellValueFactory((TreeTableColumn.CellDataFeatures<Station, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getStationNumber() == 0 ? "" : new Integer(p.getValue().getValue().getStationNumber()).toString()));
        day.setCellValueFactory((TreeTableColumn.CellDataFeatures<Station, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDay() == 0 ? "" : new Integer(p.getValue().getValue().getDay()).toString()));

        trainsList.getColumns().add(station);
        trainsList.getColumns().add(arrival);
        trainsList.getColumns().add(departure);
        trainsList.getColumns().add(stationNumber);
        trainsList.getColumns().add(day);

        System.out.println("Inside initData of MaintainTrainsController");
        this.maintainTrainsResponse = maintainTrainsResponse;
        TreeItem<Station> rootNode = new TreeItem<>(new Station("Trains", "", "", 0, 0));
        rootNode.setExpanded(true);
        ArrayList<Train> trains = maintainTrainsResponse.getTrains();
        for(Train train : trains) {
            TreeItem<Station> trainNode = new TreeItem<>(new Station(train.toString(), "", "", 0, 0));
            rootNode.getChildren().add(trainNode);
            int numStation = train.getRoute().size();
            for(int i=0;i<numStation;i++) {
                TreeItem<Station> stationLeaf = new TreeItem<>(new Station(
                        train.getRoute().get(i), train.getArrival().get(i), train.getDeparture().get(i),
                        train.getStationNumber().get(i), train.getDay().get(i)));
                trainNode.getChildren().add(stationLeaf);
            }
        }
        trainsList.setRoot(rootNode);
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
