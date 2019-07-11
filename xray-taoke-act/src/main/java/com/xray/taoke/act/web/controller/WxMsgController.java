package com.xray.taoke.act.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.CustomServiceApi.Articles;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.xray.act.exception.RtException;
import com.xray.act.jfinal.weixin.MsgInterceptor;
import com.xray.act.jfinal.weixin.MsgProvider;
import com.xray.act.util.StringUtil;
import com.xray.taoke.act.common.Constant;
import com.xray.taoke.act.model.CashInfo;
import com.xray.taoke.act.model.ItemDetail;
import com.xray.taoke.act.model.JdOrderInfo;
import com.xray.taoke.act.model.LinkConfig;
import com.xray.taoke.act.model.MpUser;
import com.xray.taoke.act.model.OrderInfo;
import com.xray.taoke.act.model.UoUser;
import com.xray.taoke.act.service.AiService;
import com.xray.taoke.act.service.MpInfoService;
import com.xray.taoke.act.service.WxAccessTokenCache;
import com.xray.taoke.act.utils.ParamUtils;
import com.xray.taoke.act.utils.SkuidUtils;
import com.xray.taoke.jdapi.Jd21dsService;
import com.xray.taoke.jdapi.JdItemService;
import com.xray.taoke.jdapi.vo.JdItemVo;
import com.xray.taoke.tkapi.TbItemService;
import com.xray.taoke.tkapi.Tk21dsService;
import com.xray.taoke.tkapi.vo.TbItemEx;
import com.xray.taoke.tkapi.vo.TbItemGy;
import com.xray.taoke.tkapi.vo.TbItemVo;

@ControllerBind(controllerKey = "/wxmsg")
public class WxMsgController extends MsgControllerAdapter implements MsgProvider {
	protected static Logger logger = LoggerFactory.getLogger(WxMsgController.class);
	private static WxAccessTokenCache accessTokenCache = new WxAccessTokenCache();

	@Before(MsgInterceptor.class)
	public void index() {
		super.index();
	}

	@Override
	public ApiConfig getApiConfig() {
		String appid = getPara();
		Map<String, String> cond = MpInfoService.instance.getMpInfo(appid);
		ApiConfig ac = new ApiConfig();
		ac.setAppId(appid);
		ac.setAppSecret(cond.get("appsecret"));
		ac.setToken(cond.get("token"));
		ac.setEncodingAesKey(cond.get("encodingaeskey"));
		ApiConfigKit.putApiConfig(ac);
		ApiConfigKit.setThreadLocalAppId(appid);
		ApiConfigKit.setAccessTokenCache(accessTokenCache);
		return ac;
	}

	@Override
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		String appid = getPara();

		String openid = inFollowEvent.getFromUserName();
		renderDefault();
		// 取关
		if (inFollowEvent.getEvent().equals(InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE)) {
			logger.info("unsubscribe,appid:{},openid:{}", appid, openid);
			MpInfoService.instance.negative(appid, openid);
			MpUser.dao.updateInfollowByOpenid(appid, openid);
			return;
		}

