package com.minos.oa.service;

import com.minos.oa.entity.LeaveForm;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author minos
 * @date 2021/3/19 20:46
 */
public class LeaveFormServiceTest {
    LeaveFormService leaveFormService = new LeaveFormService();

    @Test
    public void createLeaveFrom1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(8L);
        form.setStartTime(sdf.parse("202003 26 08"));
        form.setEndTime(sdf.parse("2020040118"));
        form.setFormType(1);
        form.setReason("市场部员工请假(72小时以上)");
        form.setCreateTime(new Date());
        LeaveForm savedFrom = leaveFormService.createLeaveFrom(form);
        System.out.println(savedFrom.getFormId());

    }
}