package net.ddns.bhargav.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    Calendar calendar;
    int year,month,day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),AddRemainderActivity.class);
                intent.putExtra("YEAR",year);
                intent.putExtra("MONTH",month);
                intent.putExtra("DAY",day);
                startActivity(intent);
            }
        });
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        initCalendar();
    }

    private void initCalendar(){
        calendarView.setShowWeekNumber(false);
        calendar = Calendar.getInstance();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int y, int m, int d) {
                year=y;
                month=m;
                day=d;
                calendar = new GregorianCalendar(year,month,day);
            }
        });
    }


}
