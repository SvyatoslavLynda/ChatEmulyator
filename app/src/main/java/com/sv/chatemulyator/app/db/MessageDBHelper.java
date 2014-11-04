package com.sv.chatemulyator.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Created by IntelliJ IDEA.
 * User: SV
 * Date: 04.11.2014
 * Time: 1:27
 * For the ChatEmulyator project.
 */


public class MessageDBHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chat";

    private static final String TABLE_NAME = "MESSAGES";
    private static final String MESSAGE_ID = "_id";
    private static final String MESSAGE_TEXT = "MESSAGE_TEXT";
    private static final String MY_MESSAGE = "MY_MESSAGE";
    private static final String TIME_STAMPE = "TIME_STAMPE";

    private DBHelper openHelper;
    private SQLiteDatabase database;

    public MessageDBHelper(Context aContext) {

        openHelper = new DBHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertData(String text, int flag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_TEXT, text);
        SimpleDateFormat s = new SimpleDateFormat("HH:mm yyyy/MM/dd");
        contentValues.put(TIME_STAMPE, s.format(new Date()));
        contentValues.put(MY_MESSAGE, flag);

        database.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData () {
        String buildSQL = "SELECT * FROM " + TABLE_NAME;
        return database.rawQuery(buildSQL, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MESSAGE_TEXT + " TEXT, "+
                    TIME_STAMPE + " TEXT, "+
                    MY_MESSAGE+ " INTEGER );";
            db.execSQL(buildSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(buildSQL);
            onCreate(db);
        }
    }
}