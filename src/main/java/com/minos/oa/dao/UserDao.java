package com.minos.oa.dao;

import com.minos.oa.entity.User;
import com.minos.oa.utils.MybatisUtils;

/**
 * 用户表dao
 * @author minos
 * @date 2021/3/18 22:52
 */
public class UserDao {
    /**
     * 按用户名查询用户表
     * @param username 用户名
     * @return User对象包含对应的用户信息,null则代表对象不存在
     */
    public User selectByUsername(String username) {
        User user =  (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername", username));
        return user;
    }
}
