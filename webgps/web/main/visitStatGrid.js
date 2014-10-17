/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var visitStatGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var visitStatGpsDeviceids = '';
var visitStatDeviceId = '';
var visitStatSearchValue = '';
var visitStatDuration = 15;
var visitStatStartTime = tmpdate+' '+'08:00:00';
var visitStatEndTime = tmpdate+' '+'18:00:00';
var visitStatUtcStartTime = new Date().getTime();
var visitStatUtcEndTime = new Date().getTime();

function ShowVisitStatDetailGridF(record){
	var tmpv = record.get('vehicleNumber');
	visitStatDeviceId = record.get('deviceId');
	Ext.getCmp('VisitStatPanel').layout.setActiveItem(1);
	Ext.getCmp('VisitStatDetailGridTitle').setText('('+tmpv+')的拜访统计报表:');
	storeLoad(Ext.getCmp('VisitStatDetailGrid').getStore(), 0, 15, '', visitStatSearchValue, visitStatStartTime, visitStatEndTime, visitStatDuration, false, visitStatDeviceId);
}
function ShowVisitStatDetailGridF2(record){
	var tmpv = record.get('vehicleNumber');
	visitStatDeviceId = record.get('deviceId');
	Ext.getCmp('VisitStatPanel').layout.setActiveItem(2);
	Ext.getCmp('VisitStatDetailGridTitle2').setText('('+tmpv+')的拜访客户:');
	storeLoad(Ext.getCmp('VisitStatDetailGrid2').getStore(), 0, 15, '', visitStatSearchValue, visitStatStartTime, visitStatEndTime, visitStatDuration, false, visitStatDeviceId);
}
function ShowVisitChart(method, deviceId, deviceIds, searchValue, vehicleNumber, startTime, endTime, utcStartTime, utcEndTime, duration){
	VisitStatCountChart.load({
		url: path+"/stat/visitStatChart.do?method="+method,
		params: {deviceId: deviceId, deviceIds: deviceIds, searchValue: encodeURI(searchValue), vehicleNumber: encodeURI(vehicleNumber), startTime: startTime, endTime: endTime, utcStartTime: utcStartTime, utcEndTime: utcEndTime, duration: duration}, // or a URL encoded string
		discardUrl: false,
		nocache: false,
		text: "图表生成中,请稍等...",
		timeout: 30,
		scripts: false
	});
	Ext.getCmp('VisitStatPanel').layout.setActiveItem(3);
}
//拜访次数图表
function ShowVisitCountChart(record){
	var tmpv = record.get('deviceId');
	var tmpvehicleNumber = record.get('vehicleNumber');
	ShowVisitChart('visitCountChart', tmpv, '', '', tmpvehicleNumber, visitStatStartTime, visitStatEndTime, visitStatUtcStartTime, visitStatUtcEndTime, visitStatDuration);
}
//拜访客户数图表
function ShowVisitCusCountChart(record){
	var tmpv = record.get('deviceId');
	var tmpvehicleNumber = record.get('vehicleNumber');
	ShowVisitChart('visitCusCountChartSql', tmpv, '', '', tmpvehicleNumber, visitStatStartTime, visitStatEndTime, visitStatUtcStartTime, visitStatUtcEndTime, visitStatDuration);
}
//拜访地点数图表
function ShowVisitPlaceCountChart(record){
	var tmpv = record.get('deviceId');
	var tmpvehicleNumber = record.get('vehicleNumber');
	ShowVisitChart('visitPlaceCountChartSql', tmpv, '', '', tmpvehicleNumber, visitStatStartTime, visitStatEndTime, visitStatUtcStartTime, visitStatUtcEndTime, visitStatDuration);
}

//拜访次数图表,整体图表
function ShowVisitCountChartAll(){
	ShowVisitChart('visitCountChartAll', '', visitStatGpsDeviceids, visitStatSearchValue, '', visitStatStartTime, visitStatEndTime, '', '', visitStatDuration);
}

