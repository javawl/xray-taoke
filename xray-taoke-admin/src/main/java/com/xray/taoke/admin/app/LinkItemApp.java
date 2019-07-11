package com.xray.taoke.admin.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.LinkItem;

public class LinkItemApp extends AbstractApp implements Runnable {

    public static void main(String[] args) throws Exception {
        // System.out.println(unicodeToString("\\u6f0f\\u6d1e\\u30102\\u53cc\\uff014\\u53ea\\uff01\\u3011\\u51b0\\u723d\\u60c5\\u4fa3\\u9632\\u6652"));
        // System.out.println(new LinkItemApp().get_allitem_list("冬阴功泡面", 1));
        System.out.println(new LinkItemApp().lanlan_list("", 1).size());
    }

    @Override
    public void run() {
        try {
            long now = System.currentTimeMillis();
            int startsort = 0;
            startsort += this.doHdk(1, startsort, now);
            startsort += this.doHdk(2, startsort, now);

            // startsort = 0;
            // startsort += this.doLanlan(1, startsort, now);
            // startsort += this.doLanlan(2, startsort, now);
        } catch (Exception e) {
            logger.error("errapp ListItemApp", e);
        }
    }

    public int doHdk(int page, int startsort, long now) throws Exception {
        LinkItem data = null;
        int srcsort = 0;
        int counter = 0;

        JSONObject obj = null;
        JSONArray arr = this.hdk_list(page);
        for (int i = 0; i < arr.size(); i++) {
            obj = arr.getJSONObject(i);
            data = LinkItem.dao.queryByItemid(obj.getString("itemid"));
            srcsort = startsort + i + 1;
            if (data != null) {
                if (data.getInt("srctype") != 1) {
                    data.set("srctype", 1);
                    data.set("srcsort", srcsort);
                    data.update();
                } else if (data.getInt("srcsort") > srcsort) {
                    data.set("srcsort", srcsort);
                    data.update();
                }
                continue;
            }
            this.saveHdk(obj, srcsort, now);
            ++counter;
        }
        logger.info("runapp ListItemApp,doHdk size:{}", counter);
        return arr.size();
    }

    public int doLanlan(int page, int startsort, long now) throws Exception {
        LinkItem data = null;
        int srcsort = 0;
        int counter = 0;

        JSONObject obj = null;
        JSONArray arr = this.lanlan_list("", page);
        for (int i = 0; i < arr.size(); i++) {
            obj = arr.getJSONObject(i);
            data = LinkItem.dao.queryByItemid(obj.getString("itemId"));
            srcsort = startsort + i + 1;
            if (data != null) {
                if (data.getInt("srctype") == 1)
                    continue;

                if (data.getInt("srcsort") > srcsort) {
                    data.set("srcsort", srcsort);
                    data.update();
                }
                continue;
            }
            this.saveLanlan(obj, srcsort, now);
            ++counter;
        }
        logger.info("runapp ListItemApp,doLl size:{}", counter);
        return arr.size();
    }

    public JSONArray hdk_list(int page) throws Exception {
        String url = "https://www.haodanku.com/indexapi/hdk_list?type=1&p=" + page + "&search_type=0&category_id=0&price_min=&price_max=&array_type=&sale_min=&tkrates_min=&coupon_max=&tkmoney_min=&avg_min=&discount_max=";
        JSONObject obj = JSONObject.parseObject(Request.Get(url).execute().returnContent().asString());
        return obj.getJSONObject("data").getJSONArray("back");
    }

    public JSONArray get_allitem_list(String keyword, int page) throws Exception {
        String url = "https://www.haodanku.com/indexapi/get_allitem_list?keyword=" + URLEncoder.encode(URLEncoder.encode(keyword, "UTF-8"), "UTF-8") + "&p=" + page;
        JSONObject obj = JSONObject.parseObject(Request.Get(url).execute().returnContent().asString());
        return obj.getJSONArray("item_info");
    }

    public JSONArray lanlan_list(String keywords, int page) throws ClientProtocolException, IOException {
        String url = "http://www.lanlanlife.com/taoke/item/list?time=all&startDate=&endDate=&startHour=&endHour=&shopRank=all&dsr=all&jpdp=0&xfzbz=0&maxPrice=&minPrice=&maxTkPrice=&minTkPrice=&maxTkRate=&minTkRate=&spyxl=&maxqzkb=&minqzkb=&qzkb=&baoyou=0&deliveryIns=0&pinpai=0&minUsedSpeed=&maxUsedSpeed=&maxCountDown=&minAmount=&maxAmount=&minAvailableCount=&maxAvailableCount=&keywords="+keywords+"&pCategoryId=&cCategoryName=&fCategory=&sort=15&qun=all&source_all=all&source_kind=all&jutao=0&ys=0&yr=0&d12=0&brand=all&maxYxe=&minYxe=&is618=0&page=" + page + "&type=index";
        String cookie = "acw_tc=76b20feb15579769587328005e52a6f792d4ffc03e7f7f9cdaad9224e53c84;_uab_collina=155797696165392527268465;aliyungf_tc=AQAAAPFy5XINVAEAbyJbcQ1rEeWBj1Sz;PHPSESSID=ep610jb4udsvb9f3hl4mrguhr5;Hm_lvt_06ea28975b5953dc4ead5ca8422ceff6=1557976962,1558168002,1558689099;u_asec=099%23KAFErYEKE5FEhYTLEEEEEpEQz0yFD6PcDul4G6fcDuiqW6N3DrJMZ60JDEFETcZdt9wuE7EFL2xhGuYTEHI37t4E5Adslw1noZjMZL1Z2e04IICcpO6urZKDkPPNlBvVy%2BvXnJCd9s0zp6PpoR0ez87YwIVVbJuYoZdIkmwPmrY%2B0f%2FMp9NQWhj2wNoSQwJ8sEFEpcZdt3illuZdsyaaolllsgyP%2F36alllzgcZddn3llu8bsyaaolllW2arE7EhssaZttLEBEFE1cZdt0uLyqMhAYFEzsIQDHSScblc7JnsLRrs3oSpiyL06aMqxGbtPt%2BZNda3adWvv0XBLQu33yyBPwI33Me6r0MZPFZqY7u8CzWYaoiqwX1CqaBlYGqHVW1gc57px3WXOaeUlQPJwRCpUwBFEY7ROzugJqm1wMWVCCbv%2Bo01Tr1HqLM1AGrK8%2FkZFQlREirAGR7CEMzRER1hvwIp%2FOWuVf52qY1u%2FOs4DTYM3lup1ucT8gzljYFETrZtt3illDQTEExCbPi5;Hm_lpvt_06ea28975b5953dc4ead5ca8422ceff6=1558689107";
        String result = httpRequest(url, cookie);
        JSONObject obj = JSONObject.parseObject(result);
        JSONArray array = obj.getJSONObject("result").getJSONObject("vm").getJSONArray("list");
        return array;
    }

