package com.xray.taoke.admin.web.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.app.TkStatDetailApp;
import com.xray.taoke.admin.app.TkStatIncomeApp;
import com.xray.taoke.admin.model.TkStatDetail;
import com.xray.taoke.admin.model.TkStatIncomeDay;
import com.xray.taoke.admin.model.TkStatIncomeItem;
import com.xray.taoke.admin.model.TkStatIncomeMonth;
import com.xray.taoke.admin.service.MpInfoService;

@ControllerBind(controllerKey = "/tkstat")
public class TkStatController extends AbstractController {

    public void goList() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "day", DateUtil.getToday());

        Map<String, String> map = MpInfoService.instance.getMpNames();
        List<TkStatDetail> dataList = TkStatDetail.dao.queryList(cond, getPageVo("`day` desc,`userall` desc"));
        for (TkStatDetail data : dataList) {
            data.setMpname(map.get(data.getStr("appid")));
        }

        setAttr("dataList", dataList);
        render("/template/pages/tkstat/list.html");
    }

    public void goIncomeDay() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "day", DateUtil.getToday());

        PageVo page = null;
        if (StringUtil.isEmpty(getPara("itemid"))) {
            page = getPageVo("`trademoney` desc, `conssum` desc");
        } else {
            page = getPageVo("`day` desc");
        }

        List<TkStatIncomeDay> list = TkStatIncomeDay.dao.queryList(cond, page);
        Map<String, String> map = TkStatIncomeItem.dao.getItemNames();
        for (TkStatIncomeDay data : list) {
            data.setItemname(map.get(data.getStr("itemid")));
        }
        setAttr("dataList", list);
        render("/template/pages/tkstat/income_day.html");
    }

    public void goIncomeMonth() {
        Map<String, Object> cond = getCondAll();
        getCond(cond, "day", DateUtil.getYesterday());

        PageVo page = null;
        if (StringUtil.isEmpty(getPara("itemid"))) {
            page = getPageVo("`trademoney` desc, `conssum` desc");
        } else {
            page = getPageVo("`day` desc");
        }

        List<TkStatIncomeMonth> list = TkStatIncomeMonth.dao.queryList(cond, page);
        Map<String, String> map = TkStatIncomeItem.dao.getItemNames();
        for (TkStatIncomeMonth data : list) {
            data.setItemname(map.get(data.getStr("itemid")));
        }
        setAttr("dataList", list);
        render("/template/pages/tkstat/income_month.html");
    }

    public void doStat() {
        String day = getPara("day", DateUtil.getToday());
        TkStatDetailApp app = new TkStatDetailApp(day);
        app.run();
        renderRtn(RtnFactory.succ);
    }

    public void doStatAll() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getDate(getPara("day"), "yyyy-MM-dd"));

        long now = System.currentTimeMillis();
        while (cal.getTimeInMillis() < now) {
            String day = DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
            TkStatDetailApp app = new TkStatDetailApp(day);
            app.run();
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        renderRtn(RtnFactory.succ);
    }

    public void doStatIncome() {
        String day = getPara("day", DateUtil.getToday());
        TkStatIncomeApp app = new TkStatIncomeApp(day);
        app.runOrder(day);
        app.runDay(day);
        app.runMonth(day);
        renderRtn(RtnFactory.succ);
    }

    public void doStatIncomeAll() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getDate(getPara("day"), "yyyy-MM-dd"));

        long now = System.currentTimeMillis();
        while (cal.getTimeInMillis() < now) {
            String day = DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
            TkStatIncomeApp app = new TkStatIncomeApp(day);
            app.runOrder(day);
            app.runDay(day);
            app.runMonth(day);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        renderRtn(RtnFactory.succ);
    }

    public void doUpdateConssum() {
        TkStatIncomeDay.dao.updateConssum(getPara("seqid"), getPara("conssum"));
        renderRtn(RtnFactory.succ);
    }

    public void goListByConfig() {
        Map<String, Object> cond = new HashMap<String, Object>();
        getCond(cond, "word", "");

        PageVo page = new PageVo(getParaToInt("pno", 1));
        page.setOrderby("`seqid` desc");
        setAttr("page", page);

        List<TkStatIncomeItem> dataList = TkStatIncomeItem.dao.queryList(cond, page);
        setAttr("dataList", dataList);
        logger.debug("dataList,size:{}", dataList.size());
        render("/template/pages/tkstat/config/list.html");
    }

    public void goAddByConfig() {
        render("/template/pages/tkstat/config/form.html");
    }

    public void goEditByConfig() {
        String seqid = getPara("seqid");
        TkStatIncomeItem data = TkStatIncomeItem.dao.findById(seqid);
        setAttr("data", data);
        render("/template/pages/tkstat/config/form.html");
    }

    public void doAddByConfig() {
        TkStatIncomeItem data = new TkStatIncomeItem();
        data.set("name", getPara("name"));
        data.set("itemid", getPara("itemid"));
        data.set("name", getPara("name"));
        data.set("tkids", getPara("tkids"));
        data.save();
        renderRtn(RtnFactory.succ);
    }

    public void doEditByConfig() {
        String seqid = getPara("seqid");
        TkStatIncomeItem data = TkStatIncomeItem.dao.findById(seqid);
        data.set("name", getPara("name"));
        data.set("itemid", getPara("itemid"));
        data.set("name", getPara("name"));
        data.set("tkids", getPara("tkids"));
        data.update();

        renderRtn(RtnFactory.succ);
    }

    public void doDelByConfig() {
        String seqid = getPara("seqid");
        TkStatIncomeItem.dao.deleteById(seqid);
        renderRtn(RtnFactory.succ);
    }

}
