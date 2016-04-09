package ro.ratt.transport;

/**
 * Created by baby on 4/6/2016.
 */

public class Lines {
    private int lineId;
    private int stationId;
    private String lineName;

    public Lines(int lineId, int stationId, String lineName) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.lineName = lineName;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}

