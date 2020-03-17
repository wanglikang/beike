package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.PersonalAppointment;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalAppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalAppointment record);

    int insertSelective(PersonalAppointment record);

    PersonalAppointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalAppointment record);

    int updateByPrimaryKey(PersonalAppointment record);

    @Select("select * from personal_appointment where person_number=#{personNumber} and appointmentdetail_id=#{appointmentdetailId} and `status` = 1")
    PersonalAppointment selectByPersonAndAppointmentdetailId(String personNumber,String appointmentdetailId);

    @Select("select * from personal_appointment where person_number=#{personNumber} and appointment_id=#{appointmentId}")
    PersonalAppointment selectIfHasAppointmented(String personNumber,String appointmentId);

    @Results(id="PersonalAppointmentResultMap",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "person_number",property = "personNumber"),
            @Result(column = "appointmentdetail_id",property = "appointmentdetailId"),
            @Result(column = "time",property = "time"),
            @Result(column = "begin_time",property = "beginTime"),
            @Result(column = "end_time",property = "endTime"),
            @Result(column = "type_id",property = "typeId"),
            @Result(column = "type_name",property = "typeName"),
    })
    @Select("select pa.*,ad.begin_time,ad.end_time , a.type_id,a.type_name\n" +
            "from personal_appointment  pa \n" +
            "left join appointmentdetail ad  \n" +
            "on pa.appointmentdetail_id=ad.id \n" +
            "left join appointment a\n" +
            "on a.id = ad.appointment_id\n"+
            "where pa.person_number = #{number} \n" +
            "and pa.status = 1 ")
    List<PersonalAppointment> selectAllAppointmentByPersonnumber(String number);
}