package cn.demo.beike.controller;

import cn.demo.beike.entity.*;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/wx")
public class WXController {
    public static Logger logger = LoggerFactory.getLogger(WXController.class);

    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
    @Autowired
    TypeMapper typeMapper;

    @Autowired
    AppointmentDetailMapper appointmentDetailMapper;

    @Autowired
    PersonalAppointmentMapper   personalAppointmentMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

//    @Autowired
//    GlobalCache globalCache;

//  private Lock lock = new ReentrantLock();
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
                    ele.put("typeName", v.getTypeName());
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
        logger.info(jsonparam.toJSONString());
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
                    logger.info(v.toString());
                    JSONObject ele = new JSONObject();
                    ele.put("typeName", v.getTypeName());
                    ele.put("number", v.getAvailableSumNumber());
                    ele.put("appointmentID", v.getAppointmentId());
                    ele.put("date",dateSdf.format(v.getDate()));
                    arrs.add(ele);
                });
                result.put("SUCCESS",true);
                result.put("INFO",arrs);
            }else {
                result.put("SUCCESS", false);
                result.put("MSG","查找不到对应下信息");
                logger.error(jsonparam.toString());
            }

//            if(arrs.size() > 0) {
//                globalCache.set("meta_typeId", arrs);
//            }
            logger.info("get meta_typeId\t"+typeId+"\tby sql");
