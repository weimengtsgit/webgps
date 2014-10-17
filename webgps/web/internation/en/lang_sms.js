//document.write('<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>');
(function() {
	function Sms(){
		this.smsReceived = "SMS Received";
		this.createSms = "Create SMS";
		this.inbox = "Inbox";
		this.sentMessages = "Sent Messages";
		this.gridName = 'Name';
		this.gridMobileNumber = 'Mobile Number';
		this.gridMessage = 'Message';
		this.gridTime = 'Time';
		this.gridRead = 'Read?Y/N';
		this.gridFrom = 'From';
		this.gridTo = 'To';
		this.gridSearch = 'Search';
		this.gridSendToExcel = 'Send to Excel';
	}
	window.sms = new Sms();

})();
