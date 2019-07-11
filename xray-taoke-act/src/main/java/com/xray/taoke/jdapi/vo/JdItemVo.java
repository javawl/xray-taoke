package com.xray.taoke.jdapi.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class JdItemVo {
	private String itemid;
	private String itemtitle;
	private double itemprice;
	private String itempic;
	private double tkrate;
	private double cpmoney;
	private String cpurl;
	private double uorate;

	private double tkmoney;

	@SuppressWarnings("unused")
	private double jiemoney;

	private double quota;

	private int sales;

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public double getQuota() {
		return quota;
	}

	public void setQuota(double quota) {
		this.quota = quota;
	}

	public double getJiemoney() {
		BigDecimal bg = new BigDecimal(ConstTkjd.getJiemoney(getTkmoney())).setScale(2, RoundingMode.UP);
		return bg.doubleValue();
	}

	public void setJiemoney(double jiemoney) {
		this.jiemoney = jiemoney;
	}

	public String getCpurl() {
		return cpurl;
	}

	public void setCpurl(String cpurl) {
		this.cpurl = cpurl;
	}

	public double getTkmoney() {
		return tkmoney;
	}

	public void setTkmoney(double tkmoney) {
		this.tkmoney = tkmoney;
	}

	public double getUorate() {
		return uorate;
	}

	public void setUorate(double uorate) {
		this.uorate = uorate;
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
