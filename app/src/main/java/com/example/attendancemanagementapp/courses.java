package com.example.attendancemanagementapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemanagementapp.Interfaces.CircularProgressView;
import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


public class courses extends AppCompatActivity {
    private Button menu, notification, profile;
    private ProgressBar course_stat;
    private TextView nameGroup;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private SessionManager sessionManager;
    private user user;
    private List<course> courses;
    private List<attendance> attendances;
    private LinearLayout courseContainer;
    private String idGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        nameGroup = findViewById(R.id.nameGroup);
        //nameGroup.setText("Group 0"+getIntent().getStringExtra("nameGroup"));

        //idGroup = getIntent().getStringExtra("idGroup");
        getCourse();

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
    private List<course> getCourses(String idGroup){
        List<course> courses = sessionManager.getCourses();
        List<String> idCourses = new ArrayList<>();
        List<group> groups = sessionManager.getGroups();
        List<course> listCourses = new ArrayList<>();

        for (group group : groups){
            if (group.getId().equals(idGroup)){
                idCourses = group.getListOfCourses();
            }
        }

        for (String courseId : idCourses) {
            for (course course : courses) {
                if (courseId.equals(course.getId())) {
                    listCourses.add(course);
                    break;
                }
            }
        }
        return listCourses;
    }

    private void getCourse() {
        courses = sessionManager.getCourses();
        courseContainer = findViewById(R.id.list_courses);

        if (courses!=null){
            for (int i = 0; i < courses.size(); i++) {

                final course currentCourse = courses.get(i);
                View courseView = LayoutInflater.from(this).inflate(R.layout.course, null);

                TextView courseNameTextView = courseView.findViewById(R.id.course_name);

                courseNameTextView.setText(currentCourse.getName());

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
    private void fetchCourseDetails(List<DocumentReference> courseReferences) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (DocumentReference courseRef : courseReferences) {
            courseRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot courseDocument = task.getResult();
                    if (courseDocument.exists()) {
                        String courseId = courseDocument.getId();
                        String courseName = courseDocument.getString("name");
                        Toast.makeText(this, "Course ID: " + courseId + ", Name: " + courseName, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the error
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e(TAG, "Error getting course document: " + exception.getMessage());
                    }
                }
            });
        }
    }
}