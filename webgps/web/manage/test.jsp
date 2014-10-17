<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*,com.sosgps.wzt.system.common.*,com.sosgps.wzt.orm.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<title></title>
</head>
<body>
<% 
TUser user = new TUser();
TEnt ent = new TEnt();//企业信息
ent.setEntName("图盟科技");
user.setId(1L);
user.setEmpCode("empRoot");
TRole role = new TRole();
user.setRole(role);
//Set rurs = new HashSet();
//RefUserRole rur = new RefUserRole();
//rurs.add(rur);
//user.setRefUserRoles(rurs);
UserInfo userInfo = new UserInfo(user,ent);
session.setAttribute("userInfo",userInfo); 
%>
<a href="<%= request.getContextPath() %>/manage/viewTermGroup.do?actionMethod=viewUserTermGroup">用户终端树测试</a><br>
<a href="<%= request.getContextPath() %>/manage/viewTermGroup.do?actionMethod=viewEntTermGroup">企业终端树测试</a><br>
<a href="<%= request.getContextPath() %>/manage/entManage.do?actionMethod=list&pageNo=1&pageSize=2">企业列表测试</a><br>
<a href="<%= request.getContextPath() %>/manage/entManage.do?actionMethod=findById&entCode=1">查看企业测试</a><br>
<a href="<%= request.getContextPath() %>/manage/Ent_Add.jsp">新增企业测试</a><br>

<a href="<%= request.getContextPath()%>/manage/termGroupManage.do?actionMethod=initUserTermGroupForEnt">用户可见终端组管理测试</a><br>
<a href="<%= request.getContextPath() %>/manage/termGroupManage.do?actionMethod=initTerminalInGroup">终端/终端组管理</a><br>
</body>
</html>
