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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_month", pkName = "month")
public class TkStatMonth extends JfModel<TkStatMonth> {
    private static final long serialVersionUID = 1L;
    public static final TkStatMonth dao = new TkStatMonth();

    public List<TkStatMonth> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_month` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("day"))) {
                sb.append(" and `month` = '").append(cond.get("day")).append("'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_month` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatMonth>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

}
