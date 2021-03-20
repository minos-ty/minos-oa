package com.minos.oa.service;

import com.minos.oa.dao.RbacDao;
import com.minos.oa.dao.UserDao;
import com.minos.oa.entity.Node;
import com.minos.oa.entity.User;
import com.minos.oa.service.exception.BusinessException;
import com.minos.oa.utils.MD5Utils;

import java.util.List;

/**
 * @author minos
 * @date 2021/3/18 22:58
 */
public class UserService {
    private UserDao userDao = new UserDao();
    private RbacDao rbacDao = new RbacDao();

    /**
     * 根据前台输入进行登录校验
     *
     * @param username 前台输入的用户名
     * @param password 前台输入的密码
     * @return 校验通过后, 返回包含对应用户数据的User实体类
     */
    public User checkLogin(String username, String password) {
        User user = userDao.selectByUsername(username);

        if (user == null) {
            // 抛出用户不存在的异常
            throw new BusinessException("L001", "用户名不存在");
        }

        // 对前台输入的密码加盐混淆后生成MD5,与保存在数据库中的MD5密码进行比较
        String md5 = MD5Utils.md5Digest(password, user.getSalt());
        if (!md5.equals(user.getPassword())) {
            throw new BusinessException("L002", "密码错误");
        }

        // 走到这里还没抛出异常,代表用户名和密码都匹配,登录成功
        return user;
    }

    public List<Node> selectNodeByUserId(Long userId) {
        return rbacDao.selectNodeByUserId(userId);
    }
}
