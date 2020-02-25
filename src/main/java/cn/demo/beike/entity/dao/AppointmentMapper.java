package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Appointment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Appointment record);

    /**
     * 返回自增的主键
     * @param typeid
     * @param createtime
     * @param detaildate
     * @param begintime
     * @param endtime
     * @param limitnum
     * @param flag
     * @param limittime
     *
     * @return id
     */
    @Results(id = "appointmentMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "typeid", column = "typeID"),
            @Result(property = "createtime", column = "createTime"),
            @Result(property = "detaildate", column = "detailDate"),
            @Result(property = "begintime", column = "beginTime"),
            @Result(property = "endtime", column = "endTime"),
            @Result(property = "limitnum", column = "limitNum"),
            @Result(property = "flag", column = "flag"),
            @Result(property = "limittime", column = "limitTime")
    })
    @Insert({"INSERT INTO appointment(typeID, createTime, detailDate, beginTime, endTime, limitNum, flag, limitTime) ",
            "VALUES (#{typeid},#{createtime},#{detaildate},#{begintime},#{endtime},#{limitnum},#{flag},#{limittime})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Integer typeid, Date createtime, Date detaildate, Date begintime, Date endtime,
                        Integer limitnum, String flag, Date limittime);

    Appointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);
}