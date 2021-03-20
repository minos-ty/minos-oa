package com.minos.oa.service;

import com.minos.oa.entity.Node;
import com.minos.oa.entity.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author minos
 * @date 2021/3/18 23:10
 */
public class UserServiceTest {

    private UserService userService = new UserService();

    @Test
    public void checkLogin1() {
        userService.checkLogin("uu", "1234");
    }

    @Test
    public void checkLogin2() {
        userService.checkLogin("m8", "1234");
    }

    @Test
    public void checkLogin3() {
        User user = userService.checkLogin("m8", "test");
        System.out.println(user);
    }

    @Test
    public void checkLogin4() {
        List<Node> nodes = userService.selectNodeByUserId(1L);
        System.out.println(nodes);
    }

}