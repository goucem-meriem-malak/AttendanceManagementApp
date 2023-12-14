package com.example.attendancemanagementapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.user;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class session extends AppCompatActivity {
    private Button menu, notification, profile, qr_generator;
    private ImageView qr_generated;
    private TextView time, date, courseName;
    private SessionManager sessionManager;
    private List<user> students;
    private LinearLayout studentContainer;
    private Boolean newSession;
    private String idCourse, idAttendance;
    private FirebaseFirestore db;
    private List<String> listOfPresence;
    private attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students);

        menu = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        profile = findViewById(R.id.profile);
        qr_generator = findViewById(R.id.qr_generator);
        qr_generated = findViewById(R.id.qr_generated);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        courseName = findViewById(R.id.course_name);

        db = FirebaseFirestore.getInstance();

        time.setText(getCurrentTime());
        date.setText(getCurrentDate());

        sessionManager = new SessionManager(getApplicationContext());
        listOfPresence = new ArrayList<>();
        idAttendance = getIntent().getStringExtra("idAttendance");
        idCourse = getIntent().getStringExtra("idCourse");
        getCourseName(idCourse);

        if (getIntent().getStringExtra("idAttendance")==null){
            newSession = true;
            createAttendance(idCourse, null);
        }

        getStudent(idCourse);

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
        qr_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQRCode("UuaBypoLQxn2cwfUdTUI");
            }
        });
    }

    private void createAttendance(String idCourse, List<String> listOfPresence) {
        Map<String, Object> attendanceData = new HashMap<>();
        List<attendance> attendances = sessionManager.getAttendances();

        attendance = new attendance();
        attendance.setIdCourse(idCourse);
        attendance.setListOfPresence(listOfPresence);
        attendance.setTime(Timestamp.now());

        attendanceData.put("idCourse", idCourse);
        attendanceData.put("time", attendance.getTime());
        attendanceData.put("listOfPresence", listOfPresence);

        DocumentReference attendanceRef = db.collection("attendance").document(); // Auto-generated ID
        attendance.setId(attendanceRef.getId());
        idAttendance = attendanceRef.getId();

        attendances.add(attendance);
        sessionManager.saveAttendances(attendances);
        Toast.makeText(session.this, idAttendance, Toast.LENGTH_SHORT).show();
        attendanceRef.set(attendanceData)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    private void updateAddAttendance(String idStudent, String idAttendance){
        db.collection("attendance").document(idAttendance).update("listOfPresence", FieldValue.arrayUnion(idStudent)).addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                });
    }
    private void updateRemoveAttendance(String idStudent, String idAttendance){
        db.collection("attendance").document(idAttendance).update("listOfPresence", FieldValue.arrayRemove(idStudent)).addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                });
    }
    private void getCourseName(String idCourse) {
        List<course> courses = sessionManager.getCourses();
        for (course course : courses){
            if (course.getId().equals(idCourse)){
                courseName.setText(course.getName());
            }
        }
    }

    private List<user> getStudents(String idCourse) {
        List<String> studentIds = new ArrayList<>();
        List<group> groups = sessionManager.getGroups();

        for (group group : groups){
            if (group.getListOfCourses().contains(idCourse)){
                studentIds = group.getListOfStudents();
            }
        }

        students = new ArrayList<>();
        List<user> listStudents = sessionManager.getStudents();

        for (user otherStudent : listStudents){
            for (String theseStudents : studentIds){
                if (otherStudent.getUid().equals(theseStudents)){
                    students.add(otherStudent);
                }
            }
        }

        return  students;
    }

    private void getStudent(String idCourse) {
        students = getStudents(idCourse);
        studentContainer = findViewById(R.id.list_students);

        for (int i = 0; i < students.size(); i++) {
            final user currentStudent= students.get(i);

            if (students!=null) {

                View attendanceView = LayoutInflater.from(this).inflate(R.layout.student, null);

                TextView nameTextView = attendanceView.findViewById(R.id.student_name);
                ImageView presentImageView = attendanceView.findViewById(R.id.stat_present);
                ImageView absentImageView = attendanceView.findViewById(R.id.stat_absent);

                nameTextView.setText(currentStudent.getLast_name()+" "+currentStudent.getFirst_name());
                presentImageView.setImageResource(R.drawable.right);
                absentImageView.setImageResource(R.drawable.wrong);

                studentContainer.addView(attendanceView);

                if (i < students.size() - 1) {
                    View dividerView = new MaterialDivider(this);
                    studentContainer.addView(dividerView);

                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (int) 0.5);
                    dividerView.setLayoutParams(dividerParams);
                }
                presentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presentImageView.setColorFilter(Color.GREEN);
                        absentImageView.setColorFilter(Color.BLACK);
                        listOfPresence.add(currentStudent.getUid());
                        updateAddAttendance(currentStudent.getUid(), idAttendance);
                    }
                });
                absentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        absentImageView.setColorFilter(Color.RED);
                        presentImageView.setColorFilter(Color.BLACK);
                        if (listOfPresence.contains(currentStudent.getUid())){
                            listOfPresence.remove(currentStudent.getUid());
                            updateRemoveAttendance(currentStudent.getUid(), idAttendance);
                        }
                    }
                });
            }
        }
    }

    private void generateQRCode(String textToEncode) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            com.google.zxing.common.BitMatrix bitMatrix = multiFormatWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            qr_generated.setImageBitmap(bitmap);
            qr_generated.setVisibility(View.VISIBLE);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        return timeFormat.format(currentTime);
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        return dateFormat.format(currentDate);
    }
}