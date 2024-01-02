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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.attendancemanagementapp.Interfaces.CircularProgressView;
import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.user;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class attendances extends AppCompatActivity {
    private Button menu, notification, profile;
    private FloatingActionButton add;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar course_stat;
    private TextView nameCourse;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private SessionManager sessionManager;
    private user user;
    private List<attendance> attendances;
    private LinearLayout attendanceContainer;
    private String idCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendances);

        db = FirebaseFirestore.getInstance();

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        add = findViewById(R.id.add);
        nameCourse = findViewById(R.id.nameCourse);


        nameCourse.setText(getIntent().getStringExtra("nameCourse"));
        idCourse = getIntent().getStringExtra("idCourse");
        getAttendances(idCourse);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivity(idCourse);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), session.class);
                intent.putExtra("idCourse", idCourse);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void refreshActivity(String idCourse) {
        db.collection("attendance").whereEqualTo("idCourse", idCourse)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        attendances.clear();
                        attendanceContainer.removeAllViews();
                        for (DocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            Timestamp time = document.getTimestamp("time");
                            List<String> listOfPresence = (List<String>) document.get("listOfPresence");
                            attendance attendance = new attendance(id, idCourse, listOfPresence, time);
                            attendances.add(attendance);
                        }

                        List<attendance> oldAttendances = sessionManager.getAttendances();
                        List<attendance> keep = new ArrayList<>();
                        List<attendance> newAttendances = new ArrayList<>();
                        for (attendance attendance : oldAttendances){
                            if (!attendance.getIdCourse().equals(idCourse)){
                                keep.add(attendance);
                            }
                        }
                        newAttendances.addAll(attendances);
                        newAttendances.addAll(keep);
                        sessionManager.saveAttendances(newAttendances);
                        getAttendances(idCourse);
                    }
                });
    }

    private List<attendance> getAttendance(String idCourse) {
        List<attendance> attendances = sessionManager.getAttendances();
        List<attendance> listAttendances = new ArrayList<>();

        for (attendance attendance : attendances) {
            if (attendance.getIdCourse().equals(idCourse)) {
                listAttendances.add(attendance);
            }
        }
        return listAttendances;
    }

    private void getAttendances(String idCourse) {
        attendances = getAttendance(idCourse);
        attendanceContainer = findViewById(R.id.list_attendances);

        if (attendances != null) {
            for (int i = 0; i < attendances.size(); i++) {

                final attendance currentAttendance = attendances.get(i);
                View attendanceView = LayoutInflater.from(this).inflate(R.layout.attendance, null);

                RelativeLayout space = attendanceView.findViewById(R.id.space);
                TextView timeTextView = attendanceView.findViewById(R.id.time);
                TextView dateTextView = attendanceView.findViewById(R.id.date);
                CircularProgressView courseStatCircularProgress = attendanceView.findViewById(R.id.attendance_stat);

                Timestamp timestamp = currentAttendance.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                timeTextView.setText(timeFormat.format(timestamp.toDate()));
                dateTextView.setText(dateFormat.format(timestamp.toDate()));

                int progress = calculateProgress(idCourse, currentAttendance.getId());
                courseStatCircularProgress.setProgress(progress);

                space.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), session.class);
                        intent.putExtra("idAttendance", currentAttendance.getId());
                        intent.putExtra("idCourse", idCourse);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                timeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), session.class);
                        intent.putExtra("idAttendance", currentAttendance.getId());
                        intent.putExtra("idCourse", idCourse);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                dateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), session.class);
                        intent.putExtra("idAttendance", currentAttendance.getId());
                        intent.putExtra("idCourse", idCourse);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                courseStatCircularProgress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), session.class);
                        intent.putExtra("idAttendance", currentAttendance.getId());
                        intent.putExtra("idCourse", idCourse);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
                attendanceContainer.addView(attendanceView);
            }
        }
    }

    private int calculateProgress(String idCourse, String idAttendance) {
        List<group> groups = sessionManager.getGroups();
        group group = new group();
        attendance attendance = new attendance();
        List<String> presentStudents = new ArrayList<>();

        for (group thisGroup : groups){
            if (thisGroup.getListOfCourses().contains(idCourse)&&thisGroup.getListOfStudents().contains(user.getUid()));
            group = thisGroup;
        }

        for (attendance thisAttendance: attendances){
            if (thisAttendance.getId().equals(idAttendance)){
                attendance = thisAttendance;
            }
        }

        int total = 0, present = 0;

        if (attendance.getListOfPresence()!=null){
            for (String student : attendance.getListOfPresence()){
                present++;
            }
        }
        for (String student : group.getListOfStudents()){
            total++;
        }
        return (total == 0) ? 0 : ((present * 100) / total);
    }
}
