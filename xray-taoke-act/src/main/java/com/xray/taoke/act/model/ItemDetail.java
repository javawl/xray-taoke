package com.xray.taoke.act.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.taoke.act.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_item_detail", pkName = "seqid")
public class ItemDetail extends JfModel<ItemDetail> {
    private static final long serialVersionUID = 1L;
    public static final ItemDetail dao = new ItemDetail();

    public void insertInto(String appid, String openid, String itemid, String itemtitle, double itemprice, double cpmoney, double tkprice, double jiemoney) {
        String sql = "INSERT INTO `tk_item_detail` (`appid`,`openid`,`itemid`,`itemtitle`,`inputtime`,`itemprice`,`cpmoney`,`tkprice`,`jiemoney`) VALUES (?,?,?,?,?,?,?,?,?)";
        Db.update(sql, appid, openid, itemid, itemtitle, System.currentTimeMillis(), itemprice, cpmoney, tkprice, jiemoney);
    }

    public void insertIntoDetail(String appid, String openid, String itemdetail) {
        String sql = "INSERT INTO `tk_item_detail` (`appid`,`openid`,`itemdetail`,`inputtime`) VALUES (?,?,?,?)";
        Db.update(sql, appid, openid, itemdetail, System.currentTimeMillis());
    }

}
