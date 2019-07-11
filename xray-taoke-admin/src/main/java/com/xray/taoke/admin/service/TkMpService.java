package com.xray.taoke.admin.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.act.exception.RtException;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.TkMaterial;
import com.xray.taoke.admin.model.MpInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

public class TkMpService {
    private static Logger logger = LoggerFactory.getLogger(TkMpService.class);
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final Prop config = PropKit.use("config.properties");
    private static final String prefix = config.get("redis.prefix") + ".material.";
    private static final String mpfans = config.get("redis.prefix") + ".material.fans.";

    private static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);
    public static TkMpService instance = new TkMpService();

    public void updateMpfans(String appid, String total) {
        String key = mpfans + appid;
        cache.set(key, total);
    }

    public int getFollows(String appid) {
        String key = mpfans + appid;
        String fans = cache.get(key);
        if (StringUtil.isEmpty(fans))
            return 0;
        return Integer.parseInt(fans);
    }

    public static Map<String, Integer> getUserSize(List<String> appidList) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Jedis jc = null;
        try {
            jc = cache.getJedisClient();
            Pipeline pipeline = jc.pipelined();
            Map<String, Response<String>> responses = new HashMap<String, Response<String>>(appidList.size());
            for (String appid : appidList) {
                String key = mpfans + appid;
                responses.put(key, pipeline.get(key));
            }
            pipeline.sync();
            for (String key : responses.keySet()) {
                String str = responses.get(key).get();
                int usersize = !StringUtil.isEmpty(str) ? Integer.parseInt(str) : -1;
                map.put(key.replace(mpfans, ""), usersize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cache.returnResource(jc);
        }
        return map;
    }

    @SuppressWarnings("rawtypes")
    public Set getSubscribe(String appid) {
        String key = mpfans + appid + "." + DateUtil.getToday();
        Set<Tuple> set = cache.zrangeWithScores(key, 0, -1);
        return set;
    }

    @SuppressWarnings("rawtypes")
    public Set getSubscribeByMin(String appid) {
        String key = mpfans + appid + "." + DateUtil.getToday();
        Set<Tuple> set = cache.zrevrangeWithScores(key, 0, 100);
        return set;
    }

    public String daihaoLv1(String appid) {
        String key = mpfans + appid + ".min";
        String val = cache.get(key);
        return val;
    }

    public String daihaoLv2(String appid) {
        String key = mpfans + appid + ".hour";
        String val = cache.get(key);
        return val;
    }

    public String daihaoLv3(String appid) {
        String key = mpfans + appid + ".day";
        String val = cache.get(key);
        return val;
    }

    public void deleteDaiHao(String appid) {
        String key = mpfans + appid + ".min";
        String key2 = mpfans + appid + ".hour";
        String key3 = mpfans + appid + ".day";
        cache.delete(key);
        cache.delete(key2);
        cache.delete(key3);
    }

    public static Map<String, Integer> getBusersize(List<String> appIdList, String time) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Jedis jc = null;
        try {
            jc = cache.getJedisClient();
            Pipeline pipeline = jc.pipelined();
            Map<String, Response<String>> responses = new HashMap<String, Response<String>>(appIdList.size());
            for (String appid : appIdList) {
                responses.put(appid, pipeline.get(mpfans + appid + "." + time));
            }
            pipeline.sync();
            for (String key : responses.keySet()) {
                String str = responses.get(key).get();
                int busersize = !StringUtil.isEmpty(str) ? Integer.parseInt(str) : 0;
                map.put(key, busersize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cache.returnResource(jc);
        }
        return map;
    }

    public Map<String, String> getMaterial(String appid) {
        if (appid == null)
            throw new RtException("null appid");
        String key = prefix + appid;
        Map<String, String> map = cache.hgetAll(key);
        return map;
    }

    public String getJxContent(String appid) {
        String key = prefix + appid;
        return cache.hget(key, "wx_1e61011294bafd3c");
    }

    public void setJxContent(String appid, String value) {
        String key = prefix + appid;
        cache.hset(key, "wx_1e61011294bafd3c", value);
    }

    public Map<String, String> getFollows(Set<String> keys) {
        return cache.getBatch(mpfans, keys);
    }

    public static void getAllContent(List<String> appIdList) {
        Jedis jc = null;
        try {
            jc = cache.getJedisClient();
            Pipeline pipeline = jc.pipelined();
            Map<String, Response<String>> responses = new HashMap<String, Response<String>>(appIdList.size());
            for (String appid : appIdList) {
                responses.put(appid, pipeline.hget(prefix + appid, "replyid1"));
            }
            pipeline.sync();
            int count = 0;
            // for (String key : responses.keySet()) {
            // String str = responses.get(key).get();
            // TkMpinfo info = TkMpinfo.dao.exists(key);
            // if (!StringUtil.isEmpty(str))
            // info.set("isreply", 1);
            // else {
            // info.set("isreply", -1);
            // count++;
            // }
            // info.update();
            // }
            logger.info("checkFollow,not have follow content ,size:{}", count);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cache.returnResource(jc);
        }
    }

    public void pubMaterial(String appid) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            List<TkMaterial> list = TkMaterial.dao.queryByAppid(appid);
            for (TkMaterial data : list) {
                map.put(data.getStr("mediaid"), doMpMaterial(data));
            }
            String key = prefix + appid;
            cache.delete(key);
            cache.hmset(key, map);
            logger.info("pubMaterial,appid:{}", appid);
        } catch (Exception e) {
            logger.error("err pubMaterial,appid:" + appid, e);
        }
    }

    public void delBySeqid(String seqid) {
        TkMaterial data = TkMaterial.dao.findById(seqid);
        String mediaid = data.getStr("mediaid");
        String appid = data.getStr("appid");
        String key = prefix + appid;

        cache.hdel(key, mediaid);
    }

    public void delMaterial(MpInfo data) {
        if (data == null)
            throw new RtException("null MpInfo");
        String key = prefix + data.getStr("appid");
        cache.delete(key);
    }

    private String doMpMaterial(TkMaterial data) throws UnsupportedEncodingException {
        String appid = data.getStr("appid");
        JSONObject obj = null;
        JSONArray arr = null;
        switch (data.getInt("type")) {
        case 1:
            obj = new JSONObject();
            obj.put("type", "wx_news");
            obj.put("data", JSONObject.parseArray(new String(data.get("content"), "UTF-8")));
            return obj.toJSONString();
        case 2:
            arr = JSONObject.parseArray(new String(data.get("content"), "UTF-8"));
            for (int i = 0; i < arr.size(); i++) {
                obj = arr.getJSONObject(i);
                String content = obj.getString("content");
                if (!"image".equals(obj.getString("type")))
                    continue;
                String mediaid = obj.getString("mediaid");
                if (StringUtil.isNotEmpty(mediaid))
                    continue;

                ApiResult res = wxApi.addMaterial(appid, obj);
                if (!res.isSucceed())
                    throw new RtException("err addMaterialImage,appid:" + appid + ",result:" + res.getJson());
                obj.put("content", content);
                obj.put("mediaid", res.get("media_id"));
            }

            data.set("content", arr.toJSONString());
            data.update();

            obj = new JSONObject();
            obj.put("type", "wx_mixed");
            obj.put("data", arr.toJSONString());
            return obj.toJSONString();
        default:
            break;
        }
        return "";
    }

}
