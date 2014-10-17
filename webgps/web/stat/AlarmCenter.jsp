<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="com.sosgps.wzt.util.*" %>

<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	//response.sendRedirect(path+"/error.jsp");
	//return;
}

Hashtable<String,String> mode5_report = (Hashtable)request.getSession().getAttribute("mode5_report");
String area_alarm_report = CharTools.javaScriptEscape(mode5_report.get("area_alarm_report"));
String licheng_report = CharTools.javaScriptEscape(mode5_report.get("licheng_report"));
String speed_alarm_report = CharTools.javaScriptEscape(mode5_report.get("speed_alarm_report"));
String active_alarm_report = CharTools.javaScriptEscape(mode5_report.get("active_alarm_report"));

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title></title>
<link rel="stylesheet" type="text/css" href="../ext-3.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="../ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../ext-3.1.1/ext-all.js"></script>

<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var area_alarm_report = '<%=area_alarm_report%>';
var licheng_report = '<%=licheng_report%>';
var speed_alarm_report = '<%=speed_alarm_report%>';
var active_alarm_report = '<%=active_alarm_report%>';

</script>

<script type="text/javascript" src="AlarmCenter.js" ></script>

</head>
<body>
<div id="grid-example"></div>
<input type="button" id="show-btn" value="vvv" />
</body>
</html>
