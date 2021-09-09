package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户ID，查询账户信息
     * @param uId
     * @return
     */
    FinanceAccount selectByUid(Integer uId);

    //修改账户余额
    int updateFinanceAccountMoney(Map<String, Object> params);


    /*
    * 返还收益(更新账户余额)
    * */
    int updateByIncomeStatusAndIncomeDate(Map<String, Object> paramMap);
}