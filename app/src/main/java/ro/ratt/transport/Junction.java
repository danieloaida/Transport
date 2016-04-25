package ro.ratt.transport;

/**
 * Created by baby on 4/25/2016.
 */
public class Junction {
    private int lineID;
    private String lineName;
    private int stationID;
    private String rawStationName;
    private String friendlyStationName;
    private String shortStationName;
    private String junctionName;
    private double lat;
    private double lng;
    private boolean invalid;
    private String verificationDate;

    public Junction(int lineID, String lineName, int stationID, String rawStationName, String friendlyStationName, String shortStationName, String junctionName, double lat, double lng, boolean invalid, String verificationDate) {
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
    }

    public Junction(String[] arrayItems) {
        this.lineID = Integer.parseInt(arrayItems[0]);
        this.lineName = arrayItems[1];
        this.stationID = Integer.parseInt(arrayItems[2]);
        this.rawStationName = arrayItems[3];
        this.friendlyStationName = arrayItems[4];
        this.shortStationName = arrayItems[5];
        this.junctionName = arrayItems[6];
        this.lat = Double.parseDouble(arrayItems[7]);
        this.lng = Double.parseDouble(arrayItems[8]);
        this.invalid = Boolean.parseBoolean(arrayItems[9]);
        this.verificationDate = arrayItems[10];

    }

    public Junction parseJunction(String[] arrayItems) {
        Junction retObj;
        int lineID = Integer.parseInt(arrayItems[0]);
        String lineName = arrayItems[1];
        int stationID = Integer.parseInt(arrayItems[2]);
        String rawStationName = arrayItems[3];
        String friendlyStationName = arrayItems[4];
        String shortStationName = arrayItems[5];
        String junctionName = arrayItems[6];
        double lat = Double.parseDouble(arrayItems[7]);
        double lng = Double.parseDouble(arrayItems[8]);
        boolean invalid = Boolean.parseBoolean(arrayItems[9]);
        String verificationDate = arrayItems[10];

        retObj = new Junction(lineID, lineName, stationID, rawStationName, friendlyStationName, shortStationName, junctionName, lat, lng, invalid, verificationDate);

        return retObj;

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

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }
}
