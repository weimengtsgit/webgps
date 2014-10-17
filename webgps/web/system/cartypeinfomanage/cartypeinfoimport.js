var searchValue = '';

var delbut = new Ext.Action({
	text: '����Excel',
	id : 'delbut',
	  handler: function(){
		document.excelformSen.action =path+'/carTypeInfo/carTypeInfo.do?method=listCarTypeInfo&expExcel=true';
		document.excelformSen.submit();
		setTimeout(function(){Ext.MessageBox.hide()},3000);
},
	iconCls: 'icon-excel'
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
//sm,
new Ext.grid.RowNumberer({header:'���',width:40}),
{header: "������������",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
{header: "������������",fixed:true, width: 100, sortable: true, dataIndex: 'desction'},
{header: "�����ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'oilWear'},
{header: "��������",fixed:true, width: 130, sortable: true, dataIndex: 'crtdate'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'center',
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
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    
});