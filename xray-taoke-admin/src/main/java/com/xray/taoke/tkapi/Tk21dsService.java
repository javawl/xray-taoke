package com.xray.taoke.tkapi;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.act.util.codec.URLCodec;
import com.xray.taoke.tkapi.vo.ConstTk;

public class Tk21dsService {
    private static String iteminfo = "https://api.open.21ds.cn/apiv1/getiteminfo?apkey=";
    private static String getitemgyurl = "https://api.open.21ds.cn/apiv1/getitemgyurl?apkey=";
    private static String getitemgybytpwd = "https://api.open.21ds.cn/apiv1/getitemgyurlbytpwd?apkey=";
    private static String getgyurlbyall = "https://api.open.21ds.cn/apiv1/getgyurlbyall?apkey=";
    private static String tkorder = "https://api.open.21ds.cn/apiv1/gettkorder?apkey=";
    private static Pattern p_itemid = Pattern.compile("&id=(\\d+)");
    private static Pattern p_itemid2 = Pattern.compile("i(\\d+)\\.htm");
    private static Pattern p_extraData = Pattern.compile("var extraData = (\\{.+\\});");

    public static Tk21dsService instance = new Tk21dsService();

    public static void main(String[] args) throws Exception {
        String appid = "wx3088f1bd705dfd20";
        String itemid = "45034936342";
        String pid = "mm_44084035_24540890_105835700459";
        // String url = "https://m.tb.cn/h.3B43F8N?sm=f3df9e";
        System.out.println(Tk21dsService.instance.getitemgyurl(appid, itemid).toJSONString());
        System.out.println(Tk21dsService.instance.gettkorder(appid, "2019-04-12 10:18:22"));
        System.out.println(Tk21dsService.instance.getitemgyurlbytpwd(appid, "￥WfzpbA2OdBX￥"));
        // https://m.tb.cn/h.3zbBirk?sm=d0e54c
        // https://m.tb.cn/h.eZO1bAL
        // https://m.tb.cn/h.eZlAEDu?sm=130fd6
        System.out.println(Tk21dsService.instance.getiteminfoByHtml("https://m.tb.cn/h.eZlAEDu?sm=130fd6"));
        System.out.println(Tk21dsService.instance.getgyurlbyall(appid, pid, "￥xijeYVIWRUf￥"));
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

    public JSONObject getiteminfoByHtml(String url) {
        String text = reqGet(url);

        JSONObject obj = new JSONObject();
        Matcher m = p_extraData.matcher(text);
        if (!m.find())
            throw new RtException(ConstTk.parse_err, "err parse,none extraData,url:" + url);
        obj = JSONObject.parseObject(m.group(1));

        m = p_itemid.matcher(text);
        if (m.find()) {
            obj.put("itemid", m.group(1));
            return obj;
        }

        m = p_itemid2.matcher(text);
        if (m.find()) {
            obj.put("itemid", m.group(1));
            return obj;
        }
        throw new RtException(ConstTk.parse_err, "err parse,none itemid,url:" + url);
    }

    public JSONObject getiteminfo1(String appid, String itemid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(iteminfo).append(map.get("apikey"));
        sb.append("&itemid=").append(itemid);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getitemgyurl(String appid, String itemid) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(getitemgyurl).append(map.get("apikey"));
        sb.append("&itemid=").append(itemid);
        sb.append("&pid=").append(map.get("pid"));
        sb.append("&tbname=").append(map.get("tbname"));
        sb.append("&tpwd=").append(1);

        JSONObject obj = JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
        if (obj.getInteger("code") == 15)
            throw new RtException(ConstTk.notaoke_err, "err getitemgyurl,itemid:" + itemid + ",json:" + obj.toJSONString());
        if (obj.getInteger("code") != 200)
            throw new RtException(ConstTk.data_err, "err getitemgyurl,itemid:" + itemid + ",json:" + obj.toJSONString());

        return obj.getJSONObject("result").getJSONObject("data");
    }

    public JSONObject getitemgyurlbytpwd(String appid, String tpwdcode) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(getitemgybytpwd).append(map.get("apikey"));
        sb.append("&tpwdcode=").append(tpwdcode);
        sb.append("&pid=").append(map.get("pid"));
        sb.append("&tbname=").append(map.get("tbname"));
        sb.append("&tpwd=").append(1);
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getitemgyurlbytpwd(String tpwdcode, String apikey, String tbname, String pid) {
        StringBuilder sb = new StringBuilder();
        sb.append(getitemgybytpwd).append(apikey);
        sb.append("&tpwdcode=").append(tpwdcode);
        sb.append("&pid=").append(pid);
        sb.append("&tbname=").append(tbname);
        sb.append("&tpwd=").append(1);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    public JSONObject gettkorder(String appid, String starttime) throws Exception {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);

        StringBuilder sb = new StringBuilder();
        sb.append(tkorder).append(map.get("apikey"));
        sb.append("&starttime=").append(URLCodec.encode(starttime, "UTF-8"));
        sb.append("&span=").append(1200);
        sb.append("&page=").append(1);
        sb.append("&pagesize=").append(100);
        sb.append("&tkstatus=").append(1);
        sb.append("&ordertype=").append("create_time");
        sb.append("&tbname=").append(map.get("tbname"));
        return JSONObject.parseObject(Request.Get(sb.toString()).execute().returnContent().asString());
    }

    public JSONObject getiteminfo(String itemid, String apikey) {
        StringBuilder sb = new StringBuilder();
        sb.append(iteminfo).append(apikey);
        sb.append("&itemid=").append(itemid);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    public JSONObject getitemgyurl(String itemid, String apikey, String tbname, String pid) {
        StringBuilder sb = new StringBuilder();
        sb.append(getitemgyurl).append(apikey);
        sb.append("&itemid=").append(itemid);
        sb.append("&pid=").append(pid);
        sb.append("&tbname=").append(tbname);
        sb.append("&tpwd=").append(1);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }
    
    public JSONObject getgyurlbyall(String appid, String pid, String content) {
        Map<String, String> map = MpInfoService.instance.getMpInfo(appid);
        if (map == null || map.size() <= 0)
            throw new RtException(ConstTk.para_err, "err para,appid:" + appid);
        
        StringBuilder sb = new StringBuilder();
        sb.append(getgyurlbyall).append(map.get("apikey"));
        sb.append("&tbname=").append(map.get("tbname"));
        sb.append("&pid=").append(pid);
        sb.append("&content=").append(content);
        sb.append("&tpwd=").append(1);
        
        System.out.println(sb.toString());
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    private String reqGet(String url) {
        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (Exception e) {
            throw new RtException(e);
        }
    }

}
