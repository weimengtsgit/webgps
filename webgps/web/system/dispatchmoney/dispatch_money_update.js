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
		Ext.MessageBox.confirm('��ʾ', '��ȷ���޸�������?', function (btn){
			if(btn=='yes'){
				var tmpdeviceId = Ext.getCmp('frmdeviceId').getValue();
				if(tmpdeviceId.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}
				var dispatchDate = Ext.getCmp('dispatchDate').getValue();
				if(dispatchDate.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ����������!');
					return;
				}
				var dispatchAmount = Ext.getCmp('dispatchAmount').getValue();
				if(dispatchAmount.length<=0){
					Ext.Msg.alert('��ʾ', '��������������!');
					return;
				}
				
				var frmhandler = Ext.getCmp('handler').getValue();
				if(frmhandler.length<=0){
					Ext.Msg.alert('��ʾ', '�����뾭����!');
					return;
				}
				var frmdemo = Ext.getCmp('frmdemo').getValue();

				Ext.Msg.show({
					msg: '�����޸� ���Ե�...',
					progressText: '�޸�...',
					width:300,
					wait:true,
					icon:'ext-mb-download'
				});
				Ext.Ajax.request({
					url:path+'/dispatchMoney/dispatchMoney.do?method=updateDispatchMoney',
					method :'POST', 
					params:{
						id: tmpid,tmpdeviceId: encodeURI(tmpdeviceId), dispatchDate:dispatchDate,
						frmhandler: encodeURI(frmhandler),dispatchAmount: encodeURI(dispatchAmount),frmdemo: encodeURI(frmdemo) 
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
	iconCls: 'icon-save'
});

	var simple = new Ext.FormPanel({
		labelWidth: 75,
		frame: true,
		flex: 1,
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
			fieldLabel: '��������',
		    id: 'dispatchDate',
		    width: 150,
		    xtype: 'datefield',
		    editable: false,
		    format: 'Y-m-d',
		    allowBlank:false
		},{
		    fieldLabel: '��������',
		    id: 'dispatchAmount',
			xtype: 'numberfield',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '������',
		    id: 'handler',
		    width: 150,
		    allowBlank:false
		},{
		    xtype:'textarea',	
			fieldLabel: '��ע',
		    id: 'frmdemo',
		    width: 150
		},{
		    xtype: 'hidden',
		    id: 'frmdeviceId'
		},{
		    xtype: 'hidden',
		    id: 'frmid'
		}]
	});
	

var proxy = new Ext.data.HttpProxy({
	url: path+'/dispatchMoney/dispatchMoney.do?method=listdispatchCondition'
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
    {name: 'dispatchDate'},
    {name: 'dispatchamount'},
    {name: 'frmhandler'},
    {name: 'demo'},
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
			Ext.getCmp('dispatchDate').setValue(rec.get('dispatchDate'));
			Ext.getCmp('dispatchAmount').setValue(rec.get('dispatchamount'));
			Ext.getCmp('handler').setValue(rec.get('frmhandler'));
			Ext.getCmp('frmdemo').setValue(rec.get('demo'));
			Ext.getCmp('frmdeviceId').setValue(rec.get('deviceId'));
			Ext.getCmp('frmid').setValue(rec.get('id'));
			
	    }
	}
});
var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'deviceId',header: "deviceId", width: 40, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "����",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "���ƺ�",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "�ֻ���",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'dispatchDate'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'dispatchamount'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'frmhandler'},
    {header: "��ע",fixed:true, width: 100, sortable: true, dataIndex: 'demo'}
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