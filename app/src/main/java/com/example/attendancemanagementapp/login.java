package com.example.attendancemanagementapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.department;
import com.example.attendancemanagementapp.classes.faculty;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.major;
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.speciality;
import com.example.attendancemanagementapp.classes.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {
    private Button login;
    private TextView forget_password;
    private EditText email, password;
    private CheckBox remember;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SessionManager sessionManager;
    private user user;
   private department department;
    private faculty faculty;
    private major major;
    private speciality speciality;
    private group group;
    private course course;
    private attendance attendance;
    private notification notification;
    private List<department> listDepartment;
    private List<faculty> listFaculty;
    private List<major> listMajor;
    private List<speciality> listSpeciality;
    private List<group> listGroup;
    private List<course> listCourse;
    private List<attendance> listAttendance;
    private List<notification> listNotification;
    private List<user> listStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = new user();
        course = new course();
        group = new group();
        speciality = new speciality();
        major = new major();
        faculty = new faculty();
        department = new department();

        sessionManager = new SessionManager(getApplicationContext());

        login = findViewById(R.id.login);
        forget_password = findViewById(R.id.forget_password);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        remember = findViewById(R.id.remember);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    sessionManager.setLoggedIn(remember.isChecked());
                    loginUser(email.getText().toString(), password.getText().toString());
                } else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your E-mail is empty, please try again", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your password is empty, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, taskk -> {
                    if (taskk.isSuccessful()) {
                        String userId = mAuth.getUid();
                        db.collection("user").document(userId).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                user.setFirst_name(document.getString("first_name"));
                                                user.setLast_name(document.getString("last_name"));
                                                user.setUid(userId);

                                                sessionManager.saveUserDetails(user);
                                            }
                                        } else {
                                            Log.e("Firestore", "Error getting document", task.getException());
                                        }
                                    }
                                });

                        getCourses(userId);
                        getNotifications(userId);
                        downloadProfilePicture(userId);
                        Intent intent = new Intent(getApplicationContext(), courses.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    } else {
                        Log.e("Firestore", "Error getting courses", taskk.getException());
                    }
                });
    }
    private void getFaculties(List<String> listFacultyIds){
        listFaculty = new ArrayList<>();
        listDepartment = new ArrayList<>();
        List<String> listDepartmentIds = new ArrayList<>();
        db.collection("faculty").whereIn(FieldPath.documentId(), listFacultyIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                faculty faculty = new faculty();
                                faculty.setName(document.getId());
                                department.setName(document.getString("idDepartment"));
                                if (!listDepartmentIds.contains(document.getString("idDepartment"))){
                                    listDepartmentIds.add(document.getString("idDepartment"));
                                    listFaculty.add(faculty);
                                    listDepartment.add(department);
                                }
                            }
                            sessionManager.saveFaculties(listFaculty);
                            sessionManager.saveDepartments(listDepartment);
                        }
                    }
                });
    }
    private void getMajors(List<String> listOfMajorIds){
        listMajor = new ArrayList<>();
        List<String> listFacultyIds = new ArrayList<>();
        db.collection("major").whereIn(FieldPath.documentId(), listOfMajorIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                major major = new major();
                                major.setName(document.getId());
                                major.setName_faculty(document.getString("idFaculty"));
                                if (!listFacultyIds.contains(document.getString("idFaculty"))){
                                    listFacultyIds.add(document.getString("idFaculty"));
                                    listMajor.add(major);
                                }
                            }
                            sessionManager.saveMajors(listMajor);
                            getFaculties(listFacultyIds);
                        }
                    }
                });
    }
    private void getSpecialities(List<String> listOfSpecialityIds){
        listSpeciality = new ArrayList<>();
        List<String> listOfMajorIds = new ArrayList<>();
        db.collection("speciality").whereIn(FieldPath.documentId(), listOfSpecialityIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                speciality speciality = new speciality();
                                speciality.setName(document.getId());
                                speciality.setDegree(document.getString("degree"));
                                speciality.setName_major(document.getString("idMajor"));
                                if (!listOfMajorIds.contains(document.getString("idMajor"))){
                                    listOfMajorIds.add(document.getString("idMajor"));
                                    listSpeciality.add(speciality);
                                }
                            }
                            sessionManager.saveSpecialities(listSpeciality);

                            getMajors(listOfMajorIds);
                        }
                    }
                });
    }
    private void getGroups(List<String> courseIds){
        listGroup = new ArrayList<>();
        List<String> listOfSpecialityIds = new ArrayList<>();
        db.collection("group").whereArrayContainsAny("listOfCourses", courseIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                group group = new group();
                                group.setId(document.getId());
                                group.setGroup(document.getString("group"));
                                group.setIdSpeciality(document.getString("idSpeciality"));
                                group.setListOfStudents((List<String>) document.get("listOfStudents"));
                                group.setListOfCourses((List<String>) document.get("listOfCourses"));
                                getStudents(group.getListOfStudents());
                                if (!listOfSpecialityIds.contains(document.getString("idSpeciality"))){
                                    listOfSpecialityIds.add(document.getString("idSpeciality"));
                                    listGroup.add(group);
                                }
                            }
                            sessionManager.saveGroups(listGroup);

                            getSpecialities(listOfSpecialityIds);
                        }
                    }
                });
    }
    private void getStudents(List<String> listOfStudents) {
        listStudent = new ArrayList<>();
        db.collection("user").whereIn(FieldPath.documentId(), listOfStudents)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user user = new user();
                                user.setUid(document.getId());
                                user.setFirst_name(document.getString("first_name"));
                                user.setLast_name(document.getString("last_name"));
                                if (!listStudent.contains(user.getUid())){
                                    listStudent.add(user);
                                }
                            }
                            sessionManager.saveStudents(listStudent);
                        }
                    }
                });

    }
    private void getCourses(String userId){
        listCourse = new ArrayList<>();
        List<String> courseIds = new ArrayList<>();
        db.collection("course").whereEqualTo("idProf", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                course course = new course();  // Create a new course instance for each iteration
                                course.setId(document.getId());
                                course.setName(document.getString("name"));
                                if (!courseIds.contains(document.getId())){
                                    courseIds.add(document.getId());
                                    listCourse.add(course);
                                }
                            }
                            sessionManager.saveCourses(listCourse);
                            getGroups(courseIds);
                            getAttendances(courseIds, userId);
                        } else {
                            Log.e("Firestore", "Error getting groups", task.getException());
                        }
                    }
                });
    }
    private void getAttendances(List<String> courseIds, String userid){
        List<attendance> attendanceList = new ArrayList<>();

        db.collection("attendance").whereIn("idCourse", courseIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String idCourse = document.getString("idCourse");
                            Timestamp time = document.getTimestamp("time");
                            List<String> listOfPresence = (List<String>) document.get("listOfPresence");
                            attendance attendance = new attendance(id, idCourse, listOfPresence, time);
                            attendanceList.add(attendance);
                        }
                        sessionManager.saveAttendances(attendanceList);
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }
    private void getNotifications(String uid){
        List<notification> userNotifications = new ArrayList<>();

        db.collection("notification").whereEqualTo("idProfessor", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String title = document.getString("title");
                            String subject = document.getString("subject");
                            String idProfessor = document.getString("idProfessor");
                            String idCourse = document.getString("idCourse");
                            Timestamp time = document.getTimestamp("time");
                            String idJustification = document.getString("idJustification");

                            notification notification = new notification(id, title, subject, idProfessor, idJustification, idCourse, time);

                            userNotifications.add(notification);
                        }
                        sessionManager.saveNotifications(userNotifications);
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }
    private void downloadProfilePicture(String userId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userFolderRef = storageRef.child("user/" + userId);

        userFolderRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (!listResult.getItems().isEmpty()) {
                        StorageReference userPicRef = listResult.getItems().get(0);

                        File localFile = null;
                        try {
                            localFile = File.createTempFile("images", "png");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        File finalLocalFile = localFile;
                        userPicRef.getFile(localFile)
                                .addOnSuccessListener(taskSnapshot -> {
                                    String localFilePath = finalLocalFile.getAbsolutePath();
                                    sessionManager.saveProfilePicturePath(localFilePath);
                                })
                                .addOnFailureListener(exception -> {
                                    Log.e(TAG, "Error downloading profile picture: " + exception.getMessage());
                                });
                    } else {
                        Log.w(TAG, "No profile picture found for user: " + userId);
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle failure to list files
                    Log.e(TAG, "Error listing files in user's folder: " + exception.getMessage());
                });
    }
}