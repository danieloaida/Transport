package ro.ratt.transport;

/**
 * Created by baby on 6/2/2016.
 */
public class LineInfo {
    private String lineName;
    private String route;

    public LineInfo(String lineName, String route) {
        this.lineName = lineName;
        this.route = route;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
