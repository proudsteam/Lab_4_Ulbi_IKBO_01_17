package com.example.lab_4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Calendar;

public class Widget extends AppWidgetProvider {

    final String LOG_TAG = "timlog";
   static AlarmManager am;
   static Intent intent1;
   static PendingIntent pIntent1;
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        SharedPreferences sp = context.getSharedPreferences(
                MainActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }

        //RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        //Intent configIntent = new Intent(context, MainActivity.class);
//
        //PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
//
        //remoteViews.setOnClickPendingIntent(R.id.ll, configPendingIntent);
        //appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }
    private static void restartNotify(Context context, int id) {
        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        intent1 = new Intent(context, Alarm.class);
        intent1.putExtra("id", id);
        pIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 17);
        calendar.set(Calendar.SECOND, 20);
        calendar.set(Calendar.MILLISECOND, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pIntent1);
    }
    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, int widgetID) {

        // Читаем параметры Preferences
        String widgetText = sp.getString(MainActivity.COUNT_DAY + widgetID, "");
        Log.d("timlog", sp.getString(MainActivity.COUNT_DAY+widgetID, "") + "КОЛИЧЕСТВО ДНЕЙ updateWidget");

        if (widgetText == null) return;
        Log.d("timlog", widgetID + " ID update");

        restartNotify(context, widgetID);
        // Настраиваем внешний вид виджета
        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        widgetView.setTextViewText(R.id.textView, widgetText + " дня осталось");

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

}
