package com.bjpowernode.p2p.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ClassName:TimerManager
 * Package:com.bjpowernode.p2p.timer
 * Description: 描述信息
 *
 * @date:2021/9/2 16:06
 * @author:LTG
 * @Copyright:动力节点
 */

@Component
public class TimerManager {

    @Reference(interfaceClass = IncomeRecordService.class, version = "1.0.0", check = false, timeout = 15000)
    private IncomeRecordService incomeRecordService;

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void generateIncomePlan() throws Exception {
//        System.out.println("======生成收益计划开始======");
//        incomeRecordService.generateIncomePlan();
//        System.out.println("======生成收益计划结束======");
//    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeBack() throws Exception {
        System.out.println("======返还收益计划开始======");
        incomeRecordService.generateIncomeBack();
        System.out.println("======返还收益计划结束======");
    }
}