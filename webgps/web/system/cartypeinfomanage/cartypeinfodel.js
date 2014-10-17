var searchValue = '';

var delbut = new Ext.Action({
	text: '删除',
	id : 'delbut',
	handler: function(){
		var tmpRecArr = sm.getSelections();
		if(tmpRecArr.length<=0){
			Ext.MessageBox.alert('提示', '请选择要删除的数据!');
    		return ;
		}
		Ext.MessageBox.confirm('提示', '您确定要删除数据吗?', function (btn){
			if(btn=='yes'){
				var tmpRecArr = sm.getSelections();
				var ids = '';
				for(var i=0;i<tmpRecArr.length;i++){
					ids+=tmpRecArr[i].get('id')+',';
				}
				ids = ids.substring(0,ids.length-1);
				Ext.Msg.show({
					msg: '正在删除 请稍等...',
					progressText: '删除...',
					width:300,
					wait:true,
					icon:'ext-mb-download'
				});
				Ext.Ajax.request({
					url:path+'/carTypeInfo/carTypeInfo.do?method=delCarTypeInfo',
					method :'POST', 
					params:{
						ids: ids
					 },
					 //timeout : 10000,
					 success : function(request) {
					   var res = Ext.decode(request.responseText);
					 	if(res.result==1){
					 		store.load({params:{start: 0, limit: 20, searchValue: encodeURI(searchValue)}});
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
	iconCls: 'icon-del'
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

var sm = new Ext.grid.CheckboxSelectionModel({});
Ext.override(Ext.grid.CheckboxSelectionModel, { 
	handleMouseDown : function(g, rowIndex, e){   
	    if(e.button !== 0 || this.isLocked()){   
	        return;   
	    }   
	    var view = this.grid.getView();   
	    if(e.shiftKey && !this.singleSelect && this.last !== false){   
	        var last = this.last;   
	        this.selectRange(last, rowIndex, e.ctrlKey);   
	        this.last = last; // reset the last   
	        view.focusRow(rowIndex);   
	    }else{   
	        var isSelected = this.isSelected(rowIndex);   
	    if(isSelected){   
	        this.deselectRow(rowIndex);   
	}else if(!isSelected || this.getCount() > 1){   
	        this.selectRow(rowIndex, true);   
	        view.focusRow(rowIndex);   
	        }   
	    }   
	} 
	});
var userColumns =  [
sm,
new Ext.grid.RowNumberer({header:'序号',width:40}),
{header: "车辆类型名称",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
{header: "车辆类型描述",fixed:true, width: 100, sortable: true, dataIndex: 'desction'},
{header: "车辆油耗",fixed:true, width: 100, sortable: true, dataIndex: 'oilWear'},
{header: "创建日期",fixed:true, width: 130, sortable: true, dataIndex: 'crtdate'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'center',
    width: 500,
    loadMask: {msg:'查询中...'},
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
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    
});