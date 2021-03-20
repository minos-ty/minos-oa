package com.minos.oa.service;

import com.minos.oa.dao.DepartmentDao;
import com.minos.oa.entity.Department;
import com.minos.oa.utils.MybatisUtils;

/**
 * @author minos
 * @date 2021/3/19 11:55
 */
public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MybatisUtils.executeQuery(sqlSession -> {
            DepartmentDao departmentDao = sqlSession.getMapper(DepartmentDao.class);
            return departmentDao.selectById(departmentId);
        });
    }
}
