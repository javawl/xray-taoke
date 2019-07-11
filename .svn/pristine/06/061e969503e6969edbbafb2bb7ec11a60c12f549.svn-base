package com.xray.taoke.admin.app.jd;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xray.taoke.jdapi.Jd21dsService;
import com.xray.taoke.jdapi.JdItemService;
import com.xray.taoke.jdapi.vo.JdItemVo;

public class JdItemApp extends AbstractApp {
    private static final String appid = "wx8dd7601283019269";

    public static void main(String[] args) {
        new JdItemApp().start();
    }

    @Override
    public void run() {
        try {
            doRun();
            logger.info("runapp JdItemApp");
        } catch (Exception e) {
            logger.error("errapp JdItemApp", e);
        }
    }

    public void doRun() {
        int size = 0;

        for (int i = 1; i <= 18; i++) {
            size += itemlist(i);
        }

        logger.info("runapp haodanku,size:{}", size);
    }

    // JDID 200
    public int itemlist(int cid) {
        JSONObject obj = null;
        String sort = "price";
        try {
            obj = Jd21dsService.instance.getjfjingxuan(appid, cid + "", sort, "asc");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (obj.getInteger("code") == -1)
            return 0;

        // 没有优惠券的就过滤
        List<JdItemVo> list = new ArrayList<JdItemVo>();
        JSONArray arr = obj.getJSONArray("data");
        JSONArray couponList = null;
        JSONObject couponInfo_list = null;
        for (int i = 0; i < arr.size(); i++) {
            couponInfo_list = arr.getJSONObject(i).getJSONObject("couponInfo");
            couponList = couponInfo_list.getJSONArray("couponList");
            if (couponList.size() == 0)
                continue;
            JSONArray pingouJson = null;
            try {
                pingouJson = arr.getJSONObject(i).getJSONArray("pinGouInfo");
            } catch (Exception e) {
                // 这样处理是因为京东有拼团和没拼团返回数据类型不一样，太坑！
                // TODO: handle exception
                continue;
            }
            if (!pingouJson.isEmpty())
                continue;
            JdItemVo itemVo = obj2vo(arr.getJSONObject(i));
            if (itemVo == null)
                continue;
            list.add(itemVo);
        }
        JdItemService.instance.addItemListVo(String.valueOf(cid + 200), list);
        return list.size();
    }

    public JdItemVo obj2vo(JSONObject obj) {
        JdItemVo data = new JdItemVo();

        String skuId = obj.getString("skuId");
        String skuName = obj.getString("skuName");
        JSONObject jsonObject = obj.getJSONObject("priceInfo");
        double itemprice = jsonObject.getDoubleValue("price");
        String itempic = obj.getJSONObject("imageInfo").getJSONArray("imageList").getJSONObject(0).getString("url");

        JSONObject couponInfo_list = obj.getJSONObject("couponInfo");
        JSONArray couponList = couponInfo_list.getJSONArray("couponList");
        String link = couponList.getJSONObject(0).getString("link");
        double discount = couponList.getJSONObject(0).getDoubleValue("discount");
        double quota = couponList.getJSONObject(0).getDoubleValue("quota");

        JSONObject jsonObject2 = obj.getJSONObject("commissionInfo");
        // double commissionShare = jsonObject2.getDoubleValue("commissionShare");
        double commission = jsonObject2.getDoubleValue("commission");

        data.setItemid(skuId);
        data.setItemtitle(skuName);
        data.setItemprice(itemprice);
        data.setItempic(itempic);
        data.setCpmoney(discount);
        data.setCpurl(link);

        data.setTkmoney(commission);
        data.setQuota(quota);

        if (data.getTkprice() < 0)
            return null;

        return data;
    }

}
