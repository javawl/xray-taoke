var count = 1;
var page = 1;
var pageno = 1;
//1为抓销量,2为抓新品
var kind = 1;



document.addEventListener('DOMContentLoaded', function() {});













if(location.href.indexOf("www.haodanku.com") != -1) {
	$(".am-avg-sm-2").each(function() {
		$(this).children("li").eq(0).children("span").text("清空");
		$(this).children("li").eq(0).children("span").attr("data-itemid", "");
		$(this).children("li").eq(0).children("span").addClass("clearByMySelf");
		$(this).children("li").eq(1).children("span").text("复制");
		$(this).children("li").eq(1).children("span").attr("data-itemid", "");
		$(this).children("li").eq(1).children("span").addClass("copybymyself");
	})
}




chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	
	if(request == "抓取阿里妈妈") {

		var arr = [];
		var itempic = "";
		var itemlink = "";
		var itemtitle = "";
		var cpmoney = 0;
		var itemprice = 0;
		var rate = 0.0;
		var ratemoney = 0;
		var yuexiao = 0;
		var id = "";
		var tkprice = 0;
		$(".block-search-box").each(function() {
			itempic = $(this).children(".pic-box").children("a").children("img").attr("src");
			if(itempic.indexOf("png")!=-1){
				itempic = $(this).children(".pic-box").children("a").children("img").attr("data-src");
			}
			console.log(itempic);
			var content_line = $(this).children(".box-content").children(".content-line");
			itemlink = content_line.eq(0).children("p").children("a").attr("href");

			id = getQueryString("id", itemlink);

			itemtitle = content_line.eq(0).children("p").children("a").attr("title");
			cpmoney = content_line.eq(1).children("span").eq(0).children("span").eq(1).children("span").text();
			var itemprice_str = content_line.eq(2).children("span").eq(0).children("span");
			itemprice = itemprice_str.eq(1).text() + itemprice_str.eq(2).text() + itemprice_str.eq(3).text();
			
			
			
			if(itemprice.indexOf(",")!=-1){
				itemprice = itemprice.replace(",","");
			}
			
			
			tkprice = itemprice-cpmoney;
			
			
			
			
			yuexiao = content_line.eq(2).children("span").eq(1).children("span").eq(1).children("span").text();

			var rate_str = content_line.eq(3).children("span").eq(0).children("span").eq(1).children("span");
			rate = rate_str.eq(0).text() + rate_str.eq(1).text() + rate_str.eq(2).text();
			rate = rate / 100;

			var yongjin_str = content_line.eq(3).children("span").eq(1).children("span").eq(1).children("span");
			ratemoney = yongjin_str.eq(1).text() + yongjin_str.eq(2).text() + yongjin_str.eq(3).text();

			var data = {
				itemid: id,
				itemtitle: itemtitle,
				itemprice: itemprice,
				itempic: itempic,
				itemsale: yuexiao,
				tkprice: tkprice,
				tkrate: rate,
				cpmoney: cpmoney,
				type:1
			}
			console.log(data);
			arr.push(data);
		})

		var html = '';
		html += '<form action="http://taoke.ldpro.cn/taoke_admin/linkinfo/doReportItem" method="post" style="display:none" id="form_data">';
		//html += '<form action="http://localhost:82/taoke_admin/linkinfo/doReportItem" method="post" style="display:none" id="form_data">';
		html += '<input type="text" name="arr" id="arr" value=""><br>';
		html += '</form>';

		$(".input-group-btn").append(html);
		$("#arr").attr("value", JSON.stringify(arr));
		$("#form_data").submit();

	} else if(request == "抓取好单库") {
		//主页
		var url = location.href;
		var arr = [];

		var html = '';
		html += '<form action="http://taoke.ldpro.cn/taoke_admin/linkinfo/doReportItem" method="post" style="display:none" id="form_data">';
		//html += '<form action="http://localhost:82/taoke_admin/linkinfo/doReportItem" method="post" style="display:none" id="form_data">';
		
		
		html += '<input type="text" name="arr" id="arr" value=""><br>';
		html += '</form>';

		$("#search_btn").append(html);
		
		var data = {};
		if(url.indexOf("https://www.haodanku.com/index/all_index")!=-1){
			
			$(".commodity-box .commodity-inner").each(function() {
			var id = $(this).children(".commodity-imgBox").children("span").eq(1).attr("data-itemid");
			var link = "https://item.taobao.com/item.htm?id="+id;
			var img = $(this).children(".commodity-imgBox").children("a").children("img").attr("src");
			if(img.indexOf("gif") != -1) {
				img = $(this).children(".commodity-imgBox").children("a").children("img").attr("data-echo");
			}
			
			
			var inProduc_l_m = $(this).children(".commodity-details");
			var fq_goods_sale = inProduc_l_m.children(".coupon-wrap");

			var juan = fq_goods_sale.children("a").children(".coupon-div").children(".coupon-number").text();
			
			var yuexiao = fq_goods_sale.children(".sales-div").children(".sales-month").children(".sales-number").text();
			yuexiao = yuexiao.trim();
		
		
			var detail = inProduc_l_m.children(".commodity-money").children("li");
			var juanhou = detail.eq(0).children("span").eq(0).text();
			var yingxiao = detail.eq(1).children("span").eq(0).text();
			var yongjin = detail.eq(2).children("span").eq(0).text();
			
			var title = inProduc_l_m.children("a").text().trim();
			var data = {
				itemid: id,
				itemtitle: title,
				itemprice: 0,
				itempic: img,
				tkprice: juanhou,
        		yuexiao: yuexiao,
				yingxiao: yingxiao,
				juan: juan,
				type:2
			}
			arr.push(data);
		})
		}else{
		
		$(".inProduc-left .inProduc-l-list > li").each(function() {
			//var id = $(this).children(".inProduc-l-ImgBox").children(".inProduc-l-ImgBottom").children("span").eq(0).attr("data-itemid");
			var datatips = $(this).children(".inProduc-l-ImgBox").children(".inProduc-l-ImgBottom").children("span").eq(1).attr("datatips");
			var i = datatips.lastIndexOf("下单：")+3;
			var j = datatips.lastIndexOf("<br>");
			
			var url = datatips.substring(i,j);
			
			var id = getQueryString("id",url);
			
			
			
			
			var link = "https://item.taobao.com/item.htm?id="+id;
			var img = $(this).children(".inProduc-l-ImgBox").children("img").attr("src");
			if(img.indexOf("gif") != -1) {
				img = $(this).children(".inProduc-l-ImgBox").children("img").attr("data-echo");
			}
			
			
			var inProduc_l_m = $(this).children(".inProduc-l-conter").children(".inProduc-l-m");
			var fq_goods_sale = $(this).children(".inProduc-l-conter").children(".inProduc-l-xs").children(".inProduc-l-couponWrap");

			var juan = fq_goods_sale.children("a").children("em").text();
			var yuexiao = fq_goods_sale.children("p").children("i").eq(0).text();
			yuexiao = yuexiao.trim();
		
		
			var detail = inProduc_l_m.children(".inProduc-l-ul").children("li");
			var juanhou = detail.eq(0).children("span").eq(0).text();
			var yingxiao = detail.eq(1).children("span").eq(0).text();
			var yongjin = detail.eq(2).children("span").eq(0).text();
			
			var title = inProduc_l_m.children(".comHidden").text().trim();
			var data = {
				itemid: id,
				itemtitle: title,
				itemprice: 0,
				itempic: img,
				tkprice: juanhou,
        		yuexiao: yuexiao,
				yingxiao: yingxiao,
				juan: juan,
				type:2
			}
			arr.push(data);
		})
		}
		
		

		$("#arr").attr("value", JSON.stringify(arr));
		$("#form_data").submit();
	}

});

var copy_txt = "";

$(".copybymyself").click(function() {

	copy_txt += removeAllSpace($(this).attr("data-tips")) + "\n";
	$(this).text("复制");

	var clipboard = new Clipboard('.copybymyself', {
		text: function() {
			return copy_txt;
		}
	});

})

function removeAllSpace(str) {
	return str.replace(/\s+/g, "");
}

$(".clearByMySelf").click(function() {
	$(this).text("清空");
	copy_txt = "";
	var clipboard = new Clipboard('.clearByMySelf', {
		text: function() {
			return " ";
		}
	});

})

function getQueryString(name, url) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = url.split("?")[1].match(reg);
	if(r != null) {
		return unescape(r[2]);
	}
	return null;
}