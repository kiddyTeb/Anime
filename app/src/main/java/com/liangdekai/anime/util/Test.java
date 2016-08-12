package com.liangdekai.anime.util;

import android.webkit.WebHistoryItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by asus on 2016/8/11.
 */
public class Test {
    private void httpUrlConnPost(String name , String password){
        HttpURLConnection httpURLConnection = null ;
        URL url = null ;
        try{
            url = new URL("http://192.168.1.105:8080/KYDServer/servlet/Login");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);//设置这个连接是否可以被重定向
            httpURLConnection.setDoInput(true);//设置这个连接是否可以写入数据
            httpURLConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            httpURLConnection.connect();//连接
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user" , URLEncoder.encode(name , "UTF-8"));
            jsonObject.put("passsword" , URLEncoder.encode(password , "UTF-8"));
            String json = jsonObject.toString();//把JSON对象按JSON的编码格式转换为字符串
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(json);
            writer.flush();
            outputStream.close();
            writer.close();

            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK){
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String str = null ;
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                inputStream.close();
                reader.close();
                JSONObject object = new JSONObject(buffer.toString());
                boolean result = object.getBoolean("json");//从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数
                if (result){
                    //处理成功返回数据的操作
                }else{
                    //处理返回失败之后的操作
                }
            }else {
                //处理返回失败后的结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            httpURLConnection.disconnect();
        }
    }
}
