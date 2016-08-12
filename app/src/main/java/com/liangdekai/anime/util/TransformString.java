package com.liangdekai.anime.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class TransformString {
    /**
     * 将中文转换为UTF-8格式
     */
    public static String transform(String trans){
        try {
            //进行URL编码
            return URLEncoder.encode(trans,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
