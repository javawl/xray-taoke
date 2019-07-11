package com.xray.taoke.admin.app;

import java.text.SimpleDateFormat;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.Cashinfo;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.service.CashService;

public class NewCashRemindApp extends AbstractApp implements Runnable {
    private List<Cashinfo> list;

    public NewCashRemindApp(List<Cashinfo> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            doRun(list);
        } catch (Exception e) {
            logger.error("errapp NewCashRemindApp,list,size:{}" + list.size(), e);
        }
    }

    // private int cashRemind() {
    // String name = "xuruibo";
    // String openid = com.xray.taoke.admin.common.Constant.getOpenid(name);
    // int send_type = 0;
    // if (list.size() == 0) {
    // logger.info("NewCashRemindApp,pepCashMsg,list.size:0");
    // // CashService.instance.delHaveRemind(openid);
    // return 0;
    // }
    // String str = CashService.instance.getHaveRemind(openid);
    // if (StringUtil.isNotEmpty(str))
    // return 0;
    // send_type = 1;
    // return send_type;
    //
    // }

    private int cashRemind() {
        int send_type = 0;
        Cashinfo cashinfo = Cashinfo.dao.queryTradeidOrder();
        String seqid = cashinfo.getStr("seqid");
        String str = CashService.instance.getHaveRemind();
        if (StringUtil.isEmpty(str)) {
            CashService.instance.setHaveRemind(seqid);
            str = seqid;
        }
        if (!seqid.equals(str)) {
            CashService.instance.setHaveRemind(seqid);
            send_type = 1;
        }
        return send_type;

    }

    private int tradeOrder() {
        OrderInfo info = OrderInfo.dao.queryTradeidOrder();
        String tradeid = info.getStr("tradeid");
        int send_type = 0;
        String redis_trad = CashService.instance.getTradeid();
        if (StringUtil.isEmpty(redis_trad)) {
            CashService.instance.setTradeid(tradeid);
            redis_trad = tradeid;
        }
        if (!tradeid.equals(redis_trad)) {
            CashService.instance.setTradeid(tradeid);
            send_type = 1;
        }
        return send_type;

    }

    public void doRun(List<Cashinfo> list) throws Exception {
        // String name = "xuruibo";
        // String openid = com.xray.taoke.admin.common.Constant.getOpenid(name);

        String today = DateUtil.getToday();
        long sys_time = System.currentTimeMillis();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-mm-dd HH:ss:mm");
        today = today + " 08:00:00";
        long time = formate.parse(today).getTime();
        if (sys_time <= time)
            return;

        int cash_send_type = cashRemind();

        int order_send_type = tradeOrder();

        String url = "http://wxadmin.liangdianpro.com/wxmp_admin/ionode/doCashTmg?size=" + list.size() + "&ordertype=" + order_send_type + "&cashtype=" + cash_send_type;
        String data = com.jfinal.weixin.sdk.utils.HttpUtils.get(url);
        int code = JSONObject.parseObject(data).getIntValue("code");
        if (code == -1)
            return;
        // if (cash_send_type == 1)
        // CashService.instance.setHaveRemind(openid);
    }

}
