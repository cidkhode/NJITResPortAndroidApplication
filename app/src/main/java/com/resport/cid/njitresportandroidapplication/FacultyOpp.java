package com.resport.cid.njitresportandroidapplication;

import org.json.JSONArray;

/**
 * Created by ritupatel on 4/5/18.
 */

public class FacultyOpp {
    String id;
    String name;
    String description;
    String position;
    JSONArray applicants;

    public FacultyOpp(String id, String name, String description, String position, JSONArray applicants)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.applicants = applicants;
    }

    public String getId() {return id;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPosition() {
        return position;
    }

    public JSONArray getApplicants() {return applicants;}
}
