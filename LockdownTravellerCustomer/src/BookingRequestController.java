import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class BookingRequestController {
    @FXML
    public GridPane root;
    @FXML
    public Hyperlink addPassengerLink, homeLink;
    public Button bookTicketsButton;
    private Scene homeScene = null;
    private String coach, trainID, userID, source, destination, date;
    private int seats;
    private ArrayList<HBox> passengers = new ArrayList<>();
    public void initData(Scene homeScene, String coach, String trainID, String userID, String source, String destination,
                         String date, int seats) {
        this.homeScene = homeScene;
        this.coach = coach;
        this.trainID = trainID;
        this.userID = userID;
        this.source = source;
        this.destination = destination;
        String[] dates = date.split("-");
        this.date = dates[2] + "/" + dates[1] +"/" + dates[0];
        this.seats = seats;
        System.out.println("Train was " + trainID);
        System.out.println("Coach was " + coach);
        System.out.println("From - " + source + " to " + destination + " on " + date + " by user " + userID);
    }

    public void sendRequestButton(ActionEvent actionEvent) {
        String name[] = new String[counter];
        String preference[] = new String[counter];
        int age[] = new int[counter];
        char gender[] = new char[counter];
        for(int i = 0; i < counter; i++) {
            name[i] = ((TextField)passengers.get(i).getChildren().get(0)).getText();
            age[i] = Integer.parseInt(((TextField)passengers.get(i).getChildren().get(1)).getText());
            gender[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(2)).getValue().charAt(0);
            preference[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(3)).getValue();
        }
        LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BookingRequest bookingRequest = new BookingRequest(source, destination, trainID.substring(0, trainID.indexOf(" ")),
                coach.replace(" ", ""), date1, name, age, gender, userID, seats, preference, Math.min(counter, 6));
        Main.SendRequest(bookingRequest);
        BookingResponse bookingResponse = (BookingResponse) Main.ReceiveResponse();
        int n = bookingResponse.getBookingIds().length;
        for(int i =0; i < n; i++) {
            System.out.println(bookingResponse.getBookingIds()[i] + " " + bookingResponse.getSeatsAlloted()[i]);
        }
    }

    public void goToHome (ActionEvent actionEvent){
        Stage stage = (Stage) homeLink.getScene().getWindow();
        stage.setScene(homeScene);
        stage.setTitle("Welcome");
    }

    private int counter = 0;
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
        passenger.setSpacing(20);
        passenger.setPadding(new Insets(0, 0, 0 ,20));
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
        name.setPromptText("Passenger name");
        age.setPromptText("Age");
        passenger.getChildren().addAll(name, age, gender, preference);
        passengers.add(passenger);
        root.add(passenger, 0, passengers.size() + 1);
    }
}
