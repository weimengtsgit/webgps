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

function showgriddetail(record){
	var tmpv = record.get('vehicleNumber');
	deviceid = record.get('deviceId');
	openTime = record.get('openTime');
	closeTime = record.get('closeTime');
	//alert(tmpg+' '+tmpv+'('+tmps+')的考勤报表:');
	cardpanel.layout.setActiveItem(1);
	Ext.getCmp('griddetail_title').setText('('+tmpv+')的详细日志统计报表:');
	storedetail.load({params:{start:0, limit:14,startTime: openTime, endTime: closeTime, poiId: deviceid}});
}

Ext.onReady(function(){
gpsDeviceid = getDeviceId();
Ext.QuickTips.init();

var proxydetail = new Ext.data.HttpProxy({
    url: path+'/stat/visitStat.do?method=listAttendanceReportDetail'
});

var readerdetail = new Ext.data.JsonReader({
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
    {name: 'visitName'}
]);

storedetail = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 14, startTime: startTime, endTime: endTime}},
    restful: true,
    proxy: proxydetail,
    reader: readerdetail,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				startTime: openTime, endTime: closeTime, deviceId : deviceid
    			};
    		}
    	}
    }
});


var griddetail = new Ext.grid.GridPanel({
    store: storedetail,
    loadMask: {msg:'查询中...'},
    columns: [
            {header: 'deviceId', width: 130, sortable: true,  dataIndex: 'deviceId', hidden:true},
			{header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber', hidden:true},
			{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard', hidden:true},
			{header: '时间', width: 130, sortable: true,  dataIndex: 'gpsTime'},
			{id: 'pd' , header: '位置描述', width: 350, sortable: true,  dataIndex: 'pd',
	            renderer: function tips(val) {
					return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				}},
			{header: '标注点名称id', width: 130, sortable: true,  dataIndex: 'visitId', hidden:true},
			{header: '到访客户', width: 130, sortable: true,  dataIndex: 'visitName'}

        ],
        stripeRows: true,
        autoExpandColumn: 'pd',
        //height: 350,
        //width: 600,
        //title: 'Array Grid',
        tbar : new Ext.Toolbar({
        	items:[{
	        	id: 'griddetail_title',
	        	//enableToggle: true,
	        	//allowDepress: false,
	        	text: ''
	        },'-',new Ext.Action({
		        text: '返回',
		        handler: function(){
		        	cardpanel.layout.setActiveItem(0);
		        },
		        iconCls: 'icon-search'
		    })]
        }),
        stateful: true,
        stateId: 'grid',
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: storedetail,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });


//拜访统计
var proxy = new Ext.data.HttpProxy({
    url: path+'/stat/visitStat.do?method=listAttendanceReport'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'deviceId'},
    {name: 'vehicleNumber'},
    {name: 'groupName'},
    {name: 'simcard'},
    {name: 'openTime'},
    {name: 'closeTime'},
    {name: 'process'}
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
	        	startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime = tmpenddate+' '+tmpendtime+':00';*/
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime, deviceIds: gpsDeviceid
    			};
    		}
    	}
    }
});

var grid = new Ext.grid.GridPanel({
    store: store,
    loadMask: {msg:'查询中...'},
    columns: [
            {header: 'deviceId', width: 130, sortable: true,  dataIndex: 'deviceId', hidden:true},
			{header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
			{header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
			{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
			{header: '开机时间', width: 130, sortable: true,  dataIndex: 'openTime'},
			{header: '关机时间', width: 130, sortable: true,  dataIndex: 'closeTime'},
			{header: '操作', width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    //在这里定义了2个操作,分别赋予不同的css class以便区分   
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='showgriddetail'>详细信息</a>";
			    var resultStr = String.format(formatStr, record.get('id'));
			    return "<div class='controlBtn'>" + resultStr + "</div>";
			  }.createDelegate(this)
			}
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
				editable: false,
				value: new Date(),
	            id: 'endDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
				editable: false,
                id: 'endTimeField',
                value: '18:00',
                increment : 1
        }),'-',
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
		    	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
		    	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		    	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
		    	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		    	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
		    	        	endTime= tmpenddate+' '+tmpendtime+':00';
		    	        	
		        			gpsDeviceid = getDeviceId();
		        			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime}});
		        			
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
	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
	        	endTime= tmpenddate+' '+tmpendtime+':00';
	        	
    			gpsDeviceid = getDeviceId();
    			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime}});
    			
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
	    	 win_show();
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

