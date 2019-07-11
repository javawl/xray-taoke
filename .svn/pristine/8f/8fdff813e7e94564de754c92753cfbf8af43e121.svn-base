package com.xray.taoke.admin.service;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;

public class AiService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".ai.";

    public static AiService instance = new AiService();

    public String authUrl(String appid, String openid, String url) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("click", "false");
        data.put("appid", appid);
        data.put("openid", openid);
        data.put("url", url);
        String aid = buildAid();
        setAidValue(aid, data);
        return authUrl() + "/auth/goBind?aid=" + aid + "&xa=" + (int) Math.random() * 1000;
    }

    public void setAidValue(String aid, Map<String, String> map) {
        String key = prefix + "aid." + aid;
        cache.hmset(key, map);
    }

    public void setAidValue(String aid, String field, String value) {
        String key = prefix + "aid." + aid;
        cache.hset(key, field, value);
    }

    public String buildAid() {
        String key = prefix + "aid";
        return String.valueOf(cache.incr(key));
    }

    public String authUrl() {
        return config.get("auth.url");
    }

}
