package com.minos.oa.test;

import com.minos.oa.utils.MybatisUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author minos
 * @date 2021/3/18 17:33
 */
@WebServlet(name =  "/testServlet", urlPatterns = "/test")
public class testServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        req.setAttribute("result",result);
        req.getRequestDispatcher("/test.ftl").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
