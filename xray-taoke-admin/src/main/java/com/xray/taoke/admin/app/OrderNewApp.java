package com.xray.taoke.admin.app;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.DateUtil;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.service.Tk21dsService;

public class OrderNewApp extends AbstractApp implements Runnable {
    private String appid;

    public OrderNewApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp neworder,appid:" + appid, e);
        }
    }

    public void doRun(String appid) throws Exception {
        String time = DateUtil.formatDate(new Date(System.currentTimeMillis() - 300000));

        JSONObject obj = Tk21dsService.instance.gettkorder(appid, time, 300);
        if (obj.getInteger("code") != 200) {
            logger.info("runapp empty neworder,appid:{},time:{}", appid, time);
            return;
        }

        String content = obj.getJSONObject("data").getString("n_tbk_order");
        if (content.startsWith("{")) {
            saveByObj(appid, obj.getJSONObject("data").getJSONObject("n_tbk_order"));
            logger.info("runapp neworder,appid:{},time:{},size:1", appid, time);
        } else {
            JSONArray arr = obj.getJSONObject("data").getJSONArray("n_tbk_order");
            for (int i = 0; i < arr.size(); i++) {
                saveByObj(appid, arr.getJSONObject(i));
            }
            logger.info("runapp neworder,appid:{},time:{},size:{}", appid, time, arr.size());
        }
    }

    public static int saveByObj(String appid, JSONObject obj) {
        if (OrderInfo.dao.exist(obj.getString("trade_id")))
            return 0;

        synchronized (OrderNewApp.class) {
            if (OrderInfo.dao.exist(obj.getString("trade_id")))
                return 0;

            if (appid.equals("wx8dd7601283019269")) {
                String adzoneid = obj.getString("adzone_id");
                if (adzoneid.equals("82576233") || adzoneid.equals("82994062"))
                    return 1;
            }

            OrderInfo data = new OrderInfo();
            String trade_id = obj.getString("trade_id");
            String trade_parent_id = obj.getString("trade_parent_id");
            data.set("appidkey", appid);
            data.set("tradeid", trade_id);
            data.set("tradeprice", obj.getDouble("alipay_total_price"));
            data.set("trademoney", obj.getDouble("pub_share_pre_fee"));
            data.set("traderate", obj.getDouble("income_rate"));
            data.set("tradetime", DateUtil.getDate(obj.getString("create_time")).getTime());
            data.set("itemid", obj.getString("num_iid"));
            data.set("itemtitle", obj.getString("item_title"));
            data.set("itemnum", obj.getInteger("item_num"));
            data.set("tkstatus", obj.getInteger("tk_status"));
            data.set("adzoneid", obj.getString("adzone_id"));
            data.set("parentid", trade_parent_id);
            if (!trade_id.equals(trade_parent_id))
                data.set("tradetype", 2);

            data.set("dividerate", obj.getDouble("commission_rate"));
            data.save();
            logger.info("runapp,succ neworder,appid:{},tradeid:{}", appid, obj.getString("trade_id"));
            return 1;
        }
    }

}
