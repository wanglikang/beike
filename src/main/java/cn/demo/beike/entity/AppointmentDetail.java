package cn.demo.beike.entity;

import java.util.Date;

public class AppointmentDetail extends AppointmentDetailKey {
    private Integer personnumber;

    private Date begintime;

    private Date endtime;

    private Integer number;

    public Integer getPersonnumber() {
        return personnumber;
    }

    public void setPersonnumber(Integer personnumber) {
        this.personnumber = personnumber;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}