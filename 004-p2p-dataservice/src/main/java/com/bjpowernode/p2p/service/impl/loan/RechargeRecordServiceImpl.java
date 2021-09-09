package com.bjpowernode.p2p.service.impl.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName:RechargeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.impl.loan
 * Description: 描述信息
 *
 * @date:2021/9/4 9:15
 * @author:LTG
 * @Copyright:动力节点
 */

@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    public int addRecharge(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }
}
