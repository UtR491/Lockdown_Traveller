import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookingHistoryController {
    @FXML
    public ListView<VBox> upcomingHolder, previousHolder;
    @FXML
    public Hyperlink homeLink;
    private Scene homeScene;

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    private BookingHistoryRequest bookingHistoryRequest;
    public void initData(Scene homeScene, BookingHistoryResponse bookingHistoryResponse) {
        this.homeScene = homeScene;
        final int n = bookingHistoryResponse.getDestination().size();
        for(int i = 0; i < n; i++) {
            VBox perPnrDetails = new VBox();
            HBox hBox = new HBox();
            Label pnr = new Label("PNR : " + bookingHistoryResponse.getPnr().get(i));
            Label source = new Label("Source : " + bookingHistoryResponse.getSource().get(i));
            Label destination = new Label("Destination : " + bookingHistoryResponse.getDestination().get(i));
            Label date = new Label("Date : " + bookingHistoryResponse.getDate().get(i));
            LocalDate dateO;
            try {
                dateO = LocalDate.parse(bookingHistoryResponse.getDate().get(i));
            } catch (DateTimeParseException e) {
                dateO = LocalDate.now();
            }
            hBox.getChildren().addAll(pnr, source, destination, date);
            perPnrDetails.getChildren().add(hBox);
            final int t = bookingHistoryResponse.getAge().get(i).size();
            for(int j = 0; j < t; j++) {
                hBox = new HBox();
                Label bookingID = new Label("ID : " + bookingHistoryResponse.getBookingID().get(i).get(j));
                Label name = new Label("Name : " + bookingHistoryResponse.getName().get(i).get(j));
                Label age = new Label("Age : " + bookingHistoryResponse.getAge().get(i).get(j));
                Label gender = new Label("Gender : " + bookingHistoryResponse.getGender().get(i).get(j));
                hBox.setSpacing(20);
                hBox.getChildren().addAll(bookingID, name, age, gender);
                perPnrDetails.getChildren().add(hBox);
            }
            if(dateO.isAfter(LocalDate.now()))
                upcomingHolder.getItems().add(perPnrDetails);
            else
                previousHolder.getItems().add(perPnrDetails);
        }
    }
}
