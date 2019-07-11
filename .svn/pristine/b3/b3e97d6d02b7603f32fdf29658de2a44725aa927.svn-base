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

@TableBind(configName = Constant.db_dataSource, tableName = "tk_cashinfo", pkName = "seqid")
public class Cashinfo extends JfModel<Cashinfo> {
	private static final long serialVersionUID = 1L;
	public static final Cashinfo dao = new Cashinfo();

	private String mpname;

	public List<Cashinfo> queryList(Map<String, Object> cond, PageVo page) {
		String sql = "select * from `tk_cashinfo` where 1=1 ";
		StringBuilder sb = new StringBuilder();
		if (cond != null) {
			if (!StringUtil.isEmpty(cond.get("openid"))) {
				sb.append(" and  `openid` =  '").append(cond.get("openid")).append("'");
			}
			if (!StringUtil.isEmpty(cond.get("appid"))) {
				sb.append(" and  `appid` =  '").append(cond.get("appid")).append("'");
			}
			if (!StringUtil.isEmpty(cond.get("userid"))) {
				sb.append(" and  `userid` =  '").append(cond.get("userid")).append("'");
			}

		}
		if (page != null) {
			String countSql = "select count(1) from `tk_cashinfo` where 1=1 " + sb.toString();
			int count = Db.queryLong(countSql).intValue();
			page.setCount(count);
			if (count <= 0) {
				return new ArrayList<Cashinfo>();
			}
			sb.append(page.orderbySql());
			sb.append(page.limitSql());
		}
		return dao.find(sql + sb.toString());
	}

	public Cashinfo queryByUserId(String userid) {
		String sql = "select * from `tk_cashinfo` where 1=1 and userid =? ";
		return dao.findFirst(sql, userid);
	}

	public Cashinfo queryByTradeId(String appid, String tradeid) {
		String sql = "select * from `tk_cashinfo` where 1=1 and `appid` =? and `tradeid` = ? ";
		return dao.findFirst(sql, appid, tradeid);
	}

	public List<Cashinfo> queryAutoByAppid(String appid) {
		String sql = "select * from `tk_cashinfo` where 1=1 and `appid` =? and `state` = 0 and `cashnum` <=5 ";
		return dao.find(sql, appid);
	}

	public List<Cashinfo> queryPepCash() {
		String sql = "select * from `tk_cashinfo` where 1=1 and `state` = 0 ";
		return dao.find(sql);
	}

	public List<Cashinfo> queryRenGongByAppid(String appid) {
		String sql = "select * from `tk_cashinfo` where 1=1 and `appid` =? and `state` = 0 and `cashnum` >5 ";
		return dao.find(sql, appid);
	}

	public int queryCountByUserid(String userid) {
		String sql = "select count(1) from `tk_cashinfo` where 1=1 and `userid` =? and `paystate` = 1  ";
		return Db.queryInt(sql, userid);
	}

	public int updateWxInfo() {
		String sql = "UPDATE `tk_cashinfo` a, `tk_uouser` b SET a.`wxname`=b.`wxname`, a.`wxavatar`=b.`wxavatar` WHERE a.`userid`=b.`userid` AND ISNULL(a.`wxname`)";
		return Db.use(Constant.db_dataSource).update(sql);
	}

	public int updateShouDong(long seqid, String tradeid, int verno) {
		String sql = "update `tk_cashinfo` set `tradeid`=?,`verno2`=?,`state`=2 where `seqid`=? and `verno2`<=0 and `state`=0";
		return Db.update(sql, tradeid, verno, seqid);
	}

	public int updateAuto(long seqid, String tradeid, int verno) {
		String sql = "update `tk_cashinfo` set `tradeid`=?,`verno2`=?,`state`=1 where `seqid`=? and `verno2`<=0 and `state`=0";
		return Db.update(sql, tradeid, verno, seqid);
	}

	public int updateCashSucc(long seqid, double qbcash) {
		String sql = "update `tk_cashinfo` set `paystate`=1,`paytime`=?,`qbcash` = ? where `seqid`=?";
		return Db.update(sql, System.currentTimeMillis(), qbcash, seqid);
	}

	public int updateCashFail(long seqid, String reason) {
		String sql = "update `tk_cashinfo` set `paystate`=-1,`paytime`=?,`reason`=? where `seqid`=?";
		return Db.update(sql, System.currentTimeMillis(), reason, seqid);
	}

	public int updateCashFailTwice(long seqid, String reason) {
		String sql = "update `tk_cashinfo` set `paystate`=2,`paytime`=?,`reason`=? where `seqid`=?";
		return Db.update(sql, System.currentTimeMillis(), reason, seqid);
	}

	public Cashinfo queryTradeidOrder() {
		String sql = "select * from `tk_cashinfo`  order by `seqid` desc";
		return dao.findFirst(sql);
	}

	public String getMpname() {
		return mpname;
	}

	public void setMpname(String mpname) {
		this.mpname = mpname;
	}

	public String getWxnameUTF8() {
		String name = null;
		try {
			name = new String(get("wxname"), "UTF-8");
			return name.replaceAll("'", "");
			// return new String(get("wxname"), "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}

}
