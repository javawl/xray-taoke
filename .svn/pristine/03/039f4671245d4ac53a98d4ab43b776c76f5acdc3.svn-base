package com.xray.taoke.admin.app;

import java.util.Calendar;
import java.util.Set;

import com.xray.taoke.admin.service.MpInfoService;

import redis.clients.jedis.Tuple;

public class CountStatApp extends AbstractApp {
    private String appid;

    public CountStatApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("err stat,appid:" + appid, e);
        }
    }

    public void doRun() {
        long activetime = this.getActivetime();

        @SuppressWarnings("unused")
        int chaquan = 0;
        Set<Tuple> set = MpInfoService.instance.getAllActive(appid);
        for (Tuple tuple : set) {
            if (activetime > tuple.getScore())
                continue;
            ++chaquan;
        }

        // TODO 记录表，存在更新，不存在新增
    }

    private long getActivetime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 48);
        return cal.getTime().getTime();
    }

}
