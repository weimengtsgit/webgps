<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@page import="com.sosgps.wzt.util.CharTools"%>
<%
	String username = CharTools.getStr(request.getParameter("username"));
	String x = request.getParameter("x");
	String y = request.getParameter("y");
	
 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="../css/selStyle.css" type="text/css">
<script language="javascript" src="../map/js/citycoord.js"></script>

<title>�ܱ߲���</title>
</head>
<body>

<script language="javaScript">
//�ύ
function onsubmit1Form(){
	var subForm =document.getElementById("nearbyfind");
	var selcity0 = document.getElementById("citylist0");
	var citycode0= selcity0.value;
	if (citycode0==""){alert("��ѡ�����");return;}
	subForm.item("param1").value =citycode0;
	subForm.submit();
}

//��λ������
function zoomToCity(cityCode){
	var cityCoord=	getCityCenter(cityCode);
	if (cityCoord==null || cityCoord=='')return;
	var cityxy = cityCoord.split(",");
	var x = cityxy[0];
	var y = cityxy[1];
	parent.ifrmap.setMapCenter(x,y);
}

</script>


<form id="nearbyfind" name="nearbyfind" action="xyrequest.jsp"  method="post" target="_self">
<input type="hidden" id="param0" name="param0" value="<%=username %>">
<input type="hidden" id="param1" name="param1" value="">
<input type="hidden" id="param6" name="param6" value="1">
<input type="hidden" id=param7 name="param7" value="1">
<input type="hidden" id="param8" name="param8" value="6">

<table width="100%" border="0" bgcolor="#759CB0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td>
		<table  width="100%" border="0" cellspacing="1" cellpadding="1">
		      <tr bgcolor="#FFFFFF">
		      <td bgcolor="#eeeeee">�ڳ���</td>
		      <td>
		      <script language="javascript" src="../map/js/citylist.js"></script>
		      
			    <script language="javascript">
			   	WriteCitylist('citylist0','zoomToCity(this.value)','010');
			    </script>
			  </td>
			</tr>
		      <tr bgcolor="#FFFFFF">
		      	<td bgcolor="#eeeeee" colspan="2" >���� <%=username %> </td>
		 		<input type="hidden" name="param2" value="<%=x %>"/>
		 		<input type="hidden" name="param3" value="<%=y %>"/>
		      </tr>
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">�ܱ�</td><td><input id="param5" name="param5" type="text" value="1000" style="width:110px" onKeyPress="inputNumberOnly()">��</td></tr>
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">��</td><td><input id="param4" name="param4" type="text"  style="width:110px" value=""><font class="bitian">*����</font><font class="huixian"> ��:����</font></td></tr>
		      <tr bgcolor="#FFFFFF"><td colspan="2" align="right"><img style="cursor:hand" src="../images/query.gif" onClick="onsubmit1Form();" alt="��ѯ"></td></tr>
		</table>
    </td>
  </tr>
</table>
</form>


</body>
</html>