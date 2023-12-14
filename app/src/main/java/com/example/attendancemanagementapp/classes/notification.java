package com.example.attendancemanagementapp.classes;

import com.google.firebase.Timestamp;

public class notification {
    private String id, title, subject, idUser, idJustification, idCourse;
    public Timestamp time;

    public notification() {
    }

    public notification(String id, String title, String subject, String idUser, String idJustification, String idCourse, Timestamp time) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.idUser = idUser;
        this.idJustification = idJustification;
        this.idCourse = idCourse;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getIdJustification() {
        return idJustification;
    }

    public void setIdJustification(String idJustification) {
        this.idJustification = idJustification;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }
}
