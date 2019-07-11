package com.xray.taoke.admin.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.act.util.codec.URLCodec;
import com.xray.taoke.admin.common.Constant;

public class Tk21dsService {
    private static String iteminfo = "https://api.open.21ds.cn/apiv1/getiteminfo?apkey=";
    private static String itemgy = "https://api.open.21ds.cn/apiv1/getitemgyurl?apkey=";
    private static String tkorder = "https://api.open.21ds.cn/apiv1/gettkorder?apkey=";
    private static String getitemgybytpwd = "https://api.open.21ds.cn/apiv1/getitemgyurlbytpwd?apkey=";
    private static Pattern p_itemid = Pattern.compile("&id=(\\d+)");
    private static Pattern p_extraData = Pattern.compile("var extraData = (\\{.+\\});");

    public static Tk21dsService instance = new Tk21dsService();

    public static void main(String[] args) throws Exception {
        String appid = "wx3088f1bd705dfd20";
        String itemid = "576029597795";
        // String url = "https://m.tb.cn/h.3B43F8N?sm=f3df9e";
        // System.out.println(Tk21dsService.instance.getiteminfo("586994766563"));
        System.out.println(Tk21dsService.instance.getitemgyurl(appid, itemid));
        System.out.println(Tk21dsService.instance.gettkorder(appid, "2019-03-31 16:19:00", 60));
        // System.out.println(Tk21dsService.instance.getiteminfoByHtml("https://m.tb.cn/h.3zbBirk?sm=d0e54c"));
    }

    public JSONObject getitemgyurlbytpwd(String appid, String tpwdcode) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(Constant.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(getitemgybytpwd).append(map.get("apikey"));
        sb.append("&tpwdcode=").append(Constant.parseTkpwd(tpwdcode));
        sb.append("&pid=").append(map.get("pid"));
        sb.append("&tbname=").append(map.get("tbname"));
        sb.append("&tpwd=").append(1);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getiteminfoByHtml(String url) throws Exception {
        String text = Request.Get(url).execute().returnContent().asString();
        Matcher m = p_extraData.matcher(text);
        if (!m.find())
            throw new RtException(Constant.data_err, "err parse,none extraData,url:" + url);
        JSONObject obj = JSONObject.parseObject(m.group(1));

        m = p_itemid.matcher(text);
        if (!m.find())
            throw new RtException(Constant.data_err, "err parse,none itemid,url:" + url);
        obj.put("itemid", m.group(1));

        String pic = obj.getString("pic");
        if (pic.indexOf("430x430") <= 0)
            obj.put("pic", pic + "_430x430q90.jpg");
        return obj;
    }

    public JSONObject getiteminfo(String appid, String itemid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(Constant.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(iteminfo).append(map.get("apikey"));
        sb.append("&itemid=").append(itemid);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getitemgyurl(String appid, String itemid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(Constant.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(itemgy).append(map.get("apikey"));
        sb.append("&itemid=").append(itemid);
        sb.append("&pid=").append(map.get("pid"));
        sb.append("&tbname=").append(map.get("tbname"));
        sb.append("&tpwd=").append(1);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject gettkorder(String appid, String starttime, int span) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(Constant.para_err, "err para,appid:" + appid);
        StringBuilder sb = new StringBuilder();
        sb.append(tkorder).append(map.get("apikey"));
        sb.append("&starttime=").append(URLCodec.encode(starttime, "UTF-8"));
        // sb.append("&span=").append(1200);
        sb.append("&span=").append(span);
        sb.append("&page=").append(1);
        sb.append("&pagesize=").append(100);
        sb.append("&tkstatus=").append(1);
        sb.append("&ordertype=").append("create_time");
        sb.append("&tbname=").append(map.get("tbname"));
        String text = Request.Get(sb.toString()).execute().returnContent().asString();
        try {
            return JSONObject.parseObject(text);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(text);
        }
        return null;
    }

}
