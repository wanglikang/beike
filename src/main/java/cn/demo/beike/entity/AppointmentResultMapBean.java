package cn.demo.beike.entity;


import java.sql.Date;

/**
 * 数据库结果映射bean类
 * 用于获取可用的预约结果
 */
public class AppointmentResultMapBean {
    public int appointmentId;
    public String typeName;
    //    public int number;
    public Date date;
    public int availableSumNumber;

    public int getAvailableSumNumber() {
        return availableSumNumber;
    }

    public void setAvailableSumNumber(int availableSumNumber) {
        this.availableSumNumber = availableSumNumber;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AppointmentResultMapBean{" +
                "appointmentId=" + appointmentId +
                ", typeName='" + typeName + '\'' +
                ", date=" + date +
                ", availableSumNumber=" + availableSumNumber +
                '}';
    }
}