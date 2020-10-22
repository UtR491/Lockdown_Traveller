import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTrainsRequestHandler extends Handler {
    private AddTrainsRequest addTrainsRequest;
    private ObjectOutputStream oos;
    AddTrainsRequestHandler(AddTrainsRequest addTrainsRequest,ObjectOutputStream oos)
    {
        this.addTrainsRequest=addTrainsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
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
        DatabaseConnector db=new DatabaseConnector();
        AddTrainsResponse addTrainsResponse=db.addTrains(query1);
        Server.SendResponse(oos,addTrainsResponse);
    }
}
