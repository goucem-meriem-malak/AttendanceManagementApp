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
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.major;
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.speciality;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class groups extends AppCompatActivity {
    private Button menu, profile, notification;
    private TextView nameSpeciality;
    private SessionManager sessionManager;
    private LinearLayout groupContainer;
    private List<group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        nameSpeciality = findViewById(R.id.nameSpeciality);
        nameSpeciality.setText(getIntent().getStringExtra("idSpeciality"));

        sessionManager = new SessionManager(getApplicationContext());

        getGroups();

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

    private void getGroups() {
        groups = sessionManager.getGroups();
        groupContainer = findViewById(R.id.list_groups);

        for (int i = 0; i < groups.size(); i++) {
            group currentGroup = groups.get(i);
            View groupView = LayoutInflater.from(this).inflate(R.layout.group, null);
            groupView.setClickable(true);


            TextView TextView = groupView.findViewById(R.id.group_nbr);

            TextView.setText("Group" + currentGroup.getGroup());

            TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), courses.class);
                    intent.putExtra("idGroup", currentGroup.getId());
                    intent.putExtra("nameGroup", currentGroup.getGroup());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            groupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), courses.class);
                    intent.putExtra("idGroup", currentGroup.getId());
                    intent.putExtra("nameGroup", currentGroup.getGroup());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            groupContainer.addView(groupView);
        }
    }
}