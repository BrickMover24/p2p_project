package com.bjpowernode.p2p.service.loan;

/**
 * ClassName:IncomeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description: 描述信息
 *
 * @date:2021/9/2 16:13
 * @author:LTG
 * @Copyright:动力节点
 */

public interface IncomeRecordService {
    /**
    * 生成收益计划
    * */
    void generateIncomePlan() throws Exception;


    /**
    * 返还收益
    * */
    void generateIncomeBack() throws Exception;

}
