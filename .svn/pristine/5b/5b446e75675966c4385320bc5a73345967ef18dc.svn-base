package com.xray.taoke.admin.app;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.act.exception.RtException;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.tkapi.HaodankuService;
import com.xray.taoke.tkapi.TbItemService;
import com.xray.taoke.tkapi.vo.TbItemVo;

public class HaodankuApp extends AbstractApp {

    public static void main(String[] args) {
        new HaodankuApp().start();
    }

    @Override
    public void run() {
        try {
            doRun();
            logger.info("runapp haodanku");
        } catch (Exception e) {
            logger.error("errapp haodanku", e);
        }
    }

    public void doRun() {
        int size = 0;
        // brand();

        for (int i = 1; i <= 17; i++) {
            size += itemlist(i);
        }

        for (int i = 1; i <= 4; i++) {
            size += sales_list(i);
        }

        for (int i = 1; i <= 10; i++) {
            size += column(i);
        }
        logger.info("runapp haodanku,size:{}", size);
    }

    public int brand() {
        JSONObject obj = HaodankuService.instance.brand();
        if (obj.getInteger("code") != 1)
            throw new RtException(Constant.data_err);

        List<TbItemVo> list = new ArrayList<TbItemVo>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0; i < arr.size(); i++) {
            list.add(obj2vo(arr.getJSONObject(i)));
        }
        TbItemService.instance.addItemListVo(String.valueOf(100), list);
        return list.size();
    }

    // ID 200
    // 1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体，17宠物
    public int itemlist(int cid) {
        JSONObject obj = HaodankuService.instance.itemlist(cid);
        if (obj.getInteger("code") != 1)
            throw new RtException(Constant.data_err);

        List<TbItemVo> list = new ArrayList<TbItemVo>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0; i < arr.size(); i++) {
            list.add(obj2vo(arr.getJSONObject(i)));
        }
        TbItemService.instance.addItemListVo(String.valueOf(cid + 200), list);
        return list.size();
    }

    // ID 300 type=1是实时销量榜（近2小时销量），type=2是今日爆单榜，type=3是昨日爆单榜，type=4是出单指数版
    public int sales_list(int type) {
        JSONObject obj = HaodankuService.instance.sales_list(type);
        if (obj.getInteger("code") != 1)
            throw new RtException(Constant.data_err);

        List<TbItemVo> list = new ArrayList<TbItemVo>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0; i < arr.size(); i++) {
            list.add(obj2vo(arr.getJSONObject(i)));
        }
        TbItemService.instance.addItemListVo(String.valueOf(type + 300), list);
        return list.size();
    }

    // ID 400
    // type=1是今日上新（当天新券商品），type=2是9.9包邮，type=3是30元封顶，type=4是聚划算，type=5是淘抢购，type=6是0点过夜单，type=7是预告单，type=8是品牌单，type=9是天猫商品，type=10是视频单
    public int column(int type) {
        JSONObject obj = HaodankuService.instance.column(type);
        if (obj.getInteger("code") != 1)
            throw new RtException(Constant.data_err);

        List<TbItemVo> list = new ArrayList<TbItemVo>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0; i < arr.size(); i++) {
            list.add(obj2vo(arr.getJSONObject(i)));
        }
        TbItemService.instance.addItemListVo(String.valueOf(type + 400), list);
        return list.size();
    }

    public TbItemVo obj2vo(JSONObject obj) {
        TbItemVo data = new TbItemVo();
        data.setItemid(obj.getString("itemid"));
        data.setItemtitle(obj.getString("itemtitle"));
        data.setItemprice(obj.getDoubleValue("itemprice"));
        data.setItempic(obj.getString("itempic"));
        data.setCpmoney(obj.getDoubleValue("couponmoney"));
        data.setTkrate(obj.getDoubleValue("tkrates") / 100);
        return data;
    }

}
