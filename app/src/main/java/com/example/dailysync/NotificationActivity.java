package com.example.dailysync;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailysync.databinding.ActivityNotificationBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.dailysync.databinding.ActivityNotificationBinding binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NotificationReceiver objReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.dailysync.ACTION_NOTIFICATION");
        registerReceiver(objReceiver, intentFilter);

        createNotificationChannel();

        selectTime(binding.selectTime1, 1);
        selectTime(binding.selectTime2, 2);
    }

    //Function to set notifications
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel("myChannelId", "channel_1", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("ECE 150 - Final Project");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    //Function to implement the selectTime buttons and the broadcast intent
    private void selectTime(Button button, int REQUEST_CODE) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timePicker = new MaterialTimePicker.Builder()
                        //.setSmallIcon(R.drawable.notif)
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Alarm Time")
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .build();
                timePicker.show(getSupportFragmentManager(), "tag1");
                timePicker
                        .addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    if (timePicker.getHour() > 12 && timePicker.getHour() < 24) {
                                        button.setText(String.format(Locale.getDefault(), "%02d:%02d PM", (timePicker.getHour() - 12), timePicker.getMinute()));
                                    } else if (timePicker.getHour() < 12 && timePicker.getHour() > 0) {
                                        button.setText(String.format(Locale.getDefault(), "%02d:%02d AM", timePicker.getHour(), timePicker.getMinute()));
                                    } else if (timePicker.getHour() == 12) {
                                        button.setText(String.format(Locale.getDefault(), "12:%02d PM", timePicker.getMinute()));
                                    } else {
                                        button.setText(String.format(Locale.getDefault(), "12:%02d AM", timePicker.getMinute()));
                                    }

                                    calendar = Calendar.getInstance();
                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MILLISECOND, 0);

                                    Intent intent = new Intent(NotificationActivity.this, NotificationReceiver.class);
                                    intent.setComponent(new ComponentName(NotificationActivity.this, NotificationReceiver.class));
                                    intent.setAction("com.example.dailysync.ACTION_NOTIFICATION");
                                    intent.putExtra("REQUEST_CODE", REQUEST_CODE);
                                    Log.d("Nicolas Shinozaki", "REQUEST_CODE = " + REQUEST_CODE);
                                    pendingIntent = PendingIntent.getBroadcast(NotificationActivity.this, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
                                    //pendingIntent.send(getApplicationContext(), REQUEST_CODE, intent);
                                    alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                    if (alarmManager != null) {
                                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                    } else {
                                        Log.e("Nicolas Shinozaki", "AlarmManager is null");
                                    }
                                    Toast.makeText(NotificationActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Nicolas Shinozaki", "Error");
                                }
                            }
                        });
            }
        });
    }

}