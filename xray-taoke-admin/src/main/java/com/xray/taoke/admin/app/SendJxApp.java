package com.xray.taoke.admin.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi.Articles;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.service.AiService;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.WeixinApi;

import redis.clients.jedis.Tuple;

public class SendJxApp extends AbstractApp {
    private static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);
    private String appid;
    private String timekey;

    public SendJxApp(String appid, String timekey) {
        this.appid = appid;
        this.timekey = timekey;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp sendjx", e);
        }
    }

    public void doRun(String appid) {
        String text = MpInfoService.instance.getJxContent(appid);
        if (StringUtil.isEmpty(text)) {
            logger.info("runapp sendjx,empty content,appid:{}", appid);
            return;
        }

        int succ = 0;
        JSONObject obj = JSONObject.parseObject(text);
        String type = obj.getString("type");
        obj = obj.getJSONArray("data").getJSONObject(0);

        int end = 500;
        long now = System.currentTimeMillis();

        int size = MpInfoService.instance.getActiveSize(appid);
        int count = (size + end - 1) / end;
        String openid;
        long activetime;
        for (int i = 0; i < count; i++) {
            Set<Tuple> sets = MpInfoService.instance.getActiveByParam(appid, i * end, ((i + 1) * end > size ? size : end * (i + 1)));
            for (Tuple tuple : sets) {
                openid = tuple.getElement();
                activetime = (long) tuple.getScore();
                // 超过时间
                if (now - activetime > 172800000)
                    break;

                // 取消订阅
                if (MpInfoService.instance.isBlackJx(appid, openid))
                    continue;
                // 已发过
                if (MpInfoService.instance.isJxOpenid(appid, openid, timekey)) {
                	 continue;
                }

                if ("wx_mixed".equals(type)) {
                    wxApi.sendText(appid, openid, obj.getString("content"));
                } else {
                    String url = obj.getString("url");
                    String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
                    if (StringUtil.isEmpty(userid)) {
                        url = AiService.instance.authUrl(appid, openid, url);
                    }
                    doCustomNews(openid, obj.getString("title"), obj.getString("description"), obj.getString("picUrl"), url);
                }
                MpInfoService.instance.addJxOpenid(appid, openid, timekey);
                // 操作提醒
                if (now - activetime > 86400000) {
                    wxApi.sendText(appid, openid, "[爱心]回复数字【1】续订[爱心]\n～不回复无法继续接收～");
                }
                ++succ;
            }
        }
        logger.info("runapp sendjx,appid:{},succ:{}", appid, succ);
        MpInfoService.instance.delJxByKey(appid, DateUtil.getYesterday(), timekey);
    }

    private boolean doCustomNews(String openid, String title, String desc, String picurl, String url) {
        Articles item = new Articles();
        item.setTitle(title);
        item.setPicurl(picurl);
        item.setDescription(desc);
        item.setUrl(url);

        List<Articles> list = new ArrayList<Articles>();
        list.add(item);
        ApiResult res = wxApi.sendKeFu(appid, openid, list);
        if (!res.isSucceed()) {
            logger.error("err sendNews,doChaQuan,openid:{}, errMsg:{},{}", openid, res.getErrorCode(), res.getErrorMsg());
            return false;
        }
        return true;
    }

}
