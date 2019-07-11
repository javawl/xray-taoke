package com.xray.taoke.act.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.taoke.act.common.Constant;

@TableBind(configName = Constant.db_dataSource, tableName = "tk_mpuser", pkName = "seqid")
public class MpUser extends JfModel<MpUser> {
    private static final long serialVersionUID = 1L;
    public static final MpUser dao = new MpUser();

    public void addByOpenid(String appid, String openid) {
        String sql = "insert into `tk_mpuser_" + appid + "` set `openid`=?,`inputtime`=?,`edittime`=? ON DUPLICATE KEY UPDATE `followno`=`followno`+1,`infollow`=1,`edittime`=?";
        long now = System.currentTimeMillis();
        Db.use(Constant.db_dataSource).update(sql, openid, now, now, now);
    }

    public boolean existByOpenid(String appid, String openid) {
        String sql = "select 1 from `tk_mpuser_" + appid + "` where `openid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, openid) != null;
    }

    public String queryUseridBySeqid(String appid, String seqid) {
        String sql = "select `userid` from `tk_mpuser_" + appid + "` where `seqid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, seqid);
    }

    public String queryOpenidBySeqid(String appid, String seqid) {
        String sql = "select `openid` from `tk_mpuser_" + appid + "` where `seqid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, seqid);
    }

    public String queryUseridByOpenid(String appid, String openid) {
        String sql = "select * from `tk_mpuser_" + appid + "` where `openid`=? ";
        MpUser data = dao.findFirst(sql, openid);
        if (data != null)
            return data.getStr("userid");
        addByOpenid(appid, openid);
        return null;
    }

    public String queryKefuidByOpenid(String appid, String openid) {
        String sql = "select `kefuid` from `tk_mpuser_" + appid + "` where `openid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, openid);
    }

    public void updateUseridBySeqid(String appid, String seqid, String userid) {
        String sql = "update  `tk_mpuser_" + appid + "` set `userid`=? where `seqid`=?";
        Db.update(sql, userid, seqid);
    }

    public void updateUseridByOpenid(String appid, String openid, String userid) {
        String sql = "update  `tk_mpuser_" + appid + "` set `userid`=? where `openid`=?";
        Db.update(sql, userid, openid);
    }

    public void updateKefuidByOpenid(String appid, String openid, String kefuid) {
        String sql = "update  `tk_mpuser_" + appid + "` set `kefuid`=? where `openid`=?";
        Db.update(sql, kefuid, openid);
    }

    public void updateInfollowByOpenid(String appid, String openid) {
        String sql = "update  `tk_mpuser_" + appid + "` set `infollow`=-1 where `openid`=?";
        Db.update(sql, openid);
    }

    public String queryOpenidByUserId(String appid, String userid) {
        String sql = "select `openid` from `tk_mpuser_" + appid + "` where `userid`=? ";
        return Db.use(Constant.db_dataSource).queryStr(sql, userid);
    }

}
