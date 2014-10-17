<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*,com.sosgps.wzt.system.common.*,com.sosgps.wzt.orm.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<title></title>
</head>
<body>
<% 
	Object obj = org.sos.helper.SpringHelper.getBean("SmsServiceImpl");
	com.sosgps.wzt.sms.service.SmsService smsService = (com.sosgps.wzt.sms.service.SmsService)obj;
	//smsService.sendSms("13426457580","测试");
	List re = smsService.receiveSms("13426457580");
	if(re!=null && re.size()>0){
		ShandongSmsRecv shandongSmsRecv = (ShandongSmsRecv)re.get(0);
		System.out.println(shandongSmsRecv.getContent());
	}
%>
</body>
</html>
