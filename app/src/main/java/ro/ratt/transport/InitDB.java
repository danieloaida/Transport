package ro.ratt.transport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 4/22/2016.
 */
public class InitDB {
    Context context;
    DBHandler dbHandler;
    CSVReader csvReader;


    public InitDB(Context context, DBHandler dbHandler) {
        this.context = context;
        this.dbHandler= dbHandler;
    }

    public void StartInit(){
        PopulateDB();

    }
    public void PopulateDB(){
        InputStream inputStream = context.getResources().openRawResource(R.raw.junctions);
        csvReader = new CSVReader(inputStream);

        List<Junction> jonctList = new ArrayList<Junction>();
        jonctList = csvReader.read();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.getBoolean("firstTime", false)) {
        for (Junction jData : jonctList){
            dbHandler.addJunction(jData);
        }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
        /*
        List<Station> list = new ArrayList<Station>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            InputStream is = context.getResources().openRawResource(R.raw.stations);
            list.addAll(xmlParser.parse(is));
            for (Station IterateStation : list) {
                LatLng sydney = new LatLng(IterateStation.getLat(), IterateStation.getLng());
                dbHandler.addJunction(IterateStation);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
*/
    }
}
