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
TEnt ent = new TEnt();//��ҵ��Ϣ
ent.setEntName("ͼ�˿Ƽ�");
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
<a href="<%= request.getContextPath() %>/manage/viewTermGroup.do?actionMethod=viewUserTermGroup">�û��ն�������</a><br>
<a href="<%= request.getContextPath() %>/manage/viewTermGroup.do?actionMethod=viewEntTermGroup">��ҵ�ն�������</a><br>
<a href="<%= request.getContextPath() %>/manage/entManage.do?actionMethod=list&pageNo=1&pageSize=2">��ҵ�б����</a><br>
<a href="<%= request.getContextPath() %>/manage/entManage.do?actionMethod=findById&entCode=1">�鿴��ҵ����</a><br>
<a href="<%= request.getContextPath() %>/manage/Ent_Add.jsp">������ҵ����</a><br>

<a href="<%= request.getContextPath()%>/manage/termGroupManage.do?actionMethod=initUserTermGroupForEnt">�û��ɼ��ն���������</a><br>
<a href="<%= request.getContextPath() %>/manage/termGroupManage.do?actionMethod=initTerminalInGroup">�ն�/�ն������</a><br>
</body>
</html>
