package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.Appointment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

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
}