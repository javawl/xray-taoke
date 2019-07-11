package com.xray.taoke.admin.app;

import java.math.BigDecimal;
import java.util.List;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.utils.WxTmplmsgUtil;

public class RemindNotActiveApp extends AbstractApp implements Runnable {
    private String appid;
    private String templateid;

    public RemindNotActiveApp(String appid, String templateid) {
        this.appid = appid;
        this.templateid = templateid;
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp RemindNotActiveApp,appid:" + appid, e);
        }
    }

    public void doRun() throws Exception {
        List<UoUser> list = UoUser.dao.queryHaveMoneyByAppid(appid);
        if (list.size() == 0)
            return;
        String userid = null;
        String openid = null;
        long active_time = 0;
        double avtive_value = 0;
        double qbcash = 0;
        double qconfirm = 0;
        String wxname = null;
        long sys_time = System.currentTimeMillis();
        for (UoUser uoUser : list) {
            userid = uoUser.getStr("userid");
            if (StringUtil.isEmpty(userid))
                continue;
            openid = MpUser.dao.queryOpenidByUserid(openid, userid);
            if (StringUtil.isEmpty(openid))
                continue;
            wxname = uoUser.getWxnameUTF8();
            qbcash = uoUser.getDouble("qbcashing");
            qconfirm = uoUser.getDouble("qbconfirm");
            avtive_value = MpInfoService.instance.getActiveByOpenid(userid, openid);
            BigDecimal bg = new BigDecimal(avtive_value + "");
            active_time = bg.longValue();
            if ((sys_time - 172800000) < active_time)
                continue;

            WxTmplmsgUtil.sendHaveMoneyTMG(appid, openid, wxname, qbcash, qconfirm, templateid);
            logger.info("doRun RemindNotActiveApp,appid:" + appid + ",openid:" + openid + ",templateid:" + templateid);
        }

    }

}
