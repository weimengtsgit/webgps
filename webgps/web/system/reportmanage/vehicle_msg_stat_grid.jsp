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
	<script type="text/javascript" src="<%=path%>/js/DateTool.js"></script>
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	var tmpdate = (new Date()).pattern("yyyy-M-d");
	var yesterday = new Date();
	yesterday.setDate(yesterday.getDate()-1);
	var tmpdateyesterday = (yesterday).pattern("yyyy-M-d");
	Ext.form.Field.prototype.msgTarget = 'under';
	function storeLoad(store, start, limit, deviceIds, searchValue, startTime, endTime, duration, expExcel, deviceId, poiId, week, workStartTime, workEndTime){
		store.load({params:{start: start, limit: limit, deviceIds: deviceIds ,searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime, duration: duration, expExcel: expExcel, deviceId: deviceId, poiId: poiId, week: week, workStartTime: workStartTime, workEndTime: workEndTime}});
	}
	</script>
	<script type="text/javascript" src="<%=path%>/system/reportmanage/vehicle_msg_stat_grid.js"></script>
	
</head>
<body>
<form name="excelform" id="excelform" method="post" action="" target="_self"></form>
</body>
</html>