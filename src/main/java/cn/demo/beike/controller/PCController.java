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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
                result.put("userName",rootList.get(0).getLoginName());
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
        logger.info(appointment.toString());
        // int num = appointmentMapper.insert(appointment);
        int num = appointmentMapper.insertSelective(appointment);
        JSONObject result = new JSONObject();
        if (num == 1) {
            result.put("id", appointment.getId());
            result.put("SUCCESS", true);
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "预约失败！");
        }


        int limittime = appointment.getLimitTime();
        long beginTime = appointment.getBeginTime().getTime();
        long endTime = appointment.getEndTime().getTime();
        long currentStamp = beginTime;
        while(currentStamp < endTime){
            AppointmentDetail nAppointmentDetail = new AppointmentDetail();
            nAppointmentDetail.setAvailableNumber(appointment.getLimitNum());
            nAppointmentDetail.setBeginTime(new Date(currentStamp));
            nAppointmentDetail.setEndTime(new Date(currentStamp+limittime*60*1000));
            nAppointmentDetail.setPersonNumber(0);
            nAppointmentDetail.setAppointmentId(appointment.getId());

            appointmentDetailMapper.insertSelective(nAppointmentDetail);
            currentStamp += limittime * 60 * 1000;
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
            curr.put("typeName", type.getTypeName());
            curr.put("typeID", type.getId());
            result.add(curr);
        }
        return result.toJSONString();
    }

    /**
     * 查询预约
     * @param pagesize
     * @param pagenum
     * @return
     */
    @GetMapping("/GetAppointment/{pagesize}_{pagenum}")
    public String GetAppointment(@PathVariable int pagesize, @PathVariable int pagenum) {
        if (pagesize <= 0)   pagesize = 10;
        if (pagenum < 0)   pagenum = 1;
        PageHelper.startPage(pagenum, pagesize);
        List<Appointment> appointmentList = appointmentMapper.selectAppointment();
        PageInfo<Appointment> appointmentPageInfo = new PageInfo<Appointment>(appointmentList);
        JSONObject result = new JSONObject();
        result.put("pageNum", appointmentPageInfo.getPageNum());
        result.put("pageSize", appointmentPageInfo.getPageSize());
        result.put("pages", appointmentPageInfo.getPages());
        result.put("total", appointmentPageInfo.getTotal());

        JSONArray jsonArray = new JSONArray();
        logger.info(appointmentPageInfo.toString());
        for (Appointment appointment : appointmentPageInfo.getList()) {
            JSONObject object = new JSONObject();
            object.put("id", appointment.getId());
            object.put("typeName", appointment.getTypeName());
            object.put("detailDate", appointment.getDetailDate());
            object.put("beginTime", appointment.getBeginTime());
            object.put("endTime", appointment.getEndTime());
            object.put("limitTime", appointment.getLimitTime());
            object.put("limitNum", appointment.getLimitNum());
            object.put("flag", appointment.getFlag());
            jsonArray.add(object);
        }
        result.put("list", jsonArray);
        return result.toString();
    }

    /**
     * 根据appointment的id获取appointment
     * @param jsonparam
     * @return
     */
    @PostMapping("/GetAppointmentById")
    @ResponseBody
    public String GetAppointmentById(@RequestBody JSONObject jsonparam) {
        System.out.println(jsonparam);
        JSONObject result = new JSONObject();
        Integer id = jsonparam.getInteger("id");
        Appointment appointment = appointmentMapper.selectByPrimaryKey(id);
        if (appointment != null) {
            logger.info(appointment.toString());
            result.put("typeName", appointment.getTypeName());
            result.put("detailDate", appointment.getDetailDate());
            result.put("beginTime", appointment.getBeginTime());
            result.put("endTime", appointment.getEndTime());
            result.put("limitTime", appointment.getLimitTime());
            result.put("limitNum", appointment.getLimitNum());
            result.put("flag", appointment.getFlag());
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "要查询的信息不存在！");
        }
        return result.toJSONString();
    }

    /**
     * 更新appointment
     * @param jsonparam
     * @return
     */
    @PostMapping("/updateAppointmentById")
    @ResponseBody
    public String updateAppointmentById(@RequestBody JSONObject jsonparam) {
        System.out.println(jsonparam);
        Appointment appointment = jsonparam.toJavaObject(Appointment.class);
        logger.info(appointment.toString());
        JSONObject result = new JSONObject();
        Appointment appointment_new = appointmentMapper.selectByPrimaryKey(appointment.getId());
        if (appointment_new != null) {
            if (appointment.getTypeName() != null) appointment_new.setTypeName(appointment.getTypeName());
            if (appointment.getDetailDate() != null) appointment_new.setDetailDate(appointment.getDetailDate());
            if (appointment.getBeginTime() != null) appointment_new.setBeginTime(appointment.getBeginTime());
            if (appointment.getEndTime() != null) appointment_new.setEndTime(appointment.getEndTime());
            if (appointment.getLimitTime() != null) appointment_new.setLimitTime(appointment.getLimitTime());
            if (appointment.getLimitNum() != null) appointment_new.setLimitNum(appointment.getLimitNum());
            if (appointment.getFlag() != null) appointment_new.setFlag(appointment.getFlag());
            int num = appointmentMapper.updateByPrimaryKeySelective(appointment_new);
            if (num != 0) {
                result.put("typeName", appointment.getTypeName());
                result.put("detailDate", appointment.getDetailDate());
                result.put("beginTime", appointment.getBeginTime());
                result.put("endTime", appointment.getEndTime());
                result.put("limitTime", appointment.getLimitTime());
                result.put("limitNum", appointment.getLimitNum());
                result.put("flag", appointment.getFlag());
            }
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "此预约不存在，无法修改！");
        }
        return result.toJSONString();
    }

    /**
     * 删除appointment
     * @param jsonparam
     * @return
     */
    @PostMapping("/DeleteAppointmentById")
    @ResponseBody
    public String DeleteAppointmentById(@RequestBody JSONObject jsonparam) {
        System.out.println(jsonparam);
        JSONObject result = new JSONObject();
        Integer id = jsonparam.getInteger("id");
        Appointment appointment = appointmentMapper.selectByPrimaryKey(id);
        logger.info(appointment.toString());
        if (appointment == null) {
            result.put("SUCCESS", false);
            result.put("MSG", "此预约不存在！");
        } else {
            appointmentMapper.deleteByPrimaryKey(id);
            result.put("SUCCESS", true);
        }
        return result.toJSONString();
    }

    /**
     * 添加新的预约
     * @param jsonparam
     * @return
     */
    @PostMapping("/AddAppointment")
    @ResponseBody
    public String AddAppointment(@RequestBody JSONObject jsonparam) {
        System.out.println(jsonparam);
        JSONObject result = new JSONObject();
        Appointment appointment = jsonparam.toJavaObject(Appointment.class);
        logger.info(appointment.toString());
        // int num = appointmentMapper.insert(appointment);
        int num = appointmentMapper.insertSelective(appointment);
        if (num == 1) {
            result.put("id", appointment.getId());
        } else {
            result.put("SUCCESS", false);
            result.put("MSG", "预约失败！");
        }
        return result.toJSONString();
    }
}
