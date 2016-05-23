package ro.ratt.transport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baby on 4/6/2016.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="transport.db";

    public static final String TABLE_JUNCTIONS = "junctions";
    public static final String COLUMN_INDEX ="nr_crt";
    public static final String COLUMN_lineID = "lineID";
    public static final String COLUMN_lineName = "lineName";
    public static final String COLUMN_stationID = "stationID";
    public static final String COLUMN_rawStationName = "rawStationName";
    public static final String COLUMN_friendlyStationName = "friendlyStationName";
    public static final String COLUMN_shortStationName = "shortStationName";
    public static final String COLUMN_junctionName = "junctionName";
    public static final String COLUMN_lat = "lat";
    public static final String COLUMN_lng = "lng";
    public static final String COLUMN_invalid = "invalid";
    public static final String COLUMN_verificationDate = "verificationDate";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_JUNCTIONS + " ( '" +
                COLUMN_INDEX + "' INTEGER, '"  +
                COLUMN_lineID + "' INTEGER, '"  +
                COLUMN_lineName + "' TEXT, '"  +
                COLUMN_stationID + "' INTEGER, '"  +
                COLUMN_rawStationName + "' TEXT, '"  +
                COLUMN_friendlyStationName + "' TEXT, '"  +
                COLUMN_shortStationName + "' TEXT, '"  +
                COLUMN_junctionName + "' TEXT, '"  +
                COLUMN_lat + "' DOUBLE, '"  +
                COLUMN_lng + "' DOUBLE, '"  +
                COLUMN_invalid + "' TEXT, '"  +
                COLUMN_verificationDate + "' TEXT "  +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUNCTIONS);
        onCreate(db);
    }

    // Insert data Object
    public void addJunction(Junction junction){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_INDEX, junction.getIndex());
        values.put(COLUMN_lineID, junction.getLineID());
        values.put(COLUMN_lineName, junction.getLineName());
        values.put(COLUMN_stationID, junction.getStationID());
        values.put(COLUMN_rawStationName, junction.getRawStationName());
        values.put(COLUMN_friendlyStationName, junction.getFriendlyStationName());
        values.put(COLUMN_shortStationName, junction.getShortStationName());
        values.put(COLUMN_junctionName, junction.getJunctionName());
        values.put(COLUMN_lat, junction.getLat());
        values.put(COLUMN_lng, junction.getLng());
        values.put(COLUMN_invalid, junction.isInvalid());
        values.put(COLUMN_verificationDate, junction.getVerificationDate());
        db.insert(TABLE_JUNCTIONS, null, values);
        db.close();
    }


    // Delete data
    public void deleteJunction(int index){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_INDEX + "=\"" + index + "\";");
    }

    public List<String> getListOfTransport(int option){
        List<String> lstReturn = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query;
        switch (option){
            case 1: query = "SELECT " + COLUMN_lineName + ", "+ COLUMN_lineID +" FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_lineName + " LIKE 'Tv%' GROUP BY " + COLUMN_lineName;
                break;
            case 2: query = "SELECT " + COLUMN_lineName + ", "+ COLUMN_lineID +" FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_lineName + " LIKE 'Tb%' GROUP BY " + COLUMN_lineName;
                break;
            case 3: query = "SELECT " + COLUMN_lineName + ", "+ COLUMN_lineID +" FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_lineName + " LIKE 'E%' GROUP BY " + COLUMN_lineName;
                break;
            case 4: query = "SELECT " + COLUMN_lineName + ", "+ COLUMN_lineID +" FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_lineName + " NOT LIKE 'Tv%' AND " + COLUMN_lineName + " NOT LIKE 'Tb%' AND " + COLUMN_lineName + " NOT LIKE 'E%' GROUP BY " + COLUMN_lineName;
                break;
            default: query = "";

        }
        //Cursor point to a location in results
        Cursor c = db.rawQuery(query, null);
        //Move to the first raw in result
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_lineName)) != null){
                lstReturn.add(c.getString(c.getColumnIndex(COLUMN_lineName)));
                c.moveToNext();
            }
        }
        db.close();

        return lstReturn;
    }

    public List<String> getListOfStations(String line){
        List<String> lstReturn = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query;

        query = "SELECT " + COLUMN_friendlyStationName + " FROM " + TABLE_JUNCTIONS + " WHERE " + COLUMN_lineName + "=\"" + line + "\";";

        //Cursor point to a location in results
        Cursor c = db.rawQuery(query, null);
        //Move to the first raw in result
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_friendlyStationName)) != null){
                lstReturn.add(c.getString(c.getColumnIndex(COLUMN_friendlyStationName)));
                c.moveToNext();
            }
        }
        db.close();

        return lstReturn;
    }
    // Print data
    public String dbToString( String columnName){
        String dbString = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JUNCTIONS + " WHERE 1";

        //Cursor point to a location in results
        Cursor c = db.rawQuery(query, null);
        //Move to the first raw in result
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(columnName)) != null){
                dbString += c.getString(c.getColumnIndex(columnName));
                dbString += "\n";
                c.moveToNext();
            }
        }
        db.close();
        return dbString;
    }


}
