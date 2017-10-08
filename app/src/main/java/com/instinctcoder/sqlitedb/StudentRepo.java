package com.instinctcoder.sqlitedb;

/**
 * Created by IT001 on 23-Jun-16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentRepo {
    private DBHelper dbHelper;

    public StudentRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Student student) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.KEY_age, student.age);
        values.put(Student.KEY_email,student.email);
        values.put(Student.KEY_name, student.name);
        values.put(Student.KEY_emergencyname, student.emergency_name);
        values.put(Student.KEY_emergencyphone,student.emergency_phone);
        values.put(Student.KEY_emergencyemail, student.emergency_email);
        // Inserting Row
        long student_Id = db.insert(Student.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) student_Id;
    }
    public int saveLog(Student student) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.KEY_subjName, student.subjName);
        values.put(Student.KEY_time, student.time);
        values.put(Student.KEY_score,student.score);
        values.put(Student.KEY_decision, student.decision);
        // Inserting Row
        long SL_No = db.insert(Student.TABLE_History, null, values);
        db.close(); // Closing database connection
        return (int) SL_No;
    }
    public void delete(int student_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Student.TABLE, Student.KEY_ID + "= ?", new String[] { String.valueOf(student_Id) });
        db.close(); // Closing database connection
    }

    public void update(Student student) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Student.KEY_age, student.age);
        values.put(Student.KEY_email,student.email);
        values.put(Student.KEY_name, student.name);
        Log.v("DB","Emergency Name: " + student.emergency_name);
        Log.v("DB","Emergency Phone: " + student.emergency_phone);
        Log.v("DB","Emergency Email: " + student.emergency_email);
        values.put(Student.KEY_emergencyname, student.emergency_name);
        values.put(Student.KEY_emergencyphone,student.emergency_phone);
        values.put(Student.KEY_emergencyemail, student.emergency_email);
        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Student.TABLE, values, Student.KEY_ID + "= ?", new String[] { String.valueOf(student.student_ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getStudentList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Student.KEY_ID + "," +
                Student.KEY_name + "," +
                Student.KEY_email + "," +
                Student.KEY_age + "," +
                Student.KEY_emergencyname + "," +
                Student.KEY_emergencyphone + "," +
                Student.KEY_emergencyemail +
                " FROM " + Student.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> student = new HashMap<String, String>();
                student.put("id", cursor.getString(cursor.getColumnIndex(Student.KEY_ID)));
                student.put("name", cursor.getString(cursor.getColumnIndex(Student.KEY_name)));
                studentList.add(student);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;

    }
    public String getFirstStudentEmergencyPhone() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Student.KEY_ID + "," +
                Student.KEY_name + "," +
                Student.KEY_email + "," +
                Student.KEY_age + "," +
                Student.KEY_emergencyname + "," +
                Student.KEY_emergencyphone + "," +
                Student.KEY_emergencyemail +
                " FROM " + Student.TABLE;

        //Student student = new Student();
       // ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
String phone ="";
        if (cursor.moveToFirst()) {
         //   do {
           //     HashMap<String, String> student = new HashMap<String, String>();
            //    student.put("id", cursor.getString(cursor.getColumnIndex(Student.KEY_ID)));
             phone =     cursor.getString(cursor.getColumnIndex(Student.KEY_emergencyphone));
           //     studentList.add(student);

           // } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return phone;
    }
    public String getFirstStudentSelfName() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Student.KEY_ID + "," +
                Student.KEY_name + "," +
                Student.KEY_email + "," +
                Student.KEY_age + "," +
                Student.KEY_emergencyname + "," +
                Student.KEY_emergencyphone + "," +
                Student.KEY_emergencyemail +
                " FROM " + Student.TABLE;

        //Student student = new Student();
        // ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        String name ="";
        if (cursor.moveToFirst()) {
            //   do {
            //     HashMap<String, String> student = new HashMap<String, String>();
            //    student.put("id", cursor.getString(cursor.getColumnIndex(Student.KEY_ID)));
            name =     cursor.getString(cursor.getColumnIndex(Student.KEY_name));
            //     studentList.add(student);

            // } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return name;
    }


    public ArrayList<HashMap<String, String>>  getDepressionLog() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Student.KEY_subjName + "," +
                Student.KEY_time + "," +
                Student.KEY_score + "," +
                Student.KEY_decision+
                " FROM " + Student.TABLE_History;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> DepressionLog = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> Log = new HashMap<String, String>();
                Log.put("Name", cursor.getString(cursor.getColumnIndex(Student.KEY_subjName)));
                Log.put("Time", cursor.getString(cursor.getColumnIndex(Student.KEY_time)));
                Log.put("Score", cursor.getString(cursor.getColumnIndex(Student.KEY_score)));
                Log.put("Decision",cursor.getString(cursor.getColumnIndex(Student.KEY_decision)));
                DepressionLog.add(Log);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return DepressionLog;

    }
    public Student getStudentById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Student.KEY_ID + "," +
                Student.KEY_name + "," +
                Student.KEY_email + "," +
                Student.KEY_age + "," +
                Student.KEY_emergencyname + "," +
                Student.KEY_emergencyphone + "," +
                Student.KEY_emergencyemail +
                " FROM " + Student.TABLE
                + " WHERE " +
                Student.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Student student = new Student();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                student.student_ID =cursor.getInt(cursor.getColumnIndex(Student.KEY_ID));
                student.name =cursor.getString(cursor.getColumnIndex(Student.KEY_name));
                student.email  =cursor.getString(cursor.getColumnIndex(Student.KEY_email));
                student.age =cursor.getInt(cursor.getColumnIndex(Student.KEY_age));
                student.emergency_name =cursor.getString(cursor.getColumnIndex(Student.KEY_emergencyname));
                student.emergency_email  =cursor.getString(cursor.getColumnIndex(Student.KEY_emergencyemail));
                student.emergency_phone =cursor.getString(cursor.getColumnIndex(Student.KEY_emergencyphone));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return student;
    }

}