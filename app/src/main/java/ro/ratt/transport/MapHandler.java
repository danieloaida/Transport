package ro.ratt.transport;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

    public void addStation(Marker marker, String lineName){
        MapStation mapStation;
        mapStation = new MapStation(marker, lineName);
        mapStationList.add(mapStation);

    }

    public void addLineStations(String line, String route){
        List<Station> stations = new ArrayList<Station>();
        stations.addAll(dbHandler.getListOfStations(line, route));
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for(Station sItem : stations){
            MarkerOptions mark = new MarkerOptions();
            LatLng coord = new LatLng(sItem.getLat(), sItem.getLng());
            options.add(coord);
            mark.position(coord);
            mark.title(sItem.getName());
            mark.snippet(String.valueOf(sItem.getId_st() + "," + sItem.getId_line()));
            Marker marker = mMap.addMarker(mark);
            addStation(marker, line);
        }


    }

    public void removeLineStations(String line){

        for (Iterator<MapStation> iter = mapStationList.listIterator(); iter.hasNext();){
            MapStation mItem = iter.next();
            if (mapStationList.contains(new MapStation(null, line))){
                mItem.marker.remove();
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
    Marker marker;
    String lineName;
    int lineID;

    public MapStation(Marker marker, String lineName, int lineID) {
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

    public MapStation(Marker marker, String lineName) {
        this.marker = marker;
        this.lineName = lineName;
        this.lineID = -1;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
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
