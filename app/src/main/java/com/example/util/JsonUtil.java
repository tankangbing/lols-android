package com.example.util;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    /**
     * 根据ob对象生成Json
     * @param ob
     * @return
     */
    public static String BuildJson(Object ob) {
        String s = JSON.toJSONString(ob);
        return s;
    }

    /**
     * 根据json生成clazz类对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parserString(String json, Class<T> clazz) {

        return JSON.parseObject(json, clazz);
    }
    /**
     * 根据json生成clazz类的list对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T>  parserStringtoList(String json, Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }

   /*
     * 将字符串转成可以被解析的字符串
     * @param str
     * @return
     */
    public static String StringToObject(String str){
        str = str.replace("\\","");
        str = str.substring(1, str.length()-1);
        return str;
    }
}
