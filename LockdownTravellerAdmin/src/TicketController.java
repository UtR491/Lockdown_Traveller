import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TicketController {
    @FXML
    public Hyperlink homeLink;
    @FXML
    public ListView<Label> ticketHolder;
    @FXML
    public Label costLabel;
    @FXML
    public Label pnrLabel;
    private Scene homeScene;

    public void initData(Scene homeScene, BookingResponse bookingResponse, String[] name, int[] age, char[] gender) {
        this.homeScene = homeScene;
        final int n = age.length;
        pnrLabel.setText(bookingResponse.getPnr());
        costLabel.setText("Rs. "+ bookingResponse.getTotalCost());
        for(int i = 0; i < n; i++) {
            Label passi = new Label();
            passi.setWrapText(true);
            passi.setText("Name: " + name[i] +
                    " Age: " + age[i] +
                    " Gender: " + gender[i] +
                    " Seat: " + bookingResponse.getSeatsAlloted()[i] +
                    " Booking ID: " + bookingResponse.getBookingIds()[i] +
                    " Status: " + (bookingResponse.getSeatsAlloted()[i].startsWith("WL", 2) ? "Waiting" :
                                        "Confirmed"));
            ticketHolder.getItems().add(passi);
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }
}
