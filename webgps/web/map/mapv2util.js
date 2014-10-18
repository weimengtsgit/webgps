
function modifyPoiMarker(pointID,lng,lat,label,sContent,imageUrl){
	var t = getOverlayById(pointID);
	if(t){
		//removeOverlayById(pointID);
		t.option.tipOption.content=sContent; 
		t.option.imageUrl = imageUrl;
		updateOverlay(t);
	}
	return t;
}

function getOverlayById(id){
	return mapObj.getOverlayById(id);
}

function updateOverlay(Mmarker){
	mapObj.updateOverlay(Mmarker);
}

function addEventListener(eventType,functionname){
	mapObj.addEventListener(mapObj,eventType,functionname);
}

function removeOverlayById(overlayId){
	mapObj.removeOverlayById(overlayId);
}

function removeOverlay(overlay){
	mapObj.removeOverlay(overlay);
}

function removeOverlays(arr){
	mapObj.removeOverlays(arr);
}
function getZoomLevel(){
	return mapObj.getZoomLevel();
}
//拖动
	function resetControls(){
		mapObj.setCurrentMouseTool(PAN_WHEELZOOM);
	}
	//放大
	function setZoomIn(){
		mapObj.zoomIn(mapObj.getCenter()); 
	}
	//缩小
	function setZoomOut(){
		mapObj.zoomOut(mapObj.getCenter());
	}
	//测距
	function rulerByMouseTool(){
	  var option={};   
	  option.hasCircle=false;   
	  option.hasPrompt=true;   
	  var test=mapObj.setCurrentMouseTool(RULER,option);
	}
	
	//测面积
	function compute_areaBymouseTool(){
	  var option={};   
	  option.hasCircle=false;   
	  option.hasPrompt=true;   
	  var test=mapObj.setCurrentMouseTool(COMPUTE_AREA,option);
	}
	
	//清除
	function clear(){
		globalMapPoiOverLayArr = [];
		parent.locationPointArr = [];
		mapObj.removeAllOverlays();
	}
	var fullmapflag = false;
	//缩放地图
	function fullmap(){
		if(!fullmapflag){
			parent.northPanel.collapse(false);
			parent.center.collapse(false);
			fullmapflag = true;
		}else{
			parent.northPanel.expand(false);
			parent.center.expand(false);
			fullmapflag = false;
		}
		
	}
// 点对象
function PointMarker(pointID,lng,lat,label,sContent,imageUrl,bZoomTo){
	this.pointID = pointID;
	this.lng = lng;
	this.lat = lat;
	this.label = label;
	this.sContent = sContent;
	this.imageUrl = imageUrl;
	this.bZoomTo = bZoomTo;
}
//创建地图点对象
//modify by zhaofeng
function createPointMarker(pointMarker){
  var labelOptions=new MLabelOptions();   
  var fontstyle = new MFontStyle();  //定义字体风格对象
  fontstyle.name ="Arial";
  fontstyle.size = 12;
  fontstyle.color = 0xFFFFFF;
  fontstyle.bold = true;
  labelOptions.fontStyle=fontstyle;
  labelOptions.content= pointMarker.label;
  labelOptions.hasBorder =true;
  labelOptions.hasBackground =true;
  //labelOptions.backgroundColor  =0x0000FF;
  //定位图标颜色
  labelOptions.backgroundColor  =0xCC3300;
  
  
	var tipOption = new MTipOptions(); 
    tipOption.titleFontStyle.size=18; 
    tipOption.fillStyle.color=0xFFFFCC;
    tipOption.content=pointMarker.sContent; 
    var markerOption = new MMarkerOptions(); 
    //markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  	//markerOption.labelStyle.bold = true;//是否粗体，默认否 
	//报警点Marker的Label设置为图片的左边：
	if(pointMarker.pointID.substr(0,6) == "alarm_"){  
  	    //markerOption.labelPosition = new  MPoint(-110,0); 
	}else{
  	    //markerOption.labelPosition = new  MPoint(0,0); 
	}
  	markerOption.isDraggable=false;//是否可以拖动  
  	markerOption.tipOption = tipOption; 
  	markerOption.labelOption = labelOptions;
  	/**
	 * modify by zhaofeng
	 * date 2011211
	 * time time:18:00
	 */
  	markerOption.canShowTip= false;  
  	markerOption.hasShadow=false;
  	markerOption.picAgent=false;
  	markerOption.imageAlign=5;//
	//markerOption.label=pointMarker.label;
	markerOption.imageUrl = pointMarker.imageUrl;
	markerOption.tipOption.tipType=FLASHTIP;
	var ll = new  MLngLat(pointMarker.lng,pointMarker.lat); 
	var marker = new MMarker(ll,markerOption);
	marker.id=pointMarker.pointID;
	/**
	 * 添加监听事件
	 * add by zhaofeng
	 * date 2011-02-17
	 * time 16:00
	 * start
	 */
	mapObj.addEventListener(marker,MOUSE_CLICK,update);
	/**
	 * end
	 */
	return marker;
}
// 单独描一个点
function addPointMarker(pointMarker){
	var marker = createPointMarker(pointMarker);
	mapObj.addOverlay(marker,pointMarker.bZoomTo); 	
	mapObj.addEventListener(marker,MOUSE_DOWN,mouseDowna);
}
function mouseDowna(e) {
	// do nothing
}

// 描多个点
function addPointMarkers(pointMarkers, bZoomTo){
	var ps = new Array();
	for(var i=0;i<pointMarkers.length;i++){
		marker = createPointMarker(pointMarkers[i]);
		ps.push(marker);
	}
	mapObj.addOverlays(ps, bZoomTo);
	return ps;
}
// 清除地图上所有覆盖物
/*function clearMap(){
	//alert('clearMap');
	mapObj.removeAllOverlays();
}*/
// 清除id对应的地图上覆盖物
function clearMapById(id){
	mapObj.removeOverlayById(id);
}

/**
 * 轨迹回放操作地图方法
 */
/**
 * 画点
 * @param {} pointID
 * @param {} lng
 * @param {} lat
 * @param {} label
 * @param {} sContent
 * @param {} imageUrl
 * @param {} bZoomTo
 * @return {}
 */
