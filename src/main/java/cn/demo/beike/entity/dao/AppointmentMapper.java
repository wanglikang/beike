package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Appointment;
import cn.demo.beike.entity.AppointmentResultMapBean;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

}