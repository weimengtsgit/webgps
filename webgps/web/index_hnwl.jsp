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
<html>
<head>
<title>1024定位</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
<!--
body {
	margin-top: 0px;
	margin-bottom: 0px;
}
.login_font{font-size:12px; color:#333333; font-weight:bold;}
.login_text{height:20px; width:120px;}
.footer{font-size:12px; color:#FFFFFF; line-height:20px;}
.code{height:20px; width:60px;}
-->
</style>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" marginwidth="0">
<form name="LoginForm" method="post" action="">
<!-- ImageReady Slices (1024定位.jpg) -->
<table width="1024" height="737" border="0" align="center" cellpadding="0" cellspacing="0" id="__01">
	<tr>
		<td colspan="6">
			<img src="images/hnwl/GPS_01.gif" width="1024" height="25" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/hnwl/GPS_02.gif" width="619" height="52" alt=""></td>
		<td colspan="2">
			<a href="#"><img src="images/hnwl/GPS_03.gif" alt="" width="134" height="52" border="0"></a></td>
		<td>
			<a href="#"><img src="images/hnwl/GPS_04.gif" alt="" width="131" height="52" border="0"></a></td>
		<td>
			<a href="#"><img src="images/hnwl/GPS_05.gif" alt="" width="130" height="52" border="0"></a></td>
		<td>
			<img src="images/hnwl/GPS_06.gif" width="10" height="52" alt=""></td>
	</tr>
	<tr>
		<td colspan="6">
			<img src="images/hnwl/GPS_07.gif" width="1024" height="37" alt=""></td>
	</tr>
	<tr>
		<td colspan="2">
			<img src="images/hnwl/GPS_08.gif" width="652" height="532" alt=""></td>
	  <td height="532" colspan="4" align="center" background="images/hnwl/GPS_09.gif">
	    <table width="265" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="114" height="25" class="login_font">企业代码：</td>
            <td colspan="2"><label>
              <input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="login_text" id="empCode"/>
            </label></td>
          </tr>
		  <tr>
            <td width="114" height="25" class="login_font">用户名：</td>
            <td colspan="2"><label>
              <input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="login_text"  id="userAccount"/>
            </label></td>
          </tr>
          <tr>
            <td height="25" class="login_font">密&nbsp;&nbsp;码：</td>
            <td colspan="2"><label>
              <input type="password" name="password"  class="login_text" id="password"/>
            </label></td>
          </tr>
          <tr>
            <td height="25" class="login_font">验证码：</td>
            <td width="77"><label>
              <input type="text" name="validateCode"  class="code" id="validateCode"/>
            </label></td>
            <td width="109"><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
          </tr>
          <tr>
            <td height="25" colspan="3" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/hnwl/LOGIN.gif" width=55></td>
          </tr>
          <tr>
            <td height="25" colspan="3" align="left"><font color="#FF0000">${error}</font></td>
          </tr>
          <tr>
            <td height="50" colspan="3" align="left">&nbsp;</td>
          </tr>
        </table>
            
	  </td>
	</tr>
	<tr>
		<td height="90" colspan="6" align="center" background="images/hnwl/GPS_10.gif" class="footer">版权所有 湖南物流信息 2003-2011,保留所有权利<br>
		  建议使用IE7.0版本 分辨率1024*768<br>
		  客服电话：0731-84085656 京ICP备06009405号 </td>
	</tr>
	<tr>
		<td>
			<img src="images/hnwl/分隔符.gif" width="619" height="1" alt=""></td>
		<td>
			<img src="images/hnwl/分隔符.gif" width="33" height="1" alt=""></td>
		<td>
			<img src="images/hnwl/分隔符.gif" width="101" height="1" alt=""></td>
		<td>
			<img src="images/hnwl/分隔符.gif" width="131" height="1" alt=""></td>
		<td>
			<img src="images/hnwl/分隔符.gif" width="130" height="1" alt=""></td>
		<td>
			<img src="images/hnwl/分隔符.gif" width="10" height="1" alt=""></td>
	</tr>
</table>
</form>
<!-- End ImageReady Slices -->
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
<input type="hidden" name="cooperation" value="hnwl">
</form>
</body>
</html>