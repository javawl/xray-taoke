package com.xray.taoke.act.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.xray.act.jfinal.weixin.MsgInterceptor;
import com.xray.act.jfinal.weixin.MsgProvider;
import com.xray.act.util.StringUtil;
import com.xray.taoke.act.model.ItemDetail;
import com.xray.taoke.act.model.MpUser;
import com.xray.taoke.act.service.MpInfoService;
import com.xray.taoke.act.service.WxAccessTokenCache;

@ControllerBind(controllerKey = "/wxai")
public class WxAiController extends MsgControllerAdapter implements MsgProvider {
    protected static Logger logger = LoggerFactory.getLogger(WxAiController.class);
    private static WxAccessTokenCache accessTokenCache = new WxAccessTokenCache();

    @Before(MsgInterceptor.class)
    public void index() {
        super.index();
    }

    @Override
    public ApiConfig getApiConfig() {
        String appid = getPara();
        Map<String, String> cond = MpInfoService.instance.getMpInfo(appid);
        ApiConfig ac = new ApiConfig();
        ac.setAppId(appid);
        ac.setAppSecret(cond.get("appsecret"));
        ac.setToken(cond.get("token"));
        ac.setEncodingAesKey(cond.get("encodingaeskey"));
        ApiConfigKit.putApiConfig(ac);
        ApiConfigKit.setThreadLocalAppId(appid);
        ApiConfigKit.setAccessTokenCache(accessTokenCache);
        return ac;
    }

    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {
        String appid = getPara();
        String openid = inFollowEvent.getFromUserName();
        renderDefault();
        // 取关
        if (inFollowEvent.getEvent().equals(InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE)) {
            logger.info("unsubscribe,appid:{},openid:{}", appid, openid);
            MpInfoService.instance.negative(appid, openid);
            MpUser.dao.updateInfollowByOpenid(appid, openid);
            return;
        }

        MpInfoService.instance.active(appid, openid);
        MpUser.dao.addByOpenid(appid, openid);
        // 客服
        this.doKefu(appid, openid);
        logger.info("inFollowEvent,appid:{},openid:{}", appid, openid);
    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
        String appid = getPara();
        String openid = inMenuEvent.getFromUserName();
        renderDefault();
        // 链接不处理
        if (!"CLICK".equals(inMenuEvent.getEvent())) {
            return;
        }
        MpInfoService.instance.active(appid, openid);
        // 客服
        this.doKefu(appid, openid);
        logger.info("inMenuEvent,appid:{},openid:{}", appid, openid);
    }

    @Override
    protected void processInImageMsg(InImageMsg inImageMsg) {
        ItemDetail.dao.insertIntoDetail(getPara(), inImageMsg.getFromUserName(), "[图片]");
        super.processInImageMsg(inImageMsg);
    }

    @Override
    protected void processInLinkMsg(InLinkMsg inLinkMsg) {
        ItemDetail.dao.insertIntoDetail(getPara(), inLinkMsg.getFromUserName(), "[链接]");
        super.processInLinkMsg(inLinkMsg);
    }

    @Override
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        ItemDetail.dao.insertIntoDetail(getPara(), inVideoMsg.getFromUserName(), "[视频]");
        super.processInVideoMsg(inVideoMsg);
    }

    @Override
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        ItemDetail.dao.insertIntoDetail(getPara(), inVoiceMsg.getFromUserName(), "[音频]");
        super.processInVoiceMsg(inVoiceMsg);
    }

    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {
        String appid = getPara();
        String openid = inTextMsg.getFromUserName();
        renderDefault();

        try {
            // 活跃
            MpInfoService.instance.active(appid, openid);
            String content = inTextMsg.getContent();

            if (!MpInfoService.instance.isKefu(appid, openid))
                ItemDetail.dao.insertIntoDetail(appid, openid, content);

            // 客服功能
            if (content.equals("开客服")) {
                MpInfoService.instance.addKefu(appid, openid);
                CustomServiceApi.sendText(openid, "打开客服模式");
                logger.info("addKefu,appid:{},openid:{}", appid, openid);
                return;
            }
            if (content.equals("关客服")) {
                MpInfoService.instance.delKefu(appid, openid);
                CustomServiceApi.sendText(openid, "关闭客服模式");
                logger.info("delKefu,appid:{},openid:{}", appid, openid);
                return;
            }
        } catch (Exception e) {
            CustomServiceApi.sendText(openid, MpInfoService.instance.getNetErrContent(appid));
        }
    }

    protected void doKefu(String appid, String openid) {
        String kefuid = MpUser.dao.queryKefuidByOpenid(appid, openid);
        if (StringUtil.isNotEmpty(kefuid)) {
            CustomServiceApi.sendText(openid, MpInfoService.instance.getFollowContent(appid));
            CustomServiceApi.sendImage(openid, kefuid);
        } else {
            String content = MpInfoService.instance.getKefuContent(appid);
            if (StringUtil.isNotEmpty(content)) {
                JSONObject obj = null;
                JSONArray arr = JSONObject.parseObject(content).getJSONArray("data");
                if (arr.size() > 1) {
                    CustomServiceApi.sendText(openid, MpInfoService.instance.getFollowContent(appid));
                    obj = arr.getJSONObject((int) (Math.random() * arr.size()));
                    kefuid = obj.getString("mediaid");
                    MpUser.dao.updateKefuidByOpenid(appid, openid, kefuid);
                    CustomServiceApi.sendImage(openid, kefuid);
                } else {
                    obj = arr.getJSONObject(0);
                    this.doCustomMsg(openid, obj);
                }

            }
        }
        logger.info("doKefu,appid:{},openid:{}", appid, openid);
    }

    protected boolean doCustomMsg(String openid, JSONObject obj) {
        if ("text".equals(obj.getString("type"))) {
            CustomServiceApi.sendText(openid, obj.getString("content"));
            return true;
        }
        if ("image".equals(obj.getString("type"))) {
            CustomServiceApi.sendImage(openid, obj.getString("mediaid"));
            return true;
        }
        return false;
    }
}
