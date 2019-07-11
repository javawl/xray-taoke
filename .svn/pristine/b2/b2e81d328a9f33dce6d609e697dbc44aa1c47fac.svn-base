package com.xray.taoke.admin.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Enhancer;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.act.util.DateUtil;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.admin.service.WeixinApi;

public class WxTmplmsgUtil {
    private static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);

    public static void sendTMG(String appid, String openid, String name, double cashnum) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keynote1 = new HashMap<String, String>();
        Map<String, String> event = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();
        first.put("value", "亲爱的" + EmojiUtil.resolveToEmojiFromByte(name));
        first.put("color", "#173177");
        keynote1.put("value", DateUtil.date2Str("yyyy-MM-dd HH:mm:ss", new Date()));
        keynote1.put("color", "#173177");
        event.put("value", "亲亲，你有" + cashnum + "元,可提现的金额哦！快发送提现试试吧！");
        event.put("color", "#173177");

        remark.put("value", "可提现提示");
        remark.put("color", "#173177");

        data.put("first", first);
        data.put("event", event);
        data.put("finish_time", keynote1);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", openid);
        paraMap.put("template_id", "gSA94uZmwITwvELRVPaoidLjCMPW8pQXha8PhRkTkC4");
        paraMap.put("url", "");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);

        System.out.println(apiResult);
    }

    public static void sendHaveMoneyTMG(String appid, String openid, String name, double cashnum, double qconfirm,String templateid) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keynote1 = new HashMap<String, String>();
        Map<String, String> event = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();
        first.put("value", "亲爱的" + EmojiUtil.resolveToEmojiFromByte(name));
        first.put("color", "#173177");
        keynote1.put("value", DateUtil.date2Str("yyyy-MM-dd HH:mm:ss", new Date()));
        keynote1.put("color", "#173177");
        event.put("value", "亲亲，你有" + cashnum + "元,可提现的金额和" + qconfirm + "元未收货金额哦！快发送提现试试吧！");
        event.put("color", "#173177");

        remark.put("value", "余额提示");
        remark.put("color", "#173177");

        data.put("first", first);
        data.put("event", event);
        data.put("finish_time", keynote1);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", openid);
        paraMap.put("template_id", templateid);
        paraMap.put("url", "");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);

        System.out.println(apiResult);
    }

    public static void sendMoreMoneyTMG(String appid, String name, int size) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keynote1 = new HashMap<String, String>();
        Map<String, String> event = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();

        first.put("value", EmojiUtil.resolveToEmojiFromByte(name));
        first.put("color", "#173177");
        keynote1.put("value", DateUtil.date2Str("yyyy-MM-dd HH:mm:ss", new Date()));
        keynote1.put("color", "#173177");
        event.put("value", "已有" + size + "条提现金额大于5元的用户发出提现申请，请速查看！");
        event.put("color", "#173177");

        remark.put("value", "用户提现报告");
        remark.put("color", "#173177");

        data.put("first", first);
        data.put("event", event);
        data.put("finish_time", keynote1);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", Constant.getOpenid(name));
        paraMap.put("template_id", "gSA94uZmwITwvELRVPaoidLjCMPW8pQXha8PhRkTkC4");
        paraMap.put("url", "http://taoke.liangdianpro.com/taoke_admin/cashinfo/goList");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);

        System.out.println(apiResult);
    }

    public static ApiResult pepCashMsg(String appid, String name, int size) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keynote1 = new HashMap<String, String>();
        Map<String, String> event = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();

        first.put("value", EmojiUtil.resolveToEmojiFromByte(name));
        first.put("color", "#173177");
        keynote1.put("value", DateUtil.date2Str("yyyy-MM-dd HH:mm:ss", new Date()));
        keynote1.put("color", "#173177");
        event.put("value", "有" + size + "条提现申请尚未处理，请速查看！");
        event.put("color", "#173177");

        remark.put("value", "用户提现报告");
        remark.put("color", "#173177");

        data.put("first", first);
        data.put("event", event);
        data.put("finish_time", keynote1);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", Constant.getOpenid(name));
        paraMap.put("template_id", "gSA94uZmwITwvELRVPaoidLjCMPW8pQXha8PhRkTkC4");
        paraMap.put("url", "http://taoke.liangdianpro.com/taoke_admin/cashinfo/goList");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);
        return apiResult;
    }

}
