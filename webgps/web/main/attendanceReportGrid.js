/**
 * 详细日志报表
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var attendanceGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var attendanceGpsDeviceids = '';
var attendanceDeviceId = '';
var attendanceSearchValue = '';
var attendanceStartTime = tmpdate+' '+'08:00:00';
var attendanceEndTime = tmpdate+' '+'18:00:00';
var attendanceOpenTime = '';
var attendanceCloseTime = '';
var attendanceWeek = '';
var attendanceStartTime = '';
var attendanceEndTime = '';


function ShowAttendanceDetailGridF(record){
	var tmpv = record.get('vehicleNumber');
	attendanceWeek = record.get('week');
	var tmpStartTime = record.get('startTime');
	var tmpEndTime = record.get('endTime');
	attendanceDeviceId = record.get('deviceId');
	attendanceOpenTime = record.get('openTime');
	attendanceCloseTime = record.get('closeTime');
	var tmpArr = attendanceOpenTime.split(" ");
	attendanceOpenTime = tmpArr[0]+" "+tmpStartTime+':00';
	tmpArr = attendanceCloseTime.split(" ");
	attendanceCloseTime = tmpArr[0]+" "+tmpEndTime+':00';
	
	Ext.getCmp('AttendanceReportPanel').layout.setActiveItem(1);
	Ext.getCmp('AttendanceDetailGridTitle').setText('('+tmpv+')的详细日志统计报表:');
	storeLoad(Ext.getCmp('AttendanceDetailGrid').getStore(), 0, 15, '', attendanceSearchValue, attendanceOpenTime, attendanceCloseTime, '', false, attendanceDeviceId, '', attendanceWeek);
}

function initAttendanceGrid(){
	var tmpstartdate = Ext.getCmp('attendancesdf').getValue().format('Y-m-d');
	//var tmpstarttime = Ext.getCmp('attendancestf').getValue();
	var tmpenddate = Ext.getCmp('attendanceedf').getValue().format('Y-m-d');
	//var tmpendtime = Ext.getCmp('attendanceetf').getValue();
	attendanceSearchValue = Ext.getCmp('attendancedif').getValue();
	attendanceStartTime = tmpstartdate+' 00:00:00';
	attendanceEndTime = tmpenddate+' 23:59:59';
    attendanceGpsDeviceids = getDeviceId();
}

function searchAttendanceGrid(){
	storeLoad(AttendanceStore, 0, 15, attendanceGpsDeviceids, attendanceSearchValue, attendanceStartTime, attendanceEndTime, 0, false);
}

var AttendanceStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/visitStat.do?method=listAttendanceReport'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'week'},{name: 'startTime'},{name: 'endTime'},{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'groupName'},{name: 'simcard'},{name: 'openTime'},{name: 'closeTime'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(attendanceSearchValue), startTime: attendanceStartTime, endTime: attendanceEndTime, deviceIds: attendanceGpsDeviceids
		    	};
			}
		}
	}
});

var AttendanceGrid = {
	xtype: 'grid',
	id: 'AttendanceGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: AttendanceStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: attendanceStat.salesmanName, width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: attendanceStat.department, width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: attendanceStat.mobile, width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: main.boot_time, width: 130, sortable: true,  dataIndex: 'openTime', renderer: function (value, meta, record) { 
			if(value == ''){return '<font color="#FF0000">'+attendanceStat.noValidData+'<font>';}else{return value;}
		}},
		{header: main.off_time, width: 130, sortable: true,  dataIndex: 'closeTime', renderer: function (value, meta, record) { 
			if(value == ''){return '<font color="#FF0000">'+attendanceStat.noValidData+'<font>';}else{return value;}
		}},
		{id: 'process',header: main.operating, width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			if(record.data.openTime == ''){return main.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowAttendanceDetailGrid'>"+main.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('id'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'AttendanceGridId',
	autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'attendancesdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			//{id: 'attendancestf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'attendanceedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			//{id: 'attendanceetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.key_word},
			{id: 'attendancedif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initAttendanceGrid();searchAttendanceGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initAttendanceGrid();
				searchAttendanceGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				win_show();
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
					case 'ShowAttendanceDetailGrid':
						ShowAttendanceDetailGridF(record);
						break;
				}
			}
		}
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: AttendanceStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

var AttendanceDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/stat/visitStat.do?method=listAttendanceReportDetail'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'simcard'},{name: 'gpsTime'},{name: 'inputTime'},{name: 'pd'},{name: 'visitId'},{name: 'visitName'},{name: 'imsi'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: attendanceOpenTime, endTime: attendanceCloseTime, deviceId : attendanceDeviceId
				};
			}
		}
	}
});
var AttendanceDetailGrid = {
	xtype: 'grid',
	id: 'AttendanceDetailGrid',
	store: AttendanceDetailStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: main.time, width: 130, sortable: true,  dataIndex: 'gpsTime'},
		{header: '接收时间', width: 130, sortable: true,  dataIndex: 'inputTime',hidden: addendaceReportInputTime},
		{id: 'pd' , header: main.position_description, width: 350, sortable: true,  dataIndex: 'pd',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: main.visiting_customers, width: 130, sortable: true,  dataIndex: 'visitName',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '手机号序列号', width: 130, sortable: true,  dataIndex: 'imsi'}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'AttendanceDetailGridId',
	autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'AttendanceDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('AttendanceReportPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 14,
        store: AttendanceDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};

function win_show(){
	var d = new Date();
	d.setDate(d.getDate() - 1);
	var dd = Ext.getCmp('attendancesdf').getValue();
	//var dd = new Date();
	//dd.setDate(dd.getDate() - 4);
	formPanel=new Ext.form.FormPanel({
		//autoWidth: true,
		//autoScroll:true,
		//autoHeight:true,
		labelWidth: 80,
	    items: [{
            xtype: 'datefield',
            fieldLabel: main.start_time,
            width: 130,
            name: 'start_time_',
            id: 'start_time_',
            altFormats: 'Y-m-d',
            editable: false,
            format: 'Y-m-d',
            //value: dd,
            maxValue :d,
            blankText: main.please_select_start_time,
            emptyText: main.please_select_start_time+'...'
        },{
            xtype: 'datefield',
            fieldLabel: main.end_time,
            width: 130,
            name: 'expire_time_',
            id: 'expire_time_',
            altFormats: 'Y-m-d',
            editable: false,
            format: 'Y-m-d',
            //value: d,
            maxValue :d,
            blankText: main.please_select_end_time,
            emptyText: main.please_select_end_time+'...'
        }]
	});
	// ----window表单----
	var win = new Ext.Window({
		title: main.export_report,
		closable:true,
		width: 250,
		modal :true,
		autoHeight: true,
		items:[formPanel],
	    buttons: [{
	        text: main.export_,
			handler:function(){
				/*if(Ext.getCmp('AttendanceDetailGrid').getStore().getCount()<=0){
					return;
				}*/
	        	var start_time_ = Ext.getCmp('start_time_').getValue();
	        	var expire_time_ = Ext.getCmp('expire_time_').getValue();
				if(!start_time_){Ext.Msg.alert(main.tips, main.please_select_start_time);return;}
				if(!expire_time_){Ext.Msg.alert(main.tips, main.please_select_end_time);return;}
	        	var day = (expire_time_.getTime()/1000 - start_time_.getTime()/1000)/60/60/24;
	        	if(day >= 0 && day <= 30){
	        		/*Ext.Msg.show({
					    msg: '正在导出 请稍等...',
					    progressText: '导出中...',
					    width:300,
					    wait:true,
					    icon:'ext-mb-download'
					});
	        		var startTime_ = start_time_.format('Y-m-d')+' 00:00:00';
	        		var endTime_ = expire_time_.format('Y-m-d')+' 23:59:59';
	        		//attendanceGpsDeviceids = getDeviceId();
		    		var tmpparam = 'expExcel=true&searchValue='+encodeURI(attendanceSearchValue)+'&startTime='+attendanceStartTime+'&endTime='+attendanceEndTime+'&deviceIds='+attendanceGpsDeviceids;
    				document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReport&'+tmpparam;
		    		document.excelform.submit();
					setTimeout(function(){Ext.MessageBox.hide()},3000);
*/
					var startTime_ = start_time_.format('Y-m-d')+' 00:00:00';
					var endTime_ = expire_time_.format('Y-m-d')+' 23:59:59';
					attendanceGpsDeviceids = getDeviceId();
					
					var excelhtml = excelpath+'/stat/visitStat.do?method=listAttendanceReport&expExcel=true&searchValue='+encodeURI(encodeURI(attendanceSearchValue))+'&startTime='+startTime_+'&endTime='+endTime_+'&deviceIds='+attendanceGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
						//url: 'http://www.sosgps.com.cn/stat/visitStat.do?method=listAttendanceReport&expExcel=true&searchValue='+encodeURI(encodeURI(attendanceSearchValue))+'&startTime='+startTime_+'&endTime='+endTime_+'&deviceIds='+attendanceGpsDeviceids,
						url: path+'/stat/visitStat.do?method=listAttendanceReport&expExcel=true&searchValue='+encodeURI(encodeURI(attendanceSearchValue))+'&startTime='+startTime_+'&endTime='+endTime_+'&deviceIds='+attendanceGpsDeviceids,
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

	        	}else if(day < 0){
					Ext.Msg.alert(main.tips, '开始时间不能大于结束时间!');
				}else{
	        		Ext.Msg.alert(main.tips, '导出报表的日期不能超过30天!');
	        	}
	        }
	    },{
	        text: main.cancel,
			handler:function(){win.close();}
	    }]
	});
	win.show();
}
