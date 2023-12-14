package com.example.attendancemanagementapp.classes;

import com.google.firebase.Timestamp;

import java.util.List;

public class attendance {
    private String id, idCourse;
    private List<String> listOfPresence;
    private Timestamp time;

    public attendance() {
    }

    public attendance(String id, String idCourse, List<String> listOfPresence, Timestamp time) {
        this.id = id;
        this.idCourse = idCourse;
        this.listOfPresence = listOfPresence;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public List<String> getListOfPresence() {
        return listOfPresence;
    }

    public void setListOfPresence(List<String> listOfPresence) {
        this.listOfPresence = listOfPresence;
    }
}
