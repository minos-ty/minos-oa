package com.minos.oa.dao;

import com.minos.oa.entity.Department;

/**
 * @author minos
 * @date 2021/3/19 11:51
 */
public interface DepartmentDao {
    public Department selectById(Long departmentId);
}
