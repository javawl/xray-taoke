package com.xray.taoke.act.service;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.UuidUtil;
import com.xray.taoke.act.common.Constant;

public class AiService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".ai.";

    public static AiService instance = new AiService();

    public static void main(String[] args) {
        System.out.println(UuidUtil.getUuidByJdk(false));
    }

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

    public boolean isClick(String aid) {
        String val = getAidValue(aid, "click");
        if (Boolean.parseBoolean(val))
            return true;
        setAidValue(aid, "click", "true");
        return false;
    }

    public String getUrl(String aid) {
        String key = prefix + "aid." + aid;
        return cache.hget(key, "url");
    }

    public Map<String, String> getMap(String aid) {
        String key = prefix + "aid." + aid;
        return cache.hgetAll(key);
    }
    
    public String toTbitemUrl(String appid, String e) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(appid)).append("/titem");
        sb.append("?e=").append(e);
        return Constant.toShortUrl(sb.toString());
    }
    

    public String authUrl(Map<String, String> map, String openid, String url) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", "authUrl");
        data.put("click", "false");
        data.put("sid", map.get("sid"));
        data.put("appid", map.get("appid"));
        data.put("openid", openid);
        data.put("url", url);

        String aid = buildAid();
        setAidValue(aid, data);
        return authUrl() + "/auth/goBind?aid=" + aid + "&nid=" + (int) Math.random() * 1000;
    }

    public String authTsouUrl(Map<String, String> map, String openid, String word) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", "authTsouUrl");
        data.put("click", "false");
        data.put("sid", map.get("sid"));
        data.put("appid", map.get("appid"));
        data.put("openid", openid);
        data.put("word", word);

        String aid = buildAid();
        setAidValue(aid, data);
        return authUrl() + "/auth/goBind?aid=" + aid + "&nid=" + (int) Math.random() * 1000;
    }

    public String authTitemUrl(Map<String, String> map, String openid, String itemid) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", "authTitemUrl");
        data.put("click", "false");
        data.put("sid", map.get("sid"));
        data.put("appid", map.get("appid"));
        data.put("openid", openid);
        data.put("itemid", itemid);

        String aid = buildAid();
        setAidValue(aid, data);
        return authUrl() + "/auth/goBind?aid=" + aid + "&nid=" + (int) Math.random() * 1000;
    }

    public String authBindUrl(Map<String, String> map, String openid) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("action", "authBindUrl");
        data.put("click", "false");
        data.put("sid", map.get("sid"));
        data.put("appid", map.get("appid"));
        data.put("openid", openid);

        String aid = buildAid();
        setAidValue(aid, data);
        return authUrl() + "/auth/goBind?aid=" + aid + "&rid=" + (int) Math.random() * 1000;
    }

    public String buildTsouUrl(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(map.get("appid"))).append("/tsoulist");
        sb.append("?word=").append(map.get("word"));
        sb.append("&sid=").append(map.get("sid"));
        sb.append("&rid=").append((int) (Math.random() * 10000));
        if (map.containsKey("mid")) {
            sb.append("&mid=").append(map.get("mid"));
        }
        return Constant.toShortUrl(sb.toString());
    }

    public String buildTitemUrl(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(map.get("appid"))).append("/titem");
        sb.append("?id=").append(map.get("itemid"));
        sb.append("&sid=").append(map.get("sid"));
        sb.append("&rid=").append((int) (Math.random() * 10000));
        if (map.containsKey("mid")) {
            sb.append("&mid=").append(map.get("mid"));
        }
        return Constant.toShortUrl(sb.toString());
    }

    public String buildTitemUrl(Map<String, String> map, String itemid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(map.get("appid"))).append("/titem");
        sb.append("?id=").append(itemid);
        sb.append("&sid=").append(map.get("sid"));
        sb.append("&rid=").append((int) (Math.random() * 10000));
        if (map.containsKey("mid")) {
            sb.append("&mid=").append(map.get("mid"));
        }
        return Constant.toShortUrl(sb.toString());
    }

    public Map<String, String> getAidValue(String aid) {
        String key = prefix + "aid." + aid;
        return cache.hgetAll(key);
    }

    public String getAidValue(String aid, String field) {
        String key = prefix + "aid." + aid;
        return cache.hget(key, field);
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
