var checkModuleComBox = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	name : 'ModuleType',
	id : 'ModuleType',
	value:'-1',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data : [
				        ['全部','-1'],
				        ['gps','gps_open_or_close'],
				        ['网络','net_open_or_close']
				        ]
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
}); 

var proxy = new Ext.data.HttpProxy({
	url : path + '/mobileMonitoring/mobileeventdata.do?method=listAllMobile'
});

var reader = new Ext.data.JsonReader({
	totalProperty : 'total',
	successProperty : 'success',
	idProperty : 'id',
	root : 'data'
}, [ {
	name : 'id'
}, {
	name : 'gpstime'
}, {
	name : 'termName'
}, {
	name : 'groupName'
}, {
	name : 'simcard'
}, {
	name : 'type'
}, {
	name : 'state'
}]);
var store = new Ext.data.Store({
	id : 'store',
	restful : true,
	proxy : proxy,
	reader : reader
});
var sm = new Ext.grid.CheckboxSelectionModel({});

//公司级考勤汇总报表
var mobileMonitoringRepoGrid = {
	xtype: 'grid',
	id: 'mobileMonitoringRepoGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: store,
	loadMask: {msg: main.loading},
	sm : sm,
	colModel: new Ext.grid.ColumnModel({
		    columns: [
		     {header: '序号', width: 100, dataIndex:'id',hidden:true},     
		     {header: '时间', width: 150, dataIndex:'gpstime'}, 
	         {header: '员工姓名', width: 100, dataIndex:'termName'},        
		     {header: '部门', width: 130, dataIndex:'groupName'},       
		     {header: '手机号码', width: 120, dataIndex:'simcard'},    
		     {header: '手机模块类型', width: 150, dataIndex:'type',renderer: function tips(val) {
					if(val == 'gps_open_or_close'){return 'gps';}else if(val == 'net_open_or_close'){return '网络';}else{return '';}
				}}, 
		     {header: '状态', width: 100, dataIndex:'state',renderer: function tips(val) {
					if(val == '0'){return '关闭';}else if(val == '1'){return '开启';}else{return '不可用';}
				}}
		    ]
		   }),
		   enableColumnMove: false,
		   enableHdMenu: false,
		   enableColumnHide:false,
		  
	stripeRows: true,
	stateful: true,
	stateId: 'mobileMonitoringRepoGridId',
	tbar : {
		xtype: 'toolbar',
		items:[ 
		        {xtype: 'label',text: '开始时间'},
				{id: 'startTime',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
				'-',
				{xtype: 'label',text: '结束时间'},
				{id: 'endTime',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
				 '-',
			    {xtype: 'label',text: '模块类型'},checkModuleComBox,
				{xtype:'label',text:'关键字'},
		        {xtype: 'textfield',id: 'keyWords',width: 100},
		        {xtype:'button',text:'查询',iconCls: 'icon-search',handler: function(){
		        	
					var startTime = Ext.getCmp('startTime').getValue().format('Y-m-d');
					var endTime = Ext.getCmp('endTime').getValue().format('Y-m-d');
					
					var keyWords = Ext.getCmp('keyWords').getValue();
					var ModuleType = Ext.getCmp('ModuleType').getValue();
					var mobileDeviceId = getDeviceId();
				
					store.baseParams = {
						start : 0,
						limit : 20,
						startTime:startTime,
						endTime:endTime,
						keyWords:encodeURI(keyWords),
						type:ModuleType,
						deviceId:mobileDeviceId
					};
					store.load({
						params : {
							start : 0,
							limit : 20,
							startTime:startTime,
							endTime:endTime,
							keyWords:encodeURI(keyWords),
							type:ModuleType,
							deviceId:mobileDeviceId
						}
					});
					
				}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 20,
		store: store,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

