package com.bjpowernode.p2p.service.loan;

import java.util.Map;

/**
 * ClassName:kuaiQianService
 * Package:com.bjpowernode.p2p.service.loan
 * Description: 描述信息
 *
 * @date:2021/9/3 22:07
 * @author:LTG
 * @Copyright:动力节点
 */
public interface kuaiQianService {


    Map<String, String> makeKuaiQianRequestParam(String orderNo,String yuan, String name, String phone, String remoteAddr);

}
