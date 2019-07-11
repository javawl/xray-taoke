package com.xray.taoke.tkapi;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.taoke.tkapi.vo.ConstTk;
import com.xray.taoke.tkapi.vo.TbItemVo;

public class HaodankuService {
    private static String itemlist = "http://v2.api.haodanku.com/itemlist/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String brand = "http://v2.api.haodanku.com/brand/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String brandinfo = "http://v2.api.haodanku.com/brandinfo/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String singlebrand = "http://v2.api.haodanku.com/singlebrand/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String sales_list = "http://v2.api.haodanku.com/sales_list/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String column = "http://v2.api.haodanku.com/column/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String supersearch = "http://v2.api.haodanku.com/supersearch/apikey/" + ConstTk.config.get("hdk.apikey");
    private static String hot_key = "http://v2.api.haodanku.com/hot_key/apikey/" + ConstTk.config.get("hdk.apikey");

    public static HaodankuService instance = new HaodankuService();

    public static void main(String[] args) throws Exception {
        System.out.println(HaodankuService.instance.hot_key());
        // System.out.println(HaodankuService.instance.brandinfo(1));
        // System.out.println(HaodankuService.instance.singlebrand(31));
    }

    public JSONObject hot_key() {
        StringBuilder sb = new StringBuilder();
        sb.append(hot_key);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // cid：0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体，17宠物
    public JSONObject itemlist(int cid) {
        StringBuilder sb = new StringBuilder();
        sb.append(itemlist);
        sb.append("/nav/3");
        sb.append("/cid/").append(cid);
        sb.append("/back/100");
        sb.append("/sort/0");// 按最新排序
        sb.append("/min_id/1");
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // 超值大牌
    public JSONObject brand() {
        StringBuilder sb = new StringBuilder();
        sb.append(brand);
        sb.append("/back/100");
        sb.append("/min_id/1");
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // 品牌信息
    public JSONObject brandinfo(int brandcat) {
        StringBuilder sb = new StringBuilder();
        sb.append(brandinfo);
        sb.append("/back/100");
        sb.append("/min_id/1");
        sb.append("/brandcat/").append(brandcat);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // 单个品牌
    public JSONObject singlebrand(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(singlebrand);
        sb.append("/id/").append(id);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    public JSONObject singlebrand(int id, int min_id) {
        StringBuilder sb = new StringBuilder();
        sb.append(singlebrand);
        sb.append("/id/").append(id);
        sb.append("/min_id/").append(min_id);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // 各大榜单
    public JSONObject sales_list(int sale_type) {
        StringBuilder sb = new StringBuilder();
        sb.append(sales_list);
        sb.append("/sale_type/").append(sale_type);
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    // 商品筛选
    // type=1是今日上新（当天新券商品），type=2是9.9包邮，type=3是30元封顶，type=4是聚划算，type=5是淘抢购，type=6是0点过夜单，type=7是预告单，type=8是品牌单，type=9是天猫商品，type=10是视频单
    public JSONObject column(int type) {
        StringBuilder sb = new StringBuilder();
        sb.append(column);
        sb.append("/type/").append(type);
        sb.append("/back/200");
        sb.append("/min_id/1");
        sb.append("/sort/11");
        sb.append("/sale_min/200");
        return JSONObject.parseObject(reqGet(sb.toString()));
    }

    public JSONObject supersearch(String keyword, int min_id, int tp_p) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(supersearch);
            sb.append("/keyword/").append(URLEncoder.encode(URLEncoder.encode(keyword, "UTF-8"), "UTF-8"));
            sb.append("/back/100");
            sb.append("/min_id/").append(min_id);
            sb.append("/tp_p/").append(tp_p);
            sb.append("/sort/1");
            sb.append("/is_tmall/1");
            sb.append("/is_coupon/1");
            return JSONObject.parseObject(reqGet(sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doSupersearch(String keyword, int min_id, int tp_p) {
        JSONObject obj = HaodankuService.instance.supersearch(keyword, min_id, tp_p);
        String lid = ConstTk.getUuid16();
        TbItemService.instance.addItemListVo(lid, this.hdk2list(obj.getJSONArray("data")));
        return lid;
    }

    public JSONArray get_allitem_list(String keyword, int pno) throws Exception {
        String url = "https://www.haodanku.com/indexapi/get_allitem_list?keyword=" + URLEncoder.encode(URLEncoder.encode(keyword, "UTF-8"), "UTF-8") + "&sort=1&p=" + pno;
        JSONObject obj = JSONObject.parseObject(Request.Get(url).execute().returnContent().asString());
        return obj.getJSONArray("item_info");
    }

    public void doAllitemList(String lwid, String keyword, int pno) {
        try {
            TbItemService.instance.delItemList(lwid);
            JSONArray arr = HaodankuService.instance.get_allitem_list(keyword, pno);
            List<TbItemVo> list = HaodankuService.instance.hdk2list(arr);
            TbItemService.instance.setItemList(lwid, list, false, 3600, false);
        } catch (Exception e) {
            throw new RtException("hdk err,keyword:" + keyword, e);
        }
    }

    private String reqGet(String url) {
        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (Exception e) {
            throw new RtException(e);
        }
    }

    public List<TbItemVo> hdk2list(JSONArray arr) {
        List<TbItemVo> list = new ArrayList<TbItemVo>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(this.hdk2item(arr.getJSONObject(i)));
        }
        return list;
    }

    public TbItemVo hdk2item(JSONObject obj) {
        TbItemVo data = obj.toJavaObject(TbItemVo.class);
        data.setCpmoney(obj.getDoubleValue("couponmoney"));
        if (obj.containsKey("itemshorttitle"))
            data.setItemtitle(ConstTk.unicodeToString(obj.getString("itemshorttitle")));
        else
            data.setItemtitle(obj.getString("itemtitle"));
        data.setTkrate(obj.getDoubleValue("tkrates") / 100.0);
        data.setIntmall("B".equals(obj.getString("shoptype")) ? 1 : 0);
        return data;
    }

}
