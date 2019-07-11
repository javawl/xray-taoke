package com.xray.taoke.admin.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;

public class CashService {
	private static RedisService cache = RedisServiceFactory.getDefaulInstance();

	private static final Prop config = PropKit.use("config.properties");
	private static final String prefix = config.get("redis.prefix") + ".cash.count.";

	private static final String remind = config.get("redis.prefix") + ".cash.remind.";

	public static CashService instance = new CashService();

	public int getCount(String seqid) {
		String key = prefix + seqid;
		String count = cache.get(key);
		if (StringUtil.isEmpty(count))
			return 0;
		return Integer.parseInt(count);
	}

	public int incCount(String seqid) {
		String key = prefix + seqid;
		cache.incr(key);
		String count = cache.get(key);
		return Integer.parseInt(count);
	}

	public void delCount(String seqid) {
		String key = prefix + seqid;
		cache.delete(key);
	}

	public void setTradeid(String tradeid) {
		String key = remind + ".tradeid.";
		cache.set(key, tradeid);
	}

	public String getTradeid() {
		String key = remind + ".tradeid.";
		if (StringUtil.isEmpty(cache.get(key)))
			return "";
		return cache.get(key);
	}

	public void delTradeid() {
		String key = remind + ".tradeid.";
		cache.delete(key);
	}

	// public String getHaveRemind(String openid) {
	// String key = remind + openid;
	// return cache.get(key);
	// }
	//
	// public void delHaveRemind(String openid) {
	// String key = remind + openid;
	// cache.delete(key);
	// }

	public String getHaveRemind() {
		String key = remind + ".cash.";
		return cache.get(key);
	}

	public void delHaveRemind() {
		String key = remind + ".cash.";
		cache.delete(key);
	}

	public void setHaveRemind(String seqid) {
		String key = remind + ".cash.";
		cache.set(key, seqid);
	}

}
