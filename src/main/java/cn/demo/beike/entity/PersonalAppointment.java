package cn.demo.beike.entity;

import java.util.Date;

public class PersonalAppointment {
    private Integer id;

    private Integer personnumber;

    private Integer appointmentdetailid;

    private Date time;

    private String flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonnumber() {
        return personnumber;
    }

    public void setPersonnumber(Integer personnumber) {
        this.personnumber = personnumber;
    }

    public Integer getAppointmentdetailid() {
        return appointmentdetailid;
    }

    public void setAppointmentdetailid(Integer appointmentdetailid) {
        this.appointmentdetailid = appointmentdetailid;
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
}