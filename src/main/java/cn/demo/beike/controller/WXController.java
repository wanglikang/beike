package cn.demo.beike.controller;

import cn.demo.beike.entity.AppointmentDetail;
import cn.demo.beike.entity.AppointmentResultMapBean;
import cn.demo.beike.entity.PersonalAppointment;
import cn.demo.beike.entity.Type;
import cn.demo.beike.entity.dao.AppointmentDetailMapper;
import cn.demo.beike.entity.dao.AppointmentMapper;
import cn.demo.beike.entity.dao.PersonalAppointmentMapper;
import cn.demo.beike.entity.dao.TypeMapper;
import cn.demo.beike.utils.GlobalCache;
import cn.demo.beike.utils.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/wx")
public class WXController {
    public static Logger logger = LoggerFactory.getLogger(WXController.class);
    @Autowired
    TypeMapper typeMapper;

    @Autowired
    AppointmentDetailMapper appointmentDetailMapper;

    @Autowired
    PersonalAppointmentMapper   personalAppointmentMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    GlobalCache globalCache;

//    private Lock lock = new ReentrantLock();
    private ConcurrentHashMap<Integer,Integer> lockMap = new ConcurrentHashMap<>();

    @GetMapping("/getAllAppointmentType")
    public String getAllAppointmentType(){
//        Object redisresultobj = globalCache.get("meta_alltypes",null);
        JSONObject result = new JSONObject();
//        if(redisresultobj!=null){
//            JSONObject redisresult = JSONObject.parseObject((String) JSONObject.toJSON(redisresultobj));
//            result.put("SUCCESS",true);
//            result.put("info",redisresult);
//            logger.info("get meta_alltypes by cache");
//        }else {
            List<Type> types = typeMapper.selectAllAvailableType();
            result = new JSONObject();
            JSONArray arrs = new JSONArray();
            if (types != null) {
                types.forEach(v -> {
                    JSONObject ele = new JSONObject();
                    ele.put("typeName", v.getTypename());
                    ele.put("id", v.getId());
                    ele.put("flag", v.getFlag());
                    arrs.add(ele);
                });
                result.put("SUCCESS",true);
            }else{
                result.put("SUCCESS",false);
                result.put("MSG","查找不到对应下信息");
            }
            result.put("INFO",arrs);
//            if(arrs.size()>0) {
//                globalCache.set("meta_alltypes", arrs);
//            }
            logger.info("get meta_alltypes by sql");
//        }

        logger.info(result.toJSONString());
        return  result.toJSONString();
    }

    @PostMapping("/getAllAppointmentTypeByTypeId")
    public String getAllAppointmentTypeByTypeId(@RequestBody JSONObject jsonparam){
        int typeId = jsonparam.getInteger("typeId");
        Date paraTime = jsonparam.getDate("time");
//        Object redisresultobj = globalCache.get("meta_typeId",null);
        JSONObject result = new JSONObject();
//        if(redisresultobj!=null){
//            JSONObject redisresult = JSONObject.parseObject((String) JSONObject.toJSON(redisresultobj));
//            result.put("SUCCESS",true);
//            result.put("info",redisresult);
//            logger.info("get meta_typeId\t"+typeId +"\tby cache");
//        }else {
            JSONArray arrs = new JSONArray();

            List<AppointmentResultMapBean> aresult = appointmentMapper.selectAvailableAppointmentByTypeId(typeId);
            if(aresult!=null && aresult.size()>0) {
                aresult.stream().forEach(v -> {
                    JSONObject ele = new JSONObject();
                    ele.put("typeName", v.getTypeName());
                    ele.put("number", v.getNumber());
                    ele.put("appointmentID", v.getAppointmentId());
                    arrs.add(ele);
                });
                result.put("SUCCESS",true);
                result.put("INFO",arrs);
            }else {
                result.put("SUCCESS", false);
                result.put("MSG","查找不到对应下信息");
                logger.error(jsonparam.toString());
            }

            if(arrs.size() > 0) {
                globalCache.set("meta_typeId", arrs);
            }
            logger.info("get meta_typeId\t"+typeId+"\tby sql");
//        }

        logger.info(result.toJSONString());
        return  result.toJSONString();
    }

