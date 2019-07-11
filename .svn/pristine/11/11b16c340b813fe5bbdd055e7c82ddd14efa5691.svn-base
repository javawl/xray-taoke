package com.xray.taoke.admin.common;

import java.io.File;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.xray.admin.common.Constant;

public class AliyunOssKit {
    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    
    public static void main(String[] args) {
        AliyunOssKit.getDefault().upload("a.png", "C:/Users/xrbo/Desktop/a.png");
        System.out.println("success");
    }

    public static AliyunOssKit getDefault() {
        AliyunOssKit oss = new AliyunOssKit();
        oss.bucketName = Constant.config.get("alicdn.bucketName");
        oss.endpoint = Constant.config.get("alicdn.endpoint");
        oss.accessKeyId = Constant.config.get("alicdn.accessKeyId");
        oss.accessKeySecret = Constant.config.get("alicdn.accessKeySecret");
        return oss;
    }

    public void upload(String key, File file) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(new PutObjectRequest(bucketName, key, file));
        ossClient.shutdown();
    }

    public void upload(String key, String file) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(new PutObjectRequest(bucketName, key, new File(file)));
        ossClient.shutdown();
    }

    public void uploadDir(String base, String dir) {
        File[] files = new File(base + "/" + dir).listFiles();
        if (files.length <= 0)
            return;

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        for (File file : files) {
            ossClient.putObject(new PutObjectRequest(bucketName, dir + "/" + file.getName(), file));
        }
        ossClient.shutdown();
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

}
