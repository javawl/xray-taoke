package com.xray.taoke.admin.app;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.DateUtil;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.service.Tk21dsService;

public class OrderStateApp extends AbstractApp implements Runnable {
    private String appid;
    private long begintime;
    private long endtime;

    public OrderStateApp(String appid, long begintime, long endtime) {
        this.appid = appid;
        this.begintime = begintime;
        this.endtime = endtime;
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp tkstatus,appid:" + appid, e);
        }
    }

    public void doRun() throws Exception {
        long curtime = this.begintime;
        String time = null;
        while (curtime < endtime) {
            time = DateUtil.formatDate(new Date(curtime));
            curtime += 1200000;
            logger.info("runapp orderstate,start,appid:{},time:{}", appid, time);
            JSONObject obj = Tk21dsService.instance.gettkorder(appid, time, 1200);
            if (obj.getInteger("code") != 200) {
                logger.info("runapp orderstate,empty,appid:{},time:{}", appid, time);
                continue;
            }

            String content = obj.getJSONObject("data").getString("n_tbk_order");
            if (content.startsWith("{")) {
                updateByObj(obj.getJSONObject("data").getJSONObject("n_tbk_order"));
            } else {
                JSONArray arr = obj.getJSONObject("data").getJSONArray("n_tbk_order");
                for (int i = 0; i < arr.size(); i++) {
                    updateByObj(arr.getJSONObject(i));
                }
            }

        }

    }

    private void updateByObj(JSONObject obj) {

        String tradeid = obj.getString("trade_id");
        int tkstatus = obj.getInteger("tk_status");
        double alimoney = 0;
        double alirate = 0;
        long alitime = 0;

        if (OrderNewApp.saveByObj(appid, obj) == 1)
            return;

        if (tkstatus == 3) {
            alimoney = obj.getDoubleValue("total_commission_fee");
            alirate = obj.getDoubleValue("total_commission_rate");
            alitime = DateUtil.getDate(obj.getString("earning_time")).getTime();
            if (OrderInfo.dao.updateTkStatus(tradeid, tkstatus, alimoney, alirate, alitime) > 0)
                logger.info("runapp tkstatus,tradeid:{},tkstatus:{}", tradeid, tkstatus);
        } else {
            if (OrderInfo.dao.updateTkStatus(tradeid, tkstatus) > 0)
                logger.info("runapp tkstatus,tradeid:{},tkstatus:{}", tradeid, tkstatus);
        }

    }

}
