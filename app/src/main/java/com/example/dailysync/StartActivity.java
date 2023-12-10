package com.example.dailysync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void login_button(View view) {
        startActivity(new Intent(this , LoginActivity.class));
    }
    public void register_button(View view) {startActivity(new Intent(this , RegisterActivity.class));}
}
