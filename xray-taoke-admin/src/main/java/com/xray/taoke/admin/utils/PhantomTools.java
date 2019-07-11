package com.xray.taoke.admin.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * @Description:根据网页地址转换成图片
 * @Author: admin
 * @CreateDate: 2018年6月22日
 */
public class PhantomTools {
    private static String tempPath = "/usr/local/xray-taoke-admin/webroot/upload/haibao";// 图片保存目录
    // private static String tempPath = "D://迅雷下载/phantomjs-2.1.1-windows/img";//
    // 图片保存目录

    // usr/local/xray-taoke-admin/webroot/upload/img;D:/迅雷下载/phantomjs-2.1.1-windows/img

    private static String BLANK = " ";
    // 下面内容可以在配置文件中配置
    private static String binPath = "/usr/local/phantomjs/bin/phantomjs";
    // 插件引入地址
    // private static String binPath =
    // "D://迅雷下载/phantomjs-2.1.1-windows/bin/phantomjs.exe";// 插件引入地址

    // usr/local;;;D://迅雷下载/phantomjs-2.1.1-windows

    private static String jsPath = "/usr/local/phantomjs/examples/rasterize.js";// js引入地址
    // private static String jsPath =
    // "D://迅雷下载/phantomjs-2.1.1-windows/examples/rasterize.js";// js引入地址

    // 执行cmd命令
    public static String cmd(String imgagePath, String url) {
        System.out.println(binPath + BLANK + jsPath + BLANK + url + BLANK + imgagePath);
        return binPath + BLANK + jsPath + BLANK + url + BLANK + imgagePath;
    }

    // 关闭命令
    public static void close(Process process, BufferedReader bufferedReader) throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

    /**
     * @param userId
     * @param url
     * @return
     * @throws IOException
     */
    public static String printUrlScreen2jpg(String url) throws IOException {
        long sys = System.currentTimeMillis();
        String imgagePath = tempPath + "/" + sys + "_jietu" + ".png";// 图片路径
        // String imgPath = "upload/haibao/" + sys + ".png";
        String imgPath = "/usr/local/xray-taoke-admin/webroot/upload/haibao/" + sys + "_jietu" + ".png";

        // Java中使用Runtime和Process类运行外部程序
        Process process = Runtime.getRuntime().exec(cmd(imgagePath, url));
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                // close(process, reader);
                sb.append(tmp);
            }
        } finally {
            close(process, reader);
            // TODO: handle finally clause
        }
        System.out.println("success");
        return imgPath;
    }

    public static void main(String[] args) throws IOException {
        // String url =
        // "http://apr8.ldplaza.com/sq/tshopimg?sid=1&pkey=5&lid=ff2044f1b9515ea2";
        // String path = PhantomTools.printUrlScreen2jpg(url);
        String path_url = "D://迅雷下载/phantomjs-2.1.1-windows/img/1557910582017_jietu.png";
        File file = new File(path_url);
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        System.out.println(sourceImg.getHeight());
        System.out.println(sourceImg.getWidth());
    }
}