<%@page pageEncoding="GBK" contentType="text/html; charset=GBK"%>
<%@ page
	import="java.util.*,com.sosgps.wzt.manage.util.*,com.sosgps.wzt.orm.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<head>
<title>企业用户列表</title>
<meta http-equiv='Content-Type' content='text/html; charset=GB2312'>
<script language=javascript src='../pagelist.js'></script>
<script language=javascript src='../common.js'></script>
<script language=JavaScript src='../DateControl.js'></script>
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
%></head>
<script>
//刷新
function reflush(){
	var paramN = document.getElementById("paramN").value;
	var paramV = document.getElementById("paramV").value;
	var vagCheckbox = document.getElementById("vag");
	var vag="n";
	if(vagCheckbox.checked){
		vag="y";
	}
	document.forms[0].pageNo.value=${userList.pageNo};
	document.forms[0].pageSize.value=${userList.pageSize};
	document.forms[0].paramName.value=paramN;
	document.forms[0].paramValue.value=paramV;
	document.forms[0].vague.value=vag;
	document.forms[0].submit();
}
//选择页数
function selectPageNo(pageNo){
	document.forms[0].pageNo.value=pageNo;
	document.forms[0].pageSize.value=${userList.pageSize};
	document.forms[0].submit();
}
//搜索
function search(){
	var paramN = document.getElementById("paramN").value;
	var paramV = document.getElementById("paramV").value;
	var vagCheckbox = document.getElementById("vag");
	var vag="";
	if(vagCheckbox.checked){
		vag="y";
	}
	document.forms[0].pageNo.value=1;
	document.forms[0].pageSize.value=${userList.pageSize};
	document.forms[0].paramName.value=paramN;
	document.forms[0].paramValue.value=paramV;
	document.forms[0].vague.value=vag;
	document.forms[0].submit();
}

</script>

<body>

<table width=100% border=0 cellspacing=0 cellpadding=0 align=center height=30>
<tr>
<td align=center>
<table width="100%" border=0 cellspacing=0>
	<tr class="top">
		<td>
			<span class=spanbutton_normal title="" style=cursor:hand onmouseover="this.className='spanbutton_over'" onmousedown="this.className='spanbutton_down'" onmouseup="this.className='spanbutton_up'" onmouseout="this.className='spanbutton_normal'">
				<a href='javascript:reflush();'><img src=../images/refresh.gif align=adsmiddle border=0>刷新</a>
			</span>
		</td>
	</tr>
</table>
</td></tr></table>

<table width=100% border=0 cellspacing=0 cellpadding=2 align=center>
	<tr>
		<td class=baobiao_title>企业用户列表</td>
	</tr>
	
</table>
<br>
<table width="100%" border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
<td>
<select id="paramN" name="paramN">
	<option value="t.userAccount" <c:if test="${paramName=='t.userAccount'}">selected</c:if>>企业用户名称</option>
</select>
<input type="text" id="paramV" name="paramV" class="textfield" size=30 value="${paramValue}">
模糊匹配：<input type="checkbox" name="vag" id="vag" <c:if test="${empty vague || vague=='y'}">checked</c:if>>
<input type=button class=button value='查询' onclick="search()">
</td>
</tr>
</table>
<br>
<form name="frm" action="<%= request.getContextPath() %>/manage/termGroupManage.do?actionMethod=listUser&entCode=${entCode}" method="post">
	<input type="hidden" name="pageNo">
	<input type="hidden" name="pageSize">
	<input type="hidden" name="paramName" value="${paramName}">
	<input type="hidden" name="paramValue" value="${paramValue}">
	<input type="hidden" name="vague" value="${vague}">
<table width=65% border=0 cellspacing=3 cellpadding=0 align=center>
	<tr>
		<td>记录条数 ${userList.totalCount}</td>
		<td align=right><c:if test="${userList.totalPages>0}">第${userList.pageNo}页</c:if> 共${userList.totalPages}页
		<c:if test="${userList.hasPre}">
			<a href="javascript:void(0);" onclick="selectPageNo('1')">首页</a>
			<a href="javascript:void(0);" onclick="selectPageNo('${userList.prePage}')">上页</a>
		</c:if>
		<c:if test="${userList.hasNext}">
		<a href="javascript:void(0);" onclick="selectPageNo('${userList.nextPage}')">下页</a>
		<a href="javascript:void(0);" onclick="selectPageNo('${userList.totalPages}')">尾页</a>
		</c:if>
		<c:if test="${userList.totalPages>0}">
		查看第 
		<select name=selpage1 onchange='selectPageNo(selpage1.value);'>
			<c:forEach var="i" begin="1" end="${userList.totalPages}" step="1">
				<option value="${i}" 
				<c:if test="${i==userList.pageNo}">selected</c:if>
				>${i}</option>
			</c:forEach>
		</select>
		 页 
		 </c:if>
		</td>
	</tr>
</table>

<table bgcolor=#f3f3f3 width=65% border=1 cellspacing=0 cellpadding=1 bordercolorlight="#999999" bordercolordark="#FFFFFF" align=center class="conlist">
	<tr class="list">
		<td width=120 align=center>用户名称</td>
	</tr>

	<c:forEach var="user" items="${userList.result}" varStatus="status">
		<tr onMouseOver="javascript:this.bgColor='#ffffff';" onMouseOut="javascript:this.bgColor='#f3f3f3';">
			<td><a href="javascript:openwindow('<%= request.getContextPath() %>/manage/termGroupManage.do?actionMethod=userTermGroup&entCode=${user.empCode}&userId=${user.id}','_blank',800,500)">${user.userAccount}</a></td>
		</tr>
	</c:forEach>
</table>

<table width=65% border=0 cellspacing=3 cellpadding=0 align=center>
	<tr>
		<td align=right><c:if test="${userList.totalPages>0}">第${userList.pageNo}页</c:if> 共${userList.totalPages}页
		<c:if test="${userList.hasPre}">
			<a href="javascript:void(0);" onclick="selectPageNo('1')">首页</a>
			<a href="javascript:void(0);" onclick="selectPageNo('${userList.prePage}')">上页</a>
		</c:if>
		<c:if test="${userList.hasNext}">
		<a href="javascript:void(0);" onclick="selectPageNo('${userList.nextPage}')">下页</a>
		<a href="javascript:void(0);" onclick="selectPageNo('${userList.totalPages}')">尾页</a>
		</c:if>
		<c:if test="${userList.totalPages>0}">
		查看第 
		<select name=selpage2 onchange='selectPageNo(selpage2.value);'>
			<c:forEach var="i" begin="1" end="${userList.totalPages}" step="1">
				<option value="${i}" 
				<c:if test="${i==userList.pageNo}">selected</c:if>
				>${i}</option>
			</c:forEach>
		</select>
		 页 
		 </c:if>
		</td>
	</tr>
</table>
</form>

</body>