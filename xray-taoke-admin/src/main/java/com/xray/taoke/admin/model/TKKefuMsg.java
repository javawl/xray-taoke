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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_mpkefu", pkName = "seqid")
public class TKKefuMsg extends JfModel<TKKefuMsg> {
    private static final long serialVersionUID = 1L;
    public static final TKKefuMsg dao = new TKKefuMsg();

    public List<TKKefuMsg> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_mpkefu` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            sb.append(" and `state`>=-1 ");
            if(StringUtil.isNotEmpty(cond.get("appid"))) {
                sb.append(" and `appid`= '").append(cond.get("appid")).append("'");
            }
            if (StringUtil.isNotEmpty(cond.get("date"))) {
                sb.append(" and `booktime` >= ").append(cond.get("date")).append(" and  `booktime` <=").append(cond.get("date2"));
            }
        }
        
        if (page != null) {
            String countSql = "select count(1) from `tk_mpkefu` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TKKefuMsg>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    
    public TKKefuMsg queryByMediaid(String mediaid) {
        String sql = "select * from `tk_mpkefu` where `mediaid`=?";
        return dao.findFirst(sql, mediaid);
    }
    
    
    public void doDels(String userid, String seqids) {
        String sql = "update `tk_mpkefu` set `state`=?,`edittime`=?,`editby`=? where seqid in(?)";
        Db.use(Constant.db_dataSource).update(sql,-1, System.currentTimeMillis(), userid, seqids);
    }
    
    
    public List<TKKefuMsg> queryByWaiting(int state) {
        String sql = "SELECT * FROM `tk_mpkefu` WHERE `state` = ?";
        return dao.find(sql,state);
    }
    

}
