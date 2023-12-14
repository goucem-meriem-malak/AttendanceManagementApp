package com.example.attendancemanagementapp.classes;

public class faculty {
    private String name, name_department;

    public faculty() {
    }

    public faculty(String name, String name_department) {
        this.name = name;
        this.name_department = name_department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_department() {
        return name_department;
    }

    public void setName_department(String name_department) {
        this.name_department = name_department;
    }
}
