package cn.demo.beike.service;

import cn.demo.beike.entity.Appointment;
import cn.demo.beike.entity.AppointmentDetail;
import cn.demo.beike.entity.PersonalAppointment;
import cn.demo.beike.entity.dao.PersonalAppointmentMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
//    @Autowired
//    PersonalAppointmentMapper personalAppointmentMapper;
//    /**
//     *
//     * 2.判断可用库存
//     * 3.减库存
//     * 4.插入记录
//     * 5.根据剩余库存情况，更改appointmentdetail状态
//     */
//    public void makeAppointment(AppointmentDetail appointmentDetail,int personNumber){
//        PersonalAppointment newRecord = new PersonalAppointment();
//        newRecord.setAppointmentdetailId(appointmentDetail.getId());
//        newRecord.setPersonNumber(personNumber);
//        newRecord.setFlag("1");
//        newRecord.setTime(new java.sql.Date(System.currentTimeMillis()));
//        newRecord.setStatus(1);
//        newRecord.setAppointmentId(appointmentDetail.getAppointmentId());
//
//        int rows = personalAppointmentMapper.insertSelective(newRecord);
//        if (rows > 0) {
//            result.put("SUCCESS", true);
//            JSONArray arr = new JSONArray();
//            JSONObject ta = new JSONObject();
//            Appointment aaa = appointmentMapper.selectByPrimaryKey(appointmentDetail.getAppointmentId());
//            ta.put("typeID", aaa.getTypeId());
//            ta.put("appointmentID", appointmentDetail.getAppointmentId());
//            ta.put("appoinmentDetailID", appointmentDetailID);
//            ta.put("date", dateSdf.format(aaa.getDetailDate()));
//            ta.put("beginTime", timeSdf.format(appointmentDetail.getBeginTime()));
//            ta.put("endTime", timeSdf.format(appointmentDetail.getEndTime()));
//            ta.put("number", personNumber);
//
//            arr.add(ta);
//            result.put("INFO", arr);
//            System.out.println(result);
//        } else {
//            result.put("SUCCESS", false);
//            result.put("MSG", "插入失败");
//            logger.error(jsonObject.toJSONString());
//        }
//    }
}
