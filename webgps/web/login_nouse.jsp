<%@ page language="java"pageEncoding="GBK"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>位置通</title>
<link href="css/all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
 function chkUser() {
  		//var empcode = document.getElementById("empCode").value;
 		var name = document.getElementById("userAccount").value;
		var pwd = document.getElementById("password").value;
		 
 		if (name == null || pwd == null || name.length==0 || pwd.length==0 ){
 		   alert("用户名或密码不能为空！");
  		   return false;
 		}
 		return true;
 }
function login(){
	if(chkUser()){
	document.forms[0].action="login.do";
	document.forms[0].submit();
	}
	else{
 		document.getElementById("userAccount").focus();
		return;
	}
}
</script>
</head>

<body>
<form method="post" action="" >
<input type="hidden" name="empCode" class="inputa" id="empCode" value="empRoot"/>
<div class="center">
<div class="top1"></div>
<div class="top2"><img src="images/logo.gif" /></div>
<div class="top3"></div>
<div class="top4"></div>
<div class="title"></div>
<div class="login">
<div class="inputs">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="30"><strong>用户名：</strong></td>
    <td><input type="text" name="userAccount" class="input1"  id="userAccount"/></td>
  </tr>
  <tr>
    <td height="30"><strong>密&nbsp;&nbsp;&nbsp;&nbsp;码：</strong></td>
    <td><input type="password" name="password"  class="input1" id="password"/></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><input name="" type="button" class="input2"  onclick="login();"/></td>
    </tr>
</table>
</div>
</div>
</div>
</form>
</body>
</html>