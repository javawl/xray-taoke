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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_detail_month", pkName = "month,appid")
public class TkStatDetailMonth extends JfModel<TkStatDetailMonth> {
    private static final long serialVersionUID = 1L;
    public static final TkStatDetailMonth dao = new TkStatDetailMonth();

    private String mpname;

    public List<TkStatDetailMonth> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_detail_month` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("appid"))) {
                sb.append(" and `appid`= '").append(cond.get("appid")).append("'");
            }
            if (StringUtil.isNotEmpty(cond.get("month"))) {
                sb.append(" and `month` = '").append(cond.get("month")).append("'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_detail_month` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatDetailMonth>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public TkStatDetailMonth queryByAppid(String month, String appid) {
        String sql = "SELECT * FROM `tk_stat_detail_month` WHERE `month` = ? and `appid` = ?";
        return dao.findFirst(sql, month, appid);
    }

    public List<TkStatDetailMonth> queryByDay(String month) {
        String sql = "SELECT * FROM `tk_stat_detail_month` WHERE `month` = ?";
        return dao.find(sql, month);
    }

    public TkStatDetailMonth sumByDay(String month) {
        String sql = "SELECT sum(userall) userall,sum(usernew) usernew,sum(activeuv) activeuv,sum(chaquanpv) chaquanpv,sum(tsoupv) tsoupv,sum(titempv) titempv,sum(tshoppv) tshoppv,sum(tlinkpv) tlinkpv FROM `tk_stat_detail_month` WHERE `month`=?";
        return dao.findFirst(sql, month);
    }

    public void deleteByAppidAndMonth(String appid, String month) {
        String sql = "delete  from `tk_stat_detail_month` where appid =? and month =? ";
        Db.update(sql, appid, month);
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

}
