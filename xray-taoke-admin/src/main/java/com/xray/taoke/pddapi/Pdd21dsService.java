package com.xray.taoke.pddapi;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.act.util.codec.URLCodec;
import com.xray.taoke.admin.utils.ParamUtils;
import com.xray.taoke.tkapi.MpInfoService;
import com.xray.taoke.tkapi.vo.ConstTk;

public class Pdd21dsService {
    private static String getpdditem = "https://api.open.21ds.cn/pdd_api_v1/getpdditem";

    private static String getincrementorderlist = "https://api.open.21ds.cn/pdd_api_v1/getincrementorderlist";

    private static String getpddorderdetail = "https://api.open.21ds.cn/pdd_api_v1/getpddorderdetail";

    private static String docreatepddpid = "https://api.open.21ds.cn/pdd_api_v1/docreatepddpid";

    private static String getpdditemtgurl = "https://api.open.21ds.cn/pdd_api_v1/getpdditemtgurl";

    private static String getnewbilllist = "https://api.open.21ds.cn/pdd_api_v1/getnewbilllist";

    private static Pattern p_itemid = Pattern.compile("&id=(\\d+)");
    private static Pattern p_itemid2 = Pattern.compile("(\\d+)\\.html");
    public static Pdd21dsService instance = new Pdd21dsService();

    public static void main(String[] args) throws Exception {
        String appid = "wx3088f1bd705dfd20";
        String goodid = "5811999281";
        String url = "https://mobile.yangkeduo.com/goods.html?goods_id=8535482957&gallery_id=134904244195&refer_page_name=opt&refer_page_id=10028_1557555481296_4QBE6g7fAF&refer_page_sn=10028";
        goodid = ParamUtils.parseParam(url).params.get("goods_id");
        long starttime = new Date(System.currentTimeMillis() - 60 * 1000).getTime() / 1000;
        long endtime = new Date(System.currentTimeMillis()).getTime() / 1000;
        JSONObject jsonObject = Pdd21dsService.instance.getnewbilllist(appid, starttime, endtime);
        System.out.println(jsonObject);
        // PddItemVo itemVo = PddItemService.instance.getItemVo(appid, goodid);
        // String shortutl = PddItemService.instance.getItemGurl(appid, goodid);

        // String pid = "8386795_65396096";
        // PddItemVo pddItemVo = PddItemService.instance.getItemVo(appid, goodid);
        // System.out.println(pddItemVo.getJiemoney());

        // JSONObject jsonObject = Jd21dsService.instance.gettgiteminfo(appid,
        // "15314114901");
        // System.out.println(jsonObject.getJSONArray("data").get(0));
        // System.out.println(com.jfinal.weixin.sdk.utils.HttpUtils.get("https://u.jd.com/mnYixB"));
        // Jd21dsService.instance.getiteminfo(appid, "45181335092");

        // Jd21dsService.instance.gettgiteminfo(appid, "45181335092");

        // String materialId = "http://item.jd.com/45181335092.html";
        // String link =
        // "http://coupon.jd.com/ilink/couponActiveFront/front_index.action?key=4043c7409b84460fa2557b93c7cd7efc&roleId=19278813&to=leicke.jd.com";
        // System.out.println(Jd21dsService.instance.getitemcpsurl(appid, materialId,
        // ""));
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        // String time = dateFormat.format(new Date(System.currentTimeMillis() -
        // 60000));
        // System.out.println(time);
        // time = "201905061902";
        // JSONObject obj = Jd21dsService.instance.getjdorder(appid, time, 1);
        // System.out.println(obj);

        // JSONObject jsonObject = Jd21dsService.instance.getjfjingxuan(appid, "13",
        // "goodComments", "desc");
        // System.out.println(jsonObject);

    }

    private String reqGet(String url) {
        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (Exception e) {
            throw new RtException(e);
        }
    }

    public String getitemidByHtml(String url) {
        String text = reqGet(url);
        Matcher m = p_itemid.matcher(text);
        if (m.find())
            return m.group(1);
        m = p_itemid2.matcher(text);
        if (m.find())
            return m.group(1);
        return null;
    }

    public JSONObject getpdditem(String appid, String goodid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(getpdditem);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&page=").append(1);
        sb.append("&pagesize=").append(100);
        sb.append("&coupon=").append(true);
        sb.append("&sort=").append(1);
        sb.append("&goods_id_list=").append(goodid);
        System.out.println(sb);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getpdditemtgurl(String appid, String p_id, String goodid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb1 = new StringBuilder();

        sb1.append("[\"").append(goodid).append("\"]");

        StringBuilder sb = new StringBuilder();
        sb.append(getpdditemtgurl);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&p_id=").append(p_id);
        sb.append("&goods_id_list=").append(URLCodec.encode(sb1.toString(), "UTF-8"));
        sb.append("&pdname=").append(map.get("pdname"));
        System.out.println(sb.toString());
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getincrementorderlist(String appid, String time, int type) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(getincrementorderlist);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&start_updte_time=").append(URLCodec.encode(time, "UTF-8"));
        sb.append("&end_update_time=").append(type);
        sb.append("&pdname=").append(1);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getpddorderdetail(String appid, String orderid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        StringBuilder sb = new StringBuilder();
        sb.append(getpddorderdetail);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&order_sn=").append(orderid);
        sb.append("&pdname=").append(map.get("pdname"));
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject docreatepddpid(String appid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        StringBuilder sb = new StringBuilder();
        sb.append(docreatepddpid);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&pdname=").append(map.get("pdname"));
        sb.append("&number=").append(1);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getnewbilllist(String appid, long start_time, long end_time) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        StringBuilder sb = new StringBuilder();
        sb.append(getnewbilllist);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&start_time=").append(start_time);
        sb.append("&end_time=").append(end_time);
        sb.append("&pdname=").append(map.get("pdname"));
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

}
