package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoService {

    /**
     * 获取平台累计投资金额
     * @return
     */
    Double queryAllBidMoney();

    /**
     * 根据产品ID，查询该产品的最近的10条投资记录
     * @param paramMap
     * @return
     */
    List<BidInfo> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap);

    /*
    * 用户投资
    * */
    void invest(Map<String, Object> params) throws Exception;

}
