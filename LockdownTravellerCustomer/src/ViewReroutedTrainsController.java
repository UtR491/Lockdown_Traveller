import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

public class ViewReroutedTrainsController {
    @FXML
    public TreeTableView<ReroutedLeaf> trainHolder;
    @FXML
    public Hyperlink homeLink;

    private Scene homeScene;
    private ViewReroutedTrainsResponse viewReroutedTrainsResponse;

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    public void initData(Scene homeScene, ViewReroutedTrainsResponse viewReroutedTrainsResponse) {
        this.homeScene = homeScene;
        this.viewReroutedTrainsResponse = viewReroutedTrainsResponse;
        TreeTableColumn<ReroutedLeaf, String> station = new TreeTableColumn<>("Station");
        TreeTableColumn<ReroutedLeaf, String> cityCode = new TreeTableColumn<>("City");
        TreeTableColumn<ReroutedLeaf, String> arrival = new TreeTableColumn<>("Arr");
        TreeTableColumn<ReroutedLeaf, String> departure = new TreeTableColumn<>("Dep");
        TreeTableColumn<ReroutedLeaf, String> dayNo = new TreeTableColumn<>("Day No.");
        TreeTableColumn<ReroutedLeaf, String> distance = new TreeTableColumn<>("Dist.");

        station.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getStation()));
        cityCode.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getCityCode()));
        arrival.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getArrival()));
        departure.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDeparture()));
        dayNo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDayNo() == -1 ? "" :
                        Integer.toString(p.getValue().getValue().getDayNo())));
        distance.setCellValueFactory((TreeTableColumn.CellDataFeatures<ReroutedLeaf, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDistance() == -1 ? "" :
                        Integer.toString(p.getValue().getValue().getDistance())));

        trainHolder.getColumns().addAll(station, cityCode, arrival, departure, dayNo, distance);

        TreeItem<ReroutedLeaf> rootNode = new TreeItem<>(new ReroutedLeaf("Reroruted Trains", "", "",
                "", -1, -1, -1));
        rootNode.setExpanded(true);
        trainHolder.setRoot(rootNode);
        for(int i = 0; i < viewReroutedTrainsResponse.getTrainID().size(); i++) {
            TreeItem<ReroutedLeaf> train = new TreeItem<>(new ReroutedLeaf(
                    viewReroutedTrainsResponse.getTrainID().get(i) + " " +
                            viewReroutedTrainsResponse.getTrainName().get(i),
                    "", "", "", -1, -1 ,-1));
            rootNode.getChildren().add(train);
            for(int j = 0; j < viewReroutedTrainsResponse.getArrival().get(0).size(); j++) {
                TreeItem<ReroutedLeaf> leafTreeItem = new TreeItem<>(new ReroutedLeaf(
                        viewReroutedTrainsResponse.getStation().get(i).get(j),
                        viewReroutedTrainsResponse.getCityCode().get(i).get(j),
                        viewReroutedTrainsResponse.getArrival().get(i).get(j),
                        viewReroutedTrainsResponse.getDeparture().get(i).get(j),
                        viewReroutedTrainsResponse.getDayNo().get(i).get(j),
                        viewReroutedTrainsResponse.getDistanceCovered().get(i).get(j),
                        viewReroutedTrainsResponse.getStationNo().get(i).get(j)
                ));
                train.getChildren().add(leafTreeItem);
            }
        }
    }
}
