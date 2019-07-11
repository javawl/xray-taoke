package com.xray.taoke.act.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.taoke.act.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_uouser", pkName = "seqid")
public class UoUser extends JfModel<UoUser> {
    private static final long serialVersionUID = 1L;
    public static final UoUser dao = new UoUser();

    public UoUser queryByUserId(String userid) {
        String sql = "select * from `tk_uouser` where `userid`=? ";
        return dao.findFirst(sql, userid);
    }

    public UoUser queryByProxycode(String proxycode) {
        String sql = "select * from `tk_uouser` where `proxycode`=? ";
        return dao.findFirst(sql, proxycode);
    }

    public String queryUseridByOpenid(String openid) {
        String sql = "select `userid` from `tk_uouser` where `openid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, openid);
    }

    public double queryRateByUserid(String userid) {
        String sql = "select `rate` from `tk_uouser` where `userid`=?";
        return Db.use(Constant.db_dataSource).queryDouble(sql, userid);
    }

    public void addData(String userid, String openid) {
        String sql = "insert into `tk_uouser` set `userid` =?, `openid`=?, `inputtime`=?";
        Db.use(Constant.db_dataSource).update(sql, userid, openid, System.currentTimeMillis());
    }

    public void addData(String userid, String openid, String bindappid) {
        String sql = "insert into `tk_uouser` set `userid` =?, `openid`=?, `inputtime`=?, `bindappid`=?";
        Db.use(Constant.db_dataSource).update(sql, userid, openid, System.currentTimeMillis(), bindappid);
    }

    public int updateCashtime(String userid, double money, int verno, long cashtime) {
        String sql = "update `tk_uouser` set `qbcashing`=" + money + ",`qbcash`=0,`qbproxycash`=0,`cashstate`=1,`cashtime`=?,`verno`=`verno`+1  where `userid`=? and `verno`=? and `cashstate`!=1";
        return Db.update(sql, cashtime, userid, verno);
    }

    public int updateSucc(String userid, double money, int verno) {
        String sql = "update `tk_uouser` set `qbtotal`=`qbtotal`+" + money + ",`ordersucc`=`ordersucc`+1,`qbconfirm`=`qbconfirm`+" + money + ",`verno`=`verno`+1 where `userid`=? and `verno`=?";
        return Db.update(sql, userid, verno);
    }

    public int updateProxyby(String proxyby, String userid) {
        String sql = "update `tk_uouser` set `proxyby`=? where `userid`=? ";
        return Db.update(sql, proxyby, userid);
    }

    public UoUser queryUseridByOpenid(String appid, String openid) {
        String sql = "select * from `tk_uouser` where `appid`=? and `openid`= ? ";
        return dao.findFirst(sql, appid, openid);
    }

    public int updateSuccProxyPeople(String userid) {
        String sql = "update `tk_uouser` set `proxypeople`=`proxypeople`+1 where `userid`=? ";
        return Db.update(sql, userid);
    }

    public int updateProxyState(String userid) {
        String sql = "update `tk_uouser` set `proxystate`=1 where `userid`=? ";
        return Db.update(sql, userid);
    }

}
