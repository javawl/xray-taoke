package com.xray.taoke.act.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xray.taoke.act.utils.ParamUtils.UrlEntity;

public class SkuidUtils {
    public static String urlCommon(String url) {
        String location = "";
        try {
            StringBuffer buffer = new StringBuffer();

            // 发送get请求
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            // 必须设置false，否则会自动redirect到重定向后的地址
            conn.setInstanceFollowRedirects(false);
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.addRequestProperty("Referer", "http://matols.com/");
            conn.connect();

            // 判定是否会进行302重定向
            if (conn.getResponseCode() == 302) {
                // 如果会重定向，保存302重定向地址，以及Cookies,然后重新发送请求(模拟请求)
                location = conn.getHeaderField("Location");
                String cookies = conn.getHeaderField("Set-Cookie");

                serverUrl = new URL(location);
                conn = (HttpURLConnection) serverUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                conn.addRequestProperty("Referer", "http://matols.com/");
                conn.connect();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;

    }

    public static String getCode(String url) {
        String code = url.substring(url.lastIndexOf("/") + 1, url.length());
        return code;
    }

    public static String getSkuId(String url) {
        String str = com.jfinal.weixin.sdk.utils.HttpUtils.get(url);
        String code = getCode(url);
        int i = str.indexOf("https://u.jd.com/jda?e=");
        if (i == -1)
            return null;

        int y = str.indexOf(code);

        if (y == -1)
            return null;
        url = str.substring(i, y + 6);
        String redirct_url = urlCommon(url);
        UrlEntity urlEntity = ParamUtils.parseParam(redirct_url);
        String skuid = urlEntity.params.get("sku");
        return skuid;
    }
}
