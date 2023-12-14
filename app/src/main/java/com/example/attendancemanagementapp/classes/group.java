package com.example.attendancemanagementapp.classes;

import java.util.List;

public class group {
    private String id, group, idSpeciality;
    private List<String> listOfCourses, listOfStudents;

    public group() {
    }

    public group(String id, String group, String idSpeciality, List<String> listOfCourses, List<String> listOfStudents) {
        this.id = id;
        this.group = group;
        this.idSpeciality = idSpeciality;
        this.listOfCourses = listOfCourses;
        this.listOfStudents = listOfStudents;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIdSpeciality() {
        return idSpeciality;
    }

    public void setIdSpeciality(String idSpeciality) {
        this.idSpeciality = idSpeciality;
    }

    public List<String> getListOfCourses() {
        return listOfCourses;
    }

    public void setListOfCourses(List<String> listOfCourses) {
        this.listOfCourses = listOfCourses;
    }

    public List<String> getListOfStudents() {
        return listOfStudents;
    }

    public void setListOfStudents(List<String> listOfStudents) {
        this.listOfStudents = listOfStudents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
