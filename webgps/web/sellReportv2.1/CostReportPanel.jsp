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
	targetTemplateType = "��";
} else if (targetTemplateType_ == 1) {
	targetTemplateType = "Ѯ";
} else {
	targetTemplateType = "��";
}
//String targetTemplateType = "��";
%>
<html>
<head>
  <title></title>
	<script type="text/javascript">
	var targetTemplateType = '<%=targetTemplateType%>';
	var path = '<%=path%>';
	var costChart;
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
	<script type="text/javascript">
	var path = '<%=path%>';
	</script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CostReportPanel.js"></script>
	
</head>
<body>
<table id="costChartTable" width="100%" height="100%" border="0">
			<tr>
				<td align="center" nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="costRadio" 
					onclick="javascript:selectFlag(true)" checked><span class="STYLE2">��<%=targetTemplateType%>���ö�����ͼ</span>				</td>
				<td width="89%" nowrap="nowrap">
				<input type="radio" name="costRadio" 
					onclick="javascript:selectFlag(false)">
					<span class="STYLE2">��ʷ���ö�����ͼ</span>				</td>
				<td width="89%" align="right" nowrap="nowrap"><span class="STYLE4" id="costUnauditedSpan">&nbsp;</span></td>
				<td width="89%" align="center" nowrap="nowrap">
					<a href="javascript:jumpToDetail()" class="STYLE4">����鿴��ϸ</a>				</td>
			</tr>
			<tr>
				<td colspan="4" align="center" nowrap="nowrap">
			    <div id="costChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div></td>
			</tr>
		</table>
</body>
</html>