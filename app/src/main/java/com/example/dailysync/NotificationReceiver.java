package com.example.dailysync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("NotificationReceiver", "onReceive: Received broadcast");
            Log.d("Nicolas Shinozaki", "entered onReceive()");
            int requestCode = intent.getIntExtra("REQUEST_CODE", -1);
            Log.d("Nicolas Shinozaki", "requestCode = " + requestCode);
            switch (requestCode) {
                case 1:
                    sendNotification(context,"Reminder: Set Your Goals!", /*should be ToDoListActivity if worked properly*/NotificationActivity.class);
                    break;
                case 2:
                    sendNotification(context,"Reminder: Log Your Day!", /*should be ToDoListActivity if worked properly*/NotificationActivity.class);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e("NotificationReceiver", "onReceive: Exception", e);
        }

    }

    private void sendNotification(Context context, String message, Class<?> activityClass) {
        Log.d("Nicolas Shinozaki", "entered sendNotification");
        Intent nextActivity = new Intent(context, activityClass);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNotif = PendingIntent.getActivity(context, 1, nextActivity, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "myChannelId")
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("DailySync")
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntentNotif);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
        /*Intent notifActivity = new Intent(context, NotificationActivity.class);
        notifActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notifActivity);*/
        context.sendBroadcast(nextActivity);
    }
}