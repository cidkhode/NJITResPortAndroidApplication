package com.resport.cid.njitresportandroidapplication;

/**
 * Created by Rahul on 2/20/2018.
 */

public class Opportunity {
    String oppName;
    String oppCollege;
    String candidateTitle;
    int numStudents;
    int weeklyHours;
    String description;
    String facultyName;
    String facultyUCID;

    public Opportunity(String oppName, String oppCollege, String candidateTitle, int numStudents, int weeklyHours, String description, String facultyName, String facultyUCID) {
        this.oppName = oppName;
        this.oppCollege = oppCollege;
        this.candidateTitle = candidateTitle;
        this.numStudents = numStudents;
        this.weeklyHours = weeklyHours;
        this.description = description;
        this.facultyName = facultyName;
        this.facultyUCID = facultyUCID;
    }

    public String getOppName() {
        return oppName;
    }

    public String getOppCollege() {
        return oppCollege;
    }

    public String getCandidateTitle() {
        return candidateTitle;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public String getDescription() {
        return description;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyUCID() {
        return facultyUCID;
    }

    public void setOppName(String oppName) {
        this.oppName = oppName;
    }

    public void setOppCollege(String oppCollege) {
        this.oppCollege = oppCollege;
    }

    public void setCandidateTitle(String candidateTitle) {
        this.candidateTitle = candidateTitle;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setFacultyUCID(String facultyUCID) {
        this.facultyUCID = facultyUCID;
    }
}