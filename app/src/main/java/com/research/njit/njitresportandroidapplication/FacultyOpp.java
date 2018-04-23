package com.research.njit.njitresportandroidapplication;

import org.json.JSONArray;

public class FacultyOpp {
    String id;
    String name;
    String description;
    String position;
    String expiration;
    JSONArray applicants;

    public FacultyOpp(String id, String name, String description, String position, String expiration, JSONArray applicants)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.expiration = expiration;
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

    public String getExpiration() {
        return expiration;
    }

    public JSONArray getApplicants() {return applicants;}
}
