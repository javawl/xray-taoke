package com.xray.taoke.pddapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.pddapi.vo.PddItemVo;
import com.xray.taoke.tkapi.vo.ConstTk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PddItemService {
    private static Logger logger = LoggerFactory.getLogger(PddItemService.class);
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final String prefix = ConstTk.config.get("redis.prefix") + ".pdditem.";
    private static final int timeout = 86400;

    public static PddItemService instance = new PddItemService();

    public void addItemListVo(String lid, List<JdItemVo> list) {
        String list_key = prefix + "lid." + lid;
        Jedis jedis = cache.getJedisClient();
        try {
            String item_key = null;
            Pipeline p = jedis.pipelined();
            for (JdItemVo data : list) {
                item_key = prefix + data.getItemid() + ".vo";
                p.setex(item_key, timeout, JSONObject.toJSONString(data));
                p.lpush(list_key, data.getItemid());
            }
            p.expire(list_key, timeout);
            p.sync();
            logger.info("add itemlist,lid:{}, size:{}", lid, list.size());
        } finally {
            cache.returnResource(jedis);
        }
    }

    public List<JdItemVo> getItemListVo(String lid, int page) {
        String list_key = prefix + "lid." + lid;

        String item_key = null;
        List<JdItemVo> list = new ArrayList<JdItemVo>();
        List<String> itemidList = cache.lrange(list_key, (page - 1) * 10, page * 10 - 1);
        for (String itemid : itemidList) {
            item_key = prefix + itemid + ".vo";
            list.add(JSONObject.parseObject(cache.get(item_key), JdItemVo.class));
        }
        return list;
    }

    public List<JdItemVo> getAllItemListVo(String lid) {
        String list_key = prefix + "lid." + lid;

        String item_key = null;
        List<JdItemVo> list = new ArrayList<JdItemVo>();
        List<String> itemidList = cache.lrange(list_key, 0, -1);
        for (String itemid : itemidList) {
            item_key = prefix + itemid + ".vo";
            list.add(JSONObject.parseObject(cache.get(item_key), JdItemVo.class));
        }
        return list;
    }

    @SuppressWarnings("unused")
    public PddItemVo getItemVo(String appid, String itemid) {
        String key = prefix + itemid + ".vo";
        String val = cache.get(key);
        if (StringUtil.isNotEmpty(val))
            return JSONObject.parseObject(val, PddItemVo.class);

        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        JSONObject obj = null;
        try {
            obj = Pdd21dsService.instance.getpdditem(map.get("appid"), itemid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.doDataErr(obj, appid, itemid);
        PddItemVo data = new PddItemVo();

        if (obj.getJSONObject("data").getInteger("total_count") == 0)
            return data;

        JSONArray array = obj.getJSONObject("data").getJSONArray("goods_list");

        JSONArray couponList = null;
        JSONObject couponInfo_list = null;
        for (int i = 0; i < array.size(); i++) {

            String itemtitle = array.getJSONObject(i).getString("goods_name");
            String goods_id = array.getJSONObject(i).getString("goods_id");

            double min_group_price = array.getJSONObject(i).getDoubleValue("min_group_price") / 100;
            double min_normal_price = array.getJSONObject(i).getDoubleValue("min_normal_price") / 100;
            double promotion_rate = array.getJSONObject(i).getDoubleValue("promotion_rate") / 1000;
            double coupon_discount = array.getJSONObject(i).getDoubleValue("coupon_discount") / 100;
            String itempic = array.getJSONObject(i).getString("goods_thumbnail_url");

            double tkmoney = (min_group_price - coupon_discount) * promotion_rate;

            data.setItemid(goods_id);
            data.setItemtitle(itemtitle);
            data.setItemprice(min_normal_price);
            data.setTogetprice(min_group_price / 100);
            data.setItempic(itempic);
            data.setCpmoney(coupon_discount);
            data.setTkmoney(tkmoney);
        }
        cache.setex(key, JSONObject.toJSONString(data), timeout);
        return data;
    }

    @SuppressWarnings("unused")
    public String getItemGurl(String appid, String itemid) {

        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        JSONObject obj = null;
        try {
            obj = Pdd21dsService.instance.getpdditemtgurl(map.get("appid"), map.get("pddid"), itemid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.doDataErr(obj, appid, itemid);
        String shorturl = obj.getJSONObject("data").getJSONArray("goods_promotion_url_list").getJSONObject(0).getString("mobile_short_url");
        return shorturl;
    }

    @SuppressWarnings("unused")
    public String createPddpid(String appid) {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        JSONObject obj = null;
        try {
            obj = Pdd21dsService.instance.docreatepddpid(map.get("appid"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (obj.getInteger("code") != 200)
            throw new RtException(ConstTk.data_err, "err code,appid:" + appid + ",json:" + obj.toJSONString());
        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("p_id_list");
        String pid = jsonArray.getJSONObject(0).getString("p_id");
        return pid;
    }
    
    
    
    
    
    
    
    public String getBindsuccUrl(String appid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(appid)).append("/bindsucc");
        return ConstTk.toShortUrl(sb.toString());
    }

    private void doDataErr(JSONObject obj, String appid, String itemid) {
        if (obj.getInteger("code") != 200)
            throw new RtException(ConstTk.data_err, "err code,appid:" + appid + ",itemid:" + itemid + ",json:" + obj.toJSONString());
    }

    public String getJdPidex(String itemid) {
        String key = prefix + itemid + ".pidex.";
        return cache.get(key);
    }

    public void delJdPidex(String itemid) {
        String key = prefix + itemid + ".pidex.";
        cache.delete(key);
    }

}
