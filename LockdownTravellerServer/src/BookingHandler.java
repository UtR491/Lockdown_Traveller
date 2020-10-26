import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingHandler {


    String Train_Name;
    String time;
    int PNR;
    int BookindID;
    int seatno;
    int AC3uppercount = 3, AC3middlecount = 2, AC3lowercount = 1;


    public void BookingHandler(int AC3uppercount, int AC3middlecount, int AC3lowercount) {
        this.AC3uppercount = AC3uppercount;
        AC3uppercount = 3;
        this.AC3middlecount = AC3middlecount;
        AC3middlecount = 2;
        this.AC3lowercount = AC3lowercount;
        AC3lowercount = 1;


    }

    public void allotSeatno() {
        Server.getConnection();

        BookingRequest bookingRequest = null;
        String trainId = bookingRequest.getTrainId();
        Date date = bookingRequest.getDate();
        String source = bookingRequest.getSource();
        String destination = bookingRequest.getDestination();
        String coach = bookingRequest.getCoach();

        String[] preference = bookingRequest.getPreference();
        int numSeat = bookingRequest.getNumSeat();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String convertedDate = df.format(date);
        String userId = bookingRequest.getUserId();
        int availableSeat = bookingRequest.getAvailableSeat();
        int[] age = bookingRequest.getAge();
        String[] name = bookingRequest.getName();
        char[] gender = bookingRequest.getGender();
        long bookingIds = 0; // booking ID code

        //
        int i = 0, n, j;
        n = bookingRequest.getNumSeat();
        if (n==1)
        {
            if(gender[0]=='F') {
                try {
                    for (i = 22; i <= 23; i++) {
                        PreparedStatement allot_female = Server.getConnection().prepareStatement("update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_female.executeQuery();
                        ResultSet resultSet = allot_female.executeQuery();
                        i= checkSet(resultSet,i,convertedDate,trainId);
                    }
                }catch(SQLException throwables){
                    throwables.printStackTrace();
                }
            }
        }
        if (preference[0] =="Special quota") {
            try {
                i=24;
                PreparedStatement allot_specialQuota= Server.getConnection().prepareStatement("update Vacancy_Info set Seat_No= ? " +
                        "where  + Train_ID=? and Booking_ID= ? and Date=?");
                allot_specialQuota.executeUpdate();
                ResultSet resultSet = allot_specialQuota.executeQuery();
                i= checkSet(resultSet,i,convertedDate,trainId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        for (i = 1; i <= 6; i++) {
            for (j = 0; j < n; j++) {
                try {
                    PreparedStatement preparedStatement = Server.getConnection().prepareStatement("select Station from Route_Info where " +
                            "Train_Id =? order by Station_no");
//                    for loop to be added
                    preparedStatement.setString(1, trainId);
                    preparedStatement.executeQuery();
                    ResultSet station_on_Route= preparedStatement.executeQuery();
                    while (station_on_Route.next()) {
                        boolean station=true;
                        if (station) {
                            List<String> list = new ArrayList<String>();
                            for (String route : list) {
                                list.add(route);

                            }
                        }
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (10 <= age[j] || age[j] < 30) {
                    if(coach=="2AC"){
                        if(coach=="1AC"){
                            break;
                        }
                        break;
                    }
                    if(coach=="SL") {
                        if (coach == "3AC") {
                            i++;
                        seatno = 3 * i;

                        try {
                            PreparedStatement allot_upperBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                    "where  + Train_ID=? and Booking_ID= ? and Date=?");
                            allot_upperBerth.setInt(1, seatno);
                            allot_upperBerth.setString(2, trainId);
                            allot_upperBerth.setLong(3, bookingIds);
                            allot_upperBerth.setString(4, convertedDate);
                            allot_upperBerth.executeUpdate();
//                        coach to be added in the tables
                            ResultSet resultSet = allot_upperBerth.executeQuery();
                            i = checkSet(resultSet, i, convertedDate, trainId);

                            if (i == 6) {
                                seatno = 2 * i;
                                allot_upperBerth.setInt(1, seatno);
                                i = checkSet(resultSet, i, convertedDate, trainId);
                            } else {
                                seatno = i;
                                allot_upperBerth.setInt(1, seatno);
                                i = checkSet(resultSet, i, convertedDate, trainId);
                            }
                            allot_upperBerth.executeUpdate();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }  } }



                else if (30 <= age[j] || age[j] < 45) {
                    if(coach=="2AC"){
                        if(15 <= age[j] || age[j] < 45){
                            i++;
                            seatno = 2 * i;
                        }
                    }
                    i++;
                    seatno = 2 * i;
                    try {
                        PreparedStatement allot_middleBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_middleBerth.setInt(1, seatno);
                        allot_middleBerth.setString(2, trainId);
                        allot_middleBerth.setLong(3, bookingIds);
                        allot_middleBerth.setString(4, convertedDate);
//                        coach to be added in the tables
                        ResultSet resultSet = allot_middleBerth.executeQuery();
                        i= checkSet(resultSet,i,convertedDate,trainId);
                         if (i == 6) {
                             if(coach == "2AC"){
                                 break;
                             }
                            seatno = 3 * i;
                            allot_middleBerth.setInt(1, seatno);
                            i= checkSet(resultSet,i,convertedDate,trainId);
                        }
                         else {
                             seatno = i;
                             allot_middleBerth.setInt(1, seatno);
                             i= checkSet(resultSet,i,convertedDate,trainId);
                         }
                         allot_middleBerth.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    i++;
                    seatno = i;
                    try {
                        PreparedStatement allot_lowerBerth = Server.getConnection().prepareStatement(" update Vacancy_Info set Seat_No= ? " +
                                "where  + Train_ID=? and Booking_ID= ? and Date=?");
                        allot_lowerBerth.setInt(1, seatno);
                        allot_lowerBerth.setString(2, trainId);
                        allot_lowerBerth.setLong(3, bookingIds);
                        allot_lowerBerth.setString(4, convertedDate);
//                        coach to be added in the tables
                        ResultSet resultSet = allot_lowerBerth.executeQuery();
                        i= checkSet(resultSet,i,convertedDate,trainId);
                        if (i == 6) {
                            if(coach=="1AC"){
                                break;
                            }
                            seatno = 2 * i;
                            allot_lowerBerth.setInt(1, seatno);
                            i= checkSet(resultSet,i,convertedDate,trainId);
                        }
                        else {
                            if(coach=="2AC"){
                                if(coach=="1AC"){
                                    break;
                                }
                                break;
                            }
                            seatno = 3 * i;
                            allot_lowerBerth.setInt(1, seatno);
                            i= checkSet(resultSet,i,convertedDate,trainId);
                        }
                        allot_lowerBerth.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                continue;
            }

        }
        Date todayDate= new Date();
        boolean before = todayDate.before(date);
        if(before==true){
            for(i=22;i<=24;i++) {
                for (j = 0; j < n; j++)
                try{
                    PreparedStatement allotBerth = Server.getConnection().prepareStatement("select Station from Route_Info where " +
                            "Train_Id =? and between source = ? and destination =? order by Station_no");
//                    for loop to be added
                    allotBerth.setString(1, trainId);
                    allotBerth.setString(2, source);
                    allotBerth.setString(3, destination);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
            }
        }



     public int checkSet(ResultSet resultSet, int i, String convertedDate, String trainId) throws SQLException {

        while (resultSet.next()) {
            int checkSeatno = (resultSet.getInt("Seat_No"));
            String checkDate = (resultSet.getString("Date"));
            String checkTrainId = (resultSet.getString("TrainID"));
            if (checkSeatno == seatno) {
                if (checkDate.equals(convertedDate)) {
                    if (checkTrainId.equals(trainId)) {
                        i++;
                    }
                }

            }
        }
        return i;
    }
}








