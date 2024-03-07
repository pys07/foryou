package com.example.foryou.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Mydb.db";
    private static final String TABLE_NAME = "user_data";
    public static final String COL_1 = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ADDRESS = "address";
    public static final String COL_CONTACTS = "contacts";
    public static final String COL_LOCATION_LATITUDE = "latitude";
    public static final String COL_LOCATION_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " VARCHAR, " +
                COL_EMAIL + " VARCHAR, " +
                COL_PASSWORD + " VARCHAR, " +
                COL_ADDRESS + " VARCHAR, " +
                COL_CONTACTS + " VARCHAR," +
                COL_LOCATION_LATITUDE + " REAL," +
                COL_LOCATION_LONGITUDE + " REAL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String name, String email, String password, String address, String contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_ADDRESS, address);
        cv.put(COL_CONTACTS, contacts);

        db.insert(TABLE_NAME, null,cv);

        db.close();
    }

    public Cursor getData(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_EMAIL, COL_PASSWORD};
        String selection = COL_EMAIL + "=?";
        String[] selectionArgs = {email};

        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null,null);
    }

    public void insertLocation(String email, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_LOCATION_LATITUDE, latitude);
        cv.put(COL_LOCATION_LONGITUDE, longitude);

        String selection = COL_EMAIL + "=?";
        String[] selectionArgs = {email};

        db.update(TABLE_NAME, cv, selection, selectionArgs);


        db.close();
    }


    public List<String> getContacts(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_CONTACTS};
        String selection = COL_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        List<String> contactsList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            String contacts = cursor.getString(cursor.getColumnIndex(COL_CONTACTS));
            if (contacts != null && !contacts.isEmpty()) {
                // Split the contacts string into individual phone numbers
                String[] contactsArray = contacts.split(",");
                contactsList.addAll(Arrays.asList(contactsArray));
            }
            cursor.close();
        }

        return contactsList;
    }



}

