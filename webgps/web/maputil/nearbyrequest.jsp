<%@ page contentType="text/html; charset=GBK" %>
<%@page import="com.sosgps.wzt.util.CharTools"%>
<html>
<head>
<link rel="stylesheet" href="../css/selStyle.css" type="text/css">
<script language="javascript" src="../map/js/mapServer.js"></script>

</head>
<body bgcolor="#ffffff">
<%
	StringBuffer X = new StringBuffer();
	StringBuffer Y = new StringBuffer();
	StringBuffer NAME = new StringBuffer();
	StringBuffer ADDRESS = new StringBuffer();
	StringBuffer TEL = new StringBuffer();
	StringBuffer POIID = new StringBuffer();
 //   String param1=CharTools.getStr(request.getParameter("param1"));
 //   String param2=CharTools.getStr(request.getParameter("param2"));
 //   String param3=CharTools.getStr(request.getParameter("param3"));
//    String param4=CharTools.getStr(request.getParameter("param4"));
 //   String param5=CharTools.getStr(request.getParameter("param5"));//开始页
//    String param6=CharTools.getStr(request.getParameter("param6"));//结束页
 //   String param7=CharTools.getStr(request.getParameter("param7"));//城市代码
 
 
    String param1=request.getParameter("param1");
    String param2=request.getParameter("param2");
    String param3=request.getParameter("param3");
    String param4=request.getParameter("param4");
    String param5=request.getParameter("param5");//开始页
    String param6=request.getParameter("param6");//结束页
    String param7=request.getParameter("param7");//城市代码   

    
    int batch = Integer.parseInt(param4);
    int sPage = Integer.parseInt(param5);
    int ePage = Integer.parseInt(param6);
    int pageRsCount=7;//每页最多显示8条记录
    int totalPage=0;
	com.mapabc.sis.client.RbBase rb = new com.mapabc.sis.client.RbBase();
	rb.localSearchByName(param7,param1,param3,param2,pageRsCount,1,batch);//
	com.mapabc.sis.client.PoiBeans pois = new com.mapabc.sis.client.PoiBeans();
	com.mapabc.sis.client.bean.PoiBean poi;
	pois = (com.mapabc.sis.client.PoiBeans)rb.getResponseBuilder();
	if(pois==null){
		out.println("在 <font color='red'>"+param1+"</font> 附近  <font color='red'>"+param2+"</font> 米内没有找到任何 <font color='red'>"+param3+" </font>");
		out.println("<table>");
		out.println("<tr><td colspan='2'>建议:</td>");
		out.println("</tr>");
		out.println("<tr><td></td><td>1.检查是否拼写错误</td></tr>");
		out.println("<tr><td></td><td>2.扩大查询范围</td></tr></table>");
	}else{
		int totalCount = pois.getCount();
        out.println("<font color='blue' style='cursor:hand' onclick=\"window.location='nearbysearch.jsp'\">返回</font>");
	if (totalCount<=0){
		out.println("在 <font color='red'>"+param1+"</font> 附近  <font color='red'>"+param2+"</font> 米内没有找到任何 <font color='red'>"+param3+" </font>");
		out.println("<table>");
		out.println("<tr><td colspan='2'>建议:</td>");
		out.println("</tr>");
		out.println("<tr><td></td><td>1.检查是否拼写错误</td></tr>");
		out.println("<tr><td></td><td>2.扩大查询范围</td></tr></table>");
	}else{
		out.println("在 <font color='red'>"+param1+"</font> 附近  <font color='red'>"+param2+"</font> 米内找到 <font color='red'>"+param3+"  "+totalCount+" </font>个");
	}
	int record = pois.getRecord();
	String conentStyle=" onmouseover=\"this.style.cursor='hand';this.style.backgroundColor='#F0F0F0'\"";
           conentStyle+=" onmouseout=\"this.style.backgroundColor='#ffffff'\"";
	for(int i=0;i<record;i++){
		String poiId="f"+(i+1);
		poi =pois.getPoi(i);
		out.println("<table "+conentStyle+" onclick=\"parent.ifrmap.zoomIntoXY('"+poi.getX()+"','"+poi.getY()+"')\" >");
		out.println("<tr  ><td colspan='2'><font color='blue'>");
		out.println((i+1)+"."+poi.getName());
		out.println("</font></td></tr>");
		out.println("<tr><td>");
		out.println("地址:"+poi.getAddress());
		out.println("</td></tr>");
		out.println("<tr><td>");
		out.println("电话:"+poi.getTel());
		out.println("</td></tr>");
		out.println("</table>");
		X=X.append(poi.getX());
		Y=Y.append(poi.getY());
		NAME=NAME.append(poi.getName());
		ADDRESS=ADDRESS.append(poi.getAddress());
		TEL=TEL.append(poi.getTel());
		POIID=POIID.append(poiId);
		X=X.append(",");
		Y=Y.append(",");
		NAME=NAME.append(",");
		ADDRESS=ADDRESS.append(",");
		TEL=TEL.append(",");
		POIID=POIID.append(",");
	}
	if (totalCount>0){
		out.println("<table><tr>");
		if (totalCount<=pageRsCount){
			sPage=1;
			ePage=1;
		}else{
			totalPage= totalCount/pageRsCount;
			if ((totalCount%pageRsCount)>0) totalPage++;
			if (totalPage <=6)ePage=totalPage;
			if (ePage>totalPage)ePage=totalPage;
		}
		if ((batch-1)<=0){
			out.println("<td><font color='blue'>上一页</font></td>");
		}else{
			out.println("<td style='cursor:hand' onclick='replyNearBy(\""+param1+"\",\""+param2+"\",\""+param3+"\",\""+(batch-1)+"\")';><font color='blue'>上一页</font></td>");
		}
		for(int i=sPage;i<=ePage;i++){
          if (i==batch){
			out.println("<td ><font color='red'>["+i+"]</font></td>");
          }else{
			out.println("<td style='cursor:hand' onclick='replyNearBy(\""+param1+"\",\""+param2+"\",\""+param3+"\",\""+i+"\")';><font color='blue'>["+i+"]</font></td>");
          }
		}
		if ((batch+1)>totalPage){
			out.println("<td><font color='blue'>下一页</font></td>");
		}else{
			out.println("<td style='cursor:hand' onclick='replyNearBy(\""+param1+"\",\""+param2+"\",\""+param3+"\",\""+(batch+1)+"\")';><font color='blue'>下一页</font></td>");
		}
        	out.println("</tr></table>");
	}}
