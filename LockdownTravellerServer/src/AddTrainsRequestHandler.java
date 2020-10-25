import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTrainsRequestHandler extends Handler {
    private AddTrainsRequest addTrainsRequest;
    Connection connection;
    ObjectOutputStream oos;
    public AddTrainsRequestHandler(Connection connection, AddTrainsRequest addTrainsRequest,ObjectOutputStream oos) {
       this.connection=connection;
       this.addTrainsRequest=addTrainsRequest;
       this.oos=oos;
    }

    @Override
    void sendQuery() {
        String date= addTrainsRequest.getAdded_Till();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        String query1="insert into Basic_Train_Info values(\""+addTrainsRequest.getTrain_ID()+"\",\""+addTrainsRequest.getTrain_Name()+
                "\",\""+addTrainsRequest.getDays_Running()+"\",\""+
                addTrainsRequest.getFirstAC_Coaches()+"\",\""+ addTrainsRequest.getSecondAC_Coaches()
                +"\"" + ",\""+addTrainsRequest.getThirdAC_Coaches()+"\",\""+addTrainsRequest.getSleeper_Coaches()
                +"\",\""+addTrainsRequest.getFirstAC_Seats() +"\",\""+addTrainsRequest.getSecondAC_Seats()
                +"\",\""+ addTrainsRequest.getThirdAC_Seats()+"\","+addTrainsRequest.getSleeper_Seats()+"\","+
                addTrainsRequest.getFirstAC_Fare()+"\","+addTrainsRequest.getSecondAC_Fare()+"\","+
                addTrainsRequest.getThirdAC_Fare()+"\","+addTrainsRequest.getSleeper_Fare()+"\","+
                date+"\",null;";
        AddTrainsResponse addTrainsResponse=addTrains(query1);
        Server.SendResponse(oos,addTrainsResponse);
    }
    public AddTrainsResponse addTrains(String query)
    {
        String reponse=null;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            int c=preparedStatement.executeUpdate();
            if(c!=0){reponse="Train added succesfully";}
            else{reponse="Error occured";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new AddTrainsResponse(reponse);

    }
}
