package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.BidUserVO;
import com.bjpowernode.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {

    /**
     * 获取平台历史平均年化收益率
     * @return
     */
    Double queryHistryAvgRate();


    /**
     * 根据产品类型查询产品列表
     * @param paramMap Map productType currentPage pageSize
     * @return 产品列表
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);


    /**
     * 分页查询产品列表
     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);


    /**
     * 根据id查询产品详情
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);



    /*
    * 投资排行榜
    * */
    List<BidUserVO> queryInvestTop();

}
