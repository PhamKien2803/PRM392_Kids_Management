package com.example.project_prm392_kidmanagement.Entity;

import java.util.Date;

public class Schedule {
    private String scheduleId;
    private String activityName;
    private Date timeStart;
    private Date timeEnd;
    private Date timeDate;

    public Schedule() {
    }

    public Schedule(String scheduleId, String activityName, Date timeStart, Date timeEnd, Date timeDate) {
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
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
