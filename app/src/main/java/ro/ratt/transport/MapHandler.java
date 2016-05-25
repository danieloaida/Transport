package ro.ratt.transport;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baby on 5/24/2016.
 */
public class MapHandler {
    private List<MapStation> mapStationList = new ArrayList<MapStation>();
    private GoogleMap mMap;
    private DBHandler dbHandler;

    public MapHandler(List<MapStation> mapStationList, GoogleMap mMap, DBHandler dbHandler) {
        this.mapStationList = mapStationList;
        this.mMap = mMap;
        this.dbHandler = dbHandler;
    }

    public void addStation(MarkerOptions markerOptions, String lineName){
        MapStation mapStation;
        mapStation = new MapStation(markerOptions, lineName);
        mapStationList.add(mapStation);

    }

    public void addLineStations(String line){
        List<Station> stations = new ArrayList<Station>();
        stations.addAll(dbHandler.getListOfStations(line));

        for(Station sItem : stations){
            MarkerOptions mark = new MarkerOptions();
            mark.position(new LatLng(sItem.getLat(), sItem.getLng()));
            mark.title(sItem.getName());
            mMap.addMarker(mark);
            addStation(mark, line);
        }

    }

    public void removeLineStations(String line){

        for (Iterator<MapStation> iter = mapStationList.listIterator(); iter.hasNext();){
            MapStation mItem = iter.next();
            if (mapStationList.contains(new MapStation(null, line))){
                mItem.marker.visible(Boolean.FALSE);
                iter.remove();
            }

        }

    }



    public List<MapStation> getMapStationList() {
        return mapStationList;
    }

    public void setMapStationList(List<MapStation> mapStationList) {
        this.mapStationList = mapStationList;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }
}

class MapStation {
    MarkerOptions marker;
    String lineName;
    int lineID;

    public MapStation(MarkerOptions marker, String lineName, int lineID) {
        this.marker = marker;
        this.lineName = lineName;
        this.lineID = lineID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapStation that = (MapStation) o;

        return lineName.equals(that.lineName);

    }

    @Override
    public int hashCode() {
        return lineName.hashCode();
    }

    public MapStation(MarkerOptions marker, String lineName) {
        this.marker = marker;
        this.lineName = lineName;
        this.lineID = -1;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getLineID() {
        return lineID;
    }

    public void setLineID(int lineID) {
        this.lineID = lineID;
    }
}
