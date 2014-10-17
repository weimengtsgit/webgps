<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="../css/selStyle.css" type="text/css">
<script language="javascript" src="../map/js/citycoord.js"></script>

<title>周边查找</title>
</head>
<body>

<script language="javaScript">
//提交
function onsubmit1Form(){
	var subForm =document.getElementById("nearbyfind");
	var selcity0 = document.getElementById("citylist0");
	var citycode0= selcity0.value;
	if (citycode0==""){alert("请选择城市");return;}
	subForm.item("param7").value =citycode0;
	subForm.submit();
}

//定位到城市
function zoomToCity(cityCode){
	var cityCoord=	getCityCenter(cityCode);
	if (cityCoord==null || cityCoord=='')return;
	var cityxy = cityCoord.split(",");
	var x = cityxy[0];
	var y = cityxy[1];
	parent.ifrmap.setMapCenter(x,y);
}

</script>


<form id="nearbyfind" name="nearbyfind" action="nearbyrequest.jsp"  method="post" target="_self">
<input type="hidden" id="param4" name="param4" value="1">
<input type="hidden" id="param5" name="param5" value="1">
<input type="hidden" id="param6" name="param6" value="6">
<input type="hidden" id="param7" name="param7" value="">
<table width="100%" border="0" bgcolor="#759CB0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td>
		<table  width="100%" border="0" cellspacing="1" cellpadding="1">
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">在城市</td>
		      <td>
		      <script language="javascript" src="../map/js/citylist.js"></script>
		      
			    <script language="javascript">
			   	WriteCitylist('citylist0','zoomToCity(this.value)','010');
			    </script>
			  </td>
			</tr>
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">查找</td><td><input id="param1" name="param1" type="text" style="width:110px" value=""><font class="huixian">如:中关村</font></td></tr>
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">周边</td><td><input id="param2" name="param2" type="text" value="1000" style="width:110px" onKeyPress="inputNumberOnly()">米</td></tr>
		      <tr bgcolor="#FFFFFF"><td bgcolor="#eeeeee">的</td><td><input id="param3" name="param3" type="text"  style="width:110px" value=""><font class="bitian">*必填</font><font class="huixian"> 如:银行</font></td></tr>
		      <tr bgcolor="#FFFFFF"><td colspan="2" align="right"><img style="cursor:hand" src="../images/query.gif" onClick="onsubmit1Form();" alt="查询"></td></tr>
		</table>
    </td>
  </tr>
</table>
</form>

</body>
</html>