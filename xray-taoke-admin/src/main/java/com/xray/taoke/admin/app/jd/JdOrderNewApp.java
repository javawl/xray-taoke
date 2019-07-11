package com.xray.taoke.admin.app.jd;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.JdOrderInfo;
import com.xray.taoke.jdapi.Jd21dsService;

public class JdOrderNewApp extends AbstractApp implements Runnable {
    private String appid;

    public JdOrderNewApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp JdOrderNewApp,appid:" + appid, e);
        }
    }

    public void doRun(String appid) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        String time = dateFormat.format(new Date(System.currentTimeMillis() - 60 * 1000));
        JSONObject obj = Jd21dsService.instance.getjdorder(appid, time, 1);
        if (obj.getInteger("code") != 200) {
            logger.info("runapp empty JdOrderNewApp,appid:{},time:{}", appid, time);
            return;
        }
        saveByObj(appid, obj.getJSONObject("data").getJSONArray("lists"));
    }

    public static int saveByObj(String appid, JSONArray array) {
        JSONObject jsonObject = null;
        JSONArray skuList_object = null;

        long tradetime = 0;
        String tradeid = null;
        String parentid = null;
        int validCode = 0;
        for (Object object : array) {
            jsonObject = JSONObject.parseObject(object.toString());
            skuList_object = jsonObject.getJSONArray("skuList");

            tradetime = jsonObject.getLongValue("orderTime");
            tradeid = jsonObject.getString("orderId");
            if (JdOrderInfo.dao.exist(tradeid))
                return 0;

            parentid = jsonObject.getString("parentid");
            for (Object object2 : skuList_object) {
                synchronized (JdOrderNewApp.class) {
                    
                    jsonObject = JSONObject.parseObject(object2.toString());
                    validCode = jsonObject.getIntValue("validCode");
                    JdOrderInfo data = new JdOrderInfo();
                    data.set("appidkey", appid);
                    data.set("tradeid", tradeid);
                    data.set("tradeprice", jsonObject.getDouble("estimateCosPrice"));
                    data.set("trademoney", jsonObject.getDouble("estimateFee"));
                    data.set("traderate", jsonObject.getDouble("commissionRate"));
                    data.set("tradetime", tradetime);
                    data.set("itemid", jsonObject.getString("skuId"));
                    data.set("itemtitle", jsonObject.getString("skuName"));
                    data.set("itemnum", jsonObject.getInteger("skuNum"));
                    data.set("tkstatus", jsonObject.getInteger("validCode"));
                    if (!StringUtil.isEmpty(parentid))
                        data.set("parentid", parentid);

                    if (!tradeid.equals(parentid))
                        data.set("tradetype", 2);

                    data.set("tkstatus", validCode);
                    data.set("dividerate", jsonObject.getDouble("finalRate"));
                    data.save();
                    logger.info("runapp,succ neworder,appid:{},tradeid:{}", appid, tradeid);
                    return 1;
                }
            }

        }
        return 0;

    }

}
