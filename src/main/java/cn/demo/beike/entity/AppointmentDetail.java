package cn.demo.beike.entity;

import java.util.Date;

public class AppointmentDetail extends AppointmentDetailKey {
    private String appointmentName;

    private Integer personNumber;

    private String personName;

    private Date beginTime;

    private Date endTime;

    private Integer availableNumber;

    private Date gmtCreate;

    private Date gmtModified;

    private String status;

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName == null ? null : appointmentName.trim();
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Integer availableNumber) {
        this.availableNumber = availableNumber;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Override
    public String toString() {
        return "AppointmentDetail{" +
                super.toString()+"-"+
                "appointmentName='" + appointmentName + '\'' +
                ", personNumber=" + personNumber +
                ", personName='" + personName + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", availableNumber=" + availableNumber +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", status='" + status + '\'' +
                '}';
    }


}