package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.AppointmentDetail;
import cn.demo.beike.entity.AppointmentDetailKey;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentDetailMapper {
    int deleteByPrimaryKey(AppointmentDetailKey key);

    int insert(AppointmentDetail record);

    int insertSelective(AppointmentDetail record);

    AppointmentDetail selectByPrimaryKey(AppointmentDetailKey key);

    int updateByPrimaryKeySelective(AppointmentDetail record);

    int updateByPrimaryKey(AppointmentDetail record);

    @Results(id="AppointmentDetailResultMap",value = {
            @Result(property = "id",column = "id",id = true),
            @Result(property = "personNumber",column = "person_number"),
            @Result(property = "beginTime",column = "begin_time"),
            @Result(property = "endTime",column = "end_time"),
            @Result(property = "availableNumber",column = "available_number"),
            @Result(property = "appointmentId",column = "appointment_id")
    })
    @Select("select * from appointmentdetail where appointment_id=#{appointmentId}")
    List<AppointmentDetail> getAppointmentDetailsByAppointmentId(String appointmentId);


    @ResultMap("AppointmentDetailResultMap")
    @Select("select * from appointmentdetail where id=#{appointmentdetailId}")
    AppointmentDetail getAppointmentDetailById(String appointmentdetailId);
}