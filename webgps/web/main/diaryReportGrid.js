var diaryDeviceids = '';
var diaryStartTime = tmpdate;
var diaryEndTime = tmpdate;
var diaryDeviceid = '';
var diaryDiaryDate = '';

function ShowDiaryDetailGridF(record){
	var tmptermName = record.get('termName');
	var tmpv = record.get('deviceId');
	diaryDeviceid = tmpv;
	var tmdiaryDate = record.get('diaryDate');
	diaryDiaryDate = tmdiaryDate;
	Ext.getCmp('DiaryReportPanel').layout.setActiveItem(1);
	Ext.getCmp('DiaryDetailGridTitle').setText('('+tmptermName+')的日志到达统计报表:');
	storeLoad(Ext.getCmp('DiaryDetailGrid').getStore(), 0, 15, tmdiaryDate, '', '', '', '', false, tmpv, '', '');
}

function searchDiaryGrid(){
	diaryStartTime = Ext.getCmp('diarysdf').getValue().format('Y-m-d');
	diaryEndTime = Ext.getCmp('diaryedf').getValue().format('Y-m-d');
	
    diaryDeviceids = getDeviceId();
	storeLoad(DiaryStore, 0, 15, diaryDeviceids, '', diaryStartTime, diaryEndTime, 0, false);
}

var DiaryStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/diarymarktj/diarymarktj.do?method=listDiaryMarkTj'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, 
			[{name: 'id'},{name: 'tjDate'},{name: 'diaryDate'},
			 {name: 'arrivalRate'},{name: 'deviceId'},{name: 'termName'},
			 {name: 'entCode'},{name: 'groupName'},{name: 'simcard'},{name: 'process'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: diaryStartTime, endTime: diaryEndTime, deviceIds: diaryDeviceids
		    	};
			}
		}
	}
});

var DiaryGrid = {
	xtype: 'grid',
	id: 'DiaryGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: DiaryStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: '名称', width: 130, sortable: true,  dataIndex: 'termName'},
		{header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: '日志日期', width: 130, sortable: true,  dataIndex: 'diaryDate'},
		{header: '到达率', width: 130, sortable: true,  dataIndex: 'arrivalRate', renderer: function (value, meta, record) { 
			if(value != '100'){return '<font color="#FF0000">'+(Math.round(value*100)/100)+'%'+'<font>';}else{return (Math.round(value*100)/100)+'%';}
		}},
		{id: 'process',header: main.operating, width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			if(record.data.diaryDate == ''){return main.detail_information;}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowDiaryDetailGrid'>"+main.detail_information+"</a>";
			var resultStr = String.format(formatStr, record.get('id'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryGridId',
	autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'diarysdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'diaryedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			/*{xtype: 'label', text: main.key_word},
			{id: 'diarydif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchDiaryGrid();}
			}}},'-',*/
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				searchDiaryGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/diarymarktj/diarymarktj.do?method=listDiaryMarkTj&expExcel=true&deviceIds='+diaryDeviceids+"&startTime="+diaryStartTime+"&endTime="+diaryEndTime+"&start=0&limit=65535",
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
				});
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
					case 'ShowDiaryDetailGrid':
						ShowDiaryDetailGridF(record);
						break;
				}
			}
		}
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: DiaryStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

var DiaryDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({
		url: path+'/diarymarktj/diarymarktj.do?method=listDiaryTjDetail'
	}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success',
		root: 'data'
	}, [{name: 'deviceId'},{name: 'diaryDate'},{name: 'diaryid'},
	    {name: 'entCode'},{name: 'isArrive'},{name: 'longitude'},{name: 'latitude'},
	    {name: 'pd'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					deviceId: diaryDeviceid, deviceIds: diaryDiaryDate
				};
			}
		}
	}
});

var DiaryDetailGrid = {
	xtype: 'grid',
	id: 'DiaryDetailGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: DiaryDetailStore,
	loadMask: {msg: main.loading},
	columns: [
		/*{id: 'pd' , header: main.position_description, width: 350, sortable: true,  dataIndex: 'pd',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},*/
		{header: '经度', width: 130, sortable: true,  dataIndex: 'longitude'},
		{header: '纬度', width: 130, sortable: true,  dataIndex: 'latitude'},
		{header: '是否到达', width: 130, sortable: true,  dataIndex: 'isArrive', renderer: function (value, meta, record) { 
			if(value == '0'){return '<font color="#FF0000">否<font>';}else{return '是';}
		}}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryDetailGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[{
			id: 'DiaryDetailGridTitle',
			text: ''
		},'-',new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('DiaryReportPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
        store: DiaryDetailStore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
};
