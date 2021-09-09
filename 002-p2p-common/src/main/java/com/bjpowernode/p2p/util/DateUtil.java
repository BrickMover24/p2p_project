package com.bjpowernode.p2p.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:DateUtil
 * Package:com.bjpowernode.p2p.util
 * Description: 生成时间戳
 *
 * @date:2021/9/4 9:04
 * @author:LTG
 * @Copyright:动力节点
 */
public class DateUtil {

    public static String getTimestramp(){

        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
