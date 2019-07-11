package com.xray.taoke.act.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.route.ControllerBind;
import com.xray.act.exception.RtException;
import com.xray.act.jfinal.JfController;
import com.xray.act.util.StringUtil;
import com.xray.act.util.codec.Base64Codec;
import com.xray.act.util.codec.MD5Codec;
import com.xray.act.web.vo.RtnFactory;
import com.xray.taoke.act.common.Constant;
import com.xray.taoke.act.common.TbErrInterceptor;
import com.xray.taoke.act.model.MpInfo;
import com.xray.taoke.act.service.LinkInfoService;
import com.xray.taoke.act.service.LvInfoService;
import com.xray.taoke.act.service.MpInfoService;
import com.xray.taoke.jdapi.Jd21dsService;
import com.xray.taoke.jdapi.JdItemService;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.tkapi.HaodankuService;
import com.xray.taoke.tkapi.TbItemService;
import com.xray.taoke.tkapi.vo.TbItemEx;
import com.xray.taoke.tkapi.vo.TbItemGy;
import com.xray.taoke.tkapi.vo.TbItemVo;
import com.xray.taoke.tkapi.vo.TkLyInfoVo;

@Before({ GET.class, TbErrInterceptor.class })
@ControllerBind(controllerKey = "/")
public class IndexController extends JfController {
	protected static Logger logger = LoggerFactory.getLogger(IndexController.class);

	public void index() {
		String id = getPara();
		if (id.startsWith("i")) {
			setAttr("lwid", id.substring(0, 3));
			setAttr("pkey", id.substring(3));
			this.bshop();
			return;
		}

		if (id.startsWith("j")) {
			setAttr("lwid", id.substring(0, 3));
			setAttr("pkey", id.substring(3));
			this.jshop();
			return;
		}

		String url = LinkInfoService.instance.decodeUrl(getPara());
		if (StringUtil.isEmpty(url))
			throw new RtException(Constant.para_err);
		redirect(url);
	}

	public void bshop() {
		if (!isParaBlank("lwid"))
			setAttr("lwid", getPara("lwid"));
		if (!isParaBlank("pkey"))
			setAttr("pkey", getPara("pkey"));

		if (!isParaBlank("word")) {
			String word = getPara("word");
			setAttr("word", word);

			String lwid = MD5Codec.encode(word).substring(8, 24);
			if (!TbItemService.instance.existsItemList(lwid)) {
				HaodankuService.instance.doAllitemList(lwid, word, 1);
			}
			setAttr("lwid", lwid);
		}

		setAttr("wordList", JSONObject
				.parseArray("[{\"keyword\":\"连衣裙\"},{\"keyword\":\"女鞋\"},{\"keyword\":\"零食\"},{\"keyword\":\"女包\"}]"));
		render("/html/bshop.html");
	}

	public void blist() {
		setAttr("dataList", TbItemService.instance.getItemList(getPara("lwid"), getParaToInt("pno")));
		render("/html/blist.html");
	}

	public void jshop() {
		if (!isParaBlank("lwid"))
			setAttr("lwid", getPara("lwid"));
		if (!isParaBlank("pkey"))
			setAttr("pkey", getPara("pkey"));
		render("/html/jshop.html");
	}

	public void jlist() {
		setAttr("dataList", JdItemService.instance.getItemList(getPara("lwid"), getParaToInt("pno")));
		render("/html/jlist.html");
	}

	public void doToken() {
		TbItemGy data = TbItemService.instance.getItemGyByPkey(getPara("pkey"), getPara("id"));
		if (data == null)
			renderRtn(RtnFactory.fail);
		else
			renderRtn(RtnFactory.newSucc(data.getTkpwd()));
	}

