package com.minos.oa.dao;

import com.minos.oa.entity.LeaveForm;
import com.minos.oa.utils.MybatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.*;

/**
 * @author minos
 * @date 2021/3/19 15:44
 */
public class LeaveFormDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);

            LeaveForm leaveForm = new LeaveForm();
            leaveForm.setEmployeeId(4L);
            leaveForm.setFormType(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = sdf.parse("2020-8-2 12:5:21");
                endTime = sdf.parse("2024-7-8 11:3:4");
            }catch (ParseException e) {
                e.printStackTrace();
            }
            leaveForm.setStartTime(startTime);
            leaveForm.setEndTime(endTime);
            leaveForm.setReason("回家");
            leaveForm.setCreateTime(new Date());
            leaveForm.setState("processing");
            leaveFormDao.insert(leaveForm);
            return null;
        });
    }
}