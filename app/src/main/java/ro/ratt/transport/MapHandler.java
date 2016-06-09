package ro.ratt.transport;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by baby on 5/24/2016.
 */
public class MapHandler {
    private List<MapStation> mapStationList = new ArrayList<MapStation>();
    private GoogleMap mMap;
    private DBHandler dbHandler;
    private Context context;

    public MapHandler(List<MapStation> mapStationList, GoogleMap mMap, DBHandler dbHandler, Context context) {
        this.mapStationList = mapStationList;
        this.mMap = mMap;
        this.dbHandler = dbHandler;
        this.context = context;
    }

    public void addStation(Marker marker, String lineName){
        MapStation mapStation;
        mapStation = new MapStation(marker, lineName);
        mapStationList.add(mapStation);

    }

    public void addLineStations(String line, String route){
        List<Station> stations = new ArrayList<Station>();
        int lastItem = 0;

        stations.addAll(dbHandler.getListOfStations(line, route));
        Station preStation = null;

        for(Station sItem : stations){
            MarkerOptions mark = new MarkerOptions();
            LatLng coord = new LatLng(sItem.getLat(), sItem.getLng());
            // marker informations
            mark.position(coord);
            mark.title(sItem.getName());
            mark.snippet(String.valueOf(sItem.getId_st() + "," + sItem.getId_line()));

            // Adding marker to map
            Marker marker = mMap.addMarker(mark);
            // Adding marker to the global list markers
            addStation(marker, line);

            //draw line between stations
            if (lastItem > 1){
            findDirections(preStation.getLat(), preStation.getLng(),sItem.getLat(),sItem.getLng(),"walking");}
            lastItem++;
            preStation = sItem;
        }

        // try receiving times for stations

        String stringUrl = "http://86.122.170.105:61978/html/timpi/sens0.php?param1="+Snippet[1];
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask(2).execute(stringUrl);
        } else {
            tvSnippet.setText("No network connection available.");
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

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(context, mMap);
        asyncTask.execute(map);
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));
        }

        Polyline newPolyline = mMap.addPolyline(rectLine);

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
    PolylineOptions line;
    String lineName;
    int lineID;
    String stationTime;

    public MapStation(Marker marker, PolylineOptions line, String lineName, int lineID, String stationTime) {
        this.marker = marker;
        this.line = line;
        this.lineName = lineName;
        this.lineID = lineID;
        this.stationTime = stationTime;
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

    public PolylineOptions getLine() {
        return line;
    }

    public void setLine(PolylineOptions line) {
        this.line = line;
    }

    public String getStationTime() {
        return stationTime;
    }

    public void setStationTime(String stationTime) {
        this.stationTime = stationTime;
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