	public void doJToken() {
		String itemid = getPara("id");
		String cpurl = getPara("cpurl");
		String pkey = getPara("pkey");
		MpInfo info = MpInfo.dao.findById(pkey);
		String appid = info.getStr("appid");
		String materialId = "http://item.jd.com/" + itemid + ".html";
		JSONObject jsonObject = null;
		try {
			jsonObject = Jd21dsService.instance.getitemcpsurl(appid, materialId, cpurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String shortUrl = jsonObject.getJSONObject("data").getString("shortURL");
		renderRtn(RtnFactory.newSucc(shortUrl));
	}

	public void tobj() {
		setAttr("obj", JSONObject.parseObject(new String(Base64Codec.decode(getPara("e")))));
		render("/html/tobj.html");
	}

	public void tcode() {
		String sid = getPara("sid");
		String tkpwd = "￥" + getPara("token") + "￥";

		MpInfoService.instance.incrTcode(sid);

		String appid = MpInfoService.instance.getAppid(sid);
		TbItemGy gdata = TbItemService.instance.getItemGyByTkpwd(appid, tkpwd, null);
		TbItemVo vdata = TbItemService.instance.getItemVo(appid, gdata.getItemid());
		setAttr("obj", new TbItemEx().setVdata(vdata).setGdata(gdata));
		setAttr("img", getPara("img"));
		setAttr("pkey", getPara("pkey"));
		setAttr("tkpwd", tkpwd);
		render("/html/tcode.html");
	}

	public void titem() {
		String sid = getPara("sid");
		String tid = getPara("tid");
		String lid = getPara("lid");
		String pkey = getPara("pkey");
		MpInfoService.instance.incrTitem(sid);

		String appid = MpInfoService.instance.getAppid(sid);
		TbItemVo vdata = null;
		if (StringUtil.isEmpty(lid))
			vdata = TbItemService.instance.getItemVo(appid, tid);
		else
			vdata = TbItemService.instance.getItemVo(appid, lid, tid);

		TbItemGy gdata = null;

		if (StringUtil.isNotEmpty(pkey)) {
			gdata = TbItemService.instance.getItemGyByPid(appid, tid, MpInfoService.instance.getPkeyPid(pkey));
		}
		if (gdata == null)
			gdata = TbItemService.instance.getItemGy(appid, tid, null);

		setAttr("obj", new TbItemEx().setVdata(vdata).setGdata(gdata).getObj());
		render("/html/tobj.html");
	}

	public void tshop() {
		MpInfoService.instance.incrTshop(this.sid());
		setAttr("lid", getPara("lid", "301"));
		render("/html/tshop.html");
	}

	public void tlink() {
		MpInfoService.instance.incrTlink(this.sid());
		setAttr("lid", getPara("lid"));
		setAttr("pkey", getPara("pkey"));
		render("/html/tshop.html");
	}

	public void tview() {
		this.sid();
		setAttr("lid", LinkInfoService.instance.getView(getPara("id")));
		setAttr("pkey", getPara("pkey"));
		render("/html/tshop.html");
	}

	public void tsou() {
		MpInfoService.instance.incrTsou(this.sid());
		String keyword = getPara("keyword");
		setAttr("keyword", getPara("keyword"));
		setAttr("lid", HaodankuService.instance.doSupersearch(keyword, 1, 1));
		render("/html/tshop.html");
	}

	public void tlist() {
		int page = getParaToInt("page");
		if (page > 10) {
			renderNull();
			return;
		}
		setAttr("dataList", TbItemService.instance.getItemListVo(getPara("lid"), page));
		render("/html/tlist.html");
	}

	public void tshopimg() {
		this.sid();
		setAttr("lid", getPara("lid"));
		setAttr("pkey", getPara("pkey"));
		render("/html/tshopimg.html");
	}

	public void titemJd() {
		String sid = getPara("sid");
		String tid = getPara("tid");
		MpInfoService.instance.incrTitem(sid);

		String appid = MpInfoService.instance.getAppid(sid);
		JdItemVo jdItemVo = JdItemService.instance.getItemVo(appid, tid);
		String materialId = "https://item.jd.com/" + jdItemVo.getItemid() + ".html";
		JSONObject jsonObject = null;
		try {
			jsonObject = Jd21dsService.instance.getitemcpsurl(appid, materialId, jdItemVo.getCpurl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("titmeJd,jsonObject:{}", jsonObject);
			e.printStackTrace();
		}
		String shortUrl = jsonObject.getJSONObject("data").getString("shortURL");
		if (!StringUtil.isNotEmpty(shortUrl)) {
			renderNull();
			return;
		}

		redirect(shortUrl);
	}

	public void tshopJd() {
		MpInfoService.instance.incrTshop(this.sid());
		setAttr("lid", getPara("lid", "201"));
		render("/html/tshopjd.html");
	}

	public void tlinkJd() {
		MpInfoService.instance.incrTlink(this.sid());
		setAttr("lid", getPara("lid"));
		setAttr("pkey", getPara("pkey"));
		render("/html/tshopjd.html");
	}

	public void tsouJd() {
		MpInfoService.instance.incrTsou(this.sid());
		String keyword = getPara("keyword");
		setAttr("keyword", getPara("keyword"));
		setAttr("lid", HaodankuService.instance.doSupersearch(keyword, 1, 1));
		render("/html/tshopjd.html");
	}

	public void tlistJd() {
		int page = getParaToInt("page");
		if (page > 10) {
			renderNull();
			return;
		}
		List<JdItemVo> dataList = JdItemService.instance.getItemListVo(getPara("lid"), page);

		setAttr("dataList", dataList);
		render("/html/tlistjd.html");
	}

	public void bindsucc() {
		render("/html/bindsucc.html");
	}

	public void lv() {
		render("/html/ly.html");
	}

	public void lvList() {
		int pno = getParaToInt("pno", 1);
		int psize = 10;
		List<TkLyInfoVo> list = LvInfoService.instance.getAll(pno, psize);
		setAttr("list", list);
		render("/html/lylist.html");
	}

	private String sid() {
		String sid = getPara("sid");
		if (StringUtil.isEmpty(sid))
			throw new RtException(Constant.para_err);
		setAttr("sid", sid);
		return sid;
	}

}
