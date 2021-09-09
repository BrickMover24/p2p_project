package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 查询历史平均年化收益率
     * @return
     */
    Double selectHistryAvgRate();

    /**
     * 根据产品类型 查询产品列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> selectLoanInfoListByProductType(Map<String, Object> paramMap);


    /**
     * 查询总条数
     * @param paramMap
     * @return
     */
    Integer selectTotalPage(Map<String, Object> paramMap);


    int updateLeftProductMoney(Map<String, Object> params);


    /*
    * 查询出产品状态为1(已满标,但是没有生成收益记录)的产品
     */
    List<LoanInfo> selectLoanInfoListByProductStatus(int productStatus);
}