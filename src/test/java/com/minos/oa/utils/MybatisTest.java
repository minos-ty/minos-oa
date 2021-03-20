package com.minos.oa.utils;

import org.junit.Test;

/**
 * @author minos
 * @date 2021/3/18 16:25
 */
public class MybatisTest {

    @Test
    public void testcase1() {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> {
            String out = (String) sqlSession.selectOne("test.sample");
            return out;
        });

        System.out.println(result);
    }

    @Test
    public void testcase2(){
        String result = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        System.out.println(result);
    }
}
