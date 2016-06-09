package ro.ratt.transport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by baby on 5/26/2016.
 */
public class MarkerInfoWindowAdapt implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private Context context;

    MarkerInfoWindowAdapt(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        myContentsView = inflater.inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
        String[] Snippet = marker.getSnippet().split(",");

        /*

        String stringUrl = "http://ratt.ro/txt/afis_msg.php?id_traseu="+Snippet[1]+"&id_statie="+Snippet[2];
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask(1, tvSnippet, marker).execute(stringUrl);
        } else {
            tvSnippet.setText("No network connection available.");
        }
        */

        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }
}
