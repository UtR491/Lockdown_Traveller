import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookingRequestController {
    // Holds the HBoxes. Each HBox has fields and combo boxes to get all information about the user.
    @FXML
    public GridPane root;
    // addPassengerLink Adds an HBox to the grid pane to add another passenger to the booking.
    // homeLink Goes to the home screen.
    @FXML
    public Hyperlink addPassengerLink, homeLink;
    // Button to send all the information to the server.
    @FXML
    public Button bookTicketsButton;
    // The home scene. Needed because the home screen shows some user related data obtained from the server at the time
    // of login.
    private Scene homeScene = null;

    // Various information related to booking. Variable names describe what they store.
    private String coach, trainID, userID, source, destination, date;

    // Number of seats available before the booking. Fare per seat in the desired Coach.
    private int seats, fare;

    // Storing the passenger information holder. The HBox has TextFields like name and age and ComboBoxes like Meal,
    // berth preference and gender.
    private final ArrayList<HBox> passengers = new ArrayList<>();

    /**
     * Takes the data related to the desired train and coach from the trains list screen to store into the request
     * object.
     * @param homeScene Home screen information.
     * @param coach Selected coach.
     * @param trainID Identifier for the selected train.
     * @param userID User ID for doing the booking.
     * @param source Boarding station entered by the user.
     * @param destination De-boarding station entered by the user.
     * @param date Date of boarding entered by the user.
     * @param seats Number of seats available at the time of booking.
     * @param fare Per seat fare for the specified journey in the specified train.
     */
    public void initData(Scene homeScene, String coach, String trainID, String userID, String source, String destination,
                         String date, int seats, int fare) {
        this.homeScene = homeScene;
        this.coach = coach;
        this.trainID = trainID;
        this.userID = userID;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        String[] dates = date.split("-");
        this.date = dates[2] + "/" + dates[1] +"/" + dates[0];
        this.seats = seats;
        System.out.println("Train was " + trainID);
        System.out.println("Coach was " + coach);
        System.out.println("From - " + source + " to " + destination + " on " + date + " by user " + userID);
    }

    /**
     * Triggered on clicking the book seats button.
     * @param actionEvent
     */
    public void sendRequestButton(ActionEvent actionEvent) {
        String[] name = new String[Math.min(counter, 6)];
        String[] preference = new String[Math.min(counter, 6)];
        int[] age = new int[Math.min(counter, 6)];
        char[] gender = new char[Math.min(counter, 6)];
        String[] quota = new String[Math.min(counter, 6)];
        int[] meal = new int[Math.min(counter, 6)];
        for(int i = 0; i < Math.min(counter, 6); i++) {
            name[i] = ((TextField)passengers.get(i).getChildren().get(0)).getText();
            age[i] = Integer.parseInt(((TextField)passengers.get(i).getChildren().get(1)).getText());
            gender[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(2)).getValue().charAt(0);
            preference[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(3)).getValue();
            quota[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(4)).getValue();
            String m = ((ComboBox<String>)passengers.get(i).getChildren().get(5)).getValue();
            if(m.equals("None"))
                meal[i] = 0;
            else if(m.equals("Veg"))
                meal[i] = 1;
            else
                meal[i] = 2;
        }
        LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BookingRequest bookingRequest = new BookingRequest(source, destination, trainID.substring(0, trainID.indexOf(" ")),
                coach.replace(" ", ""), date1, name, age, gender, userID, seats, preference, quota, meal, Math.min(counter, 6),
                fare * preference.length);
        Main.SendRequest(bookingRequest);
        BookingResponse bookingResponse = (BookingResponse) Main.ReceiveResponse();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ticket.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setTitle("Booking Details");
        stage.setScene(scene);
        TicketController ticketController = loader.getController();
        ticketController.initData(homeScene, bookingResponse, name, age, gender);
    }

    /**
     * Triggered on clicking the Go to home link.
     * @param actionEvent
     */
    public void goToHome (ActionEvent actionEvent){
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    private int counter = 0;

    /**
     * Triggered on clicking the add passenger button. Adds an HBox to the GridPaneView.
     * @param actionEvent
     */
    public void addPassenger(ActionEvent actionEvent) {
        counter+=1;
        if(counter > 6) {
            Alert dialog = new Alert(Alert.AlertType.WARNING);
            dialog.setContentText("Cannot book more than 6 seats.");
            dialog.show();
            return ;
        }
        root.setVgap(20);
        HBox passenger = new HBox();
        passenger.setSpacing(5);
        passenger.setPadding(new Insets(0, 0, 0 ,5));
        TextField name = new TextField();
        TextField age = new TextField();
        name.setMinWidth(200);
        name.setMaxWidth(200);
        age.setMinWidth(50);
        age.setMaxWidth(50);
        ComboBox<String> gender = new ComboBox<>();
        gender.setPromptText("Gender");
        gender.getItems().addAll("Male", "Female");
        ComboBox<String> preference = new ComboBox<>();
        preference.setPromptText("Preference");
        preference.getItems().addAll("Lower", "Upper");
        if(coach.equals("Second AC")) {
            preference.getItems().addAll("Side Lower", "Side Upper");
        } else if(coach.equals("Third AC") || coach.equals("Sleeper")) {
            preference.getItems().addAll("Middle", "Side Lower", "Side Upper");
        }
        ComboBox<String> quota = new ComboBox<>();
        quota.setPromptText("Quota");
        quota.getItems().addAll("General", "Viklang");
        ComboBox<String> meal = new ComboBox<>();
        meal.setPromptText("Meal");
        meal.getItems().addAll("None", "Veg", "Non-veg");
        name.setPromptText("Passenger name");
        age.setPromptText("Age");
        passenger.getChildren().addAll(name, age, gender, preference, quota, meal);
        passengers.add(passenger);
        root.add(passenger, 0, passengers.size() + 1);
    }
}
