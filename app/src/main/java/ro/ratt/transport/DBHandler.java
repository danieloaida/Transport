package ro.ratt.transport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baby on 4/6/2016.
 */
public class DBHandler extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME ="transport.db";
        public static final String TABLE_STATIONS = "stations";
        public static final String COLUMN_STATIONID ="stationid";
        public static final String COLUMN_STATIONNAME ="stationname";
        public static final String COLUMN_LAT ="lat";
        public static final String COLUMN_LNG ="lng";
        public static final String TABLE_LINES = "lines";
        public static final String COLUMN_LINEID ="lineid";
        public static final String COLUMN_LINENAME ="linename";
        public static final String COLUMN_INDEX ="ind";

        public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);


    }

        @Override
        public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_STATIONS + "(" +
                COLUMN_STATIONID + " INTEGER PRIMARY KEY, "  +
                COLUMN_STATIONNAME + " TEXT, "  +
                COLUMN_LAT + " DOUBLE, "  +
                COLUMN_LNG + " DOUBLE "  +
                ");";
        db.execSQL(query);

   /*    query = "CREATE TABLE " + TABLE_LINES + "(" +
                COLUMN_INDEX + " INTEGER PRIMARY KEY"  +
                COLUMN_LINEID + " INTEGER"  +
                COLUMN_STATIONID + " INTEGER"  +
                COLUMN_LINENAME + " TEXT "  +
                ");";

        db.execSQL(query);*/


    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        onCreate(db);
    }

        // Insert data Object
    public void addObj(Object item){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        if (item instanceof Lines){
            values.put(COLUMN_LINEID,((Lines) item).getLineId());
            values.put(COLUMN_STATIONID, ((Lines) item).getStationId());
            values.put(COLUMN_LINENAME, ((Lines) item).getLineName());
            db.insert(TABLE_LINES, null, values);
            db.close();
        }
        else if (item instanceof Station){
            values.put(COLUMN_STATIONID, ((Station) item).getId_st());
            values.put(COLUMN_STATIONNAME, ((Station) item).getName());
            values.put(COLUMN_LAT, ((Station) item).getLat());
            values.put(COLUMN_LNG, ((Station) item).getLng());
            db.insert(TABLE_STATIONS, null, values);
            db.close();
        }
    }

    // Insert data Object
    public void addLine(int id, int stationid,String name ){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_LINEID, id);
        values.put(COLUMN_STATIONID, stationid);
        values.put(COLUMN_LINENAME, name);
        db.insert(TABLE_LINES, null, values);
        db.close();

    }
    // Insert Station
    public void addStation(int id, String name, double lat, double lng){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put(COLUMN_STATIONID, id);
        values.put(COLUMN_STATIONNAME, name);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);
        db.insert(TABLE_STATIONS, null, values);
        db.close();
    }

    // Delete data
    public void delete(int index, String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName + " WHERE " + COLUMN_INDEX + "=\"" + index + "\";");
    }

    // Print data
    public String dbToString(String tableName, String columnName){
        String dbString = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE 1";

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
