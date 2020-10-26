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
    void sendQuery()  {
        String query1="update basic_train_info set Rerouted_Till=? where Train_ID=?;";
        String query2="select Distance_Covered,Train_Name,Station_No from route_info where Train_ID=? and Station in (?,?);";
        String query3="insert into route_info values (?,?,?,?,?,?,?,?,?,1);";
        String query4="update route_info set Distance_Covered=? where Station=? and Train_ID=?;";
        String query5="update route_info set Distance_Covered=Distance_Covered+? where Station in (select Station from route_info where Train_ID=? and Station_No>?);";
        String query6="update route_info set inCurrentRoute=? where Train_ID=? and Station=?;";
        String query7="update notification set Message=?,Pending_Status=1,User_ID=User_ID in (select distinct User_ID from User );";
        RerouteResponse rerouteResponse=rerouteTrains(query1,query2,query3,query4,query5,query6,query7);
        Server.SendResponse(objectOutputStream,rerouteResponse);

    }
    public RerouteResponse rerouteTrains(String query1,String query2,String query3,String query4,String query5,String query6,String query7)
    {
        String date=rerouteRequest.getsDate();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        String response = null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            preparedStatement.setString(1,date);
            preparedStatement.setString(2,rerouteRequest.getTrain_ID());
            int a=preparedStatement.executeUpdate();
            int b=0,c=0,d=0,e=0;

            if(rerouteRequest.getInplace()&&(rerouteRequest.getPrevStation()!=null))
            {
                preparedStatement=connection.prepareStatement(query2);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,rerouteRequest.getPrevStation());
                preparedStatement.setString(3,rerouteRequest.getNextStation());
                ResultSet resultSet=preparedStatement.executeQuery();

                resultSet.next();
                int prev=resultSet.getInt(1);
                String Train_Name=resultSet.getString(2);
                resultSet.next();
                int next=resultSet.getInt(1);
                int Station_No=resultSet.getInt(3);


                preparedStatement=connection.prepareStatement(query3);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,Train_Name);
                preparedStatement.setString(3,rerouteRequest.getStation());
                preparedStatement.setInt(4,Station_No-1);
                preparedStatement.setString(5,rerouteRequest.getCity_Code());
                preparedStatement.setString(6,rerouteRequest.getArrival());
                preparedStatement.setString(7,rerouteRequest.getDeparture());
                preparedStatement.setInt(8,rerouteRequest.getDay_No());
                preparedStatement.setInt(9,prev+rerouteRequest.getDistancePrev());
                 b=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query4);
                preparedStatement.setInt(1,prev+rerouteRequest.getDistancePrev()+rerouteRequest.getDistanceNext());
                preparedStatement.setString(2,rerouteRequest.getNextStation());
                preparedStatement.setString(3,rerouteRequest.getTrain_ID());
                 c=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query5);
                preparedStatement.setInt(1,rerouteRequest.getDistancePrev()+rerouteRequest.getDistanceNext()-(next-prev));
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setInt(3,Station_No);
                 d=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query6);
                preparedStatement.setInt(1,0);
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setString(3,rerouteRequest.getOldStation());
                e=preparedStatement.executeUpdate();
            }
            else if(!rerouteRequest.getInplace()&&(rerouteRequest.getNextStation()==null))
            {
                preparedStatement=connection.prepareStatement(query2);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,rerouteRequest.getPrevStation());
                preparedStatement.setString(3,rerouteRequest.getOldStation());

                ResultSet resultSet=preparedStatement.executeQuery();

                resultSet.next();
                String Train_Name=resultSet.getString(2);
                resultSet.next();
                int old=resultSet.getInt(1);
                int Station_No=resultSet.getInt(3);

                preparedStatement=connection.prepareStatement(query3);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,Train_Name);
                preparedStatement.setString(3,rerouteRequest.getStation());
                preparedStatement.setInt(4,Station_No+1);
                preparedStatement.setString(5,rerouteRequest.getCity_Code());
                preparedStatement.setString(6,rerouteRequest.getArrival());
                preparedStatement.setString(7,rerouteRequest.getDeparture());
                preparedStatement.setInt(8,rerouteRequest.getDay_No());
                preparedStatement.setInt(9,old+rerouteRequest.getDistanceOld());
                b=preparedStatement.executeUpdate();
                a=c=d=e=1;
            }
            else if(rerouteRequest.getInplace()&&(rerouteRequest.getPrevStation()==null))
            {
                preparedStatement=connection.prepareStatement(query2);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,rerouteRequest.getOldStation());
                preparedStatement.setString(3,rerouteRequest.getNextStation());
                ResultSet resultSet=preparedStatement.executeQuery();

                resultSet.next();
                int prev=resultSet.getInt(1);
                String Train_Name=resultSet.getString(2);
                resultSet.next();
                int next=resultSet.getInt(1);
                int Station_No=resultSet.getInt(3);

                preparedStatement=connection.prepareStatement(query3);
                preparedStatement.setString(1,rerouteRequest.getTrain_ID());
                preparedStatement.setString(2,Train_Name);
                preparedStatement.setString(3,rerouteRequest.getStation());
                preparedStatement.setInt(4,Station_No-1);
                preparedStatement.setString(5,rerouteRequest.getCity_Code());
                preparedStatement.setString(6,rerouteRequest.getArrival());
                preparedStatement.setString(7,rerouteRequest.getDeparture());
                preparedStatement.setInt(8,rerouteRequest.getDay_No());
                preparedStatement.setInt(9,prev+rerouteRequest.getDistancePrev());
                b=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query4);
                preparedStatement.setInt(1,prev+rerouteRequest.getDistancePrev()+rerouteRequest.getDistanceNext());
                preparedStatement.setString(2,rerouteRequest.getNextStation());
                preparedStatement.setString(3,rerouteRequest.getTrain_ID());
                c=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query5);
                preparedStatement.setInt(1,rerouteRequest.getDistancePrev()+rerouteRequest.getDistanceNext()-(next-prev));
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setInt(3,Station_No);
                d=preparedStatement.executeUpdate();

                preparedStatement=connection.prepareStatement(query6);
                preparedStatement.setInt(1,0);
                preparedStatement.setString(2,rerouteRequest.getTrain_ID());
                preparedStatement.setString(3,rerouteRequest.getOldStation());
                e=preparedStatement.executeUpdate();
            }
            else response="Cannot add station between 2 stations.Invalid Request";

            preparedStatement=connection.prepareStatement(query7);
            preparedStatement.setString(1,"Route for Train Number "+rerouteRequest.getTrain_ID()+" has been updated untill "+rerouteRequest.getsDate()+" .Please check");
            int f=preparedStatement.executeUpdate();

            if((a*b*c*d*e*f)!=0){ response="Train rerouted successfully";}
            else{response="Error Occured";}

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RerouteResponse(response);
    }

}
