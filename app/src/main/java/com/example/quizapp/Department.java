package com.example.quizapp;

public class Department {
    private String deptId;
    private String deptName;
    private String imageUrl;

    public Department(String deptId, String deptName, String imageUrl) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.imageUrl = imageUrl;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
