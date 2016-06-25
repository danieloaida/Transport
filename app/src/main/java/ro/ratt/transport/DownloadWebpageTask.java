package ro.ratt.transport;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baby on 5/26/2016.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    private TextView textView;
    private Marker markerShowingInfoWindow;
    private int option = 0;
    private String route;
    private String lineName;
    private List<MapStation> mapStationList = new ArrayList<MapStation>();
    private List<Marker> lstMarkers = new ArrayList<Marker>();
    private GoogleMap mMap;

    public DownloadWebpageTask(int opt, List<MapStation> mapStationList, List<Marker> lstMarkers, String lineName, String route, GoogleMap mMap) {
        this.option = opt;
        this.mapStationList = mapStationList;
        this.lineName = lineName;
        this.route = route;
        this.lstMarkers = lstMarkers;
        this.mMap = mMap;
    }

    public DownloadWebpageTask(int opt, TextView textView, Marker markerShowingInfoWindow) {
        this.textView = textView;
        this.markerShowingInfoWindow = markerShowingInfoWindow;
        this.option = opt;
    }


    private Marker search(int stationID, List<Marker> lstMarkers){
        String station = String.valueOf(stationID);
        for (Iterator<Marker> iter = lstMarkers.listIterator(); iter.hasNext();){
            Marker mItem = iter.next();
            if (mItem.getSnippet().contains(station)){
                return mItem;
            }
        }
        return null;
    }
    private void deleteMarkers(String title, List<Marker> lstMarkers){
        for (Iterator<Marker> iter = lstMarkers.listIterator(); iter.hasNext();){
            Marker mItem = iter.next();
            if (mItem.getTitle().contains(title)){
                mItem.remove();
                iter.remove();
            }
        }
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return null;
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        switch (option){
            case 1:
                stationTime(result);
                break;
            case 2:
                lineTimes(result);
                break;
            default:
                lineTimes(result);
                break;

        }

    }


    private void stationTime(String result){

        textView.setText(result);

        if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {
            markerShowingInfoWindow.showInfoWindow();
        }

    }

    private void addRealTimeTrans(LatLng latLng){

        MarkerOptions newTransport = new MarkerOptions();
        newTransport.title(lineName);
        newTransport.position(latLng);
        newTransport.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_small_purple));
        Marker itemAdded = mMap.addMarker(newTransport);
        lstMarkers.add(itemAdded);

    }

    private void lineTimes(String result)    {
        List<ArrivingTimes> converted;
        converted = GetAllTimesList(result);
        int i = 0;
        int lastTime = 100;
        int currentTime = 100;
        Marker lastFound;

        for(MapStation item : mapStationList){
            if (item.getLineName().equals(lineName) && item.getRoute().equals(route)){
               break;
            } else i++;
        }
        deleteMarkers(lineName, lstMarkers);

        for(ArrivingTimes item: converted){
            int found = searchInMapList(i, item.getStationName(), 25);
            if (found != -1) {
                i = found;
                String time = item.getTime();
                if (time.contains(">>")) {
                    // add transport position
                    Marker markerFound = search(mapStationList.get(found).getStationID(), lstMarkers);
                    if (markerFound != null) {
                        LatLng latLng = markerFound.getPosition();
                        addRealTimeTrans(latLng);
                        lastTime = 0;
                    }

                }   else    {
                    if (time.contains("min")){
                        currentTime = Integer.parseInt(time.substring(0,2).trim());
                        if (lastTime > currentTime) {
                            Marker markerFound = search(mapStationList.get(found).getStationID(), lstMarkers);
                            lastFound = markerFound;
                            if (markerFound != null) {
                                LatLng latLng = markerFound.getPosition();
                                addRealTimeTrans(latLng);
                                lastTime = currentTime;
                            }
                        }
                    }   else    {
                        lastTime = 100;
                    }
                }
                mapStationList.get(found).setJonctionTime(time);


            }

        }
    }

    private int searchInMapList(int start, String stationName, int end){
        int i = start;
        end = mapStationList.size();
        while((i < end) && !mapStationList.get(i).getStationName().equals(stationName) && mapStationList.get(i).getLineName().equals(lineName)){
            i++;
        }
        if (i == end) return -1;
        else
        return i;
    }
       // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 10000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            is = new BufferedInputStream(conn.getInputStream());

            String contentAsString = readStream(is);

            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        int counter= 10000;
        reader.read(buffer,0,counter);

        return new String(buffer);
    }

    public List<ArrivingTimes> GetListValues(String input) {

        List<ArrivingTimes> returnValue = new ArrayList<ArrivingTimes>();

        int _index = 0;
        int _endOfValue = 0;
        int _endOfName = 0;
        String _value = "";
        String _name = "";
        String _entrySpliter = "Sosire1:";
        String _nameBegin = "Linia: </font>";
        String _nameEnd = "<br>";
        _index = input.indexOf(_nameBegin, _index);
        _endOfName = input.indexOf(_nameEnd, _index);
        _name = input.substring(_index + _nameBegin.length(), _endOfName);

        _index = input.indexOf(_entrySpliter, _index);
        _endOfValue = input.indexOf("<br>", _index);
        _value = input.substring(_index + _entrySpliter.length(), _endOfValue);

        ArrivingTimes item = new ArrivingTimes(_name,_value,"");
        returnValue.add(item);
        return returnValue;
    }

    public List<ArrivingTimes> GetAllTimesList(String input) {


        List<ArrivingTimes> returnValue = new ArrayList<ArrivingTimes>();


        String tableHeader = "<td align=center width=\"60\"><b>Sosire";
        String stationHeader = "<td align=left width=\"200\"><b>";
        String valueEnd = "</b>";
        String timeHeader = "<td align=right width=\"60\"><b>";
        String eofString = "</ul>";

        int index = 0;
        int startCut = 0;
        int endCut = 0;
        int endIndex = 0;
        int route1Table = input.indexOf(tableHeader, 0);
        int route2Table = input.indexOf(tableHeader, route1Table+1);
        int endFile = input.indexOf(eofString, route2Table);
        // init cursor
        index = route1Table;
        endIndex = route2Table;

        while (index < endIndex){
            index = input.indexOf(stationHeader, index);
            startCut = index + stationHeader.length();
            endCut = input.indexOf(valueEnd, startCut);
            String sName = input.substring(startCut, endCut);
            index = endCut;

            index = input.indexOf(timeHeader, index);
            startCut = index + timeHeader.length();
            endCut = input.indexOf(valueEnd, startCut);
            String time = input.substring(startCut, endCut);
            ArrivingTimes item = new ArrivingTimes(sName, time, "route1");
            returnValue.add(item);

            index = input.indexOf("<table", index);


        }

        while (index != -1){
            index = input.indexOf(stationHeader, index);
            startCut = index + stationHeader.length();
            endCut = input.indexOf(valueEnd, startCut);
            String sName = input.substring(startCut, endCut);
            index = endCut;

            index = input.indexOf(timeHeader, index);
            startCut = index + timeHeader.length();
            endCut = input.indexOf(valueEnd, startCut);
            String time = input.substring(startCut, endCut);
            ArrivingTimes item = new ArrivingTimes(sName, time, "route2");
            returnValue.add(item);

            index = input.indexOf("<table", index);

        }

        return returnValue;
    }


}