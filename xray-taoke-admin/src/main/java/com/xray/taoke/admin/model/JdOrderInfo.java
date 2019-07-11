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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_orderinfo_jd", pkName = "seqid")
public class JdOrderInfo extends JfModel<JdOrderInfo> {
    private static final long serialVersionUID = 1L;
    public static final JdOrderInfo dao = new JdOrderInfo();

    private String mpname;

    public boolean exist(String tradeid) {
        String sql = "select 1 from `tk_orderinfo_jd` where `tradeid`=?";
        return dao.findFirst(sql, tradeid) != null;
    }

    public JdOrderInfo queryByTradeid(String tradeid) {
        String sql = "select * from `tk_orderinfo_jd` where `tradeid`=?";
        return dao.findFirst(sql, tradeid);
    }

    public JdOrderInfo queryByTradeid(String tradeid, String appid) {
        String sql = "select * from `tk_orderinfo_jd` where `tradeid`=? and `appid` = ?";
        return dao.findFirst(sql, tradeid, appid);
    }

    public List<JdOrderInfo> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_orderinfo_jd` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (!StringUtil.isEmpty(cond.get("tradeid"))) {
                sb.append(" and  `tradeid` =  '").append(cond.get("tradeid")).append("'");
            }
            if (!StringUtil.isEmpty(cond.get("userid"))) {
                sb.append(" and  `userid` =  '").append(cond.get("userid")).append("'");
            }

            if (StringUtil.isNotEmpty(cond.get("sday"))) {
                sb.append(" and `tradetime`>=").append(cond.get("sday"));
            }
            if (StringUtil.isNotEmpty(cond.get("eday"))) {
                sb.append(" and `tradetime`<").append(cond.get("eday"));
            }
            if (!StringUtil.isEmpty(cond.get("appid"))) {
                sb.append(" and  `appid` =  '").append(cond.get("appid")).append("'");
            }

            if (!StringUtil.isEmpty(cond.get("tkstatus"))) {
                if (Integer.valueOf((String) cond.get("tkstatus")) != 0) {
                    sb.append(" and  `tkstatus` =  '").append(cond.get("tkstatus")).append("'");
                }
            }

        }
        if (page != null) {
            String countSql = "select count(1) from `tk_orderinfo_jd` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<JdOrderInfo>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());
    }

    public JdOrderInfo queryByTraid(String traid) {
        String sql = "select * from `tk_orderinfo_jd` where  tradeid = ?";
        return dao.findFirst(sql, traid);
    }

    public List<JdOrderInfo> queryLatestTradetime(long time) {
        String sql = "select * from `tk_orderinfo_jd` where `userid`='' and `tradetime`>? and `tkstatus`=12";
        return dao.find(sql, time);
    }

    public List<JdOrderInfo> queryByTradetime(long begintime, long endtime) {
        String sql = "select * from `tk_orderinfo_jd` where `tradetime`>=? and `tradetime`<?";
        return dao.find(sql, begintime, endtime);
    }

    public List<JdOrderInfo> queryProxy() {
        String sql = "select * from `tk_orderinfo_jd` where `userid`!='' and `proxystate`>=0 and `proxystate` != `tkstatus`";
        return dao.find(sql);
    }

    public List<JdOrderInfo> queryJieByProxy() {
        String sql = "select * from `tk_orderinfo_jd` where `userid`!='' and `tkstatus`=3 and `proxystate` = 2  ";
        return dao.find(sql);
    }

    public List<JdOrderInfo> queryTkstatus(String appid) {
        String sql = "select * from `tk_orderinfo_jd` where `appid`=? and (`tkstatus`=12 or `tkstatus`=14) order by `tradetime` asc ";
        return dao.find(sql, appid);
    }

    public List<JdOrderInfo> queryTkstatusByAppidkey(String appidkey) {
        String sql = "select * from `tk_orderinfo_jd` where `appidkey`=? and `tkstatus`=16  order by `tradetime` asc ";
        return dao.find(sql, appidkey);
    }

    public JdOrderInfo queryByAppid(String appid) {
        String sql = "select * from `tk_orderinfo_jd` where `appid`=? order by `tradetime` asc";
        return dao.findFirst(sql, appid);
    }

    public List<JdOrderInfo> queryByAppidBeweenTime(String appid, long stime, long etime) {
        String sql = "select * from `tk_orderinfo_jd` where  appid = ? and tradetime >=? and tradetime <=?  and tkstatus = 14 order by tradetime asc";
        return dao.find(sql, appid, stime, etime);
    }

    public List<JdOrderInfo> queryDiffState(int size) {
        String sql = "select * from `tk_orderinfo_jd` where `tkstatus`!=`state` and `userid`!='' limit ?";
        return dao.find(sql, size);
    }

    public int updateWxInfo() {
        String sql = "UPDATE `tk_orderinfo_jd` a, `tk_uouser` b SET a.`wxname`=b.`wxname`, a.`wxavatar`=b.`wxavatar` WHERE a.`userid`=b.`userid` AND ISNULL(a.`wxname`)";
        return Db.use(Constant.db_dataSource).update(sql);
    }

    public int updateProxyState(String tradeid, int proxystate) {
        String sql = "update `tk_orderinfo_jd` set `proxystate`=? where `tradeid` = ?";
        return Db.update(sql, proxystate, tradeid);
    }

    public int updateState(int proxystate, String proxyby, double proxymoney, String seqid) {
        String sql = "update `tk_orderinfo_jd` set `proxystate`=? ,`proxyby` = ?, `proxymoney`=?  where seqid = ?";
        return Db.update(sql, proxystate, proxyby, proxymoney, seqid);
    }

    public int updateStateJie(int proxystate, String seqid) {
        String sql = "update `tk_orderinfo_jd` set `proxystate`=?  where seqid = ?";
        return Db.update(sql, proxystate, seqid);
    }

    public int updateTkStatus(String tradeid, int tkstatus) {
        String sql = "update `tk_orderinfo_jd` set `tkstatus`=? where `tradeid`=? and `tkstatus`!=?";
        return Db.update(sql, tkstatus, tradeid, tkstatus);
    }

    public int updateTkStatus(String tradeid, int tkstatus, double alimoney, double alirate, long alitime) {
        String sql = "update `tk_orderinfo_jd` set `alimoney`=?,`alirate`=?,`alitime`=?,`tkstatus`=? where `tradeid`=? and `tkstatus`!=?";
        return Db.update(sql, alimoney, alirate, alitime, tkstatus, tradeid, tkstatus);
    }

    public int updateJie(String tradeid, int verno) {
        String sql = "update `tk_orderinfo_jd` set `jietime`=?,`verno3`=?,`state`=3 where `tradeid`=? and `verno3`<=0";
        return Db.update(sql, System.currentTimeMillis(), verno, tradeid);
    }

    public int updateState(String tradeid, int tkstatus) {
        String sql = "update `tk_orderinfo_jd` set `state`=? where `tradeid`=?";
        return Db.update(sql, tkstatus, tradeid);
    }

    public int updateInvalid(String tradeid, int verno) {
        String sql = "update `tk_orderinfo_jd` set `jietime`=?,`verno13`=?,`state`=13 where `tradeid`=? and `verno13`<=0";
        return Db.update(sql, System.currentTimeMillis(), verno, tradeid);
    }

    public int updateSucc(String appid, String userid, String tradeid, double jiemoney, double jierate, int verno) {
        String sql = "update `tk_orderinfo_jd` set `appid`=?,`userid`=?,`jiemoney`=?,`jierate`=?,`verno12`=?,`state`=12,`autobind`=1 where `tradeid`=? and `verno12`<=0";
        return Db.update(sql, appid, userid, jiemoney, jierate, verno, tradeid);
    }

    public int updateProxySucc(int proxystate, String proxyuserid, String seqid) {
        String sql = "update `tk_orderinfo_jd` set `proxystate`=?,`proxyuserid`=? where `seqid`=? ";
        return Db.update(sql, proxystate, proxyuserid, seqid);
    }

    public int updateEmpty() {
        String sql = "update `tk_orderinfo_jd` set `state`=`tkstatus` where `tkstatus`=13 and `userid`=''";
        return Db.update(sql);
    }

    public int updateProxyrate(double proxyrate, double proxymoney, double proxystate, String tradeid) {
        String sql = "update `tk_orderinfo_jd` set `proxyrate`=?,`proxymoney`=?,proxystate=? where  `tradeid`=?";
        return Db.update(sql, proxyrate, proxymoney, proxystate, tradeid);
    }

    public JdOrderInfo countNewOrder(String appid, long begintime, long endtime) {
        StringBuffer sb = new StringBuffer();
        String sql = "select count(1) as ordernew,sum(tradeprice) as newprice,sum(trademoney) as newmoney,sum(trademoney)/sum(tradeprice) as newmoneyrate,sum(jiemoney) as newjie from `tk_orderinfo_jd` where 1=1 and `appid`='" + appid + "'";
        sb.append(" and `tradetime` >= " + begintime + " and `tradetime` <" + endtime);
        sb.append(" and `tkstatus` !=13");
        return dao.findFirst(sql + sb.toString());
    }

    public JdOrderInfo countXBindOrder(String appid, long begintime, long endtime) {
        StringBuffer sb = new StringBuffer();
        String sql = "select count(1) as orderxbind,sum(tradeprice) as xbindprice,sum(trademoney) as xbindmoney from `tk_orderinfo_jd` where 1=1  and `appid`='" + appid + "'";
        sb.append(" and `tradetime` >= " + begintime + " and `tradetime` <" + endtime);
        sb.append(" and `userid`='' ");
        sb.append(" and `tkstatus` !=13");
        return dao.findFirst(sql + sb.toString());
    }

    public JdOrderInfo countJieOrder(String appid, long begintime, long endtime) {
        StringBuffer sb = new StringBuffer();
        String sql = "select count(1) as orderjie,sum(alimoney) as jiemoney,sum(tradeprice) as jieprice  from `tk_orderinfo_jd` where 1=1 and `appid`='" + appid + "'";
        sb.append(" and `alitime` >= " + begintime + " and `alitime` <" + endtime);
        sb.append(" and `tkstatus` !=13");
        return dao.findFirst(sql + sb.toString());
    }

    public List<JdOrderInfo> queryInvaildOrder(String appidkey, long time) {
        String sql = "select * from `tk_orderinfo_jd` where `tkstatus` =3  and `appidkey` = ?  and `userid` !='' and `tradetime`>? and `invalidorder` = 0 order by `tradetime` asc";
        return dao.find(sql, appidkey, time);
    }

    public List<JdOrderInfo> queryInvaildOrder(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_orderinfo_jd` where 1=1";
        StringBuilder sb = new StringBuilder();
        sb.append(" and `tkstatus` = 3");
        sb.append(" and `invalidorder` !=0 ");
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("tradetime"))) {
                sb.append(" and `tradetime` >").append(cond.get("tradetime"));
            }
            if (!StringUtil.isEmpty(cond.get("appid"))) {
                sb.append(" and  `appid` =  '").append(cond.get("appid")).append("'");
            }
        }

        if (page != null) {
            String countSql = "select count(1) from `tk_orderinfo_jd` where 1=1  " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<JdOrderInfo>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());

    }

    public List<JdOrderInfo> queryInvaildOrder() {
        String sql = "select * from `tk_orderinfo_jd` where `invalidorder` !=0   order by `tradetime` asc";
        return dao.find(sql);
    }

    public int updateInvalidorder(String tradeid, int invalidorder) {
        String sql = "update `tk_orderinfo_jd` set `invalidorder`=? where `tradeid`=? and `tkstatus` =?";
        return Db.update(sql, invalidorder, tradeid, 3);
    }

    public JdOrderInfo queryTradeidOrder() {
        String sql = "select * from `tk_orderinfo_jd` where `tkstatus` =12  order by `seqid` desc";
        return dao.findFirst(sql);
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

    public String getWxnameUTF8() {
        try {
            return new String(get("wxname"), "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

    public String getTradeidENC() {
        String tradeid = getStr("tradeid");
        if (StringUtil.isEmpty(tradeid))
            return null;
        return "***" + tradeid.substring(3, tradeid.length() - 7) + "***" + tradeid.substring(tradeid.length() - 4);
    }
    
}
