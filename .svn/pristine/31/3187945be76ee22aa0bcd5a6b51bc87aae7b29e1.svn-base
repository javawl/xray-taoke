package com.xray.taoke.admin.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;

public class MpUserService {
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".mpuser.remind.";

    public static MpUserService instance = new MpUserService();

    public Double getDoubleByUserid(String userid) {
        String key = prefix;
        Double d = cache.zscore(key, userid);
        if (d == null)
            return (double) 0;
        return d;
    }

    public void addUserid(String userid) {
        String key = prefix;
        cache.zadd(key, System.currentTimeMillis(), userid);
    }

    public void delUserid(String userid) {
        String key = prefix;
        cache.delete(key);
    }

    public void addUseridByTwoDays(String userid) {
        String key = prefix + ".twoday.";
        cache.zadd(key, System.currentTimeMillis(), userid);
    }

    public Double getDoubleTwoDayByUserid(String userid) {
        String key = prefix + ".twoday.";
        Double d = cache.zscore(key, userid);
        if (d == null)
            return (double) 0;
        return d;
    }

}
