/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var cusVisitStatGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var cusVisitStatGpsDeviceids = '';
var cusVisitStatDeviceId = '';
var cusVisitStatSearchValue = '';
var cusVisitStatDuration = 15;
var cusVisitStatStartTime = tmpdate+' '+'08:00:00';
var cusVisitStatEndTime = tmpdate+' '+'18:00:00';
var cusVisitStatPoiId = '';
function ShowCusVisitStatDetailGridF(record){
	var tmpv = record.get('visitName');
	cusVisitStatPoiId = record.get('id');
	Ext.getCmp('CusVisitStatPanel').layout.setActiveItem(1);
	Ext.getCmp('CusVisitStatDetailGridTitle').setText('客户('+tmpv+')的拜访统计报表:');
	storeLoad(Ext.getCmp('CusVisitStatDetailGrid').getStore(), 0, 15, cusVisitStatGpsDeviceids, cusVisitStatSearchValue, cusVisitStatStartTime, cusVisitStatEndTime, cusVisitStatDuration, false, '',cusVisitStatPoiId);
}

function initCusVisitStatGrid(){
	var tmpstartdate = Ext.getCmp('cusVisitStatsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('cusVisitStatstf').getValue();
	var tmpenddate = Ext.getCmp('cusVisitStatedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('cusVisitStatetf').getValue();
	cusVisitStatSearchValue = Ext.getCmp('cusVisitStatdif').getValue();
	cusVisitStatDuration =  Ext.getCmp('cusVisitStatDuration').getValue();
	if(cusVisitStatDuration == ''){
		cusVisitStatDuration = 0;
	}
	cusVisitStatStartTime = tmpstartdate+' '+tmpstarttime+':00';
	cusVisitStatEndTime = tmpenddate+' '+tmpendtime+':00';
    cusVisitStatGpsDeviceids = getDeviceId();
}
//业务员出访统计查询
function searchCusVisitStatGrid(){
	storeLoad(CusVisitStatStore, 0, 15, cusVisitStatGpsDeviceids, cusVisitStatSearchValue, cusVisitStatStartTime, cusVisitStatEndTime, cusVisitStatDuration, false);
}
//业务员出访统计表
var CusVisitStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/visitStat.do?method=listCustomVisitCountTj'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'visitName'},{name: 'visitCount'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(cusVisitStatSearchValue), startTime: cusVisitStatStartTime, endTime: cusVisitStatEndTime, deviceIds: cusVisitStatGpsDeviceids, duration: cusVisitStatDuration
		    	};
			}
		}
	}
});
//业务员出访统计表
var CusVisitStatGrid = {
	xtype: 'grid',
	id: 'CusVisitStatGrid',
	store: CusVisitStatStore,
	loadMask: {msg: main.loading},
	columns: [
		{id: 'visitName',header: cusVisitStat.visitName_, width: 130, sortable: false,  dataIndex: 'visitName',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: cusVisitStat.visitCount, width: 130, sortable: false,  dataIndex: 'visitCount'},
		{id: 'process',header: main.operating, width: 100, sortable: false,  dataIndex: 'process' , renderer: function (value, meta, record) { 
			if(record.data.visitCount == 0 ){return main.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowCusVisitStatDetailGrid'>"+main.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('id'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'CusVisitStatGridId',
	autoExpandColumn: 'visitName',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'cusVisitStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'cusVisitStatstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'cusVisitStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'cusVisitStatetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.retention_period+'>=' },
			{id: 'cusVisitStatDuration',xtype: 'textfield', width: 30, enableKeyEvents: true, value: '15', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initCusVisitStatGrid();
				searchCusVisitStatGrid();
			}}}},
			{xtype: 'label', text: main.minutes}, '-',
			{xtype: 'label', text: main.key_word},
			{id: 'cusVisitStatdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initCusVisitStatGrid();searchCusVisitStatGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initCusVisitStatGrid();
				searchCusVisitStatGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				initCusVisitStatGrid();
				var excelhtml = excelpath+'/stat/visitStat.do?method=listCustomVisitCountTj&expExcel=true&searchValue='+encodeURI(encodeURI(cusVisitStatSearchValue))+'&startTime='+cusVisitStatStartTime+'&endTime='+cusVisitStatEndTime+'&deviceIds='+cusVisitStatGpsDeviceids+'&duration='+cusVisitStatDuration+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
				/*if(Ext.getCmp('CusVisitStatGrid').getStore().getCount()<=0){
					return;
				}*/
				/*Ext.Msg.show({
			    	msg: '正在导出 请稍等...',
			        progressText: '导出中...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(encodeURI(cusVisitStatSearchValue))+'&startTime='+cusVisitStatStartTime+'&endTime='+cusVisitStatEndTime+'&deviceIds='+cusVisitStatGpsDeviceids+'&duration='+cusVisitStatDuration;
    			document.excelform.action = path+'/stat/visitStat.do?method=listCustomVisitCountTj&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);*/
				/*var tmpstartdate = Ext.getCmp('cusVisitStatsdf').getValue().format('Y-m-d');
				var tmpstarttime = Ext.getCmp('cusVisitStatstf').getValue();
				var tmpenddate = Ext.getCmp('cusVisitStatedf').getValue().format('Y-m-d');
				var tmpendtime = Ext.getCmp('cusVisitStatetf').getValue();
				cusVisitStatSearchValue = Ext.getCmp('cusVisitStatdif').getValue();
				cusVisitStatDuration =  Ext.getCmp('cusVisitStatDuration').getValue();
				if(cusVisitStatDuration == ''){
					cusVisitStatDuration = 0;
				}
				cusVisitStatStartTime = tmpstartdate+' '+tmpstarttime+':00';
				cusVisitStatEndTime = tmpenddate+' '+tmpendtime+':00';*/
				/*Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/visitStat.do?method=listCustomVisitCountTj&expExcel=true&searchValue='+encodeURI(encodeURI(cusVisitStatSearchValue))+'&startTime='+cusVisitStatStartTime+'&endTime='+cusVisitStatEndTime+'&deviceIds='+cusVisitStatGpsDeviceids+'&duration='+cusVisitStatDuration,
					//url: path+'/stat/visitStat.do?method=listCustomVisitCountTj',
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
					case 'ShowCusVisitStatDetailGrid':
						ShowCusVisitStatDetailGridF(record);
						break;
				}
			}
		}
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: CusVisitStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
//业务员出访详细表
var CusVisitStatDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/stat/visitStat.do?method=listCustomVisitCountTjByCustom'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'id'},{name: 'vehicleNumber'},{name: 'simcard'},{name: 'visitId'},{name: 'visitName'},{name: 'arriveTime'},{name: 'leaveTime'},{name: 'stayTime'},{name: 'pd'},{name: 'tjDate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: cusVisitStatStartTime, endTime: cusVisitStatEndTime, poiId: cusVisitStatPoiId, deviceIds : cusVisitStatGpsDeviceids, duration: cusVisitStatDuration
				};
			}
		}
	}
});
var CusVisitStatDetailGrid = {
	xtype: 'grid',
	id: 'CusVisitStatDetailGrid',
	store: CusVisitStatDetailStore,
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
		{id: 'pd',header: main.position_description, width: 300, sortable: true, dataIndex: 'pd',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'CusVisitStatDetailGridId',
	autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'CusVisitStatDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('CusVisitStatPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: CusVisitStatDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};
