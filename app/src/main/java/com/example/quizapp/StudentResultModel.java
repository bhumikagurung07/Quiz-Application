package com.example.quizapp;

public class StudentResultModel {
    private String spid;
    private String name;
    private int mark;
    private int rank;
    private String subjectName;

    // Constructor
    public StudentResultModel(String spid, String name, int mark, int rank, String subjectName) {
        this.spid = spid;
        this.name = name;
        this.mark = mark;
        this.rank = rank;
        this.subjectName = subjectName;
    }

    // Getters
    public String getSpid() {
        return spid;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public int getRank() {
        return rank;
    }

    public String getSubjectName() {
        return subjectName;
    }

    // Setters
    public void setSpid(String spid) {
        this.spid = spid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
