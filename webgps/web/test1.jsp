<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*" %>
<%
    String poiName =  request.getParameter("poiName");
    String poiAddress = request.getParameter("poiAddress"); 
    out.println();
    out.println("poiName:"+poiName);
    out.println("poiAddress:"+poiAddress);
    out.close();   
%>

