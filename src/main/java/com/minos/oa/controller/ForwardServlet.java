package com.minos.oa.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于页面跳转的servlet
 * @author minos
 * @date 2021/3/19 22:12
 */
@WebServlet( name = "/ForwardServlet", urlPatterns = "/forward/*")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        /**
         * /forward/form
         * /forward/a/b/c/from
         */
        String subUri = uri.substring(1);
        String page = subUri.substring(subUri.indexOf("/"));
        req.getRequestDispatcher(page + ".ftl").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
