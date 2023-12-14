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
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class departments extends AppCompatActivity {
    private Button menu, profile, notification;
    private SessionManager sessionManager;
    private LinearLayout departmentContainer;
    private List<department> departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.departments);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);

        sessionManager = new SessionManager(getApplicationContext());

        getDepartments();

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

    private void getDepartments() {
        departments = sessionManager.getDepartments();
        departmentContainer = findViewById(R.id.list_departments);

        for (int i = 0; i < departments.size(); i++) {
            department currentDepartment = departments.get(i);
            View departmentView = LayoutInflater.from(this).inflate(R.layout.department, null);
            departmentView.setClickable(true);


            TextView TextView = departmentView.findViewById(R.id.department_name);

            TextView.setText(currentDepartment.getName());

            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), faculties.class);
                    intent.putExtra("idDepartment", currentDepartment.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            departmentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), faculties.class);
                    intent.putExtra("idDepartment", currentDepartment.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            departmentContainer.addView(departmentView);
        }
    }
}