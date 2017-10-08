package com.instinctcoder.sqlitedb;

/**
 * Created by IT001 on 23-Jun-16.
 */
public class Student {
    // Labels table name
    public static final String TABLE = "Student";
    public static final String TABLE_History = "History";
    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_email = "email";
    public static final String KEY_age = "age";
    public static final String KEY_emergencyname = "emergencyName";
    public static final String KEY_emergencyemail = "emergencyEmail";
    public static final String KEY_emergencyphone = "emergencyPhone";

    public static final String KEY_Sl = "sl";
    public static final String KEY_time = "time";
    public static final String KEY_score = "score";
    public static final String KEY_decision = "decision";
    public static final String KEY_subjName = "subjname";
    // property help us to keep data
    public int student_ID;
    public String name;
    public String email;
    public int age;
    public String emergency_name;
    public String emergency_email;
    public String emergency_phone;
    public boolean registered;

    public int sl_no;
    public String time;
    public double score;
    public int decision;
    public String subjName;
}