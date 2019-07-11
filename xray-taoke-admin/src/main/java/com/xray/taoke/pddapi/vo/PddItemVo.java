package com.xray.taoke.pddapi.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.xray.taoke.jdapi.vo.ConstTkjd;

public class PddItemVo {
    private String itemid;
    private String itemtitle;
    // 单独购买
    private double itemprice;
    // 拼团购买价格
    private double togetprice;

    private String itempic;
    private double tkrate;
    private double cpmoney;
    @SuppressWarnings("unused")
    private double jiemoney;

    private double tkmoney;

    public double getTkmoney() {
        return tkmoney;
    }

    public void setTkmoney(double tkmoney) {
        this.tkmoney = tkmoney;
    }

    public double getTogetprice() {
        return togetprice;
    }

    public void setTogetprice(double togetprice) {
        this.togetprice = togetprice;
    }

    public void setJiemoney(double jiemoney) {
        this.jiemoney = jiemoney;
    }

    public double getJiemoney() {
        BigDecimal bg = new BigDecimal(ConstTkjd.getJiemoney(getTkmoney())).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

    public double getTkprice() {
        return itemprice - cpmoney;
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
        // if (!itempic.contains("310x310"))
        // this.itempic += "_310x310.jpg";
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

}
