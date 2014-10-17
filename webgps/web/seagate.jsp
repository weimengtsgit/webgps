<%@ page language="java"pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.sosgps.wzt.util.*"%>
<% 
	String entCode = CookieHelper.getValue(request,"login_entCode");
	if(entCode == null) entCode = "";
	String userName = CookieHelper.getValue(request,"login_userName");
	if(userName == null) userName = "";
	request.setAttribute("entCode",entCode);
	request.setAttribute("userName",userName);
	String path = request.getContextPath();
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- saved from url=(0028)http://map.sogou.com/sosgps/ -->
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<META content=GPS定位,位置轨迹监控,手机定位,车辆定位,移动实时定位,地图定位轨迹 name=keywords>
<META content=搜狐图搜天下定位平台，提供手机、车辆的实时定位服务，由电子地图完美呈现，协助企业精细化管理销售、客服等外勤业务人员。 name=description>
<link rel="stylesheet" type="text/css" href="indexcss.css" />
<META content="MSHTML 6.00.6000.16981" name=GENERATOR>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<!--  
<script language="JavaScript" src="tip.js"></script>
-->
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
<script>
function englishEdition(){
	window.location.href = 'index_en.jsp';
}
</script>
<style type="text/css">
<!--
.foot_1_31 {	FONT-SIZE: 12px; LEFT: 20px; FLOAT: left; WIDTH: 252px; COLOR: #1a1817; LINE-HEIGHT: 20px; POSITION: relative; TOP: 0px; HEIGHT: 6px
}
.foot_1_11 {	FONT-SIZE: 18px; LEFT: 60px; FLOAT: left; WIDTH: 220px; COLOR: #fff; POSITION: relative; TOP: -16px; HEIGHT: 16px
}
-->
</style>
</HEAD>

<BODY>
<form name="LoginForm" method="post" action="">
<TABLE cellSpacing=0 cellPadding=0 width=1024 align=center border=0>
  <TBODY>
  <TR>
    <TD style="HEIGHT: 102px" width=1024><IMG  src="images/index/LogIn_Titel_seagate.jpg"  border=0></TD>
  </TR>
  <TR>
    <TD style="BACKGROUND-IMAGE: url(images/index/LogIn_Center1.jpg); POSITION: relative; HEIGHT: 416px" width=1024>
	<table width="100%" height="350" border="0">
      <tr>
        <td width="5%" rowspan="8">&nbsp;</td>
        <td height="29" colspan="2">&nbsp;</td>
        <td width="71%" height="67" rowspan="8">&nbsp;</td>
        </tr>
      
      <tr>
        <td width="8%" height="28" class="input4"><strong>企业代码：</strong></td>
        <td><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></td>
      </tr>
      <tr>
        <td width="8%" height="23" class="input4"><strong id="s_userName" style="display: ">用户名：</strong></td>
        <td width="16%"><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></td>
        </tr>
      <tr>
        <td width="8%" height="23" class="input4"><strong name="s_password" id="s_password" style="display:">密码：</strong></td>
        <td width="16%"><input type="password" name="password"  class="input1" id="password"/></td>
        </tr>
      <tr>
        <td width="8%" height="23" class="input4"><strong name="s_validatecode" id="s_validatecode" style="display:">验证码：</strong></td>
        <td width="16%"><input type="text" name="validateCode"  class="input3" id="validateCode"/>
          <img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
        </tr>
      <tr>
        <td width="8%" height="21" align="center"><IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/LogInButton.gif" width=55></td>
        <td width="16%"></td>
        </tr>
      <tr>
        <td colspan="2" class="input4"><font color="#FF0000">${error}</font></td>
        </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
        </tr>
    </table></TD>
  </TR>
  <TR>
    <TD  style="BACKGROUND-IMAGE: url(images/index/LogIn_Boot1.jpg); POSITION: relative; HEIGHT: 250px"  width=1024>
	<table width="100%" height="205" border="0">
      <tr>
        <td width="3%" height="31">&nbsp;</td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon1.jpg" width=35></td>
        <td width="27%"><span class="foot_1_1">随时定位</span></td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon2.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>轨迹查询</SPAN></td>
        <td width="5%" rowspan="2"><IMG   height=37 src="images/index/icon3.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>考勤报表</SPAN></td>
        <td width="3%">&nbsp;</td>
      </tr>
      <tr>
        <td height="15">&nbsp;</td>
        <td><SPAN class=foot_1_2>随时定位下属员工的当前所在位置</SPAN></td>
        <td><SPAN class=foot_1_2>可以查询员工的历史活动轨迹</SPAN></td>
        <td><SPAN class=foot_1_2>同时查看和下载员工的考勤报表</SPAN></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="88">&nbsp;</td>
		<td colspan="2"><span class="foot_1_31">地图给出被定位员工所在位置的标点，鼠标移动到该点，系统给出员工名称、定位时间及当前所在位置描述信息。</span></td>
        <td colspan="2"><span class="foot_1_31">只需设定时间段，即可查询某个员工在此时间段内的活动走向和轨迹，系统同时给出每个点的员工名称、定位时间及当时所在位置的描述信息。</span></td>
        <td colspan="2"><span class="foot_1_31">设定时间段，查看所有员工的考勤情况，也可以查看某个员工某时间段内的详细考勤列表，其中包括所有标点的定位时间及位置描述信息，支持Excel表格导出，方便企业的人员管理。</span></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="56">&nbsp;</td>
        <td colspan="6">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table></TD>
  </TR>
  </TBODY>
</TABLE>
</form>
<MAP id=Map name=Map><AREA shape=RECT target=_blank coords=215,3,269,18 href="http://www.sogou.com/docs/terms.htm">
<AREA shape=RECT target=_blank coords=275,11,289,27  href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202005041500024">
<AREA  shape=RECT target=_blank coords=114,3,207,16  href="http://www.miibeian.gov.cn/"></MAP>
<SCRIPT src="images/index/Md5.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/index/login.js" type=text/javascript></SCRIPT>
<iframe id="targetFrame" name="targetFrame" height="0" style="display:none;"></iframe>
<form name="LoginForm2" method="post" action="">
<input type="hidden" name="empCode">
<input type="hidden" name="userAccount">
<input type="hidden" name="password">
<input type="hidden" name="loginType">
<input type="hidden" name="validateCode">
<input type="hidden" name="edition" value="zh_cn">
<input type="hidden" name="cooperation" value="seagate">
</form>
</BODY>
</HTML>
