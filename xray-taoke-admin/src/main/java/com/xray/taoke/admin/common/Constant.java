package com.xray.taoke.admin.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.xray.act.util.StringUtil;
import com.xray.act.util.codec.MD5Codec;

public class Constant {
    public static final Prop config = PropKit.use("config.properties");
    public static final String db_dataSource = "db.dataSource.main";

    private static final Pattern tkpwd_pattern = Pattern.compile("￥([a-zA-Z0-9]{11})￥");

    public static final String wxsns_appid = config.get("wxsns.appid");
    public static final String wxsns_appsecret = config.get("wxsns.appsecret");
    public static final String wxsns_bindUri = config.get("wxsns.bindUri");

    public static final String wxapi_url = config.get("wxapi.url");

    public static final String hdk_apikey = config.get("hdk.apikey");

    public static final int para_err = -100;
    public static final int data_err = -101;
    public static final int notaoke_err = -102;

    private static final Pattern url_pattern = Pattern.compile("(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))");
    private static final Pattern itemid_pattern = Pattern.compile("id=(\\d+)");
    private static final DecimalFormat num_format = new DecimalFormat("0.00");

    public static void main(String[] args) {
        
        System.out.println(parseItemid("https://detail.tmall.com/item.htm?spm=a220o.1000855.w4023-9674083403.4.48de43a9NlUPVc&id=554924023146"));
    }

    public static String getAuthorizeURL(String state) {
        return SnsAccessTokenApi.getAuthorizeURL(Constant.wxsns_appid, Constant.wxsns_bindUri, state, true);
    }

    public static String parseUrl(String text) {
        Matcher m = url_pattern.matcher(text);
        if (m.find())
            return m.group(0);
        return null;
    }

    public static String parseItemid(String text) {
        Matcher m = itemid_pattern.matcher(text);
        if (m.find())
            return m.group(1);
        return null;
    }

    public static String parseTkpwd(String text) {
        Matcher m = tkpwd_pattern.matcher(text);
        if (m.find())
            return m.group(0);
        return null;
    }

    public static String getLinkUrl() {
        return config.get("link_url");
    }

    public static String getShopurl() {
        return config.get("shopurl");
    }

    public static String getTkLinkUrl() {
        return config.get("tk_link_url");
    }

    public static String getImgUrl() {
        return config.get("img_url");
    }
    
    public static String getTkapiAppid() {
        return config.get("tkapi.appid", "wx8dd7601283019269");
    }
    
    public static String getPath() {
        return config.get("path");
    }
    
    
    
    
    

    public static List<String> getUserList() {
        List<String> list_send = new ArrayList<>();
        list_send.add("songfeiyan");
        list_send.add("luojianlong");
        list_send.add("xuruibo");
        list_send.add("huguangyu");
        list_send.add("wenhaotian");
        list_send.add("nannan");
        list_send.add("yangshaojie");
        list_send.add("huangxiaolan");
        list_send.add("jiangsitan");
        return list_send;
    }

    public static String getOpenid(String bookby) {
        String name = config.get(bookby);
        if (StringUtil.isEmpty(name))
            return null;
        return config.get(bookby);
    }

    public static String getApiPay(String prefix) {
        return config.get(prefix + "_apikey");
    }

    public static String getIp(String prefix) {
        return config.get(prefix + "_ip");
    }

    public static String getPath(String prefix) {
        return config.get(prefix + "_path");
    }

    public static String getMchid(String prefix) {
        return config.get(prefix + "_mch_id");
    }

    public static String formatPrice(double d) {
        return num_format.format(d);
    }

    public static String toMediaid(String str) {
        return "wx_" + MD5Codec.encode(str).substring(8, 24);
    }

}
