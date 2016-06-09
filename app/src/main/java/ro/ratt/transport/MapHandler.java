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
    private List<Marker> lstMarkers = new ArrayList<Marker>();
    private GoogleMap mMap;
    private DBHandler dbHandler;
    private Context context;

    public MapHandler(List<MapStation> mapStationList, GoogleMap mMap, DBHandler dbHandler, Context context) {
        this.mapStationList = mapStationList;
        this.mMap = mMap;
        this.dbHandler = dbHandler;
        this.context = context;
    }

    private Marker search(String station){
        for (Iterator<Marker> iter = lstMarkers.listIterator(); iter.hasNext();){
            Marker mItem = iter.next();
            if (mItem.getSnippet().contains(station)){
                return mItem;
            }
        }
        return null;
    }


    public void addLineStations(String line, String route){
        List<Station> stations = new ArrayList<Station>();
        int lastItem = 0;

        stations.addAll(dbHandler.getListOfStations(line, route));
        Station preStation = stations.get(0);
        int line_id = preStation.getId_line();



        for(Station sItem : stations){
            String stationID = String.valueOf(sItem.getId_st());
            Marker found = search(stationID);
            if (found == null) {
                MarkerOptions mark = new MarkerOptions();
                LatLng coord = new LatLng(sItem.getLat(), sItem.getLng());
                // marker informations
                mark.position(coord);
                mark.title(sItem.getName());
                mark.snippet(String.valueOf(1 + "," + sItem.getId_st() + "," + sItem.getId_line()));

                // Adding marker to map
                Marker marker = mMap.addMarker(mark);
                // Adding marker to the global list markers
                lstMarkers.add(marker);
            } else {
                String markerSnippet = found.getSnippet().concat(","+stationID);
                int lineNo = Integer.parseInt(markerSnippet.substring(0,1));
                lineNo++;
                found.setSnippet(lineNo + markerSnippet.substring(1));

            }


            MapStation item = new MapStation(sItem.getName(),sItem.getId_st(), line, sItem.getId_line());
            mapStationList.add(item);

            //draw line between stations
            if (lastItem > 1){
            findDirections(preStation.getLat(), preStation.getLng(),sItem.getLat(),sItem.getLng(),"walking");}
            lastItem++;
            preStation = sItem;
        }

        // try receiving times for stations

        String stringUrl = "http://86.122.170.105:61978/html/timpi/sens0.php?param1="+line_id;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask(2, mapStationList).execute(stringUrl);
        } else {
        }




    }

    public void removeLineStations(String line){

        for (Iterator<MapStation> iter = mapStationList.listIterator(); iter.hasNext();){
            MapStation mItem = iter.next();
            if (mItem.equals(new MapStation(line))){
                iter.remove();
            }

        }
        for (Iterator<Marker> iter = lstMarkers.listIterator(); iter.hasNext();){
            Marker mItem = iter.next();
            String[] Snippet = mItem.getSnippet().split(",");
            int lineNo = Integer.parseInt(Snippet[0]);
            if (lineNo == 1){
                mItem.remove();
                iter.remove();
            } else {
                String reMerge = "";
                for(String lineItem: Snippet)
                if (lineItem.equals(line)) {
                    lineNo--;
                }else {
                    reMerge.concat(lineItem + ",");
                }
                reMerge = lineNo + reMerge.substring(1);
                mItem.setSnippet(reMerge);
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
    String stationName;
    int stationID;
    String lineName;
    int lineID;
    PolylineOptions line;
    String jonctionTime;

    public MapStation(String stationName, int stationID, String lineName, int lineID) {
        this.stationName = stationName;
        this.stationID = stationID;
        this.lineName = lineName;
        this.lineID = lineID;
        this.line = null;
        this.jonctionTime = " xx : xx";
    }

    public MapStation(String line){
        this.stationName = null;
        this.stationID = 0;
        this.lineName = line;
        this.lineID = 0;
        this.line = null;
        this.jonctionTime = " xx : xx";
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


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getJonctionTime() {
        return jonctionTime;
    }

    public void setJonctionTime(String jonctionTime) {
        this.jonctionTime = jonctionTime;
    }

    public PolylineOptions getLine() {
        return line;
    }


    public void setLine(PolylineOptions line) {
        this.line = line;
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
