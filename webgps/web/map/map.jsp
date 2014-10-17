<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="com.sosgps.wzt.orm.TEnt" %>
<%@ page import="java.util.Hashtable" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
String edition = (String) request.getSession().getAttribute("edition");
String images_path = "../internation/zh_cn/map/";
if(edition.equals("en")){
	images_path = "../internation/en/map/";
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

Hashtable<String,String> mode3_map = (Hashtable)request.getSession().getAttribute("mode3_map");
int mapbar_len = mode3_map.size();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title></title>
<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />

<style type="text/css">
.toolbaricons{ width:200px; height:31px; left:5px; top:5px; position:absolute; z-index:3; padding:0px 0 0 0px;}
.toolbaricons img{ cursor:pointer;}
.toolbaricons_hide{ width:20px; height:31px; left:5px; top:5px; position:absolute; z-index:3;  padding:0px 0 0 0px;}
.toolbaricons_hide img{ cursor:pointer;}
</style>
<script>
/*
.toolbaricons{ width:303px; height:31px; left:5px; top:5px; position:absolute; z-index:3; background:url(../images/map/iconsbg.jpg); padding:5px 0 0 6px;}
.toolbaricons img{ cursor:pointer;}
.toolbaricons_hide{ width:35px; height:31px; left:5px; top:5px; position:absolute; z-index:3; background:url(../images/map/iconsbg.jpg); padding:5px 0 0 6px;}
.toolbaricons_hide img{ cursor:pointer;}
*/
</script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
 <!-- -->
<script type="text/javascript" src="http://app.mapabc.com/apis?&t=flashmap&v=2.3.3&key=79ac15b10ca443502aa0e5bb6acb999bbec64817a5902280de20b8c8bb9aa92c07ec6a1b717f0627"></script>

<!--localhost 
 <script type="text/javascript" src="http://app.mapabc.com/apis?&t=flashmap&v=2.3.3&key=f03346eb3a99be025979045e8fa1a281c51596111ba82a535bc3ec306ccff7aff062850d8233f8cc"></script>
 -->
<script type="text/javascript">
	var centerX = '<%=centerX%>';
	var centerY = '<%=centerY%>';
	var mapZoom = '<%=mapZoom%>';
	var mapbar_len = '<%=mapbar_len%>';
	var mapObj=null;
	var path = '<%=path%>';
	var edition = '<%=edition%>';
	
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
		mapoption.groundLogo = HIDE;//背景水印图片
		mapObj = new MMap("map", mapoption); //地图初始化
		
		//var tl = new MTileLayer(TL_TRAFFIC);  //添加实时交通层
		//mapObj.addTileLayer(tl);
		
		mapObj.addEventListener(mapObj,ZOOM_CHANGED,changeZoom);
		mapObj.addEventListener(mapObj,MAP_MOVE_END,mapMoveEnd);
		mapObj.addEventListener(mapObj,MAP_READY,listVisibleLayerAndPoi);
		mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
		//mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
		//mapObj.addEventListener(mapObj,TIP_CLOSE,poitipclose);
		
		//查询poi点
		//listVisibleLayerAndPoi();
		if(mapbar_len > 0){
			document.getElementById('show_toolbar_div').style.width = 33.6 * mapbar_len;
		}
		hideToolbar();
	}
	
	function MM_swapImgRestore() { //v3.0
  		var i,x,a=document.MM_sr;
  		for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++){
  			//if(x.name==clicktoolbar&&x.name!='Image1'&&x.name!='Image2'&&x.name!='Image3'&&x.name!='Image4'&&x.name!='Image5'&&x.name!='Image6'&&x.name!='Image7'){continue;}
  			//if(x.name==clicktoolbar&&x.name!='Image1'&&x.name!='Image2'&&x.name!='Image4'&&x.name!='Image5'&&x.name!='Image6'&&x.name!='Image7'){continue;}
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
	var toolbarArr=new Array(new toolbarobj('Image1','<%=path%>/internation/zh_cn/map/u1.gif'),new toolbarobj('Image2','<%=path%>/internation/zh_cn/map/u2.gif'),new toolbarobj('Image4','<%=path%>/internation/zh_cn/map/u4.gif'),new toolbarobj('Image5','<%=path%>/internation/zh_cn/map/u5.gif'),new toolbarobj('Image6','<%=path%>/internation/zh_cn/map/u6.gif'),new toolbarobj('Image6','<%=path%>/internation/zh_cn/map/u6.gif'),new toolbarobj('Image8','<%=path%>/internation/zh_cn/map/u8.gif'),new toolbarobj('Image9','<%=path%>/internation/zh_cn/map/u9.gif'),new toolbarobj('Image10','<%=path%>/internation/zh_cn/map/u10.gif'),new toolbarobj('Image12','<%=path%>/internation/zh_cn/map/u12.gif'),new toolbarobj('Image13','<%=path%>/internation/zh_cn/map/u13.gif'),new toolbarobj('Image14','<%=path%>/internation/zh_cn/map/u14.gif'),new toolbarobj('Image15','<%=path%>/internation/zh_cn/map/b14.gif'));
	if(edition == 'en'){
		toolbarArr=new Array(new toolbarobj('Image1','<%=path%>/internation/en/map/u1.gif'),new toolbarobj('Image2','<%=path%>/internation/en/map/u2.gif'),new toolbarobj('Image4','<%=path%>/internation/en/map/u4.gif'),new toolbarobj('Image5','<%=path%>/internation/en/map/u5.gif'),new toolbarobj('Image6','<%=path%>/internation/en/map/u6.gif'),new toolbarobj('Image6','<%=path%>/internation/en/map/u6.gif'),new toolbarobj('Image8','<%=path%>/internation/en/map/u8.gif'),new toolbarobj('Image9','<%=path%>/internation/en/map/u9.gif'),new toolbarobj('Image10','<%=path%>/internation/en/map/u10.gif'),new toolbarobj('Image12','<%=path%>/internation/en/map/u12.gif'),new toolbarobj('Image13','<%=path%>/internation/en/map/u13.gif'),new toolbarobj('Image14','<%=path%>/internation/zh_cn/map/u14.gif'),new toolbarobj('Image15','<%=path%>/internation/zh_cn/map/u14.gif'));
	}
	
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
				if(tmp){
					tmp.src=toolbarArr[i].src;
				}
			}
		}
		
	}
	//隐藏工具条