function addMarker(pointID,lng,lat,label,sContent,imageUrl,bZoomTo){
	
	/*if(tempMarker!=null){
	    tempMarker.option.tipOption.content=sContent;
	    //tempMarker.option.label=label;
		tempMarker.option.labelOption.content = label;
		mapObj.updateOverlay(tempMarker);
	}else{*/
	    var tipOption = new MTipOptions(); 
    	tipOption.titleFontStyle.size=20; 
    	tipOption.fillStyle.color=0xFFFFCC;
    	tipOption.content=sContent; 
    	var markerOption = new MMarkerOptions(); 
    	markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  		markerOption.labelStyle.bold = true;//是否粗体，默认否 
  		markerOption.isDraggable=false;//是否可以拖动  
  		markerOption.tipOption = tipOption; 
  		markerOption.canShowTip= true;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		markerOption.label=label;
		markerOption.imageUrl = imageUrl;
		markerOption.tipOption.tipType=FLASHTIP;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		mapObj.addOverlay(marker,bZoomTo); 	
		//mapObj.addEventListener(marker,MOUSE_DOWN,mouseDowna);
		return marker;
	//}
}
/**
 * 得到轨迹点
 * @param {} pointID
 * @param {} lng
 * @param {} lat
 * @param {} label
 * @param {} sContent
 * @param {} imageUrl
 * @return {}
 */
function addTrackMarker(pointID,lng,lat,label,sContent,imageUrl){
	/*var markerOption = new MMarkerOptions();
		var labelOptions=new MLabelOptions();
		labelOptions.content=sContent;
		markerOption.labelOption=labelOptions;
		var tipOption = new MTipOptions();
	    markerOption.canShowTip= true;
		tipOption.title=label;
		tipOption.content=markerOption.labelOption.content;
		tipOption.tipType =FLASH_BUBBLE_TIP;
		markerOption.imageUrl=imageUrl;
		markerOption.labelPosition = new MPoint(0,0);
		markerOption.isDraggable=false;//是否可以拖动
		markerOption.imageAlign=MIDDLE_CENTER;//设置图片锚点相对于图片的位置
		markerOption.tipOption = tipOption;
		markerOption.isEditable =false;
		markerOption.hasShadow = false;
		markerOption.picAgent = false;
		var ll = new MLngLat(lng, lat,1);
		Mmarker = new MMarker(ll,markerOption); 
		Mmarker.id=pointID;*/
		
	   /* var tipOption = new MTipOptions(); 
    	tipOption.titleFontStyle.size=20; 
    	tipOption.fillStyle.color=0xFFFFCC;
    	tipOption.content=sContent; 
    	var markerOption = new MMarkerOptions(); 
    	markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  		markerOption.labelStyle.bold = false;//是否粗体，默认否 
  		markerOption.isDraggable=false;//是否可以拖动  
  		markerOption.tipOption = tipOption; 
  		markerOption.canShowTip= true;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		markerOption.label=label;
		markerOption.imageUrl = imageUrl;
		markerOption.tipOption.tipType=FLASHTIP;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		return marker;*/
		//var tipOption = new MTipOptions(); 
		//tipOption.tipType=FLASHTIP;
	
		var tipOption = new MTipOptions(); 
		var markerOption = new MMarkerOptions(); 
		markerOption.tipOption = tipOption; 
		//update  false by zhaofeng  
		markerOption.canShowTip= false;
		
		markerOption.picAgent=false;
		markerOption.imageAlign=5;//
		markerOption.imageUrl = imageUrl;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll , markerOption);
		marker.id=pointID;
		
//		mapObj.addEventListener(marker,MOUSE_CLICK,mouseClick);
		return marker;
		
}

function addTrackMarkerArrow(pointID,lng,lat,label,sContent,imageUrl){
	var tipOption = new MTipOptions(); 
	var markerOption = new MMarkerOptions(); 
	markerOption.tipOption = tipOption; 
	//update false by zhaofeng
	markerOption.canShowTip= false;
		markerOption.picAgent=false;
		markerOption.imageAlign=5;//
		//markerOption.hasShadow=false;
		markerOption.imageUrl = imageUrl;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll , markerOption);
		marker.id=pointID;
		
//		mapObj.addEventListener(marker,MOUSE_CLICK,mouseClick);
		return marker;
}

function addTrackMarker1(pointID,lng,lat,imageUrl){
	    var markerOption = new MMarkerOptions(); 
		markerOption.canShowTip= false;
		markerOption.picAgent=false;
		markerOption.imageAlign=5;//
		markerOption.imageUrl = imageUrl;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll , markerOption);
		marker.id=pointID;
		return marker;
}

function addLabelMarker(pointID,lng,lat,label,sContent,imageUrl){
//alert(pointID+','+lng+','+lat+','+label+','+sContent+','+imageUrl);
  var labelOptions=new MLabelOptions();   
  var fontstyle = new MFontStyle();  //定义字体风格对象
  fontstyle.name ="Arial";
  fontstyle.size = 12;
  fontstyle.color = 0xFFFFFF;
  fontstyle.bold = true;
  labelOptions.fontStyle=fontstyle;
  labelOptions.content= label;
  labelOptions.hasBorder =true;
  labelOptions.hasBackground =true;
  labelOptions.backgroundColor  =0x6666FF;
  
	    var tipOption = new MTipOptions();
	    tipOption.tipWidth=320;
		tipOption.tipHeight=230;
    	tipOption.titleFontStyle.size=20;
    	tipOption.fillStyle.color=0xFFFFCC;
    	//tipOption.tipType=HTML_BUBBLE_TIP;
    	tipOption.tipType=HTML_BUBBLE_TIP;
    	
    	tipOption.content=sContent; 
    	var markerOption = new MMarkerOptions(); 
    	//markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  		//markerOption.labelStyle.bold = false;//是否粗体，默认否 
  		markerOption.isDraggable=false;//是否可以拖动  
  		markerOption.tipOption = tipOption;
  		markerOption.labelOption = labelOptions;
  		markerOption.canShowTip= true;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		//markerOption.label=label;
		markerOption.imageUrl = imageUrl;
		
    	//markerOption.labelOption = labelOptions;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		return marker;
		
	    /*var tipOption = new MTipOptions(); 
    	//tipOption.titleFontStyle.size=20; 
    	//tipOption.fillStyle.color=0xFFFFCC;
		tipOption.tipWidth=300;
		tipOption.tipHeight=300;
		tipOption.content=sContent; 
		tipOption.tipType = HTML_BUBBLE_TIP;
		
    	var markerOption = new MMarkerOptions(); 
    	//markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  		//markerOption.labelStyle.bold = false;//是否粗体，默认否 
  		markerOption.isDraggable=false;//是否可以拖动  
  		markerOption.tipOption = tipOption;
  		markerOption.labelOption = labelOptions;
  		markerOption.canShowTip= true;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		//markerOption.label=label;
		markerOption.imageUrl = imageUrl;
    	//markerOption.labelOption = labelOptions;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		return marker;*/
		
}


