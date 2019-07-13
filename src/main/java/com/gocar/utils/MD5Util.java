package com.gocar.utils;

import org.springframework.util.DigestUtils;

/**
 * MD5加密工具
 * 
 */
public class MD5Util {
    //盐值
    private static final String salt = "ASsa435rDASD983/.,l7204/.,c.z/p[`0-1`d2305./]";
    public static String getMD5(String str){
        String value =  str + "/" + salt;
        return DigestUtils.md5DigestAsHex(value.getBytes());
    }
}
