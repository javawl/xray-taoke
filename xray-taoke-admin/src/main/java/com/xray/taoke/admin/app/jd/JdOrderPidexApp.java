package com.xray.taoke.admin.app.jd;

import java.util.Calendar;
import java.util.List;

import com.xray.act.exception.RtException;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.JdOrderInfo;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.jdapi.JdItemService;
import com.xray.taoke.tkapi.TbItemService;

public class JdOrderPidexApp extends AbstractApp {

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp pidexorder", e);
        }
    }

    public void doRun() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -10);

        String itemid = null;
        String value = null;
        List<JdOrderInfo> list = JdOrderInfo.dao.queryLatestTradetime(cal.getTimeInMillis());
        logger.info("runapp pidexorder,size:{}", list.size());
        for (JdOrderInfo data : list) {
            itemid = data.getStr("itemid");
            value = JdItemService.instance.getJdPidex(itemid);
            if (StringUtil.isEmpty(value))
                continue;

            String[] values = value.split(",");
            updateJie(values[0], values[1], data);
            JdItemService.instance.delJdPidex(itemid);
        }
    }

    private void updateJie(String appid, String openid, JdOrderInfo data) {
        String userid = MpInfo.dao.queryUseridByOpenid(appid, openid);
        if (StringUtil.isEmpty(userid))
            throw new RtException("errapp,none userid,appid:" + appid + ",openid:" + openid);

        UoUser user = UoUser.dao.queryByUserId(userid);
        int verno = user.getInt("verno");

        double trademoney = data.getDouble("trademoney");
        double jierate = TbItemService.instance.getFlrate(trademoney);
        jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;

        // double dividerate = data.getDouble("dividerate");
        double jiemoney = 0;
        // if (dividerate > 0)
        jiemoney = trademoney * jierate;

        String tradeid = data.getStr("tradeid");
        if (JdOrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno) > 0) {
            logger.info("runapp,succ pidexorder,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
            UoUser.dao.updateSucc(userid, jiemoney, verno);
            wxApi.sendText(appid, openid, MpInfoService.instance.getTradeSuccByJd(appid, UoUser.dao.queryByUserId(userid), JdOrderInfo.dao.queryByTradeid(tradeid)));
            return;
        }

    }
}
