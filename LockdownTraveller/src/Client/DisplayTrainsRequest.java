package Client;
import java.io.Serializable;

public class DisplayTrainsRequest implements Serializable {
    private String source;
    private String sDate;
    private String dest;



    DisplayTrainsRequest(String source, String sDate, String dest) {
        this.source = source;
        this.sDate = sDate;
        this.dest = dest;
    }

    public String getSource() {
        return source;
    }

    public String getDest() {
        return dest;
    }

    public String getsDate() {
        return sDate;
    }
}
