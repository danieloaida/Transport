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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineInfo lineInfo = (LineInfo) o;

        return ((lineName.equals(lineInfo.getLineName()))&&(route.equals(lineInfo.getRoute())));

    }

    @Override
    public int hashCode() {
        int result = lineName != null ? lineName.hashCode() : 0;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        return result;
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
