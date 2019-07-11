package com.xray.taoke.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.taoke.admin.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_income_day", pkName = "seqid")
public class TkStatIncomeDay extends JfModel<TkStatIncomeDay> {
    private static final long serialVersionUID = 1L;
    public static final TkStatIncomeDay dao = new TkStatIncomeDay();

    private String itemname;

    public List<TkStatIncomeDay> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_income_day` where 1=1 ";
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
            String countSql = "select count(1) from `tk_stat_income_day` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatIncomeDay>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());
    }

    public List<TkStatIncomeDay> queryByMonth(String day, String itemid) {
        String sql = "select * from `tk_stat_income_day` where `day` like '" + day.substring(0, 7) + "%' and `day`<=? and `itemid`=? order by `day` desc";
        return dao.find(sql, day, itemid);
    }

    public TkStatIncomeDay queryByDay(String day, String itemid) {
        String sql = "select * from `tk_stat_income_day` where `day`=? and `itemid`=?";
        return dao.findFirst(sql, day, itemid);
    }
    
    public void updateConssum(String seqid, String conssum) {
        if(Double.parseDouble(conssum) > 0) {
            String sql = "update `tk_stat_income_day` set `conssum`=?, `incomerate`=`trademoney`/? where `seqid`=?";
            Db.use(Constant.db_dataSource).update(sql, conssum, conssum, seqid);
        } else {
            String sql = "update `tk_stat_income_day` set `conssum`=? where `seqid`=?";
            Db.use(Constant.db_dataSource).update(sql, conssum, seqid);
        }
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

}
