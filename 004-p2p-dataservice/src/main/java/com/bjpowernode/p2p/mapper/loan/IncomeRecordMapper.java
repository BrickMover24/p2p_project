package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.IncomeRecord;

import java.util.List;

public interface IncomeRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

     int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);



    /**
     *  返还当前收益状态和时间
     * **/

    List<IncomeRecord> selectByStatusAndIncomeDate(int incomeStatus);

}