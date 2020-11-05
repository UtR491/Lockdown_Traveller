import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TicketController {
    // Go to home link.
    @FXML
    public Hyperlink homeLink;
    // ListView to hold ticket information.
    @FXML
    public ListView<Label> ticketHolder;
    // Label to show the final cost of booking.
    @FXML
    public Label costLabel;
    // Label to show PNR which is a unique identifier for each booking.
    @FXML
    public Label pnrLabel;
    private Scene homeScene;

    /**
     * First executed after loading the scene. Used to populate the list view with the booking response, i.e. booking\
     * id, pnr, ticket status etc.
     * @param homeScene The home scene to go to home directly.
     * @param bookingResponse The booking response object containing all the information about the booking requested.
     * @param name Names of passengers.
     * @param age Ages of passengers.
     * @param gender Gender of passengers.
     */
    public void initData(Scene homeScene, BookingResponse bookingResponse, String[] name, int[] age, char[] gender) {
        this.homeScene = homeScene;
        final int n = age.length;
        pnrLabel.setText(bookingResponse.getPnr());
        costLabel.setText("Rs. "+ bookingResponse.getTotalCost());
        // For each requested booking. Show all relevant information related to it.
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

    /**
     * Triggered on clicking the Go to home link.
     * @param actionEvent
     */
    public void goToHome(ActionEvent actionEvent) {
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }
}
