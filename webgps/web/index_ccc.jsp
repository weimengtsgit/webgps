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
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><br>
<style type="text/css">
BODY {
	MARGIN-TOP: 0px; MARGIN-LEFT: 0px
}
.input4{ text-align:left;font-family:Arial,宋体;font-size:12px;padding:0;margin:0;border:0;line-height:22px;}
.btn{margin:20px 40px;}
.btn input { padding:0px; border:none;margin:0 10px; }
.btn2 { vertical-align:middle; background:url(images/login/login.png) no-repeat; color: #fff; display: inline-block; font: 700 12px "microsoft yahei"; cursor: pointer; text-align:center; height: 19px; line-height: 19px; width: 56px;padding:0px;}
input{font-size:18px;width:130px;}
.copyright{position:absolute; top:513px; left:470px; color:#fff;}
</style>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>

<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
</head>
<body>

<form name="LoginForm" method="post" action="">
<TABLE cellSpacing=0 cellPadding=0 width=1199 align=center border=0>
  <TBODY>
  <TR>
    <TD style="BACKGROUND-IMAGE: url(images/login/login_cc.png); POSITION: relative; HEIGHT: 595px" width=1199>
	<table width="100%" height="595px" border="0">
  <tr>
    <td width="40%" height="280">&nbsp;</td>
    <td width="27%">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td height="160">&nbsp;</td>
    <td><table width="100%" height="160px" border="0" cellSpacing=0 cellPadding=0>
  <tr>
    <td width="23%" height="23" class="input4"><strong>企业代码：</strong></td>
    <td width="77%"><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></td>
  </tr>
  <tr>
    <td height="23" class="input4"><strong>用户名：</strong></td>
    <td><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></td>
  </tr>
  <tr>
    <td height="23" class="input4"><strong>密码：</strong></td>
    <td><input type="password" name="password"  class="input1" id="password"/></td>
  </tr>
  <tr>
    <td height="23" class="input4"><strong>验证码：</strong></td>
    <td><input type="text" name="validateCode"  class="input3" id="validateCode"/><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
  </tr>
  <tr>
    <td height="40px">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><input type="button" class="btn2" value="登 录" name="login" id="login" onclick=submitForm()></td>
    <td align="center"><input type="reset" class="btn2" value="取 消"></td>
  </tr>
</table>
</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><table width="100%" border="0" height="120" cellSpacing=0 cellPadding=0>
  <tr>
    <td height="30">
		<font color="#FF0000">${error}</font>	</td>
  </tr>
  <tr>
    <td height="65" class="copyright">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Copyright 2010 版权所有
	</td>
  </tr>
  <tr>
    <td>&nbsp;
	</td>
  </tr>
</table>
</td>
    <td>&nbsp;</td>
  </tr>
</table>

	</TD>
  </TR>
  </TBODY>
</TABLE>
</form>
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
<input type="hidden" name="cooperation" value="index_cc">
</form>
</body>
</html>