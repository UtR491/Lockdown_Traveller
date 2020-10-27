import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import java.util.ArrayList;

public class DisplayTrainsLandingPageController {
   @FXML
    public TreeTableView <AvailableSeats>availableList;
void init(DisplayTrainsResponse displayTrainsResponse)
{
    TreeItem<AvailableSeats> rootNode=new TreeItem<>(new AvailableSeats("Available Trains",""));
    String[] Train_ID=displayTrainsResponse.getTrain_ID();
    String[] Train_Name=displayTrainsResponse.getTrain_Name();
    for (String s:Train_ID ) {

    }
    TreeItem<AvailableSeats>secondNode=new TreeItem<>(new AvailableSeats(""))
}

}