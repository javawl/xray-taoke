package com.xray.taoke.admin.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;

public class OrderService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".order.count.";

    public static OrderService instance = new OrderService();

    public int getCount(String appid) {
        String key = prefix + appid;
        String count = cache.get(key);
        if (StringUtil.isEmpty(count))
            return 0;
        return Integer.parseInt(count);
    }

    public int incCount(String appid) {
        String key = prefix + appid;
        cache.incr(key);
        String count = cache.get(key);
        return Integer.parseInt(count);
    }

    public void delCount(String appid) {
        String key = prefix + appid;
        cache.delete(key);
    }

}
