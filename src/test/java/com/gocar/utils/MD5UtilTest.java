package com.gocar.utils;

import org.junit.Test;

/**
 * Created by Ming on 2018/2/9.
 */
public class MD5UtilTest {
    @Test
    public void getMD5() throws Exception {
        String md5 = MD5Util.getMD5("123");
        System.out.println(md5);
    }

}