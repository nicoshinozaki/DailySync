package com.example.dailysync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton logoutButton;
    private ImageButton todoListButton;
    private ImageButton notifButton;
    private ImageButton journalButton;

    private Calendar calendar;
    private DateFormat dateFormat;
    private TextView currDate;
    private Date currentDate;
    private String currDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        currDateStr = dateFormat.format(currentDate);
        currDate = findViewById(R.id.currentDate);
        currDate.setText(currDateStr);

        notifButton = findViewById(R.id.notifButton);
        logoutButton = findViewById(R.id.logout_user);
        journalButton = findViewById(R.id.journalButton);
        todoListButton = findViewById(R.id.todoButton);

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        journalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JournalActivity.class));
            }
        });

        todoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodolistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}