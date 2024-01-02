package com.example.attendancemanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {
    private Button menu, notification, profile;
    private TextView name, department, faculty, major, degree, speciality, group;
    private ImageView pic;
    private Button logout;
    private SessionManager sessionManager;
    private user user;
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        mAuth = FirebaseAuth.getInstance();

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);

        pic = findViewById(R.id.pic);

        name = findViewById(R.id.name);

        logout = findViewById(R.id.logout);

        name.setText(user.getFirst_name()+ " " + user.getLast_name());

        String profilePicturePath = sessionManager.getProfilePicturePath();

        Glide.with(this)
                .load(profilePicturePath)
                .apply(RequestOptions.circleCropTransform())
                .into(pic);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), specialities.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), notifications.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.clearSession();
                mAuth.signOut();

                Intent intent = new Intent(getApplicationContext(), launch_screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}