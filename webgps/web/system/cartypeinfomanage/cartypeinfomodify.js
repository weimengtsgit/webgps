var searchValue = '';

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
				var tmpcrtdate = Ext.getCmp('frmcrtdate').getValue();
				var tmptypename = Ext.getCmp('frmtypename').getValue();
				if(tmptypename.length<=0){
					Ext.Msg.alert('��ʾ', '�������ն���������!');
					return;
				}
				var tmpdesction = Ext.getCmp('frmdesction').getValue();
				var tmpoilwear = Ext.getCmp('frmoilwear').getValue();
				if(tmpoilwear.length<=0){
					Ext.Msg.alert('��ʾ', '�����복���ͺ�!');
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
					url:path+'/carTypeInfo/carTypeInfo.do?method=updateCarTypeInfo', 
					method :'POST', 
					params:{
						id: tmpid, typename: encodeURI(tmptypename), desction: encodeURI(tmpdesction), 
						oilwear: tmpoilwear, empCode: empCode, userId: userId, crtdate: tmpcrtdate
					 },
					 //timeout : 10000,
					 success : function(request) {
					   var res = Ext.decode(request.responseText);
					 	if(res.result==1){
					 		store.load({params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }});
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
			fieldLabel: '������������',
		    id: 'frmtypename',
		    width: 150,
		    allowBlank:false
		},{
			fieldLabel: '������������',
		    id: 'frmdesction',
		    width: 150,
		    allowBlank:false
		},{
			fieldLabel: '�����ͺ�',
			xtype: 'numberfield',
		    id: 'frmoilwear',
		    width: 150,
		    allowBlank:false
		},{
		    xtype: 'hidden',
		    id: 'frmid'
		},{
		    xtype: 'hidden',
		    id: 'frmcrtdate'
		}]
	});
	

var proxy = new Ext.data.HttpProxy({
	url: path+'/carTypeInfo/carTypeInfo.do?method=listCarTypeInfo'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'typeName'},
    {name: 'iconPath'},
    {name: 'desction'},
    {name: 'enCode'},
    {name: 'oilWear'},
    {name: 'userId'},
    {name: 'crtdate'}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'store',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) 
    			};
    		}
    	}
    }
});

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmtypename').setValue(rec.get('typeName'));
			Ext.getCmp('frmdesction').setValue(rec.get('desction'));
			Ext.getCmp('frmoilwear').setValue(rec.get('oilWear'));
			Ext.getCmp('frmid').setValue(rec.get('id'));
			Ext.getCmp('frmcrtdate').setValue(rec.get('crtdate'));
			
	    }
	}
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {header: "������������",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    {header: "������������",fixed:true, width: 100, sortable: true, dataIndex: 'desction'},
    {header: "�����ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'oilWear'},
    {header: "��������",fixed:true, width: 130, sortable: true, dataIndex: 'crtdate'}
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
	tbar: [new Ext.form.TextField({
	    id: 'DeviceIdField',
	    width: 80,
	    enableKeyEvents: true,
	    listeners: {
        	keypress : function( textField, e ) {
		        if (e.getKey() == e.ENTER) {
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
			    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
			    }
        	}
        }
	}),'-',new Ext.Action({
		text: '��ѯ',
		handler: function(){
			searchValue = Ext.getCmp('DeviceIdField').getValue();
	    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
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