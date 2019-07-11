package com.xray.taoke.act.web.controller;

import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.xray.act.jfinal.JfController;
import com.xray.act.util.StringUtil;
import com.xray.act.util.UuidUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.taoke.act.common.Constant;
import com.xray.taoke.act.model.MpUser;
import com.xray.taoke.act.model.UoUser;
import com.xray.taoke.act.service.AiService;
import com.xray.taoke.act.service.MpInfoService;

@ControllerBind(controllerKey = "/auth")
public class AuthController extends JfController {

    public void index() {
        renderError(403);
    }

    public void goBind() {
        String aid = getPara("aid");
        if (AiService.instance.isClick(aid)) {
            redirect(AiService.instance.getUrl(aid));
            return;
        }
        setAttr("url", Constant.getAuthorizeURL(aid));
        render("/html/auth.html");
    }

    public void doBind() {
        String code = getPara("code");
        String aid = getPara("state");
        Map<String, String> map = AiService.instance.getMap(aid);
        if (map == null || map.size() <= 0) {
            renderRtn(RtnFactory.fail);
            return;
        }

        String appid = map.get("appid");
        String openid = map.get("openid");
        String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
        if (StringUtil.isEmpty(userid)) {
            // 绑定
            SnsAccessToken data = SnsAccessTokenApi.getSnsAccessToken(Constant.wxsns_appid, Constant.wxsns_appsecret, code);
            String uopenid = data.getOpenid();
            userid = UoUser.dao.queryUseridByOpenid(uopenid);
            if (StringUtil.isEmpty(userid)) {
                userid = UuidUtil.getUuidByJdk(true);
                UoUser.dao.addData(userid, uopenid, appid);
                // 代理
                addProxy(appid, openid, userid);
            }
            MpUser.dao.updateUseridByOpenid(appid, openid, userid);
        }
        redirect(map.get("url"));
    }

    private void addProxy(String appid, String openid, String userid) {
        String proxycode = MpInfoService.instance.getProxycode(appid, openid);

        logger.info("addProxy,proxycode:{},appid:{},openid:{}", proxycode, appid, openid);

        if (StringUtil.isEmpty(proxycode))
            return;

        UoUser uoUser = UoUser.dao.queryByProxycode(proxycode);
        if (uoUser == null)
            return;

        String proxy_userid = uoUser.getStr("userid");
        UoUser.dao.updateProxyby(proxy_userid, userid);
        MpInfoService.instance.delProxycode(appid, openid);

        // 上级openid
        String proxy_openid = MpUser.dao.queryOpenidByUserId(appid, proxy_userid);
        if (StringUtil.isEmpty(proxy_openid)) {
            logger.error("err addProxy,empty proxy_openid,appid:{},userid:{}", appid, proxy_userid);
            return;
        }

        UoUser.dao.updateSuccProxyPeople(proxy_userid);
        CustomServiceApi.sendText(proxy_openid, MpInfoService.instance.getProxypeople(appid));
    }

}
