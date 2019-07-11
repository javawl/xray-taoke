package com.xray.taoke.admin.web.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.util.UuidUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.TkMass;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.utils.EmojiUtil;

@ControllerBind(controllerKey = "/tkmass")
public class TkMassController extends AbstractController {
    public void goList() {

        String refDateStr = getPara("refDateStr", "");

        String select_appid = getPara("select_appid", "");
        setAttr("select_appid", select_appid);
        PageVo page = new PageVo(getParaToInt("pno", 1));
        page.setOrderby("`booktime` desc");
        setAttr("page", page);

        Map<String, Object> cond = getCondAll();
        String appid = getPara("appid", "");
        setAttr("appid", appid);
        if (!StringUtil.isEmpty(appid))
            cond.put("appid", appid);
        if (!StringUtil.isEmpty(select_appid))
            cond.put("select_appid", select_appid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtil.isEmpty(refDateStr)) {
            refDateStr = sdf.format(new Date().getTime());
        } else {
            refDateStr = refDateStr + " 00:00:00";
        }
        setAttr("refDateStr", refDateStr);
        Date date_da = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", refDateStr);
        Date date2_da = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", refDateStr);
        long date = 0;
        long date2 = 0;
        try {
            date = DateUtil.parseBegin(date_da);
            date2 = DateUtil.parseEnd(date2_da);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cond.put("date", date);
        cond.put("date2", date2);

        List<TkMass> dataList = TkMass.dao.queryList(cond, page);
        setAttr("dataList", dataList);
        logger.debug("dataList,size:{}", dataList.size());
        List<MpInfo> list = MpInfo.dao.queryList(null, null);
        setAttr("dataList2", list);
        render("/template/pages/tk_mass/list.html");
    }

    public void goAdd() {
        String type = getPara("type", "1");
        String appid = getPara("appid");
        if (StringUtil.isNotEmpty(appid)) {
            setAttr("appid", appid);
            setAttr("action", getPara("action"));

            TkMass data = new TkMass();
            data.set("name", getPara("name"));
            setAttr("data", data);
        }
        setAttr("send_state", 1);
        render("/template/pages/tk_mass/form" + type + ".html");
    }

    public void goEdit() throws UnsupportedEncodingException {
        String seqid = getPara("seqid");
        String type = getPara("type");
        int send_state = 1;
        TkMass data = TkMass.dao.findById(seqid);
        if (data == null) {
            data = TkMass.dao.queryByMediaid(getPara("mediaid"));
            type = data.getStr("type");
            send_state = data.getInt("send_state");
        }
        setAttr("send_state", send_state);
        setAttr("data", data);
        setAttr("contentList", JSONObject.parseArray(new String(data.get("content"), "UTF-8")));
        render("/template/pages/tk_mass/form" + type + ".html");
    }

    public void doAdd() {
        String appid = getPara("appid", "");
        String name = getPara("name");
        int type = getParaToInt("type");
        String content = getPara("content");
        long booktime = 0;
        String time = getPara("time");
        int date = getParaToInt("date");
        if (date == 1) {
            booktime = DateUtil.str2Date("yyyy-MM-dd HH:mm", time).getTime();
        } else {
            Calendar cal = DateUtil.getCalendarBegin(date);
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
            booktime = cal.getTimeInMillis();
        }

        if (booktime < System.currentTimeMillis()) {
            renderRtn(RtnFactory.newFail("invalid time"));
            return;
        }

        TkMass data = new TkMass();
        String mediaid = "wx_" + UuidUtil.getUuidByJdk(true).substring(8, 24);
        data.set("mediaid", mediaid);
        data.set("name", name);
        data.set("appid", appid);

        String wechat_name = "";
        if (StringUtil.isNotEmpty(appid)) {
            wechat_name = MpInfo.dao.exists(appid).getStr("name");
        }

        data.set("wechat_name", wechat_name);
        data.set("type", type);
        data.set("content", EmojiUtil.resolveToEmojiFromByte(content));
        data.set("send_state", getParaToInt("state"));
        data.set("state", 1);
        data.set("inputtime", System.currentTimeMillis());
        data.set("inputby", getUserid());
        data.set("edittime", data.get("inputtime"));
        data.set("editby", data.get("inputby"));
        data.set("booktime", booktime);
        data.set("label", getParaToInt("label"));
        data.save();
        logger.info("doAdd,mediaid:{},opid:{},{}", mediaid, getUserid(), getRemoteIp());

        renderRtn(RtnFactory.succ);
    }

    public void doEdit() {
        String seqid = getPara("seqid");
        long booktime = 0;
        String time = getPara("time");
        int date = getParaToInt("date");
        if (date == 1) {
            booktime = DateUtil.str2Date("yyyy-MM-dd HH:mm", time).getTime();
        } else {
            Calendar cal = DateUtil.getCalendarBegin(date);
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
            booktime = cal.getTimeInMillis();
        }

        if (booktime < System.currentTimeMillis()) {
            renderRtn(RtnFactory.newFail("invalid time"));
            return;
        }

        TkMass data = TkMass.dao.findById(seqid);
        data.set("name", getPara("name"));
        data.set("content", getPara("content"));
        data.set("edittime", System.currentTimeMillis());
        data.set("booktime", booktime);
        data.set("editby", getUserid());
        data.set("send_state", getParaToInt("state"));
        data.set("label", getParaToInt("label"));
        data.update();
        logger.info("doEdit,mediaid:{},opid:{},{}", data.getStr("mediaid"), getUserid(), getRemoteIp());

        renderRtn(RtnFactory.succ);
    }

    public void doDelete() {
        String seqid = getPara("seqid");
        TkMass.dao.deleteById(seqid);
        logger.info("doDelete,seqid:{},opid:{},{}", new Object[] { seqid, getUserid(), getRemoteIp() });
        renderRtn(RtnFactory.succ);
    }

    // @Clear
    // public void doSendKeFu() {
    // List<MpKefuMsg> list =MpKefuMsg.dao.queryByWaiting(sending);
    // for (MpKefuMsg mpKefuMsg : list) {
    // long seqid = mpKefuMsg.getLong("seqid");
    // String appid = mpKefuMsg.getStr("appid");
    // String timestamp = KeFuService.instance.getTimeStamp(appid, seqid);
    // if(!StringUtil.isEmpty(timestamp)) {
    // Long customCacheTimeStamp =
    // Long.parseLong(KeFuService.instance.getTimeStamp(appid, seqid));
    // if ((System.currentTimeMillis() - customCacheTimeStamp) > 300000) {
    // mpKefuMsg.set("sendstate", 1);
    // mpKefuMsg.update();
    // logger.info("Refresh kefu sendstate appid:{}", appid);
    // }
    // }
    //
    // KeFuThread finalcustomThread = new KeFuThread(
    // mpKefuMsg);
    // new Thread(finalcustomThread).start();
    // }
    // renderRtn(RtnFactory.succ);
    // }

}
