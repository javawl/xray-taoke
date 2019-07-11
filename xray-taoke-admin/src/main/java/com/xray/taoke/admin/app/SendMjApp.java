package com.xray.taoke.admin.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi.Articles;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.service.AiService;
import com.xray.taoke.admin.service.MpInfoService;

public class SendMjApp extends AbstractApp {
    private String appid;
    @SuppressWarnings("unused")
    private int delay;

    public SendMjApp(String appid, int delay) {
        this.appid = appid;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp sendmj", e);
        }
    }

    public void doRun(String appid) {
        // String text = MpInfoService.instance.getMjContent(appid);
        String text = MpInfoService.instance.getJxContent(appid);
        if (StringUtil.isEmpty(text)) {
            logger.info("runapp sendmj,empty content,appid:{}", appid);
            return;
        }

        int succ = 0;
        JSONObject obj = JSONObject.parseObject(text);
        String type = obj.getString("type");
        obj = obj.getJSONArray("data").getJSONObject(0);

        String openid;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        long endtime = cal.getTimeInMillis();
        cal.add(Calendar.MINUTE, -5);
        long begintime = cal.getTimeInMillis();
        List<MpInfo> list = MpInfo.dao.queryByFiveMin(appid, begintime, endtime);
        for (MpInfo data : list) {
            openid = data.getStr("openid");
            if (MpInfoService.instance.isMjOpenid(appid, openid, data.getLong("edittime")))
                break;

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
            MpInfoService.instance.addMjOpenid(appid, openid, data.getLong("edittime"));
            ++succ;
        }
        logger.info("runapp sendmj,appid:{},succ:{}", appid, succ);
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
