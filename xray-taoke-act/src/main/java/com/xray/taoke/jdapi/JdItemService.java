package com.xray.taoke.jdapi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.util.StringUtil;
import com.xray.taoke.act.service.MpInfoService;
import com.xray.taoke.jdapi.vo.ConstTkjd;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.tkapi.vo.ConstTk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class JdItemService {
    private static Logger logger = LoggerFactory.getLogger(JdItemService.class);
    private static RedisService cache = RedisServiceFactory.getDefaulInstance();

    private static final String prefix = ConstTk.config.get("redis.prefix") + ".jditem.";
    private static final int timeout = 86400;

    public static JdItemService instance = new JdItemService();

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
    public JdItemVo getItemVo(String appid, String itemid) {
        String key = prefix + itemid + ".vo";
        String val = cache.get(key);
        if (StringUtil.isNotEmpty(val))
            return JSONObject.parseObject(val, JdItemVo.class);

        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        JSONObject obj = null;
        try {
            obj = Jd21dsService.instance.getiteminfo(appid, itemid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JdItemVo data = new JdItemVo();
        JSONArray array = obj.getJSONObject("data").getJSONArray("lists");
        JSONArray couponList = null;
        JSONObject couponInfo_list = null;
        for (int i = 0; i < array.size(); i++) {
            couponInfo_list = array.getJSONObject(i).getJSONObject("couponInfo");
            couponList = couponInfo_list.getJSONArray("couponList");
            String skuId = array.getJSONObject(i).getString("skuId");
            String skuName = array.getJSONObject(i).getString("skuName");
            JSONObject jsonObject = array.getJSONObject(i).getJSONObject("priceInfo");
            double itemprice = jsonObject.getDoubleValue("price");
            String itempic = array.getJSONObject(i).getJSONObject("imageInfo").getJSONArray("imageList").getJSONObject(0).getString("url");

            double tkmoney = array.getJSONObject(i).getJSONObject("commissionInfo").getDoubleValue("commission");
            double tkrate = array.getJSONObject(i).getJSONObject("commissionInfo").getDoubleValue("commissionShare");

            double quota = 0.0;

            String link = "";
            double discount = 0.0;
            if (couponList.size() != 0) {
                link = couponList.getJSONObject(0).getString("link");
                discount = couponList.getJSONObject(0).getDoubleValue("discount");
                quota = couponList.getJSONObject(0).getDoubleValue("quota");
            }

            data.setItemid(skuId);
            data.setItemtitle(skuName);
            data.setItemprice(itemprice);
            data.setItempic(itempic);
            data.setCpurl(link);
            data.setCpmoney(discount);
            data.setTkmoney(tkmoney);
            data.setQuota(quota);
            BigDecimal bg = new BigDecimal(ConstTkjd.getJiemoney(data.getTkmoney())).setScale(2, RoundingMode.UP);
            data.setJiemoney(bg.doubleValue());
        }
        cache.setex(key, JSONObject.toJSONString(data), timeout);
        return data;
    }

    @SuppressWarnings("unused")
    public JdItemVo getItemVo(String appid, String itemid, String openid) {
        String key = prefix + itemid + ".vo";
        String val = cache.get(key);
        if (StringUtil.isNotEmpty(val))
            return JSONObject.parseObject(val, JdItemVo.class);

        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        JSONObject obj = null;
        try {
            obj = Jd21dsService.instance.getiteminfo(appid, itemid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.doDataErr(obj, appid, itemid);

        this.setJdPidex(appid, itemid, openid);

        JdItemVo data = new JdItemVo();
        JSONArray array = obj.getJSONObject("data").getJSONArray("lists");
        JSONArray couponList = null;
        JSONObject couponInfo_list = null;
        for (int i = 0; i < array.size(); i++) {
            couponInfo_list = array.getJSONObject(i).getJSONObject("couponInfo");
            couponList = couponInfo_list.getJSONArray("couponList");
            String skuId = array.getJSONObject(i).getString("skuId");
            String skuName = array.getJSONObject(i).getString("skuName");
            JSONObject jsonObject = array.getJSONObject(i).getJSONObject("priceInfo");
            double itemprice = jsonObject.getDoubleValue("price");
            String itempic = array.getJSONObject(i).getJSONObject("imageInfo").getJSONArray("imageList").getJSONObject(0).getString("url");

            double tkmoney = array.getJSONObject(i).getJSONObject("commissionInfo").getDoubleValue("commission");
            double tkrate = array.getJSONObject(i).getJSONObject("commissionInfo").getDoubleValue("commissionShare");

            String link = "";
            double discount = 0.0;
            if (couponList.size() != 0) {
                link = couponList.getJSONObject(0).getString("link");
                discount = couponList.getJSONObject(0).getDoubleValue("discount");
            }

            data.setItemid(skuId);
            data.setItemtitle(skuName);
            data.setItemprice(itemprice);
            data.setItempic(itempic);
            data.setCpurl(link);
            data.setCpmoney(discount);
            data.setTkmoney(tkmoney);
        }
        cache.setex(key, JSONObject.toJSONString(data), timeout);
        return data;
    }

    // 自动跟单
    // private String calcPidex(String itemid, String openid, String pidex) {
    // if (openid == null)
    // return null;
    // if (StringUtil.isEmpty(pidex))
    // return null;
    // String key = prefix + itemid + ".pidex.";
    // String[] arr = pidex.split(",");
    // for (String str : arr) {
    // if (cache.exists(key + str.split("_")[3]))
    // continue;
    // return str;
    // }
    // return null;
    // }

    private void setJdPidex(String appid, String itemid, String openid) {
        if (StringUtil.isEmpty(openid))
            return;
        String key = prefix + itemid + ".pidex.";
        cache.setex(key, appid + "," + openid, 36000);
    }

    public String getJdPidex(String itemid) {
        String key = prefix + itemid + ".pidex.";
        return cache.get(key);
    }

    public void delJdPidex(String itemid) {
        String key = prefix + itemid + ".pidex.";
        cache.delete(key);
    }

    public double getFlrate(double tkmoney) {
        if (tkmoney < 2)
            return 0.81;
        else if (tkmoney < 100)
            return 0.61;
        else
            return 0.51;
    }

    public String getTobjUrl(String appid, String e) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(appid)).append("/tobj");
        sb.append("?e=").append(e);
        return ConstTk.toShortUrl(sb.toString());
    }

    public String getTsouUrl(String appid, String word) {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        StringBuilder sb = new StringBuilder();
        sb.append(map.get("url")).append("/tsou");
        sb.append("?sid=").append(map.get("sid"));
        sb.append("&keyword=").append(word);
        sb.append("&xb=").append(System.currentTimeMillis());
        return ConstTk.toShortUrl(sb.toString());
    }

    public String getBindsuccUrl(String appid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MpInfoService.instance.getUrl(appid)).append("/bindsucc");
        return ConstTk.toShortUrl(sb.toString());
    }

    // private void doNotaokeErr(JSONObject obj, String appid, String itemid) {
    // if (obj.getInteger("code") != 200)
    // throw new RtException(ConstTk.notaoke_err, "err notaoke,appid:" + appid +
    // ",itemid:" + itemid + ",json:" + obj.toJSONString());
    // }

    private void doDataErr(JSONObject obj, String appid, String itemid) {
        if (obj.getInteger("code") != 200)
            throw new RtException(ConstTk.data_err, "err code,appid:" + appid + ",itemid:" + itemid + ",json:" + obj.toJSONString());
    }
    
    
    
    
    
    
    public List<JdItemVo> getItemList(String lwid, int pno) {
        return getItemList(lwid, pno, 10);
    }

    public List<JdItemVo> getItemList(String lwid, int pno, int psize) {
        String list_key = prefix + "lwid." + lwid;
        long start = 0;
        long end = -1;
        if (pno > 0) {
            start = (pno - 1) * psize;
            end = pno * psize - 1;
        }

        List<JdItemVo> list = new ArrayList<JdItemVo>();
        Set<String> itemids = cache.zrevrange(list_key, start, end);
        Map<String, String> map = cache.getBatch(list_key + ".", itemids);
        for (String itemid : itemids) {
            list.add(JSONObject.parseObject(map.get(itemid), JdItemVo.class));
        }
        return list;
    }
    
    public boolean existsItemList(String lwid) {
        String list_key = prefix + "lwid." + lwid;
        return cache.exists(list_key);
    }
    
    public long sizeItemList(String lwid) {
        String list_key = prefix + "lwid." + lwid;
        return cache.zcard(list_key);
    }

    public void delItemList(String lwid) {
        String list_key = prefix + "lwid." + lwid;
        cache.delete(list_key);
    }
    public void setItemList(String lwid, List<JdItemVo> list,boolean inAppend) {
        this.setItemList(lwid, list,inAppend, timeout, false);
    }

  public void setItemList(String lwid, List<JdItemVo> list,boolean inAppend, int seconds, boolean inOrder) {
        String list_key = prefix + "lwid." + lwid;
        Jedis jedis = cache.getJedisClient();
        try {
            String item_key = null;
            Pipeline p = jedis.pipelined();
            if (!inAppend)
                p.del(list_key);
            
            for (JdItemVo data : list) {
                item_key = list_key + "." + data.getItemid();
                p.setex(item_key, timeout, JSONObject.toJSONString(data));
                p.zadd(list_key, -System.currentTimeMillis(), data.getItemid());
            }
            p.expire(list_key, seconds);
            p.sync();
            logger.info("add itemlist,lwid:{}, size:{}", lwid, list.size());
        } finally {
            cache.returnResource(jedis);
        }
    }
    
}
