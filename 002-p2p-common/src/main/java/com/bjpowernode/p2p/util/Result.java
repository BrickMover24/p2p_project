package com.bjpowernode.p2p.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:Result
 * Package:com.bjpowernode.p2p.util
 * Description: 描述信息
 *
 * @date:2021/8/30 11:12
 * @author:sjf
 * @Copyright:动力节点
 */
public class Result<K , V> extends HashMap<String , Object> {

    public static Result success() {
        Result<String, Object> resultJson = new Result<String , Object>();
        resultJson.put("code",1);
        resultJson.put("message","");
        resultJson.put("success",false);

        return resultJson;
    }

    public static Result success(String msg) {
        Result<String, Object> resultJson = new Result<String , Object>();
        resultJson.put("code",1);
        resultJson.put("message",msg);
        resultJson.put("success",false);

        return resultJson;
    }

    public static Result error(String msg) {
        Result<String, Object> resultJson = new Result<String , Object>();
        resultJson.put("code",-1);
        resultJson.put("message",msg);
        resultJson.put("success",false);

        return resultJson;
    }
}
