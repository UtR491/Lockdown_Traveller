import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class BookingHandler {
    private final Connection connection;
    private final BookingRequest bookingRequest;
    private final ObjectOutputStream oos;
    final private String coach, trainID, userID;
    private String coachCode;
    private boolean[] notVacant;
    private int numCoaches, seatsPerCoach, numSeats, availableSeats, sourceStationNumber=-1, totalCost;
    private ArrayList<String> stationsOnRoute = new ArrayList<>();
    public BookingHandler(Connection connection, BookingRequest bookingRequest, ObjectOutputStream oos) {
        this.connection = connection;
        this.bookingRequest = bookingRequest;
        this.oos = oos;
        trainID = this.bookingRequest.getTrainId();
        coach = this.bookingRequest.getCoach();
        userID = this.bookingRequest.getUserId();
        totalCost = this.bookingRequest.getTotalCost();
    }

    public void sendQuery() {
        numSeats = bookingRequest.getNumSeat();
        availableSeats = bookingRequest.getAvailableSeat();
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

        String query1 = "select Station, Station_No from Route_Info where Train_ID = ? and Station_No between" +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ? and inCurrentRoute = 1) and " +
                "(select Station_No from Route_Info where Train_ID = ? and Station = ? and inCurrentRoute = 1) and " +
                "inCurrentRoute = 1;";
        String query2 = "select distinct Seat_No from Vacancy_Info as V join Booking_Info as B where V.Train_ID = ? and " +
                "V.Date = ? and B.Booking_Status = 'Confirmed' and Seat_No like '" + coachCode + "%' and V.Station in (";
        String query3 = "select " + coach + "_Coaches, " + coach + "_Seats from Basic_Train_Info where Train_ID = ?";
        String query4 = "insert into Booking_Info(Booking_ID, PNR, User_ID, Passenger_Name, Passenger_Age, " +
                "Passenger_Gender, Booking_Status) values (?, ?, ?, ?, ?, ?, ?);";
        String query5 = "insert into Vacancy_Info(Train_ID, Booking_ID, Date, Station, Station_No, Seat_No) values" +
                "(?, ?, ?, ?, ?, ?);";
        String query6 = "select Total_Spend from User where User_ID = ?;";
        String query7 = "update User set Total_Spend = Total_Spend + ? where User_ID = ?;";
        BookingResponse bookingResponse = bookSeats(query1, query2, query3, query4, query5, query6, query7);
        Server.SendResponse(oos, bookingResponse);
    }

    private BookingResponse bookSeats(String query1, String query2, String query3, String query4, String query5,
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
                stationsOnRoute.add(route.getString("Station"));
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
            notVacant = new boolean[numCoaches*seatsPerCoach + 5];
            while(occupiedSeats.next()) {
                int coachNumber = 0, seat = 0;
                try {
                    coachNumber = Integer.parseInt(String.valueOf(occupiedSeats.getString("Seat_No").charAt(2)));
                    seat = Integer.parseInt(occupiedSeats.getString("Seat_No").substring(3));
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
                if(i < Math.min(numSeats, availableSeats)) {
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
            }
            return bookingResponse;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    int p = 0;
    private BookingResponse allot() {
        int[] age = bookingRequest.getAge();
        String[] preference = bookingRequest.getPreference();

        String[] bookingID = new String[numSeats];
        String[] seatNo = new String[numSeats];
        int confirmedSeats = Math.min(numSeats, availableSeats);
        String pnr = randomPNRGenerator();

        if(coachCode.equals("SL") || coachCode.equals("A3")) {
            // Upper, Middle, Lower, Side Upper and Side Lower.
            for(int i = 0; i < Math.min(availableSeats, numSeats); i++) {
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
                        seatNo[i] = coachCode + allot8(1, 6, 2, 8, 5, 7, 4, 3);
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
            }
        } else if(coachCode.equals("A2")) {
            // Upper, Lower, Side Upper and Side Lower.
            for(int i = 0; i < Math.min(availableSeats, numSeats); i++) {
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
            for(int i = 0; i < Math.min(availableSeats, numSeats); i++) {
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
            bookingID[i] = randomBookingIDGenerator() + coachCode + "XXX";
        }
        return new BookingResponse(bookingID, seatNo, confirmedSeats, pnr, totalCost);
    }

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
        return null;
    }

    private String allot6(int a, int b, int c, int d, int e, int f) {
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
        return null;
    }

    private String allot8(int a, int b, int c, int d, int e, int f, int g, int h) {
        // 8 seats combination exists.
        // 2* upper middler lower 1* side upper and side lower
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
        return null;
    }

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
        return new BookingResponse(bookingID, seat, confirmedSeats, pnr, totalCost);
    }
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