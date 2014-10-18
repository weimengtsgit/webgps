
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
//�϶�
	function resetControls(){
		mapObj.setCurrentMouseTool(PAN_WHEELZOOM);
	}
	//�Ŵ�
	function setZoomIn(){
		mapObj.zoomIn(mapObj.getCenter()); 
	}
	//��С
	function setZoomOut(){
		mapObj.zoomOut(mapObj.getCenter());
	}
	//���
	function rulerByMouseTool(){
	  var option={};   
	  option.hasCircle=false;   
	  option.hasPrompt=true;   
	  var test=mapObj.setCurrentMouseTool(RULER,option);
	}
	
	//�����
	function compute_areaBymouseTool(){
	  var option={};   
	  option.hasCircle=false;   
	  option.hasPrompt=true;   
	  var test=mapObj.setCurrentMouseTool(COMPUTE_AREA,option);
	}
	
	//���
	function clear(){
		globalMapPoiOverLayArr = [];
		parent.locationPointArr = [];
		mapObj.removeAllOverlays();
	}
	var fullmapflag = false;
	//���ŵ�ͼ
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
// �����
function PointMarker(pointID,lng,lat,label,sContent,imageUrl,bZoomTo){
	this.pointID = pointID;
	this.lng = lng;
	this.lat = lat;
	this.label = label;
	this.sContent = sContent;
	this.imageUrl = imageUrl;
	this.bZoomTo = bZoomTo;
}
//������ͼ�����
//modify by zhaofeng
function createPointMarker(pointMarker){
  var labelOptions=new MLabelOptions();   
  var fontstyle = new MFontStyle();  //�������������
  fontstyle.name ="Arial";
  fontstyle.size = 12;
  fontstyle.color = 0xFFFFFF;
  fontstyle.bold = true;
  labelOptions.fontStyle=fontstyle;
  labelOptions.content= pointMarker.label;
  labelOptions.hasBorder =true;
  labelOptions.hasBackground =true;
  //labelOptions.backgroundColor  =0x0000FF;
  //��λͼ����ɫ
  labelOptions.backgroundColor  =0xCC3300;
  
  
	var tipOption = new MTipOptions(); 
    tipOption.titleFontStyle.size=18; 
    tipOption.fillStyle.color=0xFFFFCC;
    tipOption.content=pointMarker.sContent; 
    var markerOption = new MMarkerOptions(); 
    //markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  	//markerOption.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
	//������Marker��Label����ΪͼƬ����ߣ�
	if(pointMarker.pointID.substr(0,6) == "alarm_"){  
  	    //markerOption.labelPosition = new  MPoint(-110,0); 
	}else{
  	    //markerOption.labelPosition = new  MPoint(0,0); 
	}
  	markerOption.isDraggable=false;//�Ƿ�����϶�  
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
	 * ��Ӽ����¼�
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
// ������һ����
function addPointMarker(pointMarker){
	var marker = createPointMarker(pointMarker);
	mapObj.addOverlay(marker,pointMarker.bZoomTo); 	
	mapObj.addEventListener(marker,MOUSE_DOWN,mouseDowna);
}
function mouseDowna(e) {
	// do nothing
}

// ������
function addPointMarkers(pointMarkers, bZoomTo){
	var ps = new Array();
	for(var i=0;i<pointMarkers.length;i++){
		marker = createPointMarker(pointMarkers[i]);
		ps.push(marker);
	}
	mapObj.addOverlays(ps, bZoomTo);
	return ps;
}
// �����ͼ�����и�����
/*function clearMap(){
	//alert('clearMap');
	mapObj.removeAllOverlays();
}*/
// ���id��Ӧ�ĵ�ͼ�ϸ�����
function clearMapById(id){
	mapObj.removeOverlayById(id);
}

/**
 * �켣�طŲ�����ͼ����
 */
/**
 * ����
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
    	markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  		markerOption.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
  		markerOption.isDraggable=false;//�Ƿ�����϶�  
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
 * �õ��켣��
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
		markerOption.isDraggable=false;//�Ƿ�����϶�
		markerOption.imageAlign=MIDDLE_CENTER;//����ͼƬê�������ͼƬ��λ��
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
    	markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  		markerOption.labelStyle.bold = false;//�Ƿ���壬Ĭ�Ϸ� 
  		markerOption.isDraggable=false;//�Ƿ�����϶�  
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
  var fontstyle = new MFontStyle();  //�������������
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
    	//markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  		//markerOption.labelStyle.bold = false;//�Ƿ���壬Ĭ�Ϸ� 
  		markerOption.isDraggable=false;//�Ƿ�����϶�  
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
    	//markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  		//markerOption.labelStyle.bold = false;//�Ƿ���壬Ĭ�Ϸ� 
  		markerOption.isDraggable=false;//�Ƿ�����϶�  
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
  var fontstyle = new MFontStyle();  //�������������
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
    	//markerOption.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
  		//markerOption.labelStyle.bold = false;//�Ƿ���壬Ĭ�Ϸ� 
  		markerOption.isDraggable=false;//�Ƿ�����϶�  
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
 * �õ��켣��
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
	    //�켣����ɫ�Ϳ��
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
 * ��������
 * @param {} lineID
 * @param {} points
 * @param {} label
 * @param {} title
 * @param {} bZoomTo
 */
function addPolyline(lineID,points,label,title,bZoomTo){
  	if( points==null || points.length==0) return;
 	//var tipOption = new MTipOptions(); //����tip��ʽ   
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
//�������
function addPolygon(arr,title,content,id,bZoomTo){//�ڵ�ͼ�ϻ������      

    var areopt = new MAreaOptions();//����һ����Ϊareopt����ѡ�����   
    var tipOption = new MTipOptions();   
    tipOption.title=title;  // "�����"
    tipOption.content=content ;//"����һ������Σ�";   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=true;//��ѡ��Ƿ���ж�̬��Ĭ��Ϊfalse��û�ж�̬   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id ; //"polygon101";   
    mapObj.addOverlay(polygonAPI,bZoomTo);   
} 

//������-�����ش�����
 function addRectangle(areaxy,title,content,id,bZoomTo){ //�ڵ�ͼ�ϻ�����      
 	var arr=new Array();   
 	//�����ַ� JIOMYSINMUDLLH,LNGUTKHRRLHDD#JIPMPKPSJPDLHL,LNGUURLNLLHPH
 	var areaxyArr = areaxy.split('#');
	for(var i=0;i<areaxyArr.length;i++){
		var tmpxyArr = areaxyArr[i].split(',');
		arr.push(new MLngLat(tmpxyArr[0],tmpxyArr[1]));  
	}
  var lineopt = new MAreaStyle();   
  lineopt.borderStyle.alpha = 0.7; //͸���ȣ�Ĭ��1����Χ0~1   
  lineopt.borderStyle.color = 0x00FF33; //����ɫ��Ĭ�Ϻ�ɫ   
  lineopt.borderStyle.thickness = 3;  //�ߴ�ϸ�ȣ�Ĭ��3    
  lineopt.borderStyle.lineType=LINE_SOLID;   
  lineopt.fillStyle.alpha = 0.3;   
  lineopt.fillStyle.color = 0x99FF33;   
  var tipOption = new MTipOptions();   
  tipOption.title=title;   
  tipOption.anchor =  new MPoint(0,0);   //ͼƬê���㣬MPoint����   
  tipOption.content=content;  //tip����   
  tipOption.hasShadow= false; //�Ƿ�����Ӱ   
  var areopt = new MAreaOptions();   
  areopt.areaStyle=lineopt;   
  areopt.tipOption=tipOption;   
  areopt.canShowTip = true;   
  areopt.isDimorphic=true;//��ѡ��Ƿ���ж�̬��Ĭ��Ϊfalse��û�ж�̬   
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
    		//ȡ�õ��λ������
    		var content_decodeURI = decodeURI(tmpidArr[1]);
    		var loc_desc = content_decodeURI.split('<br>')[3].split(',');
    		 if(loc_desc.toString().length==7)
    		 {
    			 if(content_.length > 0){
  				   mapObj.openOverlayTip(tmpid);
  				   return;
    			 }
    		//�õ���ľ�γ��	
    			var x=marker.lnglat.lngX;
    			var y=marker.lnglat.latY;
    		//ͨ����γ���ύ��̨������ӦlocDesc���������õ�λ����Ϣ
    			Ext.Ajax.request({
    				 url:path+'/locate/locate.do?method=locDesc',
    				 method :'POST', 
    				 params: {x:x,y:y},
    				 success : function(request){
    				 var res= request.responseText;
    				   if(res==null || res.length==0){
//    				       Ext.Msg.alert('��ʾ', "δ�鵽����!");
//    				       return;
    					   res = main.data_not_found;
    				   }
    		//�滻��Ϣ���е�λ����Ϣ		  
    				   var content=content_decodeURI.replace(loc_desc,"λ������ :"+res);
    		//����λ�������Ƚϣ�����ͬ���滻ԭ�������ݣ���ͬ�ͷ��ء�
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
    					 //alert("��ѯʧ��");
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
 

 var EARTH_RADIUS = 6378137.0;    //��λM
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
 * �����켣�طŲ�ѯ���,�ڵ�ͼ����ʾ�����
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
	//��ʾ���е�
	parent.trackAllPoint = [];
	parent.arrowAllPoint = [];
	intervaltpflag = false;
	intervaltplen = 0;
	clearInterval(intervaltpObj);
		//��������ʹ��
	parent.trackPointArr = new Array();
	//ǰһ���켣���ʱ��,�����Ƚ�������֮���ʱ��
	var prevTime = '';
	//ǰһ���켣�����,�Ƚ�������֮��ļ��ʱ��
	var prevmarker;
	var trackList = new Array();
	var poiList = new Array();
	var tmpimage = path+'/track/images/track/redpoint.gif';
	//var tmpimage = path+'/track/images/track/browndot.jpg';
	var first = true;
	var prevmarkerisblue = false;
	var marker_ = null;
	
	//modify by liuhongxiao 2012-02-07
	//��Ա
	if(Number(parent.trackSearchDevicelocateType) == '0'){
		for(var i=0;i<res.length;i++){
			if(prevTime.length>0){
				//�ж�������ʱ����
				var tmpsubtime = dateSub(prevTime,res[i].time);
				if((tmpsubtime > parent.filtertimeparam) && (parent.filtertimeparam!=0)){
					//���ʱ���������û�����ֵ,��������ͼ��Ϊ��ɫ
					tmpimage = path+'/track/images/track/bluepoint.png';
					//tmpimage = path+'/track/images/track/browndot.jpg';
					//update by zhaofeng 2011-5-24 15:31 
					//������һ�����ͼ��(�ڲ�������������޸�)
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
			var tmpscontent = '��    �� : '+parent.trackSearchDevicevehicleNumber+
			'<br>�ֻ����� : '+parent.trackSearchDevicesimcard+
			'<br>ʱ    �� : '+res[i].time+
			//'<br>�ն�id:'+res[i].deviceId+
			//'<br>�ն�����:'+"GPS"+
			//'<br>��    �� : '+res[i].speed+'  km/h'+
			'<br>λ������ : '+res[i].pd+
			'<br>��    �� : '+'�����  '+global+'  km����ǰһ��  '+res[i].distance+'  km';
			//��    ��:  �����0.943�����ǰһ��'+res[i].distance+'km'
			var tmpmarker = addTrackMarker('pointall@#'+encodeURI(tmpscontent)+'@#'+res[i].id,res[i].jmx,res[i].jmy,'',tmpscontent,tmpimage);
		
			if(is_arrow){
				if(pre_flag){
	 				tp.x = Number(res[i].deflectionx);
	 				tp.y = Number(res[i].deflectiony);
	 				//��γ�ȸ㷴��
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
	//����
	else{
		//ǰһ���켣�㾭γ��,�Ƚ�������֮���ֱ�߾���
		var prevPoint;
		//������ʻ״̬��ʶ,0-������,1-ͣ��Ϩ��,2-ͣ��δϨ��
		var carStatus = 0;
		
		for(var i=0;i<res.length;i++){
			var carDistance = '';//��ǰһ���������
			var carSubtime = '';//ͣ��ʱ������
			var deflectiony = Number(res[i].deflectiony);
			var deflectionx = Number(res[i].deflectionx);
			//�����
			global += Number(res[i].distance);
			global = Math.round((Math.floor(global*1000)/10))/100;
			//���
			if(i == 0){
				tmpimage = path+'/track/images/track/qidian.png';
				carDistance = '��ǰһ��  '+res[i].distance+'  km';
				if(!parent.filterCarStop){
				
					if(res[i].accStatus == '0' && res[i].speed == '0.0'){
						carStatus = 1;
					}
					//ͣ��δϨ��
					else if(res[i].accStatus == '1' && res[i].speed == '0.0'){
						carStatus = 2;
					}
				}
			}
			//�յ�
			else if(i == (res.length-1)){
				tmpimage =  path+'/track/images/track/zhongdian.png';
				//����ͣ��Ϩ��
				if(!parent.filterCarStop && res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 1){
					//�ж�������ʱ����
					var tmpsubtime = dateSub(prevTime,res[i].time);
					var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
					tmpmarker.option.imageUrl = tmpimage;
					if(tmpsubtime > parent.filterCarInterval){
						var labelOptions=new MLabelOptions();   
						var fontstyle = new MFontStyle();  //�������������
						fontstyle.name ="Arial";
						fontstyle.size = 12;
						fontstyle.color = 0xFFFFFF;
						fontstyle.bold = true;  
						labelOptions.fontStyle=fontstyle;
						labelOptions.content= 'ͣ��Ϩ��:'+dateFormat(Math.round(tmpsubtime));
						labelOptions.hasBorder =true;
						labelOptions.hasBackground =true;
						labelOptions.backgroundColor  =0x6666FF;
						tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
				  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
				  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
				  		tmpmarker.option.hasShadow=false;
				  		tmpmarker.option.labelOption=labelOptions;
					}
					break;
				}
				//����ͣ��δϨ��
				else if(!parent.filterCarStop && res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 2){
					//�ж�������ʱ����
					var tmpsubtime = dateSub(prevTime,res[i].time);
					var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
					tmpmarker.option.imageUrl = tmpimage;
					if(tmpsubtime > parent.filterCarInterval){
						var labelOptions=new MLabelOptions();   
						var fontstyle = new MFontStyle();  //�������������
						fontstyle.name ="Arial";
						fontstyle.size = 12;
						fontstyle.color = 0xFFFFFF;
						fontstyle.bold = true;  
						labelOptions.fontStyle=fontstyle;
						labelOptions.content= 'ͣ��δϨ��:'+dateFormat(Math.round(tmpsubtime));
						labelOptions.hasBorder =true;
						labelOptions.hasBackground =true;
						labelOptions.backgroundColor  =0xFF0000;
						
						tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
				  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
				  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
				  		tmpmarker.option.hasShadow=false;
				  		tmpmarker.option.labelOption=labelOptions;
					}
					break;
				}else{
					carDistance = '��ǰһ��  '+res[i].distance+'  km';
				}
			}
			//�м��
			else{
				if(!parent.filterCarStop){
					//����ͣ��Ϩ��
					if(res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 1){
						continue;
					}
					//����ͣ��δϨ��
					else if(res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 2){
						continue;
					}
					//ͣ��δϨ���Ϊͣ��Ϩ��
					else if(res[i].accStatus == '0' && res[i].speed == '0.0' && carStatus == 2){
						carStatus = 1;
						//�ж�������ʱ����
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //�������������
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= 'ͣ��δϨ��:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0xFF0000;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
					  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
					  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//�ж�������ʵ�ʾ���
				  		var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
				  		carDistance = '��ǰһ��  '+Math.round(tmpdistance/10)/100+'  km';
				  		tmpimage = path+'/track/images/track/stopcar1.png';
					}
					//ͣ��Ϩ���Ϊͣ��δϨ��
					else if(res[i].accStatus == '1' && res[i].speed == '0.0' && carStatus == 1){
						carStatus = 2;
						//�ж�������ʱ����
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //�������������
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= 'ͣ��Ϩ��:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0x6666FF;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
					  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
					  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//�ж�������ʵ�ʾ���
				  		var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
				  		carDistance = '��ǰһ��  '+Math.round(tmpdistance/10)/100+'  km';
				  		tmpimage = path+'/track/images/track/stopcar2.png';
					}
					else if(res[i].accStatus == '0' && res[i].speed == '0.0'){
						carStatus = 1;
						carDistance = '��ǰһ��  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/stopcar1.png';
					}
					//ͣ��δϨ��
					else if(res[i].accStatus == '1' && res[i].speed == '0.0'){
						carStatus = 2;
						carDistance = '��ǰһ��  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/stopcar2.png';
					}
					//ͣ��Ϩ���Ϊ����
					else if(carStatus == 1){
						carStatus = 0;
						//�ж�������ʱ����
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //�������������
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= 'ͣ��Ϩ��:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0x6666FF;
							
							tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
					  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
					  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//�ж�������ʵ�ʾ���
						var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
						carDistance = '��ǰһ��  '+Math.round(tmpdistance/10)/100+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
					//ͣ��δϨ���Ϊ����
					else if(carStatus == 2){
						carStatus = 0;
						//�ж�������ʱ����
						var tmpsubtime = dateSub(prevTime,res[i].time);
						var tmpmarker = parent.trackAllPoint[parent.trackAllPoint.length-1];
						if(tmpsubtime > parent.filterCarInterval){
							var labelOptions=new MLabelOptions();   
							var fontstyle = new MFontStyle();  //�������������
							fontstyle.name ="Arial";
							fontstyle.size = 12;
							fontstyle.color = 0xFFFFFF;
							fontstyle.bold = true;  
							labelOptions.fontStyle=fontstyle;
							labelOptions.content= 'ͣ��δϨ��:'+dateFormat(Math.round(tmpsubtime));
							labelOptions.hasBorder =true;
							labelOptions.hasBackground =true;
							labelOptions.backgroundColor  =0xFF0000;
							tmpmarker.option.labelStyle.color = 0x000000;  //������ɫ��Ĭ�Ϻ�ɫ 
					  		tmpmarker.option.labelStyle.bold = true;//�Ƿ���壬Ĭ�Ϸ� 
					  		tmpmarker.option.isDraggable=false;//�Ƿ�����϶�  
					  		tmpmarker.option.hasShadow=false;
					  		tmpmarker.option.labelOption=labelOptions;
						}else{
							tmpmarker.option.imageUrl = path+'/track/images/track/redpoint.gif';
						}
						//�ж�������ʵ�ʾ���
						var tmpdistance = calcDistance(prevPoint,[deflectiony,deflectionx]);
						carDistance = '��ǰһ��  '+Math.round(tmpdistance/10)/100+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}else{
						carStatus = 0;
						carDistance = '��ǰһ��  '+res[i].distance+'  km';
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
				}else{
					if(res[i].accStatus == '0'){
						tmpimage = path+'/track/images/track/bluepoint.png';
					}else{
						tmpimage = path+'/track/images/track/redpoint.gif';
					}
					carDistance = '��ǰһ��  '+res[i].distance+'  km';
				}
			}
			
			var temper=res[i].temperature!=''?'<br>��    �� : '+res[i].temperature+'��':'';
			var temhumidity=res[i].humidity!=''?'<br>ʪ    �� : '+res[i].humidity+'%':'';
			var trackimage = path+'/track/images/track/car.gif';
			var tmpscontent = '���ƺ��� : '+parent.trackSearchDevicevehicleNumber+
			'<br>�ֻ����� : '+parent.trackSearchDevicesimcard+
			'<br>ʱ    �� : '+res[i].time+
			//'<br>�ն�id:'+res[i].deviceId+
			//'<br>�ն�����:'+"GPS"+
			//update by zhaofeng
			'<br>λ������ : '+res[i].pd+
			'<br>��    �� : '+res[i].speed+'  km/h'+
			//�¶�
			temper+
			//ʪ��
			temhumidity+
			'<br>��    �� : '+'�����  '+global+'  km��'+
			//��ǰһ�����
			carDistance;
			var tmpmarker = addTrackMarker('pointall@#'+encodeURI(tmpscontent)+'@#'+res[i].id,res[i].jmx,res[i].jmy,'',tmpscontent,tmpimage);
		
			if(is_arrow){
				if(pre_flag){
	 				tp.x = Number(res[i].deflectionx);
	 				tp.y = Number(res[i].deflectiony);
	 				//��γ�ȸ㷴��
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
	
	//�طŽ���lable
	var tmpplaytempolabel = parent.Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText('1/'+parent.trackPointArr.length);
	//�طŽ�����
	var tmpplaytemposlider = parent.Ext.getCmp('playtemposlider');
	tmpplaytemposlider.setValue(1,true);
	tmpplaytemposlider.setMinValue(1);
	tmpplaytemposlider.setMaxValue(parent.trackPointArr.length);
	//���Ž������ܳ���
	parent.sliderlen = parent.trackPointArr.length - 1;
	parent.sliderposition = 0;
	clearInterval(parent.playinterval);
	parent.stop();
	
	//mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
}


//�������,��������ķ���
function dateSub(strDate_1,strDate_2){
	strDate_1=strDate_1.replace(/-/g,"/");
	strDate_2=strDate_2.replace(/-/g,"/");
	var  date1  =  Date.parse(strDate_1);
	var  date2  =  Date.parse(strDate_2);
	return (date2-date1)/(60*1000);
}


//�ڵ�ͼ�ϻ������    
function AreaAlarmaddPolygon(tmpxyArr, title, content, id, bZoomTo ,isDimorphic){  
	var arr = new Array();
	for(var i = 0;i < tmpxyArr.length;i++){
		var tmpxy = tmpxyArr[i].split(',');
	    arr.push(new MLngLat(tmpxy[0],tmpxy[1])); 
	}
    var areopt = new MAreaOptions();//����һ����Ϊareopt����ѡ�����   
    var tipOption = new MTipOptions();   
    tipOption.title=title;   
    tipOption.content=content;   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=isDimorphic;//��ѡ��Ƿ���ж�̬��Ĭ��Ϊfalse��û�ж�̬   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id;   
    mapObj.addOverlay(polygonAPI,bZoomTo);   
}  
//�õ�����ζ���
function getPolygon(tmpxyArr, title, content, id, isDimorphic){  
	var arr = new Array();
	for(var i = 0;i < tmpxyArr.length;i++){
		var tmpxy = tmpxyArr[i].split(',');
	    arr.push(new MLngLat(tmpxy[0],tmpxy[1])); 
	}
    var areopt = new MAreaOptions();//����һ����Ϊareopt����ѡ�����   
    var tipOption = new MTipOptions();   
    tipOption.title=title;   
    tipOption.content=content;   
    tipOption.hasShadow=true;   
    areopt.tipOption=tipOption;   
    areopt.canShowTip = true;   
    areopt.isDimorphic=isDimorphic;//��ѡ��Ƿ���ж�̬��Ĭ��Ϊfalse��û�ж�̬   
    areopt.dimorphicColor=0x00ff00;   
    polygonAPI = new MPolygon(arr,areopt);   
    polygonAPI.id=id;   
    return polygonAPI;
    //mapObj.addOverlay(polygonAPI,bZoomTo);   
}  
//������
 /*function addRectangle(){ //�ڵ�ͼ�ϻ�����      
  //var ll=mapObj.getCenter();   
  var arr=new Array();   
  arr.push(new MLngLat('JIOMYSINMUDLLH','LNGUTKHRRLHDD'));   
  arr.push(new MLngLat('JIPMPKPSJPDLHL','LNGUURLNLLHPH'));   
  var lineopt = new MAreaStyle();   
  lineopt.borderStyle.alpha = 0.7; //͸���ȣ�Ĭ��1����Χ0~1   
  lineopt.borderStyle.color = 0x00FF33; //����ɫ��Ĭ�Ϻ�ɫ   
  lineopt.borderStyle.thickness = 3;  //�ߴ�ϸ�ȣ�Ĭ��3    
  lineopt.borderStyle.lineType=LINE_SOLID;   
  lineopt.fillStyle.alpha = 0.3;   
  lineopt.fillStyle.color = 0x99FF33;   
  var tipOption = new MTipOptions();   
  tipOption.title="����";   
  tipOption.anchor =  new MPoint(0,0);   //ͼƬê���㣬MPoint����   
  tipOption.content="����һ������";  //tip����   
  tipOption.hasShadow= false; //�Ƿ�����Ӱ   
  var areopt = new MAreaOptions();   
  areopt.areaStyle=lineopt;   
  areopt.tipOption=tipOption;   
  areopt.canShowTip = true;   
  areopt.isDimorphic=true;//��ѡ��Ƿ���ж�̬��Ĭ��Ϊfalse��û�ж�̬   
  areopt.dimorphicColor=0x00FFFF;   
  rectangleAPI = new MRectangle(arr,areopt);         
  rectangleAPI.id="rectangle101";      
  mapObj.addOverlay(rectangleAPI,true);      
 }    */

//�����ȡλ������
//add by zhaofeng

function update(p){ 
	var tmpid =p.overlayId;
	
//ȡ�õ����
	var obj = mapObj.getOverlayById(p.overlayId);
	obj.option.canShowTip=true;
//ȡ�õ��λ������
	var loc_desc = obj.option.tipOption.content.split('<br>')[3].split(',');
 if(loc_desc.toString().length==19)
 {
//�õ���ľ�γ��	
	var x=obj.lnglat.lngX;
	var y=obj.lnglat.latY;
	
//ͨ����γ���ύ��̨������ӦlocDesc���������õ�λ����Ϣ
	Ext.Ajax.request({
		 url:path+'/locate/locate.do?method=locDesc',
		 method :'POST', 
		 params: {x:x,y:y},
		 success : function(request){
		   var res= request.responseText;
		   if(res==null || res.length==0){
//		       Ext.Msg.alert('��ʾ', "δ�鵽����!");
//		       return;
		       res = main.data_not_found;
		   }
//�õ���Ϣ���е�����
		   var scontent = obj.option.tipOption.content;
//�滻��Ϣ���е�λ����Ϣ		   
		   var content=scontent.replace(loc_desc,"λ&nbsp;&nbsp;�ã�"+res);
		   obj.option.tipOption.content=content;
		   mapObj.updateOverlay(obj);
		   mapObj.openOverlayTip(tmpid);
		   },
		 failure : function(request) {
			 alert("��ѯʧ��");
		 }
		});	
 }
 else
 {
	   
	   mapObj.openOverlayTip(tmpid);
 }
}


/*
 * ��־����-�켣��ѯ
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
	// ǰһ���켣���ʱ��,�����Ƚ�������֮���ʱ��
	var prevTime = '';
	// ǰһ���켣�����,�Ƚ�������֮��ļ��ʱ��
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
		var temper=res[i].temperature!=''?'<br>��    �� : '+res[i].temperature:'';
		var tmpscontent = '';
		global += Number(res[i].distance);
		global = Math.round((Math.floor(global*1000)/10))/100;
		var trackimage = path+'/track/images/track/2.gif';
		if(Number(locType) == 1){
			tmpscontent = '���ƺ��� : '+parent.trackSearchDevicevehicleNumber+
			'<br>�ֻ����� : '+parent.trackSearchDevicesimcard+
			'<br>ʱ    �� : '+res[i].time+
			'<br>��    �� : '+res[i].speed+'  km/h'+
			'<br>λ������ : '+res[i].pd+
			temper+
			'<br>��    �� : '+'�����  '+global+'  km����ǰһ��  '+res[i].distance+'  km';
			trackimage = path+'/track/images/track/car.gif';
		}else{
			tmpscontent = '��    �� : '+parent.trackSearchDevicevehicleNumber+
			'<br>�ֻ����� : '+parent.trackSearchDevicesimcard+
			'<br>ʱ    �� : '+res[i].time+
			'<br>λ������ : '+res[i].pd+
			'<br>��    �� : '+'�����  '+global+'  km����ǰһ��  '+res[i].distance+'  km';
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
	// �طŽ���lable
	var tmpplaytempolabel = parent.Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText('1/'+parent.trackPointArr.length);
	// �طŽ�����
	var tmpplaytemposlider = parent.Ext.getCmp('playtemposlider');
	tmpplaytemposlider.setValue(1,true);
	tmpplaytemposlider.setMinValue(1);
	tmpplaytemposlider.setMaxValue(parent.trackPointArr.length);
	// ���Ž������ܳ���
	parent.sliderlen = res.length - 1;
	parent.sliderposition = 0;
	clearInterval(parent.playinterval);
	parent.stop();
	mapObj.addEventListener(mapObj,MOUSE_CLICK,mouseClick);
}

var FINAL = 6378137.0;  
  
/** 
 * ��ĳ����γ�ȵ�ֵ�ĽǶ�ֵ 
 * @param {Object} d 
 */  
function calcDegree(d){  
    return d*Math.PI/180.0;  
}  
  
/** 
 * �������㾭γ��ֵ����ȡ���ص�ʵ�����ľ��� 
 * @param {Object} f    ��һ�������λ��[latitude,longitude] 
 * @param {Object} t    �ڶ��������λ��[latitude,longitude] 
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
 * ����ʱ������,Сʱ,���Ӹ�ʽ
 * @param {Object} dt    ʱ���,��λ:���� 
 */  
function dateFormat(dt){
	var rd = Math.floor(dt / (60 * 24));   //�õ�����
	var rh = Math.floor((dt % (60 * 24)) / 60);  //�õ�Сʱ
	var rm = Math.floor((dt % (60 * 24)) % 60);  //�õ�����
	rd = rd ? (rd + '��') : '';
	rh = rh ? (rh + 'Сʱ') : '';
	rm = rm ? (rm + '����') : '';
	return rd + rh + rm ;
}