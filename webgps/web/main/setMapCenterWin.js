function setmapcenterconfirm(btn){
	if(btn=='yes'){
	  	  Ext.Msg.show({
			  msg: '正在保存 请稍等...',
			  progressText: '保存...',
			  width:300,
			  wait:true,
			  //waitConfig: {interval:200},
			  icon:'ext-mb-download'
		  });
	  	  setmapcenterajax();
	}
}

function setmapcenterajax(){
	var center = map.mapObj.getCenter();   
	centerX=center.lngX;   
	centerY=center.latY;   
	//alert("中心点的经度为："+centerX+";"+"中心点的纬度为："+centerY); 
	var zoom = map.mapObj.getZoomLevel();   
	//alert("当前地图的缩放级别为："+zoom); 
	
	Ext.Ajax.request({
		 url:path+'/system/ent.do?method=setVeiw',
		 method :'POST', 
		 params: { mapZoom:zoom, centerX : centerX ,centerY:centerY },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('提示', '保存成功');
		   		return;
		   }else{
		   		Ext.Msg.alert('提示', "保存失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "保存失败!");
		 }
		});
}