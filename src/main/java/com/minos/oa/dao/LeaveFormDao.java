package com.minos.oa.dao;

import com.minos.oa.entity.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author minos
 * @date 2021/3/19 15:30
 */
public interface LeaveFormDao {
    public void insert(LeaveForm form);
    public List<Map> selectByParams(@Param("pf_state") String pfState, @Param("pf_operator_id") Long operatorId);
    public LeaveForm selectById(Long formId);
    public void update(LeaveForm leaveForm);
}
