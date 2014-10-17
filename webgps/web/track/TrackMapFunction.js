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
function addMarker(pointID,lng,lat,label,sContent,imageUrl){
	//var map = document.getElementById('mapifr').contentWindow;
	/*var tempMarker=mapObj.getOverlayById(pointID);
	if(tempMarker!=null){
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
  		markerOption.canShowTip= false;  
  		markerOption.hasShadow=false;
  		markerOption.picAgent=false;
  		markerOption.imageAlign=5;//
		markerOption.label=label;
		markerOption.imageUrl = imageUrl;
		markerOption.tipOption.tipType=FLASHTIP;
		var ll = new  MLngLat(lng,lat); 
		var marker = new MMarker(ll,markerOption);
		marker.id=pointID;
		//mapObj.addOverlay(marker,bZoomTo); 	
		//mapObj.addEventListener(marker,MOUSE_DOWN,mouseDowna);
		return marker;
	//}
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
	//var map = document.getElementById('mapifr').contentWindow;
  	if( points==null || points.length==0) return;
 	var tipOption = new MTipOptions(); //����tip��ʽ   
    tipOption.title=title;   
    tipOption.anchor =  new MPoint(0,0);      
    tipOption.content=label;
    var lineopt  = new MLineOptions(); 
    var mlineStyle = new MLineStyle();
    //mlineStyle.color=0x0000ff;
    //mlineStyle.thickness=2;
    mlineStyle.color=0xffffff;
    mlineStyle.thickness=1;
    lineopt.lineStyle=mlineStyle;  
    lineopt.tipOption = tipOption;   
    lineopt.canShowTip = true;   
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
