package com.xray.taoke.admin.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageHaiBao2Utill {
    private Graphics2D g = null;

    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {

        try {
            int w = b.getWidth();
            int h = b.getHeight();
            g = d.createGraphics();
            g.drawImage(b, 600, -600, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return d;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "png", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String generateCode(String path, String projectUrl, String codeUrl) {
        Font font = new Font("微软雅黑", Font.PLAIN, 30);// 添加字体的属性设置
        // String projectUrl = PathKit.getWebRootPath() + "/upload/codeimg/";
        long sys = System.currentTimeMillis();
        String imgName = path + sys + ".png";
        String path2 = "upload/haibao/" + sys + ".png";
        try {
            // String imageLocalUrl = projectUrl + "6902a789a1ad0b7b5764c037aac39a2.jpg";
            BufferedImage imageLocal = ImageIO.read(new File(projectUrl));
            // 加载用户的二维码
            BufferedImage imageCode = ImageIO.read(new File(codeUrl));
            Graphics2D g = imageLocal.createGraphics();
            // 在模板上添加用户二维码(地址,左边距,上边距,图片宽度,图片高度,未知)
            g.drawImage(imageCode, imageLocal.getWidth() - 180, imageLocal.getHeight() - 170, 150, 150, null);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.dispose();
            // 获取新文件的地址
            File outputfile = new File(imgName);
            // 生成新的合成过的用户二维码并写入新图片
            ImageIO.write(imageLocal, "png", outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回给页面的图片地址(因为绝对路径无法访问)
        // imgName = Constants.PROJECT_URL + "codeImg/" + userId + ".png";

        return path2;
    }

    public static void main(String[] args) {
        String codeUrl = "C://Users/wenht/Desktop/testQrcode/20190515163621.jpg";
        String projectUrl = "D://迅雷下载/phantomjs-2.1.1-windows/img/1557910582017_jietu.png";
        String s = generateCode("D://project/svn-ldpro/xray-taoke/xray-taoke-admin/src/main/webapp/upload/codeimg/", projectUrl, codeUrl);
        System.out.println(s);
    }
}
