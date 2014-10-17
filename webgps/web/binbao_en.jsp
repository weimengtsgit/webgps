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
<title>宾堡（北京）食品有限公司 宾堡车辆信息管理系统 图搜天下定位平台 登录</title>
<link href="index_binbao/css/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
<script language="javascript">
  function englishEdition(){
	window.location.href = 'binbao.jsp';
  }
</script>
</head>
<body>
  <div id="logo">
    <h1>SOSGPS<p>PositionServices
    </h1>
    <h2>BIMBO Vehicle Information<p>Management System</h2>
    <h3>Sohu.com Inc. and jointly launch the world map search, the platform provides users with real-
          time location information of mobile terminals,track inquiry services to help companies develop management,
          lower management costs, improve management efficiency.</h3>
</div>
  <div id="bear">
    <div id="login">
      <form id="loginform" method="post" name="LoginForm" action="">
        <ul>
        <input type="hidden" name="empCode" value="binbao">
        <li><span><strong id="s_userName" style="display: ">Username</strong>：</span>
          <input placeholder="用户名" type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="login_text"  id="userAccount" required>
        </li>
        <li><span><strong name="s_password" id="s_password" style="display:">Password</strong>：</span>
          <input placeholder="密码" type="password" name="password"  class="login_text" id="password" required>
        </li>
        <li><span><strong name="s_validatecode" id="s_validatecode" style="display:">Security Code</strong>：</span>
          <input placeholder="验证码" type="text" name="validateCode"  class="code" id="validateCode" required>
          <img id="validateImg" src="<%=path%>/system/validateCode.do" align="middle"  onClick="refreshImg()"></li>
		  <br />
		  <p>
       <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/login_button.gif" width=55>&nbsp;&nbsp;<a href="#" onclick=englishEdition()>中文(简体)</a></li>
      </form>
  <font color="#FF0000">${error}</font>
    </div>
  </div>
  
  <div id="footer">SOSGPS PositionServices BIMBO Single-minded board &copy; 2012 SOSGPS, Inc. All rights reserved.<br />
  京ICP备 09042169号-1</div>

</body>
<SCRIPT src="images/index/Md5.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/index/login.js" type=text/javascript></SCRIPT>
<iframe id="targetFrame" name="targetFrame" height="0" style="display:none;"></iframe>
<form name="LoginForm2" method="post" action="">
<input type="hidden" name="empCode" value="binbao">
<input type="hidden" name="userAccount">
<input type="hidden" name="password">
<input type="hidden" name="loginType">
<input type="hidden" name="validateCode">
<input type="hidden" name="edition" value="en">
<input type="hidden" name="cooperation" value="binbao">
</form>
<script type="text/javascript" src="js/jml/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jml/slide.js"></script>
</html>
