package com.minos.oa.service;

import com.minos.oa.dao.EmployeeDao;
import com.minos.oa.entity.Employee;
import com.minos.oa.utils.MybatisUtils;

/**
 * @author minos
 * @date 2021/3/19 11:28
 */
public class EmployeeService {

    public Employee selectById(Long employeeId) {
        return (Employee) MybatisUtils.executeQuery(sqlSession -> {
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId);
        });
    }
}
