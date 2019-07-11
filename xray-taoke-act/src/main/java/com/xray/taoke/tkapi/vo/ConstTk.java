package com.xray.taoke.tkapi.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.util.HttpUtil;
import com.xray.act.util.StringUtil;
import com.xray.act.util.UuidUtil;
import com.xray.taoke.tkapi.MpInfoService;

public class ConstTk {
	public static final Prop config = PropKit.use("config.properties");

	public static final boolean shorturl_mode = config.getBoolean("shorturl.mode", false);

	public static final int para_err = -100;
	public static final int data_err = -101;
	public static final int notaoke_err = -102;
	public static final int parse_err = -103;

	private static Pattern unicode_pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
	private static final Pattern url_pattern = Pattern.compile("(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))");
	private static final Pattern itemid_pattern = Pattern.compile("[\\?&]id=(\\d+)");
	private static final Pattern tradeid_pattern = Pattern.compile("^(\\d{14,})$");
	private static final Pattern tkpwd_pattern = Pattern.compile("([a-zA-Z0-9]{11})");
	private static final DecimalFormat num_format = new DecimalFormat("0.0#");

	public static void main(String[] args) {
		System.out.println(parseItemid(
				"<p><img src=\"http://oss2.lanlanlife.com/993735f2b861dff83cb2ae806b52d1fc_1050x700.jpg\"><br>【2盒9.9元】葵花医用酒精棉片<br>券后6.8元包邮秒杀<br>3元优惠券:<a target=\"_blank\" href=\"https://uland.taobao.com/quan/detail?sellerId=3855935192&amp;activityId=de3d017d340e476ead2853bcebb258da\">https://uland.taobao.com/quan/detail?sellerId=3855935192&amp;activityId=de3d017d340e476ead2853bcebb258da</a><br>下单链接:<a target=\"_blank\" href=\"https://detail.tmall.com/item.htm?id=590086082864\">https://detail.tmall.com/item.htm?id=590086082864</a><br>【拍2盒9.9元共200片】药店一盒就要19.8元！家里常备，小东西大用途，磕磕碰碰消消毒都好用！用来擦手机屏和眼镜都很好用！独立小包装特别方便，每次用一片~&nbsp;&nbsp;<br></p>"));
		System.out.println(parseItemid(
				"http://item.taobao.com/item.htm?spm=a219t.7900221/10.1998910419.d30ccd691.17a075a52nMQsy&id=10183401544"));
	}

	public static String formatPrice(double d) {
		return num_format.format(d);
	}

	public static String toShortUrl(String url) {
		return toShortUrl(url, false);
	}

	public static String toShortUrl(String url, boolean inCache) {
		try {
			if (inCache)
				return MpInfoService.instance.getShortUrl(url);

			if (!shorturl_mode)
				return url;

			String sendGet = HttpUtil
					.sendGet("http://api.t.sina.com.cn/short_url/shorten.json?source=222700405&url_long="
							+ URLEncoder.encode(url, "UTF-8"));
			JSONArray jsonArray = JSONArray.parseArray(sendGet);
			return JSONObject.parseObject(jsonArray.get(0).toString()).getString("url_short");
		} catch (Exception e) {
		}
		return "";
	}

	public static double getFlrate(double tkmoney) {
		return 0.55;
		// if (tkmoney < 1)
		// return 0.71;
		// else if (tkmoney < 15)
		// return 0.61;
		// return 0.51;
	}

	public static double getJiemoney(double tkmoney) {
		return tkmoney * getFlrate(tkmoney);
	}

	public static String getUuid16() {
		return UuidUtil.getUuidByJdk(true).substring(8, 24);
	}

	public static String parseUrl(String text) {
		Matcher m = url_pattern.matcher(text);
		if (!m.find())
			return null;
		String url = m.group(0);
		if (url.contains("t.cn/"))
			return null;
		return url;
	}

	public static String parseTbUrl(String content) {
		String text = parseUrl(content);
		if (StringUtil.isEmpty(text))
			return null;
		if (text.contains("taobao.com") || text.contains("tmall.com") || text.contains("tmall.hk"))
			return text;
		return null;
	}

	public static String parseItemid(String text) {
		if (text.contains("taobao.com") || text.contains("tmall.com") || text.contains("tmall.hk")) {
			Matcher m = itemid_pattern.matcher(text);
			if (m.find())
				return m.group(1);
		}
		return null;
	}

	public static String parseTradeid(String text) {
		Matcher m = tradeid_pattern.matcher(text);
		if (m.find())
			return m.group(0);
		return null;
	}

	public static String parseTkpwd(String text) {
		if (parseTradeid(text) != null)
			return null;
		Matcher m = tkpwd_pattern.matcher(text);
		if (m.find())
			return "￥" + m.group(0) + "￥";
		return null;
	}

	public static String unicodeToString(String str) {
		Matcher matcher = unicode_pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			// group 6728
			String group = matcher.group(2);
			// ch:'木' 26408
			ch = (char) Integer.parseInt(group, 16);
			// group1 \u6728
			String group1 = matcher.group(1);
			str = str.replace(group1, ch + "");
		}
		return str;
	}

	public static String stringToUnicode(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			// 直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			// 然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}