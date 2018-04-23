package com.research.njit.njitresportandroidapplication;

/**
 * Created by Rahul on 3/8/2018.
 */

public class Application  {
    String id;
    String name;
    String description;
    String position;
    String facultyName;
    String email;
    long timestamp;
    int status;
    String college;
    String ucid;
    String category;

    public Application(String id, String name, String description, String position, String facultyName, String email, long timestamp, int status, String college, String ucid, String category)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.facultyName = facultyName;
        this.email = email;
        this.timestamp = timestamp;
        this.status = status;
        this.college = college;
        this.ucid = ucid;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPosition() {
        return position;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getEmail() {
        return email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getCollege() { return college; }

    public String getUCID() { return ucid; }

    public String getCategory() { return category; }
}
