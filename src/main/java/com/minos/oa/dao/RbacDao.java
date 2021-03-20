package com.minos.oa.dao;

import com.minos.oa.entity.Node;
import com.minos.oa.utils.MybatisUtils;

import java.util.List;

/**
 * @author minos
 * @date 2021/3/19 10:33
 */
public class RbacDao {
    public List<Node> selectNodeByUserId(Long userId) {
        return (List) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId", userId));
    }
}
