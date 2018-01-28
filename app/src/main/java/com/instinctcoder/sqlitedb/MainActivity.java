package com.instinctcoder.sqlitedb;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity  implements android.view.View.OnClickListener{

    Button btnAdd;//btnGetAll;
    TextView student_Id;

    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){
        //  sendSMS("01931300471", "hello world!");





            StudentRepo repo = new StudentRepo(this);

            ArrayList<HashMap<String, String>> studentList =  repo.getStudentList();
            if(studentList.size()!=0) {
//                ListView lv = getListView();
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                        student_Id = (TextView) view.findViewById(R.id.student_Id);
//                        String studentId = student_Id.getText().toString();
//                        Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
//                        objIndent.putExtra("student_Id", Integer.parseInt( studentId));
//                        startActivity(objIndent);
//                    }
//                });
//                ListAdapter adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
//                setListAdapter(adapter);

                //  Toast.makeText(this, "Student Record Deleted", Toast.LENGTH_SHORT);

                Intent intent = new Intent(this,BandMainActivity.class);
                intent.putExtra("Emergency_phone",repo.getFirstStudentEmergencyPhone());
                intent.putExtra("Self_name",repo.getFirstStudentSelfName());
                startActivity(intent);
            }else{
                Intent intent = new Intent(this,StudentDetail.class);
                intent.putExtra("student_Id",0);
                startActivity(intent);
               // Toast.makeText(this,"You have to register first!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

   //     btnGetAll = (Button) findViewById(R.id.btnGetAll);
     //   btnGetAll.setOnClickListener(this);

    }
public void sendSMS(String phoneNumber, String text){
  //  String phoneNo = textPhoneNo.getText().toString();
    //String sms = textSMS.getText().toString();
    Log.v("Message Sending","Message to be sent");
    try {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        Toast.makeText(getApplicationContext(), "SMS Sent!",
                Toast.LENGTH_LONG).show();
    } catch (Exception e) {
        Toast.makeText(getApplicationContext(),
                "SMS failed, please try again later!",
                Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    Log.v("Message Sending","Message sent!");


}

}