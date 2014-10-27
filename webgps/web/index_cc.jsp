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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>

<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
</head>
<link rel="stylesheet" href="css/login/login_cc.css" type="text/css"/>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- Save for Web Slices (登录图片.jpg) -->
<div class="pos">
	<form name="LoginForm" method="post" action="">
	<dl> 
		<dt>企业代码：</dt> 
		<dd><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></dd> 
		<dt>用户名：</dt> 
		<dd><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></dd> 
		<dt>密码： </dt> 
		<dd><input type="password" name="password"  class="input1" id="password"/></dd> 
		<dt>验证码：</dt> 
		<dd class="chkcode"><input type="text" name="validateCode"  class="input3" id="validateCode"/><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></dd> 
		<div class="btn">
			<input type="button" class="btn2" value="登 录" name="login" id="login" onclick=submitForm()>
			<input type="reset" class="btn2" value="取消">
		</div>
		<font color="#FF0000">${error}</font>
		<div class="copyright">Copyright 2010 版权所有</div>
	</dl> 
	</form>
</div>
<!-- End Save for Web Slices -->

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
</form>
</body>
</html>