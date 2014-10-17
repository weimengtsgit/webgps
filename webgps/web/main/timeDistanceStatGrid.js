var timeDistanceStatGpsDeviceids = '';
var timeDistanceStatDeviceId = '';
var timeDistanceStatSearchValue = '';
var timeDistanceStatStartTime = tmpdate+' '+'00:00:00';
var timeDistanceStatEndTime = tmpdate+' '+'23:59:59';

function initTimeDistanceStatGrid(){
	var tmpstartdate = Ext.getCmp('timeDistanceStatsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('timeDistanceStatstf').getValue();
	var tmpenddate = Ext.getCmp('timeDistanceStatedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('timeDistanceStatetf').getValue();
	timeDistanceStatSearchValue = Ext.getCmp('timeDistanceStatdif').getValue();
	timeDistanceStatStartTime = tmpstartdate+' '+tmpstarttime+':00';
	timeDistanceStatEndTime = tmpenddate+' '+tmpendtime+':00';
    timeDistanceStatGpsDeviceids = getDeviceId();
}

function searchTimeDistanceStatGrid(){
	storeLoad(TimeDistanceStatStore, 0, 15, timeDistanceStatGpsDeviceids, timeDistanceStatSearchValue, timeDistanceStatStartTime, timeDistanceStatEndTime, 0, false);
}

var TimeDistanceStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/distanceStat.do?method=listTimeDistanceStatByCustom'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'vehicleNumber'},{name: 'distance'},{name: 'starttime'},{name: 'endtime'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: timeDistanceStatStartTime, endTime: timeDistanceStatEndTime, deviceIds: timeDistanceStatGpsDeviceids
		    	};
			}
		}
	}
});

var TimeDistanceStatGrid = {
	xtype: 'grid',
	id: 'TimeDistanceStatGrid',
	store: TimeDistanceStatStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
        {header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
        {header: '起始时间', width: 130, sortable: true,  dataIndex: 'starttime'},
        {header: '结束时间', width: 130, sortable: true,  dataIndex: 'endtime'},
        {header: '里程', width: 130, sortable: true,  dataIndex: 'distance',align: 'right'}
    ],
	stripeRows: true,
	stateful: true,
	stateId: 'TimeDistanceStatGridId',
	//autoExpandColumn: 'distance',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'timeDistanceStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'timeDistanceStatstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'timeDistanceStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'timeDistanceStatetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.key_word},
			{id: 'timeDistanceStatdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initTimeDistanceStatGrid();searchTimeDistanceStatGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initTimeDistanceStatGrid();searchTimeDistanceStatGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				
				initTimeDistanceStatGrid();
				var excelhtml = excelpath+'/stat/distanceStat.do?method=listTimeDistanceStatByCustom&expExcel=true&searchValue='+encodeURI(encodeURI(timeDistanceStatSearchValue))+'&startTime='+timeDistanceStatStartTime+'&endTime='+timeDistanceStatEndTime+'&deviceIds='+timeDistanceStatGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
		store: TimeDistanceStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
