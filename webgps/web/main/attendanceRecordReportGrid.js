var attendanceRecordGpsDeviceids = '';
var attendanceRecordDeviceId = '';
var timeAttendanceStatSearchValue = '';
var attendanceRecordStartTime = tmpdate+' '+'00:00:00';
var attendanceRecordEndTime = tmpdate+' '+'23:59:59';

function initAttendanceRecordGrid(){
	var tmpstartdate = Ext.getCmp('attendanceStatsdf').getValue().format('Y-m-d');
	//var tmpstarttime = Ext.getCmp('attendanceStatstf').getValue();
	var tmpenddate = Ext.getCmp('attendanceStatedf').getValue().format('Y-m-d');
	//var tmpendtime = Ext.getCmp('attendanceStatetf').getValue();
	attendanceRecordStartTime = tmpstartdate;//+' '+tmpstarttime+':00';
	attendanceRecordEndTime = tmpenddate;//+' '+tmpendtime+':00';
    attendanceRecordGpsDeviceids = getDeviceId();
}

function searchAttendanceRecordGrid(){
	storeLoad(AttendanceRecordStore, 0, 15, attendanceRecordGpsDeviceids, timeAttendanceStatSearchValue, attendanceRecordStartTime, attendanceRecordEndTime, 0, false);
}

var AttendanceRecordStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/attendanceStat.do?method=listAttendanceRecord'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'groupName'},{name: 'termName'},{name: 'attendanceDate'},{name: 'signinTime'},{name: 'signinDesc'},{name: 'signoffTime'},{name: 'signoffDesc'}]),//,{name: 'createTime'}
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: attendanceRecordStartTime, endTime: attendanceRecordEndTime, deviceIds: attendanceRecordGpsDeviceids
		    	};
			}
		}
	}
});

var AttendanceRecordGrid = {
	xtype: 'grid',
	id: 'AttendanceRecordGrid',
	store: AttendanceRecordStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
		{header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: '名称', width: 130, sortable: true,  dataIndex: 'termName'},
        {header: '考勤日期', width: 130, sortable: true,  dataIndex: 'attendanceDate'},
        {header: '签到时间', width: 130, sortable: true,  dataIndex: 'signinTime'},
        {header: '签到位置', width: 130, sortable: true,  dataIndex: 'signinDesc'},
        {header: '签退时间', width: 130, sortable: true,  dataIndex: 'signoffTime'},
        {header: '签退位置', width: 130, sortable: true,  dataIndex: 'signoffDesc',align:'right'}
//        ,
//        {header: '创建时间', width: 130, sortable: true,  dataIndex: 'createTime',align: 'right'}
    ],
	stripeRows: true,
	stateful: true,
	stateId: 'AttendanceRecordGridId',
	//autoExpandColumn: 'distance',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'attendanceStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'attendanceStatstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'attendanceStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'attendanceStatetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initAttendanceRecordGrid();searchAttendanceRecordGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				
				initAttendanceRecordGrid();
				var excelhtml = path+'/stat/attendanceStat.do?method=listAttendanceRecord&expExcel=true&startTime='+attendanceRecordStartTime+'&endTime='+attendanceRecordEndTime+'&deviceIds='+attendanceRecordGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: AttendanceRecordStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
