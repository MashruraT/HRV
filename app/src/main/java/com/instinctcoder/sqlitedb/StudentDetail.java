package com.instinctcoder.sqlitedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentDetail extends ActionBarActivity implements View.OnClickListener{

    Button btnSave ;//  btnDelete;
    Button btnClose;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextAge;
    EditText editTextEmergencyName;
    EditText editTextEmergencyEmail;
    EditText editTextEmergencyPhone;
    private int _Student_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
      //  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClose = (Button) findViewById(R.id.btnClose);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextEmergencyName = (EditText) findViewById(R.id.editTextEmergencyName);
        editTextEmergencyEmail = (EditText) findViewById(R.id.editTextEmergencyEmail);
        editTextEmergencyPhone = (EditText) findViewById(R.id.editTextEmergencyPhone);

        btnSave.setOnClickListener(this);
      //  btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        _Student_Id =0;
        Intent intent = getIntent();
        _Student_Id =intent.getIntExtra("student_Id", 0);
        StudentRepo repo = new StudentRepo(this);
        Student student = new Student();
        student = repo.getStudentById(_Student_Id);

        editTextAge.setText(String.valueOf(student.age));
        editTextName.setText(student.name);
        editTextEmail.setText(student.email);
        editTextEmergencyName.setText(student.emergency_name);
        editTextEmergencyEmail.setText(student.emergency_email);
        editTextEmergencyPhone.setText(student.emergency_phone);
    }



    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)){

            StudentRepo repo = new StudentRepo(this);
            Student student = new Student();
            student.age= Integer.parseInt(editTextAge.getText().toString());
            student.email=editTextEmail.getText().toString();
            student.name=editTextName.getText().toString();

            student.emergency_name=editTextEmergencyName.getText().toString();
            student.emergency_phone=editTextEmergencyPhone.getText().toString();
            student.emergency_email=editTextEmergencyEmail.getText().toString();
            student.student_ID=_Student_Id;
            Log.v("DB","Name: " + student.name );
            Log.v("DB","Emergency Name: " + student.emergency_name );
            Log.v("DB","Emergency Email: " + student.emergency_email );
            Log.v("DB","Emergency Phone: " + student.emergency_phone );
            if (_Student_Id==0){
                _Student_Id = repo.insert(student);

                Toast.makeText(this,"New User Inserted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,BandMainActivity.class);
                intent.putExtra("Emergency_phone",student.emergency_phone);
                intent.putExtra("Self_name", student.name);
                startActivity(intent);
            }else{
                repo.update(student);
                Toast.makeText(this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
            }
       //     BandMainActivity bandMainActivity = new BandMainActivity(this);
            //bandMainActivity.start();
        }//else if (view== findViewById(R.id.btnDelete)){
           // StudentRepo repo = new StudentRepo(this);
           // repo.delete(_Student_Id);
          //  Toast.makeText(this, "Student Record Deleted", Toast.LENGTH_SHORT);

          //  BandMainActivity bandMainActivity = new BandMainActivity(this);
          // finish();

  //  }
    else if (view== findViewById(R.id.btnClose)){
        finish();
    }


}

}
