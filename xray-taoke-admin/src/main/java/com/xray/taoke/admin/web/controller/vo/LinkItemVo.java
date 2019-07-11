package com.xray.taoke.admin.web.controller.vo;

import com.alibaba.fastjson.JSONObject;
import com.xray.act.util.StringUtil;

public class LinkItemVo {
    private String itemid;
    private String itemtitle;
    private double itemprice;
    private String itempic;
    private int itemsale;
    private double cpmoney;
    private double tkrate;
    private double tkprice;
    private int state;
    private int type;
    private int sort;
    
    public String getItempicmin() {
        if (StringUtil.isEmpty(itempic))
            return null;
        if (itempic.contains("haodanku")) {
            return itempic + "_310";
        }
        return itempic + "_310x310.jpg";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public double getTkmoney() {
        return tkprice * tkrate;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public double getItemprice() {
        return itemprice;
    }

    public void setItemprice(double itemprice) {
        this.itemprice = itemprice;
    }

    public String getItempic() {
        return itempic;
    }

    public void setItempic(String itempic) {
        this.itempic = itempic;
    }

    public double getTkrate() {
        return tkrate;
    }

    public void setTkrate(double tkrate) {
        this.tkrate = tkrate;
    }

    public double getCpmoney() {
        return cpmoney;
    }

    public void setCpmoney(double cpmoney) {
        this.cpmoney = cpmoney;
    }

    public int getItemsale() {
        return itemsale;
    }

    public void setItemsale(int itemsale) {
        this.itemsale = itemsale;
    }

    public double getTkprice() {
        return tkprice;
    }

    public void setTkprice(double tkprice) {
        this.tkprice = tkprice;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}
