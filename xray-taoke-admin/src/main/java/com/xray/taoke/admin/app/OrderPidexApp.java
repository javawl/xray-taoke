package com.xray.taoke.admin.app;

import java.util.Calendar;
import java.util.List;

import com.xray.act.exception.RtException;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.MpInfo;
import com.xray.taoke.admin.model.MpUser;
import com.xray.taoke.admin.model.OrderInfo;
import com.xray.taoke.admin.model.UoUser;
import com.xray.taoke.admin.service.MpInfoService;
import com.xray.taoke.admin.service.UoUserService;
import com.xray.taoke.tkapi.TbItemService;

public class OrderPidexApp extends AbstractApp {

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			logger.error("errapp pidexorder", e);
		}
	}

	public void doRun() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);

		String itemid = null;
		String adzoneid = null;
		String value = null;
		List<OrderInfo> list = OrderInfo.dao.queryLatestTradetime(cal.getTimeInMillis());
		logger.info("runapp pidexorder,size:{}", list.size());
		int result = 0;
		for (OrderInfo data : list) {
			itemid = data.getStr("itemid");
			adzoneid = data.getStr("adzoneid");
			result = updateProxyJie(adzoneid, data);
			if (result == 1)
				continue;

			value = TbItemService.instance.getPidex(itemid, adzoneid);
			if (StringUtil.isEmpty(value))
				continue;

			String[] values = value.split(",");
			updateJie(values[0], values[1], data);
			TbItemService.instance.delPidex(itemid, adzoneid);
		}
	}

	private void updateJie(String appid, String openid, OrderInfo data) {
		String userid = MpInfo.dao.queryUseridByOpenid(appid, openid);
		if (StringUtil.isEmpty(userid))
			throw new RtException("errapp,none userid,appid:" + appid + ",openid:" + openid);

		UoUser user = UoUser.dao.queryByUserId(userid);

		int verno = user.getInt("verno");

		double trademoney = data.getDouble("trademoney");
		double jierate = TbItemService.instance.getFlrate(trademoney);
		jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;

		double dividerate = data.getDouble("dividerate");
		double jiemoney = 0;
		if (dividerate > 0)
			jiemoney = trademoney * jierate * dividerate;

		String tradeid = data.getStr("tradeid");

		String proxy = user.getStr("proxyby");
		int proxystate = -1;
		if (StringUtil.isNotEmpty(proxy)) {
			proxystate = 1;
		}

		if (OrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno, proxystate) > 0) {
			logger.info("runapp,succ pidexorder,appid:{},openid:{},tradeid:{},proxystate:{}", appid, openid, tradeid,
					proxystate);
			UoUser.dao.updateSucc(userid, jiemoney, verno);
			wxApi.sendText(appid, openid, MpInfoService.instance.getTradeSucc(appid, UoUser.dao.queryByUserId(userid),
					OrderInfo.dao.queryByTradeid(tradeid)));
			return;
		}

	}

	private int updateProxyJie(String adzoneid, OrderInfo data) {
		String userid = UoUserService.instance.getProxy(adzoneid);
		if (StringUtil.isEmpty(userid)) {
			logger.error("errapp,none userid,adzoneid:{}", adzoneid);
			return 0;
		}

		UoUser user = UoUser.dao.queryByUserId(userid);
		int isproxyall = user.getInt("isproxyall");
		if (isproxyall == 0) {
			logger.info("runapp, have userid but isproxyall ==0,adzoneid:{}", adzoneid);
			return 0;
		}

		String appid = user.getStr("bindappid");

		MpUser mpUser = MpUser.dao.queryByUserid(appid, userid);
		String openid = mpUser.getStr("openid");
		int verno = user.getInt("verno");

		double trademoney = data.getDouble("trademoney");
		double jierate = TbItemService.instance.getFlrate(trademoney);
		jierate = user.getDouble("rate") > jierate ? user.getDouble("rate") : jierate;

		double dividerate = data.getDouble("dividerate");
		double jiemoney = 0;
		if (dividerate > 0)
			jiemoney = trademoney * jierate * dividerate;

		String tradeid = data.getStr("tradeid");
		if (OrderInfo.dao.updateSucc(appid, userid, tradeid, jiemoney, jierate, verno, -1) > 0) {
			logger.info("runapp,succ proxy pidexorder,appid:{},openid:{},tradeid:{}", appid, openid, tradeid);
			UoUser.dao.updateSucc(userid, jiemoney, verno);
			wxApi.sendText(appid, openid, MpInfoService.instance.getTradeSucc(appid, UoUser.dao.queryByUserId(userid),
					OrderInfo.dao.queryByTradeid(tradeid)));
			return 1;
		}
		return 0;

	}

}
