package com.xray.taoke.admin.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.tkapi.vo.ConstTk;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_linkitem", pkName = "seqid")
public class LinkItem extends JfModel<LinkItem> {
    private static final long serialVersionUID = 1L;
    public static final LinkItem dao = new LinkItem();

    public List<LinkItem> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_linkitem` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("day"))) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateUtil.getDate(cond.get("day").toString(), "yyyy-MM-dd"));

                long begintime = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                long endtime = cal.getTimeInMillis();

                sb.append(" and `inputtime` >= ").append(begintime);
                sb.append(" and `inputtime` <").append(endtime);
            }
            if (StringUtil.isNotEmpty(cond.get("itemtype"))) {
                sb.append(" and `itemtype` = ").append(cond.get("itemtype"));
            }
        }

        if (page != null) {
            String countSql = "select count(1) from `tk_linkitem` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<LinkItem>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public List<LinkItem> queryListBySeqids(Set<String> sets) {
        if (sets == null || sets.size() <= 0)
            return new ArrayList<LinkItem>();

        String sql = "select * from `tk_linkitem` where 1=1 and seqid in (";
        StringBuilder sb = new StringBuilder();
        for (String str : sets) {
            sb.append(",").append(str);
        }

        return dao.find(sql + sb.substring(1) + ")");
    }

    public LinkItem queryByItemid(String itemid) {
        String sql = "select * from `tk_linkitem` where `itemid`=?";
        return dao.findFirst(sql, itemid);
    }

    public double getTkmoney() {
        return getDouble("tkprice") * getDouble("tkrate");
    }

    public double getJiemoney() {
        return ConstTk.getJiemoney(getTkmoney());
    }

    public String getItempicmin() {
        String itempic = getStr("itempic");
        if (StringUtil.isEmpty(itempic))
            return null;
        if (itempic.contains("haodanku")) {
            return itempic + "_310";
        }
        if (itempic.contains("lanlan")) {
            return itempic;
        }
        return itempic + "_310x310.jpg";
    }

}