function addLabelPoiMarker(pointID,lng,lat,label,sContent,imageUrl){
 var labelOptions=new MLabelOptions();   
  var fontstyle = new MFontStyle();  //定义字体风格对象
  fontstyle.name ="Arial";
  fontstyle.size = 12;
  fontstyle.color = 0xFFFFFF;
  fontstyle.bold = true;
  labelOptions.fontStyle=fontstyle;
  labelOptions.content= label;
  labelOptions.hasBorder =true;
  labelOptions.hasBackground =true;
  labelOptions.backgroundColor  =0xFF0000;
  
	    var tipOption = new MTipOptions(); 
    	tipOption.titleFontStyle.size=20; 
    	tipOption.fillStyle.color=0xFFFFCC;
    	tipOption.content=sContent; 
    	var markerOption = new MMarkerOptions(); 
    	//markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
  		//markerOption.labelStyle.bold = false;//是否粗体，默认否 
  		markerOption.isDraggable=false;//是否可以拖动  
  		markerOption.tipOption = tipOption;
  		markerOption.labelOption = labelOptions;
  		markerOption.canShowTip= true;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		//markerOption.label=label;
		markerOption.imageUrl = imageUrl;
		markerOption.tipOption.tipType=FLASHTIP;
    	//markerOption.labelOption = labelOptions;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		return marker;
		
}

/**
 * 得到轨迹线
 * @param {} lineID
 * @param {} tracks
 * @param {} label
 * @param {} title
 */
function addTrackPolyline(lineID,tracks,label,title){
  	if( tracks==null || tracks.length==0) return;
  	
  	var colors = [0xff0000,0x00ff00,0x0000ff,0xfff000,0x000fff];
  	var polylines = [];
  	for(var i=0;i<tracks.length;i++){
  		var points = tracks[i];
  		var lineopt  = new MLineOptions(); 
	    var mlineStyle = new MLineStyle();
	    //轨迹线颜色和宽度
	    mlineStyle.color=colors[i];
	    mlineStyle.thickness=3;
	    
	    lineopt.lineStyle=mlineStyle;  
	    lineopt.canShowTip = false;   
	    var polylineAPI = new MPolyline(points,lineopt);     
	  	polylineAPI.id = lineID+i;
	  	polylines.push(polylineAPI);
  	}
	mapObj.addOverlays(polylines,true);
	return polylineAPI;
}

/**
 * 画多折线
 * @param {} lineID
 * @param {} points
 * @param {} label
 * @param {} title
 * @param {} bZoomTo
 */
function addPolyline(lineID,points,label,title,bZoomTo){
  	if( points==null || points.length==0) return;
 	//var tipOption = new MTipOptions(); //设置tip样式   
    //tipOption.title=title;   
    //tipOption.anchor =  new MPoint(0,0);      
    //tipOption.content=label;
    var lineopt  = new MLineOptions(); 
    var mlineStyle = new MLineStyle();
    mlineStyle.color=0x0000ff;
    mlineStyle.thickness=2;
    lineopt.lineStyle=mlineStyle;  
    //lineopt.tipOption = tipOption;   
    lineopt.canShowTip = false;   
    //if( points.length >1){
        var polylineAPI = new MPolyline(points,lineopt);     
  		polylineAPI.id = lineID;
  		 var tempLine=mapObj.getOverlayById(lineID);
		if( tempLine==null){
		    	mapObj.addOverlay(polylineAPI,bZoomTo);	
		}else{
		    	mapObj.updateOverlay(polylineAPI,bZoomTo);
		}
		return polylineAPI;
   // }
}
//画多边形
function addPolygon(arr,title,content,id,bZoomTo){//在地图上画多边形      

    var areopt = new MAreaOptions();//构建一个名为areopt的面选项对象。   
    var tipOption = new MTipOptions();   
    tipOption.title=title;  // "多边形"
    tipOption.content=content ;//"这是一个多边形！";   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=true;//可选项，是否具有二态，默认为false即没有二态   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id ; //"polygon101";   
    mapObj.addOverlay(polygonAPI,bZoomTo);   
} 

