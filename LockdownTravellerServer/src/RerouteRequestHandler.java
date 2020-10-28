import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RerouteRequestHandler extends Handler {
    private ObjectOutputStream objectOutputStream;
    private RerouteRequest rerouteRequest;
    private  Connection connection;
    RerouteRequestHandler(Connection connection,RerouteRequest rerouteRequest,ObjectOutputStream objectOutputStream)
    {
        this.connection=connection;
        this.objectOutputStream=objectOutputStream;
        this.rerouteRequest=rerouteRequest;
    }
    @Override
    void sendQuery() throws SQLException {
        // creating duplicates of the train and setting the inCurrent route to 2 for the time being,then we will set inCurrentRoute of the original route to 0 and this to 1
        String query1="insert into Route_Info (Train_ID,Train_Name,Station,Station_No,City_Code,Arrival,Departure,Day_No,Distance_Covered,inCurrentRoute) select Train_ID,Train_Name,Station,Station_No,City_Code,Arrival,Departure,Day_No,Distance_Covered,2 from Route_Info where Train_ID=? and inCurrentRoute=1";
        //updating the rerouted_till column in basic train info
        String query2="update Basic_Train_Info set Rerouted_Till=? where Train_ID=?;";
        //getting the arrival departures,Station  numbers and distances of all the three Stations involved to update
        String query3="select Distance_Covered,Train_Name,Station_No from Route_Info where Train_ID=? and Station in (?,?,?);";
        String query;
        //query depending on whether we have to insert or replace the station
        if(rerouteRequest.getInplace())
        {
            query="update Route_Info set Station=?,Station_No=?,City_Code=?,Arrival=?,Departure=?,Day_No=?,Distance_Covered=? where Train_ID=? and Station=? and inCurrentRoute=2;";
        }
        else{
            query="insert into Route_Info values(?,?,?,?,?,?,?,?,?,2);";
        }
        //updating the first  station after the rerouted station is added,i.e, if we change thr route from A B C to A D C or A B D C, the following query will update Station C
        String query4="update Route_Info set Distance_Covered=?,Arrival=?,Station_No=Station_No+? where Station=? and Train_ID=? and inCurrentRoute=2;";
        //updating the values of all the stations after station C (mentioned in the comment above)
        String query5="update Route_Info set Distance_Covered=Distance_Covered+? and Station_No=Station_No+1 where Station in (select Station from route_info where Train_ID=? and Station_No>?) and Train_ID=? and inCurrentRoute=2;";
        //changing the inCurrentRoute of the old Route to 0
        String query6="update Route_Info set inCurrentRoute=0 where Train_ID=? and inCurrentRoute=1;";
        //changing the inCurrentRoute of the new Route to 1
        String query7="update Route_Info set inCurrentRoute=1 where Train_ID=? and inCurrentRoute=2;";
        //sending the notification of train reroute
        String query8="update notification set Message=?,Pending_Status=1,User_ID=User_ID in (select distinct User_ID from User );";
        RerouteResponse rerouteResponse=rerouteTrains(query1,query2,query3,query,query4,query5,query6,query7,query8);
        Server.SendResponse(objectOutputStream,rerouteResponse);

    }
    public RerouteResponse rerouteTrains(String query1,String query2,String query3,String query,String query4,String query5,String query6,String query7,String query8) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query1);
        preparedStatement.setString(1, rerouteRequest.getTrain_ID());
        int a1 = preparedStatement.executeUpdate();
        String response;
        String sDate = rerouteRequest.getsDate();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sDate = LocalDate.parse(sDate, dtf).format(dtf2);

        preparedStatement = connection.prepareStatement(query2);
        preparedStatement.setString(1, sDate);
        preparedStatement.setString(2, rerouteRequest.getTrain_ID());
        int a2 = preparedStatement.executeUpdate();
        int a3,a4,a5;
        preparedStatement = connection.prepareStatement(query3);
        ResultSet resultSet;
        if (rerouteRequest.getInplace()) {
            if (rerouteRequest.getNextStation() != null) {
                preparedStatement.setString(2, rerouteRequest.getOldStation());
                preparedStatement.setString(3, rerouteRequest.getNextStation());
                if (rerouteRequest.getPrevStation() != null) {
                    preparedStatement.setString(1, rerouteRequest.getPrevStation());
                    resultSet=preparedStatement.executeQuery();
                    resultSet.next();

                    //replacing the old station with new
                    preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setString(1,rerouteRequest.getStation());
                    preparedStatement.setInt(2,resultSet.getInt(3)+1);
                    preparedStatement.setString(3,rerouteRequest.getCity_Code());
                    preparedStatement.setString(4,rerouteRequest.getArrivalD());
                    preparedStatement.setString(5,rerouteRequest.getDepartureD());
                    preparedStatement.setInt(6,rerouteRequest.getDayNo());
                    preparedStatement.setInt(7,resultSet.getInt(1)+rerouteRequest.getDistancePrev());
                    preparedStatement.setString(8,rerouteRequest.getTrain_ID());
                    preparedStatement.setString(9,rerouteRequest.getOldStation());
                    a3=preparedStatement.executeUpdate();

                    //updating the next station
                    preparedStatement=connection.prepareStatement(query4);
                    preparedStatement.setInt(1,rerouteRequest.getDistanceNext()+rerouteRequest.getDistancePrev()+resultSet.getInt(1));

                } else
                {
                    preparedStatement.setString(1, rerouteRequest.getOldStation());
                    resultSet=preparedStatement.executeQuery();
                    resultSet.next();

                    //updating the old station with the new one
                    preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setString(1,rerouteRequest.getStation());
                    preparedStatement.setInt(2,1);
                    preparedStatement.setString(3,rerouteRequest.getCity_Code());
                    preparedStatement.setString(4,null);
                    preparedStatement.setString(5,rerouteRequest.getDepartureD());
                    preparedStatement.setInt(6,rerouteRequest.getDayNo());
                    preparedStatement.setInt(7,0);
                    preparedStatement.setString(8,rerouteRequest.getTrain_ID());
                    preparedStatement.setString(9,rerouteRequest.getOldStation());
                    a3=preparedStatement.executeUpdate();

                    //updating the next station
                    preparedStatement=connection.prepareStatement(query4);
                    preparedStatement.setInt(1,rerouteRequest.getDistanceNext());


                }
                preparedStatement.setString(2,rerouteRequest.getArrivalC());
                preparedStatement.setInt(3,0);
                preparedStatement.setString(4,rerouteRequest.getNextStation());
                preparedStatement.setString(5,rerouteRequest.getTrain_ID());
                a4=preparedStatement.executeUpdate();
                //updating the distances and station number of the following stations
                resultSet.next();
                resultSet.next();
                preparedStatement=connection.prepareStatement(query5);
                preparedStatement.setInt(1,rerouteRequest.getDistanceNext()-resultSet.getInt(1));
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setInt(3,resultSet.getInt(3));
                preparedStatement.setString(4,rerouteRequest.getTrain_ID());
                a5=preparedStatement.executeUpdate();

            }
            else
            {
                preparedStatement.setString(1, rerouteRequest.getPrevStation());
                preparedStatement.setString(2, rerouteRequest.getOldStation());
                preparedStatement.setString(3, rerouteRequest.getOldStation());
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int distPrev = resultSet.getInt(1);
                int stationNo = resultSet.getInt(3) + 1;

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, rerouteRequest.getStation());
                preparedStatement.setInt(2, stationNo);
                preparedStatement.setString(3, rerouteRequest.getCity_Code());
                preparedStatement.setString(4, rerouteRequest.getArrivalD());
                preparedStatement.setString(5, rerouteRequest.getDepartureD());
                preparedStatement.setInt(6, rerouteRequest.getDayNo());
                preparedStatement.setInt(7, distPrev + rerouteRequest.getDistancePrev());
                preparedStatement.setString(8, rerouteRequest.getTrain_ID());
                preparedStatement.setString(9, rerouteRequest.getOldStation());
                 a3=preparedStatement.executeUpdate();
                 a4=1;
                 a5=1;
            }

        }
        else//inserting a station
        {
            preparedStatement.setString(2,rerouteRequest.getOldStation());
            if(rerouteRequest.getNextStation()!=null)
            {
                preparedStatement.setString(3,rerouteRequest.getNextStation());
                if(rerouteRequest.getPrevStation()!=null)
                {
                    preparedStatement.setString(1,rerouteRequest.getPrevStation());
                    resultSet=preparedStatement.executeQuery();
                    resultSet.next();
                    resultSet.next();

                    //replacing the old station with new
                    preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setString(1,rerouteRequest.getStation());
                    preparedStatement.setInt(2,resultSet.getInt(3)+1);
                    preparedStatement.setString(3,rerouteRequest.getCity_Code());
                    preparedStatement.setString(4,rerouteRequest.getArrivalD());
                    preparedStatement.setString(5,rerouteRequest.getDepartureD());
                    preparedStatement.setInt(6,rerouteRequest.getDayNo());
                    preparedStatement.setInt(7,resultSet.getInt(1)+rerouteRequest.getDistanceOld());
                    preparedStatement.setString(8,rerouteRequest.getTrain_ID());
                    preparedStatement.setString(9,rerouteRequest.getOldStation());
                    a3=preparedStatement.executeUpdate();

                    //updating the next station
                    preparedStatement=connection.prepareStatement(query4);
                    preparedStatement.setInt(1,rerouteRequest.getDistanceNext()+rerouteRequest.getDistanceOld()+resultSet.getInt(1));

                }
                else
                {
                 preparedStatement.setString(1,rerouteRequest.getOldStation());
                 resultSet=preparedStatement.executeQuery();
                 resultSet.next();


                    //updating the old station with the new one
                    preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setString(1,rerouteRequest.getStation());
                    preparedStatement.setInt(2,2);
                    preparedStatement.setString(3,rerouteRequest.getCity_Code());
                    preparedStatement.setString(4,rerouteRequest.getArrivalD());
                    preparedStatement.setString(5,rerouteRequest.getDepartureD());
                    preparedStatement.setInt(6,rerouteRequest.getDayNo());
                    preparedStatement.setInt(7,rerouteRequest.getDistanceOld());
                    preparedStatement.setString(8,rerouteRequest.getTrain_ID());
                    preparedStatement.setString(9,rerouteRequest.getOldStation());
                    a3=preparedStatement.executeUpdate();

                    //updating the next station
                    preparedStatement=connection.prepareStatement(query4);
                    preparedStatement.setInt(1,rerouteRequest.getDistanceNext()+rerouteRequest.getDistanceOld());
                }
                preparedStatement.setString(2,rerouteRequest.getArrivalC());
                preparedStatement.setInt(3,1);
                preparedStatement.setString(4,rerouteRequest.getNextStation());
                preparedStatement.setString(5,rerouteRequest.getTrain_ID());
                a4=preparedStatement.executeUpdate();
                //updating the distances and station number of the following stations
                resultSet.next();
                resultSet.next();
                preparedStatement=connection.prepareStatement(query5);
                preparedStatement.setInt(1,rerouteRequest.getDistanceNext()+rerouteRequest.getDistanceOld()-resultSet.getInt(1));
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setInt(3,resultSet.getInt(3));
                preparedStatement.setString(4,rerouteRequest.getTrain_ID());
                a5=preparedStatement.executeUpdate();

            }
            else
            {
                preparedStatement.setString(1, rerouteRequest.getPrevStation());
                preparedStatement.setString(2, rerouteRequest.getOldStation());
                preparedStatement.setString(3, rerouteRequest.getOldStation());
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                resultSet.next();
                int distOld = resultSet.getInt(1);
                int stationNo = resultSet.getInt(3);

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, rerouteRequest.getStation());
                preparedStatement.setInt(2, stationNo);
                preparedStatement.setString(3, rerouteRequest.getCity_Code());
                preparedStatement.setString(4, rerouteRequest.getArrivalD());
                preparedStatement.setString(5, rerouteRequest.getDepartureD());
                preparedStatement.setInt(6, rerouteRequest.getDayNo());
                preparedStatement.setInt(7, distOld + rerouteRequest.getDistanceOld());
                preparedStatement.setString(8, rerouteRequest.getTrain_ID());
                preparedStatement.setString(9, rerouteRequest.getOldStation());
                a3=preparedStatement.executeUpdate();

                //changing the value of departure from destination from null to a non null value
                String q="update Route_Info set Departure=? where Train_ID=? and Departure=null;";
                preparedStatement=connection.prepareStatement(q);
                preparedStatement.setString(1,rerouteRequest.getDepartureB());
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                a4=preparedStatement.executeUpdate();
                a5=1;
            }
        }
        preparedStatement=connection.prepareStatement(query6);
        preparedStatement.setString(1,rerouteRequest.getTrain_ID());
        int a6=preparedStatement.executeUpdate();

        preparedStatement=connection.prepareStatement(query7);
        preparedStatement.setString(1,rerouteRequest.getTrain_ID());
        int a7=preparedStatement.executeUpdate();

        preparedStatement=connection.prepareStatement(query8);
        preparedStatement.setString(1,"Train Number "+rerouteRequest.getTrain_ID()+"has been rerouted till "+rerouteRequest.getsDate()+".You can check the rerouted trains from the View Rerouted trains option in the menu");
        if(a1*a2*a3*a4*a5*a6*a7!=0)
        {
            preparedStatement.executeUpdate();
            response="Train rerouted successfully";
        }
        else
        {
            response="error occured";
        }
        return new RerouteResponse(response);
    }

}
