package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询用户总数量
     * @return
     */
    Integer selectAllUserCount();


    /**
     * 根据phone 查询User
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);


    /**
     * 验证登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User selectUserByPhoneAndPwd(String phone, String loginPassword);
}