package com.xray.taoke.admin.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;

public class UoUserService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".uouser.count.";
    private static final String proxyall = config.get("redis.prefix") + ".uouser.proxyall.";
    private static final String haibao = config.get("redis.prefix") + ".uouser.proxyall.url.";

    public static UoUserService instance = new UoUserService();

    public int getCount(String appid, String openid) {
        String key = prefix + appid + openid;
        String count = cache.get(key);
        if (StringUtil.isEmpty(count))
            return 0;
        return Integer.parseInt(count);
    }

    public int incCount(String appid, String openid) {
        String key = prefix + appid;
        cache.incr(key);
        String count = cache.get(key);
        return Integer.parseInt(count);
    }

    public void delCount(String appid, String openid) {
        String key = prefix + appid;
        cache.delete(key);
    }

    public void setProxy(String pid, String userid) {
        String key = proxyall;
        cache.hset(key, pid, userid);
    }

    public void delProxy() {
        String key = proxyall;
        cache.delete(key);
    }

    public String getProxy(String pid) {
        String key = proxyall;
        String userid = cache.hget(key, pid);
        return userid;
    }

    public void setProxyHaiBao(String userid, String url) {
        String key = haibao;
        cache.hset(key, userid, url);
    }

    public void delProxyHaiBao(String userid) {
        String key = haibao;
        cache.hdel(key, userid);
    }

    public String getProxyHaiBao(String userid) {
        String key = haibao;
        String url = cache.hget(key, userid);
        return url;
    }

}
