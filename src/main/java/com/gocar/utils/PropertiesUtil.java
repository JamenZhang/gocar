package com.gocar.utils;

import java.io.IOException;
import java.util.Properties;


public class PropertiesUtil {

    static private Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getResourceAsStream("/myConfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static String getValue(String key){
        return properties.getProperty(key);
    }


}
