var vehicleMsgStatDeviceids = '';
var vehicleMsgStatSearchValue = '';
var vehicleMsgStatStartTime = tmpdate;
var vehicleMsgStatEndTime = tmpdate;
var vehicleMsgStatCarTypeInfoId = '-1';

function getDeviceId(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var tmpgpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2) {
			tmpgpsDeviceid += idArr[0] + ',';
		}
	}
	if (tmpgpsDeviceid.length > 0) {
		tmpgpsDeviceid = tmpgpsDeviceid.substring(0, tmpgpsDeviceid.length - 1);
	}
	return tmpgpsDeviceid;
}

function getTreeId(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = node;
		}
	}
	node.eachChild(function(child) {
		getTreeId(child,treeArr);
	});
}

var car_type_info_combo = new Ext.form.ComboBox( {
	hiddenName : 'id',
	valueField : 'id',
	store : new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	}),
	displayField : 'name',
    mode: 'local',
	listeners: {
  	    expand : function(combo ) {
	  	  	var count = car_type_info_combo.getStore().getCount();
	  		if(count <=1){
	  	    	carTypeInfoComboexpand();
	  		}
    	}
    },
    //disabled : true,
    editable: false,
    triggerAction: 'all',
    emptyText:'选择车辆类型'
});

function carTypeInfoComboexpand(){
	Ext.Ajax.request({
		url : path+'/carTypeInfo/carTypeInfo.do?method=carTypeInfoCombo',
		method :'POST',
		success : function(request) {
			var res = request.responseText;
			car_type_info_combo.getStore().loadData(eval(res));
		},
		failure : function(request) {
		}
	});
}

function searchVehicleMsgStatGrid(){
	var tmp = getDeviceId();
	if(tmp.length<=0){
		Ext.Msg.alert('提示','请选择终端!');
		return;
	}
	vehicleMsgStatDeviceids = tmp;
	storeLoad(VehicleMsgStatStore, 0, 15, vehicleMsgStatDeviceids, '', '', '', '', false);
}

var VehicleMsgStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/locate/locate.do?method=listLastLoc'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, 
			[{name: 'termName'},{name: 'vehicleNumber'},{name: 'vehicleType'},{name: 'longitude'},{name: 'latitude'},
			 {name: 'gpstime'},{name: 'mqtime'},{name: 'speed'},{name: 'distance'},{name: 'groupName'},{name: 'locDesc'},{name: 'deviceId'},
			 {name: 'signalFlag'},{name: 'programVersion'},{name: 'lastDistance'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
						startTime: vehicleMsgStatStartTime, endTime: vehicleMsgStatEndTime, deviceIds: vehicleMsgStatDeviceids
		    	};
			}
		}
	}
});

var VehicleMsgStatGrid = {
    region: 'center',
	xtype: 'grid',
	id: 'VehicleMsgStatGrid',
	store: VehicleMsgStatStore,
	loadMask: {msg: '查询中...'},
	columns: [
	  	{header: '终端设备号', width: 130, sortable: true,  dataIndex: 'termName'},
		{header: '客户姓名', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: '车辆型号', width: 130, sortable: true,  dataIndex: 'vehicleType'},
		{header: '经度', width: 130, sortable: true,  dataIndex: 'longitude'},
		{header: '纬度', width: 130, sortable: true,  dataIndex: 'latitude'},
		{header: '最后回传时间', width: 130, sortable: true,  dataIndex: 'gpstime'},
		{header: '盲区倒计时', width: 130, sortable: true,  dataIndex: 'mqtime'},
		{header: '速度', width: 130, sortable: true,  dataIndex: 'speed'},
		{header: '方向', width: 130, sortable: true,  dataIndex: 'distance'},
		{header: '车机GSM信号强度', width: 130, sortable: true,  dataIndex: 'signalFlag'},
		{header: '车机工作电压', width: 130, sortable: true,  dataIndex: 'programVersion'},
		{header: '剩余油量百分比', width: 130, sortable: true,  dataIndex: 'lastDistance',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '%">' + val + '%</span>';
		}},
		{header: '所属组', width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: '位置信息', width: 200, sortable: true,  dataIndex: 'locDesc',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VehicleMsgStatGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			/*{xtype: 'label',text: '开始时间'},
			{id: 'vehicleMsgStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label',text: '结束时间'},
			{id: 'vehicleMsgStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			car_type_info_combo,
			'-',*/
			{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
				searchVehicleMsgStatGrid();
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/locate/locate.do?method=listLastLoc&expExcel=true&start=0&limit=65535&deviceIds='+vehicleMsgStatDeviceids,
					success:function(response, opts){ 
						//Ext.Msg.alert("提示", "操作成功");
					},
					form: Ext.fly('excelform'),
					isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert("提示", "操作失败");
					},
					callback:function(response, opts){
						//Ext.Msg.alert("提示", "操作成功");
					},
					scope: this
				});
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VehicleMsgStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

var loader = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'	
});

var root = new Ext.tree.AsyncTreeNode({
		text : '',
		id : '-100',
		draggable : false // 根节点不容许拖动
});

var api = new Ext.tree.TreePanel({
    region: 'west',
    rootVisible:false,
    lines:true,
    autoScroll:true,
    width:170,
    loader: loader,
    root:root
});
api.on('checkchange',function(node,checked){
    node.expand();
    node.attributes.checked = checked;
    node.on('expand',function(node){
		node.eachChild(function(child){
	    	child.ui.toggleCheck(checked);
	        child.attributes.checked = checked;
	        child.fireEvent('checkchange',child,checked);
	    });
	});
    node.eachChild(function(child){
    	//alert('2:'+child);
    	child.ui.toggleCheck(checked);
        child.attributes.checked = checked;
        child.fireEvent('checkchange',child,checked);
    });
},api);
Ext.onReady(function(){
    Ext.QuickTips.init();
    var viewport = new Ext.Viewport({layout: 'border',items: [ api, VehicleMsgStatGrid ]});
});
