var cityTrafficInfos = new Array();//城市路况
var highWayTrafficInfos = new Array();//高速路况
var refrushInterval_roadcondition = 60000; //60秒 页面请求刷新
var roadconditionId;// 城市、高速路况刷新

//清除城市路况标注
function clearCityTrafficInfos(){
	for(var i=0;i<cityTrafficInfos.length;i++){
		mapObj.removeOverlayById(cityTrafficInfos[i]);
	}
	cityTrafficInfos.length=0;
}
//清除高速路况标注
function clearHighWayTrafficInfos(){
	for(var i=0;i<highWayTrafficInfos.length;i++){
		mapObj.removeOverlayById(highWayTrafficInfos[i]);
	}
	highWayTrafficInfos.length=0;
}

//市内、高速路况查询
function roadcondition(){
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/roadcondition/roadCondition.do?method=traffic", true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			//alert(xmlHttp.responseText);
			roadcondition_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
//解析市内、高速路况查询
function roadcondition_parseResp(oXmlDoc){
	//清除城市路况标注
	clearCityTrafficInfos();
	//清除高速路况标注
	clearHighWayTrafficInfos();
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		addMapOptLog("查询路况信息未返回");
		return;
	}
	if(oXmlDoc.getElementsByTagName("roadinfo")!=null && oXmlDoc.getElementsByTagName("roadinfo").length>0){
		var roadinfos = oXmlDoc.getElementsByTagName("roadinfo");
		for(var i=0;i<roadinfos.length;i++){
			var roadinfo = roadinfos[i];
			var info_type = roadinfo.getAttribute("type");
			if(info_type=="city"){
				var id = roadinfo.getElementsByTagName("id")[0].firstChild.nodeValue;
				var spElement = roadinfo.getElementsByTagName("sp")[0].firstChild;
				var sp = spElement!=null?spElement.nodeValue:"";
				var intersection = roadinfo.getElementsByTagName("intersection")[0].firstChild.nodeValue;
				var contents = roadinfo.getElementsByTagName("contents")[0].firstChild.nodeValue;
				var directionElement = roadinfo.getElementsByTagName("direction")[0].firstChild;
				var direction = directionElement!=null?directionElement.nodeValue:"";
				var time = roadinfo.getElementsByTagName("time")[0].firstChild.nodeValue;
				var longitude = roadinfo.getElementsByTagName("longitude")[0].firstChild.nodeValue;
				var latitude = roadinfo.getElementsByTagName("latitude")[0].firstChild.nodeValue;
				var receivetime = roadinfo.getElementsByTagName("receivetime")[0].firstChild.nodeValue;
				//地图标注
				//redo
				var label="市内路况";
				var sContent = "";
				//sContent +="合作方号码："+sp+"<br>";
				sContent +="路口名称："+intersection+"<br>";
				sContent +="路况详细内容："+contents+"<br>";
				sContent +="方向："+direction+"<br>";
				var index = receivetime.indexOf(".");
				if(index!=-1){
					receivetime=receivetime.substring(0,index);
				}
				sContent +="接收时间："+receivetime+"<br>";
				
				sContent +="预计解除时间(分钟)："+time+"<br>";
				
				var imageUrl = WebRoot+"images/city_traffic.png";
				var bZoomTo=false;
				
				addMarker("city_traffic"+id,longitude,latitude,label,sContent,imageUrl,bZoomTo,0);
				cityTrafficInfos[cityTrafficInfos.length]="city_traffic"+id;
			}
			if(info_type=="highWay"){
				var id = roadinfo.getElementsByTagName("id")[0].firstChild.nodeValue;
				var spElement = roadinfo.getElementsByTagName("sp")[0].firstChild;
				var sp = spElement!=null?spElement.nodeValue:"";
				var highwayname = roadinfo.getElementsByTagName("highwayname")[0].firstChild.nodeValue;
				var contents = roadinfo.getElementsByTagName("contents")[0].firstChild.nodeValue;
				var directionElement = roadinfo.getElementsByTagName("direction")[0].firstChild;
				var direction = directionElement!=null?directionElement.nodeValue:"";
				var typeElement = roadinfo.getElementsByTagName("type")[0].firstChild;
				var type = typeElement!=null?typeElement.nodeValue:"";
				var zdnameElement = roadinfo.getElementsByTagName("zdname")[0].firstChild;
				var zdname = zdnameElement!=null?zdnameElement.nodeValue:"";
				var zdpositionElement = roadinfo.getElementsByTagName("zdposition")[0].firstChild;
				var zdposition = zdpositionElement!=null?zdpositionElement.nodeValue:"";
				var kilometer = roadinfo.getElementsByTagName("kilometer")[0].firstChild.nodeValue;
				var startkilo = roadinfo.getElementsByTagName("startkilo")[0].firstChild.nodeValue;
				var endkilo = roadinfo.getElementsByTagName("endkilo")[0].firstChild.nodeValue;
				var longitude = roadinfo.getElementsByTagName("longitude")[0].firstChild.nodeValue;
				var latitude = roadinfo.getElementsByTagName("latitude")[0].firstChild.nodeValue;
				var receivetime = roadinfo.getElementsByTagName("receivetime")[0].firstChild.nodeValue;
				//地图标注
				//redo
				var label="高速路况";
				var sContent = "";
				//sContent +="合作方号码："+sp+"<br>";
				sContent +="高速公路名称："+highwayname+"<br>";
				sContent +="路况详细内容："+contents+"<br>";
				var typeName = "";
				switch(type){
					case "0":
						typeName="匝道";
						break;
					case "1":
						typeName="里程桩";
						break;
					case "2":
						typeName="路段";
						break;
					default:
						typeName="未知";
						break;
				}
				sContent +="类型："+typeName+"<br>";
				sContent +="匝道名称："+zdname+"<br>";
				sContent +="里程桩公里数："+kilometer+"<br>";
				sContent +="路段起点公里数："+startkilo+"<br>";
				sContent +="路段终点公里数："+endkilo+"<br>";
				sContent +="方向："+direction+"<br>";
				var index = receivetime.indexOf(".");
				if(index!=-1){
					receivetime=receivetime.substring(0,index);
				}
				sContent +="接收时间："+receivetime+"<br>";
				
				var imageUrl = WebRoot+"images/highway_traffic.png";
				var bZoomTo=false;
				
				addMarker("highway_traffic"+id,longitude,latitude,label,sContent,imageUrl,bZoomTo,0);
				highWayTrafficInfos[highWayTrafficInfos.length]="highway_traffic"+id;
			}
		}
		//在消息框加提示信息
		//addMapOptLog("查询路况信息成功");
	}else{
		addMapOptLog("未查到路况信息");
	}
}
// 刷新查询市内、高速路况
function startRoadcondition(){
	clearInterval(roadconditionId);
	//在消息框加提示信息
	addMapOptLog("开始查询路况信息");
	roadcondition();
	roadconditionId = setInterval("roadcondition()",refrushInterval_roadcondition);
}
// 停止刷新查询市内、高速路况
function stopRoadcondition(){
	clearInterval(roadconditionId);
	//在消息框加提示信息
	addMapOptLog("停止查询路况信息");
}

