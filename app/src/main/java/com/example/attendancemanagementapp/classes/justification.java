package com.example.attendancemanagementapp.classes;

import java.sql.Timestamp;

public class justification {
    private String id, idAttendance, idAdministrator;
    private Timestamp time;
    private Boolean stat;

    public justification() {
    }

    public justification(String id, String idAttendance, String idAdministrator, Timestamp time, Boolean stat) {
        this.id = id;
        this.idAttendance = idAttendance;
        this.idAdministrator = idAdministrator;
        this.time = time;
        this.stat = stat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAttendance() {
        return idAttendance;
    }

    public void setIdAttendance(String idAttendance) {
        this.idAttendance = idAttendance;
    }

    public String getIdAdministrator() {
        return idAdministrator;
    }

    public void setIdAdministrator(String idAdministrator) {
        this.idAdministrator = idAdministrator;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Boolean getStat() {
        return stat;
    }

    public void setStat(Boolean stat) {
        this.stat = stat;
    }
}
