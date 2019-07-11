package com.xray.taoke.admin.app;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.UoUserService;
import com.xray.taoke.admin.service.WeixinApi;
import com.xray.taoke.admin.utils.WxTmplmsgUtil;

public class CashRemindApp extends AbstractApp implements Runnable {
    private static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);
    private String appid;

	public CashRemindApp(String appid) {
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun(appid);
        } catch (Exception e) {
            logger.error("errapp neworder,appid:" + appid, e);
        }
    }

    public void doRun(String appid) throws Exception {
        List<UoUser> list = UoUser.dao.queryCashRemindByAppid(appid);
        String openid = null;
        double active_time = 0;
        long sys_time = System.currentTimeMillis();
        double qbcash = 0;
        String wxname = null;
        int count = 0;
        for (UoUser uoUser : list) {
            openid = uoUser.getStr("openid");
            qbcash = uoUser.getDouble("qbcash");
            wxname = uoUser.getStr("wxname");
            active_time = MpInfoService.instance.getActiveByOpenid(appid, openid);
            BigDecimal bg = new BigDecimal(active_time + "");
            count = UoUserService.instance.getCount(appid, openid);
            if (count != 0)
                return;

            if ((sys_time - 172800000) < bg.longValue()) {
                // 活跃
                wxApi.sendText(openid, openid, "");
            } else {
                // 发模板消息
                WxTmplmsgUtil.sendTMG(appid, openid, wxname, qbcash);
            }
            UoUserService.instance.incCount(appid, openid);
        }
    }

}
