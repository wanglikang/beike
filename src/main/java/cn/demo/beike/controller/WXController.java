package cn.demo.beike.controller;

import cn.demo.beike.entity.Type;
import cn.demo.beike.entity.dao.TypeMapper;
import cn.demo.beike.utils.GlobalCache;
import cn.demo.beike.utils.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/wx")
public class WXController {
    public static Logger logger = LoggerFactory.getLogger(WXController.class);
    @Autowired
    TypeMapper typeMapper;

    @Autowired
    GlobalCache globalCache;

    @GetMapping("/getAllAppointmentType")
    @ResponseBody
    public String getAllAppointmentType(){


        Object redisresultobj = globalCache.get("meta_alltypes",null);
        JSONObject result = null;
        if(redisresultobj!=null){
            JSONObject redisresult = JSONObject.parseObject((String) JSONObject.toJSON(redisresultobj));
            result = redisresult;
            logger.info("get meta_alltypes by cache");
        }else {
            List<Type> types = typeMapper.selectAllType();
            result = new JSONObject();
            if (types != null) {
                JSONArray arrs = new JSONArray();
                types.forEach(v -> {
                    JSONObject ele = new JSONObject();
                    ele.put("typeName", v.getTypename());
                    ele.put("id", v.getId());
                    ele.put("flag", v.getFlag());
                    arrs.add(ele);
                });
                result.put("alltypes",arrs);
            }
            globalCache.set("meta_alltypes",result);
            logger.info("get meta_alltypes by sql");
        }

        logger.info(result.toJSONString());
        return  result.toJSONString();
    }

}
