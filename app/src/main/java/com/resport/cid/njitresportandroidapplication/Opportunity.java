package com.resport.cid.njitresportandroidapplication;

/**
 * Created by Rahul on 2/20/2018.
 */

public class Opportunity {
    String oppId;
    String oppName;
    String oppCollege;
    String candidateTitle;
    int numStudents;
    int weeklyHours;
    String description;
    String facultyName;
    String facultyUCID;
    String emailAddress;
    String category;
    String expiration;
    String tags[];

    public Opportunity(String oppId, String oppName, String oppCollege, String candidateTitle,
                       int numStudents, int weeklyHours, String description, String facultyName,
                       String facultyUCID, String emailAddress, String category, String expiration) {
        this.oppId = oppId;
        this.oppName = oppName;
        this.oppCollege = oppCollege;
        this.candidateTitle = candidateTitle;
        this.numStudents = numStudents;
        this.weeklyHours = weeklyHours;
        this.description = description;
        this.facultyName = facultyName;
        this.facultyUCID = facultyUCID;
        this.emailAddress = emailAddress;
        this.category = category;
        this.expiration = expiration;
        this.tags = null;
    }

    public Opportunity(String oppId, String oppName, String oppCollege, String candidateTitle,
                       int numStudents, int weeklyHours, String description, String facultyName,
                       String facultyUCID, String emailAddress, String category, String expiration, String tags[]) {
        this.oppId = oppId;
        this.oppName = oppName;
        this.oppCollege = oppCollege;
        this.candidateTitle = candidateTitle;
        this.numStudents = numStudents;
        this.weeklyHours = weeklyHours;
        this.description = description;
        this.facultyName = facultyName;
        this.facultyUCID = facultyUCID;
        this.emailAddress = emailAddress;
        this.category = category;
        this.expiration = expiration;
        this.tags = tags;
    }

    public String getOppId() {
        return oppId;
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

    public String getEmailAddress() { return emailAddress; }

    public String getFacultyUCID() { return facultyUCID; }

    public String getCategory() {
        return category;
    }

    public String getExpiration() { return expiration; }
}
