import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CancelTrainsRequestHandler  extends Handler{
    private ObjectOutputStream oos;
    CancelTrainsRequest cancelTrainsRequest;
    CancelTrainsRequestHandler(CancelTrainsRequest cancelTrainsRequest,ObjectOutputStream oos)
    {
        this.cancelTrainsRequest=cancelTrainsRequest;
        this.oos=oos;
    }

    @Override
    void sendQuery() throws IOException, SQLException {
        String Train_ID=cancelTrainsRequest.getTrain_ID();
        String date=cancelTrainsRequest.getsDate();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf2=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date= LocalDate.parse(date,dtf).format(dtf2);
        String query1="alter table basic_train_info(Cancelled_Till) add value(\""+date+"\");";
        String query2="select User_ID from User;";
        String query3="insert into notifications values(\""+"xxxxx"+"\",\""+"As of 11:59 PM today the train number"+Train_ID+"has been cancelled untill\""+date+"\""+"\",1);";
        DatabaseConnector db=new DatabaseConnector();
        CancelTrainsResponse cancelTrainsResponse=db.cancelTrains(query1,query2,query3);
        Server.SendResponse(oos,cancelTrainsResponse);
    }
}
