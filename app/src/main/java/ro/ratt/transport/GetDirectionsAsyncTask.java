package ro.ratt.transport;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by baby on 5/31/2016.
 */
public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList<LatLng>>
{
        public static final String USER_CURRENT_LAT = "user_current_lat";
        public static final String USER_CURRENT_LONG = "user_current_long";
        public static final String DESTINATION_LAT = "destination_lat";
        public static final String DESTINATION_LONG = "destination_long";
        public static final String DIRECTIONS_MODE = "directions_mode";
        private Context context;
        private Exception exception;
        private ProgressDialog progressDialog;
        private GoogleMap mMap;
        private List<MapStation> mapStationList = new ArrayList<MapStation>();
        private int pos;

        public GetDirectionsAsyncTask(Context context, GoogleMap mMap, List<MapStation> mapStationList, int i)
        {
                super();
                this.context = context;
                this.mMap = mMap;
                this.mapStationList = mapStationList;
                this.pos = i;
        }

        public void onPreExecute()
        {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Calculating directions");
               // progressDialog.show();
        }

        @Override
        public void onPostExecute(ArrayList result)
        {
              ///  progressDialog.dismiss();
                if (exception == null)
                {
                    ArrayList<LatLng> directionPoints = result;
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.argb(255, 0, 175, 255));

                    for(int i = 0 ; i < directionPoints.size() ; i++)
                    {
                        rectLine.add(directionPoints.get(i));
                    }

                    mapStationList.get(pos).line = mMap.addPolyline(rectLine);
                }
                else
                {
                        processException();
                }
        }

        @Override
        protected ArrayList<LatLng> doInBackground(Map<String, String>... params)
        {
                Map<String, String> paramMap = params[0];
                try
                {
                        LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)) , Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
                        LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
                        GMapV2Direction md = new GMapV2Direction();
                        Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
                        ArrayList<LatLng> directionPoints = md.getDirection(doc);
                        return directionPoints;
                }
                catch (Exception e)
                {
                        exception = e;
                        return null;
                }
        }

        private void processException()
        {
                Toast.makeText(context, "Error retriving data", 3000).show();
        }
}
