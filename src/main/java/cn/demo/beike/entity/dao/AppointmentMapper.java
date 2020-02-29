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
            @Result(property = "typeName",column = "typeName"),
            @Result(property = "number",column = "number"),

    })
    @Select("select a.* ,t.typeName,ad.`number` \n" +
            "from appointmentDetail ad left join  appointment a \n" +
            "on a.id = ad.appointmentID \n" +
            "left join `type` t \n" +
            "on  a.typeID=t.id\n" +
            "where t.id=#{typeId} and a.flag!='0' \n" +
            "and isnull(a.typeID)=0 ")
    List<AppointmentResultMapBean> selectAvailableAppointmentByTypeId(int typeId);

    @ResultMap( "availableAppointmentInfo")
    @Select("select a.* ,t.typeName,ad.`number` \n" +
            "from appointmentDetail ad left join  appointment a \n" +
            "on a.id = ad.appointmentID \n" +
            "left join `type` t \n" +
            "on  a.typeID=t.id\n" +
            "where a.flag!='0' \n" +
            "and isnull(a.typeID)=0")
    List<AppointmentResultMapBean> selectAllAvailableAppointment();

}