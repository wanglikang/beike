package cn.demo.beike.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil {
    private static RedisUtil redisUtil=null;

    @Autowired
    private Environment env;

    @Value("${beike.redis.ip}")
    public String redis_ip;

    @Value("${beike.redis.port}")
    public String redis_port;

    @Value("${beike.redis.maxTotal}")
    public String redis_maxTotal;

    @Value("${beike.redis.password}")
    public String redis_password;

    private static JedisPool pool = null;


//    public RedisUtil(){
//        System.out.println("RedisUtils init....");
//    }
    public void init(){
        createJedisPool();
    }


    @PostConstruct
    public void createJedisPool(){
        if (pool == null) {
            synchronized (RedisUtil.class) {
//                redis_ip = env.getProperty("beike.redis.ip");
//                redis_port = env.getProperty("beike.redis.port");
//                redis_maxTotal = env.getProperty("beike.redis.maxTotal");
                if(pool == null) {
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxTotal(redis_maxTotal==null ? 10:Integer.parseInt(redis_maxTotal));
//                    jedisPoolConfig.setMaxIdle(SysConfigUtil.getSysConfigUtil("redis.properties").getInt("redis.maxIdle"));
//                    jedisPoolConfig.setMaxWaitMillis(SysConfigUtil.getSysConfigUtil("redis.properties").getLong("redis.maxWaitMillis"));
//                    jedisPoolConfig.setTestOnBorrow(SysConfigUtil.getSysConfigUtil("redis.properties").getBoolean("redis.testOnBorrow"));
                    if (redis_password != null && !"".equals(redis_password)) {
                        // redis 设置了密码
                        pool = new JedisPool(jedisPoolConfig, redis_ip, Integer.parseInt(redis_port), 10000, redis_password);
                    } else {
                        // redis 未设置密码
                        pool = new JedisPool(jedisPoolConfig, redis_ip, Integer.parseInt(redis_port), 10000);
                    }
                }
            }
        }
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    @Bean
    public static RedisUtil getRedisUtil() {
        if (redisUtil == null) {
            synchronized (RedisUtil.class) {
                if (redisUtil == null) {
                    redisUtil = new RedisUtil();
                }
            }
        }
        return redisUtil;
    }
}
