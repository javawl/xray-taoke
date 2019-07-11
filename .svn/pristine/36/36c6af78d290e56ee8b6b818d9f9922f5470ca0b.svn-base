package com.xray.taoke.admin.web.controller;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.Cashinfo;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.WxPayService;

@ControllerBind(controllerKey = "/cashinfo")
public class CashinfoController extends AbstractController {

    public void goList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");
        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<Cashinfo> dataList = Cashinfo.dao.queryList(cond, getPageVo("`seqid` desc"));
        for (Cashinfo data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
        }
        setAttr("dataList", dataList);
        setAttr("mpList", MpInfo.dao.queryAllInuse());

        render("/template/pages/cashinfo/list.html");
    }

    public void doTransfers() {
        long seqid = getParaToLong("seqid");
        String return_msg = WxPayService.instance.doTransfers(Cashinfo.dao.findById(seqid));
        renderRtn(RtnFactory.newSucc(return_msg));
    }

    public void doResetTransfers() {
        long seqid = getParaToLong("seqid");
        Cashinfo cashinfo = Cashinfo.dao.findById(seqid);
        // if (cashinfo.getInt("paystate") != -1) {
        // renderRtn(RtnFactory.newFail("该订单不能重复打款！"));
        // return;
        // }
        String return_msg = WxPayService.instance.doResetTrans(cashinfo);
        renderRtn(RtnFactory.newSucc(return_msg));
    }

    public static void main(String[] args) {
        String url = "http://api-gw.haojingke.com/index.php/api/platform/openapi";
        String data = "type=goodslist&apikey=286fedb525ca9c79&cname=酒类";
        JSONObject obj = JSONObject.parseObject(HttpUtils.post(url, data));
        System.out.println(obj);
    }
}
