package com.minos.oa.service;

import com.minos.oa.dao.EmployeeDao;
import com.minos.oa.dao.LeaveFormDao;
import com.minos.oa.dao.NoticeDao;
import com.minos.oa.dao.ProcessFlowDao;
import com.minos.oa.entity.Employee;
import com.minos.oa.entity.LeaveForm;
import com.minos.oa.entity.Notice;
import com.minos.oa.entity.ProcessFlow;
import com.minos.oa.service.exception.BusinessException;
import com.minos.oa.utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请假单流程服务
 *
 * @author minos
 * @date 2021/3/19 16:35
 */
public class LeaveFormService {

    /**
     * 创建请假单
     *
     * @param form 前端输入的请假单数据
     * @return 持久化后的请假单对象
     */
    public LeaveForm createLeaveFrom(LeaveForm form) {
        LeaveForm savedForm = (LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据,8级以下员工表单状态为processing,8级(总经理)状态为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            leaveFormDao.insert(form);

            //2.增加第一条流程数据,说明表单已提交,状态为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(form.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setState("completed");
            flow1.setIsLast(0);
            processFlowDao.insert(flow1);

            //3.分情况创建其余流程数据
            //3.1 7级以下员工,生成部门经理审批任务,请假时间大于72小时,还需生成总经理审批任务
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeDao.selectLeader(employee);
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                // form.getStartTime().getTime() 取时间的毫秒数来相减
                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                // 一千毫秒=一秒 一分钟60秒 一小时60分 得一小时有1000 * 60 * 60毫秒   * 1f:让数据变成浮点型数据
                float hours = diff / (1000 * 60 * 60) * 1f;
                if (hours >= BusinessConstants.MANAGER_AUDIT_HOURS) {
                    // 如果时间大于72小时,则还需要经过总经理审批,先把当前流程持久化
                    flow2.setIsLast(0);
                    processFlowDao.insert(flow2);
                    // 获得部门经理的上级总经理
                    Employee manager = employeeDao.selectLeader(dmanager);
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(new Date());
                    flow3.setState("ready");
                    flow3.setOrderNo(3);
                    flow3.setIsLast(1);
                    processFlowDao.insert(flow3);
                } else {
                    // 如果请假时间不超时72小时,则当前请假流程为最后一个流程
                    flow2.setIsLast(1);
                    processFlowDao.insert(flow2);
                }
                // 请假单以提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交,请等待上级审批.",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));

                //通知部门经理审批消息
                noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(dmanager.getEmployeeId(), noticeContent));
            } else if (employee.getLevel() == 7) { //部门经理
                //3.2 7级员工,生成总经理审批任务
                Employee manager = employeeDao.selectLeader(employee);
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manager.getEmployeeId());
                flow.setAction("audit");
                flow.setCreateTime(new Date());
                flow.setState("process");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);

                // 请假单以提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交,请等待上级审批.",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));

                //通知总经理审批消息
                noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(manager.getEmployeeId(), noticeContent));

            } else if (employee.getLevel() == 8) {
                //3.3 8级员工,生成总经理审批任务,系统自动通过
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("audit");
                flow.setResult("approved");
                flow.setReason("自动通过");
                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());
                flow.setState("complete");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);

                String noticeContent = String.format("您的请假申请[%s-%s]系统已自动批准通过. ",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
            }
            return form;
        });
        return savedForm;
    }

    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     *
     * @param pfState    ProcessFlow任务状态
     * @param operatorId 经办人编号
     * @return 请假单及相关数据
     */
    public List<Map> getLeaveFromList(String pfState, Long operatorId) {
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> formList = leaveFormDao.selectByParams(pfState, operatorId);
            return formList;
        });
    }

    public void audit(Long formID, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            //1.无论同意/驳回,当前任务状态变更为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            List<ProcessFlow> flowList = processFlowDao.selectByFormId(formID);
            if (flowList.size() == 0) {
                throw new BusinessException("PF001", "无效的审批流程");
            }
            //获取当前任务process对象
            List<ProcessFlow> processList = flowList.stream().filter(p -> p.getOperatorId() == operatorId && p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new BusinessException("PF002", "未找到待处理任务");
            } else {
                process = processList.get(0);
                process.setState("complete");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowDao.update(process);
            }


            LeaveFormDao leaveFormDao = sqlSession.getMapper((LeaveFormDao.class));
            LeaveForm form = leaveFormDao.selectById(formID);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            // 表单提交人信息
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            //经办人信息
            Employee operator = employeeDao.selectById(operatorId);
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            //2.如果当前任务是最后一个节点,代表流程结束,更新请假单状态为对应的approved/refused
            if (process.getIsLast() == 1) {
                form.setState(result);
                leaveFormDao.update(form);

                String strResult = null;
                if ("approved".equals(result)) {
                    strResult = "批准";
                } else if ("refused".equals(reason)) {
                    strResult = "驳回";
                }

                // 发给表单提交人的通知
                String noticeContent = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,审批流程已结束",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        operator.getTitle(), operator.getName(), strResult, reason);
                noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent));

                // 发给表单经办人的通知
                noticeContent = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见:%s,审批流程已结束.",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        strResult, reason);

                noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent));

            } else {
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                //3.如果当前状态不是最后一个节点且审批通过,那下一个节点的状态从ready变为process
                if ("approved".equals(result)) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowDao.update(readyProcess);

                    //消息1: 通知表单提交人,部门经理已经审批通过,交由上级继续审批
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见:%s ,请继续等待上级审批",
                            sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent1));

                    //消息2: 通知总经理有新的审批任务
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                    noticeDao.insert(new Notice(readyProcess.getOperatorId(), noticeContent2));

                    //消息3: 通知部门经理(当前经办人),员工的申请单你已批准,交由上级继续审批
                    String noticeContent3 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见:%s,申请转至上级领导继续审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent3));

                } else if ("refused".equals(result)) {
                    //4.如果当前任务不是最后一个节点且审批驳回,则后续所有任务状态变为cancel,请假单状态变为refused
                    for (ProcessFlow p : readyList) {
                        p.setState("cancel");
                        processFlowDao.update(p);
                    }
                    form.setState("refused");
                    leaveFormDao.update(form);

                    //消息1: 通知申请人表单已被驳回
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见:%s,审批流程已结束" ,
                            sdf.format(form.getStartTime()) , sdf.format(form.getEndTime()),
                            operator.getTitle() , operator.getName(),reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(),noticeContent1));

                    //消息2: 通知经办人表单"您已驳回"
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s]您已驳回,审批意见:%s,审批流程已结束" ,
                            employee.getTitle() , employee.getName() , sdf.format( form.getStartTime()) , sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(),noticeContent2));
                }
            }
            return null;
        });
    }
}
