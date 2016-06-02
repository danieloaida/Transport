package ro.ratt.transport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 4/22/2016.
 */
public class InitDB {
    Context context;
    DBHandler dbHandler;


    public InitDB(Context context, DBHandler dbHandler) {
        this.context = context;
        this.dbHandler = dbHandler;
    }

    public void StartInit() {
        PopulateDB();

    }

    public void PopulateDB() {
        String row[] = {};
        List<String[]> jonctList = new ArrayList<String[]>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getBoolean("firstTime", false)) {

            InputStream inputStream = context.getResources().openRawResource(R.raw.junctions);
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
            try{

            for(;;) {
                row = csvReader.readNext();
                if(row != null) {
                    jonctList.add(row);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
            }
            for (String[] jData : jonctList) {
                Junction tmpJunction = new Junction(jData);
                dbHandler.addJunction(tmpJunction);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }
}
