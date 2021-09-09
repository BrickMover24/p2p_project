package com.bjpowernode.p2p.service.impl.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.service.user.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ClassName:RedisServiceImpl
 * Package:com.bjpowernode.p2p.service.impl.user
 * Description: 描述信息
 *
 * @date:2021/8/31 9:53
 * @author:sjf
 * @Copyright:动力节点
 */
@Component
@Service(interfaceClass = RedisService.class, version = "1.0.0", timeout = 15000)
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key,value,30, TimeUnit.MINUTES);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }



    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER);
    }
}
