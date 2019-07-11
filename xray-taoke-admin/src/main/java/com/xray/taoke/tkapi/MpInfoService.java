package com.xray.taoke.tkapi;

import java.net.URL;
import java.util.Map;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.exception.RtException;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;
import com.xray.taoke.tkapi.vo.ConstTk;

public class MpInfoService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".mpinfo.";
    private static final String linkinfo = config.get("redis.prefix") + ".linkinfo.";

    public static MpInfoService instance = new MpInfoService();

    public static void main(String[] args) {
        System.out.println(instance.getShortUrl("https://www.runoob.com/java/java-url-processing.html"));
    }

    public String getLongUrl(String id) {
        String key = linkinfo + "url." + id;
        return cache.get(key);
    }

    public String getShortUrl(String url) {
        try {
            String id = "g" + cache.incr(linkinfo + "url.uuid");
            String key = linkinfo + "url." + id;
            cache.set(key, url);
            String host = new URL(url).getHost();
            return "http://" + host + "/sq/" + id;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> getLinkConfig(String pkey) {
        String key = linkinfo + "config." + pkey;
        return cache.hgetAll(key);
    }

    public Map<String, String> getMpInfo(String appid) {
        String key = prefix + appid;
        Map<String, String> map = cache.hgetAll(key);
        if (map.isEmpty())
            throw new RtException(ConstTk.para_err, "empty mpinfo,appid:" + appid);
        return map;
    }

    public String getSid(String appid) {
        String key = prefix + appid;
        String val = cache.hget(key, "sid");
        if (StringUtil.isEmpty(val))
            throw new RtException(ConstTk.data_err);
        return val;
    }

    public String getAppid(String sid) {
        String key = prefix + sid;
        String val = cache.hget(key, "appid");
        if (StringUtil.isEmpty(val))
            throw new RtException(ConstTk.data_err);
        return val;
    }

    public String getUrl(String appid) {
        String key = prefix + appid;
        return cache.hget(key, "url");
    }

}
