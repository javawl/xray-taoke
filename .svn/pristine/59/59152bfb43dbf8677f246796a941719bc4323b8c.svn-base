package com.xray.taoke.act.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;

public class UoUserService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".uouser.";

    public static UoUserService instance = new UoUserService();

    public double getRate(String mid) {
        if (StringUtil.isEmpty(mid))
            return 0;
        String str = cache.get(prefix + mid);
        return StringUtil.isNotEmpty(str) ? Double.parseDouble(str) : 0;
    }

    public void setRate(String mid, String rate) {
        cache.set(prefix + mid, rate);
    }

    public void setRate(String mid, double rate) {
        cache.set(prefix + mid, String.valueOf(rate));
    }

}
