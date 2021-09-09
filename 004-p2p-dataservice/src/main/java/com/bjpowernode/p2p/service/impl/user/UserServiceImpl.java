package com.bjpowernode.p2p.service.impl.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.user.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = UserService.class, version = "1.0.0", timeout = 15000)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Integer queryAllUserCount() {
        Integer allUserCount = (Integer) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
        if (!ObjectUtils.allNotNull(allUserCount)) {
            synchronized (this) {
                allUserCount = (Integer) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
                if (!ObjectUtils.allNotNull(allUserCount)) {
                    allUserCount = userMapper.selectAllUserCount();
                    redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT,allUserCount,7, TimeUnit.DAYS);
                    System.out.println("数据库获取人数");
                }else {
                    System.out.println("缓存获取人数");
                }
            }
        }else {
            System.out.println("缓存获取人数");
        }
        return allUserCount;
    }

    @Override
    public User checkPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    /**
     * 注册用户
     * 注册用户的逻辑：
     *          1. 给用户表新增一条数据，然后再根据该用户的ID 新增一个账户信息，账户的余额是888
     * @param phone
     * @param loginPassword
     * @return
     */
    @Transactional
    @Override
    public User register(String phone, String loginPassword) throws Exception {
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int userRows = userMapper.insertSelective(user);
        if (userRows == 0) {
            throw new Exception("添加用户失败");
        }
        // 再次查询数据库
        //User userDetail = userMapper.selectUserByPhone(phone);

        // 新增账户，初始化余额是888
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(user.getId());
        financeAccount.setAvailableMoney(888.0);
        int faRows = financeAccountMapper.insertSelective(financeAccount);
        if (faRows == 0) {
            throw new Exception("添加账户失败");
        }

        return user;
    }


    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    public int modifyUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User queryUserByPhoneAndPwd(String phone, String loginPassword) {
        return userMapper.selectUserByPhoneAndPwd(phone, loginPassword);
    }
}