%>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript">
function replyNearBy(param1,param2,param3,param4){
	var sPage=1,ePage=6;
	var batch=parseInt(param4);
	if (batch>4){
		sPage=parseInt(batch)-3;
		ePage=parseInt(batch)+2;
	}
	//document.location="nearbyrequest.jsp?param1="+param1+"&param2="+param2+
	//"&param3="+param3+"&param4="+param4+"&param5="+sPage+"&param6="+ePage+"&param7="+'<=param7>';

	document.nearbyfind.param1.value=param1;
	document.nearbyfind.param2.value=param2;
	document.nearbyfind.param3.value=param3;
	document.nearbyfind.param4.value=param4;
	document.nearbyfind.param5.value=sPage;
	document.nearbyfind.param6.value=ePage;
	document.nearbyfind.param7.value='<%=param7%>';
	document.nearbyfind.submit();

}
</SCRIPT>

<SCRIPT LANGUAGE="javaScript">
var xs ='<%=X%>';
var ys ='<%=Y%>';
var names ='<%=NAME%>';
var address ='<%=ADDRESS%>';
var tel ='<%=TEL%>';
var poiID ='<%=POIID%>';
if ( xs!=null&& xs!=""){
var xArray = xs.split(",");
var yArray = ys.split(",");
var nArray =names.split(",");
var aArray = address.split(",");
var tArray = tel.split(",");
var pArray =poiID.split(",");
for(var i=0;i<xArray.length;i++){
if (xArray[i]!=null && xArray[i]!=""){
	//addFeaturePoiToMap(pArray[i],xArray[i],yArray[i],nArray[i],aArray[i],tArray[i]);
	parent.ifrmap.addSearchPoi(pArray[i],xArray[i],yArray[i],nArray[i],aArray[i],tArray[i]);
	
}
}
//zoomToFindFeature(poiID);
}
</SCRIPT>

<form id="nearbyfind" name="nearbyfind" action="nearbyrequest.jsp"  method="post" target="_self">
<input type="hidden" id="param1" name="param1" value="">
<input type="hidden" id="param2" name="param2" value="">
<input type="hidden" id="param3" name="param3" value="">
<input type="hidden" id="param4" name="param4" value="1">
<input type="hidden" id="param5" name="param5" value="1">
<input type="hidden" id="param6" name="param6" value="6">
<input type="hidden" id="param7" name="param7" value="010">
</form>
</body>
</html>
