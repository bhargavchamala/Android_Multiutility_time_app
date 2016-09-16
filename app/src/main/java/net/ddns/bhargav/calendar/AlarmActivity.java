package net.ddns.bhargav.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.alarmList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),AddAlarmActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        final AlarmDatabaseHelper alarmDatabaseHelper = new AlarmDatabaseHelper(getBaseContext());
        ArrayList alarmList = alarmDatabaseHelper.getAllAlarms();
        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.list_item,alarmList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),AddAlarmActivity.class);
                ArrayList ids = alarmDatabaseHelper.getAllAlarmIds();
                intent.putExtra("ALARM_ID",ids.get(position).toString());
                startActivity(intent);
            }
        });
    }

}
