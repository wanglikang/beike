package cn.demo.beike.entity;

import java.util.Date;

public class PersonalAppointment {
    private Integer id;

    private Integer personNumber;

    private Integer appointmentdetailId;

    private Integer appointmentId;
    private Date time;

    private String flag;
    private Date beginTime;
    private Date endTime;
    private String typeName;
    private int typeId;


    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    private Date gmtCreate;

    private Date gmtModified;

    private int status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public Integer getAppointmentdetailId() {
        return appointmentdetailId;
    }

    public void setAppointmentdetailId(Integer appointmentdetailId) {
        this.appointmentdetailId = appointmentdetailId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PersonalAppointment{" +
                "id=" + id +
                ", personNumber=" + personNumber +
                ", appointmentdetailId=" + appointmentdetailId +
                ", time=" + time +
                ", flag='" + flag + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", typeName='" + typeName + '\'' +
                ", typeId=" + typeId +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", status=" + status +
                '}';
    }
}