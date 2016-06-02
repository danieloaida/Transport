package ro.ratt.transport;

/**
 * Created by baby on 4/25/2016.
 */
public class Junction {
    private int index;
    private int lineID;
    private String lineName;
    private int stationID;
    private String rawStationName;
    private String friendlyStationName;
    private String shortStationName;
    private String junctionName;
    private double lat;
    private double lng;
    private String invalid;
    private String verificationDate;
    private String route;

    public Junction(int index, int lineID, String lineName, int stationID, String rawStationName, String friendlyStationName, String shortStationName, String junctionName, double lat, double lng, String invalid, String verificationDate, String route) {
        this.index = index;
        this.lineID = lineID;
        this.lineName = lineName;
        this.stationID = stationID;
        this.rawStationName = rawStationName;
        this.friendlyStationName = friendlyStationName;
        this.shortStationName = shortStationName;
        this.junctionName = junctionName;
        this.lat = lat;
        this.lng = lng;
        this.invalid = invalid;
        this.verificationDate = verificationDate;
        this.route = route;
    }

    public Junction(String[] arrayItems) {
        this.index = Integer.parseInt(arrayItems[0]);
        this.lineID = Integer.parseInt(arrayItems[1]);
        this.lineName = arrayItems[2];
        this.stationID = Integer.parseInt(arrayItems[3]);
        this.rawStationName = arrayItems[4];
        this.friendlyStationName = arrayItems[5];
        this.shortStationName = arrayItems[6];
        this.junctionName = arrayItems[7];
        this.lat = Double.parseDouble(arrayItems[8]);
        this.lng = Double.parseDouble(arrayItems[9]);
        this.invalid = arrayItems[10];
        this.verificationDate = arrayItems[11];
        this.route = arrayItems[12];

    }

    public Junction parseJunction(String[] arrayItems) {
        Junction retObj;
        int index = Integer.parseInt(arrayItems[0]);
        int lineID = Integer.parseInt(arrayItems[1]);
        String lineName = arrayItems[2];
        int stationID = Integer.parseInt(arrayItems[3]);
        String rawStationName = arrayItems[4];
        String friendlyStationName = arrayItems[5];
        String shortStationName = arrayItems[6];
        String junctionName = arrayItems[7];
        double lat = Double.parseDouble(arrayItems[8]);
        double lng = Double.parseDouble(arrayItems[9]);
        String invalid = arrayItems[10];
        String verificationDate = arrayItems[11];
        String route = arrayItems[12];

        retObj = new Junction(index, lineID, lineName, stationID, rawStationName, friendlyStationName, shortStationName, junctionName, lat, lng, invalid, verificationDate, route);

        return retObj;

    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public int getLineID() {
        return lineID;
    }

    public void setLineID(int lineID) {
        this.lineID = lineID;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getRawStationName() {
        return rawStationName;
    }

    public void setRawStationName(String rawStationName) {
        this.rawStationName = rawStationName;
    }

    public String getFriendlyStationName() {
        return friendlyStationName;
    }

    public void setFriendlyStationName(String friendlyStationName) {
        this.friendlyStationName = friendlyStationName;
    }

    public String getShortStationName() {
        return shortStationName;
    }

    public void setShortStationName(String shortStationName) {
        this.shortStationName = shortStationName;
    }

    public String getJunctionName() {
        return junctionName;
    }

    public void setJunctionName(String junctionName) {
        this.junctionName = junctionName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String isInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }
}
