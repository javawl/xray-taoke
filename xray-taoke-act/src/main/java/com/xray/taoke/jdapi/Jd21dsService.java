package com.xray.taoke.jdapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.xray.act.exception.RtException;
import com.xray.act.util.StringUtil;
import com.xray.act.util.codec.URLCodec;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.tkapi.MpInfoService;
import com.xray.taoke.tkapi.vo.ConstTk;

public class Jd21dsService {
    private static String iteminfo = "https://api.open.21ds.cn/jd_api_v1/getjdunionitems";

    private static String jdorder = "https://api.open.21ds.cn/jd_api_v1/getjdunionorders";

    private static String cpurl = "https://api.open.21ds.cn/jd_api_v1/getitemcpsurl";

    private static String jfjingxuan = "https://api.open.21ds.cn/jd_api_v1/jfjingxuan";

    private static String giteminfo = "https://api.open.21ds.cn/jd_api_v1/gettgiteminfo";

    private static String apikey = "286fedb525ca9c79";
    
    
    private static Pattern p_itemid = Pattern.compile("&id=(\\d+)");
    private static Pattern p_itemid2 = Pattern.compile("(\\d+)\\.html");
    public static Jd21dsService instance = new Jd21dsService();

    public static void main(String[] args) throws Exception {
        String appid = "wx3088f1bd705dfd20";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String time = dateFormat.format(new Date(System.currentTimeMillis() - 300000));
        JSONObject obj = Jd21dsService.instance.getjdorder(appid, time, 1);
        System.out.println(obj);

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

    public JSONObject getiteminfo(String appid, String skuIds) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(iteminfo);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&skuIds=").append(skuIds);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getjdorder(String appid, String time, int type) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        // key jd推广key,redis
        StringBuilder sb = new StringBuilder();
        sb.append(jdorder);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&time=").append(URLCodec.encode(time, "UTF-8"));
        sb.append("&type=").append(type);
        sb.append("&pageNo=").append(1);
        sb.append("&pageSize=").append(100);

        sb.append("&key=").append(map.get("jdkey"));
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getitemcpsurl(String appid, String materialId, String couponUrl) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        // materialId 推广商品或者推广优惠券链接
        // unionId jd推广id,redis
        StringBuilder sb = new StringBuilder();
        sb.append(cpurl);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&materialId=").append(URLCodec.encode(materialId, "UTF-8"));
        sb.append("&unionId=").append(map.get("jdid"));

        if (StringUtil.isNotEmpty(couponUrl)) {
            sb.append("&couponUrl=").append(URLCodec.encode(couponUrl, "UTF-8"));
        }
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getjfjingxuan(String appid, String eliteId, String sortName) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        StringBuilder sb = new StringBuilder();
        sb.append(jfjingxuan);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&eliteId=").append(eliteId);
        sb.append("&pageIndex=").append(1);
        sb.append("&pageSize=").append(50);
        sb.append("&sortName=").append(sortName);
        sb.append("&sort=").append("desc");
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject gettgiteminfo(String appid, String skuIds) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(giteminfo);
        sb.append("?apkey=").append(map.get("apikey"));
        sb.append("&skuids=").append(skuIds);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    
    
    public void doAllitemList(String lwid, String keyword, int pno) {
        try {
            JdItemService.instance.delItemList(lwid);
            JdItemService.instance.setItemList(lwid, jd_list(1, keyword),false);// 美妆
        } catch (Exception e) {
            throw new RtException("jd err,keyword:" + keyword, e);
        }
    }
    
    public List<JdItemVo> jd_list(int page, String cname) throws Exception {
        String url = "http://api-gw.haojingke.com/index.php/api/platform/openapi";
        String data = "&source_type=1&type=goodslist&apikey="+apikey+"&cname=" + cname;
        System.out.println(HttpUtils.post(url, data));
        JSONObject obj = JSONObject.parseObject(HttpUtils.post(url, data));
        JSONArray arr = obj.getJSONArray("data");
        List<JdItemVo> list = new ArrayList<JdItemVo>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(this.jd2item(arr.getJSONObject(i)));
        }
        return list;
        
    }
    public JdItemVo jd2item(JSONObject obj) {
        JdItemVo data = new JdItemVo();
        data.setItemid(obj.getString("skuId"));
        data.setItemtitle(obj.getString("skuName"));
        data.setItempic(obj.getString("picUrl"));
        data.setCpmoney(obj.getDoubleValue("discount"));
        data.setItemprice(obj.getDoubleValue("wlPrice"));
        data.setCpurl(obj.getString("couponList"));
        data.setTkmoney(obj.getDoubleValue("wlCommission"));
        data.setTkrate(obj.getDoubleValue("wlCommissionShare")/100);
        return data;
    }
    
    
    
    
}
