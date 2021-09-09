package com.bjpowernode.p2p.service.impl.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 15000)
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryAllBidMoney() {
        Double allBidMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
        if (!ObjectUtils.allNotNull(allBidMoney)) {
            synchronized (this) {
                allBidMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
                if (!ObjectUtils.allNotNull(allBidMoney)) {
                    allBidMoney = bidInfoMapper.selectAllBidMoney();
                    redisTemplate.opsForValue().set(Constants.ALL_BID_MONEY,allBidMoney);
                }

            }
        }
        return allBidMoney;
    }

    /**
     * 根据产品ID，查询该产品的最新的投资记录
     * @param paramMap
     * @return
     */
    @Override
    public List<BidInfo> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap) {

        return bidInfoMapper.selectRecentlyBidInfoByLoanId(paramMap);
    }



    @Transactional
    public void invest(Map<String, Object> paramMap) throws Exception {
        // 投资的业务逻辑 新加一条投资记录(b_bid_info)
        Integer loanId = (Integer) paramMap.get("loanId");
        Integer uid = (Integer) paramMap.get("uid");
        Double bidMoney = (Double) paramMap.get("bidMoney");
        BidInfo bidInfo = new BidInfo();
        bidInfo.setLoanId(loanId);
        bidInfo.setUid(uid);
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(1);
        int bidInfoRows = bidInfoMapper.insertSelective(bidInfo);
        if (bidInfoRows == 0) {
            throw new Exception("投资失败，添加投资信息失败");
        }


        // 修改账户的可用资金

        int faRows = financeAccountMapper.updateFinanceAccountMoney(paramMap);
        if (faRows == 0) {
            throw new Exception("投资失败，修改账户余额失败");
        }

        // 修改产品信息表中的剩余可投金额（可能页需要修改产品的状态）
        // 先获取version
        LoanInfo loanInfoDetail = loanInfoMapper.selectByPrimaryKey(loanId);
        paramMap.put("version",loanInfoDetail.getVersion());
        int rows  = loanInfoMapper.updateLeftProductMoney(paramMap);
        if (rows == 0) {
            throw new Exception("投资失败，修改产品剩余可投金额失败");
        }

        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        if (loanInfo.getLeftProductMoney() == 0) {
            // 将产品的状态由0改为1
            loanInfo.setProductStatus(1);
            // 设置满标时间
            loanInfo.setProductFullTime(new Date());
            int row = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
            if (row == 0) {
                throw new Exception("投资失败，修改产品状态失败");
            }

        }

        String phone  = (String) paramMap.get("phone");

        // 记录投资排行榜
        redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP, phone, bidMoney);



    }
}
