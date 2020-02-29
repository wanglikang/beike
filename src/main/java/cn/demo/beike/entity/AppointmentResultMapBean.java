package cn.demo.beike.entity;


/**
 * 数据库结果映射bean类
 * 用于获取可用的预约结果
 */
public class AppointmentResultMapBean {
    public int appointmentId;
    public String typeName;
    public int number;
    public String date;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