//画矩形-报警回传区域
 function addRectangle(areaxy,title,content,id,bZoomTo){ //在地图上画矩形      
 	var arr=new Array();   
 	//矩形字符 JIOMYSINMUDLLH,LNGUTKHRRLHDD#JIPMPKPSJPDLHL,LNGUURLNLLHPH
 	var areaxyArr = areaxy.split('#');
	for(var i=0;i<areaxyArr.length;i++){
		var tmpxyArr = areaxyArr[i].split(',');
		arr.push(new MLngLat(tmpxyArr[0],tmpxyArr[1]));  
	}
  var lineopt = new MAreaStyle();   
  lineopt.borderStyle.alpha = 0.7; //透明度，默认1，范围0~1   
  lineopt.borderStyle.color = 0x00FF33; //线颜色，默认黑色   
  lineopt.borderStyle.thickness = 3;  //线粗细度，默认3    
  lineopt.borderStyle.lineType=LINE_SOLID;   
  lineopt.fillStyle.alpha = 0.3;   
  lineopt.fillStyle.color = 0x99FF33;   
  var tipOption = new MTipOptions();   
  tipOption.title=title;   
  tipOption.anchor =  new MPoint(0,0);   //图片锚定点，MPoint类型   
  tipOption.content=content;  //tip内容   
  tipOption.hasShadow= false; //是否有阴影   
  var areopt = new MAreaOptions();   
  areopt.areaStyle=lineopt;   
  areopt.tipOption=tipOption;   
  areopt.canShowTip = true;   
  areopt.isDimorphic=true;//可选项，是否具有二态，默认为false即没有二态   
  areopt.dimorphicColor=0x00FFFF;   
  rectangleAPI = new MRectangle(arr,areopt);         
  rectangleAPI.id=id;      
  mapObj.addOverlay(rectangleAPI,bZoomTo);      
 }    
 
  /**
  * update by zhaofeng
  * date 2011-03-08
  * time :9:00
  */
 function mouseClick(param){
 	var tmpid = param.overlayId;
 	var tmpidArr = tmpid.split('@#');
 	if(tmpidArr[0] == 'pointall' || tmpidArr[0] == 'arrow'){
 		var marker = getOverlayById(tmpid);
 		marker.option.canShowTip=true;
 		var content_ = marker.option.tipOption.content;
//    	var tipOption = new MTipOptions(); 
//    	tipOption.titleFontStyle.size=20; 
//    	tipOption.fillStyle.color=0xFFFFCC;
    	if(tmpidArr.length>2){
    		//取得点的位置描述
    		var content_decodeURI = decodeURI(tmpidArr[1]);
    		var loc_desc = content_decodeURI.split('<br>')[3].split(',');
    		 if(loc_desc.toString().length==7)
    		 {
    			 if(content_.length > 0){
  				   mapObj.openOverlayTip(tmpid);
  				   return;
    			 }
    		//得到点的经纬度	
    			var x=marker.lnglat.lngX;
    			var y=marker.lnglat.latY;
    		//通过经纬度提交后台调用相应locDesc方法解析得到位置信息
    			Ext.Ajax.request({
    				 url:path+'/locate/locate.do?method=locDesc',
    				 method :'POST', 
    				 params: {x:x,y:y},
    				 success : function(request){
    				 var res= request.responseText;
    				   if(res==null || res.length==0){
//    				       Ext.Msg.alert('提示', "未查到数据!");
//    				       return;
    					   res = main.data_not_found;
    				   }
    		//替换信息框中的位置信息		  
    				   var content=content_decodeURI.replace(loc_desc,"位置描述 :"+res);
    		//两次位置描述比较，不相同就替换原来的内容，相同就返回。
    				   if(marker.option.tipOption.content!=content){
    					   marker.option.tipOption.content=content;
    					   mapObj.updateOverlay(marker);
        				   mapObj.openOverlayTip(tmpid);
    				   }else{
    					   return;
//    			       tipOption.tipType=FLASHTIP;
//    					   alert("2");
//    			       mapObj.updateOverlay(marker);
//    				   mapObj.openOverlayTip(tmpid);
    				   }
//    			       marker.option.canShowTip=true;
    				   },
    				 failure : function(request) {
    					 //alert("查询失败");
    				 }
    				});	
    		 }
    		 else
    		 {		var content = tmpidArr[1];
    		 		var content_decodeURI = decodeURI(content);
    			 	marker.option.tipOption.content=content_decodeURI;
				   mapObj.updateOverlay(marker);
    			   mapObj.openOverlayTip(tmpid);
    		 }
    		
    		
    	}
 	}/*else if(tmpidArr[0] == 'arrow'){
 		var tmpid_ = 'pointall@#' + tmpidArr[1] + '@#' + tmpidArr[2];
 		var marker_ = getOverlayById(tmpid_);
 		var tipOption = new MTipOptions(); 
    	tipOption.titleFontStyle.size=20; 
    	tipOption.fillStyle.color=0xFFFFCC;
    	if(tmpidArr.length>2){
    		tipOption.content=tmpidArr[1]; 
    	}
    	tipOption.tipType=FLASHTIP;
    	marker_.option.tipOption = tipOption; 
		mapObj.updateOverlay(marker_);
		mapObj.openOverlayTip(tmpid_);
 	}*/
 }
 
 
 function GeoPoint()
 {
 	this.x =0;
 	this.y=0;
 }

 var fp=new GeoPoint();
 fp.x=0;
 fp.y=0;
 var tp=new GeoPoint();
 tp.x=1;
 tp.y=1;


 function geo_direction(gptfrom, gptto){
 	

 	var head=0;
     if(gptto.y == gptfrom.y){
         if(gptto.x > gptfrom.x){
             head = 90;
         }else{
             head = 270;
 		}
     }else{
         head = Math.atan((gptto.x - gptfrom.x) / (gptto.y - gptfrom.y)) * 57.295780490443; //180/3.1415926
 		if(gptto.x > gptfrom.x){
 			if(gptto.y < gptfrom.y){
 				//2
 				head = 180 + head;
 			}
 		}else{
 			if(gptto.y < gptfrom.y){
 				//3
 				head = 180 + head;
 			}else{
 				//4
 				head = 360 + head;
 				//head =  head;
 			}
 		}
     }
     //alert(head);
     //alert("(" + gptfrom.x + "," + gptfrom.y + ") - (" + "(" + gptto.x + "," + gptto.y + ") = " + head)
 	return head;
 }
 

 var EARTH_RADIUS = 6378137.0;    //单位M
 var PI = Math.PI;
 
 function getRad(d){
     return d*PI/180.0;
 }
 
 /**
  * caculate the great circle distance
  * @param {Object} lat1
  * @param {Object} lng1
  * @param {Object} lat2
  * @param {Object} lng2
  */
 function getGreatCircleDistance(lat1,lng1,lat2,lng2){
     var radLat1 = getRad(lat1);
     var radLat2 = getRad(lat2);
     
     var a = radLat1 - radLat2;
     var b = getRad(lng1) - getRad(lng2);
     
     var s = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
     s = s*EARTH_RADIUS;
     s = Math.round(s*10000)/10000.0;
             
     return s;
 }


 /**
  * approx distance between two points on earth ellipsoid
  * @param {Object} lat1
  * @param {Object} lng1
  * @param {Object} lat2
  * @param {Object} lng2
  */
 function getFlatternDistance(lat1,lng1,lat2,lng2){
     var f = getRad((lat1 + lat2)/2);
     var g = getRad((lat1 - lat2)/2);
     var l = getRad((lng1 - lng2)/2);
     
     var sg = Math.sin(g);
     var sl = Math.sin(l);
     var sf = Math.sin(f);
     
     var s,c,w,r,d,h1,h2;
     var a = EARTH_RADIUS;
     var fl = 1/298.257;
     
     sg = sg*sg;
     sl = sl*sl;
     sf = sf*sf;
     
     s = sg*(1-sl) + (1-sf)*sl;
     c = (1-sg)*(1-sl) + sf*sl;
     
     w = Math.atan(Math.sqrt(s/c));
     r = Math.sqrt(s*c)/w;
     d = 2*w*a;
     h1 = (3*r -1)/2/c;
     h2 = (3*r +1)/2/s;
     
     return d*(1 + fl*(h1*sf*(1-sg) - h2*(1-sf)*sg));
 }


 var pre_flag = false;
 var is_arrow = true;
/**
 * 解析轨迹回放查询结果,在地图上显示点和线
 * @param {} res
 */
