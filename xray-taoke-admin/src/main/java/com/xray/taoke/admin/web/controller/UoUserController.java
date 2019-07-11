package com.xray.taoke.admin.web.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.LinkConfig;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.UoUserService;
import com.xray.taoke.admin.service.WeixinApi;
import com.xray.taoke.admin.utils.DownloadUtil;
import com.xray.taoke.admin.utils.ImageUtil;

@ControllerBind(controllerKey = "/uouser")
public class UoUserController extends AbstractController {
    static WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);

    public void goList() {
        String orderby = getPara("orderby", "`seqid` desc");
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");
        getCond(cond, "wxname", "");
        setAttr("orderby", orderby);
        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<UoUser> dataList = UoUser.dao.queryList(cond, getPageVo(orderby));
        for (UoUser data : dataList) {
            data.setMpname(map.get(data.getStr("bindappid")));
        }

        setAttr("dataList", dataList);
        setAttr("mpList", MpInfo.dao.queryAllInuse());

        setAttr("configList", LinkConfig.dao.queryList(null, null));

        render("/template/pages/uouser/list.html");
    }

    public void goProxyList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");
        getCond(cond, "wxname", "");
        getCond(cond, "proxystate", "1");
        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<UoUser> dataList = UoUser.dao.queryList(cond, getPageVo("`seqid` desc"));
        for (UoUser data : dataList) {
            data.setMpname(map.get(data.getStr("bindappid")));
        }
        setAttr("dataList", dataList);
        setAttr("mpList", MpInfo.dao.queryAllInuse());
        render("/template/pages/uouser/proxy_list.html");
    }

    public void doEditRate() {
        UoUser.dao.updateRate(getParaToLong("seqid"), Double.parseDouble(getPara("rate")), getPara("ratereason"), getUser().getName());
        renderRtn(RtnFactory.succ);
    }

    public void doEditProxystate() {
        long seqid = getParaToLong("seqid");
        int state = getParaToInt("state");
        UoUser uoUser = UoUser.dao.findById(seqid);
        if (state == 2) {
            if (!StringUtil.isEmpty(uoUser.getStr("proxycode"))) {
                renderRtn(RtnFactory.succ);
                return;
            }

            double proxyrate = Double.parseDouble(getPara("proxyrate"));
            // String proxycode = getPara("invitecode");

            String proxycode = "F" + uoUser.getLong("seqid");

            String appid = uoUser.getStr("bindappid");

            String url = wxApi.CreateQrcode(appid, proxycode);
            String before_img = PathKit.getWebRootPath() + "/upload/codeimg/" + "b34f9ee18e77b852f6826761cd06670.jpg";
            url = DownloadUtil.getFilePath(url);

            url = ImageUtil.generateCode(url, before_img);
            String openid = MpUser.dao.queryOpenidByUserid(appid, uoUser.getStr("userid"));

            ApiResult result = wxApi.queryByOpenid(appid, openid);
            String headimgurl = result.getStr("headimgurl");
            headimgurl = DownloadUtil.getFilePath(headimgurl);

            Map<String, String> cond = ImageUtil.generateCode2(headimgurl, url);

            ApiResult apiResult = wxApi.uploadImg(appid, cond.get("imgName"));
            String json = apiResult.getJson();
            // File file = new File(url);
            // file.delete();
            String downpath = cond.get("downpath");

            JSONObject jsonObject = JSONObject.parseObject(json);
            String media_id = jsonObject.getString("media_id");
            uoUser.set("proxystate", state);
            uoUser.set("proxyrate", proxyrate);
            uoUser.set("proxycode", proxycode);
            uoUser.set("proxyqrid", media_id);
            if (StringUtil.isNotEmpty(downpath))
                uoUser.set("haibaopath", downpath);

            uoUser.update();
            wxApi.sendText(appid, openid, "[玫瑰]申请通过，分享海报即可邀请用户~");
            wxApi.sendImg(appid, openid, media_id);
        } else if (state == 3) {
            uoUser.set("proxystate", state);
            uoUser.update();
        }
        renderRtn(RtnFactory.succ);
    }

    public void doEditProxyAll() {
        String seqid = getPara("seqid");
        String pkey = getPara("pid");

        UoUser uoUser = UoUser.dao.findById(seqid);
        uoUser.set("proxypkey", pkey);
        uoUser.set("isproxyall", 1);
        uoUser.update();
        renderRtn(RtnFactory.succ);
    }

    public void doFabu() {
        List<UoUser> list = UoUser.dao.queryProxyAll();
        String pid = null;
        String seqid = null;
        LinkConfig config = null;
        String adzoneid = null;
        for (UoUser uoUser : list) {
            seqid = uoUser.getStr("proxypkey");
            config = LinkConfig.dao.findById(seqid);
            if (config == null)
                continue;
            pid = config.getStr("pid");
            adzoneid = pid.split("_")[3];
            UoUserService.instance.setProxy(adzoneid, uoUser.getStr("userid"));
        }
        renderRtn(RtnFactory.succ);
    }

    public void doDel() {
        UoUserService.instance.delProxy();
        renderRtn(RtnFactory.succ);
    }

}
