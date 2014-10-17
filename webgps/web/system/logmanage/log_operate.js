

var proxy = new Ext.data.HttpProxy({
    url: path+'/log/optlog.do?method=listOptLog'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'userName'},
    {name: 'optDesc'},
    {name: 'optTime'},
    {name: 'accessIp'},
    {name: 'result'}
    
]);

var searchValue = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
var startTime = tmpdate+' '+'08:00:00';
var endTime = tmpdate+' '+'18:00:00';

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 15, searchValue: searchValue ,startTime: startTime, endTime: endTime}},
    restful: true,
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';
    			this.baseParams ={
    				searchValue: searchValue , startTime: startTime, endTime: endTime
    			}
    		}
    	}
    }
});


var grid = new Ext.grid.GridPanel({
	region: 'center',
    store: store,
    loadMask: {msg:'查询中...'},
    columns: [
    		new Ext.grid.RowNumberer({header:'序号',width:40}),
            {id:'id',header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
            {header: '操作人', width: 130, sortable: true,  dataIndex: 'userName'},
            {	header: '操作描述', 
            	width: 350, sortable: true,  
            	dataIndex: 'optDesc',
            	renderer: function tips(val) {
	        	var tmp = '';
	            if(val.indexOf('_recv')!=-1){
					val = val.substring(0,val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
				}else{
					tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				}
			
				return tmp;
				//return '<span style="display:table;width:100%;" qtip=' + val + '>' + val + '</span>';
            	}
            },
            {header: '操作时间', width: 130, sortable: true, dataIndex: 'optTime'},
            {header: '访问地址', width: 130, sortable: true, dataIndex: 'accessIp'},
            {header: '操作结果', width: 130, sortable: true, dataIndex: 'result'}
        ],
        stripeRows: true,
        //autoExpandColumn: 'company',
        //height: 350,
        //width: 600,
        //title: 'Array Grid',
        stateful: true,
        stateId: 'grid',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'开始时间'
        }),new Ext.form.DateField({
				id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				editable: false,
				value: new Date()
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
				editable: false,
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'结束时间'
        }),new Ext.form.DateField({
				width: 100,
				fieldLabel: 'Date',
				format:'Y-m-d',
				value: new Date(),
				editable: false,
	            id: 'endDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'endTimeField',
                value: '18:00',
				editable: false,
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'名称'
        }),new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
		    	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
		    	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		    	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
		    	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
		    	        	var searchValue = encodeURI(tmpDeviceIdField);
		    	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
		    	        	var endTime = tmpenddate+' '+tmpendtime+':00';
		        			store.load({params:{start:0, limit:15, searchValue: searchValue , startTime: startTime, endTime: endTime}});
		        			
		                }
        			}
        		}
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';
    			store.load({params:{start:0, limit:15, searchValue: searchValue , startTime: startTime, endTime: endTime}});
    			
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';
    			//store.load({params:{expExcel: 'true', start:0, limit:15, searchValue: searchValue , startTime: startTime, endTime: endTime}});
    			var tmpparam = 'expExcel=true&searchValue='+searchValue+'&startTime='+startTime+'&endTime='+endTime;
    			document.excelform.action = path+'/log/optlog.do?method=listOptLog&'+tmpparam;
    			document.excelform.submit();
				
	        },
	        iconCls: 'icon-excel'
	    })
        ]
        }),
        bbar: new Ext.PagingToolbar({
            pageSize: 15,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });
    
    
Ext.onReady(function(){
	Ext.QuickTips.init();
	var view = new Ext.Viewport({
		layout: 'border',
		items: [ grid ]
	});
	
    //var tmpdate = (new Date()).pattern("yyyy-M-d");
    //store.load({params:{start:0, limit:15, startTime: tmpdate+' '+'08:00:00', endTime: tmpdate+' '+'18:00:00'}});
    
});
