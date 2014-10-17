var searchValue = '';

var delbut = new Ext.Action({
	text: '����',
	id : 'delbut',
	handler: function(){
		Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�����������?', function (btn){
			if(btn=='yes'){  
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
					url:path+'/carTypeInfo/carTypeInfo.do?method=addCarTypeInfo',
					method :'POST', 
					params:{
						typename: encodeURI(tmptypename), desction: encodeURI(tmpdesction), oilwear: tmpoilwear, empCode: empCode, userId: userId
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
	iconCls: 'icon-save'
});

	var simple = new Ext.FormPanel({
        region: 'center',
		labelWidth: 100,
		frame: true,
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
    			}
    		}
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