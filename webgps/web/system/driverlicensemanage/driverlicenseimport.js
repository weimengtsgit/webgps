var searchValue = '';
var lastMonth = new Date();
lastMonth.setMonth(lastMonth.getMonth()-1);
var startTime = (lastMonth).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";

var proxy = new Ext.data.HttpProxy({
	url: path+'/driverLicense/driverLicense.do?method=listDriverLicense'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'employeeNum'},
    {name: 'employeeWorknum'},
    {name: 'employeeName'},
    {name: 'employeeId'},
    {name: 'department'},
    {name: 'examineDate'},
    {name: 'expireDate'},
    {name: 'condition'},
    {name: 'demo'}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}},
    id: 'store',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime
    			};
    		}
    	}
    }
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    //{id:'deviceId',header: "deviceId", width: 40, sortable: true, dataIndex: 'deviceId',hidden:true},
	{header: "Ա�����",fixed:true, width: 100, sortable: true, dataIndex: 'employeeNum'},
    {header: "Ա������",fixed:true, width: 100, sortable: true, dataIndex: 'employeeWorknum'},
    {header: "Ա������",fixed:true, width: 100, sortable: true, dataIndex: 'employeeName'},
    {header: "Ա�����֤��",fixed:true, width: 100, sortable: true, dataIndex: 'employeeId'},
    {header: "���ڲ���",fixed:true, width: 100, sortable: true, dataIndex: 'department'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'examineDate'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'expireDate'},
    {header: "�������",fixed:true, width: 100, sortable: true, dataIndex: 'condition'},
    {header: "��ע",fixed:true, width: 100, sortable: true, dataIndex: 'demo'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'center',
    width: 500,
    loadMask: {msg:'��ѯ��...'},
    enableColumnHide : false,
    store: store,
    columns : userColumns,
    margins: '0 0 0 0',
	tbar: [new Ext.form.DateField({
		id: 'startDateField',
		fieldLabel: 'Date',
		format:'Y-m-d',
		width: 100,
		editable: false,
		value: lastMonth
	}),'-',new Ext.form.DateField({
		width: 100,
		fieldLabel: 'Date',
		format:'Y-m-d',
		editable: false,
		value: new Date(),
	    id: 'endDateField'
	}),'-',new Ext.form.TextField({
	    id: 'DeviceIdField',
	    width: 80,
	    enableKeyEvents: true,
	    listeners: {
        	keypress : function( textField, e ) {
		        if (e.getKey() == e.ENTER) {
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
				    startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
				    endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
			    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
			    }
        	}
        }
	}),'-',new Ext.Action({
		text: '��ѯ',
		handler: function(){
			searchValue = Ext.getCmp('DeviceIdField').getValue();
			startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
			endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
	    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
	    },
		iconCls: 'icon-search'
	}),'-',new Ext.Action({
		text: '����Excel',
        handler: function(){
      			var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime+'&endTime='+endTime;
      			document.excelformSen.action = path+'/driverLicense/driverLicense.do?method=listDriverLicense&'+tmpparam;
      			document.excelformSen.submit();
      			setTimeout(function(){Ext.MessageBox.hide()},3000);
        },
        iconCls: 'icon-excel'
    })
    ],
    bbar: new Ext.PagingToolbar({
        pageSize: 20,
        store: store,
        displayInfo: true,
        displayMsg: '��{0}����{1}������ ��{2}��',
        emptyMsg: "û������"
    })
});

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    
});