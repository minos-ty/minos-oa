package com.minos.oa.dao;

import com.minos.oa.entity.ProcessFlow;

import java.util.List;

/**
 * @author minos
 * @date 2021/3/19 15:30
 */
public interface ProcessFlowDao {
    public void insert(ProcessFlow form);
    public void update(ProcessFlow processFlow);
    public List<ProcessFlow> selectByFormId(Long formId);
}
