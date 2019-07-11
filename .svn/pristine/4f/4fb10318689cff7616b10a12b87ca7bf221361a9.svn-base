package com.xray.taoke.tkapi.vo;

import com.xray.act.util.StringUtil;

public class TbItemVo {
	private String itemid;
	private String itemtitle;
	private double itemprice;
	private String itempic;
	private int itemsale;
	private int itemsale2;
	private double tkrate;
	private double tkprice;
	private double tkmoney;
	private double cpmoney;
	private int intmall;

	public String getItempicmin() {
		if (StringUtil.isEmpty(itempic))
			return null;
		if (itempic.contains("_310"))
			return itempic;
		if (itempic.contains("haodanku"))
			return itempic + "_310";
		return itempic + "_310x310.jpg";
	}

	public double getTkprice() {
		if (tkprice > 0)
			return tkprice;
		tkprice = itemprice - cpmoney;
		return tkprice;
	}

	public double getJiemoney() {
		return ConstTk.getJiemoney(getTkprice() * tkrate);
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

	public int getItemsale() {
		return itemsale;
	}

	public void setItemsale(int itemsale) {
		this.itemsale = itemsale;
	}

	public int getItemsale2() {
		return itemsale2;
	}

	public void setItemsale2(int itemsale2) {
		this.itemsale2 = itemsale2;
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

	public int getIntmall() {
		return intmall;
	}

	public void setIntmall(int intmall) {
		this.intmall = intmall;
	}

	public double getTkmoney() {
		return tkmoney;
	}

	public void setTkmoney(double tkmoney) {
		this.tkmoney = tkmoney;
	}

	public void setTkprice(double tkprice) {
		this.tkprice = tkprice;
	}

}
