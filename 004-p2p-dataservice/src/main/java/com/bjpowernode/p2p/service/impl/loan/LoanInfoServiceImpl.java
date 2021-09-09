package com.bjpowernode.p2p.service.impl.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.BidUserVO;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 15000)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    /**
     * 查询历史平均年化收益率
     * @return
     */
    @Override
    public Double queryHistryAvgRate() {
        // 修改Redis的key的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 使用同步代码块+双重验证解决Reids缓存击穿的问题
        // 1. 从Reids中获取数据
        Double histryAvgRate = (Double) redisTemplate.opsForValue().get(Constants.HISTRY_AVG_RATE);
        // 2. 判断histryAvgRate是否为空
        if (!ObjectUtils.allNotNull(histryAvgRate)) {
            synchronized (this) {
                histryAvgRate = (Double) redisTemplate.opsForValue().get(Constants.HISTRY_AVG_RATE);
                if (!ObjectUtils.allNotNull(histryAvgRate)) {
                    // Reids中没有值，查询数据库
                    histryAvgRate = loanInfoMapper.selectHistryAvgRate();
                    redisTemplate.opsForValue().set(Constants.HISTRY_AVG_RATE,histryAvgRate,7, TimeUnit.DAYS);
                    System.out.println("从数据库中获取数据");
                }else {
                    System.out.println("从Redis中获取数据");
                }
            }

        } else {
            System.out.println("从Redis中获取数据");
        }
        return histryAvgRate;
    }

    /**
     * 根据productType和分页参数 查询产品
     * @param paramMap Map productType currentPage pageSize
     * @return
     */
    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoListByProductType(paramMap);
    }

    /**
     * 分页查询产品列表
     * @param paramMap
     * @return
     */
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        // 封装一个PaginationVO(分页)对象
        PaginationVO<LoanInfo> paginationVO = new PaginationVO<>();
        // 查询产品列表(productType(可选)、currentPage(当前页)、pageSize 每页显示的条数)
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductType(paramMap);
        // (根据产品类型，可选) 查询总条数
        Integer totalSize = loanInfoMapper.selectTotalPage(paramMap);
        paginationVO.setDatas(loanInfoList);
        paginationVO.setTotalSize(totalSize);
        return paginationVO;
    }

    /**
     * 根据ID查询产品详情
     * @param id
     * @return
     */
    @Override
    public LoanInfo queryLoanInfoById(Integer id) {

        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BidUserVO> queryInvestTop() {
        List<BidUserVO> list = new ArrayList<BidUserVO>();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 5);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        BidUserVO bidUser ;
        while (iterator.hasNext()) {
            bidUser = new BidUserVO();
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue(); // 手机号
            Double bidMoney = next.getScore(); // 金额
            bidUser.setPhone(phone);
            bidUser.setBidMoney(bidMoney);
            list.add(bidUser);

        }
        return list;
    }


}