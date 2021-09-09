package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 * ClassName:FinanceAccountService
 * Package:com.bjpowernode.p2p.service.user
 * Description: 描述信息
 *
 * @date:2021/8/31 16:20
 * @author:sjf
 * @Copyright:动力节点
 */
public interface FinanceAccountService {
    FinanceAccount queryFinanceAccountByUid(Integer uId);
}
