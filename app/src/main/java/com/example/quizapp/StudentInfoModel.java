package com.example.quizapp;

public class StudentInfoModel {
    private String s_id, spid, password, name, phone_no, dob, dept_name;

    public StudentInfoModel(String s_id, String spid, String password, String name, String phone_no, String dob, String dept_name) {
        this.s_id = s_id;
        this.spid = spid;
        this.password = password;
        this.name = name;
        this.phone_no = phone_no;
        this.dob = dob;
        this.dept_name = dept_name;
    }

    public String getS_id() {
        return s_id;
    }

    public String getSpid() {
        return spid;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getDob() {
        return dob;
    }

    public String getDept_name() {
        return dept_name;
    }
}
