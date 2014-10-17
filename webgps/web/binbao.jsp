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
<title>������������ʳƷ���޹�˾ ����������Ϣ����ϵͳ ͼ�����¶�λƽ̨ ��¼</title>
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
	window.location.href = 'binbao_en.jsp';
  }
</script>
</head>
<body>
  <div id="logo">
    <h1>��λ���� ͼ������</h1>
    <h2>����������Ϣ����ϵͳ</h2>
    <h3>��ͼ�����¹�˾�Ƴ��Ķ�λ����ƽ̨����ƽ̨�����û��ṩ�ƶ��ն˵ļ�ʱλ����Ϣ���켣��ѯ�ȷ��񣬰�����ҵ��չ����ʽ�����͹���ɱ�����߹���Ч�ʡ�</h3>
  </div>
  <div id="bear">
    <div id="login">
      <form id="loginform" method="post" name="LoginForm" action="">
        <ul>
        <input type="hidden" name="empCode" value="binbao">
        <li><span>�û�����</span>
          <input placeholder="�û���" type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="login_text"  id="userAccount" required>
        </li>
        <li><span>���룺</span>
          <input placeholder="����" type="password" name="password"  class="login_text" id="password" required>
        </li>
        <li><span>��֤�룺</span>
          <input placeholder="��֤��" type="text" name="validateCode"  class="code" id="validateCode" required>
          <img id="validateImg" src="<%=path%>/system/validateCode.do" align="middle"  onClick="refreshImg()"></li>
		  <br />
		  <p>
       <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/LogInButton.gif" width=55>&nbsp;&nbsp;<a href="#" onclick=englishEdition()>English</a></li>
      </form>
  <font color="#FF0000">${error}</font>
    </div>
  </div>
  
  <div id="footer">ͼ�����¶�λ����ƽ̨����ר�� &copy; 2012 SOSGPS, Inc. All rights reserved.<br />
  ��ICP�� 09042169��-1</div>

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
<input type="hidden" name="edition" value="zh_cn">
<input type="hidden" name="cooperation" value="binbao">
</form>
<script type="text/javascript" src="js/jml/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jml/slide.js"></script>
</html>
