<%@ page language="java"pageEncoding="GBK"%>
<html>
<head>
<title>WZT</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<link rel="stylesheet" href="../css/sap.css" type="text/css">
</head>
<%
String msg = (String)request.getAttribute("msg");
%>
<Script language="JavaScript" src="../closewin.js"></Script>
<body text="#000000" leftmargin="0" topmargin="0" scroll=no>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="baobiao_title">�����ɹ�</td>
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
      <p><%=msg%>
      <p>&nbsp;
      <p>
    </td>
  </tr>
  <tr align="center"> 
    <td align="center"><input type="button" class="button" value="��     ��" onclick="goback();"></td>
  </tr>
</table>
</body>
<script language="javascript">
function goback(){
window.history.back(-1);
}
</script>
</html>
