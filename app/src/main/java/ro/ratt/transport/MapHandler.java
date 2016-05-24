package ro.ratt.transport;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 5/24/2016.
 */
public class MapHandler {
    List<MapStation> mapStationList = new ArrayList<MapStation>();
    GoogleMap mMap;
    DBHandler dbHandler;

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
            addStation(mark,sItem.getName());
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
