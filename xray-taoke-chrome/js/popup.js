
function executeScriptToCurrentTab(code)
{
	getCurrentTabId((tabId) =>
	{
		chrome.tabs.executeScript(tabId, {code: code});
	});
}


function getCurrentTabId(callback)
{
	chrome.tabs.query({active: true, currentWindow: true}, function(tabs)
	{
		if(callback) callback(tabs.length ? tabs[0].id: null);
	});
}


// 向content-script主动发送消息
function sendMessageToContentScript(message, callback)
{
	getCurrentTabId((tabId) =>
	{
		chrome.tabs.sendMessage(tabId, message, function(response)
		{
			if(callback) callback(response);
		});
	});
}
















$('#catch_alimama').click(() => {
	
	sendMessageToContentScript('抓取阿里妈妈', (response) => {
		if(response) alert('收到来自content-script的回复：'+response);
	});
	
});



$('#catch_haodanku').click(() => {
	
	sendMessageToContentScript('抓取好单库', (response) => {
		if(response) alert('收到来自content-script的回复：'+response);
	});
	
});


$('#catch_taobao').click(() => {
	
	sendMessageToContentScript('抓取淘宝', (response) => {
		if(response) alert('收到来自content-script的回复：'+response);
	});
	
});