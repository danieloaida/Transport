package ro.ratt.transport;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by baby on 5/26/2016.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    TextView textView;
    Marker markerShowingInfoWindow;


    public DownloadWebpageTask(TextView textView, Marker markerShowingInfoWindow) {
        this.textView = textView;
        this.markerShowingInfoWindow = markerShowingInfoWindow;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
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
    private String downloadUrl(String myurl) throws IOException {
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
            //String converted = GetListValues(contentAsString);
            return contentAsString;

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
     /* String sors = "";
        int ch;
        char p='p';
        while (((ch = stream.read()) != -1)){

            p = (char) ch;

            sors+=Character.toString(p);

        }
        stream.close();
    return sors;*/
    }

    public String GetListValues(String input) {

        int _index = 0;
        int _endOfValue = 0;
        int _endOfName = 0;
        String _value = "";
        String returnValue = "";
        String _name = "";
        String _entrySpliter = "<OPTION   value=\"";
        String _nameBegin = "\"  >";
        String _nameEnd = "<";
        _index = input.indexOf(_entrySpliter);
        while (_index != -1){
            _endOfValue = input.indexOf("\"", _index);
            _value = input.substring(_index + _entrySpliter.length(), _endOfValue);
            _endOfName = input.indexOf(_nameEnd, _index);
            _name = input.substring(_endOfValue + _nameBegin.length(), _endOfName);
            _index = input.indexOf(_entrySpliter);
            returnValue.concat("Value = " + _value);
            returnValue.concat(" Station = " + _name + "\n");

        }
        return returnValue;
    }

}