import javafx.beans.property.ReadOnlyIntegerWrapper;
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

public class MaintainSeatsController {
    // Holder for Seat and Coach information for a train.
    @FXML
    public TreeTableView<Coach> seatList;
    // Link to go to home.
    @FXML
    public Hyperlink homeLink;

    /**
     * Triggered when Go to home link is pressed.
     * @param actionEvent
     */
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

    /**
     * Triggered first when called form the LandingPageController to populate the TreeTableView with the Response
     * received.
     * @param maintainSeatsResponse
     */
    public void initData(MaintainSeatsResponse maintainSeatsResponse) {

        // Columns of the TreeTableView.
        TreeTableColumn<Coach, String> coach = new TreeTableColumn<>("Coach");
        TreeTableColumn<Coach, Number> coaches = new TreeTableColumn<>("Number of Coaches");
        TreeTableColumn<Coach, Number> seats = new TreeTableColumn<>("Seats per Coach");

        // Set the cell value factory, to specify functions that give the give the value to a field.
        coach.setCellValueFactory((TreeTableColumn.CellDataFeatures<Coach, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getCoach()));
        coaches.setCellValueFactory((TreeTableColumn.CellDataFeatures<Coach, Number> p) ->
                new ReadOnlyIntegerWrapper(p.getValue().getValue().getCoaches()));
        seats.setCellValueFactory((TreeTableColumn.CellDataFeatures<Coach, Number> p) ->
                new ReadOnlyIntegerWrapper(p.getValue().getValue().getSeats()));

        seatList.getColumns().addAll(coach, coaches, seats);

        // Root node for the TreeTableView.
        TreeItem<Coach> rootNode = new TreeItem<>(new Coach("Seat Information", 0, 0));
        rootNode.setExpanded(true);

        // The direct children of root node are nodes that have train name + id. Coach name and number of seats per coach
        // are children of these nodes.
        ArrayList<Train2> trains = maintainSeatsResponse.getTrains();
        for(Train2 train : trains) {

            // Direct child of the root node.
            TreeItem<Coach> trainName = new TreeItem<>(new Coach(train.toString(), 0, 0));
            rootNode.getChildren().add(trainName);

            // Children of train name.
            ArrayList<Coach> coaches1 = train.getCoachInfo();
            for(Coach coach1 : coaches1) {
                TreeItem<Coach> coachLeaf = new TreeItem<>(coach1);
                trainName.getChildren().add(coachLeaf);
            }
        }
        seatList.setRoot(rootNode);
    }
}
