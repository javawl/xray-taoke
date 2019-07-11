package com.xray.taoke.admin.app;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.TkStatDetail;
import com.xray.taoke.admin.service.MpInfoService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class TkStatDetailApp extends AbstractApp {
    private String day;

    public TkStatDetailApp(String day) {
        this.day = day;
    }

    @Override
    public void run() {
        try {
            TkStatDetail.dao.delByDay(day);
            List<String> list = MpInfo.dao.queryAppidInuse();
            for (String appid : list) {
                this.runStat(appid, day);
                this.runUser(appid, day);
            }
        } catch (Exception e) {
            logger.error("errapp,TkStatDetailApp,day:" + day, e);
        }
    }

    public void runUser(String appid, String day) {
        String sid = MpInfoService.instance.getSid(appid);
        TkStatDetail data = new TkStatDetail();
        data.set("day", day);
        data.set("appid", appid);
        setUserData(data, sid);
        data.save();
        logger.info("runapp,TkStatDetailApp,runUser,appid:{},day:{}", appid, day);
    }

    public void runStat(String appid, String day) {
        // 历史数据不能改
        if (!DateUtil.getToday().equals(day))
            return;

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getDate(day, "yyyy-MM-dd"));
        long begintime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endtime = cal.getTimeInMillis();

        String key = MpInfoService.instance.getStatKey(appid, "userall", day);
        cache.set(key, String.valueOf(MpUser.dao.countUserall(appid, endtime)));

        key = MpInfoService.instance.getStatKey(appid, "usernew", day);
        cache.set(key, String.valueOf(MpUser.dao.countUsernew(appid, begintime, endtime)));

        key = MpInfoService.instance.getStatKey(appid, "activeuv", day);
        cache.set(key, String.valueOf(MpInfoService.instance.getActiveSize(appid)));
    }

    private void setUserData(TkStatDetail data, String sid) {
        Jedis jedis = cache.getJedisClient();
        try {
            String appid = data.getStr("appid");
            String day = data.getStr("day");

            Map<String, Response<String>> resp = new HashMap<String, Response<String>>();
            Pipeline p = jedis.pipelined();
            resp.put("userall", p.get(MpInfoService.instance.getStatKey(appid, "userall", day)));
            resp.put("usernew", p.get(MpInfoService.instance.getStatKey(appid, "usernew", day)));
            resp.put("activeuv", p.get(MpInfoService.instance.getStatKey(appid, "activeuv", day)));
            resp.put("chaquanpv", p.get(MpInfoService.instance.getStatKey(appid, "chaquan", day)));

            resp.put("tsoupv", p.get(MpInfoService.instance.getStatKey(sid, "tsou", day)));
            resp.put("titempv", p.get(MpInfoService.instance.getStatKey(sid, "titem", day)));
            resp.put("tshoppv", p.get(MpInfoService.instance.getStatKey(sid, "tshop", day)));
            resp.put("tlinkpv", p.get(MpInfoService.instance.getStatKey(sid, "tlink", day)));
            p.sync();

            setVal(data, "userall", resp);
            setVal(data, "usernew", resp);
            setVal(data, "activeuv", resp);
            setVal(data, "chaquanpv", resp);
            setVal(data, "tsoupv", resp);
            setVal(data, "titempv", resp);
            setVal(data, "tshoppv", resp);
            setVal(data, "tlinkpv", resp);
        } finally {
            cache.returnResource(jedis);
        }
    }

    private static void setVal(TkStatDetail data, String key, Map<String, Response<String>> resp) {
        String val = resp.get(key).get();
        if (StringUtil.isNotEmpty(val))
            data.set(key, Integer.valueOf(val));
    }

}
