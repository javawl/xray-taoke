package com.xray.taoke.admin.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.model.TkItemDetail;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.ItemDetailService;
import com.xray.taoke.admin.service.LinkInfoService;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.WeixinApi;

@ControllerBind(controllerKey = "/orderinfo")
public class OrderInfoController extends AbstractController {
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void goList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");
        String view = getCond(cond, "view", "list");

        if (StringUtil.isNotEmpty(getPara("day"))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtil.getDate(getPara("day"), "yyyy-MM-dd"));

            long begintime = cal.getTimeInMillis();
            cond.put("sday", begintime);

            cal.add(Calendar.DAY_OF_MONTH, 1);
            long endtime = cal.getTimeInMillis();
            cond.put("eday", endtime);
        }

        Map<String, String> map = MpInfoService.instance.getMpNames();
        Map<String, String> pidmap = LinkInfoService.instance.getLinkConfigPid();
        List<OrderInfo> dataList = OrderInfo.dao.queryList(cond, getPageVo("`tradetime` desc"));
        for (OrderInfo data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
            data.setPidname(pidmap.get(data.getStr("adzoneid")));
        }
        setAttr("mpList", MpInfo.dao.queryAllInuse());
        setAttr("dataList", dataList);
        render("/template/pages/orderinfo/" + view + ".html");
    }

    public void goItemList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");

        if (StringUtil.isNotEmpty(getPara("day"))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtil.getDate(getPara("day"), "yyyy-MM-dd"));

            long begintime = cal.getTimeInMillis();
            cond.put("sday", begintime);

            cal.add(Calendar.DAY_OF_MONTH, 1);
            long endtime = cal.getTimeInMillis();
            cond.put("eday", endtime);
        }

        Map<String, String> map = MpInfoService.instance.getMpNames();
        Map<String, String> pidmap = LinkInfoService.instance.getLinkConfigPid();
        List<OrderInfo> dataList = OrderInfo.dao.queryList(cond, getPageVo("`tradetime` desc"));
        for (OrderInfo data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
            data.setPidname(pidmap.get(data.getStr("adzoneid")));
        }
        setAttr("mpList", MpInfo.dao.queryAllInuse());
        setAttr("dataList", dataList);
        render("/template/pages/orderinfo/item_list.html");
    }

    public void goInvaildList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");

        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<OrderInfo> dataList = OrderInfo.dao.queryInvaildOrder(getCondAll(), getPageVo("`tradetime` desc"));
        for (OrderInfo data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
        }
        setAttr("mpList", MpInfo.dao.queryAllInuse());
        setAttr("dataList", dataList);
        render("/template/pages/orderinfo/list_invalid.html");
    }

    public void goChaQuan() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "appid", "");
        getCond(cond, "openid", "");
        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<TkItemDetail> dataList = TkItemDetail.dao.queryList(getCondAll(), getPageVo("`seqid` desc"));
        for (TkItemDetail data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
        }
        if (dataList.size() > 0 && getParaToInt("pno", 1) == 1 && StringUtil.isEmpty(getPara("appid"))) {
            ItemDetailService.instance.setTradeid(dataList.get(0).getStr("seqid"), getUserid());
        }
        setAttr("dataList", dataList);
        setAttr("mpList", MpInfo.dao.queryAllInuse());
        render("/template/pages/orderinfo/chaquan.html");
    }

    public void doNewItemDetail() {
        TkItemDetail detail = TkItemDetail.dao.queryOne();
        String seqid = detail.getStr("seqid");
        String userid = getUserid();

        int type = 0;
        if (!seqid.equals(ItemDetailService.instance.getTradeid(userid))) {
            ItemDetailService.instance.setTradeid(seqid, userid);
            type = 1;
        }
        renderRtn(RtnFactory.newSucc(type));

    }

    public void doReply() {
        String openid = getPara("openid");
        String appid = getPara("appid");
        String text = getPara("text");
        String seqid = getPara("seqid");
        WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);
        ApiResult apiResult = wxApi.sendText(appid, openid, text);
        if (apiResult.getErrorCode() != 0) {
            renderRtn(RtnFactory.newFail(apiResult.getErrorMsg()));
            return;
        }
        TkItemDetail detail = TkItemDetail.dao.findById(seqid);
        detail.set("type", 1);
        detail.update();
        renderRtn(RtnFactory.succ);
    }

    @Clear
    public void doUserInfo() {
        List<TkItemDetail> dataList = TkItemDetail.dao.queryListBy();
        for (TkItemDetail data : dataList) {
            MpUser mpUser = MpUser.dao.queryWxnameByOpenid(data.getStr("appid"), data.getStr("openid"));
            data.set("wxavatar", mpUser.getStr("wxavatar"));
            data.set("wxname", mpUser.get("wxname"));
            data.set("state", 1);
            data.update();
        }
        renderRtn(RtnFactory.succ);
    }

    public void doUnBindOrder() {
        String seqid = getPara("seqid");
        OrderInfo data = OrderInfo.dao.findById(seqid);
        int tk_status = data.getInt("tkstatus");
        if (tk_status != 12) {
            renderRtn(RtnFactory.newFail("只能解绑付款的订单！"));
            return;
        }
        String userid = data.getStr("userid");
        if (StringUtil.isEmpty(userid)) {
            renderRtn(RtnFactory.newFail("该订单未绑定用户！不能解绑！"));
            return;
        }
        double money = data.getDouble("jiemoney");
        UoUser uoUser = UoUser.dao.queryByUserId(userid);
        int verno = uoUser.getInt("verno");
        if (UoUser.dao.updateUnBind(userid, money, verno) > 0) {
            data.set("userid", "");
            data.set("alimoney", 0.0);
            data.set("alirate", 0);
            data.set("alitime", 0);
            data.set("state", 0);
            data.set("verno3", 0);
            data.set("verno12", 0);
            data.set("verno13", 0);
            data.set("autobind", 0);
            data.set("wxname", null);
            data.set("wxavatar", "");
            data.set("jierate", 0);
            data.set("jiemoney", 0);
            data.update();
            logger.info("doUnBindOrder,userid:{},money:{},id:{}", userid, money, seqid);
        }

        renderRtn(RtnFactory.succ);
    }

}
