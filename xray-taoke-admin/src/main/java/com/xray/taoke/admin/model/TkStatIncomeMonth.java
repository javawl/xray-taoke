package com.xray.taoke.admin.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.taoke.admin.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_income_month", pkName = "seqid")
public class TkStatIncomeMonth extends JfModel<TkStatIncomeMonth> {
    private static final long serialVersionUID = 1L;
    public static final TkStatIncomeMonth dao = new TkStatIncomeMonth();

    private String itemname;

    public List<TkStatIncomeMonth> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_income_month` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("itemid"))) {
                sb.append(" and `itemid` = '").append(cond.get("itemid")).append("'");
                if (StringUtil.isNotEmpty(cond.get("day"))) {
                    sb.append(" and `day` <= '").append(cond.get("day")).append("'");
                }
            } else if (StringUtil.isNotEmpty(cond.get("day"))) {
                sb.append(" and `day` = '").append(cond.get("day")).append("'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_income_month` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatIncomeMonth>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());
    }

    public TkStatIncomeMonth queryByDay(String day, String itemid) {
        String sql = "select * from `tk_stat_income_month` where `day`=? and `itemid`=?";
        return dao.findFirst(sql, day, itemid);
    }

    public void updateConssum(String seqid, String conssum) {
        String sql = "update `tk_stat_income_month` set `conssum`=? where `seqid`=?";
        Db.use(Constant.db_dataSource).update(sql, conssum, seqid);
    }

    public void delByDay(String day) {
        String sql = "delete from `tk_stat_income_month` where `day`=?";
        Db.use(Constant.db_dataSource).update(sql, day);
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getDaysize() {
        try {
            String day = getStr("day");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                date = sdf.parse(day);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            int day_size = ca.get(Calendar.DAY_OF_MONTH);
            return day_size;
        } catch (Exception e) {
        }
        return 1;
    }

}
