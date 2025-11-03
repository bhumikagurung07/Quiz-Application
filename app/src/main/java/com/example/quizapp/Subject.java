package com.example.quizapp;

public class Subject {
    private String subId;
    private String subName;
    private String deptId;
    private String techId;
    private String imageUrl;

    public Subject(String subId, String subName, String deptId, String techId, String imageUrl) {
        this.subId = subId;
        this.subName = subName;
        this.deptId = deptId;
        this.techId = techId;
        this.imageUrl = imageUrl;
    }

    public String getSubId() {
        return subId;
    }

    public String getSubName() {
        return subName;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getTechId() {
        return techId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
