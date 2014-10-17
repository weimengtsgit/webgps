/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var cusVisitReportGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var cusVisitReportGpsDeviceids = '';
var cusVisitReportDeviceId = '';
var cusVisitReportSearchValue = '';
var cusVisitReportDuration = 15;
var cusVisitReportStartTime = tmpdate+' '+'08:00:00';
var cusVisitReportEndTime = tmpdate+' '+'18:00:00';
var cusVisitReportPoiId = '';
function ShowCusVisitReportDetailGridF(record){
	var tmpv = record.get('visitName');
	cusVisitReportPoiId = record.get('id');
	Ext.getCmp('CusVisitReportPanel').layout.setActiveItem(1);
	Ext.getCmp('CusVisitReportDetailGridTitle').setText('客户('+tmpv+')的拜访统计报表:');
	storeLoad(Ext.getCmp('CusVisitReportDetailGrid').getStore(), 0, 15, cusVisitReportGpsDeviceids, cusVisitReportSearchValue, cusVisitReportStartTime, cusVisitReportEndTime, cusVisitReportDuration, false, '',cusVisitReportPoiId);
}

function initCusVisitReportGrid(){
	var tmpstartdate = Ext.getCmp('cusVisitReportsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('cusVisitReportstf').getValue();
	var tmpenddate = Ext.getCmp('cusVisitReportedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('cusVisitReportetf').getValue();
	cusVisitReportSearchValue = Ext.getCmp('cusVisitReportdif').getValue();
	cusVisitReportDuration =  Ext.getCmp('cusVisitReportDuration').getValue();
	if(cusVisitReportDuration == ''){
		cusVisitReportDuration = 0;
	}
	cusVisitReportStartTime = tmpstartdate+' '+tmpstarttime+':00';
	cusVisitReportEndTime = tmpenddate+' '+tmpendtime+':00';
    cusVisitReportGpsDeviceids = getDeviceId();
}
//业务员出访统计查询
function searchCusVisitReportGrid(){
	storeLoad(CusVisitReportStore, 0, 15, cusVisitReportGpsDeviceids, cusVisitReportSearchValue, cusVisitReportStartTime, cusVisitReportEndTime, cusVisitReportDuration, false);
}
//业务员出访统计表
var CusVisitReportStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/visit/visit.do?method=listCustomVisitCountTj'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'visitName'},{name: 'visitCount'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(cusVisitReportSearchValue), startTime: cusVisitReportStartTime, endTime: cusVisitReportEndTime, deviceIds: cusVisitReportGpsDeviceids, duration: cusVisitReportDuration
		    	};
			}
		}
	}
});
//业务员出访统计表
var CusVisitReportGrid = {
	xtype: 'grid',
	id: 'CusVisitReportGrid',
	store: CusVisitReportStore,
	loadMask: {msg: main.loading},
	columns: [
		{id: 'visitName',header: cusVisitStat.visitName_, width: 200, sortable: false,  dataIndex: 'visitName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: cusVisitStat.visitCount, width: 130, sortable: false,  dataIndex: 'visitCount'},
		{id: 'process',header: main.operating, width: 100, sortable: false,  dataIndex: 'process' , renderer: function (value, meta, record) { 
			if(record.data.visitCount == 0 ){return main.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowCusVisitReportDetailGrid'>"+main.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('id'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'CusVisitReportGridId',
	//autoExpandColumn: 'visitName',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'cusVisitReportsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'cusVisitReportstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'cusVisitReportedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'cusVisitReportetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.retention_period+'>=' },
			{id: 'cusVisitReportDuration',xtype: 'textfield', width: 30, enableKeyEvents: true, value: '15', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initCusVisitReportGrid();
				searchCusVisitReportGrid();
			}}}},
			{xtype: 'label', text: main.minutes}, '-',
			{xtype: 'label', text: main.key_word},
			{id: 'cusVisitReportdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initCusVisitReportGrid();searchCusVisitReportGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initCusVisitReportGrid();
				searchCusVisitReportGrid();
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initCusVisitReportGrid();
					document.excelform.action = encodeURI( path+'/visit/visit.do?method=listCustomVisitCountTj&expExcel=true&searchValue='
							+cusVisitReportSearchValue+'&start=0&limit=65536'+'&startTime='+cusVisitReportStartTime+'&endTime='
							+cusVisitReportEndTime+'&deviceIds='+cusVisitReportGpsDeviceids+'&duration='+cusVisitReportDuration);
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
					case 'ShowCusVisitReportDetailGrid':
						ShowCusVisitReportDetailGridF(record);
						break;
				}
			}
		}
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: CusVisitReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
//业务员出访详细表
var CusVisitReportDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/visit/visit.do?method=listCustomVisitCountTjByCustom'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'id'},{name: 'vehicleNumber'},{name: 'visitName'},{name: 'arriveTime'},{name: 'leaveTime'},{name: 'stayTime'},{name: 'signInDesc'},{name: 'signOutDesc'},{name: 'tjDate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: cusVisitReportStartTime, endTime: cusVisitReportEndTime, poiId: cusVisitReportPoiId, deviceIds : cusVisitReportGpsDeviceids, duration: cusVisitReportDuration
				};
			}
		}
	}
});
var CusVisitReportDetailGrid = {
	xtype: 'grid',
	id: 'CusVisitReportDetailGrid',
	store: CusVisitReportDetailStore,
	loadMask: {msg: main.loading},
	columns: [
        //{header: '业务员手机号码', width: 130, sortable: true,  dataIndex: 'simcard', hidden:true},
		{header: cusVisitStat.visit_name, width: 130, sortable: true,  dataIndex: 'visitName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: cusVisitStat.vehicle_number, width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: cusVisitStat.arrive_time, width: 130, sortable: true,  dataIndex: 'arriveTime'},
		{header: cusVisitStat.leave_time, width: 130, sortable: true,  dataIndex: 'leaveTime'},
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
	stateId: 'CusVisitReportDetailGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'CusVisitReportDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('CusVisitReportPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: CusVisitReportDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};
