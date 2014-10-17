//解析查询结果
/*function parseLayerPoiData(res){
	layerArr = [];
	poiIdArr = [];
	poiOverLayArr = [];
	for(var i = 0; i < res.data.length; i++){
		var tmpid = res.data[i].id;
		var tmplayerName = res.data[i].layerName;
		var tmpmapLevel = res.data[i].mapLevel;
		var tmppois = res.data[i].pois;
		var tmppoisArr = [];
		if(tmppois.length > 0){
			for(var j = 0;j < tmppois.length;j++){
				var tmppoiid = tmppois[j].id;
				var tmppoiType = tmppois[j].poiType;
				var tmppoiDatas = tmppois[j].poiDatas;
				var tmplnglat = tmppoiDatas.split(',');
				var tmppoiEncryptDatas = tmppois[j].poiEncryptDatas;
				var tmpiconpath = tmppois[j].iconpath;
				var tmppoiName = tmppois[j].poiName;
				var tmpaddress = tmppois[j].address;
				var tmppoiDesc = tmppois[j].poiDesc;
				var tmptelephone = tmppois[j].telephone;
				var tmppoiOverLay = null;
				
				var sContent = '名字:'+tmppoiName+'<br>'+
				'地址:'+tmpaddress+'<br>'+
				'电话:'+tmptelephone+'<br>'+
				'备注:'+tmppoiDesc+'<br>';
				//poi类型为点,并且有经纬度坐标
				if(tmppoiType == '0' && tmplnglat.length == 2){
					tmppoiOverLay = map.addTrackMarker('visiPoi@'+tmppoiid,tmplnglat[0],tmplnglat[1],tmppoiName,sContent,tmpiconpath);
					//alert(tmppoiOverLay);
					poiOverLayArr.push(tmppoiOverLay);
				}
				
				var tmpvisiPoiObj = new visiPoiObj(tmppoiid, tmppoiType, tmppoiDatas, tmppoiEncryptDatas, tmpiconpath, tmppoiName, tmpaddress, tmppoiType, tmptelephone, tmppoiOverLay);
				
				poiIdArr.push(tmppoiid);
				tmppoisArr.push(tmpvisiPoiObj);
				
			}
		}
		var tmpvisiLayerObj = new visiLayerObj(tmpid,tmplayerName,tmpmapLevel,tmppoisArr);
		layerArr.push(tmpvisiLayerObj);
	}
	//alert(poiOverLayArr.length);
	//map.mapObj.addOverlays(poiOverLayArr,true);
	
}*/

/*
//map.jsp 改变zoom级别
function changeZoom(param){

	//alert(param.mapId+";"+param.eventType+";"+param.eventX+";"+param.eventY+";"+param.zoom);
	for(var i = 0; i < poiOverLayArr.length; i++){
		map.mapObj.removeOverlay(poiOverLayArr[i]);
	}
	//map.mapObj.removeOverlays(poiOverLayArr);
	
	var tmpOverlayArr = new Array();
	for(var i = 0; i < layerArr.length; i++){
		var tmppois = layerArr[i].pois;
		//alert(layerArr[i].mapLevel +'== '+param.zoom+'=:'+(layerArr[i].mapLevel == param.zoom));
		if(tmppois.length > 0 && Number(layerArr[i].mapLevel) >= Number(param.zoom)){
			for(var j = 0; j < tmppois.length; j++){
				//alert('tmppois[j].poiOverLay:'+tmppois[j].poiOverLay);
				//alert('tmppois[j].poiOverLay != null:'+(tmppois[j].poiOverLay != null));
				if(tmppois[j].poiType == '0' && tmppois[j].poiOverLay != null){
					tmpOverlayArr.push(tmppois[j].poiOverLay);
					//alert(tmppois[j].poiOverLay.id);
					map.mapObj.addOverlay(tmppois[j].poiOverLay,false);
					//alert(tmppois[j].poiOverLay);
				}
			}
		}
	}
	//map.mapObj.addOverlays(tmpOverlayArr,true);
}
*/

//登录用户可见图层数组,包括可见poi
//var layerArr = new Array();
//poi id数组,用来控制poi的加载
//var poiIdArr = new Array();
//poi数组,用来控制poi的显示
var visiPoiObjArr = new Array();
function visiPoiObj(poiid, mapLevel, poiType, overLay){
	this.poiid = poiid;
	this.poiType = poiType;
	this.mapLevel = mapLevel;
	this.overLay = overLay;
}

