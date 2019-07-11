package com.xray.taoke.admin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.admin.util.HttpUtil;
import com.xray.taoke.admin.service.LinkInfoService;

public class ShortUrlUtils {
    private static Logger logger = LoggerFactory.getLogger(ShortUrlUtils.class);
    public static ShortUrlUtils instance = new ShortUrlUtils();

    public String toShorUrl(String url) {
        String sendGet = "";
        try {
            sendGet = HttpUtil.sendGet("http://api.t.sina.com.cn/short_url/shorten.json?source=222700405&url_long=" + URLEncoder.encode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("exception shorturl:{}", e);
        }
        JSONArray jsonArray = JSONArray.parseArray(sendGet);
        String url_short = JSONObject.parseObject(jsonArray.get(0).toString()).getString("url_short");
        return url_short;
    }

    public String domainToShorUrl(String url, String domain) {
        String id = LinkInfoService.instance.encodeUrl(url);
        return "http://" + domain.substring(domain.indexOf(".") + 1) + "/" + id;
    }

    public static void main(String[] args) {
        // ShortUrlUtils.instance.domainToShorUrl(url, domain)
    }

}
