package com.example.lab_4;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button button;
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    NotificationManager nm;
    AlarmManager am;
    Intent intent1;
    PendingIntent pIntent1;

    public final static String WIDGET_PREF = "widget_pref";
    public final static String DATE = "date";
    public final static String COUNT_DAY = "count";
    final String LOG_TAG = "timlog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        calendarView = findViewById(R.id.calendarView);

        Log.d(LOG_TAG, "onCreate config");

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);


       ButtonClick();
        calendarChangeDate();
    }

    public void calendarChangeDate() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month +=1;
                String monthD = month+"";
                String dayOfMonthD = dayOfMonth+"";
                if (month < 10) {
                    monthD = "0" + month;
                }
                if (dayOfMonth < 10) {
                    dayOfMonthD = "0" + dayOfMonth;
                }
                String date = dayOfMonthD +"." + monthD +"."+year;
                Log.d("timlog", date);

                SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(COUNT_DAY+widgetID,getDays(date));
                editor.apply();
                //restartNotify(MainActivity.this, widgetID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                Widget.updateWidget(MainActivity.this, appWidgetManager, sp, widgetID);


            }
        });
    }

    public String getDays(String date) {
        String dayCount = "";
        Date thisDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(thisDate);
        Log.d("timlog", currentDate);

        int d1 = Integer.parseInt(date.substring(0, 2));
        int m1 = Integer.parseInt(date.substring(3, 5));
        int y1 = Integer.parseInt(date.substring(6, 10));
        Log.d("timlog", d1 +"." + m1 +"."+y1);

        int d2 = Integer.parseInt(currentDate.substring(0, 2));
        int m2 = Integer.parseInt(currentDate.substring(3, 5));
        int y2 = Integer.parseInt(currentDate.substring(6, 10));
        Log.d("timlog", d2 +"." + m2 +"."+y2);

        if (y2 > y1) {
            return "Некорректная дата";
        }
        if(y2==y1)
        {
            if(m2 > m1) return "Некорректная дата";
            if (m2 == m1) {
                return String.valueOf(d1 - d2);
            }
            if (m2 < m1) {

                int monthDiff = (m1 - m2) * 30;
                monthDiff = monthDiff + (d1-d2);
                return String.valueOf(monthDiff);
            }
        }


        Log.d(LOG_TAG, currentDate);
        if (Integer.parseInt(currentDate) < 0) {
            return "Некорректная дата";
        }
        return dayCount;
    }

    public void ButtonClick() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                Log.d("timlog", sp.getString(DATE+widgetID, "") + "mm");


                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

    }
}
