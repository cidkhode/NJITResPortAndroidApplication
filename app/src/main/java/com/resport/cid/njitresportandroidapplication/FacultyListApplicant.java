package com.resport.cid.njitresportandroidapplication;

/**
 * Created by ritupatel on 4/5/18.
 */

public class FacultyListApplicant {
    String name;
    String major;
    Double gpa;
    String classStanding;
    String ucid;
    Integer appid;
    Integer status;
    String college;

    public FacultyListApplicant(String name, String major, Double gpa, String classStanding, String ucid, Integer appid, Integer status, String college)
    {
        this.name = name;
        this.major = major;
        this.gpa = gpa;
        this.classStanding = classStanding;
        this.ucid = ucid;
        this.appid = appid;
        this.status = status;
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public Double getGpa() {return gpa; }

    public String getClassStanding() { return classStanding; }

    public String getUcid() {return ucid; }

    public Integer getAppid() {return appid; }

    public Integer getStatus() {return status; }

    public String getCollege() { return college; }
}
