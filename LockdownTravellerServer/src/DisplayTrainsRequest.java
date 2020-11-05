import java.io.Serializable;

/**
 * Request class for Display trains feature.
 */
public class DisplayTrainsRequest extends Request implements Serializable {

    final private String source;
    final private String sDate;
    final private String dest;

    /**
     * Constructor for DisplayTrainsRequest, initializes the object with source, destination and date of journey for
     * which the train has to be searched.
     * @param source Boarding station.
     * @param sDate Date of journey.
     * @param dest De-boarding station.
     */
    DisplayTrainsRequest(String source, String sDate, String dest) {
        this.source = source;
        this.dest = dest;
        this.sDate = sDate;
    }

    /**
     * Getter for boarding station.
     * @return Boarding station.
     */
    public String getSource() {
        return source;
    }

    /**
     * Getter for de-boarding station.
     * @return De-boarding station.
     */
    public String getDest() {
        return dest;
    }

    /**
     * Getter for Date of journey.
     * @return Date of journey.
     */
    public String getsDate() {
        return sDate;
    }
}
