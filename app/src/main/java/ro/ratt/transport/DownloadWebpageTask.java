package ro.ratt.transport;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 5/26/2016.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    TextView textView;
    Marker markerShowingInfoWindow;
    int option = 0;


    public DownloadWebpageTask(int opt, TextView textView, Marker markerShowingInfoWindow) {
        this.textView = textView;
        this.markerShowingInfoWindow = markerShowingInfoWindow;
        this.option = opt;
    }

    public DownloadWebpageTask(int opt ) {
        this.option = opt;
    }
    @Override
    protected List<ArrivingTimes> doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return null;
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        textView.setText(result);

        if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {
            markerShowingInfoWindow.showInfoWindow();
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private List<ArrivingTimes> downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 10000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            List<ArrivingTimes> converted;

            switch (option){
                case 1:
                    converted = GetListValues(contentAsString);
                    break;
                case 2:
                    converted = GetAllTimesList(contentAsString);
                    break;
                default:
                    converted = GetAllTimesList(contentAsString);
                    break;

            }
            return converted;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
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
        int route2Table = input.indexOf(tableHeader, route1Table);
        int endFile = input.indexOf(eofString, route2Table);
        // init cursor
        index = route1Table;
        endIndex = endFile;

        while (index > endIndex){
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

        }

        endIndex = endFile;
        while (index > endIndex){
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

        }

        return returnValue;
    }


}