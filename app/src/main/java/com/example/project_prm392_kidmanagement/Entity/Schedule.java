package com.example.project_prm392_kidmanagement.Entity;


public class Schedule {
    private String scheduleId;
    private String activityName;
    private String timeStart;
    private String timeEnd;
    private String timeDate;

    public Schedule() {
    }

    public Schedule(String scheduleId, String activityName, String timeStart, String timeEnd, String timeDate) {
        this.scheduleId = scheduleId;
        this.activityName = activityName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.timeDate = timeDate;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", timeDate=" + timeDate +
                '}';
    }
}
