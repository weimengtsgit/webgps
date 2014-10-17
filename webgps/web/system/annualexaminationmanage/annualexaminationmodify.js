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
				var tmpexaminationDate = Ext.getCmp('frmexaminationDate').getValue();
				if(tmpexaminationDate.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ����������!');
					return;
				}
				var tmpproject = Ext.getCmp('frmproject').getValue();
				if(tmpproject.length<=0){
					Ext.Msg.alert('��ʾ', '������������Ŀ!');
					return;
				}
				var tmpcondition = Ext.getCmp('frmcondition').getValue();
				if(tmpcondition.length<=0){
					Ext.Msg.alert('��ʾ', '�������������!');
					return;
				}
				var tmpexpenses = Ext.getCmp('frmexpenses').getValue();
				if(tmpexpenses.length<=0){
					Ext.Msg.alert('��ʾ', '������������!');
					return;
				}
				var tmphandler = Ext.getCmp('frmhandler').getValue();
				if(tmphandler.length<=0){
					Ext.Msg.alert('��ʾ', '�����뾭����!');
					return;
				}
				var tmpexpireDate = Ext.getCmp('frmexpireDate').getValue();
				if(tmpexpireDate.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ��������!');
					return;
				}
				var tmpdemo = Ext.getCmp('frmdemo').getValue();
				var tmpdeviceId = Ext.getCmp('frmdeviceId').getValue();
				Ext.Msg.show({
					msg: '���ڱ��� ���Ե�...',
					progressText: '����...',
					width:300,
					wait:true,
					icon:'ext-mb-download'
				});
				Ext.Ajax.request({
					url:path+'/annualExamination/annualExamination.do?method=updateAnnualExamination',
					method :'POST', 
					params:{
						id: tmpid, deviceId: tmpdeviceId, examinationDate: tmpexaminationDate  , project: encodeURI(tmpproject) ,
						condition: encodeURI(tmpcondition), expenses: tmpexpenses, handler: encodeURI(tmphandler),
						expireDate: tmpexpireDate, demo: encodeURI(tmpdemo)
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
		labelWidth: 75,
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
		    fieldLabel: '��������',
		    xtype: 'datefield',
		    id: 'frmexaminationDate',
		    editable: false,
		    format: 'Y-m-d',
		    width: 150,
		    allowBlank:false
		},{
			fieldLabel: '������Ŀ',
		    id: 'frmproject',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '�������',
		    id: 'frmcondition',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '������',
			xtype: 'numberfield',
		    id: 'frmexpenses',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '������',
		    id: 'frmhandler',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '��������',
		    xtype: 'datefield',
		    id: 'frmexpireDate',
		    editable: false,
		    format: 'Y-m-d',
		    width: 150,
		    allowBlank:false
		},{
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
	url: path+'/annualExamination/annualExamination.do?method=listAnnualExamination'
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
    {name: 'examinationDate'},
    {name: 'project'},
    {name: 'condition'},
    {name: 'expenses'},
    {name: 'handler'},
    {name: 'expireDate'},
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

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmdevice').setValue(rec.get('termName'));
			Ext.getCmp('frmexaminationDate').setValue(rec.get('examinationDate'));
			Ext.getCmp('frmproject').setValue(rec.get('project'));
			Ext.getCmp('frmcondition').setValue(rec.get('condition'));
			Ext.getCmp('frmexpenses').setValue(rec.get('expenses'));
			Ext.getCmp('frmhandler').setValue(rec.get('handler'));
			Ext.getCmp('frmexpireDate').setValue(rec.get('expireDate'));
			Ext.getCmp('frmdemo').setValue(rec.get('demo'));
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
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'examinationDate'},
    {header: "������Ŀ",fixed:true, width: 100, sortable: true, dataIndex: 'project'},
    {header: "�������",fixed:true, width: 100, sortable: true, dataIndex: 'condition'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'expenses'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'handler'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'expireDate'},
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