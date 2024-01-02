package com.example.attendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class launch_screen extends AppCompatActivity {
    private SessionManager sessionManager;
    private Button get_started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        get_started = findViewById(R.id.get_started);
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), specialities.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
}