package com.agnostix.activent.activent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Shivam on 11/11/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    //database contract
    public static abstract class macAddressEntry implements BaseColumns {
        public static final String TABLE_NAME = "macEntries";
        public static final String C_RADDR = "router_address";
        public static final String C_PLACE = "router_place";
        public static final String C_COARSE_PLACE = "router_coarse_position";

        public static final int _ID_INDEX = 0;
        public static final int C_RADDR_INDEX = 1;
        public static final int C_PLACE_INDEX = 2;
        public static final int C_COARSE_PLACE_INDEX  = 3;
    }

    public static abstract class eventEntry implements BaseColumns{
        public static final String TABLE_NAME = "eventEntries";
        public static final String C_TITLE = "event_title";
        public static final String C_DAY = "event_day";
        public static final String C_START_TIME = "event_start_time";
        public static final String C_END_TIME = "event_end_time";
        public static final String C_PLACE = "event_place";
        public static final String C_THREAD_ID = "thread_id";


        public static final int C_TITLE_INDEX = 0;
        public static final int C_DAY_INDEX = 1;
        public static final int C_START_TIME_INDEX  = 2;
        public static final int C_END_TIME_INDEX  = 3;
        public static final int C_PLACE_INDEX  = 4;
        public static final int C_THREAD_ID_INDEX = 5;

    }

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Activent.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " INTEGER"; //using unix timestamps
    private static final String SEP = ", ";

    private static final String COM_CREATE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String COM_DROP = "DROP TABLE ";
    private static final String COM_DELETE = "DELETE FROM ";
    private static final String COM_SELECT = "SELECT * FROM ";
    private static final String COM_UPDATE = "UPDATE ";

    private static final String SQL_CREATE_MAC_TABLE =
            COM_CREATE_IF_NOT_EXISTS
                    + macAddressEntry.TABLE_NAME + " ("
                    + macAddressEntry._ID + " INTEGER PRIMARY KEY" + SEP
                    + macAddressEntry.C_RADDR + TEXT_TYPE + SEP
                    + macAddressEntry.C_PLACE + TEXT_TYPE + SEP
                    + macAddressEntry.C_COARSE_PLACE + TEXT_TYPE
                    + " );";
    private static final String SQL_CREATE_EVENT_TABLE =
            COM_CREATE_IF_NOT_EXISTS
                    + eventEntry.TABLE_NAME + " ("
                    + eventEntry._ID + " INTEGER PRIMARY KEY" + SEP
                    + eventEntry.C_TITLE + TEXT_TYPE + SEP
                    + eventEntry.C_DAY + DATE_TYPE + SEP
                    + eventEntry.C_START_TIME + DATE_TYPE + SEP
                    + eventEntry.C_END_TIME + DATE_TYPE + SEP
                    + eventEntry.C_PLACE + TEXT_TYPE + SEP
                    + eventEntry.C_THREAD_ID + TEXT_TYPE
                    + " );";

    private static final String SQL_DELETE_MAC_TABLE =
            COM_DROP
                    + macAddressEntry.TABLE_NAME + SEP;
    private static final String SQL_DELETE_EVENT_TABLE =
            COM_DROP
                    + eventEntry.TABLE_NAME + SEP;


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MAC_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        createNewMacEntries(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MAC_TABLE);
        db.execSQL(SQL_DELETE_EVENT_TABLE);
        onCreate(db);
    }

    public void createNewMacEntries(SQLiteDatabase db){
        ContentValues values = new ContentValues();

        for (int value = 0; value < MacMapping.macMap.length; value++) {
            values.clear();
            String[] map = MacMapping.macMap[value];

            values.put(macAddressEntry.C_RADDR, map[0]);
            values.put(macAddressEntry.C_PLACE, map[1]);
            values.put(macAddressEntry.C_COARSE_PLACE, map[2]);

            db.insert(macAddressEntry.TABLE_NAME, "null", values);
        }
    }

    public String[] getPlaceFromMac(String macAddress){
        String[] placeInfo = new String[4];
        if(macAddress == null || macAddress.trim().equals("") ){
            return placeInfo;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                macAddressEntry._ID,
                macAddressEntry.C_RADDR,
                macAddressEntry.C_PLACE,
                macAddressEntry.C_COARSE_PLACE
        };
        String sortOrder = macAddressEntry._ID + " ASC";
        String selection = macAddressEntry.C_RADDR + "=?";
        String[] selectionArgs = new String[]{macAddress};
        Cursor cursor = db.query(
                macAddressEntry.TABLE_NAME,
                projection,
                selection, selectionArgs,
                null, null,
                sortOrder
                );

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            long item_id = cursor.getLong(cursor.getColumnIndexOrThrow(macAddressEntry._ID));
            placeInfo[macAddressEntry._ID_INDEX] = String.valueOf(item_id);
            placeInfo[macAddressEntry.C_RADDR_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(macAddressEntry.C_RADDR));
            placeInfo[macAddressEntry.C_PLACE_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(macAddressEntry.C_PLACE));
            placeInfo[macAddressEntry.C_COARSE_PLACE_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(macAddressEntry.C_COARSE_PLACE));
        }

        return placeInfo;
    }

    public void addNewActivityEntry(String[] eventDetails){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues entryValues = new ContentValues();

        entryValues.put(eventEntry.C_TITLE, eventDetails[eventEntry.C_TITLE_INDEX]);
        entryValues.put(eventEntry.C_DAY, eventDetails[eventEntry.C_DAY_INDEX]);
        entryValues.put(eventEntry.C_START_TIME, eventDetails[eventEntry.C_START_TIME_INDEX]);
        entryValues.put(eventEntry.C_END_TIME, eventDetails[eventEntry.C_END_TIME_INDEX]);
        entryValues.put(eventEntry.C_PLACE, eventDetails[eventEntry.C_PLACE_INDEX]);
        entryValues.put(eventEntry.C_THREAD_ID, eventDetails[eventEntry.C_THREAD_ID_INDEX]);

        db.insert(eventEntry.TABLE_NAME, "null", entryValues);
    }


    public String[] getActivityEntry(int eventID){
        String[] activityEntry = new String[6];

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                eventEntry.C_TITLE,
                eventEntry.C_DAY,
                eventEntry.C_START_TIME,
                eventEntry.C_END_TIME,
                eventEntry.C_PLACE,
                eventEntry.C_THREAD_ID
        };

        String selection = macAddressEntry.C_RADDR + "=?";
        String[] selectionArgs = new String[]{String.valueOf(eventID)};
        Cursor cursor = db.query(
                macAddressEntry.TABLE_NAME,
                projection,
                selection, selectionArgs,
                null, null,
                null
        );

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            activityEntry[eventEntry.C_TITLE_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_TITLE));
            activityEntry[eventEntry.C_DAY_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_DAY));
            activityEntry[eventEntry.C_START_TIME_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_START_TIME));
            activityEntry[eventEntry.C_END_TIME_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_END_TIME));
            activityEntry[eventEntry.C_PLACE_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_PLACE));
            activityEntry[eventEntry.C_THREAD_ID_INDEX] = cursor.getString(cursor.getColumnIndexOrThrow(eventEntry.C_THREAD_ID));
        }

        return activityEntry;
    }
}
