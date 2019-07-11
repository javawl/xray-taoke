(function () {
chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {
	console.log(request);
		if(request.action == 'goMpLogin') {
			var data = request.data;
			for (x in data) {
				var obj = data[x];
				chrome.cookies.set({
					'url': 'https://mp.weixin.qq.com/',
					'name': obj.name,
					'value': obj.value,
					'secure': obj.secure,
					'httpOnly': obj.httpOnly
				}, function (cookie) {
				});
			}
			chrome.tabs.query({active: true, currentWindow: true}, function(tabs){  
					chrome.tabs.sendMessage(tabs[0].id, {action:"doMpLoginSucc"}, function(response) {});
			});
		}else if(request.action=="上报COOKIE"){
			  chrome.cookies.getAll({}, function(cookies) {	
			  		console.log(cookies);
  			});
			
		}
});









})();



