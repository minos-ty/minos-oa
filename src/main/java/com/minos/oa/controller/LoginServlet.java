package com.minos.oa.controller;

import com.alibaba.fastjson.JSON;
import com.minos.oa.entity.User;
import com.minos.oa.service.UserService;
import com.minos.oa.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author minos
 * @date 2021/3/19 09:04
 */
@WebServlet(name = "/LoginServlet", urlPatterns = "/check_login")
public class LoginServlet extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> result = new HashMap<>();

        try {
            User user = userService.checkLogin(username, password);
            // 把登录的用户信息存储到session中
            HttpSession session = req.getSession();
            session.setAttribute("login_user",user);

            result.put("code", "0");
            result.put("message", "success");
            result.put("redirect_url", "/index");
        }catch (BusinessException exception) {
            logger.error(exception.getMessage(), exception);
            result.put("code", exception.getCode());
            result.put("message", exception.getMessage());
        }catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            result.put("code", exception.getClass().getSimpleName());
            result.put("message", exception.getMessage());
        }

        //利用fastJson把数据转换成json格式响应给前端
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