function hideToolbar(){
	document.getElementById("show_toolbar_div").style.display="none";
	document.getElementById("hide_toolbar_div").style.display="";
}
//显示工具条
function showToolbar(){
	document.getElementById("show_toolbar_div").style.display="";
	document.getElementById("hide_toolbar_div").style.display="none";
}
var tl = new MTileLayer(TL_TRAFFIC);  //添加实时交通层
//实时路况
function addTileLayerTraffic(){
		mapObj.addTileLayer(tl);
		document.getElementById("addtmc").style.display="none";
		document.getElementById("removetmc").style.display="";
}
//删除实时交通层    
function removeTrafficLayer(){   
  mapObj.removeTileLayer(TL_TRAFFIC); 
  document.getElementById("addtmc").style.display="";
  document.getElementById("removetmc").style.display="none";
}  

</script>
<!-- 定时画点 -->
<script type="text/javascript" src="<%=path%>/map/intervaltp.js"></script>
<script type="text/javascript" src="<%=path%>/map/mapv2util.js"></script>
<script src="<%=path%>/map/draw.js" type="text/javascript"></script>
<script src="<%=path%>/map/poi.js" type="text/javascript"></script>
<script src="<%=path%>/main/treeRightClickFun.js" type="text/javascript"></script>

<script type="text/javascript" src="<%=path%>/internation/<%=edition%>/lang.js"></script>

</head>
<body onload="mapInit();" style="margin:0 0 0 0;" scroll="no" >
<div id="map" style="width: 100%; height: 100%"></div>

