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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddAlarmActivity extends AppCompatActivity {
    TimePicker timePicker;
    Switch aSwitch;
    CheckBox checkBox;
    EditText editText;
    AlarmDatabaseHelper alarmDatabaseHelper;
    String alarmId;
    Button button,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        aSwitch = (Switch) findViewById(R.id.switchAlarm);
        checkBox = (CheckBox) findViewById(R.id.checkBoxAlarm);
        editText  =(EditText) findViewById(R.id.editTextAlarmName);
        button = (Button) findViewById(R.id.buttonAlarmOk);
        buttonCancel = (Button) findViewById(R.id.buttonAlarmCancel);

        Intent intent = getIntent();
        alarmDatabaseHelper = new AlarmDatabaseHelper(getBaseContext());
        alarmId = intent.getStringExtra("ALARM_ID");
        if(alarmId!=null){
            Cursor res = alarmDatabaseHelper.getAlarmDetails(alarmId);
            res.moveToFirst();
            int hour = res.getInt(res.getColumnIndex(AlarmDatabaseHelper.COLUMN_HOUR));
            int min = res.getInt(res.getColumnIndex(AlarmDatabaseHelper.COLUMN_MIN));
            String text = res.getString(res.getColumnIndex(AlarmDatabaseHelper.COLUMN_ALARM_NAME));
            int checkState = res.getInt(res.getColumnIndex(AlarmDatabaseHelper.COLUMN_EVERYDAY));
            int checkStateSwitch = res.getInt(res.getColumnIndex(AlarmDatabaseHelper.COLUMN_STATE));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);
            checkBox.setChecked(checkState == 1);
            aSwitch.setChecked(checkStateSwitch == 1);
            editText.setText(text);
        }
        else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddHHmmss");
            alarmId = dateFormat.format(cal.getTime());
            aSwitch.setChecked(true);
            alarmDatabaseHelper.insertAlarm(dateFormat.format(cal.getTime()),"My Alarm",timePicker.getCurrentHour(),timePicker.getCurrentMinute(),0,0);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatebase();
                setAlarm();
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDatabase();
                cancleAlarm();
                finish();
            }
        });

    }

    void deleteFromDatabase(){
        alarmDatabaseHelper.deleteAlarm(alarmId);
    }

    void cancleAlarm(){
        String text="My Alarm";
        if(editText.getText().toString().trim().length()!=0)
            text = editText.getText().toString().trim();
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        intent.putExtra("MESSAGE",text);
        PendingIntent pi = PendingIntent.getService(getBaseContext(),Integer.parseInt(alarmId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

    void setAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        String text="My Alarm";
        if(editText.getText().toString().trim().length()!=0)
            text = editText.getText().toString().trim();
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        intent.putExtra("MESSAGE",text);
        PendingIntent pi = PendingIntent.getService(getBaseContext(),Integer.parseInt(alarmId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        if(aSwitch.isChecked())
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-20*1000,AlarmManager.INTERVAL_DAY, pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-20*1000,pi);
        Calendar current = Calendar.getInstance();
        long difference = calendar.getTimeInMillis()-current.getTimeInMillis();
        String setTime = String.format("%d hrs, %d min",
                TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference));
        Toast.makeText(getBaseContext(),"Alarm Set "+setTime+" from now.",Toast.LENGTH_SHORT).show();
    }

    void addToDatebase(){
        int everyday = checkBox.isChecked()?1:0;
        int state = aSwitch.isChecked()?1:0;
        String text="My Alarm";
        if(editText.getText().toString().trim().length()!=0)
            text = editText.getText().toString().trim();
        alarmDatabaseHelper.updateAlarm(alarmId,text,timePicker.getCurrentHour(),timePicker.getCurrentMinute(),everyday,state);
    }
}
