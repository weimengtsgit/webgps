
//������ѯ���
function parsePoiByDeviceIdData(res){
	queryPoiByDeviceIdArr = new Array();
	for(var i = 0; i < res.data.length; i++){
		var tmpdata = res.data[i];
		var tmpid = tmpdata.id;
		var tmppoiType = tmpdata.poiType;
		var tmppoiDatas = tmpdata.poiDatas;
		var tmplnglat = tmppoiDatas.split(',');
		var tmpiconpath = path+'/images/poi/'+tmpdata.iconpath;
		var tmppoiName = tmpdata.poiName;
		var tmppoiDesc = tmpdata.poiDesc;
		var tmpaddress = tmpdata.address;
		var tmptelephone = tmpdata.telephone;
		var sContent = '����:'+tmppoiName+'<br>'+
		'�绰:'+tmptelephone+'<br>'+
		'����:'+tmppoiDesc+'<br>'+
		'��ַ:'+tmpaddress+'<br>';
		//poi����Ϊ��,�����о�γ������
		if(tmppoiType == '0' && tmplnglat.length == 2){
			var tmppoiOverLay = addLabelPoiMarker('PoiByDeviceId@'+tmpid,tmplnglat[0],tmplnglat[1],tmppoiName,sContent,tmpiconpath);
			//alert('tmppoiOverLay:'+tmppoiOverLay);
			//map.mapObj.removeOverlay(tmppoiOverLay);
			queryPoiByDeviceIdArr.push(tmppoiOverLay);
			//mapObj.addOverlay(tmppoiOverLay,false);
		}
	}
	//alert(queryPoiByDeviceIdArr.length);
	//mapObj.addOverlays(queryPoiByDeviceIdArr,true);
	if(queryPoiByDeviceIdArr.length == 1){
		mapObj.addOverlay(queryPoiByDeviceIdArr[0] , true);
	}else if(queryPoiByDeviceIdArr.length > 1){
		mapObj.addOverlays(queryPoiByDeviceIdArr , true);
	}
}

var queryPoiByDeviceIdArr = new Array();
//����deviceid��ѯpoi
function queryPoiByDeviceId(node){
	var tmpidArr = node.id.split('@#');
	if(tmpidArr.length<=1){
		return;
	}
	//alert('queryPoiByDeviceIdArr.length:'+queryPoiByDeviceIdArr.length);
	for(var i=0;i<queryPoiByDeviceIdArr.length;i++){
		//alert(queryPoiByDeviceIdArr[i].id);
		mapObj.removeOverlayById(queryPoiByDeviceIdArr[i].id);
	}
	
	Ext.Ajax.request({
		url:path+'/poi/poi.do?method=queryPoiByDeviceId',
		method :'POST',
		params: { start: '1', limit : '65535' ,deviceId : encodeURI(tmpidArr[0]) },
		//timeout : 60000,
		success : function(request) {
			
			//mapObj.removeOverLays(queryPoiByDeviceIdArr);
			/*if(queryPoiByDeviceIdArr.length>1){
				alert('1');
				mapObj.removeOverLays(queryPoiByDeviceIdArr);
				alert('2');
			}else if(queryPoiByDeviceIdArr.length == 1){
				alert(queryPoiByDeviceIdArr[0]);
				mapObj.removeOverLay(queryPoiByDeviceIdArr[0]);
			}*/
			var res = Ext.decode(request.responseText);
			if(res.data.length == 0){
				parent.Ext.Msg.alert('��ʾ', "δ�󶨱�ע!");
			}
		    parsePoiByDeviceIdData(res);
		},
		failure : function(request) {
		}
	});
}


//������ѯ���
function parseAreaByDeviceIdData(res){
	queryAreaByDeviceIdArr = new Array();
	for(var i = 0; i < res.data.length; i++){
		var tmpdata = res.data[i];
		var tmpid = tmpdata.id;
		var tmpareaName = tmpdata.areaName;
		var tmpareaPoints = tmpdata.areaPoints;
		var tmpareaType = tmpdata.areaType;
		var tmpalarmType = tmpdata.alarmType ;
		if(tmpalarmType == '0'){
			tmpalarmType = '������';
		}else if(tmpalarmType == '1'){
			tmpalarmType = '������';
		}
		
		var tmpareaxy = tmpareaPoints.split('#');
		
		if(tmpareaxy.length>0 && tmpareaType == '1'){
			var tmparea = getPolygon(tmpareaxy, tmpareaName, '<br>��������:  '+tmpareaName+'</br>'+'<br>���򱨾�����:  '+tmpalarmType+'</br>', 'AreaByDeviceId'+tmpid, false);
			queryAreaByDeviceIdArr.push(tmparea);
			//mapObj.addOverlay(tmparea , true);
		}
	}
	//mapObj.addOverlays(queryPoiByDeviceIdArr , true);
	if(queryAreaByDeviceIdArr.length == 1){
		mapObj.addOverlay(queryAreaByDeviceIdArr[0] , true);
	}else if(queryAreaByDeviceIdArr.length > 1){
		mapObj.addOverlays(queryAreaByDeviceIdArr , true);
	}
}

var queryAreaByDeviceIdArr = new Array();
//����deviceid��ѯpoi
function queryAreaByDeviceId(node){
	var tmpidArr = node.id.split('@#');
	if(tmpidArr.length<=1){
		return;
	}
	
	//alert('queryAreaByDeviceIdArr.length:'+queryAreaByDeviceIdArr.length);
	for(var i=0;i<queryAreaByDeviceIdArr.length;i++){
		//alert(queryPoiByDeviceIdArr[i].id);
		mapObj.removeOverlayById(queryAreaByDeviceIdArr[i].id);
	}
	
	/*if(queryAreaByDeviceIdArr.length>1){
		mapObj.removeOverLays(queryAreaByDeviceIdArr);
	}else if(queryAreaByDeviceIdArr.length == 1){
		mapObj.removeOverLay(queryAreaByDeviceIdArr[0]);
	}*/
	Ext.Ajax.request({
		url:path+'/area/area.do?method=queryAreaByDeviceId',
		method :'POST',
		params: { start: '1', limit : '65535' ,deviceId : encodeURI(tmpidArr[0])},
		//timeout : 60000,
		success : function(request) {
			
			var res = Ext.decode(request.responseText);
			//alert(res.data.length);
			if(res.data.length == 0){
				parent.Ext.Msg.alert('��ʾ', "δ�󶨵���Χ��!");
			}
		    parseAreaByDeviceIdData(res);
		},
		failure : function(request) {
		}
	});
}
