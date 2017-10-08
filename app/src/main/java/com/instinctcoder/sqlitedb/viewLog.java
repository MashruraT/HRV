package com.instinctcoder.sqlitedb;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//public class viewLog extends ListActivity implements View.OnClickListener {
public class viewLog extends Activity implements View.OnClickListener {

    Button buttonClose;
    TextView tvHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);

        buttonClose = (Button) findViewById(R.id.close);
        buttonClose.setOnClickListener(this);

        tvHistory = (TextView) findViewById(R.id.tvHistory);

        StudentRepo repo = new StudentRepo(this);
        ArrayList<HashMap<String, String>> DepressionLog =  repo.getDepressionLog();
        if(DepressionLog.size()>0)
        {
            for (HashMap<String, String> map : DepressionLog){
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    if(entry.getKey()=="Decision")
                    {
//                        if(entry.getValue()== "1")
//                        {
//                            tvHistory.append("Decision" + " :\t" + "Depressed"+"\n");
//                        }
//                        else
//                        {
//                            tvHistory.append("Decision" + " :\t" + "Not Depressed"+"\n");
//                        }
                    }
                    else{
                        tvHistory.append(entry.getKey() + " :\t" + entry.getValue()+"\n");
                  }
                }
                tvHistory.append("\n\n");
            }
//            tvHistory.append("\n\n");
        }

//            ListView lv = getListView();
//            ListAdapter adapter = new SimpleAdapter( viewLog.this,DepressionLog, R.layout.view_log_entry, new String[] { "Time","Score", "Decision"}, new int[] {R.id.time, R.id.score, R.id.decision});
//            setListAdapter(adapter);
        else {
            Toast.makeText(this, "No Log saved!", Toast.LENGTH_SHORT).show();

        }


    }


    public void onClick(View v)
    {
        if (v== findViewById(R.id.close)){
            finish();
        }
    }
}
