package com.android.ninos.techmaaxx.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;
import java.util.List;

import static com.android.ninos.techmaaxx.database.Bluetooth.COUNTABLE_LIST;
import static com.android.ninos.techmaaxx.database.Bluetooth.KEY_CAT;
import static com.android.ninos.techmaaxx.database.Bluetooth.KEY_REPORT_NAME;
import static com.android.ninos.techmaaxx.database.Bluetooth.REPORT_LIST;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_BOOKING;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_COUNT;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_REPORT;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_SELECTED_BOOKING;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_SELECTED_DATE;
import static com.android.ninos.techmaaxx.database.Bluetooth.TABLE_SIGN_UP_NAME;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    long result;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    Session session;

    // Database Name
    private static final String DATABASE_NAME = "Hotel_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //sessionForRepeatValue = new SessionForRepeatValue(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        //create sign up table
        db.execSQL(Bluetooth.CREATE_SIGN_UP_TABLE);


        //create Notification table
        db.execSQL(Bluetooth.CREATE_TABLE_BOOKING);

        db.execSQL(Bluetooth.SELECTED_BOOKING_TABLE);
        db.execSQL(Bluetooth.CREATE_TABLE_SELECTED_DATE);


        db.execSQL(COUNTABLE_LIST);
        db.execSQL(REPORT_LIST);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if existed sign u table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGN_UP_NAME);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);

        //Drop inspection table

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_BOOKING);

        //Drop inspection table

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_DATE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);

        onCreate(db);
    }

    public Cursor getTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tablename, null);
        return res;
    }


    //


   /* public boolean insertCountable(String id, String category, String product, String box, String prize) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Bluetooth.COLUMN_ID, id);
        contentValues.put(Bluetooth.KEY_CAT, category);
        contentValues.put(Bluetooth.KEY_PRODUCT, product);
        contentValues.put(Bluetooth.KEY_BOX, box);
        contentValues.put(Bluetooth.KEY_PRIZE, prize);

        result = db.insert(Bluetooth.TABLE_COUNT, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }*/

    public boolean updateCountable(String tablename, String id, String category, String product, String box, String prize) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Bluetooth.COLUMN_ID, id);
        contentValues.put(KEY_CAT, category);
        contentValues.put(Bluetooth.KEY_PRODUCT, product);
        contentValues.put(Bluetooth.KEY_BOX, box);
        contentValues.put(Bluetooth.KEY_PRIZE, prize);

        db.update(tablename, contentValues, "id = ?", new String[]{id});
        return true;
    }

    //to check rows/values is present or not in the table
    public int getProfilesCount(String tablename) {
        SQLiteDatabase db = this.getReadableDatabase();
        int cnt = (int) DatabaseUtils.queryNumEntries(db, tablename);
        db.close();
        return cnt;
    }

    //to check whether the table is present or not in the database
    public boolean checkTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur2 = db.rawQuery("select name from sqlite_master where name='"
                + table + "'", null);

        if (cur2.getCount() != 0) {
            if (!cur2.isClosed())
                cur2.close();
            return true;
        } else {
            if (!cur2.isClosed())
                cur2.close();
            return false;
        }
    }

    public long insertSelectedDate(String date, String date_mm_yy, String day, String day_full_name) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Bluetooth.COLUMN_DATE, date);
        values.put(Bluetooth.COLUMN_DATE_MM_YYYY, date_mm_yy);
        values.put(Bluetooth.COLUMN_DAY, day);
        values.put(Bluetooth.COLUMN_DAY_FULLNAME, day_full_name);

        // insert row
        long id = db.insert(TABLE_SELECTED_DATE, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insertreport(String r_name, String r_co, String r_orgin, String r_des, String r_freight, String r_date, String p_mode) {
        // get writable database as we want to write data

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_REPORT_NAME, r_name);
        contentValues.put(Bluetooth.KEY_REPORT_CO, r_co);
        contentValues.put(Bluetooth.KEY_REPORT_ORGIN, r_orgin);
        contentValues.put(Bluetooth.KEY_REPORT_DESTINATION, r_des);
        contentValues.put(Bluetooth.KEY_REPORT_FREIGHT, r_freight);
        contentValues.put(Bluetooth.KEY_REPORT_DATE, r_date);
        contentValues.put(Bluetooth.KEY_PAYMENT_MODE, p_mode);

        //result = db.insert(Bluetooth.TABLE_COUNT, null, contentValues);


        // insert row
        long id = db.insert(TABLE_REPORT, null, contentValues);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<Bluetooth> getAll_Report_list() {
        List<Bluetooth> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPORT + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";


        /*// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COUNT + " CATEGORY " + Bluetooth.KEY_CAT + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";
*/
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bluetooth note = new Bluetooth();
                note.setId(cursor.getInt(cursor.getColumnIndex(Bluetooth.COLUMN_ID)));
                note.setR_name(cursor.getString(cursor.getColumnIndex(KEY_REPORT_NAME)));
                note.setR_con(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_REPORT_CO)));
                note.setR_orgin(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_REPORT_ORGIN)));
                note.setR_des(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_REPORT_DESTINATION)));
                note.setR_freight(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_REPORT_FREIGHT)));
                note.setR_date(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_REPORT_DATE)));
                note.setP_mode(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_PAYMENT_MODE)));


                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }


    public long insertCountable(String category, String product, String box, String prize) {
        // get writable database as we want to write data

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_CAT, category);
        contentValues.put(Bluetooth.KEY_PRODUCT, product);
        contentValues.put(Bluetooth.KEY_BOX, box);
        contentValues.put(Bluetooth.KEY_PRIZE, prize);

        //result = db.insert(Bluetooth.TABLE_COUNT, null, contentValues);


        // insert row
        long id = db.insert(TABLE_COUNT, null, contentValues);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<Bluetooth> getAllNotes() {
        List<Bluetooth> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COUNT + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";


        /*// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COUNT + " CATEGORY " + Bluetooth.KEY_CAT + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";
*/
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bluetooth note = new Bluetooth();
                note.setId(cursor.getInt(cursor.getColumnIndex(Bluetooth.COLUMN_ID)));
                note.setCat(cursor.getString(cursor.getColumnIndex(KEY_CAT)));
                note.setPro(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_PRODUCT)));
                note.setBox(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_BOX)));
                note.setPrize(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_PRIZE)));


                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public List<Bluetooth> getAllNotes_SelectedDate() {
        List<Bluetooth> notes = new ArrayList<>();

        // Select All Query
      /* String selectQuery = "SELECT  * FROM " + TABLE_BOOKING + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";
*/

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SELECTED_DATE + " GROUP BY " + Bluetooth.COLUMN_DATE + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bluetooth note = new Bluetooth();
                note.setId(cursor.getInt(cursor.getColumnIndex(Bluetooth.COLUMN_ID)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Bluetooth.COLUMN_DATE)));
                note.setDate_mm_yyy(cursor.getString(cursor.getColumnIndex(Bluetooth.COLUMN_DATE_MM_YYYY)));
                note.setDay(cursor.getString(cursor.getColumnIndex(Bluetooth.COLUMN_DAY)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }


    public int sum() {
        int total = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + Bluetooth.KEY_PRIZE + ") as Total FROM " + Bluetooth.TABLE_COUNT, null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            return total;
        }
        return total;
    }

    public void deleteRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COUNT + " WHERE " + KEY_CAT + "='" + value + "'");
        db.close();
    }

    public void deleteNote(Bluetooth note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COUNTABLE_LIST, Bluetooth.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

     public void deleteAllRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Bluetooth.TABLE_COUNT);

    }


    public void deleteAllR() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Bluetooth.TABLE_REPORT);
    }

    public void getparticulareNote(Bluetooth note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SIGN_UP_NAME, Bluetooth.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

}
