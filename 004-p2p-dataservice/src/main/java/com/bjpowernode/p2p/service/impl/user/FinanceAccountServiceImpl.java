package com.bjpowernode.p2p.service.impl.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName:FinanceAccountServiceImpl
 * Package:com.bjpowernode.p2p.service.impl.user
 * Description: 描述信息
 *
 * @date:2021/8/31 16:21
 * @author:sjf
 * @Copyright:动力节点
 */
@Component
@Service(interfaceClass = FinanceAccountService.class, version = "1.0.0", timeout = 15000)
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer uId) {
        return financeAccountMapper.selectByUid(uId);
    }
}
