/*************** help交互式导航页面 ***************/
//取得应用路径
function getAppPath(){
	url = window.location.href;
	pathname = window.location.pathname;
	pathname = pathname.substr(1,pathname.length);
	i = pathname.indexOf("/");
	pathname = pathname.substr(0,i);
	re = window.location.protocol + "//" + window.location.host;
	re += "/"+pathname;
	return re;
}

//-------------------------------------------------------------------------------//
 //展现终端调度信息请求div
function sendBaJuMessage(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	

	var divTitle = "信息发送";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">信息发送：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\" valign=\"top\"><input name=\"title\" id=\"title\" class='input1'></input></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<tr></tr>";
  	inner += "<td align=\"right\" valign=\"top\">信息内容：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"textarea\" id=\"msgcont\" class=\"tip_area\"></textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"bj_sendMessage('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//发送短信请求
function bj_sendMessage(re){

	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	 var title = document.getElementById("title").value;
	var content =  document.getElementById("msgcont").value;
	if(title==null||title == ""){
		alert("短信标题不能为空！");
		return;
	}
	if(content==null||content == ""){
		alert("短信内容不能为空！");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200！");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/baju/task.do?method=createMsg&deviceIds="+re+"&content="+content+"&title="+title, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			baju_parseResp(xmlHttp.responseText);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}

//解析短信
function baju_parseResp(oXmlDoc){
 if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	 
	var pl_content =oXmlDoc;
	 
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;
}
 
//-------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------//
 //展现发送目的地请求div
function sendBaJuDestination(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
	var cityHtml = "";
	cityHtml = getCity();
	 
	var divTitle = "目的地发送";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr nowrap>";
  	inner += "<td align=\"right\" nowrap valign=\"top\">标题：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\" valign=\"top\"><input name=\"destitle\" id=\"destitle\" style=\"width:190px; height:18px; \"></input></td>";
  	inner += "</tr>";
  	inner += "<tr></tr>";
  	inner += "<tr nowrap>";
  	inner += "<td align=\"right\" nowrap valign=\"top\">内容：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"textarea\" id=\"descont\" class=\"tip_area\"></textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" nowrap valign=\"top\">手动绘制：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><a class=\"huixian\" href=\"javascript:drawDesPoint();\"> 选择目的地</a></td>";
  	inner += "</tr>";
  	inner += "<tr >";
  	inner += "<td align=\"right\" nowrap valign=\"middle\">查询绘制：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\" valign=\"bottom\">选择城市 "+cityHtml+"<br>目标名称&nbsp;&nbsp;<input name=\"desname\" id=\"desname\" style=\"width:90px; height:18px;\">&nbsp;&nbsp;<input type=\"button\" name=\"button\"  value=\"查询\" onclick='searchDestination()' class=\"btn\"></td>";
  	inner += "</tr>";
  	
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "&nbsp;&nbsp;<h5>信息窗口</h5><ul class=\"content\" id=\"poiSearch_result\"></ul>";
  	inner += "</td>";
  	inner += "</tr>";
  	
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"bj_sendDestination('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//发送目的地请求
function bj_sendDestination(re){

	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	 var title = document.getElementById("destitle").value;
	var content =  document.getElementById("descont").value;
	var xy = coords;
	if(xy==null || xy=="" || xy=="undefined"){
		alert("请选择目的地！");return;
	}
	if(title==null||title == ""){
		alert("标题不能为空！");
		return;
	}
	if(content==null||content == ""){
		alert("内容不能为空！");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200！");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/baju/task.do?method=createDestination&deviceIds="+re+"&content="+content+"&title="+title+"&xy"+xy, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			baju_parseResp(xmlHttp.responseText);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}

//解析 
function baju_parseResp(oXmlDoc){
 if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	 
	var pl_content =oXmlDoc;
	 
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;
}
 
//-------------------------------------------------------------------------------//

//绘制目的地点
 function drawDesPoint(){
 		 
	    mapObj.removeAllOverlays();
	    setDefaultMarkerOption("目的地");
		mapObj.addEventListener(mapObj, ADD_OVERLAY,addBaJuOverlayOver);		 
		var test=mapObj.setCurrentMouseTool(ADD_MARKER);
	 
}

var drawTempObjectID;
//绘制完目的地点的事件响应
function addBaJuOverlayOver(event){
		
		
		var overlay=mapObj.getOverlayById(event.overlayId);
		if( drawTempObjectID!="") 
		    mapObj.removeOverlayById(drawTempObjectID); //删除前一个点
			
		drawTempObjectID=event.overlayId;
		  var xys ;
		  if (overlay.TYPE=="Marker"){
 				 xys =  overlay.lnglat.lngX + ","+overlay.lnglat.latY;
 				fillPointData(null,null,xys);
 			}
 		 
	}
	
	var coords;//全局变量，存储经纬度信息
	function fillPointData(ptype, pradio, pcoords){
		coords = pcoords;
	}
	//设置点样式
	function setDefaultMarkerOption(label){   
	    var tipOption = new MTipOptions();   
	    tipOption.title="点";   
	    tipOption.anchor =  new MPoint(0,0);   //图片锚定点，Point类型   
	    tipOption.content="";  //tip内容   
	  
	    var markerOption = new MMarkerOptions();//点选项   
	    markerOption.label=label; //标注   
	    markerOption.imageUrl="";   
	    markerOption.labelStyle.name = "黑体"; //字体名称   
	    markerOption.labelStyle.size = 16; //字体大小   
	    markerOption.labelStyle.color = 0x222222; //字体颜色   
	    markerOption.labelStyle.bold = true;//是否粗体   
	    markerOption.labelPosition = new MPoint(5,0);   
	    markerOption.isDraggable=false;//是否可以拖动    
	    markerOption.imageAlign=8;//设置图片锚点相对于图片的位置   
	    markerOption.tipOption = tipOption;   
	    markerOption.canShowTip= false;    
	  
	    mapObj.setDefaultMarkerOption(markerOption);   
}   

	//目的地搜索
    function searchDestination(){
    
    	var desname = document.getElementById("desname").value;
    	if (desname==null || desname=="" || desname=="undefined"){
    		alert("请输入名称");return;
    	}
    	var cityCode = document.getElementById("cityCode").value;
    	if (cityCode==null || cityCode=="" || cityCode=="undefined"){
    		alert("请选择城市！");return;
    	}
    	
    	var sis = new MLocalSearch();
		sp = new MLocalSearchOptions();
		
		sis.setCallbackFunction(localSearchCallBack);
 		sis.poiSearchByKeywords(desname,cityCode,sp);
    }
    
    function localSearchCallBack(data){
    	var arr = data;
    	alert(arr.message);  
    	alert(arr.count);
    	for (var i = 0; i < arr.length; i++) 
    	{ 
    		alert(arraData.poilist[i].name);
    	}
 
   }
    
    
//-----------------------------------------------------------------//
	function inputNumberOnly(){
	if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
	|| (window.event.keyCode == 13) || (window.event.keyCode == 46)
	|| (window.event.keyCode == 45))){
		window.event.keyCode = 0 ;
   }	
 }
 
 

//得到选中对象序列号
function getDevids(){
	var re = new Array();
   var objs = getCheckObjects(false);
   s = objs.length;
   for(var i=0;i<s;i++){
   		var obj = objs[i];
   		var realid = objs[i].id.substring(13);
   		if(realid.indexOf("g")!=-1){
   		}else if(realid.indexOf("c")!=-1){
   		}else{
   		   			re[re.length]=realid;
   			 //+" name:"+obj.text+" typecode:"+obj.value;
   		}
   		 
   }
   if (re.length <=0){
   	  alert("请选择终端");
   	  return re;
   	  }
    //alert(re);
   return re;
}

//获得选中的lbs的id
function getLbsDevids(){
	var re = new Array();
   var objs = getCheckObjects(false);
   s = objs.length;
   for(var i=0;i<s;i++){
   		var obj = objs[i];
   		var realid = objs[i].id.substring(13);
   		if(realid.indexOf("g")!=-1){
   		}else if(realid.indexOf("c")!=-1){
   		}else{
   		hd_node = getNodeByRealId(realid);
   		locate_type=hd_node.expend1;
   		
   		if(locate_type==1){
   		alert("请确定没有选中gps终端！")
   		return "";
   		}
   		
   			re[re.length]=realid;
   			 //+" name:"+obj.text+" typecode:"+obj.value;
   		}
   		 
   }
   if (re.length <=0){
   	  alert("请选择终端");
   	  return re;
   	  }
    //alert(re);
   return re;
}

 //下发目的地
function bjSendDestination(){
	var targetids = getDevids();
	if( targetids == "null" || targetids=="" || targetids==undefined || targetids=="undefined"){
 			return;
 	}
 	
 	window.open("../baju/task.do?method=initDestination&deviceIds="+targetids,"newwindow", "height=600, width=1000, top=100, left=200, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
 	
}

 //下发路径
function bjSendRoute(){
	var targetids = getDevids();
	if( targetids == "null" || targetids=="" || targetids==undefined || targetids=="undefined"){
 			return;
 	}
 	
 	window.open("../baju/task.do?method=initRoute&deviceIds="+targetids,"newwindow", "height=600, width=1000, top=100, left=200, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no");
 	
}