//拜访客户数图表,整体图表
function ShowVisitCusCountChartAll(){
	ShowVisitChart('visitCusCountChartAll', '', visitStatGpsDeviceids, visitStatSearchValue, '', visitStatStartTime, visitStatEndTime, '', '', visitStatDuration);
}
//拜访地点数图表,整体图表
function ShowVisitPlaceCountChartAll(){
	ShowVisitChart('visitPlaceCountChartAll', '', visitStatGpsDeviceids, visitStatSearchValue, '', visitStatStartTime, visitStatEndTime, '', '', visitStatDuration);
}
//拜访地点数地图
function ShowVisitPlaceCountMap(record){
	var tmpv = record.get('deviceId');
	Ext.Ajax.request({
		 //url :url,
		 url: path+'/stat/visitStatChart.do?method=visitStatPlaceMap2',
		 method :'POST', 
		 params: { deviceId: tmpv, startTime: visitStatStartTime, endTime: visitStatEndTime, duration: visitStatDuration},
		 timeout : 30000,
		 success : function(request) {
			Ext.getCmp('VisitStatPanel').layout.setActiveItem(4);
			var res = request.responseText;
			var locs = Ext.util.JSON.decode(res);
			var lng = 0;
			var lat = 0;
			var radius = 500;
			var zoom = 13;
			var circleArr = [];
			var v_map = document.getElementById('visitStatPlaceMapIfr').contentWindow;
			v_map.addMCircles(locs);
		 },
		 failure : function(request) {
		 }
		});
}
//业务员出访统计查询
function initVisitStatGrid(){
	var tmpstartdate = Ext.getCmp('visitStatsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('visitStatstf').getValue();
	var tmpenddate = Ext.getCmp('visitStatedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('visitStatetf').getValue();
	visitStatSearchValue = Ext.getCmp('visitStatdif').getValue();
	visitStatDuration =  Ext.getCmp('visitStatDuration').getValue();
	if(visitStatDuration == ''){
		visitStatDuration = 0;
	}
	visitStatStartTime = tmpstartdate+' '+tmpstarttime+':00';
	visitStatEndTime = tmpenddate+' '+tmpendtime+':00';
	var tmpUtcStartTime = Ext.getCmp('visitStatsdf').getValue();
	var tmpUtcEndTime = Ext.getCmp('visitStatedf').getValue();
	visitStatUtcStartTime = tmpUtcStartTime.getTime();
	visitStatUtcEndTime = tmpUtcEndTime.getTime();
	//visitStatUtcStartTime = parseInt(Ext.getCmp('visitStatsdf').getValue().getTime()/100O);
	//visitStatUtcEndTime = parseInt(Ext.getCmp('visitStatedf').getValue().getTime()/100O);
    visitStatGpsDeviceids = getDeviceId();
	
}

function searchVisitStatGrid(){
	storeLoad(VisitStatStore, 0, 15, visitStatGpsDeviceids, visitStatSearchValue, visitStatStartTime, visitStatEndTime, visitStatDuration, false);
}

//业务员出访统计表
var VisitStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/visitStat.do?method=listVisitCountTjSql'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'visitCount'},{name: 'visitCusCount'},{name: 'visitPlaceCount'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(visitStatSearchValue), startTime: visitStatStartTime, endTime: visitStatEndTime, deviceIds: visitStatGpsDeviceids, duration: visitStatDuration, entCode: entCode ,userId: userId
		    	};
			}
		}
	}
});
//业务员出访统计表
var VisitStatGrid = {
	xtype: 'grid',
	id: 'VisitStatGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: VisitStatStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: visitStat.vehicleNumber, width: 130, sortable: false,  dataIndex: 'vehicleNumber'},
		{header: visitStat.visitCount, width: 130, sortable: false,  dataIndex: 'visitCount' , renderer: function (value, meta, record) {
			if(value <= 0){return value;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitCountChart'>"+value+"</a>";
			var resultStr = String.format(formatStr, record.get('deviceId'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)},
		{header: visitStat.visitCusCount, width: 130, sortable: false,  dataIndex: 'visitCusCount' , renderer: function (value, meta, record) {   
			if(value <= 0){return value;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitStatDetailGrid2'>"+value+"</a>";
			var resultStr = String.format(formatStr, record.get('deviceId'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)},
		{header: visitStat.visitPlaceCount, width: 130, sortable: false,  dataIndex: 'visitPlaceCount' , renderer: function (value, meta, record) {   
			if(value <= 0){return visitStat.map;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitPlaceCountChart'>"+value+"</a>";
			var resultStr = String.format(formatStr, record.get('deviceId'));
			if(value < 10){
				resultStr += '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp';
			}else if(value >= 10){
				resultStr += '&nbsp&nbsp&nbsp&nbsp&nbsp';
			}else if(value >= 100){
				resultStr += '&nbsp&nbsp&nbsp&nbsp';
			}else if(value >= 1000){
				resultStr += '&nbsp&nbsp&nbsp';
			}
			var formatStr1 = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitPlaceCountMap'>"+visitStat.map+"</a>";
			var resultStr1 = String.format(formatStr1, record.get('deviceId'));
				//return "<div class='controlBtn'>" + resultStr + resultStr1 + "</div>";
			return "<div class='controlBtn'>" + resultStr1 + "</div>";
			}.createDelegate(this)},
		{id: 'process',header: main.operating, width: 100, sortable: false,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			if(record.data.visitCount == 0 && record.data.visitCusCount == 0 && record.data.visitPlaceCount == 0){return visitStat.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitStatDetailGrid'>"+visitStat.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('deviceId'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitStatGridId',
	autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'visitStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'visitStatstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'visitStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'visitStatetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.retention_period+'>=' },
			{id: 'visitStatDuration',xtype: 'textfield', width: 30, enableKeyEvents: true, value: '15', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatGrid();
				searchVisitStatGrid();
			}}}},
			{xtype: 'label', text: main.minutes}, '-',
			{xtype: 'label', text: main.key_word},
			{id: 'visitStatdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initVisitStatGrid();searchVisitStatGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initVisitStatGrid();
				searchVisitStatGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				initVisitStatGrid();
				var excelhtml = excelpath+'/stat/visitStat.do?method=listVisitCountTjSql&expExcel=true&searchValue='+encodeURI(encodeURI(visitStatSearchValue))+'&startTime='+visitStatStartTime+'&endTime='+visitStatEndTime+'&deviceIds='+visitStatGpsDeviceids+'&duration='+visitStatDuration+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
				var win = new Ext.Window({
					title : '下载文件',
					closable : true,
					closeAction : 'close',
					autoWidth : false,
					width : 200,
					heigth : 150,
					items : [new Ext.Panel({
						html : "<a href='"+excelhtml+"' target='_blank' onclick='Ext.Msg.hide()'>点击此链接下载报表</a>",
						frame : true
					})]
				});
				win.show();
				/*Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/visitStat.do?method=listVisitCountTjSql&expExcel=true&searchValue='+encodeURI(encodeURI(visitStatSearchValue))+'&startTime='+visitStatStartTime+'&endTime='+visitStatEndTime+'&deviceIds='+visitStatGpsDeviceids+'&duration='+visitStatDuration+'&entCode='+entCode+'&userId='+userId,
					//url: 'http://222.128.0.178:8083/stat/visitStat.do?method=listVisitCountTjSql&expExcel=true&searchValue='+encodeURI(encodeURI(visitStatSearchValue))+'&startTime='+visitStatStartTime+'&endTime='+visitStatEndTime+'&deviceIds='+visitStatGpsDeviceids+'&duration='+visitStatDuration+'&entCode='+entCode+'&userId='+userId,
					success:function(response, opts){
						var obj = Ext.decode(response.responseText);
						window.location.href = obj.path;
						
						//Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					//form: Ext.fly('excelform'),
					//isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_failure);
					},
					callback:function(response, opts){
						var obj = Ext.decode(response.responseText);
						window.location.href = obj.path;
						//Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					scope: this
				});*/
			}}
		]
	},
	listeners: {
		cellclick: function (grid, rowIndex, columnIndex, e) {
			var btn = e.getTarget('.controlBtn');
			if (btn) {
				var t = e.getTarget();
				var record = grid.getStore().getAt(rowIndex);
				var control = t.className;
				switch (control) {
					case 'ShowVisitStatDetailGrid':
						ShowVisitStatDetailGridF(record);
						break;
					case 'ShowVisitStatDetailGrid2':
						ShowVisitStatDetailGridF2(record);
						break;
					case 'ShowVisitCountChart':
						ShowVisitCountChart(record);
						break;
					case 'ShowVisitCusCountChart':
						ShowVisitCusCountChart(record);
						break;
					case 'ShowVisitPlaceCountChart':
						ShowVisitPlaceCountChart(record);
						break;
					case 'ShowVisitPlaceCountMap':
						ShowVisitPlaceCountMap(record);
						break;
				}
			}
		},
		headerclick : function (grid, columnIndex, e ){
			if(grid.getStore().getCount()<=0){
				return;
			}
			if(columnIndex == 1){
				ShowVisitCountChartAll();
			}else if(columnIndex == 2){
				ShowVisitCusCountChartAll();
			}else if(columnIndex == 3){
				//ShowVisitPlaceCountChartAll();
			}
		}
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VisitStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
//业务员出访详细表
var VisitStatDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/stat/visitStat.do?method=listVisitCountTjByDeviceId'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'id'},{name: 'visitId'},{name: 'visitName'},{name: 'arriveTime'},{name: 'leaveTime'},{name: 'stayTime'},{name: 'pd'},{name: 'tjDate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: visitStatStartTime, endTime: visitStatEndTime, deviceId : visitStatDeviceId, duration: visitStatDuration
				};
			}
		}
	}
});
var VisitStatDetailGrid = {
	xtype: 'grid',
	id: 'VisitStatDetailGrid',
	store: VisitStatDetailStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: visitStat.visitName, width: 130, sortable: true,  dataIndex: 'visitName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: visitStat.arriveTime, width: 130, sortable: true,  dataIndex: 'arriveTime'},
		{header: visitStat.leaveTime, width: 130, sortable: true,  dataIndex: 'leaveTime'},
		{header: visitStat.stayTime, width: 80, sortable: true,  dataIndex: 'stayTime'},
		//{header: '统计时间', width: 130, sortable: true,  dataIndex: 'tjDate', hidden:true},
		{id: 'pd',header: visitStat.pd, width: 300, sortable: true, dataIndex: 'pd',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitStatDetailGridId',
	autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'VisitStatDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitStatPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: VisitStatDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};

//业务员出访详细表
var VisitStatDetailStore2 = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/stat/visitStat.do?method=listVisitCountTjByDeviceId2'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'poiName'},{name: 'pd'},{name: 'poi_count'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: visitStatStartTime, endTime: visitStatEndTime, deviceId : visitStatDeviceId, duration: visitStatDuration
				};
			}
		}
	}
});

