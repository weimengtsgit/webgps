var gpsDeviceid = '';
var searchValue = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
//var startTime = tmpdate+' '+'08:00:00';
//var endTime = tmpdate+' '+'18:00:00';
var startTime = tmpdate;
var endTime = tmpdate;

function getDeviceId(){
	var treeArr = new Array();
	parent.getTreeId(parent.root, treeArr);
	var tmpgpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2) {
			tmpgpsDeviceid += idArr[0] + ',';
		}
	}
	if (tmpgpsDeviceid.length > 0) {
		tmpgpsDeviceid = tmpgpsDeviceid.substring(0, tmpgpsDeviceid.length - 1);
	}
	return tmpgpsDeviceid;
}

Ext.onReady(function(){
gpsDeviceid = getDeviceId();
Ext.QuickTips.init();
var proxy = new Ext.data.HttpProxy({
    url: path+'/stat/distanceStat.do?method=listDistanceStatByCustom'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'a',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'vehicleNumber'},
    {name: 'distance'},
    {name: 'tjdate'}
    
]);

var store = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 14, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime, deviceIds: gpsDeviceid}},
    restful: true,
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';*/
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime ,deviceIds: gpsDeviceid
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
            {header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
            {header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
            {header: '公里数', width: 130, sortable: true,  dataIndex: 'distance',align: 'right'
            //,renderer:function(val){
            //	return '<span style="display:table;width:100%;" align="right">' + val + '</span>';
            //}
            },
            {id:'tjdate' ,header: '统计时间', width: 130, sortable: true,  dataIndex: 'tjdate'}
        ],
        stripeRows: true,
        autoExpandColumn: 'tjdate',
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
	    }),
	    /*new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 1
        }),*/
        '-',new Ext.form.Label({
        		text:'结束时间'
        }),new Ext.form.DateField({
				width: 100,
				fieldLabel: 'Date',
				format:'Y-m-d',
				editable: false,
				value: new Date(),
	            id: 'endDateField'
	    }),
	    /*new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'endTimeField',
                value: '18:00',
                increment : 1
        }),*/
        '-',
        /*new Ext.form.Label({
        		text:'名称'
        }),*/
        new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
		    	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		    	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		    	        	startTime = tmpstartdate;
		    	        	endTime = tmpenddate;
		    	        	gpsDeviceid = getDeviceId();
		        			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid , searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime}});
		        			
		                }
        			}
        		}
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	//var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	//var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	        	startTime = tmpstartdate;
	        	endTime = tmpenddate;
	        	//startTime= tmpstartdate+' '+tmpstarttime+':00';
	        	//endTime= tmpenddate+' '+tmpendtime+':00'
	        	
	        	gpsDeviceid = getDeviceId();
    			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid , searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime}});
    			
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	        	/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';*/
    			//store.load({params:{expExcel: 'true', start:0, limit:14, searchValue: searchValue , startTime: startTime, endTime: endTime}});
    			Ext.Msg.show({
			    	msg: '正在导出 请稍等...',
			        progressText: '导出中...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime+'&endTime='+endTime+'&deviceIds='+gpsDeviceid;
    			document.excelform.action = path+'/stat/distanceStat.do?method=listDistanceStatByCustom&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);
	        },
	        iconCls: 'icon-excel'
	    })
        ]
        }),
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });
    
	var view = new Ext.Viewport({
		layout: 'border',
		items: [ grid ]
	});
	
    //var tmpdate = (new Date()).pattern("yyyy-M-d");
    //store.load({params:{start:0, limit:14, startTime: tmpdate+' '+'08:00:00', endTime: tmpdate+' '+'18:00:00'}});
    
});
