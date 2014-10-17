<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/buttons.css" />
<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
	var path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/target/rate_update.js"></script>
</head>
<body></body>
</html>