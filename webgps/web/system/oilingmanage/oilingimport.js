var searchValue = '';
var lastMonth = new Date();
lastMonth.setMonth(lastMonth.getMonth()-1);
var startTime = (lastMonth).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";



var proxy = new Ext.data.HttpProxy({
	url: path+'/oiling/oiling.do?method=listOiling'
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
    {name: 'oilLiter'},
    {name: 'oilCost'},
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

//var sm = new Ext.grid.CheckboxSelectionModel({});
//Ext.override(Ext.grid.CheckboxSelectionModel, { 
//	handleMouseDown : function(g, rowIndex, e){   
//	    if(e.button !== 0 || this.isLocked()){   
//	        return;   
//	    }   
//	    var view = this.grid.getView();   
//	    if(e.shiftKey && !this.singleSelect && this.last !== false){   
//	        var last = this.last;   
//	        this.selectRange(last, rowIndex, e.ctrlKey);   
//	        this.last = last; // reset the last   
//	        view.focusRow(rowIndex);   
//	    }else{   
//	        var isSelected = this.isSelected(rowIndex);   
//	    if(isSelected){   
//	        this.deselectRow(rowIndex);   
//	}else if(!isSelected || this.getCount() > 1){   
//	        this.selectRow(rowIndex, true);   
//	        view.focusRow(rowIndex);   
//	        }   
//	    }   
//	} 
//	});
var userColumns =  [
//sm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    //{id:'deviceId',header: "deviceId", width: 40, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "名称",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "车牌号",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "手机号",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "所属组",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "加油量(升)",fixed:true, width: 100, sortable: true, dataIndex: 'oilLiter'},
    {header: "加油金额",fixed:true, width: 100, sortable: true, dataIndex: 'oilCost'},
    {header: "加油日期",fixed:true, width: 100, sortable: true, dataIndex: 'createDate'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'center',
    width: 500,
    loadMask: {msg:'查询中...'},
    enableColumnHide : false,
    store: store,
//    sm : sm,
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
		text: '查询',
		handler: function(){
			searchValue = Ext.getCmp('DeviceIdField').getValue();
			startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
			endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
	    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
	    },
		iconCls: 'icon-search'
	}),'-',new Ext.Action({
		text: '导出Excel',
		id : 'delbut',
        handler: function(){
        		var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime+'&endTime='+endTime;
        		document.excelformSen.action =path+'/oiling/oiling.do?method=listOiling&'+tmpparam;
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
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
});

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    
});