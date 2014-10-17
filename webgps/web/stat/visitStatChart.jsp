<%@ page contentType="text/html;charset=GBK"%>
<%
String graphURL = (String)request.getSession().getAttribute("graphURL");
//System.out.println("graphURL="+graphURL);
String filename = (String)request.getSession().getAttribute("filename");
//System.out.println("filename="+filename);
%>
<img src="<%=graphURL%>" border="0" usemap="#<%=filename%>">
