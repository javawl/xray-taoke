package com.xray.taoke.admin.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.TkMaterial;
import com.xray.taoke.admin.service.MpInfoService;

@ControllerBind(controllerKey = "/mpinfo")
public class MpInfoController extends AbstractController {

    public void goList() {
        setAttr("wxapi_url", Constant.wxapi_url);

        Map<String, Object> cond = getCondAll();
        getCond(cond, "state", "1");

        setAttr("dataList", MpInfo.dao.queryList(cond, getPageVo("`seqid` desc")));
        render("/template/pages/mpinfo/list.html");
    }

    public void goAdd() {
        render("/template/pages/mpinfo/form.html");
    }

    public void goEdit() {
        setAttr("data", MpInfo.dao.findById(getPara("seqid")));
        render("/template/pages/mpinfo/form.html");
    }

    public void doAdd() {
        MpInfo data = new MpInfo();
        data.set("appid", getPara("appid"));
        data.set("name", getPara("name"));
        data.set("appsecret", getPara("appsecret"));
        data.set("token", getPara("token"));
        data.set("encodingaeskey", getPara("encodingaeskey"));
        data.set("session", getPara("session", ""));
        data.set("pid", getPara("pid"));
        data.set("tbname", getPara("tbname"));
        data.set("shopurl", getPara("shopurl", ""));
        data.set("apikey", getPara("apikey"));
        data.set("inputtime", System.currentTimeMillis());
        data.set("pidex", getPara("pidex"));
        data.set("wxpayid", getPara("wxpayid"));
        data.set("templateid", getPara("templateid"));

        data.set("jdkey", getPara("jdkey"));
        data.set("jdid", getPara("jdid"));

        data.set("pdname", getPara("pdname", ""));

        data.save();
        renderRtn(RtnFactory.succ);
    }

    public void doEdit() {
        MpInfo data = MpInfo.dao.findById(getPara("id"));
        data.set("name", getPara("name"));
        data.set("appsecret", getPara("appsecret"));
        data.set("token", getPara("token"));
        data.set("encodingaeskey", getPara("encodingaeskey"));
        data.set("session", getPara("session", ""));
        data.set("pid", getPara("pid"));
        data.set("tbname", getPara("tbname"));
        data.set("shopurl", getPara("shopurl", ""));
        data.set("apikey", getPara("apikey"));
        data.set("pidex", getPara("pidex"));
        data.set("wxpayid", getPara("wxpayid"));
        data.set("templateid", getPara("templateid"));

        data.set("jdkey", getPara("jdkey"));
        data.set("jdid", getPara("jdid"));

        data.set("pdname", getPara("pdname", ""));

        data.update();
        renderRtn(RtnFactory.succ);
    }

    public void doCreateByAppid() {
        MpInfo data = MpInfo.dao.findById(getPara("seqid"));

        String appid = data.getStr("appid");
        MpInfo.dao.createUserByAppid(appid);

        JSONObject obj = new JSONObject();
        obj.put("type", "text");
        JSONArray arr = new JSONArray();
        arr.add(obj);

        TkMaterial mdata = null;
        long time = System.currentTimeMillis();
        String[] words = new String[] { "客服" };
        for (String word : words) {
            obj.put("content", word);

            mdata = new TkMaterial();
            mdata.set("mediaid", Constant.toMediaid(word));
            mdata.set("appid", appid);
            mdata.set("type", 2);
            mdata.set("name", word);
            mdata.set("word", word);
            mdata.set("content", arr.toJSONString());
            mdata.set("inputby", getUserid());
            mdata.set("inputtime", time);
            mdata.set("state", 1);
            mdata.save();
        }
        data.set("state", 1);
        data.update();
        renderRtn(RtnFactory.succ);
    }

    public void doDel() {
        String seqid = getPara("seqid");
        MpInfo mpinfo = MpInfo.dao.findById(seqid);
        mpinfo.set("state", -1);
        mpinfo.update();
        renderRtn(RtnFactory.succ);
    }

    public void doFaBu() {
        String seqid = getPara("seqid");
        MpInfo data = MpInfo.dao.findById(seqid);
        MpInfoService.instance.setMpInfo(data.getStr("appid"), data);
        MpInfoService.instance.setMpNames();
        renderRtn(RtnFactory.succ);
    }

    public void doFaBus() {
        String idsStr = getPara("idsStr");
        List<MpInfo> list = MpInfo.dao.queryByIds(Arrays.asList(idsStr.split(",")));
        for (MpInfo data : list) {
            MpInfoService.instance.setMpInfo(data.getStr("appid"), data);
        }
        MpInfoService.instance.setMpNames();
        renderRtn(RtnFactory.succ);
    }

    public void doUpdateState() {
        String seqid = getPara("seqid");
        int state = getParaToInt("state");
        MpInfo info = MpInfo.dao.findById(seqid);
        info.set("state", state);
        info.update();
        renderRtn(RtnFactory.succ);
    }

    public void doEditRemark() {
        String seqid = getPara("seqid");
        String remark = getPara("remark");
        MpInfo info = MpInfo.dao.findById(seqid);
        info.set("remark", remark);
        info.update();
        renderRtn(RtnFactory.succ);
    }

}
