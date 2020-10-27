import java.io.Serializable;

public class DisplayTouristPackageResponse extends Response implements Serializable {
    String packageCode,packageName,destinationCovered,boardingDeboardingPoints,stationDeparture,Class,startDate,endDate,duration,tariff,mealPlan;

    public DisplayTouristPackageResponse(String packageCode, String packageName, String destinationCovered, String boardingDeboardingPoints, String stationDeparture, String Class, String startDate, String endDate, String duration, String tariff, String mealPlan) {
        this.packageCode = packageCode;
        this.packageName = packageName;
        this.destinationCovered = destinationCovered;
        this.boardingDeboardingPoints = boardingDeboardingPoints;
        this.stationDeparture = stationDeparture;
        this.Class = Class;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.tariff = tariff;
        this.mealPlan = mealPlan;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDestinationCovered() {
        return destinationCovered;
    }

    public String getBoardingDeboardingPoints() {
        return boardingDeboardingPoints;
    }

    public String getStationDeparture() {
        return stationDeparture;
    }


    public String getClasS() {
        return Class;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getTariff() {
        return tariff;
    }

    public String getMealPlan() {
        return mealPlan;
    }
}
