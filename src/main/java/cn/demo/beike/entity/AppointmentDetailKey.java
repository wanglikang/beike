package cn.demo.beike.entity;

public class AppointmentDetailKey {
    private Integer id;

    private Integer appointmentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return "AppointmentDetailKey{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                '}';
    }
}