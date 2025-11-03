package com.example.quizapp;
public class StudentModel {
    private String r_id;
    private String name;
    private String spid;

    public StudentModel(String r_id, String name, String spid) {
        this.r_id = r_id;
        this.name = name;
        this.spid = spid;
    }

    public String getR_id() {
        return r_id;
    }

    public String getName() {
        return name;
    }

    public String getSpid() {
        return spid;
    }
}
