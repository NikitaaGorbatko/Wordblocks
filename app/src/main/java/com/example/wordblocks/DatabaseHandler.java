package com.example.wordblocks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "local.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS languages (" +
                "lang VARCHAR(100) PRIMARY KEY," +
                "keyx VARCHAR(5));\n");

        db.execSQL("CREATE TABLE IF NOT EXISTS topics (" +
                "top VARCHAR(100) UNIQUE NOT NULL PRIMARY KEY);\n");

        db.execSQL("CREATE TABLE IF NOT EXISTS word_sets (" +
                "word_set_id SERIAL PRIMARY KEY,\n" +
                "lang_id VARCHAR(100) REFERENCES languages(lang),\n" +
                "topic_id VARCHAR(100) REFERENCES topics(top),\n" +
                "name VARCHAR(120) UNIQUE NOT NULL,\n" +
                "description VARCHAR(250) UNIQUE NOT NULL,\n" +
                "data TEXT NOT NULL,\n" +
                "cost INTEGER);\n");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}
