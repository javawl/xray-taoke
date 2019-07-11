package com.xray.taoke.admin.web.controller;

import java.util.List;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.LinkConfig;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.service.LinkInfoService;
import com.xray.taoke.admin.service.MpInfoService;

@ControllerBind(controllerKey = "/linkconfig")
public class LinkConfigController extends AbstractController {

    public void goList() {
        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<LinkConfig> dataList = LinkConfig.dao.queryList(getCondAll(), getPageVo("seqid desc"));
        for (LinkConfig data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
        }
        setAttr("dataList", dataList);
        setAttr("pkey", Integer.valueOf(LinkInfoService.instance.getPkey(getUserid())));
        render("/template/pages/linkconfig/list.html");
    }

    public void goAdd() {
        setAttr("mpList", MpInfo.dao.queryAllLine());
        render("/template/pages/linkconfig/form.html");
    }

    public void goEdit() {
        setAttr("mpList", MpInfo.dao.queryAllLine());
        setAttr("data", LinkConfig.dao.findById(getPara("seqid")));
        render("/template/pages/linkconfig/form.html");
    }

    public void doDel() {
        String seqid = getPara("seqid");
        LinkConfig.dao.deleteById(seqid);
        renderRtn(RtnFactory.succ);
    }

    public void doAdd() {
        LinkConfig data = new LinkConfig();
        data.set("appid", getPara("appid"));
        data.set("sid", getPara("sid"));
        data.set("pid", getPara("pid"));
        data.set("remark", getPara("remark"));
        data.set("inputtime", System.currentTimeMillis());
        data.save();
        renderRtn(RtnFactory.succ);
    }

    public void doEdit() {
        LinkConfig data = LinkConfig.dao.findById(getPara("seqid"));
        data.set("appid", getPara("appid"));
        data.set("sid", getPara("sid"));
        data.set("pid", getPara("pid"));
        data.set("remark", getPara("remark"));
        data.update();
        renderRtn(RtnFactory.succ);
    }

    public void doFabu() {
        List<LinkConfig> list = LinkConfig.dao.queryList(null, null);
        for (LinkConfig data : list) {
            MpInfoService.instance.setPkeyPid(String.valueOf(data.getLong("seqid")), data.getStr("pid"));
        }
        LinkInfoService.instance.addLinkConfig(list);
        renderRtn(RtnFactory.succ);
    }

    public void doBind() {
        LinkInfoService.instance.setPkey(getUserid(), getPara("seqid"));
        renderRtn(RtnFactory.succ);
    }

}
