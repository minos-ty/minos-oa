package com.minos.oa.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author minos
 * @date 2021/3/19 14:07
 */
public class MD5UtilsTest {

    @Test
    public void md5Digest() {
        System.out.println(MD5Utils.md5Digest("test"));
    }
}