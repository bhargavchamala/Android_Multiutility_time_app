package net.ddns.bhargav.calendar;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class RemainderDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "remainder_database";
    public static final String COLUMN_REMAINDER_NAME = "remainder_name";
    public static final String COLUMN_REMAINDER_I = "remainder_id";
    public static final String COLUMN_REMAINDER_DESCRIPTION = "remainder_description";
    public static final String COLUMN_REMAINDER_YEAR = "remainder_year";
    public static final String COLUMN_REMAINDER_MONTH = "remainder_month";
    public static final String COLUMN_REMAINDER_DAY = "remainder_day";


    public RemainderDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE "+TABLE_NAME+" ("
                +COLUMN_REMAINDER_I+" text primary key,"
                +COLUMN_REMAINDER_NAME+" text,"
                +COLUMN_REMAINDER_YEAR+" integer,"
                +COLUMN_REMAINDER_MONTH+" integer,"
                +COLUMN_REMAINDER_DAY+" integer,"
                +COLUMN_REMAINDER_DESCRIPTION+" text)";
        Log.d("dwaraka", "Create table: " + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertRemainder(String REMAINDER_I,String REMAINDER_NAME,int REMAINDER_YEAR,int REMAINDER_MONTH,int REMAINDER_DAY,String REMAINDER_DESCRIPTION){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REMAINDER_I,REMAINDER_I);
        contentValues.put(COLUMN_REMAINDER_NAME,REMAINDER_NAME);
        contentValues.put(COLUMN_REMAINDER_YEAR,REMAINDER_YEAR);
        contentValues.put(COLUMN_REMAINDER_MONTH,REMAINDER_MONTH);
        contentValues.put(COLUMN_REMAINDER_DAY,REMAINDER_DAY);
        contentValues.put(COLUMN_REMAINDER_DESCRIPTION,REMAINDER_DESCRIPTION);
        long l= db.insert(TABLE_NAME,null,contentValues);
        Log.d("dwaraka", "added row at position " + l);
        return l;
    }

    public Cursor getRemainderDetails(String REMAINDER_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_REMAINDER_I + " = '" +REMAINDER_ID+"'", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateRemainder(String REMAINDER_I,String REMAINDER_NAME,int REMAINDER_YEAR,int REMAINDER_MONTH,int REMAINDER_DAY,String REMAINDER_DESCRIPTION){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REMAINDER_NAME,REMAINDER_NAME);
        contentValues.put(COLUMN_REMAINDER_YEAR,REMAINDER_YEAR);
        contentValues.put(COLUMN_REMAINDER_MONTH,REMAINDER_MONTH);
        contentValues.put(COLUMN_REMAINDER_DAY,REMAINDER_DAY);
        contentValues.put(COLUMN_REMAINDER_DESCRIPTION,REMAINDER_DESCRIPTION);
        long l= db.update(TABLE_NAME, contentValues, COLUMN_REMAINDER_I + " = '" +REMAINDER_I+"'",null);
        Log.d("dwaraka","updated row at position "+l);
        return true;
    }

    public void deleteRemainder(String REMAINDER_I){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete=db.delete(TABLE_NAME, COLUMN_REMAINDER_I + " = '" +REMAINDER_I+"'",null);
        Log.d("dwaraka","deleted code returned = "+delete);
    }

    public ArrayList<String> getAllRemainders()
    {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_REMAINDER_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllRemainderIds()
    {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_REMAINDER_I)));
            res.moveToNext();
        }
        return array_list;
    }
}
