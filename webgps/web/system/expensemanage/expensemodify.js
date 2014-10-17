var searchValue = '';
var lastMonth = new Date();
lastMonth.setMonth(lastMonth.getMonth()-1);
var startTime = (lastMonth).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";

var delbut = new Ext.Action({
	text: '�޸�',
	id : 'delbut',
	handler: function(){
		var tmpid = Ext.getCmp('frmid').getValue();
		if(tmpid.length<=0){
			Ext.Msg.alert('��ʾ', '��ѡ���¼!');
			return;
		}
		Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸�������?', function (btn){
			if(btn=='yes'){
				var tmpid = Ext.getCmp('frmid').getValue();
				var tmpdeviceId = Ext.getCmp('frmdeviceId').getValue();
				var tmpvehicleDepreciation = Ext.getCmp('frmvehicleDepreciation').getValue();
				if(tmpvehicleDepreciation.length<=0){
					Ext.Msg.alert('��ʾ', '�����복���۾ɷ���!');
					return;
				}
				var tmppersonalExpenses = Ext.getCmp('frmpersonalExpenses').getValue();
				if(tmppersonalExpenses.length<=0){
					Ext.Msg.alert('��ʾ', '��������Ա����!');
					return;
				}
				var tmpinsurance = Ext.getCmp('frminsurance').getValue();
				if(tmpinsurance.length<=0){
					Ext.Msg.alert('��ʾ', '�����뱣��̯������!');
					return;
				}
				var tmpmaintenance = Ext.getCmp('frmmaintenance').getValue();
				if(tmpmaintenance.length<=0){
					Ext.Msg.alert('��ʾ', '������ά�ޱ�������!');
					return;
				}
				var tmptoll = Ext.getCmp('frmtoll').getValue();
				if(tmptoll.length<=0){
					Ext.Msg.alert('��ʾ', '�������·���ŷ�!');
					return;
				}
				var tmpannualCheck = Ext.getCmp('frmannualCheck').getValue();
				if(tmpannualCheck.length<=0){
					Ext.Msg.alert('��ʾ', '��������켰��������!');
					return;
				}
				var tmpcreateDate = Ext.getCmp('frmcreateDate').getValue();
				if(tmpcreateDate.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ������!');
					return;
				}
				Ext.Msg.show({
					msg: '���ڱ��� ���Ե�...',
					progressText: '����...',
					width:300,
					wait:true,
					icon:'ext-mb-download'
				});
				Ext.Ajax.request({
					url:path+'/vehicleExpense/vehicleExpense.do?method=updateVehicleExpense',
					method :'POST', 
					params:{
						id: tmpid, deviceId: tmpdeviceId, vehicleDepreciation: tmpvehicleDepreciation, personalExpenses: tmppersonalExpenses,
						insurance: tmpinsurance, maintenance: tmpmaintenance, toll: tmptoll, 
						annualCheck: tmpannualCheck, createDate: tmpcreateDate
					 },
					 //timeout : 10000,
					 success : function(request) {
					   var res = Ext.decode(request.responseText);
					 	if(res.result==1){
					 		store.load({params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
					   		Ext.Msg.alert('��ʾ', '�����ɹ�');
					   		return;
					   }else{
					   		//store.reload();
					   		Ext.Msg.alert('��ʾ', "����ʧ��!");
					   		return;
					   }
					 },
					 failure : function(request) {
						 Ext.Msg.alert('��ʾ', "����ʧ��!");
					 }
					});
			}
		});
	},
	iconCls: 'icon-modify'
});

	var simple = new Ext.FormPanel({
		labelWidth: 100,
		frame: true,
        region: 'center',
		bodyStyle: 'padding:5px 5px 0',
		autoScroll : true,
		width: 200,
		defaults: {width: 150},
		defaultType: 'textfield',
		items: [{
			fieldLabel: '�ն�����',
		    id: 'frmdevice',
		    width: 150,
		    readOnly: true,
		    allowBlank:false
		},{
			fieldLabel: '�����۾ɷ���',
			xtype: 'numberfield',
		    id: 'frmvehicleDepreciation',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '��Ա����',
			xtype: 'numberfield',
		    id: 'frmpersonalExpenses',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '����̯������',
			xtype: 'numberfield',
		    id: 'frminsurance',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: 'ά�ޱ�������',
			xtype: 'numberfield',
		    id: 'frmmaintenance',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '��·���ŷ�',
			xtype: 'numberfield',
		    id: 'frmtoll',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '��켰��������',
			xtype: 'numberfield',
		    id: 'frmannualCheck',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '����',
		    xtype: 'datefield',
		    id: 'frmcreateDate',
		    editable: false,
		    format: 'Y-m-d',
		    width: 150,
		    allowBlank:false
		},{
		    xtype: 'hidden',
		    id: 'frmdeviceId'
		},{
		    xtype: 'hidden',
		    id: 'frmid'
		}]
	});
	

var proxy = new Ext.data.HttpProxy({
	url: path+'/vehicleExpense/vehicleExpense.do?method=listVehicleExpense'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'deviceId'},
    {name: 'termName'},
    {name: 'vehicleNumber'},
    {name: 'simcard'},
    {name: 'groupName'},
    {name: 'vehicleDepreciation'},
    {name: 'personalExpenses'},
    {name: 'insurance'},
    {name: 'maintenance'},
    {name: 'toll'},
    {name: 'annualCheck'},
    {name: 'createDate'}
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

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmdevice').setValue(rec.get('termName'));
			Ext.getCmp('frmvehicleDepreciation').setValue(rec.get('vehicleDepreciation'));
			Ext.getCmp('frmpersonalExpenses').setValue(rec.get('personalExpenses'));
			Ext.getCmp('frminsurance').setValue(rec.get('insurance'));
			Ext.getCmp('frmmaintenance').setValue(rec.get('maintenance'));
			Ext.getCmp('frmtoll').setValue(rec.get('toll'));
			Ext.getCmp('frmannualCheck').setValue(rec.get('annualCheck'));
			Ext.getCmp('frmcreateDate').setValue(rec.get('createDate'));
			Ext.getCmp('frmdeviceId').setValue(rec.get('deviceId'));
			Ext.getCmp('frmid').setValue(rec.get('id'));
			
	    }
	}
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    //{id:'deviceId',header: "deviceId", width: 40, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "����",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "���ƺ�",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "�ֻ���",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "�����۾ɷ���",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleDepreciation'},
    {header: "��Ա����",fixed:true, width: 100, sortable: true, dataIndex: 'personalExpenses'},
    {header: "����̯������",fixed:true, width: 100, sortable: true, dataIndex: 'insurance'},
    {header: "ά�ޱ�������",fixed:true, width: 100, sortable: true, dataIndex: 'maintenance'},
    {header: "��·���ŷ�",fixed:true, width: 100, sortable: true, dataIndex: 'toll'},
    {header: "��켰��������",fixed:true, width: 100, sortable: true, dataIndex: 'annualCheck'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'createDate'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'west',
    width: 500,
    loadMask: {msg:'��ѯ��...'},
    enableColumnHide : false,
    store: store,
    sm : sm,
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
	}),'-',delbut
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
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
});