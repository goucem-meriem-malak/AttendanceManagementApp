package com.example.attendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.department;
import com.example.attendancemanagementapp.classes.faculty;
import com.example.attendancemanagementapp.classes.major;
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class majors extends AppCompatActivity {
    private Button menu, profile, notification;
    private TextView nameFaculty;
    private SessionManager sessionManager;
    private LinearLayout majorContainer;
    private List<major> majors;
    private String idFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.majors);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        nameFaculty = findViewById(R.id.nameFaculty);
        idFaculty = getIntent().getStringExtra("idFaculty");

        sessionManager = new SessionManager(getApplicationContext());

        nameFaculty.setText(idFaculty);
        getMajors();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), departments.class);
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
    }

    private void getMajors() {
        majors = sessionManager.getMajors();
        majorContainer = findViewById(R.id.list_majors);

        for (int i = 0; i < majors.size(); i++) {
            major currentMajor = majors.get(i);
            View majorView = LayoutInflater.from(this).inflate(R.layout.major, null);
            majorView.setClickable(true);


            TextView TextView = majorView.findViewById(R.id.major_name);

            TextView.setText(currentMajor.getName());

            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), degrees.class);
                    intent.putExtra("idMajor", currentMajor.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            majorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), degrees.class);
                    intent.putExtra("idMajor", currentMajor.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            majorContainer.addView(majorView);
        }
    }
}