function parseTrackData(res){
	if(parent.trackAllPoint.length>0){
		mapObj.removeAllOverlays();
		parent.locationPointArr = [];
		drawPoiOverLay(getZoomLevel());
	}
	var global = 0;
	pre_flag = false;
	//alert('a');
	//显示所有点
	parent.trackAllPoint = [];
	parent.arrowAllPoint = [];
	intervaltpflag = false;
	intervaltplen = 0;
	clearInterval(intervaltpObj);
		//用来播放使用
	parent.trackPointArr = new Array();
	//前一个轨迹点的时间,用来比较两个点之间的时间
	var prevTime = '';
	//前一个轨迹点对象,比较两个点之间的间隔时间
	var prevmarker;
	var trackList = new Array();
	var poiList = new Array();
	var tmpimage = path+'/track/images/track/redpoint.gif';
	//var tmpimage = path+'/track/images/track/browndot.jpg';
	var first = true;
	var prevmarkerisblue = false;
	var marker_ = null;
	
	//modify by liuhongxiao 2012-02-07
	//人员
	if(Number(parent.trackSearchDevicelocateType) == '0'){
		for(var i=0;i<res.length;i++){
			if(prevTime.length>0){
				//判断两点间的时间间隔
				var tmpsubtime = dateSub(prevTime,res[i].time);
				if((tmpsubtime > parent.filtertimeparam) && (parent.filtertimeparam!=0)){
					//如果时间间隔大于用户输入值,则这两点图标为蓝色
					tmpimage = path+'/track/images/track/bluepoint.png';
					//tmpimage = path+'/track/images/track/browndot.jpg';
					//update by zhaofeng 2011-5-24 15:31 
					//更改上一个点的图标(在不是起点的情况下修改)
					if(i!=1){
						parent.trackAllPoint[i-1].option.imageUrl=path+'/track/images/track/bluepoint.png';
					}
					//parent.trackAllPoint[i-1].option.imageUrl=path+'/track/images/track/browndot.jpg';
				}else{
					tmpimage = path+'/track/images/track/redpoint.gif';
					//tmpimage = path+'/track/images/track/browndot.jpg';
				}
			}
		
			if(i == 0){
				tmpimage = path+'/track/images/track/qidian.png';
			}else if(i == (res.length-1)){
				tmpimage =  path+'/track/images/track/zhongdian.png';
			}
		
		
			global += Number(res[i].distance);
			global = Math.round((Math.floor(global*1000)/10))/100;
			var trackimage = path+'/track/images/track/2.gif';
			var tmpscontent = '姓    名 : '+parent.trackSearchDevicevehicleNumber+
			'<br>手机号码 : '+parent.trackSearchDevicesimcard+
			'<br>时    间 : '+res[i].time+
			//'<br>终端id:'+res[i].deviceId+
			//'<br>终端类型:'+"GPS"+
			//'<br>速    度 : '+res[i].speed+'  km/h'+
			'<br>位置描述 : '+res[i].pd+
			'<br>里    程 : '+'距起点  '+global+'  km，距前一点  '+res[i].distance+'  km';
			//里    程:  距起点0.943公里，距前一点'+res[i].distance+'km'
			var tmpmarker = addTrackMarker('pointall@#'+encodeURI(tmpscontent)+'@#'+res[i].id,res[i].jmx,res[i].jmy,'',tmpscontent,tmpimage);
		
			if(is_arrow){
				if(pre_flag){
	 				tp.x = Number(res[i].deflectionx);
	 				tp.y = Number(res[i].deflectiony);
	 				//经纬度搞反了
	 				var degree = geo_direction(fp , tp);
	 				//marker_ = addTrackMarker('arrow@#'+i,res[i].jmx,res[i].jmy,'','',path+"/track/arrow_/"+parseInt(degree/10)+".jpg");
	 				//marker_ = addTrackMarker('arrow@#'+i,(Number(fp.x) + Number(tp.x)) / 2 , (Number(fp.y) + Number(tp.y)) / 2 ,'','',path+"/track/arrow_/"+parseInt(degree/10)+".jpg");
	 				
	 				//var head_ = Math.atan((tp.x - fp.x) / (tp.y - fp.y)) * 57.295780490443;
	 				/*var dis_ = getFlatternDistance(tp.y, tp.x, fp.y, fp.x);
	 				var sin_ = tp.y - fp.y / dis_;
	 				var cos_ = tp.x - fp.x / dis_;
	 				var y_ = tp.y - sin_*0.005;
	 				var x_ = tp.x - cos_*0.005;*/
	 				var x_ = (Number(tp.x) - Number(fp.x))*0.5+Number(fp.x);
	 				var y_ = (Number(tp.y) - Number(fp.y))*0.5+Number(fp.y);
	 				
	 				marker_ = addTrackMarkerArrow('arrow@#'+encodeURI(tmpscontent)+'@#'+res[i].id, x_ , y_ ,'','',path+"/track/arrow_/"+parseInt(degree/10)+".gif");
	 				//mapObj.addOverlay(marker_ , false);
	 				parent.arrowAllPoint.push(marker_);
	 			}
	 			fp.x = Number(res[i].deflectionx);
				fp.y = Number(res[i].deflectiony);
				pre_flag = true;
			}
		
			if(prevTime!='' && prevTime.substr(0, 10)!=res[i].time.substr(0, 10)){
				trackList.push(poiList);
				poiList = new Array();
			}
			prevTime = res[i].time;
			parent.trackAllPoint.push(tmpmarker);
			tmpmarker = addTrackMarker1('currentpoint',res[i].jmx,res[i].jmy,trackimage);
			parent.trackPointArr.push(tmpmarker);
			poiList.push(new MLngLat(res[i].jmx,res[i].jmy));
		}
		trackList.push(poiList);
	}
	//车辆
	else{
		//前一个轨迹点经纬度,比较两个点之间的直线距离
		var prevPoint;
		//车辆行驶状态标识,0-正常点,1-停车熄火,2-停车未熄火
		var carStatus = 0;
		
		for(var i=0;i<res.length;i++){
			var carDistance = '';//与前一点距离描述
			var carSubtime = '';//停车时间描述
			var deflectiony = Number(res[i].deflectiony);
			var deflectionx = Number(res[i].deflectionx);
			//总里程
			global += Number(res[i].distance);
			global = Math.round((Math.floor(global*1000)/10))/100;
			//起点
			if(i == 0){
				tmpimage = path+'/track/images/track/qidian.png';
				carDistance = '距前一点  '+res[i].distance+'  km';
				if(!parent.filterCarStop){
				
					if(res[i].accStatus == '0' && res[i].speed == '0.0'){
						carStatus = 1;
					}
					//停车未熄火
					else if(res[i].accStatus == '1' && res[i].speed == '0.0'){
						carStatus = 2;
					}
				}
			}
			//终点
			else if(i == (res.length-1)){
				tmpimage =  path+'/track/images/track/zhongdian.png';
				//继续停车熄火
				if(!parent.filterCarStop && res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 1){
					//判断两点间的时间间隔
					var tmpsubtime = dateSub(prevTime,res[i].time);
					var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
					tmpmarker.option.imageUrl = tmpimage;
					if(tmpsubtime > parent.filterCarInterval){
						var labelOptions=new MLabelOptions();   
						var fontstyle = new MFontStyle();  //定义字体风格对象
						fontstyle.name ="Arial";
						fontstyle.size = 12;
						fontstyle.color = 0xFFFFFF;
						fontstyle.bold = true;  
						labelOptions.fontStyle=fontstyle;
						labelOptions.content= '停车熄火:'+dateFormat(Math.round(tmpsubtime));
						labelOptions.hasBorder =true;
						labelOptions.hasBackground =true;
						labelOptions.backgroundColor  =0x6666FF;
						tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
				  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
				  		tmpmarker.option.isDraggable=false;//是否可以拖动  
				  		tmpmarker.option.hasShadow=false;
				  		tmpmarker.option.labelOption=labelOptions;
					}
					break;
				}
				//继续停车未熄火
				else if(!parent.filterCarStop && res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 2){
					//判断两点间的时间间隔
					var tmpsubtime = dateSub(prevTime,res[i].time);
					var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
					tmpmarker.option.imageUrl = tmpimage;
					if(tmpsubtime > parent.filterCarInterval){
						var labelOptions=new MLabelOptions();   
						var fontstyle = new MFontStyle();  //定义字体风格对象
						fontstyle.name ="Arial";
						fontstyle.size = 12;
						fontstyle.color = 0xFFFFFF;
						fontstyle.bold = true;  
						labelOptions.fontStyle=fontstyle;
						labelOptions.content= '停车未熄火:'+dateFormat(Math.round(tmpsubtime));
						labelOptions.hasBorder =true;
						labelOptions.hasBackground =true;
						labelOptions.backgroundColor  =0xFF0000;
						
						tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
				  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
				  		tmpmarker.option.isDraggable=false;//是否可以拖动  
				  		tmpmarker.option.hasShadow=false;
				  		tmpmarker.option.labelOption=labelOptions;
					}
					break;
				}else{
					carDistance = '距前一点  '+res[i].distance+'  km';
				}
			}
			//中间点
			else{
				if(!parent.filterCarStop){
					//继续停车熄火
					if(res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 1){
						continue;
					}
					//继续停车未熄火
					else if(res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 2){
						continue;
					}
					//停车未熄火变为停车熄火
					else if(res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 2){
						carStatus = 1;
						//判断两点间的时间间隔
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //定义字体风格对象
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= '停车未熄火:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0xFF0000;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
					  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
					  		tmpmarker.option.isDraggable=false;//是否可以拖动  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//判断两点间的实际距离
				  		var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
				  		carDistance = '距前一点  '+Math.round(tmpdistance/10)/100+'  km';
				  		tmpimage = path+'/track/images/track/stopcar1.png';
					}
					//停车熄火变为停车未熄火
					else if(res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 1){
						carStatus = 2;
						//判断两点间的时间间隔
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //定义字体风格对象
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= '停车熄火:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0x6666FF;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
					  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
					  		tmpmarker.option.isDraggable=false;//是否可以拖动  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//判断两点间的实际距离
				  		var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
				  		carDistance = '距前一点  '+Math.round(tmpdistance/10)/100+'  km';
				  		tmpimage = path+'/track/images/track/stopcar2.png';
					}
					else if(res[i].accStatus == '0' && res[i].speed == '0.0'){
						carStatus = 1;
						carDistance = '距前一点  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/stopcar1.png';
					}
					//停车未熄火
					else if(res[i].accStatus == '1' && res[i].speed == '0.0'){
						carStatus = 2;
						carDistance = '距前一点  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/stopcar2.png';
					}
					//停车熄火变为正常
					else if(carStatus == 1){
						carStatus = 0;
						//判断两点间的时间间隔
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //定义字体风格对象
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= '停车熄火:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0x6666FF;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
					  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
					  		tmpmarker.option.isDraggable=false;//是否可以拖动  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//判断两点间的实际距离
						var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
						carDistance = '距前一点  '+Math.round(tmpdistance/10)/100+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
					//停车未熄火变为正常
					else if(carStatus == 2){
						carStatus = 0;
						//判断两点间的时间间隔
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //定义字体风格对象
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= '停车未熄火:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0xFF0000;
							tmpmarker.option.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
					  		tmpmarker.option.labelStyle.bold = true;//是否粗体，默认否 
					  		tmpmarker.option.isDraggable=false;//是否可以拖动  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//判断两点间的实际距离
						var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
						carDistance = '距前一点  '+Math.round(tmpdistance/10)/100+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}else{
						carStatus = 0;
						carDistance = '距前一点  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
				}else{
					if(res[i].accStatus == '0'){
						tmpimage = path+'/track/images/track/bluepoint.png';
					}else{
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
					carDistance = '距前一点  '+res[i].distance+'  km';
				}
			}
			
			var temper=res[i].temperature!=''?'<br>温    度 : '+res[i].temperature+'℃':'';
			var temhumidity=res[i].humidity!=''?'<br>湿    度 : '+res[i].humidity+'%':'';
			var trackimage = path+'/track/images/track/car.gif';
			var tmpscontent = '车牌号码 : '+parent.trackSearchDevicevehicleNumber+
			'<br>手机号码 : '+parent.trackSearchDevicesimcard+
			'<br>时    间 : '+res[i].time+
			//'<br>终端id:'+res[i].deviceId+
			//'<br>终端类型:'+"GPS"+
			//update by zhaofeng
			'<br>位置描述 : '+res[i].pd+
			'<br>速    度 : '+res[i].speed+'  km/h'+
			//温度
			temper+
			//湿度
			temhumidity+
			'<br>里    程 : '+'距起点  '+global+'  km，'+
			//与前一点距离
			carDistance;
			var tmpmarker = addTrackMarker('pointall@#'+encodeURI(tmpscontent)+'@#'+res[i].id,res[i].jmx,res[i].jmy,'',tmpscontent,tmpimage);
		
			if(is_arrow){
				if(pre_flag){
	 				tp.x = Number(res[i].deflectionx);
	 				tp.y = Number(res[i].deflectiony);
	 				//经纬度搞反了
	 				var degree = geo_direction(fp , tp);
	 				var x_ = (Number(tp.x) - Number(fp.x))*0.5+Number(fp.x);
	 				var y_ = (Number(tp.y) - Number(fp.y))*0.5+Number(fp.y);
	 				
	 				marker_ = addTrackMarkerArrow('arrow@#'+encodeURI(tmpscontent)+'@#'+res[i].id, x_ , y_ ,'','',path+"/track/arrow_/"+parseInt(degree/10)+".gif");
	 				//mapObj.addOverlay(marker_ , false);
	 				parent.arrowAllPoint.push(marker_);
	 			}
	 			fp.x = Number(res[i].deflectionx);
				fp.y = Number(res[i].deflectiony);
				pre_flag = true;
			}
			
			if(prevTime!='' && prevTime.substr(0, 10)!=res[i].time.substr(0, 10)){
				trackList.push(poiList);
				poiList = new Array();
			}
			
			prevTime = res[i].time;
			prevPoint = [deflectiony,deflectionx];
		
			parent.trackAllPoint.push(tmpmarker);
			tmpmarker = addTrackMarker1('currentpoint',res[i].jmx,res[i].jmy,trackimage);
			parent.trackPointArr.push(tmpmarker);
			poiList.push(new MLngLat(res[i].jmx,res[i].jmy));
		}
		trackList.push(poiList);
	}

	//parent.trackAllPoint.push(prevmarker);
	
	parent.trackAllLine = addTrackPolyline('trackline',trackList,'','');
	mapObj.addOverlay(parent.trackAllPoint[0] , false);
	mapObj.addOverlay(parent.trackAllPoint[parent.trackAllPoint.length-1] , false);
	//mapObj.addOverlays(parent.trackAllPoint,false);

	intervaltrackPoint();
	intervaltpObj=setInterval('intervaltrackPoint();',2000);
	
	//回放进度lable
	var tmpplaytempolabel = parent.Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText('1/'+parent.trackPointArr.length);
	//回放进度条
	var tmpplaytemposlider = parent.Ext.getCmp('playtemposlider');
	tmpplaytemposlider.setValue(1,true);
	tmpplaytemposlider.setMinValue(1);
	tmpplaytemposlider.setMaxValue(parent.trackPointArr.length);
	//播放进度条总长度
	parent.sliderlen = parent.trackPointArr.length - 1;
	parent.sliderposition = 0;
	clearInterval(parent.playinterval);
	parent.stop();
	
	//mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
}


//日期相减,返回相减的分钟
function dateSub(strDate_1,strDate_2){
	strDate_1=strDate_1.replace(/-/g,"/");
	strDate_2=strDate_2.replace(/-/g,"/");
	var  date1  =  Date.parse(strDate_1);
	var  date2  =  Date.parse(strDate_2);
	return (date2-date1)/(60*1000);
}


//在地图上画多边形    
function AreaAlarmaddPolygon(tmpxyArr, title, content, id, bZoomTo ,isDimorphic){  
	var arr = new Array();
	for(var i = 0;i < tmpxyArr.length;i++){
		var tmpxy = tmpxyArr[i].split(',');
	    arr.push(new MLngLat(tmpxy[0],tmpxy[1])); 
	}
    var areopt = new MAreaOptions();//构建一个名为areopt的面选项对象。   
    var tipOption = new MTipOptions();   
    tipOption.title=title;   
    tipOption.content=content;   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=isDimorphic;//可选项，是否具有二态，默认为false即没有二态   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id;   
    mapObj.addOverlay(polygonAPI,bZoomTo);   
}  
//得到多边形对象
function getPolygon(tmpxyArr, title, content, id, isDimorphic){  
	var arr = new Array();
	for(var i = 0;i < tmpxyArr.length;i++){
		var tmpxy = tmpxyArr[i].split(',');
	    arr.push(new MLngLat(tmpxy[0],tmpxy[1])); 
	}
    var areopt = new MAreaOptions();//构建一个名为areopt的面选项对象。   
    var tipOption = new MTipOptions();   
    tipOption.title=title;   
    tipOption.content=content;   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=isDimorphic;//可选项，是否具有二态，默认为false即没有二态   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id;   
    return polygonAPI;
    //mapObj.addOverlay(polygonAPI,bZoomTo);   
}  
//画矩形
 /*function addRectangle(){ //在地图上画矩形      
  //var ll=mapObj.getCenter();   
  var arr=new Array();   
  arr.push(new MLngLat('JIOMYSINMUDLLH','LNGUTKHRRLHDD'));   
  arr.push(new MLngLat('JIPMPKPSJPDLHL','LNGUURLNLLHPH'));   
  var lineopt = new MAreaStyle();   
  lineopt.borderStyle.alpha = 0.7; //透明度，默认1，范围0~1   
  lineopt.borderStyle.color = 0x00FF33; //线颜色，默认黑色   
  lineopt.borderStyle.thickness = 3;  //线粗细度，默认3    
  lineopt.borderStyle.lineType=LINE_SOLID;   
  lineopt.fillStyle.alpha = 0.3;   
  lineopt.fillStyle.color = 0x99FF33;   
  var tipOption = new MTipOptions();   
  tipOption.title="矩形";   
  tipOption.anchor =  new MPoint(0,0);   //图片锚定点，MPoint类型   
  tipOption.content="这是一个矩形";  //tip内容   
  tipOption.hasShadow= false; //是否有阴影   
  var areopt = new MAreaOptions();   
  areopt.areaStyle=lineopt;   
  areopt.tipOption=tipOption;   
  areopt.canShowTip = true;   
  areopt.isDimorphic=true;//可选项，是否具有二态，默认为false即没有二态   
  areopt.dimorphicColor=0x00FFFF;   
  rectangleAPI = new MRectangle(arr,areopt);         
  rectangleAPI.id="rectangle101";      
  mapObj.addOverlay(rectangleAPI,true);      
 }    */

//点击点取位置描述
//add by zhaofeng

function update(p){ 
	var tmpid =p.overlayId;
	
//取得点对象
	var obj = mapObj.getOverlayById(p.overlayId);
	obj.option.canShowTip=true;
//取得点的位置描述
	var loc_desc = obj.option.tipOption.content.split('<br>')[3].split(',');
 if(loc_desc.toString().length==19)
 {
//得到点的经纬度	
	var x=obj.lnglat.lngX;
	var y=obj.lnglat.latY;
	
//通过经纬度提交后台调用相应locDesc方法解析得到位置信息
	Ext.Ajax.request({
		 url:path+'/locate/locate.do?method=locDesc',
		 method :'POST', 
		 params: {x:x,y:y},
		 success : function(request){
		   var res= request.responseText;
		   if(res==null || res.length==0){
//		       Ext.Msg.alert('提示', "未查到数据!");
//		       return;
		       res = main.data_not_found;
		   }
//得到信息框中的内容
		   var scontent = obj.option.tipOption.content;
//替换信息框中的位置信息		   
		   var content=scontent.replace(loc_desc,"位&nbsp;&nbsp;置："+res);
		   obj.option.tipOption.content=content;
		   mapObj.updateOverlay(obj);
		   mapObj.openOverlayTip(tmpid);
		   },
		 failure : function(request) {
			 alert("查询失败");
		 }
		});	
 }
 else
 {
	   
	   mapObj.openOverlayTip(tmpid);
 }
}


/*
 * 日志中心-轨迹查询
 */
var diaryLocPointArr = [];
var diaryTrackPointArr = [];
var diaryArrowPointArr = [];

function parseDiaryTrackData(res){
	if(parent.trackAllPoint.length>0){
		mapObj.removeAllOverlays();
		diaryLocPointArr = [];
		drawPoiOverLay(getZoomLevel());
	}
	var global = 0;
	pre_flag = false;
	diaryTrackPointArr = [];
	diaryArrowPointArr = [];
	// 前一个轨迹点的时间,用来比较两个点之间的时间
	var prevTime = '';
	// 前一个轨迹点对象,比较两个点之间的间隔时间
	var prevmarker;
	var poiList = new Array();
	var tmpimage = path+'/track/images/track/redpoint.gif';
	var first = true;
	var prevmarkerisblue = false;
	var marker_ = null;
	var locType = res[0].locType;
	for(var i=0;i<res.length;i += 1){
		if(Number(locType) == 0){
			tmpimage = path+'/track/images/track/redpoint.gif';
		}else{
			if(res[i].accStatus == '0'){
				tmpimage = path+'/track/images/track/bluepoint.png';
			}else{
				tmpimage = path+'/track/images/track/redpoint.gif';
			}
		}
		if(i == 0){
			tmpimage = path+'/track/images/track/qidian.png';
		}else if(i == (res.length-1)){
			tmpimage =  path+'/track/images/track/zhongdian.png';
		}
		var temper=res[i].temperature!=''?'<br>温    度 : '+res[i].temperature:'';
		var tmpscontent = '';
		global += Number(res[i].distance);
		global = Math.round((Math.floor(global*1000)/10))/100;
		var trackimage = path+'/track/images/track/2.gif';
		if(Number(locType) == 1){
			tmpscontent = '车牌号码 : '+parent.trackSearchDevicevehicleNumber+
			'<br>手机号码 : '+parent.trackSearchDevicesimcard+
			'<br>时    间 : '+res[i].time+
			'<br>速    度 : '+res[i].speed+'  km/h'+
			'<br>位置描述 : '+res[i].pd+
			temper+
			'<br>里    程 : '+'距起点  '+global+'  km，距前一点  '+res[i].distance+'  km';
			trackimage = path+'/track/images/track/car.gif';
		}else{
			tmpscontent = '姓    名 : '+parent.trackSearchDevicevehicleNumber+
			'<br>手机号码 : '+parent.trackSearchDevicesimcard+
			'<br>时    间 : '+res[i].time+
			'<br>位置描述 : '+res[i].pd+
			'<br>里    程 : '+'距起点  '+global+'  km，距前一点  '+res[i].distance+'  km';
			trackimage = path+'/track/images/track/2.gif';
		}
		var tmpmarker = addTrackMarker('pointall@#'+encodeURI(tmpscontent)+'@#'+res[i].id,res[i].jmx,res[i].jmy,'',tmpscontent,tmpimage);
		if(is_arrow){
			 tp.x = Number(res[i].deflectionx);
			 tp.y = Number(res[i].deflectiony);
			 var degree = geo_direction(fp , tp);
			 var x_ = (Number(tp.x) - Number(fp.x))*0.5+Number(fp.x);
			 var y_ = (Number(tp.y) - Number(fp.y))*0.5+Number(fp.y);
			 marker_ = addTrackMarkerArrow('arrow@#'+encodeURI(tmpscontent)+'@#'+res[i].id, x_ , y_ ,'','',path+"/track/arrow_/"+parseInt(degree/10)+".gif");
			 parent.arrowAllPoint.push(marker_);
			 fp.x = Number(res[i].deflectionx);
		 	 fp.y = Number(res[i].deflectiony);
		 }
		parent.trackAllPoint.push(tmpmarker);
		prevmarker = tmpmarker;
		prevTime = res[i].time;
		tmpmarker = addTrackMarker1('currentpoint',res[i].jmx,res[i].jmy,trackimage);
		parent.trackPointArr.push(tmpmarker);
		poiList.push(new MLngLat(res[i].jmx,res[i].jmy));
	}
	parent.trackAllLine = addTrackPolyline('trackline',poiList,'','');
	mapObj.addOverlay(parent.trackAllPoint[0] , false);
	mapObj.addOverlay(parent.trackAllPoint[parent.trackAllPoint.length-1] , false);
	mapObj.addOverlay(parent.trackAllLine,true);
	intervaltrackPoint();
	intervaltpObj=setInterval('intervaltrackPoint();',2000);
	// 回放进度lable
	var tmpplaytempolabel = parent.Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText('1/'+parent.trackPointArr.length);
	// 回放进度条
	var tmpplaytemposlider = parent.Ext.getCmp('playtemposlider');
	tmpplaytemposlider.setValue(1,true);
	tmpplaytemposlider.setMinValue(1);
	tmpplaytemposlider.setMaxValue(parent.trackPointArr.length);
	// 播放进度条总长度
	parent.sliderlen = res.length - 1;
	parent.sliderposition = 0;
	clearInterval(parent.playinterval);
	parent.stop();
	mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
}

var FINAL = 6378137.0;  
  
/** 
 * 求某个经纬度的值的角度值 
 * @param {Object} d 
 */  
function calcDegree(d){  
    return d*Math.PI/180.0;  
}  
  
/** 
 * 根据两点经纬度值，获取两地的实际相差的距离 
 * @param {Object} f    第一点的坐标位置[latitude,longitude] 
 * @param {Object} t    第二点的坐标位置[latitude,longitude] 
 */  
function calcDistance(f,t){  
    var flat = calcDegree(f[0]);  
    var flng = calcDegree(f[1]);  
    var tlat = calcDegree(t[0]);  
    var tlng = calcDegree(t[1]) ;  
 
    var result = Math.sin(flat)*Math.sin(tlat);  
    result += Math.cos(flat)*Math.cos(tlat)*Math.cos(flng-tlng);  
    return Math.acos(result)*FINAL;  
}

/** 
 * 返回时间差的天,小时,分钟格式
 * @param {Object} dt    时间差,单位:分钟 
 */  
function dateFormat(dt){
	var rd = Math.floor(dt / (60 * 24));   //得到天数
	var rh = Math.floor((dt % (60 * 24)) / 60);  //得到小时
	var rm = Math.floor((dt % (60 * 24)) % 60);  //得到分钟
	rd = rd ? (rd + '天') : '';
	rh = rh ? (rh + '小时') : '';
	rm = rm ? (rm + '分钟') : '';
	return rd + rh + rm ;
}