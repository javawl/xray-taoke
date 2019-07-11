package com.xray.taoke.admin.app.jd;

import java.util.List;

import com.xray.taoke.admin.model.JdOrderInfo;
import com.xray.taoke.admin.model.UoUser;

public class JdOrderJieApp extends AbstractApp implements Runnable {

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            logger.error("errapp jieorder", e);
        }
    }

    public void doRun() throws Exception {
        UoUser user = null;
        List<JdOrderInfo> list = JdOrderInfo.dao.queryDiffState(10);
        logger.info("runapp jieorder,size:{}", list.size());

        String userid = null;
        String tradeid = null;
        int tkstatus = 0;
        double jiemoney = 0;
        double qbcash = 0;
        double qbconfirm = 0;

        int verno = 0;
        for (JdOrderInfo data : list) {
            userid = data.getStr("userid");
            tradeid = data.getStr("tradeid");
            tkstatus = data.getInt("tkstatus");

            user = UoUser.dao.queryByUserId(userid);
            verno = user.getInt("verno");
            qbcash = user.getDouble("qbcash");
            qbconfirm = user.getDouble("qbconfirm");

            // 失效订单
            if (tkstatus <= 14)
                tkstatus = 1;

            switch (tkstatus) {
            // 此处有疑问,京东确认收货后，订单状态为已完成，订单结算状态不知什么时候改变，所以已完成之后就结算
            case 17:
                jiemoney = data.getDouble("jiemoney");
                JdOrderInfo.dao.updateJie(tradeid, verno);
                UoUser.dao.updateJie(userid, jiemoney, verno);
                logger.info("runapp,tkstatus:17,tradeid:{},qbcash:{},qbconfirm:{}", tradeid, qbcash, qbconfirm);
                break;
            case 1:
                jiemoney = data.getDouble("jiemoney");
                JdOrderInfo.dao.updateInvalid(tradeid, verno);
                UoUser.dao.updateInvalid(userid, jiemoney, verno);
                logger.info("runapp,tkstatus 1-13,tradeid:{},qbcash:{},qbconfirm:{}", tradeid, qbcash, qbconfirm);
                break;
            case 16:
                JdOrderInfo.dao.updateState(tradeid, 16);
                logger.info("runapp,tkstatus:16,tradeid:{},qbcash:{},qbconfirm:{}", tradeid, qbcash, qbconfirm);
                break;
            default:
                break;
            }
        }
        JdOrderInfo.dao.updateEmpty();
    }

}
