<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
String roleCode = user.getRoleCode();
String listModule = "/system/module.do?method=listUserModule&checked=true";
if(roleCode.equalsIgnoreCase("super_administrator")||roleCode.equalsIgnoreCase("defaultEntAdminRole")){
	listModule = "/system/module.do?method=listAllModule&checked=true";
}
listModule = "/system/module.do?method=listAllModule&checked=true";
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
    
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=path%>/ext-3.1.1/examples/ux/RowEditor.js"></script>
	
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	var listModule = '<%=listModule%>';
	</script>
	
	<script type="text/javascript" src="ent_role_modify.js"></script>
</head>
<body><br>

</body>
</html>