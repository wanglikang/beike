package cn.demo.beike.entity.dao;

import cn.demo.beike.entity.AppointmentDetail;

public interface AppointmentDetailMapper {
    int insert(AppointmentDetail record);

    int insertSelective(AppointmentDetail record);
}