/**
var roadcondition_cityId;// 城市路况刷新
var roadcondition_highWayId;// 高速路况刷新
//城市路况查询
function roadcondition_city_old(){
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/roadcondition/roadCondition.do?method=cityTraffic", true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			//alert(xmlHttp.responseText);
			roadcondition_city_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
//解析城市路况查询
function roadcondition_city_parseResp(oXmlDoc){
	//清除城市路况标注
	clearCityTrafficInfos();
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		addMapOptLog("查询市内路况未返回");
		return;
	}
	if(oXmlDoc.getElementsByTagName("roadinfo")!=null && oXmlDoc.getElementsByTagName("roadinfo").length>0){
		var roadinfos = oXmlDoc.getElementsByTagName("roadinfo");
		for(var i=0;i<roadinfos.length;i++){
			var roadinfo = roadinfos[i];
			var id = roadinfo.getElementsByTagName("id")[0].firstChild.nodeValue;
			var spElement = roadinfo.getElementsByTagName("sp")[0].firstChild;
			var sp = spElement!=null?spElement.nodeValue:"";
			var intersection = roadinfo.getElementsByTagName("intersection")[0].firstChild.nodeValue;
			var contents = roadinfo.getElementsByTagName("contents")[0].firstChild.nodeValue;
			var directionElement = roadinfo.getElementsByTagName("direction")[0].firstChild;
			var direction = directionElement!=null?directionElement.nodeValue:"";
			var time = roadinfo.getElementsByTagName("time")[0].firstChild.nodeValue;
			var longitude = roadinfo.getElementsByTagName("longitude")[0].firstChild.nodeValue;
			var latitude = roadinfo.getElementsByTagName("latitude")[0].firstChild.nodeValue;
			var receivetime = roadinfo.getElementsByTagName("receivetime")[0].firstChild.nodeValue;
			//地图标注
			//redo
			var label="市内路况";
			var sContent = "";
			//sContent +="合作方号码："+sp+"<br>";
			sContent +="路口名称："+intersection+"<br>";
			sContent +="路况详细内容："+contents+"<br>";
			sContent +="方向："+direction+"<br>";
			var index = receivetime.indexOf(".");
			if(index!=-1){
				receivetime=receivetime.substring(0,index);
			}
			sContent +="接收时间："+receivetime+"<br>";
			
			sContent +="预计解除时间(分钟)："+time+"<br>";
			
			var imageUrl = WebRoot+"images/city_traffic.png";
			var bZoomTo=false;
			
			addMarker("city_traffic"+id,longitude,latitude,label,sContent,imageUrl,bZoomTo,0);
			cityTrafficInfos[cityTrafficInfos.length]="city_traffic"+id;
		}
		//在消息框加提示信息
		addMapOptLog("查询市内路况成功");
	}else{
		addMapOptLog("未查到市内路况信息");
	}
}
//高速路况查询
function roadcondition_highway(){
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/roadcondition/roadCondition.do?method=highWayTraffic", true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			//alert(xmlHttp.responseText);
			roadcondition_highway_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
//解析高速路况查询
function roadcondition_highway_parseResp(oXmlDoc){
	//清除高速路况标注
	clearHighWayTrafficInfos();
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		addMapOptLog("查询高速路况未返回");
		return;
	}
	if(oXmlDoc.getElementsByTagName("roadinfo")!=null && oXmlDoc.getElementsByTagName("roadinfo").length>0){
		var roadinfos = oXmlDoc.getElementsByTagName("roadinfo");
		for(var i=0;i<roadinfos.length;i++){
			var roadinfo = roadinfos[i];
			var id = roadinfo.getElementsByTagName("id")[0].firstChild.nodeValue;
			var spElement = roadinfo.getElementsByTagName("sp")[0].firstChild;
			var sp = spElement!=null?spElement.nodeValue:"";
			var highwayname = roadinfo.getElementsByTagName("highwayname")[0].firstChild.nodeValue;
			var contents = roadinfo.getElementsByTagName("contents")[0].firstChild.nodeValue;
			var directionElement = roadinfo.getElementsByTagName("direction")[0].firstChild;
			var direction = directionElement!=null?directionElement.nodeValue:"";
			var typeElement = roadinfo.getElementsByTagName("type")[0].firstChild;
			var type = typeElement!=null?typeElement.nodeValue:"";
			var zdnameElement = roadinfo.getElementsByTagName("zdname")[0].firstChild;
			var zdname = zdnameElement!=null?zdnameElement.nodeValue:"";
			var zdpositionElement = roadinfo.getElementsByTagName("zdposition")[0].firstChild;
			var zdposition = zdpositionElement!=null?zdpositionElement.nodeValue:"";
			var kilometer = roadinfo.getElementsByTagName("kilometer")[0].firstChild.nodeValue;
			var startkilo = roadinfo.getElementsByTagName("startkilo")[0].firstChild.nodeValue;
			var endkilo = roadinfo.getElementsByTagName("endkilo")[0].firstChild.nodeValue;
			var longitude = roadinfo.getElementsByTagName("longitude")[0].firstChild.nodeValue;
			var latitude = roadinfo.getElementsByTagName("latitude")[0].firstChild.nodeValue;
			var receivetime = roadinfo.getElementsByTagName("receivetime")[0].firstChild.nodeValue;
			//地图标注
			//redo
			var label="高速路况";
			var sContent = "";
			//sContent +="合作方号码："+sp+"<br>";
			sContent +="高速公路名称："+highwayname+"<br>";
			sContent +="路况详细内容："+contents+"<br>";
			var typeName = "";
			switch(type){
				case "0":
					typeName="匝道";
					break;
				case "1":
					typeName="里程桩";
					break;
				case "2":
					typeName="路段";
					break;
				default:
					typeName="未知";
					break;
			}
			sContent +="类型："+typeName+"<br>";
			sContent +="匝道名称："+zdname+"<br>";
			sContent +="里程桩公里数："+kilometer+"<br>";
			sContent +="路段起点公里数："+startkilo+"<br>";
			sContent +="路段终点公里数："+endkilo+"<br>";
			sContent +="方向："+direction+"<br>";
			var index = receivetime.indexOf(".");
			if(index!=-1){
				receivetime=receivetime.substring(0,index);
			}
			sContent +="接收时间："+receivetime+"<br>";
			
			var imageUrl = WebRoot+"images/highway_traffic.png";
			var bZoomTo=false;
			
			addMarker("highway_traffic"+id,longitude,latitude,label,sContent,imageUrl,bZoomTo,0);
			highWayTrafficInfos[highWayTrafficInfos.length]="highway_traffic"+id;
		}
		//在消息框加提示信息
		addMapOptLog("查询高速路况成功");
	}else{
		addMapOptLog("未查到高速路况信息");
	}
}
// 刷新查询市内路况
function startRoadcondition_city(){
	roadcondition_cityId = setInterval("roadcondition_city()",refrushInterval_roadcondition);
}
// 停止刷新查询市内路况
function stopRoadcondition_city(){
	clearInterval(roadcondition_cityId);
}
// 刷新查询高速路况
function startRoadcondition_highway(){
	roadcondition_highWayId = setInterval("roadcondition_highway()",refrushInterval_roadcondition);
}
// 停止刷新查询高速路况
function stopRoadcondition_highway(){
	clearInterval(roadcondition_highWayId);
}
*/