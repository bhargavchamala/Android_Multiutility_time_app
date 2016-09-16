package net.ddns.bhargav.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class RemainderActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listViewRemainderList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH)+1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                Intent intent = new Intent(getBaseContext(),AddRemainderActivity.class);
                intent.putExtra("YEAR",year);
                intent.putExtra("MONTH",month);
                intent.putExtra("DAY",day);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        final RemainderDatabaseHelper remainderDatabaseHelper = new RemainderDatabaseHelper(getBaseContext());
        ArrayList remainderList = remainderDatabaseHelper.getAllRemainders();
        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.list_item,remainderList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),AddRemainderActivity.class);
                ArrayList ids = remainderDatabaseHelper.getAllRemainderIds();
                String remainderId = ids.get(position).toString();
                Cursor res = remainderDatabaseHelper.getRemainderDetails(remainderId);
                res.moveToFirst();
                intent.putExtra("REMAINDER_ID",remainderId);
                intent.putExtra("YEAR",res.getInt(res.getColumnIndex(RemainderDatabaseHelper.COLUMN_REMAINDER_YEAR)));
                intent.putExtra("MONTH",res.getInt(res.getColumnIndex(RemainderDatabaseHelper.COLUMN_REMAINDER_MONTH)));
                intent.putExtra("DAY",res.getInt(res.getColumnIndex(RemainderDatabaseHelper.COLUMN_REMAINDER_DAY)));
                startActivity(intent);
            }
        });
    }

}
