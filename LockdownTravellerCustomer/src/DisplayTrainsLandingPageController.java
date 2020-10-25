import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DisplayTrainsLandingPageController {
    @FXML
    public TextArea trainID;
    @FXML
    public TextArea trainName;
    @FXML
    public TextArea departure;
    @FXML
    public TextArea arrival;
    @FXML
    public TextArea thirdAC;
    @FXML
    public TextArea secondAC;
    @FXML
    public TextField heading;
    @FXML
    public TextArea firstAc;
    @FXML
    public TextArea sleeper;
    String [] Train_ID,Train_Name,Departure,Arrival,Third_AC,Second_AC,First_AC,Sleeper;
    String source,dest,date;
    int i;
void init(String [] Train_ID,String [] Train_Name,String [] Departure,String [] Arrival,String []  Third_AC,String [] Second_AC,String [] First_AC,String [] Sleeper,String source,String dest,String date,int i)
{
    this.Arrival=Arrival;
    this.date=date;
    this.Departure=Departure;
    this.dest=dest;
    this.First_AC=First_AC;
    this.i=i;
    this.Second_AC=Second_AC;
    this.Third_AC=Third_AC;
    this.Sleeper=Sleeper;
    this.source=source;
    this.Train_ID=Train_ID;
    this.Train_Name=Train_Name;
}

}