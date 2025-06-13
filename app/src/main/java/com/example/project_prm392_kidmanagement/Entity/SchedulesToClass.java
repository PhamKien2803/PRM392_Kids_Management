package com.example.project_prm392_kidmanagement.Entity;

public class SchedulesToClass {
    private int scheduleClassID;
    private int scheduleId;
    private int classId;

    public SchedulesToClass() {
    }

    public SchedulesToClass(int scheduleClassID, int scheduleId, int classId) {
        this.scheduleClassID = scheduleClassID;
        this.scheduleId = scheduleId;
        this.classId = classId;
    }

    public int getScheduleClassID() {
        return scheduleClassID;
    }

    public void setScheduleClassID(int scheduleClassID) {
        this.scheduleClassID = scheduleClassID;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "SchedulesToClass{" +
                "scheduleClassID=" + scheduleClassID +
                ", scheduleId=" + scheduleId +
                ", classId=" + classId +
                '}';
    }
}
