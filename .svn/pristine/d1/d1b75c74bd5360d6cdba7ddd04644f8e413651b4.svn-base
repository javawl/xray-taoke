package com.xray.taoke.admin.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.fluent.Request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.DateUtil;
import com.xray.taoke.tkapi.HaodankuService;
import com.xray.taoke.tkapi.TbItemService;
import com.xray.taoke.tkapi.vo.TbItemVo;

public class LinkWareApp extends AbstractApp {

    public static void main(String[] args) throws Exception {
        new LinkWareApp().run();
    }

    @Override
    public void run() {
        try {
            
            
            String lwid = "i01";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 1, 2));// 女装
            TbItemService.instance.setItemList(lwid, doHdkList(1, 3, 2), true);// 内衣
            
            
            lwid = "i00";
            HaodankuService.instance.doAllitemList(lwid,"", 1);
            
            lwid = "i04";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 4, 2));// 美妆

            lwid = "i06";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 6, 2));// 鞋品
            TbItemService.instance.setItemList(lwid, doHdkList(1, 7, 2), true);// 箱包

            lwid = "i08";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 8, 2));// 母婴
            TbItemService.instance.setItemList(lwid, doHdkList(1, 9, 2), true);// 儿童

            lwid = "i10";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 10, 2));// 居家
            TbItemService.instance.setItemList(lwid, doHdkList(1, 13, 2));// 家电

            lwid = "i11";
            TbItemService.instance.setItemList(lwid, doHdkList(1, 11, 2));// 美食

            lwid = "i42";
            TbItemService.instance.setItemList(lwid, doHdkColumn(2));// 9.9包邮

            lwid = "i43";
            TbItemService.instance.setItemList(lwid, doHdkColumn(3));// 30元封顶

            lwid = "i44";
            TbItemService.instance.setItemList(lwid, doHdkColumn(4));// 聚划算

            lwid = "i45";
            TbItemService.instance.setItemList(lwid, doHdkColumn(5));// 淘抢购

            lwid = "i47";
            TbItemService.instance.setItemList(lwid, doHdkColumn(7));// 预告单

            lwid = "i48";
            TbItemService.instance.setItemList(lwid, doHdkBrand());// 品牌单

            cache.set("LinkWareApp", DateUtil.now());
            logger.info("runapp LinkWareApp");
        } catch (Exception e) {
            logger.error("errapp LinkWareApp", e);
        }
    }
    
    public List<TbItemVo> doHdkBrand() throws Exception {
        List<TbItemVo> list = new ArrayList<TbItemVo>();
        JSONArray arr = HaodankuService.instance.brand().getJSONArray("data");
        for (int i = 0; i < arr.size(); i++) {
            list.addAll(HaodankuService.instance.hdk2list(arr.getJSONObject(i).getJSONArray("item")));
        }
        return list;
    }

    // type=1是今日上新（当天新券商品），type=2是9.9包邮，type=3是30元封顶，type=4是聚划算，type=5是淘抢购，type=6是0点过夜单，type=7是预告单，type=8是品牌单，type=9是天猫商品，type=10是视频单
    public List<TbItemVo> doHdkColumn(int type) throws Exception {
        return HaodankuService.instance.hdk2list(HaodankuService.instance.column(type).getJSONArray("data"));
    }

    public List<TbItemVo> doHdkList(int page, int category_id, double tkmoney_min) throws Exception {
        return HaodankuService.instance.hdk2list(this.hdk_list(page, category_id, tkmoney_min));
    }

    public JSONArray hdk_list(int page, int category_id, double tkmoney_min) throws Exception {
        String url = "https://www.haodanku.com/indexapi/hdk_list?type=1&p=" + page + "&search_type=1&category_id=" + category_id + "&price_min=&price_max=&array_type=&sale_min=&tkrates_min=&coupon_max=&tkmoney_min=" + tkmoney_min + "&avg_min=&discount_max=";
        JSONObject obj = JSONObject.parseObject(Request.Get(url).execute().returnContent().asString());
        return obj.getJSONObject("data").getJSONArray("back");
    }

}
