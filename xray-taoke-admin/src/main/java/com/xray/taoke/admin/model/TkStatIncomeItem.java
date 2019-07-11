package com.xray.taoke.admin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.PageVo;
import com.xray.taoke.admin.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_income_item", pkName = "seqid")
public class TkStatIncomeItem extends JfModel<TkStatIncomeItem> {
    private static final long serialVersionUID = 1L;
    public static final TkStatIncomeItem dao = new TkStatIncomeItem();

    public List<TkStatIncomeItem> queryListAll() {
        String sql = "select * from `tk_stat_income_item`";
        return dao.find(sql);
    }

    public List<TkStatIncomeItem> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_income_item` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {

            if (StringUtil.isNotEmpty(cond.get("word"))) {
                sb.append(" and `name` like '%").append(cond.get("word")).append("%'");
            }

        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_income_item` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatIncomeItem>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public Map<String, String> getItemNames() {
        String sql = "select * from `tk_stat_income_item`";
        List<TkStatIncomeItem> list = dao.find(sql);
        Map<String, String> map = new HashMap<String, String>();
        for (TkStatIncomeItem data : list) {
            map.put(data.getStr("itemid"), data.getStr("name"));
        }
        return map;
    }

}
