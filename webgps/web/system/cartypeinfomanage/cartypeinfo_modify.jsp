<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	//response.sendRedirect(path+"/error.jsp");
	//return;
}
String empCode = user.getEmpCode();
Long userId = user.getUserId();
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=path%>/js/DateTool.js"></script>
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	var empCode = '<%=empCode%>';
	var userId = '<%=userId%>';

	</script>
	<script type="text/javascript" src="<%=path%>/system/cartypeinfomanage/cartypeinfomodify.js"></script>
	
</head>
<body>
</body>
</html>