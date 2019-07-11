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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_linkconfig", pkName = "seqid")
public class LinkConfig extends JfModel<LinkConfig> {
    private static final long serialVersionUID = 1L;
    public static final LinkConfig dao = new LinkConfig();

    private String mpname;

    public List<LinkConfig> queryAll() {
        String sql = "select * from `tk_linkconfig`";
        return dao.find(sql);
    }

    public List<LinkConfig> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_linkconfig` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("opby"))) {
                sb.append(" and `opby` like '%").append(cond.get("opby")).append("%'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_linkconfig` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<LinkConfig>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public void updateRemark(String seqid, String remark) {
        String sql = "update `tk_linkconfig` set `remark`=? where `seqid`=?";
        Db.update(sql, remark, seqid);
    }

    public List<LinkConfig> queryByPid(String pid) {
        String sql = "SELECT * FROM `tk_linkconfig`  where `pid`=?";
        return dao.find(sql, pid);
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

}
