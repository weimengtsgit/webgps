<%@ page contentType="text/html;charset=GBK"%>
<HTML>
<HEAD>
<TITLE>组部门管理</TITLE>
<script type="text/javascript">
var valuePage="group_manager.jsp";
</script>
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
%></HEAD>

<frameset name ="group_manager_main" id="group_manager_main" rows="15%,*" frameborder="no" border="0" framespacing="0">
  <frame src="group_manager_top.jsp" frameborder="no" border="0" framespacing="0">
  <frameset name ="group_manager" id="group_manager" cols="25%,75%"  frameborder="no" border="0" framespacing="0">
	<frame name="group_left" id="group_left" scrolling ="auto" frameborder="0" src="/group/group.do?method=getTTargetGroup" noresize="noresize" />
	<frame name="group_right" id="group_right"  scrolling ="no" src="group_manager_right.jsp"noresize="noresize">
  </frameset>
</frameset>

</HTML>
