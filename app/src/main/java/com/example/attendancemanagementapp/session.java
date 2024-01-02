package com.example.attendancemanagementapp;

import static com.example.attendancemanagementapp.R.drawable.delete;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout = findViewById(R.id.refresh);

        db = FirebaseFirestore.getInstance();

        sessionManager = new SessionManager(getApplicationContext());

        listOfPresence = new ArrayList<>();
        attendance = new attendance();
        newSession = false;

        idAttendance = getIntent().getStringExtra("idAttendance");
        idCourse = getIntent().getStringExtra("idCourse");
        getCourseName(idCourse);

        if (idAttendance==null){
            newSession = true;
            attendance = createAttendance(null);
        } else {
            attendance = getAttendance(idAttendance);
        }

        getTimeNDate();

        getStudent(attendance);
        if (!checkEditable(attendance.getTime())){
            qr_generator.setBackgroundResource(delete);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActivity(idAttendance);
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
        qr_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEditable(attendance.getTime())){
                    generateQRCode(idAttendance);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(session.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this Session?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("attendance").document(idAttendance).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Session deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error deleting document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            finish();
                            List<attendance> all = sessionManager.getAttendances();
                            all.remove(attendance);
                            sessionManager.saveAttendances(all);
                        }

                    });


                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User clicked Cancel, do nothing
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void getTimeNDate() {
        Timestamp timestamp = attendance.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        time.setText(timeFormat.format(timestamp.toDate()));
        date.setText(dateFormat.format(timestamp.toDate()));
    }

    private attendance getAttendance(String idAttendance) {
        List<attendance> attendances = sessionManager.getAttendances();

        for (attendance thisAttendance : attendances){
            if (thisAttendance.getId().equals(idAttendance)){
                attendance.setTime(thisAttendance.getTime());
                attendance.setListOfPresence(thisAttendance.getListOfPresence());
                break;
            }
        }
        return attendance;
    }

    private void refreshActivity(String idAttendance){
        newSession = false;

        db.collection("attendance").document(idAttendance)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentContainer.removeAllViews();

                        attendance.setListOfPresence((List<String>) task.getResult().get("listOfPresence"));

                        List<attendance> newAttendances = sessionManager.getAttendances();
                        for (attendance currentAttendance : newAttendances){
                            if (currentAttendance.getId().equals(idAttendance)){
                                newAttendances.remove(currentAttendance);
                                break;
                            }
                        }
                        newAttendances.add(attendance);
                        sessionManager.saveAttendances(newAttendances);
                        getStudent(attendance);
                    }
                });
    }

    private attendance createAttendance(List<String> listOfPresence) {
        Map<String, Object> attendanceData = new HashMap<>();
        List<attendance> attendances = sessionManager.getAttendances();
        Map<String, Object> emptyArrayMap = new HashMap<>();
        DocumentReference attendanceRef = db.collection("attendance").document();

        idAttendance = attendanceRef.getId();

        attendance = new attendance();
        attendance.setId(attendanceRef.getId());
        attendance.setIdCourse(idCourse);
        attendance.setListOfPresence(listOfPresence);
        attendance.setTime(Timestamp.now());

        attendanceData.put("idCourse", attendance.getIdCourse());
        attendanceData.put("time", attendance.getTime());
        attendanceData.put("listOfPresence", emptyArrayMap);

        attendances.add(attendance);
        sessionManager.saveAttendances(attendances);
        attendanceRef.set(attendanceData)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
        return attendance;
    }
    private void updateAddAttendance(String idStudent, String idAttendance){
        List<String> newPresence = new ArrayList<>();
        if (attendance.getListOfPresence()==null){
            newPresence.add(idStudent);
            attendance.setListOfPresence(newPresence);
        } else {
            newPresence = attendance.getListOfPresence();
            newPresence.add(idStudent);
            attendance.setListOfPresence(newPresence);
        }
        db.collection("attendance").document(idAttendance).update("listOfPresence", FieldValue.arrayUnion(idStudent)).addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                });
    }
    private void updateRemoveAttendance(String idStudent, String idAttendance){
        List<String> newPresence = new ArrayList<>();
        if (attendance.getListOfPresence()!=null){
            newPresence = attendance.getListOfPresence();
            newPresence.remove(idStudent);
            attendance.setListOfPresence(newPresence);
        }
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
    private void getStudent(attendance attendance) {
        students = getStudents(idCourse);
        studentContainer = findViewById(R.id.list_students);

        if (students!=null&&attendance!=null) {
            for (int i = 0; i < students.size(); i++) {
                final user currentStudent = students.get(i);


                View attendanceView = LayoutInflater.from(this).inflate(R.layout.student, null);

                TextView nameTextView = attendanceView.findViewById(R.id.student_name);
                ImageView presentImageView = attendanceView.findViewById(R.id.stat_present);
                ImageView absentImageView = attendanceView.findViewById(R.id.stat_absent);

                nameTextView.setText(currentStudent.getLast_name() + " " + currentStudent.getFirst_name());
                presentImageView.setImageResource(R.drawable.right);
                absentImageView.setImageResource(R.drawable.wrong);

                if (!newSession && attendance.getListOfPresence()!=null) {
                    if (attendance.getListOfPresence().contains(currentStudent.getUid())) {
                        presentImageView.setColorFilter(Color.GREEN);
                        absentImageView.setColorFilter(Color.BLACK);
                    } else {
                        absentImageView.setColorFilter(Color.RED);
                        presentImageView.setColorFilter(Color.BLACK);
                    }
                } else {
                    absentImageView.setColorFilter(Color.RED);
                    presentImageView.setColorFilter(Color.BLACK);
                }

                if (!checkEditable(attendance.getTime())) {
                    presentImageView.setEnabled(false);
                    absentImageView.setEnabled(false);
                } else {
                    presentImageView.setEnabled(true);
                    absentImageView.setEnabled(true);
                }

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
                        if (listOfPresence.contains(currentStudent.getUid())) {
                            listOfPresence.remove(currentStudent.getUid());
                            updateRemoveAttendance(currentStudent.getUid(), idAttendance);
                        }
                    }
                });

            }
        }
    }

    private Boolean checkEditable(Timestamp time) {
        long currentTimeMillis = System.currentTimeMillis();
        long timestampMillis = time.toDate().getTime();

        long timeDifferenceMillis = currentTimeMillis - timestampMillis;

        if ((timeDifferenceMillis >= 24 * 60 * 60 * 1000)||(timeDifferenceMillis > 1.5 * 60 * 60 * 1000)) {
            return false;
        } else return true;
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