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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_daily", pkName = "day")
public class TkStatDaily extends JfModel<TkStatDaily> {
    private static final long serialVersionUID = 1L;
    public static final TkStatDaily dao = new TkStatDaily();

    public List<TkStatDaily> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_daily` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("day"))) {
                sb.append(" and `day` = '").append(cond.get("day")).append("'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_daily` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatDaily>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public TkStatDaily sumByMonth(String month) {
        String sql = "SELECT sum(userall) userall,sum(usernew) usernew,sum(activeuv) activeuv,sum(chaquanpv) chaquanpv,sum(tsoupv) tsoupv,sum(titempv) titempv,sum(tshoppv) tshoppv,sum(tlinkpv) tlinkpv,sum(`ordernew`) ordernew,sum(`newprice`) newprice,sum(`newmoney`) newmoney,sum(`newjie`) newjie,sum(`orderjie`) orderjie,sum(`jiemoney`) jiemoney,sum(`orderxbind`) orderxbind,sum(`xbindmoney`) xbindmoney FROM `tk_stat_daily` WHERE `day` like '" + month + "%'";
        return dao.findFirst(sql);
    }
}