grid.on('cellclick', function (grid, rowIndex, columnIndex, e) {   
  var btn = e.getTarget('.controlBtn');
  if (btn) {   
    var t = e.getTarget();   
    var record = grid.getStore().getAt(rowIndex);   
    var control = t.className;   
    switch (control) {   
    case 'showgriddetail':
      this.showgriddetail(record);
      break;
    }
  }   
},   
this);  



    cardpanel = new Ext.Panel({
		layout: 'card',
		activeItem : 0,
		region: 'center',
		items: [grid, griddetail]
	});

	var view = new Ext.Viewport({
		layout: 'border',
		items: [ cardpanel ]
	});
	
    //var tmpdate = (new Date()).pattern("yyyy-M-d");
    //store.load({params:{start:0, limit:14, startTime: tmpdate+' '+'08:00:00', endTime: tmpdate+' '+'18:00:00'}});
	var formPanel;
	
	function win_show(){
		var d = new Date();
		d.setDate(d.getDate() - 1);
		var dd = Ext.getCmp('startDateField').getValue();
		//var dd = new Date();
		//dd.setDate(dd.getDate() - 4);
		
		formPanel=new Ext.form.FormPanel({
			//autoWidth: true,
			//autoScroll:true,
			//autoHeight:true,
			labelWidth: 80,
	        items: [{
            	xtype: 'datefield',
                fieldLabel: '开始时间',
                width: 130,
                name: 'start_time_',
                id: 'start_time_',
                altFormats: 'Y-m-d',
                editable: false,
                format: 'Y-m-d',
                //value: dd,
                maxValue :d,
                blankText: '请选择开始时间',
                emptyText: '请选择开始时间 ...'
            },{
            	xtype: 'datefield',
                fieldLabel: '结束时间',
                width: 130,
                name: 'expire_time_',
                id: 'expire_time_',
                altFormats: 'Y-m-d',
                editable: false,
                format: 'Y-m-d',
                //value: d,
                maxValue :d,
                blankText: '请选择结束时间',
                emptyText: '请选择结束时间...'
            }]
		});
		// ----window表单----
		
		var win = new Ext.Window({
			title: '报表导出',
			closable:true,
			width: 250,
			modal :true,
			autoHeight: true,
			items:[formPanel],
	        buttons: [{
	            text: '导出',
				handler:function(){
	        		var start_time_ = Ext.getCmp('start_time_').getValue();
	        		var expire_time_ = Ext.getCmp('expire_time_').getValue();
	        		
	        		var day = (expire_time_.getTime()/1000 - start_time_.getTime()/1000)/60/60/24;
	        		if(day >= 0 && day <= 30){
	        			Ext.Msg.show({
					    	msg: '正在导出 请稍等...',
					        progressText: '导出中...',
					        width:300,
					        wait:true,
					        icon:'ext-mb-download'
					    });
	        			var startTime_ = start_time_.format('Y-m-d')+' 00:00:00';
	        			var endTime_ = expire_time_.format('Y-m-d')+' 23:59:59';
	        			gpsDeviceid = getDeviceId();
		    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime_+'&endTime='+endTime_+'&deviceIds='+gpsDeviceid;
		    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReport&'+tmpparam;
		    			document.excelform.submit();
						setTimeout(function(){Ext.MessageBox.hide();},3000);
	        		}else{
	        			Ext.Msg.alert('提示','导出报表的日期不能超过30天!');
	        		}
	        	}
	        },{
	            text: '取消',
				handler:function(){win.close();}
	        }]
		});
		win.show();
	}
	
});
