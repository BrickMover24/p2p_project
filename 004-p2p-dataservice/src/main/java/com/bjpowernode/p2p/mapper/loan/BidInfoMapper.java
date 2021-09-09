package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


    /**
     * 获取平台总成交数量
     * @return
     */
    Double selectAllBidMoney();

    /**
     * 根据产品ID查询该产品最近的10条投资记录
     * @param paramMap
     * @return
     */
    List<BidInfo> selectRecentlyBidInfoByLoanId(Map<String, Object> paramMap);

    /*
    * 根据loanId 查询投资记录列表
    * */
    List<BidInfo> selectLoanId(Integer loanInfoId);
}