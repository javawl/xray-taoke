package com.xray.taoke.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.act.web.vo.PairVo;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.app.LinkWareApp;
import com.xray.taoke.admin.app.LinkWareJdApp;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.service.LinkInfoService;
import com.xray.taoke.jdapi.JdItemService;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.tkapi.TbItemService;
import com.xray.taoke.tkapi.vo.TbItemVo;

@ControllerBind(controllerKey = "/linkware")
public class LinkWareController extends AbstractController {
	protected static RedisService cache = RedisServiceFactory.getDefaulInstance();

	public void goList() {
		String pkey = getPara("pkey", LinkInfoService.instance.getPkey(getUserid()));
		setAttr("pkey", pkey);

		List<PairVo<String, String>> dataList = new ArrayList<PairVo<String, String>>();
		dataList.add(new PairVo<String, String>("实时榜", "i00"));
		dataList.add(new PairVo<String, String>("女装", "i01"));
		dataList.add(new PairVo<String, String>("美妆", "i04"));
		dataList.add(new PairVo<String, String>("鞋包", "i06"));
		dataList.add(new PairVo<String, String>("母婴", "i08"));
		dataList.add(new PairVo<String, String>("居家", "i10"));
		dataList.add(new PairVo<String, String>("美食", "i11"));
		dataList.add(new PairVo<String, String>("9.9包邮", "i42"));
		dataList.add(new PairVo<String, String>("30封顶", "i43"));
		dataList.add(new PairVo<String, String>("聚划算", "i44"));
		dataList.add(new PairVo<String, String>("淘抢购", "i45"));
		dataList.add(new PairVo<String, String>("预告单", "i47"));
		dataList.add(new PairVo<String, String>("超大牌", "i48"));
		setAttr("dataList", dataList);

		setAttr("LinkWareApp", cache.get("LinkWareApp"));
		setAttr("configList", LinkInfoService.instance.getLinkConfig());
		render("/template/pages/linkware/list.html");
	}

	public void goItemList() {
		int pno = getParaToInt("pno", 1);
		setAttr("pno", pno);
		String lwid = getPara("lwid");
		setAttr("lwid", lwid);

		List<TbItemVo> dataList1 = new ArrayList<TbItemVo>();
		List<TbItemVo> dataList2 = new ArrayList<TbItemVo>();
		List<TbItemVo> dataList = TbItemService.instance.getItemList(lwid, pno);
		for (int i = 0; i < dataList.size(); i++) {
			if (i % 2 == 0)
				dataList1.add(dataList.get(i));
			else
				dataList2.add(dataList.get(i));
		}
		setAttr("dataList1", dataList1);
		setAttr("dataList2", dataList2);
		render("/template/pages/linkware/item_list.html");
	}

	public void doHdk() {
		new LinkWareApp().run();
		renderRtn(RtnFactory.succ);
	}

	@Clear
	public void doJd() {
		new LinkWareJdApp().run();
		renderRtn(RtnFactory.succ);
	}

	public void goListByJd() {
		String pkey = getPara("pkey", "");
		setAttr("pkey", pkey);

		List<PairVo<String, String>> dataList = new ArrayList<PairVo<String, String>>();
		dataList.add(new PairVo<String, String>("实时榜", "j00"));
		dataList.add(new PairVo<String, String>("食品、酒类", "j01"));
		dataList.add(new PairVo<String, String>("鞋靴、礼品箱包", "j04"));
		dataList.add(new PairVo<String, String>("美妆护肤", "j06"));
		dataList.add(new PairVo<String, String>("母婴、玩具乐器", "j08"));
		dataList.add(new PairVo<String, String>("家具日用", "j10"));
		dataList.add(new PairVo<String, String>("运动户外", "j11"));
		dataList.add(new PairVo<String, String>("数码", "j12"));
		setAttr("dataList", dataList);

		setAttr("LinkWareApp", cache.get("LinkWareJdApp"));
		setAttr("configList", MpInfo.dao.queryAllLine());
		render("/template/pages/linkware/list_jd.html");
	}

	public void goItemListByJd() {
		int pno = getParaToInt("pno", 1);
		setAttr("pno", pno);
		String lwid = getPara("lwid");
		setAttr("lwid", lwid);

		List<JdItemVo> dataList1 = new ArrayList<JdItemVo>();
		List<JdItemVo> dataList2 = new ArrayList<JdItemVo>();
		List<JdItemVo> dataList = JdItemService.instance.getItemList(lwid, pno);
		for (int i = 0; i < dataList.size(); i++) {
			if (i % 2 == 0)
				dataList1.add(dataList.get(i));
			else
				dataList2.add(dataList.get(i));
		}
		setAttr("dataList1", dataList1);
		setAttr("dataList2", dataList2);
		render("/template/pages/linkware/item_list_jd.html");
	}

}
