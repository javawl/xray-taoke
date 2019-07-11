package com.xray.taoke.admin.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Enhancer;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.admin.service.WeixinApi;

public class WxTmplmsgNewUtil {
    private static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);

    public static ApiResult sendTMG(String appid, String openid, String template_id, String name, double cashnum, String title) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keyword1 = new HashMap<String, String>();
        Map<String, String> keyword2 = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();
        first.put("value", "确认收货成功，" + cashnum + "元已到账，回复【查询】查看账号信息\n");
        first.put("color", "#ff0000");
        keyword1.put("value", "确认收货成功");
        keyword1.put("color", "#173177");
        keyword2.put("value", title);
        keyword2.put("color", "#173177");

        data.put("first", first);
        data.put("keyword2", keyword2);
        data.put("keyword1", keyword1);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", openid);
        paraMap.put("template_id", template_id);
        paraMap.put("url", "");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);
        return apiResult;
    }

    public static ApiResult sendCanCashTMG(String appid, String openid, String template_id, double cashnum) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keyword1 = new HashMap<String, String>();
        Map<String, String> keyword2 = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();
        String val = Constant.formatPrice(cashnum);

        first.put("value", "亲，返利金额" + val + "元可提现，回复【提现】系统24小时自动到账\n");
        first.put("color", "#ff0000");
        keyword1.put("value", "返利金额提现");
        keyword1.put("color", "#173177");

        keyword2.put("value", "回复【查询】查看账号信息");
        keyword2.put("color", "#173177");

        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", openid);
        paraMap.put("template_id", template_id);
        paraMap.put("url", "");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);
        return apiResult;
    }

    public static ApiResult sendConfirmTMG(String appid, String openid, String template_id, double cashnum) {
        Map<String, Object> data = new TreeMap<String, Object>();
        Map<String, String> first = new HashMap<String, String>();
        Map<String, String> keyword1 = new HashMap<String, String>();
        Map<String, String> keyword2 = new HashMap<String, String>();
        Map<String, String> remark = new HashMap<String, String>();
        first.put("value", "亲，确认收货后，即可提现返利哦，~回复【查询】查看账号信息\n");
        first.put("color", "#ff0000");
        keyword1.put("value", "未确认收货");
        keyword1.put("color", "#173177");
        keyword2.put("value", "回复【查询】查看账号信息！");
        keyword2.put("color", "#173177");

        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("remark", remark);
        Map<String, Object> paraMap = new HashMap<String, Object>();

        paraMap.put("touser", openid);
        paraMap.put("template_id", template_id);
        paraMap.put("url", "");
        paraMap.put("data", data);
        String jsonStr = JSON.toJSONString(paraMap);
        ApiResult apiResult = wxApi.sendTemplate(appid, jsonStr);
        return apiResult;
    }

}
