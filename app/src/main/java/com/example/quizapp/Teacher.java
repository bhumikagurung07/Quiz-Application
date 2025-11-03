package com.example.quizapp;

public class Teacher {
    private String techId;
    private String fullName;
    private String username;
    private String password;

    public Teacher(String techId, String fullName, String username, String password) {
        this.techId = techId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public String getTechId() {
        return techId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
