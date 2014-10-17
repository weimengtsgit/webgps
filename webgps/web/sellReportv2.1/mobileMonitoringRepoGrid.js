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
				        ['ȫ��','-1'],
				        ['gps','gps_open_or_close'],
				        ['����','net_open_or_close']
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

//��˾�����ڻ��ܱ���
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
		     {header: '���', width: 100, dataIndex:'id',hidden:true},     
		     {header: 'ʱ��', width: 150, dataIndex:'gpstime'}, 
	         {header: 'Ա������', width: 100, dataIndex:'termName'},        
		     {header: '����', width: 130, dataIndex:'groupName'},       
		     {header: '�ֻ�����', width: 120, dataIndex:'simcard'},    
		     {header: '�ֻ�ģ������', width: 150, dataIndex:'type',renderer: function tips(val) {
					if(val == 'gps_open_or_close'){return 'gps';}else if(val == 'net_open_or_close'){return '����';}else{return '';}
				}}, 
		     {header: '״̬', width: 100, dataIndex:'state',renderer: function tips(val) {
					if(val == '0'){return '�ر�';}else if(val == '1'){return '����';}else{return '������';}
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
		        {xtype: 'label',text: '��ʼʱ��'},
				{id: 'startTime',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
				'-',
				{xtype: 'label',text: '����ʱ��'},
				{id: 'endTime',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
				 '-',
			    {xtype: 'label',text: 'ģ������'},checkModuleComBox,
				{xtype:'label',text:'�ؼ���'},
		        {xtype: 'textfield',id: 'keyWords',width: 100},
		        {xtype:'button',text:'��ѯ',iconCls: 'icon-search',handler: function(){
		        	
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
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};

