

//添加区域
function addAreaAlarm(btn){
	var name = Ext.getCmp('areaNamefrm').getValue();
	var areaPoints = Ext.getCmp('areaPointsfrm').getValue();
	
	if(btn=='yes'){
		parent.Ext.Msg.show({
			msg: '正在保存 请稍等...',
			progressText: '保存...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 url:path+'/area/area.do?method=addArea',
		 method :'POST', 
		 params: { areaPoints: areaPoints, areaType : '1' , name: encodeURI(name) },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//清除地图上绘制区域
            	map.removePolygonById(map.drawTempObjectID);
            	map.resetControls();
		 		AreaAlarmstore.load({params:{start:0, limit:10}});
		 		AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	AreaAlarmWindow.setHeight(400);
            	AreaActiveItem = true;
		   		Ext.Msg.alert('提示', '保存成功');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('提示', "保存失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "保存失败!");
		 }
		});
	}
}

//修改区域
function modifyAreaAlarm(btn){
	var name = Ext.getCmp('areaNamefrm').getValue();
	var areaPoints = Ext.getCmp('areaPointsfrm').getValue();
	var id = Ext.getCmp('areaIdfrm').getValue();
	
	if(btn=='yes'){
		parent.Ext.Msg.show({
			msg: '正在保存 请稍等...',
			progressText: '保存...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 url:path+'/area/area.do?method=updateArea',
		 method :'POST', 
		 params: { areaPoints: areaPoints, areaType : '1' , id: id, name: encodeURI(name) },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//清除地图上绘制区域
            	map.removePolygonById(map.drawTempObjectID);
		 		AreaAlarmstore.load({params:{start:0, limit:10}});
		 		AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	AreaAlarmWindow.setHeight(400);
            	AreaActiveItem = true;
            	map.resetControls();
		   		Ext.Msg.alert('提示', '修改成功');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('提示', "修改失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "修改失败!");
		 }
		});
	}
}

//删除区域
function delAreaAlarm(btn){
	var tmpselArr = AreaAlarmsm.getSelections();
	var ids = '';
	for(var i=0;i<tmpselArr.length;i++){
		ids +=tmpselArr[i].get('id')+',';
	}
	ids = ids.substring(0,ids.length-1);
	if(btn=='yes'){
		parent.Ext.Msg.show({
			msg: '正在删除 请稍等...',
			progressText: '删除...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 url:path+'/area/area.do?method=deleteAreas',
		 method :'POST', 
		 params: { ids: ids},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		AreaAlarmstore.load({params:{start:0, limit:10}});
		   		Ext.Msg.alert('提示', '删除成功');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('提示', "删除失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "删除失败!");
		 }
		});
	}
}


//终端绑定区域
function refTermArea(btn){
	var treeArr = new Array();
	getTreeId(root,treeArr);
	var deviceid = '';
	for(var i=0;i<treeArr.length;i++){
		//var idArr = treeArr[i].id.split('@#');
		var idArr = treeArr[i].id.split('@#');
		deviceid += idArr[0]+',';
	}
	
	if(deviceid.length>0){
		deviceid = deviceid.substring(0,deviceid.length-1);
	}
	
	var areaId = Ext.getCmp('refTermAreaIdfrm').getValue();
	var alarmType = Ext.getCmp('areaAlarmTypefrm').getValue();
	var startTime = Ext.getCmp('areaAlarmStartTimefrm').getValue();
	var endTime = Ext.getCmp('areaAlarmEndTimefrm').getValue();
	
	if(btn=='yes'){
		parent.Ext.Msg.show({
			msg: '正在绑定 请稍等...',
			progressText: '绑定...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 url:path+'/area/area.do?method=refTermArea',
		 method :'POST', 
		 params: { areaIds: areaId , deviceIds: deviceid, startTime: startTime, endTime: endTime, alarmType: alarmType},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//AreaAlarmstore.load({params:{start:0, limit:10}});
		   		Ext.Msg.alert('提示', '绑定成功');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('提示', "绑定失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "绑定失败!");
		 }
		});
	}
}