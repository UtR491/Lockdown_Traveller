import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelBookingRequestHandler extends Handler
{
    Connection connection;
    CancelBookingRequest cb;
    ObjectOutputStream oos;
    CancelBookingRequestHandler(Connection connection,CancelBookingRequest cb,ObjectOutputStream oos)
    {
        this.connection=connection;
        this.cb=cb;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws  SQLException {
        System.out.println("Inside Handler's get response method");
        String query1="update Booking_Info set Booking_Status='Cancelled' where PNR=? and User_ID =? and Booking_Status <> 'Cancelled';";
        String query2="select Booking_ID from Booking_Info where PNR=?;";
//        //query to see if there is any waiting tkt between those stations,and allot them the cancelled seats


        //query3will give us train id , date and coach type of the cancelled tkt
       String query3="select Train_ID,Date,Station,Station_No,Seat_No from Vacancy_Info where Booking_ID=?;";
       //query4 will get us source station of waiting list customer
       String query4="select distinct Station from Vacancy_Info where Train_ID=? and Date=? and Seat_No like ? and Station_No in (select MAX(Station_No) from Vacancy_Info where Train_ID=? and Date=? and Seat_No like ?);";
       // query5 will get us the destination station of waiting list customer
       String query5="select distinct Station from Vacancy_Info where Train_ID=? and Date=? and Seat_No like ? and Station_No in (select MIN(Station_No) from Vacancy_Info where Train_ID=? and Date=? and Seat_No like ?);";


       //now we will be checking the availability of seats in that train for that particular day and in the given coach,if the seat is vacant then we will change the seat number to the cancelled seat number
        String query6="select distinct Booking_ID from Vacancy_Info where Train_ID=? and Date=? and Seat_No like ?;";
        String query7="update Vacancy_Info set Seat_No=? where Train_ID=? and Date=? and Station=? and Booking_ID =? ";
        String query8="update Booking_Info set Booking_Status= 'Confirmed' where Booking_ID=?;";
        String  query9="select User_ID from Booking_Info where Booking_ID=?;";

      //sending notifications regarding the cancelled and cleared waiting list
        String query10="insert into notification(?,?,1);";

        CancelBookingResponse cancelBookingResponse = CancelBooking(query1,query10,cb);
        clearWaitingList(query2,query3,query4,query5,query6,query7,query8,query9,query10,cb);
        Server.SendResponse(oos,cancelBookingResponse);
    }

    public CancelBookingResponse CancelBooking(String query1,String query10,CancelBookingRequest cb) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(query1);
        preparedStatement.setString(1,cb.getPNR());
        preparedStatement.setString(2,cb.getUserId());
        int result=preparedStatement.executeUpdate();
        String response = null;
        if(result==0){response="failure";}
        else if(result>0){
            response="success";
            preparedStatement=connection.prepareStatement(query10);
            preparedStatement.setString(1,cb.getUserId());
            preparedStatement.setString(2,"Your booking with PNR "+cb.getPNR()+"has been cancelled");
        }
        return new CancelBookingResponse(response);
    }
    public void clearWaitingList(String query2,String query3,String query4,String query5,String query6,String query7,String query8,String query9,String query10,CancelBookingRequest cb) throws SQLException {
        PreparedStatement preparedStatement;

        preparedStatement=connection.prepareStatement(query2);
        preparedStatement.setString(1,cb.getPNR());
        ResultSet bookingID=preparedStatement.executeQuery();


        while(bookingID.next()) {
            preparedStatement = connection.prepareStatement(query3);
            preparedStatement.setString(1, bookingID.getString(1));
            ResultSet trainInfo = preparedStatement.executeQuery();
            trainInfo.next();
            String coach=trainInfo.getString(4).substring(0,1);

            preparedStatement=connection.prepareStatement(query4);
            preparedStatement.setString(1,trainInfo.getString(1));
            preparedStatement.setString(2,trainInfo.getString(2));
            preparedStatement.setString(3,coach.concat("WL%"));
            preparedStatement.setString(4,trainInfo.getString(1));
            preparedStatement.setString(5,trainInfo.getString(2));
            preparedStatement.setString(6,coach.concat("WL%"));
            ResultSet destination=preparedStatement.executeQuery();
            destination.next();


            preparedStatement=connection.prepareStatement(query5);
            preparedStatement.setString(1,trainInfo.getString(1));
            preparedStatement.setString(2,trainInfo.getString(2));
            preparedStatement.setString(3,coach.concat("WL%"));
            preparedStatement.setString(4,trainInfo.getString(1));
            preparedStatement.setString(5,trainInfo.getString(2));
            preparedStatement.setString(6,coach.concat("WL%"));
            ResultSet source=preparedStatement.executeQuery();
            source.next();
// now we will check the availability of seats
           DisplayTrainsRequestHandler displayTrainsRequestHandler=new DisplayTrainsRequestHandler();
            // query to check the expiration of reroute period
            String q1="select Rerouted_Till,Train_ID from Basic_Train_Info where Rerouted_Till is not null;";
            String q2="select Train_ID from Basic_Train_Info where Train_ID=?";
            String q3="delete from Route_Info where Train_ID=? and inCurrentRoute=1;";
            String q4="update Route_Info set inCurrentRoute=1 where Train_ID=? and inCurrentRoute=0;";
            String q5="update Basic_Train_Info set Rerouted_Till = null where Train_ID=?;";
            displayTrainsRequestHandler.checkRerouteStatus(q1,q2,q3,q4,q5);


            //create a query to find the trains between source and destination
            String Q1 = "select x.*, y.Days_Running from\n" +
                    "(select a.Train_ID, a.Train_Name, b.Arrival, a.Departure, a.Day_No from \n" +
                    "(select * from Route_Info\n" +
                    "where Station=? and inCurrentRoute=1) as a\n" +
                    "join \n" +
                    "(select * from Route_Info\n" +
                    "where Station=? and inCurrentRoute=1) as b\n" +
                    "where a.Train_ID = b.Train_ID) as x\n" +
                    "join \n" +
                    "Basic_Train_Info as y\n" +
                    "on x.Train_ID = y.Train_ID;\n" +
                    "\n";
            //create query to find total seats in each class
            String Q2 = "select Sleeper_Coaches,Sleeper_Seats,FirstAC_Coaches,FirstAC_Seats,SecondAC_Coaches,SecondAC_Seats,ThirdAC_Coaches,ThirdAC_Seats,Sleeper_Fare,FirstAC_Fare,SecondAC_Fare,ThirdAC_Fare from Basic_Train_Info where Train_ID=?;";

            String Q3 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'SL%' and Seat_No not like '%null'));";
            String Q4 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'A1%' and Seat_No not like '%null'));";
            String Q5 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'A2%' and Seat_No not like '%null'));";
            String Q6 = "select count(Booking_ID) from Booking_Info where Booking_Status<>'Cancelled' and Booking_ID in(select distinct Booking_ID from Vacancy_Info where Train_ID=? and Station_No in (select Station_No from Route_Info where Train_ID= ? and Station_No between (select Station_No from Route_Info where Train_ID=? and Station=?) and (select Station_No from Route_Info where Train_ID=? and Station=?) and Date=? and Seat_No like 'A3%' and Seat_No not like '%null'));";
            String Q7 = "select Added_Till,Cancelled_Till from Basic_Train_Info where Train_ID=?;";
            String Q8="select Distance_Covered from Route_Info where Train_ID=? and inCurrentRoute=1 and Station in (?,?);";

            DisplayTrainsResponse displayTrainsResponse = displayTrainsRequestHandler.DisplayTrains(Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,trainInfo.getString(1),source.getString(1),destination.getString(1));
            int vacantseats;
            if(coach.equals("SL"))
            {
                vacantseats= Integer.parseInt(displayTrainsResponse.getSleeper().get(displayTrainsResponse.getTrain_ID().indexOf(trainInfo.getString(1))));
            }
            else  if(coach.equals("A1"))
            {
                vacantseats= Integer.parseInt(displayTrainsResponse.getFirst_AC().get(displayTrainsResponse.getTrain_ID().indexOf(trainInfo.getString(1))));
            }
            else if(coach.equals("A2"))
            {
                vacantseats= Integer.parseInt(displayTrainsResponse.getSecond_AC().get(displayTrainsResponse.getTrain_ID().indexOf(trainInfo.getString(1))));
            }
            else if(coach.equals("A3"))
            {
                vacantseats= Integer.parseInt(displayTrainsResponse.getThird_AC().get(displayTrainsResponse.getTrain_ID().indexOf(trainInfo.getString(1))));
            }
            else
            {
                vacantseats=-10000;
            }
            if(vacantseats>0)
                 preparedStatement=connection.prepareStatement(query6);
                preparedStatement.setString(1,trainInfo.getString(1));
                preparedStatement.setString(2,trainInfo.getString(2));
                preparedStatement.setString(3,coach.concat("WL%"));
                ResultSet bookingid=preparedStatement.executeQuery();
                bookingid.next();

                preparedStatement=connection.prepareStatement(query7);
                preparedStatement.setString(1,trainInfo.getString(5));
                preparedStatement.setString(2,trainInfo.getString(1));
                preparedStatement.setString(3,trainInfo.getString(3));
                preparedStatement.setString(4,bookingid.getString(1));
                int a=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query8);
                preparedStatement.setString(1,bookingid.getString(1));
                int b=preparedStatement.executeUpdate();

                if(a*b!=0)
                {
                    preparedStatement=connection.prepareStatement(query9);
                    preparedStatement.setString(1,bookingid.getString(1));
                    ResultSet userID=preparedStatement.executeQuery();
                    userID.next();

                    preparedStatement=connection.prepareStatement(query10);
                    preparedStatement.setString(1,userID.getString(1));
                    preparedStatement.setString(2,"Your Booking with Booking ID: "+bookingid.getString(1)+" from "+source.getString(1)+" to"+destination.getString(1)+" on "+trainInfo.getString(2)+" has been confirmed. Your Seat Number is "+trainInfo.getString(5)+".");
                    preparedStatement.executeUpdate();
                }

            }
        }
    }

