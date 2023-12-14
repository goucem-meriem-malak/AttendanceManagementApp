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
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class faculties extends AppCompatActivity {
    private Button menu, profile, notification;
    private TextView nameDepartment;
    private SessionManager sessionManager;
    private LinearLayout facultyContainer;
    private List<faculty> faculties;
    private String idDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculties);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);

        nameDepartment = findViewById(R.id.nameDepartment);

        sessionManager = new SessionManager(getApplicationContext());

        nameDepartment.setText(getIntent().getStringExtra("idDepartment"));

        getFaculties();

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

    private void getFaculties() {
        faculties = sessionManager.getFaculties();
        facultyContainer = findViewById(R.id.list_faculties);

        for (int i = 0; i < faculties.size(); i++) {
            faculty currentFaculty = faculties.get(i);
            View facultyView = LayoutInflater.from(this).inflate(R.layout.faculty, null);
            facultyView.setClickable(true);


            TextView TextView = facultyView.findViewById(R.id.faculty_name);

            TextView.setText(currentFaculty.getName());

            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), majors.class);
                    intent.putExtra("idFaculty", currentFaculty.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            facultyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), majors.class);
                    intent.putExtra("idFaculty", currentFaculty.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            facultyContainer.addView(facultyView);
        }
    }
}