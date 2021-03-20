package com.minos.oa.controller;

import com.alibaba.fastjson.JSON;
import com.minos.oa.dao.LeaveFormDao;
import com.minos.oa.entity.LeaveForm;
import com.minos.oa.entity.User;
import com.minos.oa.service.LeaveFormService;
import com.minos.oa.utils.MybatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author minos
 * @date 2021/3/19 21:09
 */
@WebServlet(name = "/LeaveFormServlet", urlPatterns = "/leave/*")
public class LeaveFormServlet extends HttpServlet {

    private LeaveFormService leaveFormService = new LeaveFormService();
    private Logger logger = LoggerFactory.getLogger(LeaveFormServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        // http://localhost/leave/create  类似这个地址我们要去最后一个单词确定具体执行的逻辑
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        if ("create".equals(methodName)) {
            this.create(req, resp);
        } else if ("list".equals(methodName)) {
            this.getLeaveFormList(req, resp);
        } else if ("audit".equals(methodName)) {
            this.audit(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * 创建请假单
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.接收请假单的各项数据
        HttpSession session = req.getSession();
        User User = (User) session.getAttribute("login_user");
        String formType = req.getParameter("formType");
        String strStartTime = req.getParameter("startTime");
        String strEndTime = req.getParameter("endTime");
        String reason = req.getParameter("reason");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        Map<String, String> result = new HashMap<>();
        try {
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(User.getUserId());
            form.setStartTime(sdf.parse(strStartTime));
            form.setEndTime(sdf.parse(strEndTime));
            form.setFormType(Integer.parseInt(formType));
            form.setReason(reason);
            form.setCreateTime(new Date());
            // 2.调用业务逻辑方法
            leaveFormService.createLeaveFrom(form);
            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请假申请异常", e);
            result.put("code", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
        }
        // 3.组织响应数据
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    /**
     * 查询需要审核的请假单列表
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getLeaveFormList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("login_user");
        List<Map> fromList = leaveFormService.getLeaveFromList("process", user.getEmployeeId());
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "");
        result.put("count", fromList.size());
        result.put("data", fromList);

        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);

    }

    /**
     * 处理审批操作
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void audit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String formId = req.getParameter("formId");
        String result = req.getParameter("result");
        String reason = req.getParameter("reason");
        User user = (User) req.getSession().getAttribute("login_user");

        Map mpResult = new HashMap();
        try {
            leaveFormService.audit(Long.parseLong(formId), user.getEmployeeId(), result, reason);
            mpResult.put("code", "0");
            mpResult.put("message", "success");
        } catch (Exception e) {
            logger.error("请假单审核失败", e);
            mpResult.put("code", e.getClass().getSimpleName());
            mpResult.put("message", e.getMessage());
        }
        String json = JSON.toJSONString(mpResult);
        resp.getWriter().println(json);
    }

}
