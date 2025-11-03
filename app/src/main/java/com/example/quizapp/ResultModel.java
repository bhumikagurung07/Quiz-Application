package com.example.quizapp;

public class ResultModel {
    private String resultId, spid, deptName, subName, finalMark, timeConsumed;

    public ResultModel(String resultId, String spid, String deptName, String subName, String finalMark, String timeConsumed) {
        this.resultId = resultId;
        this.spid = spid;
        this.deptName = deptName;
        this.subName = subName;
        this.finalMark = finalMark;
        this.timeConsumed = timeConsumed;
    }

    public String getResultId() { return resultId; }
    public String getSpid() { return spid; }
    public String getDeptName() { return deptName; }
    public String getSubName() { return subName; }
    public String getFinalMark() { return finalMark; }
    public String getTimeConsumed() { return timeConsumed; }
}
