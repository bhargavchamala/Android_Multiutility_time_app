package net.ddns.bhargav.calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRemainderActivity extends AppCompatActivity {

    TextView textView;
    EditText editTextTitle,editTextDescription;
    Button buttonCancel,buttonSave;
    String remainderId;
    int year,month,day;
    RemainderDatabaseHelper remainderDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remainder);
        textView = (TextView) findViewById(R.id.textViewDate);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonSave = (Button) findViewById(R.id.buttonRemainderSave);
        buttonCancel = (Button) findViewById(R.id.buttonRemainderCancel);
        remainderDatabaseHelper = new RemainderDatabaseHelper(getBaseContext());

        year =getIntent().getIntExtra("YEAR",1970);
        month=getIntent().getIntExtra("MONTH",1);
        day=getIntent().getIntExtra("DAY",1);
        String date = day+"/"+month+"/"+year;
        textView.setText(date);
        remainderId = getIntent().getStringExtra("REMAINDER_ID");
        if(remainderId!=null){
            Cursor res = remainderDatabaseHelper.getRemainderDetails(remainderId);
            res.moveToFirst();
            String textTitle = res.getString(res.getColumnIndex(RemainderDatabaseHelper.COLUMN_REMAINDER_NAME));
            String textDescription = res.getString(res.getColumnIndex(RemainderDatabaseHelper.COLUMN_REMAINDER_DESCRIPTION));
            editTextTitle.setText(textTitle);
            editTextDescription.setText(textDescription);
        }
        else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
            remainderId = dateFormat.format(cal.getTime());
            remainderDatabaseHelper.insertRemainder(remainderId,"My Remainder",1970,1,1,"description...");
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatabase();
                setRemainder();
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDatabase();
                cancelRemainder();
                finish();
            }
        });
    }

    void addToDatabase(){
        String textTitle="My Alarm";
        if(editTextTitle.getText().toString().trim().length()!=0)
            textTitle = editTextTitle.getText().toString().trim();
        String textDescription="My Alarm";
        if(editTextDescription.getText().toString().trim().length()!=0)
            textDescription = editTextDescription.getText().toString().trim();
        remainderDatabaseHelper.updateRemainder(remainderId,textTitle,year,month,day,textDescription);
    }

    void setRemainder(){
        String text="My Alarm";
        if(editTextTitle.getText().toString().trim().length()!=0)
            text = editTextTitle.getText().toString().trim();
        Intent intent = new Intent(getBaseContext(),RemainderReceiver.class);
        intent.putExtra("MESSAGE",text);
        PendingIntent pi = PendingIntent.getService(getBaseContext(),Integer.parseInt(remainderId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pi);
    }

    void deleteFromDatabase(){
        remainderDatabaseHelper.deleteRemainder(remainderId);
    }

    void cancelRemainder(){
        String text="My Alarm";
        if(editTextTitle.getText().toString().trim().length()!=0)
            text = editTextTitle.getText().toString().trim();
        Intent intent = new Intent(getBaseContext(),RemainderReceiver.class);
        intent.putExtra("MESSAGE",text);
        PendingIntent pi = PendingIntent.getService(getBaseContext(),Integer.parseInt(remainderId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }
}
