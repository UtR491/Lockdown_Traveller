import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class TrainsListController {
    @FXML
    public Hyperlink homeLink;
    @FXML
    public TreeTableView<AvailableSeats> trainHolder;
    private Scene homeScene = null;
    private DisplayTrainsResponse displayTrainsResponse = null;
    private String userID = null, source = null, destination = null, date = null;
    public void goToHome (ActionEvent actionEvent){
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }
    public void initData (Scene homeScene, DisplayTrainsResponse displayTrainsResponse, String userID, String source,
                          String destination, String date) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.homeScene = homeScene;
        this.displayTrainsResponse = displayTrainsResponse;
        this.userID = userID;
        TreeTableColumn<AvailableSeats, String> coach = new TreeTableColumn<>("Coach");
        TreeTableColumn<AvailableSeats, String> availability = new TreeTableColumn<>("Availability");
        TreeTableColumn<AvailableSeats, String> fare = new TreeTableColumn<>("Ticket Cost");
        TreeTableColumn<AvailableSeats, Void> book = new TreeTableColumn<>("Book");
        coach.setMinWidth(250);
        availability.setMinWidth(100);
        fare.setMinWidth(120);
        book.setMinWidth(100);
        Callback<TreeTableColumn<AvailableSeats, Void>, TreeTableCell<AvailableSeats, Void>> bookButton =
                new Callback<TreeTableColumn<AvailableSeats, Void>, TreeTableCell<AvailableSeats, Void>>() {
            @Override
            public TreeTableCell<AvailableSeats, Void> call(TreeTableColumn<AvailableSeats, Void> param) {
                final TreeTableCell<AvailableSeats, Void> cell = new TreeTableCell<AvailableSeats, Void>() {
                  private final Button button = new Button("Reserve");
                    {
                        button.setOnAction((ActionEvent event) -> {
                            AvailableSeats availableSeats = getTreeTableView().getTreeItem(getTreeTableRow().getIndex()).getValue();
                            AvailableSeats train = getTreeTableView().getTreeItem(getTreeTableRow().getIndex()).getParent().getValue();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingRequest.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(loader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = (Stage) homeLink.getScene().getWindow();
                            stage.setScene(scene);
                            stage.setTitle("Passenger Details");
                            BookingRequestController bookingRequestController = loader.getController();
                            bookingRequestController.initData(homeScene, availableSeats.getCoach(), train.getCoach(),
                                    userID, source, destination, date, Integer.parseInt(availableSeats.getSeats()),
                                    availableSeats.getFare());
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        boolean isNotLeaf = false;
                        try {
                            getTreeTableView().getTreeItem(getTreeTableRow().getIndex()).getParent().getParent().getParent();
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
        book.setCellFactory(bookButton);
        coach.setCellValueFactory((TreeTableColumn.CellDataFeatures<AvailableSeats, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getCoach()));
        availability.setCellValueFactory((TreeTableColumn.CellDataFeatures<AvailableSeats, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getSeats()));
        fare.setCellValueFactory((TreeTableColumn.CellDataFeatures<AvailableSeats, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getFare() == 0 ? "" :
                        Integer.toString(p.getValue().getValue().getFare())));
        trainHolder.getColumns().addAll(coach, availability, fare, book);
        TreeItem<AvailableSeats> rootNode = new TreeItem<>(new AvailableSeats("Trains", "", 0));
        rootNode.setExpanded(true);
        for(int i = 0; i < displayTrainsResponse.getTrain_Name().size(); i++) {
            TreeItem<AvailableSeats> train = new TreeItem<>(new AvailableSeats(
                    displayTrainsResponse.getTrain_ID().get(i) + " " +
                            displayTrainsResponse.getTrain_Name().get(i), "", 0));
            rootNode.getChildren().add(train);
            TreeItem<AvailableSeats> coachInfo = new TreeItem<>(new AvailableSeats("First AC",
                    displayTrainsResponse.getFirst_AC().get(i), displayTrainsResponse.getAC1Fare().get(i)));
            if(!displayTrainsResponse.getFirst_AC().get(i).equalsIgnoreCase("N/A"))
                train.getChildren().add(coachInfo);
            coachInfo = new TreeItem<>(new AvailableSeats("Second AC", displayTrainsResponse.getSecond_AC().get(i),
                    displayTrainsResponse.getAC2Fare().get(i)));
            if(!displayTrainsResponse.getSecond_AC().get(i).equalsIgnoreCase("N/A"))
                train.getChildren().add(coachInfo);
            coachInfo = new TreeItem<>(new AvailableSeats("Third AC", displayTrainsResponse.getThird_AC().get(i),
                    displayTrainsResponse.getAC3Fare().get(i)));
            if(!displayTrainsResponse.getThird_AC().get(i).equalsIgnoreCase("N/A"))
                train.getChildren().add(coachInfo);
            coachInfo = new TreeItem<>(new AvailableSeats("Sleeper", displayTrainsResponse.getSleeper().get(i),
                    displayTrainsResponse.getSLFare().get(i)));
            if(!displayTrainsResponse.getSleeper().get(i).equalsIgnoreCase("N/A"))
            train.getChildren().add(coachInfo);
        }
        trainHolder.setRoot(rootNode);
    }
}
