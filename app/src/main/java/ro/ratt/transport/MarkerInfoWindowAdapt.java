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
    MapsActivity mapsActivity;
  //  TimeReceiver timeReceiver;

    MarkerInfoWindowAdapt(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        myContentsView = inflater.inflate(R.layout.info_window, null);
        mapsActivity = (MapsActivity) context;
    //    this.timeReceiver = new TimeReceiver(context, mapsActivity.getMapStationList(), mapsActivity.getLstMarkers(), mapsActivity.getmMap());
    }

    private int searchInMapList(int start, int stationID, int lineID){
        int i = start;
        int end = mapsActivity.getMapStationList().size();
        while((i < end) && !(mapsActivity.getMapStationList().get(i).getStationID() == stationID) || !(mapsActivity.getMapStationList().get(i).getLineID() == lineID)){
            i++;
        }
        if (i == end) return -1;
        else
            return i;
    }

   /* private void onClickUpdate(int lineNo, int[] lstLines, String[] lstLNames, String[] lstRoutes){

        for(int i = 0; i < lineNo; i++){

            timeReceiver.StartDownload(lstLines[i], lstLNames[i], lstRoutes[i]);
        }

    }*/
    @Override
    public View getInfoContents(final Marker marker) {
        final int[] lstLines = new int[10];
        final String[] lstRoutes = new String[10];
        final String[] lstLNames = new String[10];
        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
        String snippetText = "";
        if (marker.getSnippet().contains("transport")){
            tvSnippet.setText(snippetText);
        }
        else {
            String[] Snippet = marker.getSnippet().split(",");
            final int lineNo = Integer.parseInt(Snippet[0]);

            for (int i = 0; i < lineNo; i++) {
                int index = searchInMapList(0, Integer.parseInt(Snippet[1]), Integer.parseInt(Snippet[i + 2]));
                if (index != -1) {
                    MapStation item = mapsActivity.getMapStationList().get(index);
                    if (!item.getJonctionTime().equals("xx : xx")) {
                        snippetText = snippetText + item.getLineName() + ": " + item.getJonctionTime() + "\n";
                        lstLines[i] = item.getLineID();
                        lstRoutes[i] = item.getRoute();
                        lstLNames[i] = item.getLineName();

                    }

                }
            }
            tvSnippet.setText(snippetText);
        }
        if (marker != null && marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }

       /* LinearLayout infoLayout = (LinearLayout) myContentsView.findViewById(R.id.infoLayout);

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdate(lineNo, lstLines, lstLNames, lstRoutes);
                if (marker != null && marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
            }
        });*/
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
