package com.xray.taoke.admin.app.jd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.taoke.admin.model.JdOrderInfo;
import com.xray.taoke.jdapi.Jd21dsService;

public class JdOrderNewStateApp extends AbstractApp implements Runnable {
    private String appid;

    public JdOrderNewStateApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp tkstatus,appid:{}", appid, e);
        }
    }

    public void doRun(String appid) throws Exception {
        String time = null;
        List<JdOrderInfo> list = JdOrderInfo.dao.queryTkstatusByAppidkey(appid);
        logger.info("runapp JdOrderNewStateApp,appid:{},size:{}", appid, list.size());
        if (list.size() <= 0)
            return;

        long currtime = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        for (JdOrderInfo data : list) {
            if ((currtime + 60000) > data.getLong("tradetime"))
                continue;

            currtime = data.getLong("tradetime");

            time = dateFormat.format(new Date(currtime));
            logger.info("runapp JdOrderNewStateApp,start,appid:{},time:{}", appid, currtime);

            JSONObject obj = Jd21dsService.instance.getjdorder(appid, time, 1);

            if (obj.getInteger("code") != 200) {
                logger.info("runapp JdOrderNewStateApp,empty,appid:{},time:{}", appid, time);
                continue;
            }
            updateByObj(appid, obj.getJSONObject("data").getJSONArray("lists"));

        }

    }

    private void updateByObj(String appid, JSONArray array) {

        if (JdOrderNewApp.saveByObj(appid, array) == 1)
            return;

        JSONObject jsonObject = null;
        JSONArray skuList_object = null;
        int tkstatus = 0;
        double alimoney = 0;
        double alirate = 0;
        long alitime = 0;
        String tradeid = null;

        long finishtime = 0;

        for (Object object : array) {
            jsonObject = JSONObject.parseObject(object.toString());

            tradeid = jsonObject.getString("orderId");
            skuList_object = jsonObject.getJSONArray("skuList");
            tkstatus = jsonObject.getIntValue("validCode");
            finishtime = jsonObject.getLongValue("finishTime");

            for (Object object2 : skuList_object) {
                jsonObject = JSONObject.parseObject(object2.toString());
                tkstatus = jsonObject.getIntValue("validCode");

                if (tkstatus == 17) {
                    alimoney = jsonObject.getDoubleValue("estimateFee");
                    alirate = jsonObject.getDoubleValue("finalRate");
                    alitime = finishtime;
                    if (JdOrderInfo.dao.updateTkStatus(tradeid, tkstatus, alimoney, alirate, alitime) > 0) {
                        logger.info("runapp tkstatus,tradeid:{},tkstatus:{}", tradeid, tkstatus);
                    }

                } else {
                    if (JdOrderInfo.dao.updateTkStatus(tradeid, tkstatus) > 0)
                        logger.info("runapp tkstatus,tradeid:{},tkstatus:{}", tradeid, tkstatus);
                }

            }

        }

    }

}
