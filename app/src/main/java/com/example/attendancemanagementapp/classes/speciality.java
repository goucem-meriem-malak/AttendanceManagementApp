package com.example.attendancemanagementapp.classes;

public class speciality {
    private String name, degree, name_major;

    public speciality() {
    }

    public speciality(String name, String degree, String name_major) {
        this.name = name;
        this.degree = degree;
        this.name_major = name_major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getName_major() {
        return name_major;
    }

    public void setName_major(String name_major) {
        this.name_major = name_major;
    }
}
