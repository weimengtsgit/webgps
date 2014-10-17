document.write('<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>');
(function() {
	function Sms(){
		this.smsReceived = "收短信";
		this.createSms = "写短信";
		this.inbox = "收信箱";
		this.sentMessages = "已发短信";
		this.gridName = '名称';
		this.gridMobileNumber = '手机号码';
		this.gridMessage = '内容';
		this.gridTime = '时间';
		this.gridRead = '是否已读';
		this.gridFrom = '开始时间';
		this.gridTo = '结束时间';
		this.gridSearch = '查询';
		this.gridSendToExcel = '导出Excel';
	}
	window.sms = new Sms();

})();
