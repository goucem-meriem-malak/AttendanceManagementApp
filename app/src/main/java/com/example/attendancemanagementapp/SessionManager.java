package com.example.attendancemanagementapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.attendancemanagementapp.classes.attendance;
import com.example.attendancemanagementapp.classes.course;
import com.example.attendancemanagementapp.classes.department;
import com.example.attendancemanagementapp.classes.faculty;
import com.example.attendancemanagementapp.classes.group;
import com.example.attendancemanagementapp.classes.major;
import com.example.attendancemanagementapp.classes.notification;
import com.example.attendancemanagementapp.classes.speciality;
import com.example.attendancemanagementapp.classes.user;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DEPARTMENTS = "departments";
    private static final String KEY_FACULTIES = "faculties";
    private static final String KEY_MAJORS = "majors";
    private static final String KEY_SPECIALITIES = "specialities";
    private static final String KEY_GROUPS = "groups";
    private static final String KEY_COURSES = "courses";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_ATTENDANCES = "attendances";
    private static final String KEY_STUDENTS = "students";
    private static final String KEY_PROFILE_PICTURE_PATH = "profile_pic_path";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveUserDetails(user user) {
        editor.putString(KEY_FIRST_NAME, user.getFirst_name());
        editor.putString(KEY_LAST_NAME, user.getLast_name());
        editor.putString(KEY_EMAIL, user.getEmail());

        editor.apply();
    }
    public user getUserDetails() {
        user user = new user();
        user.setFirst_name(pref.getString(KEY_FIRST_NAME, ""));
        user.setLast_name(pref.getString(KEY_LAST_NAME, ""));
        user.setEmail(pref.getString(KEY_EMAIL, ""));

        return user;
    }

    public void saveDepartments(List<department> departments) {
        Gson gson = new Gson();
        String departmentsJson = gson.toJson(departments);
        editor.putString(KEY_DEPARTMENTS, departmentsJson);
        editor.apply();
    }
    public List<department> getDepartments() {
        String departmentJson = pref.getString(KEY_DEPARTMENTS, "");

        if (!departmentJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<department>>() {}.getType();
            return gson.fromJson(departmentJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveFaculties(List<faculty> faculties) {
        Gson gson = new Gson();
        String facultiesJson = gson.toJson(faculties);
        editor.putString(KEY_FACULTIES, facultiesJson);
        editor.apply();
    }
    public List<faculty> getFaculties() {
        String facultyJson = pref.getString(KEY_FACULTIES, "");

        if (!facultyJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<faculty>>() {}.getType();
            return gson.fromJson(facultyJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveMajors(List<major> majors) {
        Gson gson = new Gson();
        String majorsJson = gson.toJson(majors);
        editor.putString(KEY_MAJORS, majorsJson);
        editor.apply();
    }
    public List<major> getMajors() {
        String majorJson = pref.getString(KEY_MAJORS, "");

        if (!majorJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<major>>() {}.getType();
            return gson.fromJson(majorJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveSpecialities(List<speciality> specialities) {
        Gson gson = new Gson();
        String specialitiesJson = gson.toJson(specialities);
        editor.putString(KEY_SPECIALITIES, specialitiesJson);
        editor.apply();
    }
    public List<speciality> getSpecialities() {
        String specialityJson = pref.getString(KEY_SPECIALITIES, "");

        if (!specialityJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<speciality>>() {}.getType();
            return gson.fromJson(specialityJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveGroups(List<group> groups) {
        Gson gson = new Gson();
        String groupsJson = gson.toJson(groups);
        editor.putString(KEY_GROUPS, groupsJson);
        editor.apply();
    }
    public List<group> getGroups() {
        String groupsJson = pref.getString(KEY_GROUPS, "");

        if (!groupsJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<group>>() {}.getType();
            return gson.fromJson(groupsJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveCourses(List<course> courses) {
        Gson gson = new Gson();
        String coursesJson = gson.toJson(courses);
        editor.putString(KEY_COURSES, coursesJson);
        editor.apply();
    }
    public List<course> getCourses() {
        String coursesJson = pref.getString(KEY_COURSES, "");

        if (!coursesJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<course>>() {}.getType();
            return gson.fromJson(coursesJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveAttendances(List<attendance> attendances) {
        Gson gson = new Gson();
        String attendancesJson = gson.toJson(attendances);
        editor.putString(KEY_ATTENDANCES, attendancesJson);
        editor.apply();
    }
    public List<attendance> getAttendances() {
        String attendancesJson = pref.getString(KEY_ATTENDANCES, "");

        if (!attendancesJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<attendance>>() {}.getType();
            return gson.fromJson(attendancesJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveNotifications(List<notification> notifications) {
        Gson gson = new Gson();
        String notificationsJson = gson.toJson(notifications);
        editor.putString(KEY_NOTIFICATIONS, notificationsJson);
        editor.apply();
    }
    public List<notification> getNotifications() {
        String notificationsJson = pref.getString(KEY_NOTIFICATIONS, "");

        if (!notificationsJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<notification>>() {}.getType();
            return gson.fromJson(notificationsJson, listType);
        }

        return new ArrayList<>();
    }
    public void saveStudents(List<user> students) {
        Gson gson = new Gson();
        String studentsJson = gson.toJson(students);
        editor.putString(KEY_STUDENTS, studentsJson);
        editor.apply();
    }
    public List<user> getStudents() {
        String studentsJson = pref.getString(KEY_STUDENTS, "");

        if (!studentsJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<user>>() {}.getType();
            return gson.fromJson(studentsJson, listType);
        }

        return new ArrayList<>();
    }

    public void saveProfilePicturePath(String profilePicturePath) {
        editor.putString(KEY_PROFILE_PICTURE_PATH, profilePicturePath);
        editor.apply();
    }
    public String getProfilePicturePath() {
        return pref.getString(KEY_PROFILE_PICTURE_PATH, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}

