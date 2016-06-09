package ro.ratt.transport;

/**
 * Created by baby on 6/9/2016.
 */
public class ArrivingTimes {
    String stationName;
    String time;
    String route;

    public ArrivingTimes(String stationName, String time, String route) {
        this.stationName = stationName;
        this.time = time;
        this.route = route;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}


