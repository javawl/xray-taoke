package com.xray.taoke.jdapi.vo;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.util.HttpUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.util.UuidUtil;

public class ConstTkjd {
    public static final Prop config = PropKit.use("config.properties");

    public static final boolean shorturl_mode = config.getBoolean("shorturl.mode", false);

    public static final int para_err = -100;
    public static final int data_err = -101;
    public static final int notaoke_err = -102;
    public static final int parse_err = -103;

    private static final Pattern url_pattern = Pattern.compile("(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))");
    private static final Pattern itemid_pattern = Pattern.compile("id=(\\d+)");
    private static final Pattern tradeid_pattern = Pattern.compile("^(\\d{14,})$");
    private static final Pattern tkpwd_pattern = Pattern.compile("([a-zA-Z0-9]{11})");
    private static final DecimalFormat num_format = new DecimalFormat("0.0#");

    public static String formatPrice(double d) {
        return num_format.format(d);
    }

    public static String toShortUrl(String url) {
        try {
            if (!shorturl_mode)
                return url;
            String sendGet = HttpUtil.sendGet("http://api.t.sina.com.cn/short_url/shorten.json?source=222700405&url_long=" + URLEncoder.encode(url, "UTF-8"));
            JSONArray jsonArray = JSONArray.parseArray(sendGet);
            return JSONObject.parseObject(jsonArray.get(0).toString()).getString("url_short");
        } catch (Exception e) {
        }
        return "";
    }

    public static double getFlrate(double tkmoney) {
        if (tkmoney < 2)
            return 0.81;
        else if (tkmoney < 100)
            return 0.61;
        return 0.51;
    }

    public static double getJiemoney(double tkmoney) {
        return tkmoney * getFlrate(tkmoney);
    }

    public static String getUuid16() {
        return UuidUtil.getUuidByJdk(true).substring(8, 24);
    }

    public static String parseUrl(String text) {
        Matcher m = url_pattern.matcher(text);
        if (!m.find())
            return null;
        String url = m.group(0);
        if (url.contains("t.cn/"))
            return null;
        return url;
    }

    public static String parseTbUrl(String content) {
        String text = parseUrl(content);
        if (StringUtil.isEmpty(text))
            return null;
        if (text.contains("taobao.com") || text.contains("tmall.com"))
            return text;
        return null;
    }

    public static String parseItemid(String text) {
        if (text.contains("taobao.com") || text.contains("tmall.com")) {
            Matcher m = itemid_pattern.matcher(text);
            if (m.find())
                return m.group(1);
        }
        return null;
    }

    public static String parseTradeid(String text) {
        Matcher m = tradeid_pattern.matcher(text);
        if (m.find())
            return m.group(0);
        return null;
    }

    public static String parseTkpwd(String text) {
        if (parseTradeid(text) != null)
            return null;
        Matcher m = tkpwd_pattern.matcher(text);
        if (m.find())
            return "￥" + m.group(0) + "￥";
        return null;
    }

}