package com.bjpowernode.p2p.service.user;

/**
 * ClassName:RedisService
 * Package:com.bjpowernode.p2p.service.user
 * Description: 描述信息
 *
 * @date:2021/8/31 9:53
 * @author:sjf
 * @Copyright:动力节点
 */
public interface RedisService {
    void put(String key, String value);

    String get(String phone);



    Long getOnlyNumber();

}
