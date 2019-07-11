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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_linkinfo", pkName = "seqid")
public class LinkInfo extends JfModel<LinkInfo> {
    private static final long serialVersionUID = 1L;
    public static final LinkInfo dao = new LinkInfo();

    public List<LinkInfo> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_linkinfo` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("date"))) {
                sb.append(" and `inputtime` >= ").append(cond.get("date")).append(" and  `inputtime` <=").append(cond.get("date2"));
            }
            if (StringUtil.isNotEmpty(cond.get("opby"))) {
                sb.append(" and `opby` like '%").append(cond.get("opby")).append("%'");
            }
            if (StringUtil.isNotEmpty(cond.get("begin"))) {
                sb.append(" and `inputtime` >= ").append(cond.get("begin"));
            }

            if (StringUtil.isNotEmpty(cond.get("end"))) {
                sb.append(" and `inputtime` <= ").append(cond.get("end"));
            }

        }

        if (page != null) {
            String countSql = "select count(1) from `tk_linkinfo` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<LinkInfo>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public LinkInfo queryByLinkid(String linkid) {
        String sql = "SELECT * FROM `tk_linkinfo` where link_id=?";
        return dao.findFirst(sql, linkid);
    }

    public void updateRemark(String seqid, String remark) {
        String sql = "update `tk_linkinfo` set `remark`=? where `seqid`=?";
        Db.update(sql, remark, seqid);
    }

    public String getOpbyUTF8() {
        try {
            return new String(get("opby"), "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

}
