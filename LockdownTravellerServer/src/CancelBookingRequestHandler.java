import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler {

    final private Connection connection;
    final private CancelBookingRequest cb;
    final private ObjectOutputStream oos;

    private String pnr, userID;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param cb The cancel booking request object with relevant information.
     * @param oos The output stream to send the object.
     */
    CancelBookingRequestHandler(Connection connection, CancelBookingRequest cb, ObjectOutputStream oos) {
        this.connection = connection;
        this.cb = cb;
        this.oos = oos;

        this.pnr = cb.getPNR();
        this.userID = cb.getUserId();
    }

    @Override
    void sendQuery() {
        // Train and seats in it which get vacant on cancellation.
        String query1 = "select Seat_No, Train_ID, Date from Vacancy_Info where Booking_ID in (select Booking_ID from Booking_Info " +
                "where PNR = ? and User_ID = ?) limit 1;";
        // Change status corresponding to the PNR from 'Confirmed' or 'Waiting' to 'Cancelled'
        String query2 = "update Booking_Info set Booking_Status = 'Cancelled' where PNR = ? and User_ID = ?;";
        // Get waiting booking ids in Booking_Info table.
        String query3 = "select Booking_ID, Seat_No  from Vacancy_Info where Seat_No like ? and Date = ? and Train_ID" +
                " = ? group by Booking_ID, Seat_No order by Seat_No;";
        // Now allot seats for passengers in the waiting list. Find seats that are occupied at some station in the
        // journey of a passenger in waiting.
        String query4 = "select distinct Seat_No from Vacancy_Info as V join Booking_Info as B where V.Train_ID = ? " +
                "and V.Date = ? and B.Booking_Status = 'Confirmed' and Seat_No like ? and V.Station in ( select " +
                "Station from Vacancy_Info where Booking_ID = ?);";
        // Confirm waiting if seat found.
        String query5 = "update Booking_Info set Status = 'Confirmed' where Booking_ID = ?;";
        // Update the the vacancy info when a waiting is confirmed.
        String query6 = "update Vacancy_Info set Seat_No = ? where Booking_ID = ?;";

        CancelBookingResponse cancelBookingResponse = CancelBooking(query1, query2, query3, query4, query5, query6);
        Server.SendResponse(oos, cancelBookingResponse);
    }

    /**
     * Response of the server to the cancel request. Returns success even if waiting was no cleared.
     * @params Queries.
     * @return Reponse object.
     */
    private CancelBookingResponse CancelBooking(String query1, String query2, String query3, String query4, String query5, String query6) {

        String seatType, trainID, date, query7;
        int numCoaches, seatsPerCoach;
        boolean[] notVacant;

        try {
            PreparedStatement getCancelledBooking = connection.prepareStatement(query1);
            getCancelledBooking.setString(1, pnr);
            getCancelledBooking.setString(2, userID);
            ResultSet pnrDetail = getCancelledBooking.executeQuery();

            if(pnrDetail.next()) {
                seatType = pnrDetail.getString("Seat_No").substring(0, 2);
                trainID = pnrDetail.getString("Train_ID");
                date = pnrDetail.getString("Date");
                query7 = "select " + coach(seatType) + "_Coaches, " + coach(seatType) + "_Seats from " +
                        "Basic_Train_Info where Train_ID = ?";
                PreparedStatement changeStatus = connection.prepareStatement(query2);
                changeStatus.setString(1, pnr);
                changeStatus.setString(2, userID);
                int c = changeStatus.executeUpdate();
                if(c == 0) {
                    // Could not change the status of cancelled booking from Confirmed to Cancelled.
                    return new CancelBookingResponse("failure");
                } else if(seatType.equalsIgnoreCase("WL")) {
                    // If the cancelled seat was waiting type. Then no seat has been made available, so it makes no
                    // sense to check and allot seats in this case.
                    return new CancelBookingResponse("success");
                }
            } else {
                // Result set was empty. Either the PNR does no exists or some other user is trying to cancel
                // someone else's booking by hit and trial.
                return new CancelBookingResponse("failure");
            }
            PreparedStatement waitingList = connection.prepareStatement(query3);
            waitingList.setString(1, seatType + "WL%");
            waitingList.setString(2, date);
            waitingList.setString(3, trainID);
            System.out.println(waitingList.toString());
            ResultSet waiting = waitingList.executeQuery();
            if(waiting.next()) {
                // For every booking id in waiting, do this. Check if a seat will be available for the stations in
                // the journey of that booking, if yes give that seat to the person in waiting.
                do {
                    PreparedStatement occupiedSeats = connection.prepareStatement(query4);
                    occupiedSeats.setString(1, trainID);
                    occupiedSeats.setString(2, date);
                    occupiedSeats.setString(3, seatType + "%");
                    occupiedSeats.setString(4, waiting.getString("Booking_ID"));
                    ResultSet occupied = occupiedSeats.executeQuery();
                    PreparedStatement arrangementQuery = connection.prepareStatement(query7);
                    arrangementQuery.setString(1, trainID);
                    ResultSet coachArrangement = arrangementQuery.executeQuery();
                    coachArrangement.next();
                    numCoaches = coachArrangement.getInt(coach(seatType) + "_Coaches");
                    seatsPerCoach = coachArrangement.getInt(coach(seatType) + "_Seats");
                    notVacant = new boolean[numCoaches*seatsPerCoach + 5];
                    while(occupied.next()) {
                        int coachNumber = 0, seat = 0;
                        try {
                            coachNumber = Integer.parseInt(String.valueOf(occupied.getString("Seat_No").charAt(2)));
                            seat = Integer.parseInt(occupied.getString("Seat_No").substring(3));
                            System.out.println(coachNumber + " " + seat);
                        } catch (NumberFormatException e) {
                            notVacant[0] = true;
                        }
                        try {
                            notVacant[(coachNumber - 1) * seatsPerCoach + seat] = true;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            notVacant[0] = true;
                        }
                        for(int i = 1; i < numCoaches; i++) {
                            int j;
                            for(j = 1; j < seatsPerCoach; j++) {
                                if(!notVacant[(i-1)*seatsPerCoach + j]) {
                                    PreparedStatement updateBooking = connection.prepareStatement(query5);
                                    updateBooking.setString(1, occupied.getString("Booking_ID"));
                                    PreparedStatement updateVacancy = connection.prepareStatement(query6);
                                    updateVacancy.setString(1, seatType + i + (j > 9 ? j : "0"+j));
                                    updateVacancy.setString(2, occupied.getString("Booking_ID"));
                                    updateBooking.executeUpdate();
                                    updateVacancy.executeUpdate();
                                    notVacant[(i-1)*seatsPerCoach + j] = true;
                                    break;
                                }
                            }
                            if(j < seatsPerCoach)
                                break;
                        }
                    }
                } while(waiting.next());
                // Does not matter if waiting was cleared, but the booking was cancelled for sure.
                return new CancelBookingResponse("success");
            } else {
                // No waiting.
                return new CancelBookingResponse("success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // Returning from here only means something unexpected happened.
        return new CancelBookingResponse("failure");
    }

    /**
     * Give the prefix for coach to be used in the database.
     * @param seatType Code for seat.
     * @return Seat prefix deduced from the code.
     */
    private String coach(String seatType) {
        if(seatType == "SL")
            return "Sleeper";
        else if(seatType == "A1")
            return "FirstAC";
        else if(seatType == "A2")
            return "SecondAC";
        else
            return "ThirdAC";
    }

}