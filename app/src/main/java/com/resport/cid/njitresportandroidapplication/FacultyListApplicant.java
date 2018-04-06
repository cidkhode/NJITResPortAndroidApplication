package com.resport.cid.njitresportandroidapplication;

/**
 * Created by ritupatel on 4/5/18.
 */

public class FacultyListApplicant {
    String name;
    String major;
    Double gpa;
    String class1;
    String ucid;

    public FacultyListApplicant(String name, String major, Double gpa, String class1, String ucid)
    {
        this.name = name;
        this.major = major;
        this.gpa = gpa;
        this.class1 = class1;
        this.ucid = ucid;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public Double getGpa() {return gpa;}

    public String getClass1() {return class1;}

    public String getUcid() {return ucid;}
}
