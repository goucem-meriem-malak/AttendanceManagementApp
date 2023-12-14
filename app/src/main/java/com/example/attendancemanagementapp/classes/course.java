package com.example.attendancemanagementapp.classes;

public class course {
    private String id, name;

    public course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public course() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
