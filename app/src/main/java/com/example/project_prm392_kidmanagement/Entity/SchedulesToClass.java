package com.example.project_prm392_kidmanagement.Entity;

public class SchedulesToClass {
    private String scheduleClassID;
    private Schedule scheduleId;
    private Class classId;

    public SchedulesToClass() {
    }

    public SchedulesToClass(String scheduleClassID, Schedule scheduleId, Class classId) {
        this.scheduleClassID = scheduleClassID;
        this.scheduleId = scheduleId;
        this.classId = classId;
    }

    public Schedule getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Schedule scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleClassID() {
        return scheduleClassID;
    }

    public void setScheduleClassID(String scheduleClassID) {
        this.scheduleClassID = scheduleClassID;
    }

    public Class getClassId() {
        return classId;
    }

    public void setClassId(Class classId) {
        this.classId = classId;
    }
}
