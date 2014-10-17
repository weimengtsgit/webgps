var gpsDeviceid = '';
var deviceid = '';
var searchValue = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
var startTime = tmpdate+' '+'08:00:00';
var endTime = tmpdate+' '+'18:00:00';
var cardpanel;
var storedetail;
var openTime;
var closeTime;

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

//车辆定位信息
var proxy = new Ext.data.HttpProxy({
	 url: path+'/stat/visitStat.do?method=listAttendanceReportDetail'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'deviceId'},
    {name: 'vehicleNumber'},
    {name: 'simcard'},
    {name: 'gpsTime'},
    {name: 'pd'},
    {name: 'visitId'},
    {name: 'visitName'},
    {name: 'longitude'},
    {name: 'latitude'},
    {name: 'speed'},
    {name: 'direction'},
    {name: 'distance'},
    {name: 'accStatus'},
    {name: 'temperature'}
]);

var store = new Ext.data.Store({
    restful: true,
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,deviceId: gpsDeviceid
    			}
    		}
    	}
    }
});

var grid = new Ext.grid.GridPanel({
    store: store,
    loadMask: {msg:'查询中...'},
    columns: [
            {header: '车牌号', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
			{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
			{header: '纬度', width: 130, sortable: true,  dataIndex: 'latitude'},
			{header: '经度', width: 130, sortable: true,  dataIndex: 'longitude'},
			{header: '速度', width: 130, sortable: true,  dataIndex: 'speed'},
			{header: '方向', width: 130, sortable: true,  dataIndex: 'direction'},
			{header: '里程', width: 130, sortable: true,  dataIndex: 'distance'},
			{header: '行驶状态', width: 130, sortable: true,  dataIndex: 'accStatus'},
			{header: '位置描述', width: 130, sortable: true,  dataIndex: 'pd'},
			{header: '到访客户', width: 130, sortable: true,  dataIndex: 'visitName'},
			{header: '时间', width: 130, sortable: true,  dataIndex: 'gpsTime'},
			{header: '温度', width: 130, sortable: true,  dataIndex: 'temperature',renderer:function(r,d,v){return r=="null"?"":r;}}
        ],
        stripeRows: true,
        stateful: true,
        stateId: 'grid',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'日期'
        }),new Ext.form.DateField({
				id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				editable: false,
				maxValue:new Date(),
				value: new Date()
	    }),'-' ,new Ext.form.Label({
        		text:'开始时间'
        }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
				editable: false,
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 5
        }),'-',new Ext.form.Label({
        		text:'结束时间'
        })
//        ,new Ext.form.DateField({
//				width: 100,
//				fieldLabel: 'Date',
//				format:'Y-m-d',
//				editable: false,
//				value: new Date(),
//	            id: 'endDateField'
//	    })
        ,new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
				editable: false,
                id: 'endTimeField',
                value: '18:00',
                increment : 5
        }),'-',new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	    		var expExcel="false";
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('startDateField').getValue().format('Y-m-d');//
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
	        	endTime= tmpenddate+' '+tmpendtime+':00'
	        	
    			gpsDeviceid = getDeviceId();
	        	var ids=gpsDeviceid.split(",");
	        	if(ids.length>22){
	        		Ext.Msg.alert('提示','最多选择22个终端!');
	        	}else{
	        		if(tmpstarttime>=tmpendtime){
	        			Ext.Msg.alert('提示','开始时间必须小于于结束时间!');
	   				}else{
	   					store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
	   				}
//	        		if(tmpstartdate>tmpenddate){
//	        			Ext.Msg.alert('提示','开始日期不能大于结束日期!');
//	        		}else if(tmpstartdate==tmpenddate){
//	        			if(tmpstarttime>=tmpendtime){
//    	        			Ext.Msg.alert('提示','开始时间必须小于于结束时间!');
//        				}else{
//        					store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
//        				}
//	        		}else{
//	        			store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
//	        		}	
	        	}	
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	    		var expExcel='true';
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('startDateField').getValue().format('Y-m-d');//
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';
	        	
	        	var ids=gpsDeviceid.split(",");
	        	if(ids.length>22){
	        		Ext.Msg.alert('提示','最多选择22个终端!');
	        	}else{
	        		
	        		if(tmpstarttime>=tmpendtime){
	        			Ext.Msg.alert('提示','开始时间必须小于于结束时间!');
    				}else{
    					var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
    	    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
    	    			document.excelform.submit();
    	    			Ext.Msg.show({
    				    	msg: '正在导出 请稍等...',
    				        progressText: '导出中...',
    				        width:300,
    				        wait:true,
    				        icon:'ext-mb-download'
    				    });
    	    			setTimeout(function(){Ext.MessageBox.hide()},3000);
    				}
//	        		if(tmpstartdate>tmpenddate){
//	        			Ext.Msg.alert('提示','开始日期不能大于结束日期!');
//	        		}else if(tmpstartdate==tmpenddate){
//	        			if(tmpstarttime>=tmpendtime){
//    	        			Ext.Msg.alert('提示','开始时间必须小于于结束时间!');
//        				}else{
//        					var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
//        	    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
//        	    			document.excelform.submit();
//        	    			Ext.Msg.show({
//        				    	msg: '正在导出 请稍等...',
//        				        progressText: '导出中...',
//        				        width:300,
//        				        wait:true,
//        				        icon:'ext-mb-download'
//        				    });
//        	    			setTimeout(function(){Ext.MessageBox.hide()},3000);
//        				}
//	        		}else{
//	        			var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
//		    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
//		    			document.excelform.submit();
//		    			Ext.Msg.show({
//					    	msg: '正在导出 请稍等...',
//					        progressText: '导出中...',
//					        width:300,
//					        wait:true,
//					        icon:'ext-mb-download'
//					    });
//		    			setTimeout(function(){Ext.MessageBox.hide()},3000);
//	        		}
	        	}
	        	
	        },
	        iconCls: 'icon-excel'
	    })
        ]
        }),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });

    cardpanel = new Ext.Panel({
		layout: 'card',
		activeItem : 0,
		region: 'center',
		items: [grid]
	});

	var view = new Ext.Viewport({
		layout: 'border',
		items: [ cardpanel ]
	});
	
});