<%
boolean f_flag = false;
if(mapbar_len > 0){
%>

<div nowrap class="toolbaricons" id="show_toolbar_div" style="display:">
<table width="100%" border="0" cellspacing="1" cellpadding="0" bgcolor="#9CADF1">
<tr>
 <!-- 
<div id="show_toolbar_div" style="width: 100px; height: 20px; position:absolute;top:0px;left:80px;">
<table width="100%" border="0" cellspacing="1" cellpadding="0" bgcolor="#9CADF1">
<tr>
 -->
	<% 
		String zoomin = (String)mode3_map.get("zoomin");
		if(zoomin != null && zoomin.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:setZoomIn()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image1','','"+images_path+"b1.gif',1)\"><img title=\"放大\"  name=\"Image1\" border=\"0\" src=\""+images_path+"u1.gif\" onClick=\"ctoolbar('Image1')\"></a></td>");
		}

		String zoomout = (String)mode3_map.get("zoomout");
		if(zoomout != null && zoomin.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:setZoomOut()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image2','','"+images_path+"b2.gif',1)\"><img  title=\"缩小\" name=\"Image2\" border=\"0\" src=\""+images_path+"u2.gif\" onClick=\"ctoolbar('Image2')\"></a></td>");
		}

		String ceju = (String)mode3_map.get("ceju");
		if(ceju != null && ceju.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:rulerByMouseTool()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image6','','"+images_path+"b6.gif',1)\"><img  title=\"测距\" name=\"Image6\" border=\"0\" src=\""+images_path+"u6.gif\" onClick=\"ctoolbar('Image6')\"></a></td>");
		}

		String ce_area = (String)mode3_map.get("ce_area");
		if(ce_area != null && ce_area.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:compute_areaBymouseTool()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image13','','"+images_path+"b13.gif',1)\"><img  title=\"测面积\" name=\"Image13\" border=\"0\" src=\""+images_path+"u13.gif\" onClick=\"ctoolbar('Image13')\"></a></td>");
		}
		
		String clear = (String)mode3_map.get("clear");
		if(clear != null && clear.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:clear()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image4','','"+images_path+"b4.gif',1)\"><img  title=\"清除\" name=\"Image4\" border=\"0\" src=\""+images_path+"u4.gif\" onClick=\"ctoolbar('Image4')\"></a></td>");
		}

		String print = (String)mode3_map.get("print");
		if(print != null && print.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:window.print();\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image5','','"+images_path+"b5.gif',1)\"><img  title=\"打印\" name=\"Image5\" border=\"0\" src=\""+images_path+"u5.gif\" onClick=\"ctoolbar('Image5')\"></a></td>");
		}

		String areaalarm = (String)mode3_map.get("areaalarm");
		if(areaalarm != null && areaalarm.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:parent.map_areaalarm_img()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image8','','"+images_path+"b8.gif',1)\"><img  title=\"电子围栏\" name=\"Image8\" border=\"0\" src=\""+images_path+"u8.gif\" onClick=\"ctoolbar('Image8')\"></a></td>");
		}

		String layer = (String)mode3_map.get("layer");
		if(layer != null && layer.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:parent.map_layer_img()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image9','','"+images_path+"b9.gif',1)\"><img  title=\"图层\" name=\"Image9\" border=\"0\" src=\""+images_path+"u9.gif\" onClick=\"ctoolbar('Image9')\"></a></td>");
		}

		String mapset = (String)mode3_map.get("mapset");
		if(mapset != null && mapset.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:parent.map_mapset_img()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image10','','"+images_path+"b10.gif',1)\"><img  title=\"地图视野设置\" name=\"Image10\" border=\"0\" src=\""+images_path+"u10.gif\" onClick=\"ctoolbar('Image10')\"></a></td>");

		}

		String realtime_track = (String)mode3_map.get("realtime_track");
		if(realtime_track != null && realtime_track.length() > 0){
			f_flag = true;
			out.println("<td><a href=\"javascript:parent.realtime_track_img()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image12','','"+images_path+"b12.gif',1)\"><img  title=\"实时追踪\" name=\"Image12\" border=\"0\" src=\""+images_path+"u12.gif\" onClick=\"ctoolbar('Image12')\"></a></td>");

		}
		
		String carCurrentDirection = (String)mode3_map.get("car_current_direction");
		if(carCurrentDirection != null && carCurrentDirection.length() > 0){
			f_flag = true;
			out.println("<td id='openCarCurrentDirection' style='display:'><a href=\"javascript:parent.carCurrentDirectionImgClick()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image16','','"+images_path+"b16.png',1)\"><img  title=\"开启定位\" name=\"Image16\" border=\"0\" src=\""+images_path+"u16.png\" onClick=\"ctoolbar('Image16')\"></a></td>");
			out.println("<td id='closeCarCurrentDirection' style='display:none'><a href=\"javascript:parent.StopCarCurrentDirection()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image17','','"+images_path+"b16.png',1)\"><img  title=\"关闭定位\" name=\"Image17\" border=\"0\" src=\""+images_path+"u16.png\" onClick=\"ctoolbar('Image17')\"></a></td>");

		}
		
		String tmc = (String)mode3_map.get("tmc");
		if(tmc != null && tmc.length() > 0 ){
			f_flag = true;
			out.println("<td id='addtmc' style='display:'><a href=\"javascript:addTileLayerTraffic()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image14','','"+images_path+"b14.gif',1)\"><img  title=\"开启路况\" name=\"Image14\" border=\"0\" src=\""+images_path+"u14.gif\" onClick=\"ctoolbar('Image14')\"></a></td>");
			out.println("<td id='removetmc'  style='display:none'><a href=\"javascript:removeTrafficLayer()\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image15','','"+images_path+"b14.gif',1)\"><img  title=\"关闭路况\" name=\"Image15\" border=\"0\" src=\""+images_path+"u14.gif\" onClick=\"ctoolbar('Image15')\"></a></td>");
		}

}
%>

		<!-- 
		<a href="javascript:fullmap();"><img title="缩放" src=""+images_path+"icon5.jpg" onMouseover="this.src='"+images_path+"icon5_hover.jpg'" onMouseout="this.src='"+images_path+"icon5.jpg'" /></a>
		 -->
		 <%
		 if(f_flag){
		 	out.println("<td><a href=\"javascript:hideToolbar();\"><img title=\"隐藏工具条\" src=\"../images/map/icon11.gif\" onMouseover=\"this.src='../images/map/icon11_hover.gif'\" onMouseout=\"this.src='../images/map/icon11.gif'\" /></a></td>");
		 }
		 %>
		<!-- 
	</tr>
</table>
 -->
 	</tr>
</table>
</div>

	<div class="toolbaricons_hide" id="hide_toolbar_div" style="display:none">
		<a href="javascript:showToolbar();"><img src="../images/map/icon11.gif" onMouseover="this.src='../images/map/icon11_hover.gif'" onMouseout="this.src='../images/map/icon11.gif'" /></a>
	</div>
	
</body>
</html>
