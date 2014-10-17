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
<title>�����ɶ�λ����ϵͳ</title>
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
  <h1>��λ���� ͼ������</h1>
  <h2>�����ɶ�λ����ƽ̨V2.0</h2>
  <h3>��ͼ�����¹�˾�Ƴ��Ķ�λ����ƽ̨����ƽ̨�����û��ṩ�ƶ��ն˵ļ�ʱλ����Ϣ���켣��ѯ�ȷ��񣬰�����ҵ��չ����ʽ�����͹���ɱ�����߹���Ч�ʡ�</h3>
  <div id="client">ǩԼ�ͻ�</div>
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
    <li><span>�û�����</span>
    <input placeholder="�û���" type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="login_text"  id="userAccount" required>
    </li>
    <li><span>���룺</span>
    <input placeholder="����" type="password" name="password"  class="login_text" id="password" required>
    </li>
    <li><span>��֤�룺</span>
    <input placeholder="��֤��" type="text" name="validateCode"  class="code" id="validateCode" required>
    <img id="validateImg" src="<%=path%>/system/validateCode.do" align="middle"  onClick="refreshImg()"></li>
    <button id="submit" name="userlogin" value="true" onClick="submitForm()">��¼</button>
  </form>
  <font color="#FF0000">${error}</font>
  </div>
</div>
<div id="footer">
ͼ�����¶�λ����ƽ̨������ר�� &copy; 2012 SOSGPS, Inc. All rights reserved.<br />
��ICP�� 09042169��-1
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
