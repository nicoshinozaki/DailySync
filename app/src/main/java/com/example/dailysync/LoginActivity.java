package com.example.dailysync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private String txtEmail;
    private String txtPassword;
    private CheckBox rememberMe;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        rememberMe = findViewById(R.id.rememberMe);
        preferences = getPreferences(MODE_PRIVATE);

        // Check if the user has already logged in and should be remembered
        if (preferences.getBoolean("rememberMe", false)) {
            email.setText(preferences.getString("email", ""));
            password.setText(preferences.getString("password", ""));
            rememberMe.setChecked(true);
        }

    }

    public void login_user(View view) {
        txtEmail = email.getText().toString();
        txtPassword = password.getText().toString();

        mAuth.signInWithEmailAndPassword(txtEmail , txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                if (rememberMe.isChecked()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", txtEmail);
                    editor.putString("password", txtPassword);
                    editor.putBoolean("rememberMe", true);
                    editor.apply();
                }

                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
