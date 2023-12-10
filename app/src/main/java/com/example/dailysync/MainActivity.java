package com.example.dailysync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton logoutButton;
    ImageButton todoListButton;
    ImageButton notifButton;
    ImageButton journalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifButton = findViewById(R.id.notifButton);
        logoutButton = findViewById(R.id.logout_user);
        journalButton = findViewById(R.id.journalButton);

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
    }
}