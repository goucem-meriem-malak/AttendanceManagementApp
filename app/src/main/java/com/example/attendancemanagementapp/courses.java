package com.example.attendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.attendancemanagementapp.classes.course;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;


public class courses extends AppCompatActivity {
    private Button menu, notification, profile;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);

        sessionManager = new SessionManager(getApplicationContext());

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);

        getCourse();

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getApplicationContext(), courses.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), courses.class);
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
    private void getCourse() {
        List<course> courses = sessionManager.getCourses();
        LinearLayout courseContainer = findViewById(R.id.list_courses);

        if (courses !=null){
            for (int i = 0; i < courses.size(); i++) {

                final course currentCourse = courses.get(i);
                View courseView = LayoutInflater.from(this).inflate(R.layout.course, null);

                TextView courseNameTextView = courseView.findViewById(R.id.course_name);


                courseNameTextView.setText(currentCourse.getName());
                RelativeLayout space = courseView.findViewById(R.id.space);
                space.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), attendances.class);
                        intent.putExtra("idCourse", currentCourse.getId());
                        intent.putExtra("nameCourse", currentCourse.getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                courseNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), attendances.class);
                        intent.putExtra("idCourse", currentCourse.getId());
                        intent.putExtra("nameCourse", currentCourse.getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                courseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), attendances.class);
                        intent.putExtra("idCourse", currentCourse.getId());
                        intent.putExtra("nameCourse", currentCourse.getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                courseContainer.addView(courseView);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Toast.makeText(this, "scanned", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not scanned", Toast.LENGTH_SHORT).show();
            }
        }
    }
}