package com.xray.taoke.admin.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.xray.act.util.HttpUtil;
import com.xray.act.util.StringUtil;
import com.xray.taoke.admin.model.TkLyInfo;
import com.xray.taoke.admin.utils.ParamUtils.UrlEntity;

public class HttpUtils {
	// private static final Pattern url_pattern =
	// Pattern.compile("(https?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))");
	private static final Pattern url_pattern = Pattern.compile(
			"https?://(\\\\w|-)+(\\\\.(\\\\w|-)+)+(/(\\\\w|-)+(/((\\\\w|-)+\\\\.(\\\\w|-)+)|/(\\\\w|-)*)|(/((\\\\w|-)+\\\\.(\\\\w|-)+)|/(\\\\w|-)+)|/?)");

	public static List<String> parseUrl(String text) {
		Matcher m = url_pattern.matcher(text);
		List<String> list = new ArrayList<>();
		while (m.find()) {
			list.add(m.group());
		}
		return list;
	}

	public static String httpRequest(String url, String cookie) {
		String result = "";
		String location = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			if (StringUtil.isNotEmpty(cookie))
				connection.setRequestProperty("Cookie", cookie);
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3067.6 Safari/537.36");
			connection.setConnectTimeout(30000);

			connection.setConnectTimeout(120000);
			// 建立实际的连接
			connection.connect();
			connection.getHeaderField("Location");

			// for(int i=1;connection.getHeaderField(i)!=null;i++){
			// String cookieVal = connection.getHeaderField(i);
			// System.out.println(cookieVal);
			// }

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(location)) {
			result = "{\"location\":\"" + location + "\"}";
		}
		return result;
	}

	public static String urlCommon(String url) {
		String location = "";
		try {
			StringBuffer buffer = new StringBuffer();

			// 发送get请求
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setRequestMethod("GET");
			// 必须设置false，否则会自动redirect到重定向后的地址
			conn.setInstanceFollowRedirects(false);
			conn.addRequestProperty("Accept-Charset", "UTF-8;");
			conn.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
			conn.addRequestProperty("Referer", "http://matols.com/");
			conn.connect();

			// 判定是否会进行302重定向
			if (conn.getResponseCode() == 302) {
				// 如果会重定向，保存302重定向地址，以及Cookies,然后重新发送请求(模拟请求)
				location = conn.getHeaderField("Location");
				String cookies = conn.getHeaderField("Set-Cookie");

				serverUrl = new URL(location);
				conn = (HttpURLConnection) serverUrl.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Cookie", cookies);
				conn.addRequestProperty("Accept-Charset", "UTF-8;");
				conn.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
				conn.addRequestProperty("Referer", "http://matols.com/");
				conn.connect();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;

	}

	public static String getCode(String url) {
		String code = url.substring(url.lastIndexOf("/") + 1, url.length());
		return code;
	}

	public static String getSkuId(String url) {
		String str = com.jfinal.weixin.sdk.utils.HttpUtils.get(url);
		String code = getCode(url);
		int i = str.indexOf("https://u.jd.com/jda?e=");
		int y = str.indexOf(code);
		url = str.substring(i, y + 6);
		System.out.println(url);
		String redirct_url = urlCommon(url);
		UrlEntity urlEntity = ParamUtils.parseParam(redirct_url);
		String skuid = urlEntity.params.get("sku");
		return skuid;
	}

	public static void main(String[] args) {
		String day = "2019-06-20";
		String url = "https://www.qilvyoo.com/home/prod_log.jsp?d=" + day + "&s=0";
		String cookie = "JSESSIONID=iSETeDIxK1Rh; dc4e01dbca1cd374ffb9068b31380fc2=3Y9QTM2UTM5ADO3QzNxESOjZXdyJWZj5Ve09XelBTPmA3YzVFdp9DZx0zN1AzMwcmJ1N3cfRHdwlTZy0mJiR2Xpxmb9s2chFzcwQmJfd3YzVFdp9DZ10DM4UTNmgXafNGZzlDcx0mJzl2Xyd3bwVTPmAXafN2dphGb9UCMsZWYn5SPsZXefRWa9QnJvJGbfVWa9QyM1Z2cyV2XklXPsFTe4AjMmUXdlNlcu9WYl1ePuiOuUmOkzaiomY2cnljby0UQ3QEMDNkRFJTQxgTQ5EzN4gTQ3EEM2UTQzQTRyYwM==";
		String data = HttpUtils.httpRequest(url, cookie);

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
				TkLyInfo info = TkLyInfo.dao.queryByInfo(infoid, url);
				if (info == null) {
					TkLyInfo info2 = new TkLyInfo();
					info2.set("url", url);
					info2.set("inputtime", System.currentTimeMillis());
					info2.set("infoid", infoid);
					info2.set("title", title);
					info2.set("itemurl", itemurl);
					info2.save();
				}
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
