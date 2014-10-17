<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="com.sosgps.wzt.orm.TEnt" %>

<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
TEnt ent = user.getEnt();
String centerX = "116.397428";
String centerY = "39.90923";
String mapZoom = "4";

if(ent != null){
	if((ent.getCenterX()!=null && ent.getCenterX().length()>0)&&(ent.getCenterX()!=null && ent.getCenterX().length()>0)){
		centerX = ent.getCenterX();
		centerY = ent.getCenterY();
	}
	if(ent.getMapZoom()!=null){
		mapZoom = ent.getMapZoom()+"";
	}
}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title></title>

<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>


<script type="text/javascript" src="http://app.mapabc.com/apis?&t=flashmap&v=2.3.3&key=40d3fc2416c6fcef4e969e3556657f0e7eae3ddcabc877093b33b6301625d14d43a4346522ffcca5"></script>
 <!--
 <script type="text/javascript" src="http://app.mapabc.com/apis?&t=flashmap&v=2.3.3&key=f03346eb3a99be025979045e8fa1a281c51596111ba82a535bc3ec306ccff7aff062850d8233f8cc"></script>
  -->
<script type="text/javascript">
	var centerX = '<%=centerX%>';
	var centerY = '<%=centerY%>';
	var mapZoom = '<%=mapZoom%>';
	var mapObj=null;
	var path = '<%=path%>';
	
	function  mapInit() {
	var mapoption = new MMapOptions();
	mapoption.zoom = mapZoom;//设置地图zoom级别
	mapoption.center=new MLngLat(centerX,centerY);
	//mapoption.toolbar=DEFAULT; //设置工具条
	mapoption.toolbarPos=new MPoint(0,0);
	mapoption.overviewMap = MINIMIZE; //设置鹰眼
	mapoption.mapComButton= HIDE;//隐藏商户
	mapoption.isCongruence=true;
	mapoption.returnCoordType=COORD_TYPE_OFFSET;
	mapObj = new MMap("map", mapoption); //地图初始化
	
	
	}
	
	
	function MM_swapImgRestore() { //v3.0
  		var i,x,a=document.MM_sr;
  		for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++){
  			//if(x.name==clicktoolbar&&x.name!='Image1'&&x.name!='Image2'&&x.name!='Image3'&&x.name!='Image4'&&x.name!='Image5'&&x.name!='Image6'&&x.name!='Image7'){continue;}
  			if(x.name==clicktoolbar&&x.name!='Image1'&&x.name!='Image2'&&x.name!='Image4'&&x.name!='Image5'&&x.name!='Image6'&&x.name!='Image7'){continue;}
  			x.src=x.oSrc;
  		}
	}
	
	function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments;document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}
	
	function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&frames.length) {
	    d=frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}
	
	//var toolbarArr=new Array(new toolbarobj('Image1','<!%=path%>/images/map/b1.gif'),new toolbarobj('Image2','<!%=path%>/images/map/b2.gif'),new toolbarobj('Image3','<!%=path%>/images/map/b3.gif'),new toolbarobj('Image4','<!%=path%>/images/map/b4.gif'),new toolbarobj('Image5','<!%=path%>/images/map/b5.gif'),new toolbarobj('Image6','<!%=path%>/images/map/b6.gif'),new toolbarobj('Image7','<!%=path%>/images/map/b7.gif'));
	var toolbarArr=new Array(new toolbarobj('Image1','<%=path%>/images/map/b1.gif'),new toolbarobj('Image2','<%=path%>/images/map/b2.gif'),new toolbarobj('Image4','<%=path%>/images/map/b4.gif'),new toolbarobj('Image5','<%=path%>/images/map/b5.gif'),new toolbarobj('Image6','<%=path%>/images/map/b6.gif'),new toolbarobj('Image7','<%=path%>/images/map/b7.gif'));
	
	var clicktoolbar;
	function toolbarobj(name,src){
		this.name=name;
		this.src=src;
	}
	
	function ctoolbar(name){
		clicktoolbar=name;
		for(var i=0;i<toolbarArr.length;i++){
			if(toolbarArr[i].name==name){
			}else{
				var tmp=document.getElementById(toolbarArr[i].name);
				tmp.src=toolbarArr[i].src;
			}
		}
		
	}
	
</script>
<script type="text/javascript" src="mapv2util.js"></script>
<script src="<%=path%>/map/draw.js" type="text/javascript"></script>
<script src="<%=path%>/map/poi.js" type="text/javascript"></script>

</head>
<body onload="mapInit();" style="margin:0 0 0 0;" scroll="no" >
<div id="map" style="width: 100%; height: 100%"></div>
<div style="width: 100px; height: 20px; position:absolute;top:0px;left:80px;">
<table width="100%" border="0" cellspacing="1" cellpadding="0" bgcolor="#9CADF1">
<tr>
<!-- 
<td ><a href="javascript:resetControls()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image3','','<!%=path%>/images/map/u3.gif',1)"><img name="Image3" border="0" src="<!%=path%>/images/map/b3.gif" onClick="ctoolbar('Image3')"></a></td>
 -->
<td ><a href="javascript:setZoomIn()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1','','<%=path%>/images/map/u1.gif',1)"><img name="Image1" border="0" src="<%=path%>/images/map/b1.gif" onClick="ctoolbar('Image1')"></a></td>
<td ><a href="javascript:setZoomOut()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image2','','<%=path%>/images/map/u2.gif',1)"><img name="Image2" border="0" src="<%=path%>/images/map/b2.gif" onClick="ctoolbar('Image2')"></a></td>

<td ><a href="javascript:rulerByMouseTool()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image6','','<%=path%>/images/map/u6.gif',1)"><img name="Image6" border="0" src="<%=path%>/images/map/b6.gif" onClick="ctoolbar('Image6')"></a></td>
<td ><a href="javascript:clear()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image4','','<%=path%>/images/map/u4.gif',1)"><img name="Image4" border="0" src="<%=path%>/images/map/b4.gif" onClick="ctoolbar('Image4')"></a></td>
<td ><a href="javascript:window.print();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image5','','<%=path%>/images/map/u5.gif',1)"><img name="Image5" border="0" src="<%=path%>/images/map/b5.gif" onClick="ctoolbar('Image5')"></a></td>
<td ><a href="javascript:fullmap();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image7','','<%=path%>/images/map/u7.gif',1)"><img name="Image7" border="0" src="<%=path%>/images/map/b7.gif" onClick="ctoolbar('Image7')"></a></td>

</tr>
</table>
</div>
</body>
</html>
