package com.xray.taoke.admin.app;

import java.sql.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.DateUtil;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.tkapi.Tk21dsService;

public class InvalidOrderApp extends AbstractApp {
    private String appid;

    public InvalidOrderApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp InvalidOrderApp,appid:" + appid, e);
        }
    }

    public void doRun(String appid) throws Exception {
        String time = null;

        long time_before = new java.util.Date().getTime() - 15 * 24 * 60 * 60 * 1000;
        List<OrderInfo> list = OrderInfo.dao.queryInvaildOrder(appid, time_before);
        logger.info("runapp InvalidOrderApp,appid:{},size:{}", appid, list.size());
        if (list.size() <= 0)
            return;

        long currtime = 0;
        for (OrderInfo data : list) {
            if ((currtime + 1200000) > data.getLong("tradetime"))
                continue;

            currtime = data.getLong("tradetime");
            time = DateUtil.formatDate(new Date(currtime));
            logger.info("runapp InvalidOrderApp,start,appid:{},time:{}", appid, currtime);

            JSONObject obj = Tk21dsService.instance.gettkorder(appid, time);
            if (obj.getInteger("code") != 200) {
                logger.info("runapp InvalidOrderApp,empty,appid:{},time:{}", appid, time);
                continue;
            }

            String content = obj.getJSONObject("data").getString("n_tbk_order");
            if (content.startsWith("{")) {
                updateByObj(obj.getJSONObject("data").getJSONObject("n_tbk_order"), appid);
            } else {
                JSONArray arr = obj.getJSONObject("data").getJSONArray("n_tbk_order");
                for (int i = 0; i < arr.size(); i++) {
                    updateByObj(arr.getJSONObject(i), appid);
                }
            }
        }

    }

    private void updateByObj(JSONObject obj, String appid) {
        String tradeid = obj.getString("trade_id");
        int tkstatus = obj.getInteger("tk_status");
        if (tkstatus == 3)
            return;
        if (OrderInfo.dao.updateInvalidorder(tradeid, tkstatus) > 0)
            logger.info("runapp InvalidOrderApp,tradeid:{},invaildorder:{}", tradeid, tkstatus);
    }
}
