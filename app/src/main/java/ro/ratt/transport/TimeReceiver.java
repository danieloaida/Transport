package ro.ratt.transport;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

/**
 * Created by baby on 6/11/2016.
 */
public class TimeReceiver {
    private Context context;
    private String stringUrl;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private List<MapStation> mapStationList;

    public TimeReceiver(Context context, List<MapStation> mapStationList) {
        this.context = context;
        this.mapStationList = mapStationList;
    }

    public void StartDownload(int line_id, String line, String route){
        stringUrl =  "http://86.122.170.105:61978/html/timpi/sens0.php?param1="+line_id;
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask(2, mapStationList, line, route).execute(stringUrl);
        } else {
        }
    }

}
