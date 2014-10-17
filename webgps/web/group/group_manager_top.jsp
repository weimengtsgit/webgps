<%@ page contentType="text/html;charset=GBK"%>
<HTML>
<HEAD>
<TITLE> New Document </TITLE>
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
</HEAD>

<BODY bgcolor="#f3f3f3" style="overflow:hidden">
<table width="100%" height="30" border=0 cellspacing=0 cellpadding=0 align=center>
<tr class="top">
<td align="right">
	<span class="spanbutton_normal" title="" style="cursor:hand;" onmouseover="this.className='spanbutton_over'" onmousedown="this.className='spanbutton_down'" onmouseup="this.className='spanbutton_up'" onmouseout="this.className='spanbutton_normal'"><a href="javascript:parent.window.close()"><img src="../images/reload.gif" align="adsmiddle" border="0">关闭</a>
	</span>
</td>
</tr>
</table>

<table width="100%" border=0 cellspacing=0 cellpadding=0 align=center>
	<tr>
		<td class="baobiao_title">组部门管理</td>
	</tr>
</table>
</BODY>
</HTML>
