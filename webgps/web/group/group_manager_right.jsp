<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String result=(String)request.getAttribute("result");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>组部门管理</title>
<%
	if(session.getAttribute("t11thngame")==null){
%>
<link rel="stylesheet" href="../css/sap.css" type="text/css">
<link rel="StyleSheet" href="../css/dtree.css" type="text/css" />
<%
}else{	
%>
<link rel="StyleSheet" href="../t11thngame/css/main.css" type="text/css" />
<%
}
%>
<script type="text/javascript" src="../js/dtree.js"></script>
<script type="text/javascript" language="JavaScript">
var result='<%=result%>';
function ctlName(f){
	if(f=='add'){
		var childGroupName=document.getElementById("childGroupName").value;
		 
		if(childGroupName==''||childGroupName.length<=0){
			alert("请输入下属部门名称");
			return;
		}
	}
	document.getElementById("ctl").value=f;
	document.form1.submit();
}

function alertResult(){
	if(result!='null'&&result!=''&&result.length>0){
		alert(result);
		parent.group_left.location.reload();
	}
}
</script>
</head>
<body bgcolor=#f3f3f3 onLoad="alertResult()">
<form action="../group/group.do?method=tTargetGroupCtl" method="POST" name="form1">
<input type="hidden" name="ent" id="endcode" value="empRoot">

<table bgcolor=#f3f3f3 width=100% border=1 cellspacing=0 cellpadding=1 bordercolorlight="#999999" bordercolordark="#FFFFFF" align=center class="conlist">
	<tr>
      <td width="20%" height="27">部门名称
        <input type="hidden" name="groupId">
	    <input type="hidden" name="ctl" id="ctl">    
	  </td>
      <td width="40%">
        <input type="text" name="groupName" size="20"  disabled>
        <input type="button" name="modify" class="button" value="修改" onClick="ctlName('modify')" disabled> &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
      <td width="20%"><input type="button" name="del" class="button" value="删除" onClick="ctlName('del')" disabled></td>
  </tr>
  <tr>
    <td height="20">增加下属部门</td>
    <td>
      <input type="text" name="childGroupName" size="20"  disabled>
      <input type="button" name="add" value="增加" class="button" onClick="ctlName('add')" disabled>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>


</form>
</body>
</html>
