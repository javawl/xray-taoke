package com.xray.taoke.admin.app;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xray.taoke.admin.model.Cashinfo;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.model.UoUser;

public class UserInfoApp extends AbstractApp implements Runnable {

    private String appid;

    public UserInfoApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
            doUoUserInfo(appid);
        } catch (Exception e) {
            logger.error("errapp userinfo,appid:" + appid, e);
        }
    }

    public void doRun(String appid) {
        List<MpInfo> list = MpInfo.dao.queryByInfollow(appid);
        logger.info("runapp userinfo,size:{}", list.size());

        JSONObject obj = null;
        String openid = null;
        for (MpInfo data : list) {
            openid = data.getStr("openid");
            try {
                obj = JSONObject.parseObject(wxApi.queryByOpenid(appid, openid).getJson());
                if (obj.getInteger("subscribe") == 0)
                    MpInfo.dao.updateUserInfoNotInf(appid, openid);
                else
                    MpInfo.dao.updateUserInfo(appid, openid, obj.getIntValue("sex"), obj.getIntValue("subscribe"), obj.getString("nickname"), obj.getString("headimgurl"));
            } catch (Exception e) {
                logger.error("errapp userinfo,appid:" + appid + ",openid:" + openid, e);
            }
        }
    }

    public void doUoUserInfo(String appid) {
        UoUser.dao.updateWxInfo(appid);
    }

    public static void doCashUserInfo() {
        Cashinfo.dao.updateWxInfo();
    }

    public static void doOrderUserInfo() {
        OrderInfo.dao.updateWxInfo();
    }

}
