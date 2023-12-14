package com.example.attendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class launch_screen extends AppCompatActivity {
    private Button get_started;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private user user;
    private FirebaseFirestore db;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        get_started = findViewById(R.id.get_started);
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), courses.class);
            startActivity(intent);
            finish();
        }
        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager = new SessionManager(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
}