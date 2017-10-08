package com.instinctcoder.sqlitedb;

/**
 * Created by IT001 on 23-Jun-16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Student.TABLE  + "("
                + Student.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Student.KEY_name + " TEXT, "
                + Student.KEY_age + " INTEGER, "
                + Student.KEY_email + " TEXT  ,"
                + Student.KEY_emergencyname + " TEXT, "
                + Student.KEY_emergencyemail + " TEXT, "
                + Student.KEY_emergencyphone + " TEXT )";

        db.execSQL(CREATE_TABLE_STUDENT);
        Log.v("DatabaseCreate","User table created successfully");


        String CREATE_TABLE_HISTORY = "CREATE TABLE " + Student.TABLE_History  + "("
                + Student.KEY_Sl  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Student.KEY_subjName + " TEXT, "
                + Student.KEY_time + " TEXT, "
                + Student.KEY_score + " INTEGER, "
                + Student.KEY_decision + " INTEGER )";

        db.execSQL(CREATE_TABLE_HISTORY);
        Log.v("DatabaseCreate","Log table created successfully");
        //Toast.makeText(this.getActivity(), "No Log saved!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE);

        // Create tables again
        onCreate(db);

    }

}