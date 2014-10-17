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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>今麦郎定位管理系统</title>
<link href="css/jml/all.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>

</head>

<body>
<div id="header">
  <div id="logo"></div>
  <div id="comlogo"></div>
</div>
<div id="content">
<div id="info">
  <h1>定位服务 图搜天下</h1>
  <h2>今麦郎定位服务平台V2.0</h2>
  <h3>由图搜天下公司推出的定位服务平台，该平台可向用户提供移动终端的即时位置信息、轨迹查询等服务，帮助企业拓展管理方式，降低管理成本，提高管理效率。</h3>
  <div id="client">签约客户</div>
  <div id="slide">
    <ul class="slideul1">
      <li class="slideli1">
        <ul class="slideul2">
          <li><img src="images/jml/slide/1.png"/></li>
          <li><img src="images/jml/slide/2.png"/></li>
          <li><img src="images/jml/slide/3.png"/></li>
          <li><img src="images/jml/slide/4.png"/></li>
          <li><img src="images/jml/slide/5.png"/></li>
        </ul>
      <li class="slideli2"></li>
    </ul>
  </div>
</div>
  <div id="login">
  
    <form id="loginform" method="post" name="LoginForm" action="">
  <ul>
<input type="hidden" name="empCode" value="jinmailang">
    <li><span>用户名：</span>
    <input placeholder="用户名" type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="login_text"  id="userAccount" required>
    </li>
    <li><span>密码：</span>
    <input placeholder="密码" type="password" name="password"  class="login_text" id="password" required>
    </li>
    <li><span>验证码：</span>
    <input placeholder="验证码" type="text" name="validateCode"  class="code" id="validateCode" required>
    <img id="validateImg" src="<%=path%>/system/validateCode.do" align="middle"  onClick="refreshImg()"></li>
    <button id="submit" name="userlogin" value="true" onClick="submitForm()">登录</button>
  </form>
  <font color="#FF0000">${error}</font>
  </div>
</div>
<div id="footer">
图搜天下定位服务平台今麦郎专版 &copy; 2012 SOSGPS, Inc. All rights reserved.<br />
京ICP备 09042169号-1
</div>
</body>
<SCRIPT src="images/index/Md5.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/index/login.js" type=text/javascript></SCRIPT>
<iframe id="targetFrame" name="targetFrame" height="0" style="display:none;"></iframe>
<form name="LoginForm2" method="post" action="">
<input type="hidden" name="empCode" value="jinmailang">
<input type="hidden" name="userAccount">
<input type="hidden" name="password">
<input type="hidden" name="loginType">
<input type="hidden" name="validateCode">
<input type="hidden" name="edition" value="zh_cn">
<input type="hidden" name="cooperation" value="jml">
</form>
<script type="text/javascript" src="js/jml/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jml/slide.js"></script>
</html>
