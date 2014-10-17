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
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>

	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	</script>
	<script type="text/javascript" src="<%=path%>/js/DateTool.js"></script>
	<script type="text/javascript" src="<%=path%>/system/poimanage/poilist.js"></script>
	
</head>
<body>
<form name="excelform"  method="post" action="" target="_self">
<input type="hidden" name="searchValue" id="searchValue">
<input type="hidden" name="startTime" id="startTime">
<input type="hidden" name="endTime" id="endTime">
<input type="hidden" name="expExcel" id="expExcel">
</form>

</body>
</html>