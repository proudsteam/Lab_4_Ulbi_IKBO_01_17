package com.example.lab_4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Map;

public class Alarm extends BroadcastReceiver {
    SharedPreferences sp;
    int widgetId;
    int countDay;
    @Override
    public void onReceive(Context context, Intent intent) {
        final String LOG_TAG = "timlog";

        SharedPreferences sp = context.getSharedPreferences(MainActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        widgetId = intent.getIntExtra("id", 0);
        countDay = Integer.parseInt(sp.getString(MainActivity.COUNT_DAY + widgetId, ""));
        AppWidgetManager awp = AppWidgetManager.getInstance(context);
        Log.d(LOG_TAG, countDay + " count alarm");
        Log.d(LOG_TAG, widgetId + " id alarm");
        SharedPreferences.Editor editor = sp.edit();

        countDay--;
        if (countDay == 0) {
            Log.d(LOG_TAG, "СРАБОТАЛО");
            editor.putString(MainActivity.COUNT_DAY+widgetId,countDay+"");
            editor.apply();
            Widget.updateWidget(context,awp,sp,widgetId);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Title")
                            .setContentText("ВРЕМЯ ВЫШЛО!");

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        } else {
            editor.putString(MainActivity.COUNT_DAY+widgetId,countDay+"");
            Log.d(LOG_TAG, "уменьшило" +countDay);
            editor.apply();
            Widget.updateWidget(context,awp,sp,widgetId);
        }


    }
}
