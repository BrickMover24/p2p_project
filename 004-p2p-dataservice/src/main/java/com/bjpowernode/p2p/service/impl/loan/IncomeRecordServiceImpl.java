package com.bjpowernode.p2p.service.impl.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * ClassName:IncomeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.impl.loan
 * Description: 描述信息
 *
 * @date:2021/9/2 16:14
 * @author:LTG
 * @Copyright:动力节点
 */
@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    /*
    * 生成收益计划
    * */

    public void generateIncomePlan() throws Exception {
        // 目标：生成收益计划数据
        // 1. 查询投资产品信息表(条件 productStatus == 1) =>  List<IncomeRecord>
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductStatus(1);
        // 2. 遍历已经满标的产品(productStatu == 1) 得到每一天满标产品
        for (LoanInfo loanInfo : loanInfoList) {
            // 3. 根据每一个满表产品 得到投资记录
            List<BidInfo> bidInfoList = bidInfoMapper.selectLoanId(loanInfo.getId());
            // 4. 遍历(已满标)投资记录
            for (BidInfo bidInfo : bidInfoList) {
                // 5. 和根据每一条投资记录信息(产品信息) 生成收益计划数据
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setLoanId(bidInfo.getLoanId());
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeStatus(0);  // 0 :未返还 1：已返回

                Date incomeDate = null;  // 收益时间
                Double incomeMoney = null;
                if (loanInfo.getProductType() == 0) {
                    // 新手宝 收益时间 = 产品的满标时间(Date) + 周期(天)(int)
                    incomeDate =  DateUtils.addDays(loanInfo.getProductFullTime(), loanInfo.getCycle());
                    // 新手宝 收益金额 = 投资金额 * 日利率 *  周期(天)
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 / 365) * loanInfo.getCycle();
                } else {
                    // 散标和优选 收益时间 = 产品的满标时间(Date) + 周期(月)(int)
                    incomeDate =  DateUtils.addMonths(loanInfo.getProductFullTime(), loanInfo.getCycle());
                    // 散标和优选 收益金额 = 投资金额 * 日利率 *  周期(天) * 30
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 / 365) * loanInfo.getCycle() * 30;
                }
                //

                // 收益金额
                incomeMoney = Math.round(incomeMoney*Math.pow(10,2))/Math.pow(10,2);

                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecordMapper.insertSelective(incomeRecord);



            }
            //6. 将当前产品的产品状态由1 改成2(已满标，且生成收益计划)
            loanInfo.setProductStatus(2);
            int rows = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
            if (rows == 0) {
                throw new Exception("产品状态由1 改成2失败");
            }


        }

    }

    /*
    * 返还收益
    * */
    @Transactional
    public void generateIncomeBack() throws Exception {
        //1. 获取待返还收益的记录(收益状态为0 and 收益时间=当前时间)
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectByStatusAndIncomeDate(0);
        for (IncomeRecord incomeRecord : incomeRecordList) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("bidMoney",incomeRecord.getBidMoney());
            paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());
            paramMap.put("uid",incomeRecord.getUid());
            int faRows = financeAccountMapper.updateByIncomeStatusAndIncomeDate(paramMap);
            if (faRows == 0) {
                throw new Exception("返还收益更新账户余额失败");
            }

            // 2. 修改当前的收益的状态为1(已返回)
            incomeRecord.setIncomeStatus(1);
            int irRows = incomeRecordMapper.updateByPrimaryKeySelective(incomeRecord);
            if (irRows == 0) {
                throw new Exception("返还收益更新收益状态失败");
            }

        }
    }
}