    @PostMapping("/getAppointmentDetail")
    public String getAppointmentDetail(@RequestBody JSONObject jsonparam){

        String appointmentID = jsonparam.getString("appointmentID");
        //这个接口先不使用缓存

        JSONObject result=new JSONObject();
        List<AppointmentDetail> appointmentdetails = appointmentDetailMapper.getAppointmentByAppointmentId(appointmentID);
        JSONArray arrs = new JSONArray();
        if(appointmentdetails!=null && appointmentdetails.size()>0) {
            appointmentdetails.forEach(v -> {
                JSONObject ele = new JSONObject();
                ele.put("id", v.getId());
                ele.put("personNumber", v.getPersonnumber());
                ele.put("beginTime", v.getBegintime());
                ele.put("endTime", v.getEndtime());
                ele.put("number", v.getNumber());
                ele.put("appointmentId", v.getAppointmentid());
                arrs.add(ele);
            });
            result.put("SUCCESS",true);
        }else{
            result.put("SUCCESS",false);
            result.put("MSG","查找不到对应的信息");
            logger.error(jsonparam.toString());
        }

        result.put("INFO",arrs);


        logger.info(result.toJSONString());
        return result.toJSONString();
    }


    /**
     * 需要事务，加锁，锁库存
     * @param jsonObject
     * @return
     */
    @PostMapping("/makeAppointment")
    public String makeAppointment(@RequestBody JSONObject jsonObject){
        logger.info("rev"+jsonObject.toJSONString());
        int name = jsonObject.getInteger("name");//人员姓名
        int number = jsonObject.getInteger("number");//人员工号
        int appointmentDetailID = jsonObject.getInteger("appointmentDetailID");//预约详情号
        JSONObject result = new JSONObject();

        List<AppointmentDetail> appointmentDetail = appointmentDetailMapper.getAppointmentByAppointmentId(appointmentDetailID + "");
        if(appointmentDetail.size()==0){
            logger.warn("获取不到对应的appointmentDetail:"+appointmentDetailID);
        }else{
            AppointmentDetail apd = appointmentDetail.get(0);
            Integer num = apd.getNumber();
            if(num<1){
                //库存不足，，操作失败
                result.put("SUCCESS",false);
                result.put("MSG","可用席位不足");
                logger.warn(jsonObject.toJSONString());
            }else {
                int tryCount = 5;
                boolean catched = false;
                while (tryCount-->0 && !catched) {
                    Integer lockid = lockMap.get(appointmentDetailID);
                    //库存充足，成功
                    if(lockid!=null && lockid>0){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        lockMap.put(appointmentDetailID, 1);
                        //抢购
                        PersonalAppointment newRecord = new PersonalAppointment();
                        newRecord.setAppointmentdetailid(appointmentDetailID);
                        newRecord.setPersonnumber(number);
                        newRecord.setFlag("1");
                        newRecord.setTime(new java.sql.Date(System.currentTimeMillis()));
                        int rows = personalAppointmentMapper.insert(newRecord);

                        if (rows > 0) {
                            result.put("SUCCESS", true);
                            JSONArray arr = new JSONArray();
                            arr.add(new JSONObject().put("appoinmentDetailID", appointmentDetailID));
                            result.put("INFO", arr);
                        } else {
                            result.put("SUCCESS", false);
                            result.put("MSG", "插入失败");
                            logger.error(jsonObject.toJSONString());
                        }
                        apd.setNumber(num - 1);
                        int effectRow = appointmentDetailMapper.updateByPrimaryKeySelective(apd);
                        if (effectRow < 1) {
                            logger.warn("更新未成功");
                        }
                        lockMap.put(appointmentDetailID,0);
                        catched = true;
                    }
                }
            }
        }
        logger.info(result.toString());
        return result.toJSONString();
    }



}