//解析查询结果
function parseLayerPoiData(res){
	visiPoiObjArr = [];
	for(var i = 0; i < res.data.length; i++){
		var tmpmapLevel = res.data[i].mapLevel;
		var tmppois = res.data[i].pois;
		if(tmppois.length > 0){
			for(var j = 0;j < tmppois.length;j++){
				var tmppoiid = tmppois[j].id;
				var tmppoiType = tmppois[j].poiType;
				var tmppoiDatas = tmppois[j].poiDatas;
				var tmplnglat = tmppoiDatas.split(',');
				var tmppoiEncryptDatas = tmppois[j].poiEncryptDatas;
				var tmpiconpath = tmppois[j].iconpath;
				var tmppoiName = tmppois[j].poiName;
				var tmpaddress = tmppois[j].address;
				var tmppoiDesc = tmppois[j].poiDesc;
				var tmptelephone = tmppois[j].telephone;
				var tmppoiOverLay = null;
				var sContent = '名字:'+tmppoiName+'<br>'+
				'地址:'+tmpaddress+'<br>'+
				'电话:'+tmptelephone+'<br>'+
				'备注:'+tmppoiDesc+'<br>';
				//poi类型为点,并且有经纬度坐标
				if(tmppoiType == '0' && tmplnglat.length == 2){
					tmppoiOverLay = map.addTrackMarker('visiPoi@'+tmppoiid,tmplnglat[0],tmplnglat[1],tmppoiName,sContent,tmpiconpath);
					//alert(tmppoiOverLay);
					
					//map.mapObj.removeOverlay(tmppoiOverLay);
					var tmpvisiPoiObj = new visiPoiObj(tmppoiid, tmpmapLevel, tmppoiType, tmppoiOverLay);
					visiPoiObjArr.push(tmpvisiPoiObj);
				}
				
			}
		}
	}
	
	/*for(var i=0;i<visiPoiObjArr.length;i++){
		alert('1:'+visiPoiObjArr[i].overLay.id);
	}*/
}

function changeZoom(param){
	drawPoiOverLay(param.zoom);
}

function drawPoiOverLay(zoom){
	var tmpAddOverLayArr = [];
	var tmpRemoveOverLayArr = [];
	
	for(var i = 0; i < visiPoiObjArr.length; i++){
		//alert(visiPoiObjArr[i].poiid);
		if(Number(visiPoiObjArr[i].mapLevel) <= Number(zoom)){
			var tmpoverlay = map.mapObj.getOverlayById('visiPoi@'+visiPoiObjArr[i].poiid);
			//alert('tmpoverlay:'+tmpoverlay.id);
			//alert('tmpoverlay == null:'+(tmpoverlay == null));
			if(visiPoiObjArr[i].poiType == '0' && tmpoverlay == null){
				tmpAddOverLayArr.push(visiPoiObjArr[i].overLay);
				//map.mapObj.addOverlay(visiPoiObjArr[i].overLay,false);
			}
		}else{
			
			//alert('removeOverlay:'+visiPoiObjArr[i].overLay);
			//map.mapObj.removeOverlay(visiPoiObjArr[i].overLay);
			tmpRemoveOverLayArr.push(visiPoiObjArr[i].overLay);
		}
		
	}
	
	map.mapObj.addOverlays(tmpAddOverLayArr,false);
	map.mapObj.removeOverlays(tmpRemoveOverLayArr);
	
}
/*
//图层对象
function visiLayerObj(id, layerName, mapLevel, pois){
	this.id = id;
	this.layerName = layerName;
	this.mapLevel = mapLevel;
	this.pois = pois;
}
//poi对象
function visiPoiObj(id, poiType, poiDatas, poiEncryptDatas, iconpath, poiName, address, poiType, telephone, poiOverLay){
	this.id = id;
	this.poiType = poiType;
	this.poiDatas = poiDatas;
	this.poiEncryptDatas = poiEncryptDatas;
	this.iconpath = iconpath;
	this.poiName = poiName;
	this.address = address;
	this.poiType = poiType;
	this.telephone = telephone;
	this.poiOverLay = poiOverLay;
}*/

//查询登录用户的可见图层和poi点
function listVisibleLayerAndPoi(){
	
	/*for(var i = 0; i < visiPoiObjArr.length; i++){
		map.mapObj.removeOverlay(visiPoiObjArr[i].overLay);
	}*/
	var tmpOverLayArr = new Array();
	for(var i = 0; i < visiPoiObjArr.length; i++){
		tmpOverLayArr.push(visiPoiObjArr[i].overLay);
		//map.mapObj.removeOverlay(visiPoiObjArr[i].overLay);
		
		//alert(visiPoiObjArr[i].overLay);
	}
	alert(tmpOverLayArr.length);
	map.removeOverlays(tmpOverLayArr);
	
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
		   drawPoiOverLay(map.mapObj.getZoomLevel());
		   //alert(request.responseText);
		   //alert(res);
		 },
		 failure : function(request) {
		 }
		});
}

