//poi����,��������poi����ʾ,�����Ѽ���poi��
var visiPoiObjArr = new Array();
var globalMapPoiOverLayArr = new Array();

function visiPoiObj(poiid, mapLevel, poiType, overLay , editable){
	this.poiid = poiid;
	this.poiType = poiType;
	this.mapLevel = mapLevel;
	this.overLay = overLay;
	this.editable = editable;
}
//�ı��ͼ����
function changeZoom(param){
	//alert('changeZoom');
	drawPoiOverLay(param.zoom);
}
//��ͼƽ��
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


//��poi��
function drawPoiOverLay(zoom){
	var tmpAddOverLayArr = [];
	var tmpRemoveOverLayArr = [];
	var tmpglobalMapPoiOverLayArr = [];
	var bounds=mapObj.getLngLatBounds();
	var len = visiPoiObjArr.length;
	//if(len > 500){
	//	len = 500;
	//}
	//�ȱ�������poi��,�ҳ��ڵ�ǰ��ͼ�����¿���ʾ�ĵ�
	for(var i = 0; i < len; i++){
		//alert(visiPoiObjArr[i].poiid);
		
		//�ж�poi�����ʾ������Ƿ��ڵ�ͼ��Ұ��Χ��
		//alert('visiPoiObjArr[i].mapLevel:'+visiPoiObjArr[i].mapLevel+';'+(Number(visiPoiObjArr[i].mapLevel) <= Number(zoom)));
		//alert('��ͼ��Ұ��Χ��:'+isInBound(bounds, visiPoiObjArr[i].overLay));
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
				//�жϵ�ͼ���Ƿ��������
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
			//�жϵ�ͼ���Ƿ��������
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

//�жϵ�ǰ��ͼ���а�������ע�㼯��
function isInBound(bounds,marker)
{
	//��ȡ��ǰ��ͼ��
	var southWestX = bounds.southWest.lngX;//���Ͻǵľ�γ������
	var southWestY = bounds.southWest.latY;//���Ͻǵľ�γ������
	var northEastX = bounds.northEast.lngX;//�����ǵľ�γ������
	var northEastY = bounds.northEast.latY;//�����ǵľ�γ������
	//alert(southWestX +","+southWestY +"  "+northEastX +","+northEastY);
	//�б����ע���Ƿ����뵱ǰbounds
	var lngX = marker.lnglat.lngX;
	var latY = marker.lnglat.latY;
	if((lngX > southWestX) && (lngX < northEastX) && (latY < northEastY) && (latY > southWestY)){
		return true;
	}
	return false;
}

//��ѯ��¼�û��Ŀɼ�ͼ���poi��
function listVisibleLayerAndPoi(){
	
	/*for(var i = 0; i < visiPoiObjArr.length; i++){
		mapObj.removeOverlay(visiPoiObjArr[i].overLay);
	}*/
	var tmpOverLayArr = new Array();
	for(var i = 0; i < visiPoiObjArr.length; i++){
		tmpOverLayArr.push(visiPoiObjArr[i].overLay);
	}
	//��ѯǰ�������ͼ�ϵ�poi��
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


//������ѯ���
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
				/*var sContent = '����:'+tmppoiName+'<br>'+
				'��ַ:'+tmpaddress+'<br>'+
				'�绰:'+tmptelephone+'<br>'+
				'��ע:'+tmppoiDesc+'<br>';
				*/
				var param = '?id='+tmppoiid+'&name='+tmppoiName+'&address='+tmpaddress+'&telephone='+tmptelephone+
				'&desc='+tmppoiDesc+'&visitDistance='+tmpvisitDistance+'&iconpath='+tmppois[j].iconpath+'&layerid='+layerid
				+'&poiDatas='+tmppoiDatas+'&poiEncryptDatas='+tmppoiEncryptDatas + '&editable=' + tmpeditable;
				
				var sContent='<iframe  src="'+path+'/map/poitip.jsp'+param+'" width="100%" height="190" marginwidth="0" framespacing="0" marginheight="0" scrolling="no" frameborder="0" border="0" ></iframe>';  //tip����
            	//alert(sContent);
				//poi����Ϊ��,�����о�γ������
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
