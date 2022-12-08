package com.example.gps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String LOCATION_DATA_TABLE = "LOCATION_DATA_TABLE";
    public static final String COLUMN_AP_BSSID = "AP_BSSID";
    public static final String COLUMN_AP_LATITUDE = "AP_LATITUDE";
    public static final String COLUMN_AP_LONGITUDE = "AP_LONGITUDE";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "LocationData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + LOCATION_DATA_TABLE + " (" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_AP_BSSID + " TEXT, " + COLUMN_AP_LATITUDE + " REAL, " + COLUMN_AP_LONGITUDE + " REAL)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(LocationData locationData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AP_BSSID,locationData.getAp_BSSID());
        cv.put(COLUMN_AP_LATITUDE, locationData.getLatitude());
        cv.put(COLUMN_AP_LONGITUDE, locationData.getLongitude());
        long insert = db.insert(LOCATION_DATA_TABLE, null,cv);
        if (insert == -1)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public List<LocationData> selectAll(){
        List<LocationData> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ LOCATION_DATA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String locationDataBSSID = cursor.getString(0);
                double locationDataLatitude = cursor.getDouble(1);
                double locationDataLongitude = cursor.getDouble(2);
                LocationData newLocData = new LocationData(locationDataLatitude,locationDataLongitude,locationDataBSSID);
                returnList.add(newLocData);
            }while(cursor.moveToNext());
        }
        else{
            //failure. Do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<LocationData> selectWhereBSSID(String bssid){
        List<LocationData> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ LOCATION_DATA_TABLE + "WHERE " +COLUMN_AP_BSSID + " = " + bssid +";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                String locationDataBSSID = cursor.getString(0);
                double locationDataLatitude = cursor.getDouble(1);
                double locationDataLongitude = cursor.getDouble(2);
                LocationData newLocData = new LocationData(locationDataLatitude,locationDataLongitude,locationDataBSSID);
                returnList.add(newLocData);
            }while(cursor.moveToNext());
        }
        else{
            //failure. Do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }
}
