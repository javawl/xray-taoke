package com.xray.taoke.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.web.vo.PageVo;
import com.xray.taoke.admin.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_mpuser", pkName = "seqid")
public class MpUser extends JfModel<MpUser> {
    private static final long serialVersionUID = 1L;
    public static final MpUser dao = new MpUser();

    public List<MpUser> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "select * from `tk_mpuser_" + cond.get("appid") + "` where 1=1 ";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
        }
        if (page != null) {
            String countSql = "select count(1) from `tk_mpuser_" + cond.get("appid") + "` where 1=1 " + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<MpUser>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());
    }

    public String queryUseridByOpenid(String appid, String openid) {
        String sql = "select `userid` from `tk_mpuser_" + appid + "` where `openid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, openid);
    }

    public String queryOpenidByUserid(String appid, String userid) {
        String sql = "select `openid` from `tk_mpuser_" + appid + "` where `userid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, userid);
    }

    public MpUser queryByUserid(String appid, String userid) {
        String sql = "select * from `tk_mpuser_" + appid + "` where `userid`=? ";
        return dao.findFirst(sql, userid);
    }

    public Long queryInputtimeByOpenid(String appid, String openid) {
        String sql = "select `inputtime` from `tk_mpuser_" + appid + "` where `openid`=? ";
        return Db.use(Constant.db_dataSource).queryLong(sql, openid);
    }

    public MpUser queryWxnameByOpenid(String appid, String openid) {
        String sql = "select * from `tk_mpuser_" + appid + "` where `openid`=? ";
        return dao.findFirst(sql, openid);
    }

    public List<MpUser> queryListByFollow(String appid) {
        String sql = "select * from `tk_mpuser_" + appid + "` where `userid`!='' and `infollow` = 1 ";
        return dao.find(sql);
    }

    public int countUsernew(String appid, long begintime, long endtime) {
        StringBuffer sb = new StringBuffer();
        String sql = "select count(1) from `tk_mpuser_" + appid + "` where 1=1";
        sb.append(" and `inputtime` >= " + begintime + " and `inputtime` <" + endtime);
        sb.append(" and `infollow` !=-1");
        return Db.use(Constant.db_dataSource).queryInt(sql + sb.toString());
    }

    public int countUserall(String appid, long endtime) {
        StringBuffer sb = new StringBuffer();
        String sql = "select count(1) from `tk_mpuser_" + appid + "` where 1=1";
        sb.append("  and `inputtime` <" + endtime);
        sb.append("  and `infollow` !=-1");
        return Db.use(Constant.db_dataSource).queryInt(sql + sb.toString());
    }

    public String getWxnameUTF8() {
        try {
            return new String(get("wxname"), "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

    private String appid;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return appid;
    }

}