var VisitStatDetailGrid2 = {
	xtype: 'grid',
	id: 'VisitStatDetailGrid2',
	store: VisitStatDetailStore2,
	loadMask: {msg: main.loading},
	columns: [
		{header: visitStat.visitName, width: 130, sortable: true,  dataIndex: 'poiName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{id: 'pd',header: visitStat.pd, width: 300, sortable: true, dataIndex: 'pd',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: '被拜访次数', width: 300, sortable: true, dataIndex: 'poi_count'
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitStatDetailGridId2',
	autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'VisitStatDetailGridTitle2',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitStatPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: VisitStatDetailStore2,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};


var VisitStatCountChart = new Ext.Panel({
	id: 'VisitStatCountChart',
	//xtype: 'panel',
    frame:true,
    autoScroll: true,
	//autoLoad:{
	//	url: '../bar/bar3.jsp'
	//},
	//html: '<iframe src="../bar/bar3.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[/*{
			id: 'VisitStatCountChartTitle',
			text: ''
		},'-',*/new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitStatPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		}),new Ext.Action({
			text: '点击右键另存',
			handler: function(){
				/*Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/visitStatChart.do?method=downloadChart',
					success:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					form: Ext.fly('excelform'),
					isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_failure);
					},
					callback:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					scope: this
				});*/
			},
			iconCls: 'icon-search'
		})]
	}
});

var VisitStatPlaceMap = new Ext.Panel({
	id: 'VisitStatPlaceMap',
    //frame:true,
    autoScroll: true,
	html: '<iframe id="visitStatPlaceMapIfr" name="visitStatPlaceMapIfr" src="'+path+'/stat/visitStatPlaceMap.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[/*{
			id: 'VisitStatPlaceMapTitle',
			text: ''
		},'-',*/new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitStatPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	}
});
