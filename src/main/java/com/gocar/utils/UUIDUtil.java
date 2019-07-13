package com.gocar.utils;

import java.util.UUID;

public class UUIDUtil {


    public static String getUUIDByAPI(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getUUIDByTime(){
        String str = "";
        for (int i = 0; i < 4; i++) {
            str += (int)(Math.random()*10);
        }
        return System.currentTimeMillis()+str;
    }

}
