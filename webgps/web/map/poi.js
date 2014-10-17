//poi数组,用来控制poi的显示,所有已加载poi点
var visiPoiObjArr = new Array();
var globalMapPoiOverLayArr = new Array();

function visiPoiObj(poiid, mapLevel, poiType, overLay , editable){
	this.poiid = poiid;
	this.poiType = poiType;
	this.mapLevel = mapLevel;
	this.overLay = overLay;
	this.editable = editable;
}
//改变地图级别
function changeZoom(param){
	//alert('changeZoom');
	drawPoiOverLay(param.zoom);
}
//地图平移
function mapMoveEnd(param){
	//alert('mapMoveEnd');
	//mapObj.setCtrlPanelState(OVERVIEW_CTRL,MINIMIZE);
	//mapObj.removeAllOverlays();
	//var bounds=mapObj.getLngLatBounds();
	//result = isInBound(bounds, getMarkers());
	//addMarkers(result);
	drawPoiOverLay(getZoomLevel());
	//alert(tipopenpoiflag);
	//alert(tipopenpoiid.length);
	//alert(tipopenpoiflag && tipopenpoiid.length>0);
	
	//if(tipopenpoiflag && tipopenpoiid.length>0){
	//	mapObj.openOverlayTip(tipopenpoiid);
	//}
}
	
	/*var tipopenpoiid = '';
	var tipopenpoiflag = false;
	function poitipopen(param){
		alert('poitipopen');
		tipopenpoiid = param.overlayId;
		tipopenpoiflag = true;
	}
	
	function poitipclose(param){
		alert('poitipclose');
		tipopenpoiid = '';
		tipopenpoiflag = false;
	}*/
	
//var globalAddOverLayArr = new Array();
//var globalRemoveOverLayArr = new Array();


//画poi点
function drawPoiOverLay(zoom){
	var tmpAddOverLayArr = [];
	var tmpRemoveOverLayArr = [];
	var tmpglobalMapPoiOverLayArr = [];
	var bounds=mapObj.getLngLatBounds();
	var len = visiPoiObjArr.length;
	//if(len > 500){
	//	len = 500;
	//}
	//先遍历所有poi点,找出在当前地图级别下可显示的点
	for(var i = 0; i < len; i++){
		//alert(visiPoiObjArr[i].poiid);
		
		//判断poi点的显示级别和是否在地图视野范围内
		//alert('visiPoiObjArr[i].mapLevel:'+visiPoiObjArr[i].mapLevel+';'+(Number(visiPoiObjArr[i].mapLevel) <= Number(zoom)));
		//alert('地图视野范围内:'+isInBound(bounds, visiPoiObjArr[i].overLay));
		if((Number(visiPoiObjArr[i].mapLevel) <= Number(zoom))){
			var flag = true;
			for(var j = 0;j < globalMapPoiOverLayArr.length;j++){
				//if(visiPoiObjArr[i].poiType == '0' ){
					//tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
					if(visiPoiObjArr[i].poiid == globalMapPoiOverLayArr[j].poiid){
						tmpglobalMapPoiOverLayArr.push(visiPoiObjArr[i]);
						flag = false;
						break;
					}
				//}
			}
			if(flag&&isInBound(bounds, visiPoiObjArr[i].overLay)){
				tmpglobalMapPoiOverLayArr.push(visiPoiObjArr[i]);
				tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
			}
			
			
			/*if(flag){
				
				tmpglobalMapPoiOverLayArr.push(visiPoiObjArr[i]);
			}
			if(isInBound(bounds, visiPoiObjArr[i].overLay)){
				tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
			}*/
			
			/*
			if(isInBound(bounds, visiPoiObjArr[i].overLay)){
				var flag = true;
				for(var j = 0;j < globalMapPoiOverLayArr.length;j++){
					//if(visiPoiObjArr[i].poiType == '0' ){
						//tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
						if(visiPoiObjArr[i].poiid == globalMapPoiOverLayArr[j].poiid){
							tmpglobalMapPoiOverLayArr.push(visiPoiObjArr[i]);
							flag = false;
							break;
						}
					//}
				}
				
				if(flag){
					tmpglobalMapPoiOverLayArr.push(visiPoiObjArr[i]);
					tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
					//globalMapPoiOverLayArr.push(visiPoiObjArr[i]);
				}
			}
			*/
			
			//var tmpoverlay = mapObj.getOverlayById(visiPoiObjArr[i].poiid);
			//alert('tmpoverlay:'+tmpoverlay.id);
			//alert('tmpoverlay == null:'+(tmpoverlay == null));
			//if(visiPoiObjArr[i].poiType == '0' && tmpoverlay == null){
				//判断地图上是否有这个点
			//	tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
				//mapObj.addOverlay(visiPoiObjArr[i].overLay,false);
			//}
		}else{
			//alert('removeOverlay:'+visiPoiObjArr[i].overLay);
			//mapObj.removeOverlay(visiPoiObjArr[i].overLay);
			
			for(var j = 0;j < globalMapPoiOverLayArr.length;j++){
				//if( visiPoiObjArr[i].poiType == '0' && visiPoiObjArr[i].poiid == globalMapPoiOverLayArr[j].poiid ){
				if( visiPoiObjArr[i].poiid == globalMapPoiOverLayArr[j].poiid ){
					tmpRemoveOverLayArr.push(visiPoiObjArr[i].overLay);
				}
			}
			
			/*var tmpoverlay = mapObj.getOverlayById(visiPoiObjArr[i].poiid);
			//判断地图上是否有这个点
			if(tmpoverlay == null){
			}else{
				tmpRemoveOverLayArr.push(visiPoiObjArr[i].overLay);
			}*/
		}
	}
	
	globalMapPoiOverLayArr = tmpglobalMapPoiOverLayArr;
	
	//alert('tmpAddOverLayArr.length:'+tmpAddOverLayArr.length);
	//alert('tmpRemoveOverLayArr.length:'+tmpRemoveOverLayArr.length);
	//alert('globalMapPoiOverLayArr.length:'+globalMapPoiOverLayArr.length);
	
	mapObj.addOverlays(tmpAddOverLayArr,false);
	mapObj.removeOverlays(tmpRemoveOverLayArr);
	
}

