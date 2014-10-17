var visiLayerCheckIdArr = new Array();
//图层列表,图层控制
function listVisibleLayer(){
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=listVisibleLayer',
		 method :'POST', 
		 params: { start: '1', limit : '1000'},
		 timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   parseLayerControlData(res);
		 },
		 failure : function(request) {
		 }
	});
}

function updateLayerVisible(ids){
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=updateLayerVisible',
		 method :'POST', 
		 params: { layerIds: ids },
		 timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   if(res.result == 1){
		   	Ext.Msg.alert('提示','修改成功!');
		   	listVisibleLayer();
		   	map.listVisibleLayerAndPoi();
		   	
		   	return;
		   }
		 },
		 failure : function(request) {
		 	Ext.Msg.alert('提示','修改失败!');
		 	return;
		 }
	});
}

var visiLayerControlObj = new Array();

function visiLayerObj(id, layerName, mapLevel, visible){
	this.id = id;
	this.layerName = layerName;
	this.mapLevel = mapLevel;
	this.visible = visible;
	
}

//解析查询结果
function parseLayerControlData(res){
	visiLayerControlObj = [];
	for(var i = 0; i < res.data.length; i++){
		var tmpid = res.data[i].id;
		var tmplayerName = res.data[i].layerName;
		var tmpmapLevel = res.data[i].mapLevel;
		var tmpvisible = res.data[i].visible;
		var tmpvisiLayerObj = new visiLayerObj(tmpid, tmplayerName, tmpmapLevel, tmpvisible);
		
		visiLayerControlObj.push(tmpvisiLayerObj);
	}
	
}