		MpInfoService.instance.active(appid, openid);
		MpUser.dao.addByOpenid(appid, openid);
		// 客服
		this.doKefu(appid, openid);
		logger.info("inFollowEvent,appid:{},openid:{}", appid, openid);
	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		String appid = getPara();
		String openid = inQrCodeEvent.getFromUserName();
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent())) {
			logger.info("扫码未关注：" + inQrCodeEvent.getFromUserName());
			if (StringUtil.isNotEmpty(inQrCodeEvent.getEventKey())) {
				String code = inQrCodeEvent.getEventKey().replaceAll("qrscene_", "");
				this.doProxy(appid, code, openid);

				MpInfoService.instance.active(appid, openid);
				MpUser.dao.addByOpenid(appid, openid);
				// 客服
				this.doKefu(appid, openid);
				logger.info("QrCode inFollowEvent,appid:{},openid:{}", appid, openid);

			}

		}
	}

	@Override
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		String appid = getPara();
		String openid = inMenuEvent.getFromUserName();
		renderDefault();
		// 链接不处理
		if (!"CLICK".equals(inMenuEvent.getEvent())) {
			return;
		}
		MpInfoService.instance.active(appid, openid);

		if ("wx_9eefb7335988c499".equals(inMenuEvent.getEventKey())) {
			doUserInfo(inMenuEvent);
			return;
		}
		if ("wx_29439fb58e4bbe22".equals(inMenuEvent.getEventKey())) {
			doCashInfo(inMenuEvent);
			return;
		}
		if ("wx_1927feb4c2a6ac4a".equals(inMenuEvent.getEventKey())) {
			doWeburl(inMenuEvent, appid, openid, "今日爆款，限时抢券", 401);
			return;
		}
		if ("wx_88089a486b11a542".equals(inMenuEvent.getEventKey())) {
			doWeburl(inMenuEvent, appid, openid, "【9.9包邮】有券有返利~", 402);
			return;
		}
		if ("wx_7ad08046600055c0".equals(inMenuEvent.getEventKey())) {
			doWeburl(inMenuEvent, appid, openid, "【30封顶】好券限时抢~", 403);
			return;
		}
		if ("wx_a3bc832644548599".equals(inMenuEvent.getEventKey())) {
			doWeburl(inMenuEvent, appid, openid, "聚划算爆款券，限时抢~", 404);
			return;
		}
		if (!inMenuEvent.getEventKey().equals("wx_05682dbef5eeb0a8") && inMenuEvent.getEventKey().startsWith("wx_")) {
			String content = MpInfoService.instance.getMaterialContent(appid, inMenuEvent.getEventKey());
			if (StringUtil.isNotEmpty(content)) {
				doWxMixedMsg(inMenuEvent, JSONObject.parseObject(content));
				return;
			}
		}

		// 客服
		this.doKefu(appid, openid);
		logger.info("inMenuEvent,appid:{},openid:{}", appid, openid);
	}

	@Override
	protected void processInImageMsg(InImageMsg inImageMsg) {
		ItemDetail.dao.insertIntoDetail(getPara(), inImageMsg.getFromUserName(), "[图片]");
		super.processInImageMsg(inImageMsg);
	}

	@Override
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		ItemDetail.dao.insertIntoDetail(getPara(), inLinkMsg.getFromUserName(), "[链接]");
		super.processInLinkMsg(inLinkMsg);
	}

	@Override
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		ItemDetail.dao.insertIntoDetail(getPara(), inVideoMsg.getFromUserName(), "[视频]");
		super.processInVideoMsg(inVideoMsg);
	}

	@Override
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		ItemDetail.dao.insertIntoDetail(getPara(), inVoiceMsg.getFromUserName(), "[音频]");
		super.processInVoiceMsg(inVoiceMsg);
	}

	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {
		String appid = getPara();
		String openid = inTextMsg.getFromUserName();
		renderDefault();

		try {
			// 活跃
			MpInfoService.instance.active(appid, openid);

			String text = null;
			String content = inTextMsg.getContent();
			content = content.trim();

			if (!MpInfoService.instance.isKefu(appid, openid))
				ItemDetail.dao.insertIntoDetail(appid, openid, content);

			if (MpInfoService.instance.isKefu(appid, openid)) {
				if (content.startsWith("[链接]")) {
					String[] arr = content.split("\n");
					doCustomNews(inTextMsg, arr[1], "马上点击，即可查看~", Constant.auth_picurl, arr[2]);
					return;
				}
			}

			// 客服功能
			if (content.equals("开客服")) {
				MpInfoService.instance.addKefu(appid, openid);
				CustomServiceApi.sendText(openid, "打开客服模式");
				logger.info("addKefu,appid:{},openid:{}", appid, openid);
				return;
			}
			if (content.equals("关客服")) {
				MpInfoService.instance.delKefu(appid, openid);
				CustomServiceApi.sendText(openid, "关闭客服模式");
				logger.info("delKefu,appid:{},openid:{}", appid, openid);
				return;
			}
			if (content.startsWith("亲，找了")) {
				text = Constant.parseUrl(content);
				if (StringUtil.isNotEmpty(text)) {
					doCustomNews(inTextMsg, "找了几款优惠券，❤亲亲看一下~", "马上点击，即可查看~", Constant.auth_picurl, text);
					return;
				}
			}
			// 客服功能 END

			// 查券
			text = Constant.parseUrl(content);
			if (StringUtil.isNotEmpty(text) && !text.contains("t.cn/")) {
				String itemid = Constant.parseItemid(text);
				if (StringUtil.isNotEmpty(itemid)) {
					CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
					doChaQuanByItemid(inTextMsg, appid, openid, itemid);
					return;
				}
			}
			text = Constant.parseTkpwd(content);
			if (StringUtil.isNotEmpty(text) && !content.contains("jd.com")) {
				CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
				doChaQuanByTkpwd(inTextMsg, appid, openid, text);
				return;
			}
			text = Constant.parseUrl(content);
			if (StringUtil.isNotEmpty(text) && !text.contains("t.cn/")) {
				String itemid = Tk21dsService.instance.getitemidByHtml(text);
				if (StringUtil.isNotEmpty(itemid)) {
					CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
					doChaQuanByItemid(inTextMsg, appid, openid, itemid);
					return;
				}
			}
			// 查券 END

			// 京东查券开始
			if (StringUtil.isNotEmpty(text) && content.contains("item.jd.com")) {
				String skuid = Jd21dsService.instance.getitemidByHtml(text);
				if (StringUtil.isNotEmpty(skuid)) {
					CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
					doChaQuanByJdSkuid(inTextMsg, appid, openid, skuid);
					return;
				}
			}

			if (StringUtil.isNotEmpty(text) && content.contains("wqitem.jd.com")) {
				String skuid = ParamUtils.getParam(content, "sku");
				if (StringUtil.isNotEmpty(skuid)) {
					CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
					doChaQuanByJdSkuid(inTextMsg, appid, openid, skuid);
					return;
				}
			}

			if (StringUtil.isNotEmpty(text) && content.contains("u.jd.com")) {
				String skuid = SkuidUtils.getSkuId(text);
				if (StringUtil.isNotEmpty(skuid)) {
					CustomServiceApi.sendText(openid, MpInfoService.instance.getChaquanStart(appid));
					doChaQuanByJdSkuid(inTextMsg, appid, openid, skuid);
					return;
				}
			}

			// 查券京东 END

			// 搜券
			if (content.startsWith("买") || content.startsWith("找") || content.startsWith("搜")) {
				doSouQuan(inTextMsg, content.substring(1));
				return;
			}

			// 淘宝订单
			text = Constant.parseTradeid(content);
			if (StringUtil.isNotEmpty(text)) {
				// 淘宝
				doOrderInfo(inTextMsg, text);
				// 京东
				// doJdOrderInfo(inTextMsg, text);
				return;
			}

			// 提现
			text = Constant.toMediaid(content);
			if ("wx_29439fb58e4bbe22".equals(text)) {
				doCashInfo(inTextMsg);
				return;
			}
			// 查询
			if ("wx_9eefb7335988c499".equals(text)) {
				doUserInfo(inTextMsg);
				return;
			}
			// 指令
			if ("0".equals(content)) {
				MpInfoService.instance.addBlackJx(appid, openid);
				CustomServiceApi.sendText(openid, MpInfoService.instance.getCancelJx(appid));
				return;
			}
			if (content.matches("\\d")) {
				MpInfoService.instance.delBlackJx(appid, openid);
				MpInfoService.instance.addWhiteJx(appid, openid);
				// 精选
				text = MpInfoService.instance.getJxContent(appid);
				if (StringUtil.isNotEmpty(text)) {
					doJingxuan(inTextMsg, text);
					return;
				}
			}
			// 推广
			if ("wx_fa3352ed2c26ed60".equals(text)) {
				doSendProxy(inTextMsg, appid, openid);
				return;
			}

			// 标题
			if (content.length() > 10) {
				CustomServiceApi.sendText(openid, MpInfoService.instance.getTitleContent(appid));
				return;
			}

			text = Constant.toMediaid(content);
			text = MpInfoService.instance.getMaterialContent(appid, text);
			if (StringUtil.isNotEmpty(text)) {
				doWxMixedMsg(inTextMsg, JSONObject.parseObject(text));
				return;
			}

			// 默认搜券
			doSouQuan(inTextMsg, content);
		} catch (Exception e) {
			logger.error("err intextmsg", e);
			if (e instanceof RtException) {
				CustomServiceApi.sendText(openid, MpInfoService.instance.getNoTaokeContent(appid));
				return;
			}
			CustomServiceApi.sendText(openid, MpInfoService.instance.getNetErrContent(appid));
		}

	}

	private void doChaQuanByItemid(InMsg inMsg, String appid, String openid, String itemid) {
		TbItemVo vdata = null;
		TbItemGy gdata = null;
		if (MpInfoService.instance.isKefu(appid, openid)) {
			vdata = TbItemService.instance.getItemVo(appid, itemid);
			gdata = TbItemService.instance.getItemGy(appid, itemid, null);
		} else {
			vdata = TbItemService.instance.getItemVo(appid, itemid);
			gdata = TbItemService.instance.getItemGy(appid, itemid, openid);
		}

		int result = doProxyAll(inMsg, vdata, gdata);
		if (result == 1)
			return;

		doChaQuan(inMsg, vdata, gdata);
	}

	@Deprecated
	protected void doChaQuanByHtml(InMsg inMsg, String appid, String openid, String tburl) {
		TbItemVo vdata = null;
		TbItemGy gdata = null;
		if (MpInfoService.instance.isKefu(appid, openid)) {
			vdata = TbItemService.instance.getItemVoByHtml(appid, tburl);
			gdata = TbItemService.instance.getItemGy(appid, vdata.getItemid(), null);
		} else {
			vdata = TbItemService.instance.getItemVoByHtml(appid, tburl);
			gdata = TbItemService.instance.getItemGy(appid, vdata.getItemid(), openid);
		}

		int result = doProxyAll(inMsg, vdata, gdata);
		if (result == 1)
			return;

		doChaQuan(inMsg, vdata, gdata);
	}

	private void doChaQuanByTkpwd(InMsg inMsg, String appid, String openid, String tkpwd) {
		TbItemVo vdata = null;
		TbItemGy gdata = null;
		if (MpInfoService.instance.isKefu(appid, openid)) {
			gdata = TbItemService.instance.getItemGyByTkpwd(appid, tkpwd, null);
			vdata = TbItemService.instance.getItemVo(appid, gdata.getItemid());
		} else {
			gdata = TbItemService.instance.getItemGyByTkpwd(appid, tkpwd, openid);
			vdata = TbItemService.instance.getItemVo(appid, gdata.getItemid());
		}

		int result = doProxyAll(inMsg, vdata, gdata);
		if (result == 1)
			return;

		doChaQuan(inMsg, vdata, gdata);
	}

	private int doProxyAll(InMsg inMsg, TbItemVo vdata, TbItemGy gdata) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		// 绑定
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid)) {
			return 0;
		}

		UoUser uoUser = UoUser.dao.queryByUserId(userid);
		int isproxyall = uoUser.getInt("isproxyall");

		if (isproxyall == 0)
			return 0;

		long proxypkey = uoUser.getLong("proxypkey");
		LinkConfig conf = LinkConfig.dao.findById(proxypkey);
		if (conf == null) {
			return 0;
		}

		int sid = conf.getInt("sid");
		String domain = MpInfoService.instance.getUrl(appid);
		domain = domain + "/titem?sid=" + sid + "&pkey=" + proxypkey + "&tid=" + gdata.getItemid();
		logger.info("doProxyAll,domain:{},appid:{},openid:{}", domain, appid, openid);
		TbItemEx data = new TbItemEx().setVdata(vdata).setGdata(gdata);
		doCustomNews(inMsg, data.getChaTitle(), data.getChaDesc(), data.getChaPicurl(), domain);

		return 1;
	}

	private void doChaQuan(InMsg inMsg, TbItemVo vdata, TbItemGy gdata) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		MpInfoService.instance.incrChaquan(appid, openid);

		TbItemEx data = new TbItemEx().setVdata(vdata).setGdata(gdata);
		String url = TbItemService.instance.getTobjUrl(appid, data.getEContent());
		// 客服
		if (MpInfoService.instance.isKefu(appid, openid)) {
			doCustomNews(inMsg, data.getChaTitle(), data.getChaDesc(), data.getChaPicurl(), url);
			return;
		}
		// 绑定
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid)) {
			url = AiService.instance.authUrl(appid, openid, url);
		} else {
			data.setUorate(UoUser.dao.queryRateByUserid(userid));
		}

		doCustomNews(inMsg, data.getChaTitle(), data.getChaDesc(), data.getChaPicurl(), url);
		// 查券存库
		ItemDetail.dao.insertInto(appid, openid, vdata.getItemid(), vdata.getItemtitle(),
				(data.getTkprice() + gdata.getCpmoney()), gdata.getCpmoney(), data.getTkprice(), data.getJiemoney());
		logger.info("doChaQuan,appid:{},openid:{}", appid, openid);
	}

	private void doChaQuanByJd(InMsg inMsg, JdItemVo data) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		MpInfoService.instance.incrChaquan(appid, openid);

		// 客服
		@SuppressWarnings("unused")
		String url = data.getCpurl();
		String materialId = "https://item.jd.com/" + data.getItemid() + ".html";
		JSONObject jsonObject = null;
		try {
			jsonObject = Jd21dsService.instance.getitemcpsurl(appid, materialId, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		url = jsonObject.getJSONObject("data").getString("shortURL");
		// 绑定
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid)) {
			url = AiService.instance.authUrl(appid, openid, data.getCpurl());
		} else {
			data.setUorate(UoUser.dao.queryRateByUserid(userid));
		}

		doCustomNews(inMsg, getChaTitle(data), data.getItemtitle(), data.getItempic(), url);
		// 查券存库
		ItemDetail.dao.insertInto(appid, openid, data.getItemid(), data.getItemtitle(),
				(data.getTkprice() + data.getCpmoney()), data.getCpmoney(), data.getTkprice(), data.getJiemoney());

		logger.info("doChaQuanByJd,appid:{},openid:{}", appid, openid);
	}

	private String getChaTitle(JdItemVo data) {
		StringBuilder sb = new StringBuilder();
		if (data.getCpmoney() > 0) {
			sb.append("❤优惠:" + data.getCpmoney() + "\n");
			sb.append("❤付款价:" + (data.getItemprice() - data.getCpmoney()));
		} else {
			sb.append("❤返利:" + data.getJiemoney() + "\n");
			sb.append("❤付款价:" + (data.getItemprice() - data.getCpmoney()));
		}
		return sb.toString();
	}

	private void doSouQuan(InMsg inMsg, String word) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();

		String url = TbItemService.instance.getTsouUrl(appid, word);
		// 客服
		if (MpInfoService.instance.isKefu(appid, openid)) {
			doCustomNews(inMsg, "找到关于【" + word + "】的优惠券", "马上点击，即可查看~", Constant.auth_picurl, url);
			return;
		}
		// 绑定
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid))
			url = AiService.instance.authUrl(appid, openid, url);
		doCustomNews(inMsg, "找到关于【" + word + "】的优惠券", "马上点击，即可查看~", Constant.auth_picurl, url);
		logger.info("doSouQuan,appid:{},openid:{}", appid, openid);
	}

	private void doOrderParentInfo(InMsg inMsg, String parentid, String appid, String openid, String userid) {
		List<OrderInfo> list = OrderInfo.dao.queryByParentTradeid(parentid);
		if (list.size() == 0) {
			logger.info("doOrderParentInfo,no trade,appid:{},openid:{},parentid:{}", appid, openid, parentid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeNotExist(appid));
			return;
		}

		UoUser user = null;
		int verno = 0;
		double trademoney = 0;
		double jierate = 0;
		double jiemoney = 0;
		String tradeid = null;
		String order_userid = null;
		int succ = 0;
		int bind = 0;
		double dividerate = 0;

		for (OrderInfo data : list) {
			user = UoUser.dao.queryByUserId(userid);
			verno = user.getInt("verno");

			order_userid = data.getStr("userid");
			tradeid = data.getStr("tradeid");

			if (StringUtil.isEmpty(order_userid)) {
				trademoney = data.getDouble("trademoney");
				jierate = TbItemService.instance.getFlrate(trademoney);
				jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;

				dividerate = data.getDouble("dividerate");
				if (dividerate > 0)
					jiemoney = trademoney * jierate * dividerate;

				if (OrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno) > 0) {
					logger.info("doOrderParentInfo,succ,appid:{},openid:{},tradeid:{},parentid:{}", appid, openid,
							tradeid, parentid);
					UoUser.dao.updateSucc(userid, jiemoney, verno);
					++succ;
					continue;
				}
			}

			if (userid.equals(order_userid)) {
				++bind;
				continue;
			}
		}

		if (succ > 0) {
			logger.info("doOrderParentInfo,succ,appid:{},openid:{},parentid:{}", appid, openid, parentid);
			CustomServiceApi.sendText(openid,
					MpInfoService.instance.getTradeParentSucc(appid, UoUser.dao.queryByUserId(userid)));
			return;
		}

		if (bind > 0) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid));
			return;
		}

		logger.error("doOrderParentInfo,NOT YOURS,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
		CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
	}

	private void doOrderInfo(InMsg inMsg, String tradeid) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		// 账号未绑定
		String userid = this.checkBind(inMsg, appid, openid);
		if (StringUtil.isEmpty(userid))
			return;

		// 订单不存在
		OrderInfo data = OrderInfo.dao.queryByTradeidInuse(tradeid);
		if (data == null) {
			doOrderParentInfo(inMsg, tradeid, appid, openid, userid);
			return;
		}
		// 订单状态
		if (data.getInt("tkstatus") == Constant.order_fail) {
			logger.info("doOrderInfo,err tkstatus,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
			return;
		}
		// 可以绑定
		String order_userid = data.getStr("userid");
		if (StringUtil.isEmpty(order_userid)) {
			UoUser user = UoUser.dao.queryByUserId(userid);
			int verno = user.getInt("verno");

			double trademoney = data.getDouble("trademoney");
			double jierate = TbItemService.instance.getFlrate(trademoney);
			jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;
			double jiemoney = trademoney * jierate;

			double dividerate = data.getDouble("dividerate");
			if (dividerate > 0)
				jiemoney = trademoney * jierate * dividerate;

			String proxy = user.getStr("proxyby");
			int proxystate = -1;
			if (StringUtil.isNotEmpty(proxy)) {
				proxystate = 1;
			}

			if (OrderInfo.dao.updateSuccNew(appid, userid, tradeid, jiemoney, jierate, verno, proxystate) > 0) {
				logger.info("doOrderInfo,succ,appid:{},openid:{},tradeid:{},proxyOrder:{}", appid, openid, tradeid,
						proxystate);
				UoUser.dao.updateSucc(userid, jiemoney, verno);
				CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid,
						UoUser.dao.queryByUserId(userid), OrderInfo.dao.queryByTradeidInuse(tradeid)));
				return;
			}
		}
		// 已经绑定你
		if (userid.equals(order_userid)) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid));
			return;
		}
		// 别人家的订单
		logger.error("doOrderInfo,NOT YOURS,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
		CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
	}

	private void doJdOrderInfo(InMsg inMsg, String tradeid) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		// 账号未绑定
		String userid = this.checkBind(inMsg, appid, openid);
		if (StringUtil.isEmpty(userid))
			return;

		// 订单不存在
		JdOrderInfo data = JdOrderInfo.dao.queryByTradeid(tradeid);
		if (data == null) {
			doJdOrderParentInfo(inMsg, tradeid, appid, openid, userid);
			return;
		}
		// 订单状态
		// 失效订单
		if (data.getInt("tkstatus") <= Constant.jdorder_fail) {
			logger.info("doJdOrderInfo,err tkstatus,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
			return;
		}
		// 可以绑定
		String order_userid = data.getStr("userid");
		if (StringUtil.isEmpty(order_userid)) {
			UoUser user = UoUser.dao.queryByUserId(userid);
			int verno = user.getInt("verno");

			double trademoney = data.getDouble("trademoney");
			double jierate = TbItemService.instance.getFlrate(trademoney);
			jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;
			double jiemoney = trademoney * jierate;

			// double dividerate = data.getDouble("dividerate");
			// if (dividerate > 0)
			jiemoney = trademoney * jierate;

			if (JdOrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno) > 0) {
				logger.info("doOrderInfo,succ,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
				UoUser.dao.updateSucc(userid, jiemoney, verno);
				CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid,
						UoUser.dao.queryByUserId(userid), JdOrderInfo.dao.queryByTradeid(tradeid)));
				return;
			}
		}
		// 已经绑定你
		if (userid.equals(order_userid)) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid));
			return;
		}
		// 别人家的订单
		logger.error("doOrderInfo,NOT YOURS,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
		CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
	}

	private void doJdOrderParentInfo(InMsg inMsg, String parentid, String appid, String openid, String userid) {
		List<JdOrderInfo> list = JdOrderInfo.dao.queryByParentTradeid(parentid);
		if (list.size() == 0) {
			logger.info("doJdOrderParentInfo,no trade,appid:{},openid:{},parentid:{}", appid, openid, parentid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeNotExist(appid));
			return;
		}

		UoUser user = null;
		int verno = 0;
		double trademoney = 0;
		double jierate = 0;
		double jiemoney = 0;
		String tradeid = null;
		String order_userid = null;
		int succ = 0;
		int bind = 0;
		double dividerate = 0;

		for (JdOrderInfo data : list) {
			user = UoUser.dao.queryByUserId(userid);
			verno = user.getInt("verno");

			order_userid = data.getStr("userid");
			tradeid = data.getStr("tradeid");

			if (StringUtil.isEmpty(order_userid)) {
				trademoney = data.getDouble("trademoney");
				jierate = TbItemService.instance.getFlrate(trademoney);
				jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;

				// dividerate = data.getDouble("dividerate");
				// if (dividerate > 0)
				jiemoney = trademoney * jierate * dividerate;

				if (JdOrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno) > 0) {
					logger.info("doOrderParentInfo,succ,appid:{},openid:{},tradeid:{},parentid:{}", appid, openid,
							tradeid, parentid);
					UoUser.dao.updateSucc(userid, jiemoney, verno);
					++succ;
					continue;
				}
			}

			if (userid.equals(order_userid)) {
				++bind;
				continue;
			}
		}

		if (succ > 0) {
			logger.info("doJdOrderParentInfo,succ,appid:{},openid:{},parentid:{}", appid, openid, parentid);
			CustomServiceApi.sendText(openid,
					MpInfoService.instance.getTradeParentSucc(appid, UoUser.dao.queryByUserId(userid)));
			return;
		}

		if (bind > 0) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeSucc(appid));
			return;
		}

		logger.error("doJdOrderParentInfo,NOT YOURS,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
		CustomServiceApi.sendText(openid, MpInfoService.instance.getTradeFail(appid));
	}

	private void doCashInfo(InMsg inMsg) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		// 账号未绑定
		String userid = this.checkBind(inMsg, appid, openid);
		if (StringUtil.isEmpty(userid))
			return;

		UoUser user = UoUser.dao.queryByUserId(userid);
		// 提现中
		if (user.getInt("cashstate") == 1) {
			logger.error("doCashInfo,in cashing,appid:{},openid:{}", appid, openid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getCashing(appid));
			return;
		}
		// 没钱提
		if (user.getDouble("qbcash") < 0.3) {
			logger.error("doCashInfo,no money,appid:{},openid:{}", appid, openid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getCashNoMoney(appid));
			return;
		}
		// 申请提现
		int verno = user.getInt("verno");
		double money = user.getDouble("qbcash");

		// 提现加上代理的金额
		double qbproxycash = user.getDouble("qbproxycash");
		money = money + qbproxycash;

		long cashtime = System.currentTimeMillis();
		if (UoUser.dao.updateCashtime(userid, money, verno, cashtime) > 0) {
			CashInfo info = new CashInfo();
			info.set("appid", appid);
			info.set("userid", userid);
			info.set("openid", openid);
			info.set("cashtime", cashtime);
			info.set("cashnum", money);
			info.set("verno", verno);
			info.save();
			logger.info("doCashInfo,succ,userid:{},openid:{},verno:{}", userid, openid, verno);
			CustomServiceApi.sendText(openid,
					MpInfoService.instance.getCashSucc(appid, UoUser.dao.queryByUserId(userid)));
		}
	}

	private void doJingxuan(InMsg inMsg, String text) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		logger.info("doJingxuan,appid:{},openid:{}", appid, openid);

		JSONObject obj = JSONObject.parseObject(text);
		if ("wx_mixed".equals(obj.getString("type"))) {
			obj = obj.getJSONArray("data").getJSONObject(0);
			doCustomMsg(openid, obj);
			return;
		}

		obj = obj.getJSONArray("data").getJSONObject(0);
		String url = obj.getString("url");
		// 客服
		if (MpInfoService.instance.isKefu(appid, openid)) {
			doCustomNews(inMsg, obj.getString("title"), obj.getString("description"), obj.getString("picUrl"), url);
			return;
		}

		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid)) {
			url = AiService.instance.authUrl(appid, openid, url);
		}
		doCustomNews(inMsg, obj.getString("title"), obj.getString("description"), obj.getString("picUrl"), url);
	}

	@SuppressWarnings("unused")
	private void doDingyue(InMsg inMsg) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		logger.info("doDingyue,appid:{},openid:{}", appid, openid);
		CustomServiceApi.sendText(openid, "收到~[玫瑰]券后【9.9包邮】推荐给亲亲~");

		this.doWeburl(inMsg, appid, openid, "券后【9.9包邮】还有返利哦~", 402);
	}

	private void doWeburl(InMsg inMsg, String appid, String openid, String title, int lid) {
		String url = MpInfoService.instance.getShopUrl(appid) + "&lid=" + lid;
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid)) {
			url = AiService.instance.authUrl(appid, openid, url);
		}
		doCustomNews(inMsg, title, "马上点击查看~", Constant.auth_picurl, url);
	}

	private void doUserInfo(InMsg inMsg) {
		String appid = getPara();
		String openid = inMsg.getFromUserName();
		try {
			// 账号未绑定
			String userid = this.checkBind(inMsg, appid, openid);
			if (StringUtil.isEmpty(userid)) {
				logger.info("doUserInfo,fail checkBind,appid:{},openid:{}", appid, openid);
				return;
			}
			UoUser user = UoUser.dao.queryByUserId(userid);

			// 会员信息
			logger.info("doUserInfo,succ,appid:{},openid:{}", appid, openid);
			CustomServiceApi.sendText(openid, MpInfoService.instance.getUserInfo(appid, user));
		} catch (Exception e) {
			logger.error("err doUserInfo", e);
			doErrMsg(inMsg, e);
		}
	}

	private String checkBind(InMsg inMsg, String appid, String openid) {
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isNotEmpty(userid))
			return userid;
		logger.info("invalid checkBind,appid:{},openid:{}", appid, openid);
		String url = TbItemService.instance.getBindsuccUrl(appid);
		url = AiService.instance.authUrl(appid, openid, url);
		doCustomNews(inMsg, "同步数据", "马上点击开始同步", Constant.auth_picurl, url);
		return null;
	}

	private void doKefu(String appid, String openid) {
		String content = MpInfoService.instance.getKefuContent(appid);
		if (StringUtil.isEmpty(content))
			return;

		String kefuid = MpUser.dao.queryKefuidByOpenid(appid, openid);
		if (StringUtil.isNotEmpty(kefuid)) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getFollowContent(appid));
			CustomServiceApi.sendImage(openid, kefuid);
		} else {
			JSONObject obj = null;
			JSONArray arr = JSONObject.parseObject(content).getJSONArray("data");
			if (arr.size() > 1) {
				CustomServiceApi.sendText(openid, MpInfoService.instance.getFollowContent(appid));
				obj = arr.getJSONObject((int) (Math.random() * arr.size()));
				kefuid = obj.getString("mediaid");
				MpUser.dao.updateKefuidByOpenid(appid, openid, kefuid);
				CustomServiceApi.sendImage(openid, kefuid);
			} else {
				obj = arr.getJSONObject(0);
				this.doCustomMsg(openid, obj);
			}
		}
		logger.info("doKefu,appid:{},openid:{}", appid, openid);
	}

	private void doErrMsg(InMsg inMsg, Exception e) {
		String appid = getPara();
		if (e instanceof RtException) {
			RtException rte = (RtException) e;
			if (rte.getCode() == Constant.parse_err) {
				CustomServiceApi.sendText(inMsg.getFromUserName(), MpInfoService.instance.getParseErrContent(appid));
				return;
			} else if (rte.getCode() == Constant.notaoke_err) {
				CustomServiceApi.sendText(inMsg.getFromUserName(), MpInfoService.instance.getNoTaokeContent(appid));
				return;
			}
		}
		CustomServiceApi.sendText(inMsg.getFromUserName(), MpInfoService.instance.getNetErrContent(appid));
	}

	private void doProxy(String appid, String proxycode, String openid) {
		if (StringUtil.isEmpty(proxycode))
			return;
		String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isNotEmpty(userid))
			return;
		logger.info("doProxy,proxycode:{},appid:{},openid:{}", proxycode, appid, openid);
		MpInfoService.instance.addProxycode(appid, openid, proxycode);
	}

	private void doSendProxy(InMsg inMsg, String appid, String openid) {
		// 账号未绑定
		String userid = this.checkBind(inMsg, appid, openid);
		if (StringUtil.isEmpty(userid)) {
			logger.info("doSendProxy,fail checkBind,appid:{},openid:{}", appid, openid);
			return;
		}
		UoUser user = UoUser.dao.queryByUserId(userid);
		if (user.getInt("proxystate") == Constant.proxystate_inuse) {
			CustomServiceApi.sendImage(openid, user.getStr("proxyqrid"));
			return;
		}

		CustomServiceApi.sendText(openid, MpInfoService.instance.getProxySucc(appid));
		UoUser.dao.updateProxyState(userid);
	}

	// 根据skuid查询商品优惠信息
	private void doChaQuanByJdSkuid(InMsg inMsg, String appid, String openid, String skuid) {
		JdItemVo data = JdItemService.instance.getItemVo(appid, skuid, openid);
		if (data == null) {
			CustomServiceApi.sendText(openid, MpInfoService.instance.getNoTaokeContent(appid));
			return;
		}
		doChaQuanByJd(inMsg, data);
	}

	@Deprecated
	protected boolean doRenderMsg(InMsg inMsg, JSONObject obj) {
		if ("text".equals(obj.getString("type"))) {
			renderOutTextMsg(obj.getString("content"));
			return true;
		}
		if ("image".equals(obj.getString("type"))) {
			OutImageMsg outMsg = new OutImageMsg(inMsg);
			outMsg.setMediaId(obj.getString("content"));
			render(outMsg);
			return true;
		}
		renderDefault();
		return false;
	}

	protected boolean doCustomMsg(String openid, JSONObject obj) {

		logger.info("doCustomMsg,obj:{}", obj);

		if ("text".equals(obj.getString("type"))) {
			CustomServiceApi.sendText(openid, obj.getString("content"));
			return true;
		}
		if ("image".equals(obj.getString("type"))) {
			CustomServiceApi.sendImage(openid, obj.getString("mediaid"));
			return true;
		}

		if (StringUtil.isNotEmpty(obj.getString("title"))) {
			doCustomNewsByOpenid(openid, obj.getString("title"), obj.getString("description"), obj.getString("picUrl"),
					obj.getString("url"));
			return true;
		}

		return false;
	}

	protected boolean doWxMixedMsg(InMsg inMsg, JSONObject obj) {
		if ("wx_mixed".equals(obj.getString("type"))) {
			JSONArray arr = obj.getJSONArray("data");
			for (int i = 0; i < arr.size(); i++) {
				doCustomMsg(inMsg.getFromUserName(), arr.getJSONObject(i));
			}
			return true;
		}
		if ("wx_news".equals(obj.getString("type"))) {
			JSONArray arr = obj.getJSONArray("data");
			obj = arr.getJSONObject(0);
			String url = obj.getString("url");

			String appid = getPara();
			String openid = inMsg.getFromUserName();
			String userid = MpUser.dao.queryUseridByOpenid(appid, openid);
			if (StringUtil.isEmpty(userid)) {
				url = AiService.instance.authUrl(appid, openid, url);
			}
			doCustomNews(inMsg, obj.getString("title"), obj.getString("description"), obj.getString("picUrl"), url);
		}
		return false;
	}

	protected boolean doCustomNews(InMsg inMsg, String title, String desc, String picurl, String url) {
		Articles item = new Articles();
		item.setTitle(title);
		item.setPicurl(picurl);
		item.setDescription(desc);
		item.setUrl(url);

		List<Articles> list = new ArrayList<Articles>();
		list.add(item);
		ApiResult res = CustomServiceApi.sendNews(inMsg.getFromUserName(), list);
		if (!res.isSucceed()) {
			logger.error("err sendNews,doChaQuan,inMsg:{}, errMsg:{},{}", inMsg.toString(), res.getErrorCode(),
					res.getErrorMsg());
			return false;
		}
		return true;
	}

	protected boolean doCustomNewsByOpenid(String openid, String title, String desc, String picurl, String url) {
		Articles item = new Articles();
		item.setTitle(title);
		item.setPicurl(picurl);
		item.setDescription(desc);
		item.setUrl(url);

		List<Articles> list = new ArrayList<Articles>();
		list.add(item);
		ApiResult res = CustomServiceApi.sendNews(openid, list);
		if (!res.isSucceed()) {
			logger.error("err sendNews,doChaQuan,inMsg:{}, errMsg:{},{}", openid, res.getErrorCode(),
					res.getErrorMsg());
			return false;
		}
		return true;
	}

}