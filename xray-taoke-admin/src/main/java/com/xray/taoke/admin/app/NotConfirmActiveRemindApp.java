package com.xray.taoke.admin.app;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.MpUserService;
import com.xray.taoke.admin.utils.WxTmplmsgNewUtil;

public class NotConfirmActiveRemindApp extends AbstractApp implements Runnable {
    private String appid;

    public NotConfirmActiveRemindApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp NotConfirmActiveRemindApp,appid:" + appid, e);
        }
    }

    public void doRun() throws Exception {

        long weeks_long = System.currentTimeMillis() - 604800000;

        List<OrderInfo> list = OrderInfo.dao.queryOrderByWeek(weeks_long, appid);
        if (list.size() == 0) {
            logger.info("runapp,NotConfirmActiveRemindApp,no list size:0,appid:{}", appid);
            return;
        }
        String userid = null;
        String openid = null;
        double val = 0.0;
        long sys_time = System.currentTimeMillis();
        double qbcash = 0.0;
        long todayBegin = getTodayBegin();
        Double value = 0.0;
        BigDecimal bg = null;
        int count = 0;

        Double active_time = null;
        for (OrderInfo data : list) {
            userid = data.getStr("userid");
            MpUser mpUser = MpUser.dao.queryByUserid(appid, userid);
            if (mpUser == null)
                continue;
            openid = mpUser.getStr("openid");

            // 48小时只能发一次
            value = MpUserService.instance.getDoubleTwoDayByUserid(userid);
            bg = new BigDecimal(value + "");
            if ((sys_time - 172800000) < bg.longValue())
                continue;

            active_time = MpInfoService.instance.getActiveByOpenid(appid, openid);
            // 如果没有关注时间，说明取关
            if (active_time == null)
                continue;

            bg = new BigDecimal(active_time + "");
            // 活跃的过滤
            if ((sys_time - 172800000) < bg.longValue())
                continue;

            // 今日已发过结算通知,不再发
            value = MpUserService.instance.getDoubleByUserid(userid);
            bg = new BigDecimal(value + "");

            if (bg.longValue() > todayBegin)
                continue;

            String template_id = MpInfoService.instance.getMpInfo(appid).get("templateid");
            if (StringUtil.isEmpty(template_id)) {
                logger.error("runapp,NotActiveRemindApp,no this template_id,appid:{}", appid);
                return;
            }

            count++;

            // ApiResult apiResult = null;
            // apiResult = WxTmplmsgNewUtil.sendConfirmTMG(appid, openid, template_id,
            // qbcash);
            //
            // if (apiResult.getErrorCode() != 0) {
            // logger.info("runapp,NotActiveRemindApp,sendTMG,appid:{},openid:{},msg:{}",
            // appid, openid, apiResult.getErrorMsg());
            // return;
            // }

            // MpUserService.instance.addUseridByTwoDays(userid);

        }
        logger.info("runapp,NotActiveRemindApp,sendTMG,appid:{},openid:{},succ:count:{}", appid, openid, count);

    }

    private long getTodayBegin() {

        String dateTimeStr = DateUtil.getTodayBegin();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return dateFormat.parse(dateTimeStr).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}
