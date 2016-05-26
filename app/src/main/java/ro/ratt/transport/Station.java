package ro.ratt.transport;

/**
 * Created by baby on 4/6/2016.
 */

public class Station {
    private final String name;
    private final double lat;
    private final double lng;
    private final int id_st;
    private final int id_line;

    public Station(String name, double lat, double lng, int id_st, int id_line) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id_st = id_st;
        this.id_line = id_line;
    }

    public int getId_line() {
        return id_line;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getId_st() {
        return id_st;
    }
}
