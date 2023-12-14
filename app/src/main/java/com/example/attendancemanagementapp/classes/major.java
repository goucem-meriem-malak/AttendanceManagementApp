package com.example.attendancemanagementapp.classes;

public class major {
    private String name, name_faculty;

    public major() {
    }

    public major(String name, String name_faculty) {
        this.name = name;
        this.name_faculty = name_faculty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_faculty() {
        return name_faculty;
    }

    public void setName_faculty(String name_faculty) {
        this.name_faculty = name_faculty;
    }
}
