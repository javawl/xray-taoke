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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_stat_detail", pkName = "day,appid")
public class TkStatDetail extends JfModel<TkStatDetail> {
    private static final long serialVersionUID = 1L;
    public static final TkStatDetail dao = new TkStatDetail();

    private String mpname;

    public List<TkStatDetail> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_stat_detail` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (StringUtil.isNotEmpty(cond.get("appid"))) {
                sb.append(" and `appid`= '").append(cond.get("appid")).append("'");
                if (StringUtil.isNotEmpty(cond.get("day"))) {
                    sb.append(" and `day` <= '").append(cond.get("day")).append("'");
                }
            } else if (StringUtil.isNotEmpty(cond.get("day"))) {
                sb.append(" and `day` = '").append(cond.get("day")).append("'");
            }
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_stat_detail` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<TkStatDetail>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }

        return dao.find(sql + sb.toString());
    }

    public TkStatDetail queryByAppid(String day, String appid) {
        String sql = "SELECT * FROM `tk_stat_detail` WHERE `day` = ? and `appid` = ?";
        return dao.findFirst(sql, day, appid);
    }

    public List<TkStatDetail> queryByDay(String day) {
        String sql = "SELECT * FROM `tk_stat_detail` WHERE `day` = ?";
        return dao.find(sql, day);
    }

    public TkStatDetail sumByDay(String day) {
        String sql = "SELECT sum(userall) userall,sum(usernew) usernew,sum(activeuv) activeuv,sum(chaquanpv) chaquanpv,sum(tsoupv) tsoupv,sum(titempv) titempv,sum(tshoppv) tshoppv,sum(tlinkpv) tlinkpv FROM `tk_stat_detail` WHERE `day`=?";
        return dao.findFirst(sql, day);
    }

    public TkStatDetail sumByMonthByAppid(String month, String appid) {
        String sql = "SELECT sum(userall) userall,sum(usernew) usernew,sum(activeuv) activeuv,sum(chaquanpv) chaquanpv,sum(tsoupv) tsoupv,sum(titempv) titempv,sum(tshoppv) tshoppv,sum(tlinkpv) tlinkpv,sum(`ordernew`) ordernew,sum(`newprice`) newprice,sum(`newmoney`) newmoney,sum(`newjie`) newjie,sum(`orderjie`) orderjie,sum(`jiemoney`) jiemoney FROM `tk_stat_detail` WHERE `day` like '" + month + "%' and appid=?";
        return dao.findFirst(sql, appid);
    }

    public void delByDay(String day) {
        String sql = "delete from `tk_stat_detail` where `day`=?";
        Db.use(Constant.db_dataSource).update(sql, day);
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

}