//        }

        logger.info(result.toJSONString());
        return  result.toJSONString();
    }

    @PostMapping("/getAppointmentDetail")
    public String getAppointmentDetail(@RequestBody JSONObject jsonparam){
        logger.info(jsonparam.toJSONString());

        String appointmentID = jsonparam.getString("appointmentID");

        logger.info("appointmentID:"+appointmentID);
        //这个接口先不使用缓存

        JSONObject result=new JSONObject();
        List<AppointmentDetail> appointmentdetails = appointmentDetailMapper.getAppointmentDetailsByAppointmentId(appointmentID);
        JSONArray arrs = new JSONArray();
        if(appointmentdetails!=null && appointmentdetails.size()>0) {
            appointmentdetails.forEach(v -> {
                logger.info(v.toString());
                JSONObject ele = new JSONObject();
                ele.put("id", v.getId());
                ele.put("personNumber", v.getPersonNumber());
                ele.put("beginTime",timeSdf.format(v.getBeginTime()));
                ele.put("endTime", timeSdf.format(v.getEndTime()));
                ele.put("number", v.getAvailableNumber());
                ele.put("appointmentId", v.getAppointmentId());
                ele.put("appointmentDetailID", v.getId());
                arrs.add(ele);
            });
            result.put("SUCCESS",true);
            result.put("INFO",arrs);
        }else{
            result.put("SUCCESS",false);
            result.put("MSG","查找不到对应的信息");
            logger.error(jsonparam.toString());
        }



        logger.info(result.toJSONString());
        return result.toJSONString();
    }


    /**
     * 需要事务，加锁，锁库存
     * 流程如下：
     *  1.获取appointment详情
     *  2.对预约详情appointmentdetailId加乐观锁，重试5次，每次睡5毫秒
     *  3.
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/makeAppointment")
    public String makeAppointment(@RequestBody JSONObject jsonObject) {
        logger.info(jsonObject.toJSONString());
        String name = jsonObject.getString("name");//人员姓名
        int personNumber = jsonObject.getInteger("number");//人员工号
        int appointmentDetailID = jsonObject.getInteger("appointmentDetailID");//预约详情号
        JSONObject result = new JSONObject();
        AppointmentDetail appointmentDetail = appointmentDetailMapper.getAppointmentDetailById(appointmentDetailID + "");
        if (appointmentDetail != null) {
            PersonalAppointment optinalPersonalAppointment = personalAppointmentMapper.selectIfHasAppointmented(personNumber + "", appointmentDetail.getAppointmentId() + "");
            if (optinalPersonalAppointment != null) {
                result.put("SUCCESS", false);
                result.put("MSG", "曾经预约过了");
                logger.info(result.toJSONString());
                return result.toJSONString();
            }
            int tryCount = 5;
            boolean catched = false;
            while (tryCount-- > 0 && !catched) {
                Integer lockid = lockMap.get(appointmentDetailID);
                //先上锁
                if (lockid != null && lockid > 0) {
                    try {
                        logger.info("被锁，，等待一下" + tryCount);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.info("抢到锁，，进行减库存操作");

                    //是否存在相同的appointment的预约过

                    logger.info(appointmentDetail.toString());
                    Integer num = appointmentDetail.getAvailableNumber();
                    if (num < 1) {
                        //库存不足，，操作失败
                        result.put("SUCCESS", false);
                        result.put("MSG", "可用席位不足");
                        logger.warn(jsonObject.toJSONString());
                    } else {
                        //map中设置对象，，即加排他锁
                        lockMap.put(appointmentDetailID, 1);
                        //抢购
                        PersonalAppointment newRecord = new PersonalAppointment();
                        newRecord.setAppointmentdetailId(appointmentDetailID);
                        newRecord.setPersonNumber(personNumber);
                        newRecord.setFlag("1");
                        newRecord.setTime(new java.sql.Date(System.currentTimeMillis()));
                        newRecord.setStatus(1);
                        newRecord.setAppointmentId(appointmentDetail.getAppointmentId());

                        int rows = personalAppointmentMapper.insertSelective(newRecord);

                        if (rows > 0) {
                            result.put("SUCCESS", true);
                            JSONArray arr = new JSONArray();
                            JSONObject ta = new JSONObject();
                            Appointment aaa = appointmentMapper.selectByPrimaryKey(appointmentDetail.getAppointmentId());
                            ta.put("typeID", aaa.getTypeId());
                            ta.put("appointmentID", appointmentDetail.getAppointmentId());
                            ta.put("appoinmentDetailID", appointmentDetailID);
                            ta.put("date", dateSdf.format(aaa.getDetailDate()));
                            ta.put("beginTime", timeSdf.format(appointmentDetail.getBeginTime()));
                            ta.put("endTime", timeSdf.format(appointmentDetail.getEndTime()));
                            ta.put("number", personNumber);

                            arr.add(ta);
                            result.put("INFO", arr);
                            System.out.println(result);
                            appointmentDetail.setAvailableNumber(num - 1);
                            logger.info(appointmentDetail.toString());
                            int effectRow = appointmentDetailMapper.updateByPrimaryKeySelective(appointmentDetail);
                            if (effectRow < 1) {
                                logger.warn("更新未成功");
                            }
                        } else {
                            result.put("SUCCESS", false);
                            result.put("MSG", "插入失败");
                            logger.error(jsonObject.toJSONString());
                        }

                        //释放锁
                        lockMap.put(appointmentDetailID, 0);
                        catched = true;
                    }
                }
            }
        }else{
            logger.warn("获取不到对应的appointmentDetail:" + appointmentDetailID);
            result.put("SUCCESS",false);
            result.put("MSG","获取不到对应的appointmentDetail:" + appointmentDetailID);
        }

        logger.info(result.toString());
        return result.toJSONString();
    }


    @PostMapping("/cancelAppointment")
    public String cancelAppointment(@RequestBody JSONObject jsonObject){
        logger.info(jsonObject.toJSONString());
        String name = jsonObject.getString("name");
        String number = jsonObject.getString("number");
        int appointmentDetailID = jsonObject.getInteger("appointmentDetailID");
        JSONObject result = new JSONObject();


        try {
            PersonalAppointment pa = personalAppointmentMapper.selectByPersonAndAppointmentdetailId(number + "", appointmentDetailID + "");
            pa.setStatus(0);
            JSONArray arr = new JSONArray();
            personalAppointmentMapper.updateByPrimaryKeySelective(pa);
            result.put("SUCCESS",true);
            arr.add(appointmentDetailID);
            result.put("INFO",arr);
        }catch (Exception e){
            result.put("SUCCESS",false);
            result.put("MSG",e.getCause());
            e.printStackTrace();
        }

        logger.info(result.toJSONString());
        return result.toJSONString();
    }

    @PostMapping("/getAllAppointment")
    public String getAllAppointment(@RequestBody JSONObject jsonObject){
        String name = jsonObject.getString("name");
        String number = jsonObject.getString("number");
        JSONObject result = new JSONObject();
        logger.info(jsonObject.toJSONString());

        try {
            List<PersonalAppointment> pas = personalAppointmentMapper.selectAllAppointmentByPersonnumber(number);
            JSONArray arr = new JSONArray();
            if (pas != null && pas.size() > 0) {
                pas.stream().forEach(v -> {
                    logger.info(v.toString());
                    JSONObject ele = new JSONObject();
                    /**
                     *    appointmentDetailID，
                     *        date，
                     *        endtime
                     *        begintime
                     *        typename
                     */
                    ele.put("appointmentDetailID", v.getAppointmentdetailId());
                    ele.put("date", dateSdf.format(v.getTime()));
                    ele.put("begintime", timeSdf.format(v.getBeginTime()));
                    ele.put("endtime", timeSdf.format(v.getEndTime()));
                    ele.put("typename", v.getTypeName());
                    arr.add(ele);
                });
            }
            result.put("SUCCESS", true);
            result.put("INFO", arr);
        }catch (Exception e){
            e.printStackTrace();
            result.put("SUCCESS", false);
            result.put("MSG", "发生异常"+"用户工号：\t" + number);
        }

        logger.info(result.toJSONString());
        return result.toJSONString();

    }

}
