package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Appointment;
import cn.demo.beike.entity.AppointmentResultMapBean;
import cn.demo.beike.entity.StaticBean;
import cn.demo.beike.entity.Type;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Appointment record);

    /**
     * 返回自增的主键
     * @param record
     *
     * @return id
     */
    int insertSelective(Appointment record);

    Appointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);

    @Results(id = "availableAppointmentInfo", value = {
            @Result(property = "appointmentId",column = "id"),
            @Result(property = "typeName",column = "type_name"),
            @Result(property = "availableSumNumber",column = "available_sum_number"),
            @Result(property = "date",column = "detail_date"),
    })
    @Select("select a.id, a.detail_date,t.type_name, sum(ad.`available_number`) as 'available_sum_number' \n" +
            "from appointmentdetail ad left join  appointment a \n" +
            "on a.id = ad.appointment_id \n" +
            "left join `type` t \n" +
            "on  a.type_id=t.id\n" +
            "where t.id=#{typeId} and a.flag!='0' \n" +
            "and isnull(a.type_id)=0 \n" +
            "group by a.id ")
    List<AppointmentResultMapBean> selectAvailableAppointmentByTypeId(int typeId);

//    @ResultMap( "availableAppointmentInfo")
    @Select("select a.* ,t.type_name,ad.`available_number` \n" +
            "from appointmentdetail ad left join  appointment a \n" +
            "on a.id = ad.appointment_id \n" +
            "left join `type` t \n" +
            "on  a.type_id=t.id\n" +
            "where a.flag!='0' \n" +
            "and isnull(a.type_id)=0")
    List<AppointmentResultMapBean> selectAllAvailableAppointment();


    /***
     * 根据条件查找所有符合条件的appointment
     * @return List<Appointment>
     */
    @ResultMap("BaseResultMap")
    @Select("<script>" +
            "select * " +
            "from appointment " +
            "<if test='typeName != null'>" +
            "where type_name=#{typeName} " +
            "</if>" +
            "<if test='detailDate != null'>" +
            "<if test='typeName != null'>" +
            "and detail_date=#{detailDate} " +
            "</if>" +
            "<if test='typeName == null'>" +
            "where detail_date=#{detailDate} " +
            "</if>" +
            "</if>"+
            "</script>")
    List<Appointment> selectAppointment(String typeName, Date detailDate);

//    @ResultMap("BaseResultMap")
    @Results(id="staticBeanMap",value = {
            @Result(column = "typeName",property = "typeName"),
            @Result(column = "detailDate",property = "detailDate"),
            @Result(column = "beginTime",property = "beginTime"),
            @Result(column = "endTime",property = "endTime"),
            @Result(column = "limitNum",property = "limitNum"),
            @Result(column = "number",property = "number"),
    })
    @Select("SELECT appointment.type_name AS typeName, " +
            "appointment.detail_date AS detailDate, " +
            "appointment.begin_time AS beginTime, " +
            "appointment.end_time AS endTime, " +
            "SUM(appointment.limit_num) AS limitNum, " +
            "SUM(appointment.limit_num - appointmentdetail.available_number) AS number " +
            "FROM appointment " +
            "RIGHT JOIN appointmentdetail " +
            "ON appointment.id = appointmentdetail.appointment_id " +
            "GROUP BY appointment.type_name, appointment.detail_date, appointment.begin_time, appointment.end_time")
    public List<StaticBean> selectAppointmentStatostics();
}