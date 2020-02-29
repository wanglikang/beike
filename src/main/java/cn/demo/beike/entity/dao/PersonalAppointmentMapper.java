package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.PersonalAppointment;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalAppointment record);

    int insertSelective(PersonalAppointment record);

    PersonalAppointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalAppointment record);

    int updateByPrimaryKey(PersonalAppointment record);
}