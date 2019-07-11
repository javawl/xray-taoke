package com.xray.taoke.act.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.taoke.act.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_cashinfo", pkName = "seqid")
public class CashInfo extends JfModel<CashInfo> {
    private static final long serialVersionUID = 1L;
    public static final CashInfo dao = new CashInfo();

    public CashInfo queryByTradeid(String tradeid) {
        String sql = "select * from `tk_cashinfo` where `tradeid`=?";
        return dao.findFirst(sql, tradeid);
    }

    public void updateUseridByTradeid(String tradeid, String userid) {
        String sql = "update `tk_cashinfo` set `userid`=? where `tradeid`=?";
        Db.use(Constant.db_dataSource).update(sql, userid, tradeid);
    }

}
