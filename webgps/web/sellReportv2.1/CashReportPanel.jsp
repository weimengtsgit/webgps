<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
String targetTemplateType = user.getTargetTemplateType();
int targetTemplateType_ = targetTemplateType.equals("") ? 2
		: Integer.valueOf(targetTemplateType);
if (targetTemplateType_ == 0) {
	targetTemplateType = "周";
} else if (targetTemplateType_ == 1) {
	targetTemplateType = "旬";
} else {
	targetTemplateType = "月";
}
//String targetTemplateType = "月";
%>
<html>
<head>
  <title></title>
	<script type="text/javascript">
	var targetTemplateType = '<%=targetTemplateType%>';
	var path = '<%=path%>';
	var cashChart;
	var flag = true;
	function selectFlag(flag_){
		flag = flag_;
	}
	function funConvertUTCToNormalDateTime(utc){
		var date = new Date(utc);
		date.setHours(date.getHours()+8);
		var ndt;
		var year_ = date.getUTCFullYear();
		var month_ = (date.getUTCMonth()+1);
		var date_ = date.getUTCDate();
		month_ = month_ >= 10 ? month_ : '0' + month_;
		date_ = date_ >= 10 ? date_ : '0' + date_;
		ndt = year_ + "-" + month_ + "-" + date_ + " " + (date.getUTCHours())+":"+date.getUTCMinutes()+":"+date.getUTCSeconds();
		return ndt;
	}
	</script>
	<link rel="stylesheet" type="text/css" href="<%=path%>/mainv2.1/index.css" />
	<script type="text/javascript" src="<%=path%>/js/jquery/jquery.min.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/highcharts.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/modules/exporting.js"></script>
	<script src="<%=path%>/sellReportv2.1/ReportLineChart.js"></script>
	
</head>
<body>
<table id="cashChartTable" width="90%" height="60%" border="0">
			<tr>
				<td align="center" nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="cashRadio" 
					onclick="javascript:selectFlag(true)" checked><span class="STYLE2">本<%=targetTemplateType%>回款额趋势图</span>				</td>
				<td width="89%" nowrap="nowrap">
				<input type="radio" name="cashRadio" 
					onclick="javascript:selectFlag(false)">
					<span class="STYLE2">历史回款额趋势图</span>				</td>
				<td width="89%" align="right" nowrap="nowrap"><span class="STYLE4" id="cashUnauditedSpan">&nbsp;</span></td>
				<td width="89%" align="center" nowrap="nowrap">
					<a href="javascript:jumpToDetail('cashChartDiv')" class="STYLE4">点击查看明细</a>				</td>
			</tr>
			<tr>
				<td colspan="4" align="center" nowrap="nowrap">
			    <div id="cashChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div></td>
			</tr>
		</table>
</body>
</html>