package com.xray.taoke.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.app.MpUserChangeApp;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.UoUser;

@ControllerBind(controllerKey = "/mpuser")
public class MpUserController extends AbstractController {

    public void goList() {
        setAttr("dataList", MpUser.dao.queryList(getCondAll(), getPageVo("`inputtime` desc")));
        render("/template/pages/mpuser/list.html");
    }

    public void doChangeAppid() {
        String appid = getPara("appid");
        String userid = getPara("userid");
        if (StringUtil.isEmpty(userid)) {
            renderRtn(RtnFactory.newFail("没有userid！不能转移！"));
            return;
        }
        List<MpInfo> list = MpInfo.dao.queryAppidAndNameAllLine();
        List<MpUser> list_user = new ArrayList<>();
        for (MpInfo mpInfo : list) {

            String string = mpInfo.getStr("appid");
            String name = mpInfo.getStr("name");

            if (string.equals(appid))
                continue;
            MpUser mpUser = MpUser.dao.queryByUserid(string, userid);
            if (mpUser == null)
                continue;
            mpUser.setAppid(string);
            mpUser.setName(name);
            list_user.add(mpUser);
        }
        if (list_user.size() == 0) {
            renderRtn(RtnFactory.newFail("没有关注其他账号！"));
            return;
        }
        if (list_user.size() == 1) {
            UoUser uoUser = UoUser.dao.queryByUserId(userid);
            uoUser.set("bindappid", list_user.get(0).getAppid());
            uoUser.update();
            renderRtn(RtnFactory.newCode(2));
            return;
        }
        renderRtn(RtnFactory.newSucc(list_user));
        return;
    }

    public void doChangeAppids() {
        String appid = getPara("appid");

        List<MpUser> mpUsers = MpUser.dao.queryListByFollow(appid);

        int threadRunNum = mpUsers.size() / 8;
        int fromIndex = 0;
        List<List<MpUser>> threadList = new ArrayList<>();
        while (true) {
            List<MpUser> subList = new ArrayList<MpUser>();
            if (fromIndex + threadRunNum >= mpUsers.size() - 1) {
                subList = mpUsers.subList(fromIndex, mpUsers.size());
                threadList.add(subList);
                break;
            }
            subList = mpUsers.subList(fromIndex, fromIndex + threadRunNum);
            threadList.add(subList);
            fromIndex = fromIndex + threadRunNum;
        }

        for (List<MpUser> subList : threadList) {
            MpUserChangeApp thread = new MpUserChangeApp(subList, appid);
            Thread t = new Thread(thread);
            t.start();
        }
        renderRtn(RtnFactory.succ);
        return;
    }

    public void doChangeUoUser() {
        String appid = getPara("appid");
        String userid = getPara("userid");
        UoUser uoUser = UoUser.dao.queryByUserId(userid);
        uoUser.set("bindappid", appid);
        uoUser.update();
        renderRtn(RtnFactory.succ);
    }

}
