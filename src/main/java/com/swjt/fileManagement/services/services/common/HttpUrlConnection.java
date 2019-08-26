package com.swjt.fileManagement.services.services.common;



import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpUrlConnection {
    public static String get(String router) {
        String message = "";
        try {
            URL url = new URL(router);
            //得到connection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //超时时间
            connection.setConnectTimeout(5 * 1000);
            //连接
            connection.connect();
            //得到状态码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                byte[] data = new byte[1024];
                StringBuffer sb = new StringBuffer();
                int length = 0;
                //将流转为字符串
                while ((length = inputStream.read(data)) != -1) {
                    String s = new String(data, Charset.forName("utf-8"));
                    sb.append(s);
                }
                message = sb.toString();
                //关闭流
                inputStream.close();
                //关闭连接
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    public static String doJsonPost(String urlPath, JSONObject Json) {
        String message = "";
        try {
            // 创建url资源
            URL url = new URL(urlPath);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);

            conn.setDoInput(true);

            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = (Json.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));

            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");


            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            out.write((Json.toString()).getBytes());
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in = conn.getInputStream();

                try {
                    byte[] data1 = new byte[in.available()];
                    in.read(data1);
                    // 转成字符串
                    message = new String(data1);
                    System.out.println(message);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                System.out.println("no++");
            }

        } catch (Exception e) {

        }
        return message;

    }
}
