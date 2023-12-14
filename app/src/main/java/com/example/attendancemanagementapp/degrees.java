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

import com.example.attendancemanagementapp.classes.department;
import com.example.attendancemanagementapp.classes.faculty;
import com.example.attendancemanagementapp.classes.major;
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.speciality;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class degrees extends AppCompatActivity {
    private Button menu, profile, notification;
    private TextView nameMajor;
    private SessionManager sessionManager;
    private LinearLayout degreeContainer;
    private List<speciality> specialities;
    private String idMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.degrees);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        nameMajor = findViewById(R.id.nameMajor);

        idMajor = getIntent().getStringExtra("idMajor");
        nameMajor.setText(idMajor);
        sessionManager = new SessionManager(getApplicationContext());

        getDegrees();

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
                Intent intent = new Intent(getApplicationContext(), notification.class);
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

    private void getDegrees() {
        specialities = sessionManager.getSpecialities();
        degreeContainer = findViewById(R.id.list_degrees);

        for (int i = 0; i < specialities.size(); i++) {
            speciality currentSpeciality = specialities.get(i);
            View degreeView = LayoutInflater.from(this).inflate(R.layout.degree, null);
            degreeView.setClickable(true);


            TextView TextView = degreeView.findViewById(R.id.degree_name);

            TextView.setText(currentSpeciality.getDegree());

            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), specialities.class);
                    intent.putExtra("Degree", currentSpeciality.getDegree());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            degreeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), specialities.class);
                    intent.putExtra("Degree", currentSpeciality.getDegree());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            degreeContainer.addView(degreeView);
        }
    }
}