import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.common.util.report.qual.ReportWrite;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class BookingRequestHandler extends Handler {

    private final Connection connection;
    private final BookingRequest bookingRequest;
    private final ObjectOutputStream oos;
    private final String coach, trainID, userID;
    private @NonNull String coachCode;
    private boolean @MonotonicNonNull [] notVacant;
    private int numCoaches, seatsPerCoach, numSeats, availableSeats, sourceStationNumber=-1, totalCost;
    private ArrayList<String> stationsOnRoute = new ArrayList<>();
    private int currentWaiting = 0;

    /**
     * Constructor. Initializes the object with the database connection, request object and output stream to send the
     * response.
     * @param connection The connection to the database to execute updates and queries.
     * @param bookingRequest The booking request object with relevant information.
     * @param oos The output stream to send the object.
     */
    public BookingRequestHandler(Connection connection, BookingRequest bookingRequest, ObjectOutputStream oos) {

        this.connection = connection;
        this.bookingRequest = bookingRequest;
        this.oos = oos;

        trainID = this.bookingRequest.getTrainId();
        coach = this.bookingRequest.getCoach();
        userID = this.bookingRequest.getUserId();
        totalCost = this.bookingRequest.getTotalCost();
        currentWaiting = Math.max(0, -bookingRequest.getAvailableSeat());
        // Station which are in the route.
        switch (coach) {
            case "Sleeper":
                coachCode = "SL";
                break;
            case "FirstAC":
                coachCode = "A1";
                break;
            case "SecondAC":
                coachCode = "A2";
                break;
            default:
                coachCode = "A3";
                break;
        }
        System.out.println(currentWaiting);
    }

    /**
     * This is the first function that is called inside the request identifier. In this function, we form the relevant
     * sql queries and pass it to another function to execute.
     */
    @Override
    public void sendQuery() {
        numSeats = bookingRequest.getNumSeat();
        availableSeats = bookingRequest.getAvailableSeat();

        // Select stations on that the journey takes from the specified Source and Destination.
        // TODO Can be made more efficient.
        String query1 = "select Station, Station_No from Route_Info where Train_ID = ? and Station_No between" +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ? and inCurrentRoute = 1) and " +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ? and inCurrentRoute = 1) and " +
                "inCurrentRoute = 1;";
        // Find the seats that are already occupied for some station between Source and Destination.
        String query2 = "select distinct Seat_No from Vacancy_Info as V join Booking_Info as B where V.Train_ID = ? and " +
                "V.Date = ? and B.Booking_Status = 'Confirmed' and Seat_No like '" + coachCode + "%' and V.Station in (";
        // Find the number of coaches and seats of the type the user wants to book.
        String query3 = "select " + coach + "_Coaches, " + coach + "_Seats from Basic_Train_Info where Train_ID = ?";
        // Insert the booking details in the database.
        String query4 = "insert into Booking_Info(Booking_ID, PNR, User_ID, Passenger_Name, Passenger_Age, " +
                "Passenger_Gender, Booking_Status) values (?, ?, ?, ?, ?, ?, ?);";
        // Insert the details of the seats occupied and the stations for which they are occupied.
        String query5 = "insert into Vacancy_Info(Train_ID, Booking_ID, Date, Station, Station_No, Seat_No) values" +
                "(?, ?, ?, ?, ?, ?);";
        // Check how much this particular user has spent in past. This is to offer a discount.
        String query6 = "select Total_Spend from User where User_ID = ?;";
        // Update the spent amount with the amount of this particular booking.
        String query7 = "update User set Total_Spend = Total_Spend + ? where User_ID = ?;";

        BookingResponse bookingResponse = bookSeats(query1, query2, query3, query4, query5, query6, query7);
        Server.SendResponse(oos, bookingResponse);
    }

    /**
     * This is where the statements are executed.
     * @return Response to the booking request.
     */
    private @Nullable BookingResponse bookSeats(String query1, String query2, String query3, String query4, String query5,
                                                String query6, String query7) {
        try {
            PreparedStatement stations = connection.prepareStatement(query1);
            stations.setString(1, trainID);
            stations.setString(2, trainID);
            stations.setString(3, bookingRequest.getSource());
            stations.setString(4, trainID);
            stations.setString(5, bookingRequest.getDestination());
            ResultSet route = stations.executeQuery();
            while(route.next()) {
                if(sourceStationNumber == -1)
                    sourceStationNumber = route.getInt("Station_No");
                @SuppressWarnings("assignment.type.incompatible") // Station cannot be null. Refer the README.
                @NonNull String s = route.getString("Station");
                stationsOnRoute.add(s);
                query2 = query2 + "?, ";
            }
            query2 = query2.substring(0, query2.length()-2) + ");";
            System.out.println(query2);
            PreparedStatement occupied = connection.prepareStatement(query2);
            occupied.setString(1, trainID);
            occupied.setString(2, bookingRequest.getDate().toString());
            for(int i = 0; i < stationsOnRoute.size(); i++) {
                occupied.setString(i+3, stationsOnRoute.get(i));
            }
            System.out.println(occupied.toString());
            ResultSet occupiedSeats = occupied.executeQuery();
            PreparedStatement arrangementQuery = connection.prepareStatement(query3);
            arrangementQuery.setString(1, trainID);
            ResultSet coachArrangement = arrangementQuery.executeQuery();
            coachArrangement.next();
            numCoaches = coachArrangement.getInt(coach + "_Coaches");
            seatsPerCoach = coachArrangement.getInt(coach + "_Seats");
            initVacantArray();
            while(occupiedSeats.next()) {
                int coachNumber = 0, seat = 0;
                try {
                    @SuppressWarnings("assignment.type.incompatible") // Seat_No cannot be null. Refer the README.
                    @NonNull String s = occupiedSeats.getString("Seat_No");
                    coachNumber = Integer.parseInt(String.valueOf(s.charAt(2)));
                    seat = Integer.parseInt(s.substring(3));
                    System.out.println(coachNumber + " " + seat);
                } catch (NumberFormatException e) {
                    notVacant[0] = true;
                }
                try {
                    notVacant[(coachNumber-1)*seatsPerCoach + seat] = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    notVacant[0] = true;
                }
            }
            try {
                // Discounting from total cost.
                PreparedStatement getPreviousSpent = connection.prepareStatement(query6);
                getPreviousSpent.setString(1, userID);
                ResultSet previousSpent = getPreviousSpent.executeQuery();
                previousSpent.next();
                int prev = previousSpent.getInt("Total_Spend");
                if(prev < 5000) {
                    // no discount
                } else if(prev < 20000) {
                    // 10%
                    totalCost = (int) (0.9 * totalCost);
                } else if(prev < 40000) {
                    // 20%
                    totalCost = (int) (0.8 * totalCost);
                } else {
                    // 30%
                    totalCost = (int) (0.7 * totalCost);
                }
            } catch (SQLException e) {
                totalCost = totalCost;
            }
            PreparedStatement updateSpent = connection.prepareStatement(query7);
            updateSpent.setInt(1, totalCost);
            updateSpent.setString(2, userID);
            updateSpent.executeUpdate();
            BookingResponse bookingResponse;
            if(bookingRequest.getNumSeat() == 1 && bookingRequest.getGender()[0] == 'F')
                bookingResponse = allotSingleFemale();
            else
                bookingResponse = allot();
            PreparedStatement updateBookingInfo = connection.prepareStatement(query4);
            PreparedStatement updateVacancyInfo = connection.prepareStatement(query5);
            int c = 1;
            for(int i = 0; i < numSeats; i++) {
                updateBookingInfo.setString(1, bookingResponse.getBookingIds()[i]);
                updateBookingInfo.setString(2, bookingResponse.getPnr());
                updateBookingInfo.setString(3, userID);
                updateBookingInfo.setString(4, bookingRequest.getName()[i]);
                updateBookingInfo.setInt(5, bookingRequest.getAge()[i]);
                updateBookingInfo.setString(6, String.valueOf(bookingRequest.getGender()[i]));
                updateBookingInfo.setString(7, i < Math.min(numSeats, availableSeats) ? "Confirmed"
                        : "Waiting");
                c *= updateBookingInfo.executeUpdate();
                int st = sourceStationNumber;
                for(String station : stationsOnRoute) {
                    updateVacancyInfo.setString(1, trainID);
                    updateVacancyInfo.setString(2, bookingResponse.getBookingIds()[i]);
                    updateVacancyInfo.setString(3, bookingRequest.getDate().toString());
                    updateVacancyInfo.setString(4, station);
                    updateVacancyInfo.setInt(5, st++);
                    updateVacancyInfo.setString(6, bookingResponse.getSeatsAlloted()[i]);
                    c *= updateVacancyInfo.executeUpdate();
                }
            }
            return bookingResponse;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @EnsuresNonNull("notVacant")
    private void initVacantArray() {
        notVacant = new boolean[numCoaches*seatsPerCoach + 5];
    }

    int p = 0;

    /**
     * Seat distribution logic. Seats are alloted on the basis of age and preference.
     * @return Response to the booking request.
     */
    @RequiresNonNull("notVacant")
    private BookingResponse allot() {

        int[] age = bookingRequest.getAge();
        String[] preference = bookingRequest.getPreference();
        String[] bookingID = new String[numSeats];
        String[] seatNo = new String[numSeats];
        int confirmedSeats = Math.min(numSeats, availableSeats);
        String pnr = randomPNRGenerator();

        if(coachCode.equals("SL") || coachCode.equals("A3")) {
            // Upper, Middle, Lower, Side Upper and Side Lower.
            for(int i = 0; i < numSeats; i++) {
                p = i;
                if(age[i] <= 15) {
                    // Male Female does not matter.
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = coachCode + allot8(1, 6, 3, 5, 7, 4, 2, 8);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot8(2, 8, 4, 3, 5, 7, 1, 6);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot8(3, 5, 7, 4, 2, 8, 1, 6);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot8(4, 2, 8, 5, 7, 3, 1, 6);
                    } else if(preference[i].equals("Middle")) {
                        seatNo[i] = coachCode + allot8(5, 7, 3, 4, 2, 8, 1, 6);
                    }
                } else if(age[i] <= 50) {
                    if(preference[i].equals("Lower")) {
                        System.out.println("SL lower between 50 and 15");
                        seatNo[i] = coachCode + allot8(1, 6, 2, 8, 5, 7, 4, 3);
                        System.out.println("Alloted seat was " + seatNo[i]);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot8(2, 8, 5, 7, 4, 1, 6, 3);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot8(3, 2, 8, 5, 7, 4, 1, 6);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot8(4, 5, 7, 1, 6, 2, 8, 3);
                    } else if(preference[i].equals("Middle")) {
                        seatNo[i] = coachCode + allot8(5, 7, 4, 2, 8, 1, 6, 3);
                    }
                } else {
                    // Here too, male female does not matter.
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = coachCode + allot8(1, 6, 3, 4, 5, 7, 2, 8);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot8(2, 8, 1, 6, 3, 4, 5, 7);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot8(3, 1, 6, 4, 5, 7, 2, 8);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot8(4, 1, 6, 5, 7, 3, 2, 8);
                    } else if(preference[i].equals("Middle")) {
                        seatNo[i] = coachCode + allot8(5, 7, 1, 6, 4, 3, 2, 8);
                    }
                }
                bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                System.out.println("Booking id was " + bookingID[i]);
            }
        } else if(coachCode.equals("A2")) {
            // Upper, Lower, Side Upper and Side Lower.
            for(int i = 0; i < numSeats; i++) {
                p = i;
                if(age[i] <= 15) {
                    // Male Female does not matter.
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = coachCode + allot6(1, 5, 4, 3, 2, 6);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot6(2, 6, 4, 1, 5, 3);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot6(3, 4, 2, 6, 1, 5);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot6(4, 3, 2, 6, 1, 5);
                    }
                } else if(age[i] <= 50) {
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = coachCode + allot6(1, 5, 4, 3, 2, 6);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot6(2, 6, 4, 3, 1, 5);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot6(3, 4, 2, 6, 1, 5);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot6(4, 3, 1, 5, 2, 6);
                    }
                } else {
                    // Here too, male female does not matter.
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = coachCode + allot6(1, 5, 3, 4, 2, 6);
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = coachCode + allot6(2, 6, 1, 5, 3, 4);
                    } else if(preference[i].equals("Side Lower")) {
                        seatNo[i] = coachCode + allot6(3, 1, 5, 4, 2, 6);
                    } else if(preference[i].equals("Side Upper")) {
                        seatNo[i] = coachCode + allot6(4, 1, 5, 3, 2, 6);
                    }
                }
                bookingID[i] = randomBookingIDGenerator() + seatNo[i];
            }
        } else if(coachCode.equals("A1")){
            for(int i = 0; i < numSeats; i++) {
                p = i;
                if(age[i] <= 15) {
                    // Male Female does not matter.
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = ("A1"+allot2(1, 2));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = ("A1"+allot2(2, 1));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    }
                } else if(age[i] <= 50) {
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = ("A1"+allot2(1, 2));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = ("A1"+allot2(2, 1));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    }
                } else {
                    if(preference[i].equals("Lower")) {
                        seatNo[i] = ("A1"+allot2(1, 2));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    } else if(preference[i].equals("Upper")) {
                        seatNo[i] = ("A1"+allot2(2, 1));
                        bookingID[i] = randomBookingIDGenerator() + seatNo[i];
                    }
                }
            }
        }
        for(int i = Math.min(Math.max(0, availableSeats), numSeats); i < numSeats; i++) {
            bookingID[i] = randomBookingIDGenerator() + seatNo[i];
        }
        return new BookingResponse(bookingID, seatNo, confirmedSeats, pnr, totalCost);
    }

    /**
     * This is to allot seats in First AC coach, where there are only two types of seats. What seat number corresponds to
     * what seat is specified just inside the function.
     * @param a 1 or 2, implies which type of seat is checked first.
     * @param b 1 or 2, implies which type of seat is checked first.
     * @return Seat number which was alloted. The number is of the format coach + coachNo + seatNo for eg, SL101.
     */
    @RequiresNonNull("notVacant")
    private String allot2(int a, int b) {
        // 4 seats
        // 1 2 3 4. odd is lower. even is upper.
        for(int i = 1; i <= numCoaches; i++) {
            for(int j = a; j <= seatsPerCoach; j+=2) {
                if(!notVacant[(i-1)*seatsPerCoach + j]) {
                    notVacant[(i-1)*seatsPerCoach + j] = true;
                    return String.valueOf(i) + (j > 9 ? j : "0"+j);
                }
            }
            for(int j = b; j <= seatsPerCoach; j+=2) {
                if(!notVacant[(i-1)*seatsPerCoach + j]) {
                    notVacant[(i-1)*seatsPerCoach + j] = true;
                    return String.valueOf(i) + (j > 9 ? j : "0"+j);
                }
            }
        }
        return "WL" + currentWaiting++;
    }

    /**
     * Allot seats in Second AC coach where there are 4 types of seats in a compartment. 2*Upper, 2*lower, 1*side upper
     * and 1*side lower. Thus the six parameters.
     * @params Order of checking the seat vacancy.
     * @return Alloted seat number.
     */
    @RequiresNonNull("notVacant")
    private String allot6(int a, int b, int c, int d, int e, int f) {
        // 6 seats.
        // 1 and 5 are lower. 2 and 6 are upper. 3 is side lower. 4 is side upper.
        ArrayList<Integer> order = new ArrayList<>();
        order.add(a);
        order.add(b);
        order.add(c);
        order.add(d);
        order.add(e);
        order.add(f);
        for(int x : order) {
            for(int i = 1; i <= numCoaches; i++) {
                for(int j = x; j <= seatsPerCoach; j+=8) {
                    if(!notVacant[(i-1)*seatsPerCoach + j] && (j != 1 || bookingRequest.getQuota()[p].equals("Viklang"))) {
                        notVacant[(i-1)*seatsPerCoach + j] = true;
                        return String.valueOf(i) + (j > 9 ? j : "0"+j);
                    }
                }
            }
        }
        return "WL" + currentWaiting++;
    }

    /**
     * Allot seats in Sleeper or Third AC coach where there are 4 types of seats in a compartment. 2*Upper, 2*lower,
     * 2*middle, 1*side upper and 1*side lower. Thus the eight parameters.
     * @params Order of checking the seat vacancy.
     * @return Alloted seat number.
     */
    @RequiresNonNull("notVacant")
    private String allot8(int a, int b, int c, int d, int e, int f, int g, int h) {
        // 8 seats in a compartment.
        // 1 and 6 are lower. 2 and 8 are upper.5 and 7 are middle. 3 is side lower. 4 is side upper.
        ArrayList<Integer> order = new ArrayList<>();
        order.add(a);
        order.add(b);
        order.add(c);
        order.add(d);
        order.add(e);
        order.add(f);
        order.add(g);
        order.add(h);
        for(int x : order) {
            for(int i = 1; i <= numCoaches; i++) {
                for(int j = x; j <= seatsPerCoach; j+=8) {
                    if(!notVacant[(i-1)*seatsPerCoach + j] && (j != 1 || bookingRequest.getQuota()[p].equals("Viklang"))) {
                        System.out.println("alloting seat " + j + " in coach " + i + " since notVacant is " + notVacant[(i-1)*seatsPerCoach + j]);
                        notVacant[(i-1)*seatsPerCoach + j] = true;
                        return String.valueOf(i) + (j > 9 ? j : "0"+j);
                    }
                }
            }
        }
        System.out.println("Could not allot a seat so returning WL");
        return "WL" + currentWaiting++;
    }

    /**
     * Allot nearby seats to female passengers if they are travelling alone.
     * @return Response to the booking request.
     */
    @RequiresNonNull("notVacant")
    private BookingResponse allotSingleFemale() {
        // We will give them what they want, starting from the back of the coach.
        String[] bookingID = new String[1], seat = new String[1];
        int confirmedSeats = 0;
        String pnr = randomPNRGenerator();
        for(int coach = 1; coach <= numCoaches; coach++) {
            for(int i = seatsPerCoach; i >= 1; i--) {
                if(!notVacant[(coach-1)*seatsPerCoach]) {
                    seat[confirmedSeats] = coachCode + coach + (i < 10 ? "0" + i : i);
                    notVacant[(coach-1)*seatsPerCoach] = true;
                    bookingID[confirmedSeats] = randomBookingIDGenerator() + seat[confirmedSeats];
                    confirmedSeats++;
                }
            }
        }
        if(confirmedSeats > 0)
            return new BookingResponse(bookingID, seat, confirmedSeats, pnr, totalCost);
        else {
            seat[0] = coachCode + "WL" + currentWaiting++;
            bookingID[0] = randomBookingIDGenerator() + seat[0];
            return new BookingResponse(bookingID, seat, confirmedSeats, pnr, totalCost);
        }
    }

    // Random ID generator. No special logic.
    final private static char[] base36Char = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
    final private static Random random = new Random();
    public static String randomBookingIDGenerator() {
        String id = "";
        for(int i = 0; i < 4; i++) {
            id += base36Char[random.nextInt(36)];
        }
        return id;
    }
    public static String randomPNRGenerator() {
        String id = "";
        for(int i = 0; i < 5; i++) {
            id += base36Char[random.nextInt(36)];
        }
        return id;
    }
}