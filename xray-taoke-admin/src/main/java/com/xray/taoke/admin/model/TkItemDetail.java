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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_item_detail", pkName = "seqid")
public class TkItemDetail extends JfModel<TkItemDetail> {
    private static final long serialVersionUID = 1L;
    public static final TkItemDetail dao = new TkItemDetail();

    private String mpname;

    private String name;

    public List<TkItemDetail> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_item_detail` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (!StringUtil.isEmpty(cond.get("appid"))) {
                sb.append(" and  `appid` =  '").append(cond.get("appid")).append("'");
            }

            if (!StringUtil.isEmpty(cond.get("openid"))) {
                sb.append(" and  `openid` =  '").append(cond.get("openid")).append("'");
            }

        }
        if (page != null) {
            String countSql = "select count(1) from `tk_item_detail` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkItemDetail>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public List<TkItemDetail> queryListBy() {
        String sql = "select * from `tk_item_detail` where 1=1  and `state` =0 ";
        return dao.find(sql);
    }

    public TkItemDetail queryOne() {
        String sql = "select * from `tk_item_detail` where 1=1  order by `inputtime` desc ";
        return dao.findFirst(sql);
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWxnameUTF8() {
        try {
            return new String(get("wxname"), "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

    public String getItemdetail1() {
        try {
            return new String(get("itemdetail"), "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

}
