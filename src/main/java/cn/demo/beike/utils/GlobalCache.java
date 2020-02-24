package cn.demo.beike.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

@Component
public class GlobalCache {
    private static Logger logger = LoggerFactory.getLogger(GlobalCache.class);

    @Autowired
    RedisUtil redisUtil;

    private GlobalCache cache =null;
    private GlobalCache(){}

    public Object get(String key,Class clazz){
         Jedis rediscli = redisUtil.getJedis();
        return rediscli.get(key);
    }

    public void set(String key,Object value){
        Jedis rediscli = redisUtil.getJedis();
        if(value instanceof JSONObject) {
            JSONObject jsvalue = (JSONObject) value;
            byte[] bytes = JSONObject.toJSONBytes(jsvalue);
            rediscli.set(key.getBytes(), bytes);
        }else{
            logger.error("unsupport type"+value.getClass().getName());
        }
    }


    @Bean
    public GlobalCache getGlobalCache(){
        if(cache==null){
            synchronized (GlobalCache.class){
                if(cache==null){
                    cache = new GlobalCache();
                }
            }
        }
        return cache;
    }

}

