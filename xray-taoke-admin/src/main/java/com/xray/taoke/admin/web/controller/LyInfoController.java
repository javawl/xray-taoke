package com.xray.taoke.admin.web.controller;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.xray.act.util.DateUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.web.controller.AbstractController;
import com.xray.taoke.admin.model.TkLyInfo;
import com.xray.taoke.admin.service.LvInfoService;
import com.xray.taoke.admin.utils.HttpUtils;
import com.xray.taoke.admin.utils.ParamUtils;
import com.xray.taoke.admin.vo.TkLyInfoVo;

@ControllerBind(controllerKey = "/lvinfo")
public class LyInfoController extends AbstractController {

	public void goList() {
		String day = getPara("day", "");
		setAttr("dayStr", day);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> cond = getCondAll();
		if (StringUtil.isEmpty(cond.get("source")))
			setAttr("source", "");
		else
			setAttr("source", cond.get("source"));
		String begin = null;
		String end = null;
		if (!StringUtil.isEmpty(day)) {
			try {
				begin = DateUtil.getDayBegin(simpleDateFormat.parse(day));
				end = DateUtil.getDayEnd(simpleDateFormat.parse(day));
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cond.put("begin", simpleDateFormat2.parse(begin).getTime());
				cond.put("end", simpleDateFormat2.parse(end).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		setAttr("dataList", TkLyInfo.dao.queryList(cond, getPageVo("seqid desc")));
		render("/template/pages/lyinfo/list.html");
	}

	@Clear
	public void doFaBu() {
		List<TkLyInfoVo> list = TkLyInfo.dao.listVo(TkLyInfo.dao.queryList(), TkLyInfoVo.class);
		LvInfoService.instance.addAll(list);
		renderRtn(RtnFactory.succ);
	}

	@Clear
	public void doPullOff() {

		String day = getPara("day", com.xray.act.util.DateUtil.getToday());
		String url = "https://www.qilvyoo.com/home/prod_log.jsp?d=" + day + "&s=1";
		String cookie = "JSESSIONID=iSETeDIxK1Rh; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5ADO3QzNxESOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZx0zN1AzMwcmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzcwQmJfd3YzVFdp9DZ10DM4UTNmgXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9QnJvJGbfVWa9QyM1Z2cyV2XklXPsFTe4AjMmUXdlNlcu9WYl1ePuiOuUmOkzaiomY2cnljby0UQ3QEMDNkRFJTQxgTQ5EzN4gTQ3EEM2UTQzQTRyYwM==";

		String data = HttpUtils.httpRequest(url, cookie);
		catchLvYouPullOff(data, url, 1);

		url = "https://www.jingchengvip.com/home/prod_log.jsp?d=" + day + "&s=1";

		cookie = "JSESSIONID=gSDD6j4UHH_e; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5EDN3gzNwQSOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZy0jM5UzNxgmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzc4EmJfd3YzVFdp9DZy0TOzcTOmIXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9QnJvJGbfVWa9QyM1Z2cyV2XklWPnxmYyIyN1Z2cyV2Xh5Wb9UK646Z6QSr5iOiJzZWaudUP1MTMCljR5MjRzQjQ0QDR2cDRFlUQGVzMxgDN0ADM4g";
		data = HttpUtils.httpRequest(url, cookie);
		catchLvYouPullOff(data, url, 1);

		url = "http://szqz.honeynice.com/home/prod_log.jsp?d=" + day + "&s=1";

		cookie = "JSESSIONID=fOv0LhLI-YN5; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5ETNwEjM1QSOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZy0DMyIzM0cmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzc4AmJfd3YzVFdp9DZ40DNxgTOmMXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9Q1bCFlcwYld4cDbulja4BDcpdGawMGNLRnY6VSWyZ2blx2XklTPxEnJzVXZfJWa9QGbmdDezEnJzVXZfJmbtFTZo3rrpjJlmD6smInJpN2Z94UM3IENFFjMEVkMFNTNCh0Q3YEMFZDO4IUN5MkR5QER=M";
		data = HttpUtils.httpRequest(url, cookie);
		catchLvYouPullOff(data, url, 1);

		renderRtn(RtnFactory.succ);

	}

	public void doEditTitle() {
		String seqid = getPara("seqid");
		String title = getPara("title");
		TkLyInfo info = TkLyInfo.dao.findById(seqid);
		info.set("title", title);
		info.update();
		renderRtn(RtnFactory.succ);
	}

	public void doEditState() {
		String seqid = getPara("seqid");
		String state = getPara("state");

		TkLyInfo info = TkLyInfo.dao.findById(seqid);
		info.set("state", state);
		info.update();
		renderRtn(RtnFactory.succ);
	}

	public void doEditPullOff() {
		String seqid = getPara("seqid");
		String state = getPara("state");
		TkLyInfo info = TkLyInfo.dao.findById(seqid);
		info.set("state", state);
		info.update();
		renderRtn(RtnFactory.succ);
	}

	@Clear
	public void doCatchByVist() {
		String day = getPara("day", com.xray.act.util.DateUtil.getToday());
		String url = "https://www.qilvyoo.com/home/prod_log.jsp?d=" + day + "&s=0";
		String cookie = "JSESSIONID=iSETeDIxK1Rh; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5ADO3QzNxESOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZx0zN1AzMwcmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzcwQmJfd3YzVFdp9DZ10DM4UTNmgXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9QnJvJGbfVWa9QyM1Z2cyV2XklXPsFTe4AjMmUXdlNlcu9WYl1ePuiOuUmOkzaiomY2cnljby0UQ3QEMDNkRFJTQxgTQ5EzN4gTQ3EEM2UTQzQTRyYwM==";
		String data = HttpUtils.httpRequest(url, cookie);
		catchLvYou(data, url, 0);
		url = "https://www.jingchengvip.com/home/prod_log.jsp?d=" + day + "&s=0";
		cookie = "JSESSIONID=gSDD6j4UHH_e; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5EDN3gzNwQSOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZy0jM5UzNxgmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzc4EmJfd3YzVFdp9DZy0TOzcTOmIXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9QnJvJGbfVWa9QyM1Z2cyV2XklWPnxmYyIyN1Z2cyV2Xh5Wb9UK646Z6QSr5iOiJzZWaudUP1MTMCljR5MjRzQjQ0QDR2cDRFlUQGVzMxgDN0ADM4g";
		data = HttpUtils.httpRequest(url, cookie);
		catchLvYou(data, url, 0);
		url = "http://szqz.honeynice.com/home/prod_log.jsp?d=" + day + "&s=0";
		cookie = "JSESSIONID=fOv0LhLI-YN5; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5ETNwEjM1QSOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZy0DMyIzM0cmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzc4AmJfd3YzVFdp9DZ40DNxgTOmMXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9Q1bCFlcwYld4cDbulja4BDcpdGawMGNLRnY6VSWyZ2blx2XklTPxEnJzVXZfJWa9QGbmdDezEnJzVXZfJmbtFTZo3rrpjJlmD6smInJpN2Z94UM3IENFFjMEVkMFNTNCh0Q3YEMFZDO4IUN5MkR5QER=M";
		data = HttpUtils.httpRequest(url, cookie);
		catchLvYou(data, url, 0);
		renderRtn(RtnFactory.succ);
	}

	private void catchLvYouPullOff(String data, String url, int state) {
		java.net.URL url2 = null;
		try {
			url2 = new java.net.URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String Fs = url2.getHost();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode tagNode = htmlCleaner.clean(data);
		String infoid = null;
		try {
			Object[] aArr = tagNode.evaluateXPath("//table[@class='l_detail']//tr//td//b");
			for (Object object : aArr) {
				TagNode n = (TagNode) object;
				infoid = n.getText().toString();
				TkLyInfo info = TkLyInfo.dao.queryByInfo(infoid, Fs);
				if (info != null) {
					info.set("state", 1);
					info.update();
				}

			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void catchLvYou(String data, String url, int state) {
		java.net.URL url2 = null;
		try {
			url2 = new java.net.URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String Fs = url2.getHost();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode tagNode = htmlCleaner.clean(data);
		String title = null;
		String itemurl = null;
		String infoid = null;
		try {
			Object[] aArr = tagNode.evaluateXPath("//a");
			for (Object object : aArr) {
				TagNode n = (TagNode) object;
				itemurl = n.getAttributes().get("href");
				if (!itemurl.contains("info_id"))
					continue;
				infoid = ParamUtils.getParam(itemurl, "info_id");
				title = n.getText().toString();
				TkLyInfo info = TkLyInfo.dao.queryByInfo(infoid, Fs);
				if (state == 0) {
					if (info == null) {
						TkLyInfo info2 = new TkLyInfo();
						info2.set("url", Fs);
						info2.set("inputtime", System.currentTimeMillis());
						info2.set("infoid", infoid);
						info2.set("title", title);
						info2.set("itemurl", Fs + itemurl);
						info2.set("state", 0);
						info2.save();
					}
				} else {
					if (info != null) {
						info.set("state", 1);
						info.update();
					}
				}

			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
