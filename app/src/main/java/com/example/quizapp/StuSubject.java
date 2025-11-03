package com.example.quizapp;

public class StuSubject {
    private String subjectId;
    private String subjectName;
    private String base64Image;

    public StuSubject(String subjectId, String subjectName, String base64Image) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.base64Image = base64Image;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getBase64Image() {
        return base64Image;
    }
}

