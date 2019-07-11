package com.xray.taoke.act.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;

public class WxAccessTokenCache implements IAccessTokenCache {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".wx.accesstoken.";
    private static final int timeout = 7200 - 9;

    @Override
    public String get(String key) {
        return cache.get(prefix + key);
    }

    @Override
    public void set(String key, String jsonValue) {
        cache.setex(prefix + key, jsonValue, timeout);
    }

    @Override
    public void remove(String key) {
        cache.delete(prefix + key);
    }

}
