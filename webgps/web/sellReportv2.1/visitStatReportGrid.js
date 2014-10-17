/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var visitStatGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var visitStatReportGpsDeviceids = '';
var visitStatReportDeviceId = '';
var visitStatReportSearchValue = '';
var visitStatReportDuration = 15;
var visitStatReportStartTime = tmpdate+' '+'08:00:00';
var visitStatReportEndTime = tmpdate+' '+'18:00:00';
var visitStatReportUtcStartTime = new Date().getTime()/1000;
var visitStatReportUtcEndTime = new Date().getTime()/1000;

function ShowVisitStatReportDetailGridF(record){
	var tmpv = record.get('vehicleNumber');
	visitStatReportDeviceId = record.get('deviceId');
	Ext.getCmp('VisitStatReportPanel').layout.setActiveItem(1);
	Ext.getCmp('VisitStatReportDetailGridTitle').setText('('+tmpv+')的拜访统计报表:');
	storeLoad(Ext.getCmp('VisitStatReportDetailGrid').getStore(), 0, 15, '', visitStatReportSearchValue, visitStatReportStartTime, visitStatReportEndTime, visitStatReportDuration, false, visitStatReportDeviceId);
}

//业务员出访统计查询
function initVisitStatReportGrid(){
	var tmpstartdate = Ext.getCmp('visitStatReportsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('visitStatReportstf').getValue();
	var tmpenddate = Ext.getCmp('visitStatReportedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('visitStatReportetf').getValue();
	visitStatReportSearchValue = Ext.getCmp('visitStatReportdif').getValue();
	visitStatReportDuration =  Ext.getCmp('visitStatReportDuration').getValue();
	if(visitStatReportDuration == ''){
		visitStatReportDuration = 0;
	}
	visitStatReportStartTime = tmpstartdate+' '+tmpstarttime+':00';
	visitStatReportEndTime = tmpenddate+' '+tmpendtime+':00';
	var tmpUtcStartTime = Ext.getCmp('visitStatReportsdf').getValue();
	var tmpUtcEndTime = Ext.getCmp('visitStatReportedf').getValue();
	visitStatReportUtcStartTime = tmpUtcStartTime.getTime() / 1000;
	visitStatReportUtcEndTime = tmpUtcEndTime.getTime() / 1000;
	//visitStatReportUtcStartTime = parseInt(Ext.getCmp('visitStatReportsdf').getValue().getTime()/100O);
	//visitStatReportUtcEndTime = parseInt(Ext.getCmp('visitStatReportedf').getValue().getTime()/100O);
    visitStatReportGpsDeviceids = getDeviceId();
	
}

function searchVisitStatReportGrid(){
	storeLoad(VisitStatReportStore, 0, 65535, visitStatReportGpsDeviceids, visitStatReportSearchValue, visitStatReportStartTime, visitStatReportEndTime, visitStatReportDuration, false);
}

//业务员出访统计表
var VisitStatReportStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/visit/visit.do?method=listVisitCountTjSql'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'visitCount'},{name: 'visitCusCount'},{name: 'visitPlaceCount'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(visitStatReportSearchValue), startTime: visitStatReportStartTime, endTime: visitStatReportEndTime, deviceIds: visitStatReportGpsDeviceids, duration: visitStatReportDuration, entCode: entCode ,userId: userId
		    	};
			}
		}
	}
});
//业务员出访统计表
var VisitStatReportGrid = {
	xtype: 'grid',
	id: 'VisitStatReportGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: VisitStatReportStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: visitStat.vehicleNumber, width: 130, sortable: false,  dataIndex: 'vehicleNumber'},
		{header: visitStat.visitCount, width: 130, sortable: false,  dataIndex: 'visitCount'},
		{header: visitStat.visitCusCount, width: 130, sortable: false,  dataIndex: 'visitCusCount'},
		{id: 'process',header: main.operating, width: 100, sortable: false,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			if(record.data.visitCount == 0 && record.data.visitCusCount == 0 && record.data.visitPlaceCount == 0){return visitStat.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowVisitStatReportDetailGrid'>"+visitStat.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('deviceId'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitStatReportGridId',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'visitStatReportsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'visitStatReportstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'visitStatReportedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'visitStatReportetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.retention_period+'>=' },
			{id: 'visitStatReportDuration',xtype: 'textfield', width: 30, enableKeyEvents: true, value: '15', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatReportGrid();
				searchVisitStatReportGrid();
			}}}},
			{xtype: 'label', text: main.minutes}, '-',
			{xtype: 'label', text: main.key_word},
			{id: 'visitStatReportdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initVisitStatReportGrid();searchVisitStatReportGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initVisitStatReportGrid();
				searchVisitStatReportGrid();
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initVisitStatReportGrid();
					document.excelform.action = encodeURI( path+'/visit/visit.do?method=listVisitCountTjSql&expExcel=true&searchValue='
							+visitStatSearchValue+'&start=0&limit=65536'+'&startTime='+visitStatReportStartTime+'&endTime='
							+visitStatReportEndTime+'&deviceIds='+visitStatReportGpsDeviceids+'&duration='+visitStatReportDuration);
					document.excelform.submit();
				}
				
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
					case 'ShowVisitStatReportDetailGrid':
						ShowVisitStatReportDetailGridF(record);
						break;
				}
			}
		}
	}/*,
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VisitStatReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})*/
};
//业务员出访详细表
var VisitStatReportDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/visit/visit.do?method=listVisitCountTjByCustom'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'id'},{name: 'visitName'},{name: 'arriveTime'},{name: 'leaveTime'},{name: 'stayTime'},{name: 'signInDesc'},{name: 'signOutDesc'},{name: 'tjDate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: visitStatReportStartTime, endTime: visitStatReportEndTime, deviceId : visitStatReportDeviceId, duration: visitStatReportDuration
				};
			}
		}
	}
});
var VisitStatReportDetailGrid = {
	xtype: 'grid',
	id: 'VisitStatReportDetailGrid',
	store: VisitStatReportDetailStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: visitStat.visitName, width: 130, sortable: true,  dataIndex: 'visitName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: visitStat.arriveTime, width: 130, sortable: true,  dataIndex: 'arriveTime'},
		{header: visitStat.leaveTime, width: 130, sortable: true,  dataIndex: 'leaveTime'},
		{header: main.retention_period+'('+main.minutes+')', width: 80, sortable: true,  dataIndex: 'stayTime'},
		//{header: '统计时间', width: 130, sortable: true,  dataIndex: 'tjDate', hidden:true},
		{header: '签到位置描述', width: 300, sortable: true, dataIndex: 'signInDesc',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: '签退位置描述', width: 300, sortable: true, dataIndex: 'signOutDesc',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitStatReportDetailGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'VisitStatReportDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitStatReportPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: VisitStatReportDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};

