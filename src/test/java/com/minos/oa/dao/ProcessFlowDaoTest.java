package com.minos.oa.dao;

import com.minos.oa.entity.ProcessFlow;
import com.minos.oa.utils.MybatisUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @author minos
 * @date 2021/3/19 16:19
 */
public class ProcessFlowDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow processFlow = new ProcessFlow();
            processFlow.setFormId(3L);
            processFlow.setOperatorId(2L);
            processFlow.setAction("audit");
            processFlow.setReason("approved");
            processFlow.setReason("同意");
            processFlow.setCreateTime(new Date());
            processFlow.setAuditTime(new Date());
            processFlow.setOrderNo(1);
            processFlow.setState("ready");
            processFlow.setIsLast(1);
            processFlowDao.insert(processFlow);
            return null;
        });
    }
}