    public static LinkItem hdkObj(JSONObject obj, int srcsort, long now) {
        LinkItem data = new LinkItem();
        data.set("srctype", 1);
        data.set("srcsort", srcsort);
        data.set("srcsale", obj.getIntValue("itemsale2"));

        data.set("itemid", obj.getString("itemid"));
        data.set("itemtitle", unicodeToString(obj.getString("itemshorttitle")));
        data.set("itemprice", obj.getDoubleValue("itemprice"));
        if (StringUtil.isEmpty(obj.getString("itempic_copy"))) {
            data.set("itempic", obj.getString("itempic"));
        } else if (obj.getString("itempic_copy").startsWith("http")) {
            data.set("itempic", obj.getString("itempic_copy"));
        } else {
            data.set("itempic", "http://img.haodanku.com/" + obj.getString("itempic_copy").replaceAll("_600", ""));
        }
        data.set("itemsale", obj.getIntValue("itemsale"));
        data.set("itemtype", obj.getIntValue("fqcat"));
        data.set("itemdesc", unicodeToString(obj.getString("itemdesc")));

        data.set("cpmoney", obj.getDoubleValue("couponmoney"));
        data.set("tkrate", obj.getDoubleValue("tkrates") / 100.0);
        data.set("tkprice", obj.getDoubleValue("itemendprice"));

        data.set("intmall", "B".equals(obj.getString("shoptype")) ? 1 : 0);
        data.set("inbrand", obj.getIntValue("is_brand") == 1 ? 1 : 0);
        data.set("injutao", obj.getIntValue("is_foreshow") == 1 ? 1 : 0);

        data.set("inputtime", now);
        return data;
    }

    public static LinkItem lanlanObj(JSONObject obj, int srcsort, long now) {
        LinkItem data = new LinkItem();
        data.set("srctype", 2);
        data.set("srcsort", srcsort);

        // 两小时销量
        data.set("srcsale", obj.getInteger("monthSales2h"));
        data.set("itemid", obj.getString("itemId"));
        if(StringUtil.isNotEmpty(obj.getString("title"))) {
            data.set("itemtitle", unicodeToString(obj.getString("title")));
        } else {
            data.set("itemtitle", unicodeToString(obj.getString("itemTitle")));
        }
        data.set("itemprice", LlnumberFormat(obj.getString("originPrice")));

        if(StringUtil.isNotEmpty(obj.getString("sendImage"))) {
            data.set("itempic", obj.getString("sendImage"));
        } else {
            data.set("itempic", obj.getString("coverImage"));
        }

        data.set("itemsale", obj.getIntValue("monthSales"));
        data.set("itemdesc", unicodeToString(obj.getString("recommend")));

        data.set("cpmoney", obj.getDoubleValue("amount"));
        data.set("tkrate", Double.valueOf(obj.getString("tkRate").replaceAll("%", "")) / 100.0);
        data.set("tkprice", LlnumberFormat(obj.getString("price")));

        data.set("intmall", "tmall".equals(obj.getString("source")) ? 1 : 0);
        data.set("injutao", obj.getIntValue("isJuItem") == 1 ? 1 : 0);
        data.set("inbrand", 0);
        data.set("inputtime", now);
        return data;
    }

    private void saveHdk(JSONObject obj, int srcsort, long now) {
        hdkObj(obj, srcsort, now).save();
    }

    private void saveLanlan(JSONObject obj, int srcsort, long now) {
        lanlanObj(obj, srcsort, now).save();
    }

    static Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    public static String unicodeToString(String str) {
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            // group 6728
            String group = matcher.group(2);
            // ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            // group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }

    private static double LlnumberFormat(String price) {
        return Double.valueOf(price.replaceAll("¥", ""));
    }

    public static String stringToUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            // 直接获取字符串的unicode二进制
            byte[] bytes = s.getBytes("unicode");
            // 然后将其byte转换成对应的16进制表示即可
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String httpRequest(String url, String cookie) {
        String result = "";
        String location = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            if (StringUtil.isNotEmpty(cookie))
                connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3067.6 Safari/537.36");
            connection.setConnectTimeout(30000);

            connection.setConnectTimeout(120000);
            // 建立实际的连接
            connection.connect();
            connection.getHeaderField("Location");

            // for(int i=1;connection.getHeaderField(i)!=null;i++){
            // String cookieVal = connection.getHeaderField(i);
            // System.out.println(cookieVal);
            // }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (StringUtil.isNotEmpty(location)) {
            result = "{\"location\":\"" + location + "\"}";
        }
        return result;
    }

}
