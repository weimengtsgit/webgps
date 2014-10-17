
//新增标点,终端绑定标点
function addPOI(btn){
	//是否选择终端
	var deviceid = '';
	var tmpisguanlian = Ext.getCmp('isguanlianfrm').getValue().getGroupValue();
		    	if(tmpisguanlian == 0){
		    		var treeArr = new Array();
					getTreeId(root,treeArr);
					
					
					for(var i=0;i<treeArr.length;i++){
						var idArr = treeArr[i].id.split('@#');
						deviceid += idArr[0]+',';
					}
					
					if(deviceid.length>0){
						deviceid = deviceid.substring(0,deviceid.length-1);
					}
		    	}
		    	
	
	
	var poiName = Ext.getCmp('poiNamefrm').getValue();
	var poiDesc = Ext.getCmp('poiDescfrm').getValue();
	var poiAddress = Ext.getCmp('poiAddressfrm').getValue();
	var poiPhoum = Ext.getCmp('poiPhoumfrm').getValue();
	var tmppoiCoordx = Ext.getCmp('poiCoordx').getValue();
	var tmppoiCoordy = Ext.getCmp('poiCoordy').getValue();
	var poiLayer = Ext.getCmp('poiLayerfrm').getValue();
	var iconpath = Ext.getCmp('poiImgHid').getValue();
	var poiRange = Ext.getCmp('poiRange').getValue();
	
	
	if(btn=='yes'){
		/*parent.Ext.Msg.show({
			msg: '正在保存 请稍等...',
			progressText: '保存...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});*/
		Ext.Ajax.request({
		 url:path+'/poi/poi.do?method=addPoi',
		 method :'POST', 
		 params: { 
			 poiName: encodeURI(poiName) , poiDesc: encodeURI(poiDesc), poiType:'0',
			 poiDatas: tmppoiCoordx+','+tmppoiCoordy, poiEncryptDatas: tmppoiCoordx+','+tmppoiCoordy, 
			 telephone: encodeURI(poiPhoum), address: encodeURI(poiAddress), 
			 iconpath: iconpath, layerId: poiLayer, visitDistance: poiRange, 
			 deviceIds: deviceid
		 },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(res.result);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//AreaAlarmstore.load({params:{start:0, limit:10}});
		   		Ext.Msg.alert('提示', '保存成功');
		   		map.listVisibleLayerAndPoi();
		   		//Ext.Msg.hide();
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('提示', "保存失败!");
		   		//Ext.Msg.hide();
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "保存失败!");
			 //Ext.Msg.hide();
			 return;
		 }
		});
	}
}