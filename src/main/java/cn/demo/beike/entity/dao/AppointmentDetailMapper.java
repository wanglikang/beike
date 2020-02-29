package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.AppointmentDetail;
import cn.demo.beike.entity.AppointmentDetailKey;
import org.apache.ibatis.annotations.Result;
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
            @Result(property = "personnumber",column = "personNumber"),
            @Result(property = "begintime",column = "beginTime"),
            @Result(property = "endtime",column = "endTime"),
            @Result(property = "number",column = "number"),
            @Result(property = "appointmentid",column = "appointmentID")
    })
    @Select("select * from appointmentDetail where appointmentId=#{id}")
    List<AppointmentDetail> getAppointmentByAppointmentId(String id);
}