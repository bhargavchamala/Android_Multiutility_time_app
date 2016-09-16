package net.ddns.bhargav.calendar;

/**
 * Created by dwaraka on 9/13/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "alarm_database";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    public static final String COLUMN_ALARM_I = "alarm_id";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MIN = "min";
    public static final String COLUMN_EVERYDAY = "everyday";
    public static final String COLUMN_STATE = "state";


    public AlarmDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE "+TABLE_NAME+" ("
                +COLUMN_ALARM_I+" text primary key,"
                +COLUMN_ALARM_NAME+" text,"
                +COLUMN_HOUR+" integer,"
                +COLUMN_MIN+" integer,"
                +COLUMN_EVERYDAY+" integer,"
                +COLUMN_STATE+" integer)";
        Log.d("dwaraka", "Create table: " + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertAlarm(String ALARM_ID,String ALARM_NAME,int HOUR,int MIN,int EVERYDAY,int STATE){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALARM_I,ALARM_ID);
        contentValues.put(COLUMN_ALARM_NAME,ALARM_NAME);
        contentValues.put(COLUMN_HOUR,HOUR);
        contentValues.put(COLUMN_MIN,MIN);
        contentValues.put(COLUMN_EVERYDAY,EVERYDAY);
        contentValues.put(COLUMN_STATE,STATE);
        long l= db.insert(TABLE_NAME,null,contentValues);
        Log.d("dwaraka", "added row at position " + l);
        return l;
    }

    public Cursor getAlarmDetails(String ALARM_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ALARM_I + " = '" +ALARM_ID+"'", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateAlarm(String ALARM_ID,String ALARM_NAME,int HOUR,int MIN,int EVERYDAY,int STATE){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALARM_NAME,ALARM_NAME);
        contentValues.put(COLUMN_HOUR,HOUR);
        contentValues.put(COLUMN_MIN,MIN);
        contentValues.put(COLUMN_EVERYDAY,EVERYDAY);
        contentValues.put(COLUMN_STATE,STATE);
        long l= db.update(TABLE_NAME, contentValues, COLUMN_ALARM_I + " = '" +ALARM_ID+"'",null);
        Log.d("dwaraka","updated row at position "+l);
        return true;
    }

    public void deleteAlarm(String ALARM_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete=db.delete(TABLE_NAME, COLUMN_ALARM_I + " = '" +ALARM_ID+"'",null);
        Log.d("dwaraka","deleted code returned = "+delete);
    }

    public ArrayList<String> getAllAlarms()
    {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_ALARM_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllAlarmIds()
    {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_ALARM_I)));
            res.moveToNext();
        }
        return array_list;
    }
}
