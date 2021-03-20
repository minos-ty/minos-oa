package com.minos.oa.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author minos
 * @date 2021/3/19 12:56
 */
public class MD5Utils {

    public static String md5Digest(String source) {
        return DigestUtils.md5Hex(source);
    }

    /**
     * 给原始字符串在生成md5前加上盐值混淆,进一步增强数据安全
     *
     * @param source 源数据
     * @param salt   盐值
     * @return md5摘要
     */
    public static String md5Digest(String source, Integer salt) {
        // 字符数据
        char[] ca = source.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
        return DigestUtils.md5Hex(target);
    }

    public static void main(String[] args) {
        System.out.println(md5Digest("test", 188));
    }
}
