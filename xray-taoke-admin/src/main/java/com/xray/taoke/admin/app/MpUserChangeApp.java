package com.xray.taoke.admin.app;

import java.util.List;

import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.UoUser;

public class MpUserChangeApp extends AbstractApp implements Runnable {
    private List<MpUser> mpUsers;
    private String appid;

    public MpUserChangeApp(List<MpUser> mpUsers, String appid) {
        this.mpUsers = mpUsers;
        this.appid = appid;
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp MpUserChangeApp,list:" + mpUsers.size(), e);
        }
    }

    public void doRun() throws Exception {

        List<MpInfo> list = MpInfo.dao.queryAppidAndNameAllLine();
        String userid = null;
        for (MpUser mpUser : mpUsers) {
            userid = mpUser.getStr("userid");

            for (MpInfo mpInfo : list) {

                String string = mpInfo.getStr("appid");
                if (string.equals(appid))
                    continue;
                MpUser mpUser1 = MpUser.dao.queryByUserid(string, userid);
                if (mpUser1 == null)
                    continue;
                UoUser uoUser = UoUser.dao.queryByUserId(userid);
                uoUser.set("bindappid", string);
                uoUser.update();
                break;
            }

        }

    }

}
