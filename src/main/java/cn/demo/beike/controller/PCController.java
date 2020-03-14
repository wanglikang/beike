package cn.demo.beike.controller;

import cn.demo.beike.entity.Appointment;
import cn.demo.beike.entity.AppointmentDetail;
import cn.demo.beike.entity.Root;
import cn.demo.beike.entity.Type;
import cn.demo.beike.entity.dao.AppointmentDetailMapper;
import cn.demo.beike.entity.dao.AppointmentMapper;
import cn.demo.beike.entity.dao.RootMapper;
import cn.demo.beike.entity.dao.TypeMapper;
import cn.demo.beike.utils.GlobalCache;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pc")
public class PCController {
    public static Logger logger = LoggerFactory.getLogger(PCController.class);

    @Autowired
    RootMapper rootMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    AppointmentDetailMapper appointmentDetailMapper;

    @Autowired
    TypeMapper typeMapper;

    @Autowired
    GlobalCache globalCache;

    /**
     * 管理员登录
     * @param jsonparam
     * @return json
     */
    @PostMapping("/Login")
    @ResponseBody
    public String login(@RequestBody JSONObject jsonparam){
        String loginName = jsonparam.getString("loginName");
        String pass = jsonparam.getString("password");
        JSONObject result = new JSONObject();
        logger.info(loginName);
        logger.info(pass);
        if (loginName != null && pass != null) {
            List<Root> rootList = rootMapper.findRoot(loginName, pass);
            if (rootList.size() == 0) {
                result.put("SUCCESS", false);
                result.put("MSG", "用户名和密码错误！");
            }else{
                result.put("id",rootList.get(0).getId());
                result.put("userName",rootList.get(0).getLoginname());
            }
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "请输入用户名和密码！");
        }
        return result.toJSONString();
    }

    /**
     * 管理员添加预约信息
     * 需要自动根据时间间隔，自动生成appointmentDetail
     * @param jsonparam
     * @return
     */
    @PostMapping("/NewAppointment")
    @ResponseBody
    public String NewAppointment(@RequestBody JSONObject jsonparam) {
        System.out.println(jsonparam);
        Appointment appointment = jsonparam.toJavaObject(Appointment.class);
//        int num = appointmentMapper.insert(appointment);
        int num = appointmentMapper.insertSelective(appointment);
        JSONObject result = new JSONObject();
        if (num == 1) {
            result.put("id", appointment.getId());
            result.put("SUCCESS", true);
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "预约失败！");
        }


        int limittime = appointment.getLimittime();
        long beginTime = appointment.getBegintime().getTime();
        long endTime = appointment.getEndtime().getTime();
        long currentStamp = beginTime;
        while(currentStamp<endTime){
            AppointmentDetail nAppointmentDetail = new AppointmentDetail();
            nAppointmentDetail.setNumber(appointment.getLimitnum());
            nAppointmentDetail.setBegintime(new Date(currentStamp));
            nAppointmentDetail.setEndtime(new Date(currentStamp+limittime*60*1000));
            nAppointmentDetail.setPersonnumber(0);
            nAppointmentDetail.setAppointmentId(appointment.getId());

            appointmentDetailMapper.insert(nAppointmentDetail);
            currentStamp+=limittime*60*1000;
            logger.info("插入自动生成的appointmentDetail 记录。。。");
            logger.info(nAppointmentDetail.toString());
        }

        logger.info("return ");
        logger.info(result.toJSONString());
        return result.toJSONString();
    }

    @GetMapping("/GetAllType")
    public String GetALLType() {
        JSONArray result = new JSONArray();
        List<Type> types = typeMapper.selectAllType();
        for (Type type : types) {
            JSONObject curr = new JSONObject();
            curr.put("typeName", type.getTypename());
            curr.put("typeID", type.getId());
            result.add(curr);
        }
        return result.toJSONString();
    }
}
