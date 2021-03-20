package com.minos.oa.controller;

import com.minos.oa.entity.Department;
import com.minos.oa.entity.Employee;
import com.minos.oa.entity.Node;
import com.minos.oa.entity.User;
import com.minos.oa.service.DepartmentService;
import com.minos.oa.service.EmployeeService;
import com.minos.oa.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author minos
 * @date 2021/3/19 10:51
 */
@WebServlet(name = "/IndexServlet", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService = new UserService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("login_user");
        // 通过用户信息获取到对应的员工信息,并把信息存放大到session中
        Employee employee = employeeService.selectById(user.getEmployeeId());
        session.setAttribute("current_employee", employee);
        // 通过查询的的员工信息查询该员工所在部门的信息,放到session中
        Department department = departmentService.selectById(employee.getDepartmentId());
        session.setAttribute("current_department", department);

        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());
        req.setAttribute("node_list", nodeList);
        req.getRequestDispatcher("/index.ftl").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
