package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Appointment;
import cn.demo.beike.entity.AppointmentResultMapBean;
import cn.demo.beike.entity.Type;
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


    /***
     * 根据条件查找所有符合条件的appointment
     * @return List<Appointment>
     */
    @ResultMap("BaseResultMap")
    @Select("<script>" +
            "select * " +
            "from appointment " +
            "<if test='orderType != null'>" +
            "where type_name=#{orderType} " +
            "</if>" +
            "<if test='detailDate != null'>" +
                "<if test='orderType != null'>" +
                "and detail_date=#{detailDate} " +
                "</if>" +
                "<if test='orderType == null'>" +
                "where detail_date=#{detailDate} " +
                "</if>" +
            "</if>" +
            "<if test='beginTime != null'>" +
                "<if test='orderType != null or detailDate != null'>" +
                "and begin_time=#{beginTime} " +
                "</if>" +
                "<if test='orderType == null and detailDate == null'>" +
                "where begin_time=#{beginTime} " +
                "</if>" +
            "</if>" +
            "<if test='endTime != null'>" +
                "<if test='orderType != null or detailDate != null or beginTime != null'>" +
                "and end_time=#{endTime} " +
                "</if>" +
                "<if test='orderType == null and detailDate == null and beginTime == null'>" +
                "where end_time=#{endTime} " +
                "</if>" +
            "</if>" +
            "<if test='limitTime != null'>" +
                "<if test='orderType != null or detailDate != null or beginTime != null or endTime != null'>" +
                "and limit_time=#{limitTime} " +
                "</if>" +
                "<if test='orderType == null and detailDate == null and beginTime == null and endTime == null'>" +
                "where limit_time=#{limitTime} " +
                "</if>" +
            "</if>" +
            "<if test='limitNum != null'>" +
                "<if test='orderType != null or detailDate != null or beginTime != null or endTime != null or limitTime != null'>" +
                "and limit_num=#{limitNum} " +
                "</if>" +
                "<if test='orderType == null and detailDate == null and beginTime == null and endTime == null and limitTime == null'>" +
                "where limit_num=#{limitNum} " +
                "</if>" +
            "</if>" +
            "<if test='flag != null'>" +
                "<if test='orderType != null or detailDate != null or beginTime != null or endTime != null or limitTime != null or limitNum != null'>" +
                "and flag=#{flag}" +
                "</if>" +
                "<if test='orderType == null and detailDate == null and beginTime == null and endTime == null and limitTime == null and limitNum == null'>" +
                "where flag=#{flag}" +
                "</if>" +
            "</if>"+
    "</script>")
    List<Appointment> selectAppointment(String orderType, Date detailDate, Date beginTime,
                                        Date endTime, Integer limitTime, Integer limitNum, String flag);
}