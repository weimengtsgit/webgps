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
	<link rel="stylesheet" type="text/css" href="<%=path%>/mainv2.1/index.css" />
	<script type="text/javascript" src="<%=path%>/js/jquery/jquery.min.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/highcharts.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/modules/exporting.js"></script>
	<script type="text/javascript">
	var path = '<%=path%>';
	</script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/visitRankChart.js"></script>
	
</head>
<body>
<table id="visitRankChartTable" width="100%" height="100%" border="0">
			<tr>
				<td nowrap="nowrap"></td>
			</tr>
			<tr>
				<td width="100%">
					<div id="visitRankChartDiv" style="min-width: 500px; margin: 0 auto"></div>
				</td>
			</tr>
		</table>
</body>
</html>