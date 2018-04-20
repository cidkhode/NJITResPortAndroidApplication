package com.resport.cid.njitresportandroidapplication;

public class EditOpp {

    String id;
    String name;
    String description;
    String position;
    Integer num;
    Integer hours;
    Double minGPA;
    Integer clg;
    Integer category;
    String expiration;
    Integer expiryDate;
    String tags[];

    public EditOpp(String id, String name, String description, String position,
                   Integer num, Integer hours, Double minGPA, Integer clg,
                   Integer category, String expiration, Integer expiryDate, String[] tags)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.num = num;
        this.hours = hours;
        this.minGPA = minGPA;
        this.clg = clg;
        this.category = category;
        this.expiration = expiration;
        this.expiryDate = expiryDate;
        this.tags = tags;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getPosition() { return position; }

    public Integer getNum() { return num; }

    public Integer getHours() { return hours; }

    public Double getMinGPA() { return minGPA; }

    public Integer getCollege() { return clg; }

    public Integer getCategory() { return category; }

    public String getExpiration() { return expiration; }

    public Integer getExpiryDate() { return expiryDate; }

    public String[] getTags() { return tags; }

}
