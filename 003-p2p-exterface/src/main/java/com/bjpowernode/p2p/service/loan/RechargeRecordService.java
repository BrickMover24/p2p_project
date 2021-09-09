package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

/**
 * ClassName:RechargeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description: 描述信息
 *
 * @date:2021/9/4 9:13
 * @author:LTG
 * @Copyright:动力节点
 */
public interface RechargeRecordService {

/**
 *生成订单
* */
    int addRecharge(RechargeRecord rechargeRecord);
}
