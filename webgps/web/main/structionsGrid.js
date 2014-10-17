var structionsDeviceids = '';
var timeDistanceStatSearchValue = '';
var structionsStartTime = tmpdate + ' ' + '00:00:00';
var structionsEndTime = tmpdate + ' ' + '23:59:59';
var structionsType='';

function initStructionsRecordGrid() {
	var tmpstartdate = Ext.getCmp('structionsStatsdf').getValue().format(
			'Y-m-d');
	var tmpenddate = Ext.getCmp('structionsStatedf').getValue().format('Y-m-d');
	var type=Ext.getCmp('structionsType').getValue();
	structionsType=type;
	structionsStartTime = tmpstartdate;
	structionsEndTime = tmpenddate;
	structionsDeviceids = getDeviceId();
}

function searchStructionsRecordGrid() {
	storeLoad(StructionsRecordStore, 0, 15, structionsDeviceids,
			timeDistanceStatSearchValue, structionsStartTime,
			structionsEndTime, 0,structionsType, false);
}

var StructionsRecordStore = new Ext.data.Store({
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : path + '/struction/struction.do?method=listStructionsRecord'
	}),
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		successProperty : 'success',
		idProperty : 'a',
		root : 'data'
	}, [ {
		name : 'id'
	}, {
		name : 'termName'
	}, {
		name : 'deviceId'
	}, {
		name : 'instruction'
	}, {
		name : 'state'
	}, {
		name : 'type1'
	}, {
		name : 'param'
	}, {
		name : 'createTime'
	} ]),
	listeners : {
		beforeload : {
			fn : function(thiz, options) {
				this.baseParams = {
					startTime : structionsStartTime,
					endTime : structionsEndTime,
					deviceIds : structionsDeviceids,
					type:structionsType
				};
			}
		}
	}
});

var StructionsRecordGrid = {
	xtype : 'grid',
	id : 'StructionsRecordGrid',
	store : StructionsRecordStore,
	loadMask : {
		msg : main.loading
	},
	columns : [
			{
				header : 'id',
				width : 10,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			},
			{
				header : '名称',
				width : 130,
				sortable : true,
				dataIndex : 'termName',
				renderer : function tips(val) {
					return '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
			},
			{
				header : '终端序列号',
				width : 130,
				sortable : true,
				dataIndex : 'deviceId'
			},
			{
				header : '指令内容',
				width : 130,
				sortable : true,
				dataIndex : 'instruction',
				renderer : function tips(val) {
					return '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
			},
			{
				header : '发送状态',
				width : 130,
				sortable : true,
				dataIndex : 'state',
				renderer : function tips(val) {
					var tmp = '';
					if (val == 0) {
						tmp = '发送成功';
					} else if(val==1){
						tmp = '待处理';
					}else {
						tmp = '发送失败';
					}
					return tmp;
				}
			},
			{
				header : '指令类别',
				width : 130,
				sortable : true,
				dataIndex : 'type1',
				renderer : function tips(val) {
					var tmp = '';
					if (val == 0) {
						tmp = '设置频率指令';
					} else if (val == 2) {
						tmp = '断油断电指令';
					} else if (val == 3) {
						tmp = '超速设置指令';
					} else if (val == 7) {
						tmp = '解除劫警指令';
					} else {
						tmp = '';
					}
					return tmp;
				}
			},
			{
				header : '指令类别内容',
				width : 130,
				sortable : true,
				dataIndex : 'param',
				align : 'right'
			},
			{
				header : '时间',
				width : 130,
				sortable : true,
				dataIndex : 'createTime',
				align : 'right'
			} ],
	stripeRows : true,
	stateful : true,
	stateId : 'StructionsRecordGridId',
	tbar : {
		xtype : 'toolbar',
		items : [
				{
					xtype : 'label',
					text : main.start_time
				},
				{
					id : 'structionsStatsdf',
					xtype : 'datefield',
					fieldLabel : 'Date',
					format : 'Y-m-d',
					width : 80,
					editable : false,
					value : new Date()
				},
				{
					xtype : 'label',
					text : main.end_time
				},
				{
					id : 'structionsStatedf',
					xtype : 'datefield',
					fieldLabel : 'Date',
					format : 'Y-m-d',
					width : 80,
					editable : false,
					value : new Date()
				},
				'-',
				{
					xtype : 'label',
					text : '指令类别'
				},
				'-',
				new Ext.form.ComboBox({
					id : 'structionsType',
					enableKeyEvents : true,
					grow : true,
					name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// 数据是在本地
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : [ [ '所有类型', '' ], [ '设置频率指令', '0' ],
								[ '断油断电指令', '2' ], [ '超速设置指令', '3' ],
								[ '解除协警指令', '7' ] ]
					})
				}),
				'-',
				{
					xtype : 'button',
					text : main.search,
					iconCls : 'icon-search',
					handler : function() {
						initStructionsRecordGrid();
						searchStructionsRecordGrid();
					}
				},
				'-',
				{
					xtype : 'button',
					text : main.oupput_from_excel,
					iconCls : 'icon-excel',
					handler : function() {

						initStructionsRecordGrid();
						var excelhtml = path
								+ '/struction/struction.do?method=listStructionsRecord&expExcel=true&startTime='
								+ structionsStartTime + '&endTime='
								+ structionsEndTime + '&deviceIds='
								+ structionsDeviceids +'&type='
								+ structionsType + '&entCode=' + empCode64
								+ '&userAccount=' + account64 + '&password='
								+ password64;
						var win = new Ext.Window(
								{
									title : '下载文件',
									closable : true,
									closeAction : 'close',
									autoWidth : false,
									width : 200,
									heigth : 150,
									items : [ new Ext.Panel(
											{
												html : "<a href='"
														+ excelhtml
														+ "' target='_blank' onclick='Ext.Msg.hide()'>点击此链接下载报表</a>",
												frame : true
											}) ]
								});
						win.show();
					}
				} ]
	},
	bbar : new Ext.PagingToolbar({
		pageSize : 15,
		store : StructionsRecordStore,
		displayInfo : true,
		displayMsg : '第{0}到第{1}条数据 共{2}条',
		emptyMsg : "没有数据"
	})
};
