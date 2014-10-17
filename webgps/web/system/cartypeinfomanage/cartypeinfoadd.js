var searchValue = '';

var delbut = new Ext.Action({
	text: '增加',
	id : 'delbut',
	handler: function(){
		Ext.MessageBox.confirm('提示', '您确定要添加新数据吗?', function (btn){
			if(btn=='yes'){  
				var tmptypename = Ext.getCmp('frmtypename').getValue();
				if(tmptypename.length<=0){
					Ext.Msg.alert('提示', '请输入终端类型名称!');
					return;
				}
				var tmpdesction = Ext.getCmp('frmdesction').getValue();
				var tmpoilwear = Ext.getCmp('frmoilwear').getValue();
				if(tmpoilwear.length<=0){
					Ext.Msg.alert('提示', '请输入车辆油耗!');
					return;
				}
				Ext.Msg.show({
					msg: '正在保存 请稍等...',
					progressText: '保存...',
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
					   		Ext.Msg.alert('提示', '操作成功');
					   		return;
					   }else{
					   		//store.reload();
					   		Ext.Msg.alert('提示', "操作失败!");
					   		return;
					   }
					 },
					 failure : function(request) {
						 Ext.Msg.alert('提示', "操作失败!");
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
			fieldLabel: '车辆类型名称',
		    id: 'frmtypename',
		    width: 150,
		    allowBlank:false
		},{
			fieldLabel: '车辆类型描述',
		    id: 'frmdesction',
		    width: 150,
		    allowBlank:false
		},{
			fieldLabel: '车辆油耗',
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
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {header: "车辆类型名称",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    {header: "车辆类型描述",fixed:true, width: 100, sortable: true, dataIndex: 'desction'},
    {header: "车辆油耗",fixed:true, width: 100, sortable: true, dataIndex: 'oilWear'},
    {header: "创建日期",fixed:true, width: 130, sortable: true, dataIndex: 'crtdate'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'west',
    width: 500,
    loadMask: {msg:'查询中...'},
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
		text: '查询',
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
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
});

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});

});