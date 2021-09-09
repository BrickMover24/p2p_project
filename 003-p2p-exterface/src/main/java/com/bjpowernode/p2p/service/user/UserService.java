package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.User;

/**
 * User相关的接口
 */
public interface UserService {

    /**
     * 查询平台的用户数量
     * @return
     */
    Integer queryAllUserCount();


    /**
     * 根据手机号查询User
     * @param phone
     * @return
     */
    User checkPhone(String phone);


    /**
     * 注册用户
     * @param phone
     * @param loginPassword
     * @return
     */
    User register(String phone, String loginPassword) throws Exception;


    /**
     * 实名认证后更新用户的真实姓名和身份证号
     * @param user
     * @return
     */
    int modifyUser(User user);


    /**
     * 登录验证
     * @param phone
     * @param loginPassword
     * @return
     */
    User queryUserByPhoneAndPwd(String phone, String loginPassword);
}
