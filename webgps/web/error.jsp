<%@ page language="java"pageEncoding="GBK"%>
<% 
	String errorInfo = (String)request.getAttribute("error");
	if(errorInfo == null || errorInfo.length() <=0 ){
		errorInfo = "��¼ʧ�ܣ������µ�¼����";
	}
%>
<html>
<head>
<title>SOSGPS</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<link rel="stylesheet" href="./css/sap.css" type="text/css">
</head>
<Script language="JavaScript" src="./closewin.js"></Script>
<body text="#000000" leftmargin="0" topmargin="0" scroll=no>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="baobiao_title">ϵͳ����</td>
    </tr>
    <tr> 
      <td height="1"></td>
    </tr>
    <tr>
      <td bgcolor="#CCCCCC" height="1"></td>
    </tr>
  </table>
  <br>
<table width="403" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr> 
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td align="center" height="150"> 
      <p><%=errorInfo%>������ϵͳ����Ա��ϵ!
      <p>&nbsp;
      <p>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
</table>
</body>
<script language="javascript">
function loginOut(){
window.location="/index.jsp";
window.reload();
}
</script>
</html>
