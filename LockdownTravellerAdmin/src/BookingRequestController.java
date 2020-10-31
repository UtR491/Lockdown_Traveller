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
    @FXML
    public GridPane root;
    @FXML
    public Hyperlink addPassengerLink, homeLink;
    public Button bookTicketsButton;
    private Scene homeScene = null;
    private String coach, trainID, userID, source, destination, date;
    private int seats, fare;
    private ArrayList<HBox> passengers = new ArrayList<>();
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

    public void sendRequestButton(ActionEvent actionEvent) {
        String name[] = new String[Math.min(counter, 6)];
        String preference[] = new String[Math.min(counter, 6)];
        int age[] = new int[Math.min(counter, 6)];
        char gender[] = new char[Math.min(counter, 6)];
        String quota[] = new String[Math.min(counter, 6)];
        for(int i = 0; i < Math.min(counter, 6); i++) {
            name[i] = ((TextField)passengers.get(i).getChildren().get(0)).getText();
            age[i] = Integer.parseInt(((TextField)passengers.get(i).getChildren().get(1)).getText());
            gender[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(2)).getValue().charAt(0);
            preference[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(3)).getValue();
            quota[i] = ((ComboBox<String>)passengers.get(i).getChildren().get(4)).getValue();
        }
        LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BookingRequest bookingRequest = new BookingRequest(source, destination, trainID.substring(0, trainID.indexOf(" ")),
                coach.replace(" ", ""), date1, name, age, gender, userID, seats, preference, quota, Math.min(counter, 6),
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
        name.setPromptText("Passenger name");
        age.setPromptText("Age");
        passenger.getChildren().addAll(name, age, gender, preference, quota);
        passengers.add(passenger);
        root.add(passenger, 0, passengers.size() + 1);
    }
}
