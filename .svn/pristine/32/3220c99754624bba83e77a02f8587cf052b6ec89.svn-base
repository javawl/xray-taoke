package com.xray.taoke.act.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;
import com.xray.taoke.tkapi.vo.TkLyInfoVo;

public class LvInfoService {
	private static RedisService cache = RedisServiceFactory.getDefaulInstance();

	private static final Prop config = PropKit.use("config.properties");
	private static final String prefix = config.get("redis.prefix") + ".lvinfo.";

	public static LvInfoService instance = new LvInfoService();

	public String getAll() {
		String key = prefix + ".addall";
		if (StringUtil.isEmpty(cache.get(key)))
			return "";
		return cache.get(key);
	}
	
	public List<TkLyInfoVo> getAll(int pno, int psize){
	    String key = prefix + ".addall";
        long start = 0;
        long end = -1;
        if (pno > 0) {
            start = (pno - 1) * psize;
            end = pno * psize - 1;
        }

        List<TkLyInfoVo> list = new ArrayList<TkLyInfoVo>();
        Set<String> str = cache.zrevrange(key, start, end);
        for (String data : str) {
            list.add(JSONObject.parseObject(data, TkLyInfoVo.class));
        }
        return list;
	}
	
}
