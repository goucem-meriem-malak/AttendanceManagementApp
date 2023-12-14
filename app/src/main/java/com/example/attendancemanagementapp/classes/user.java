package com.example.attendancemanagementapp.classes;

import android.os.Parcelable;

import java.util.List;

public class user {
    private String uid, first_name, last_name, email;

    public user(String uid, String first_name, String last_name, String email) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public user() {
    }

    public <T extends Parcelable> user(Object user) {
    }

    public String getUid() {
        return uid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