//判断当前地图界中包含待标注点集合
function isInBound(bounds,marker)
{
	//获取当前地图界
	var southWestX = bounds.southWest.lngX;//西南角的经纬度坐标
	var southWestY = bounds.southWest.latY;//西南角的经纬度坐标
	var northEastX = bounds.northEast.lngX;//东北角的经纬度坐标
	var northEastY = bounds.northEast.latY;//东北角的经纬度坐标
	//alert(southWestX +","+southWestY +"  "+northEastX +","+northEastY);
	//判别待标注点是否落入当前bounds
	var lngX = marker.lnglat.lngX;
	var latY = marker.lnglat.latY;
	if((lngX > southWestX) && (lngX < northEastX) && (latY < northEastY) && (latY > southWestY)){
		return true;
	}
	return false;
}

//查询登录用户的可见图层和poi点
function listVisibleLayerAndPoi(){
	
	/*for(var i = 0; i < visiPoiObjArr.length; i++){
		mapObj.removeOverlay(visiPoiObjArr[i].overLay);
	}*/
	var tmpOverLayArr = new Array();
	for(var i = 0; i < visiPoiObjArr.length; i++){
		tmpOverLayArr.push(visiPoiObjArr[i].overLay);
	}
	//查询前先清除地图上的poi点
	removeOverlays(tmpOverLayArr);
	
	Ext.Ajax.request({
		//url :url,
		url:path+'/poi/poi.do?method=listVisibleLayerAndPoi',
		method :'POST', 
		params: { start: '1', limit : '65535'},
		timeout : 60000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
		    //alert(res.data.length);
		    parseLayerPoiData(res);
		    drawPoiOverLay(getZoomLevel());
		    //alert(request.responseText);
		    //alert(res);
		},
		failure : function(request) {
		}
	});
}


//解析查询结果
function parseLayerPoiData(res){
	//addLabelMarker();
	visiPoiObjArr = [];
	//globalAddOverLayArr = [];
	//globalRemoveOverLayArr = [];
	globalMapPoiOverLayArr = [];
	
	for(var i = 0; i < res.data.length; i++){
		var layerid = res.data[i].id;
		var tmpmapLevel = res.data[i].mapLevel;
		var tmpeditable = res.data[i].editable;
		var tmppois = res.data[i].pois;
		
		if(tmppois.length > 0){
			for(var j = 0;j < tmppois.length;j++){
				var tmppoiid = tmppois[j].id;
				var tmppoiType = tmppois[j].poiType;
				var tmppoiDatas = tmppois[j].poiDatas;
				var tmplnglat = tmppoiDatas.split(',');
				var tmppoiEncryptDatas = tmppois[j].poiEncryptDatas;
				var tmpiconpath = path+'/images/poi/'+tmppois[j].iconpath;
				var tmppoiName = tmppois[j].poiName;
				var tmpaddress = tmppois[j].address;
				var tmppoiDesc = tmppois[j].poiDesc;
				var tmptelephone = tmppois[j].telephone;
				var tmpvisitDistance = tmppois[j].visitDistance;
				
				var tmppoiOverLay = null;
				/*var sContent = '名字:'+tmppoiName+'<br>'+
				'地址:'+tmpaddress+'<br>'+
				'电话:'+tmptelephone+'<br>'+
				'备注:'+tmppoiDesc+'<br>';
				*/
				var param = '?id='+tmppoiid+'&name='+tmppoiName+'&address='+tmpaddress+'&telephone='+tmptelephone+
				'&desc='+tmppoiDesc+'&visitDistance='+tmpvisitDistance+'&iconpath='+tmppois[j].iconpath+'&layerid='+layerid
				+'&poiDatas='+tmppoiDatas+'&poiEncryptDatas='+tmppoiEncryptDatas + '&editable=' + tmpeditable;
				
				var sContent='<iframe  src="'+path+'/map/poitip.jsp'+param+'" width="100%" height="190" marginwidth="0" framespacing="0" marginheight="0" scrolling="no" frameborder="0" border="0" ></iframe>';  //tip内容
            	//alert(sContent);
				//poi类型为点,并且有经纬度坐标
				if(tmppoiType == '0' && tmplnglat.length == 2){
					tmppoiOverLay = addLabelMarker('visiPoi@'+tmppoiid,tmplnglat[0],tmplnglat[1],tmppoiName,sContent,tmpiconpath);
					//alert(tmppoiOverLay);
					//tmp.push(tmppoiOverLay);
					//map.mapObj.removeOverlay(tmppoiOverLay);
					var tmpvisiPoiObj = new visiPoiObj('visiPoi@'+tmppoiid, tmpmapLevel, tmppoiType, tmppoiOverLay, tmpeditable);
					visiPoiObjArr.push(tmpvisiPoiObj);
				}
				
			}
		}
	}
	
	/*var tmp = new Array();
	for(var i=0;i<visiPoiObjArr.length;i++){
		tmp.push(visiPoiObjArr[i].overLay);
		
	}
	alert('tmp.length:'+tmp.length);
	mapObj.addOverlays(tmp,false);*/
}
