package com.minos.oa.dao;

import com.minos.oa.entity.Employee;
import org.apache.ibatis.annotations.Param;

/**
 * @author minos
 * @date 2021/3/19 11:22
 */
public interface EmployeeDao {
    public Employee selectById(Long employeeId);

    /**
     *  根据传入员工对象获取上级主管对象
     * @param employee 员工对象
     * @return 上级主管对象
     */
    public Employee selectLeader(@Param("emp") Employee employee);
}
