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
				header : '����',
				width : 130,
				sortable : true,
				dataIndex : 'termName',
				renderer : function tips(val) {
					return '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
			},
			{
				header : '�ն����к�',
				width : 130,
				sortable : true,
				dataIndex : 'deviceId'
			},
			{
				header : 'ָ������',
				width : 130,
				sortable : true,
				dataIndex : 'instruction',
				renderer : function tips(val) {
					return '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
			},
			{
				header : '����״̬',
				width : 130,
				sortable : true,
				dataIndex : 'state',
				renderer : function tips(val) {
					var tmp = '';
					if (val == 0) {
						tmp = '���ͳɹ�';
					} else if(val==1){
						tmp = '������';
					}else {
						tmp = '����ʧ��';
					}
					return tmp;
				}
			},
			{
				header : 'ָ�����',
				width : 130,
				sortable : true,
				dataIndex : 'type1',
				renderer : function tips(val) {
					var tmp = '';
					if (val == 0) {
						tmp = '����Ƶ��ָ��';
					} else if (val == 2) {
						tmp = '���Ͷϵ�ָ��';
					} else if (val == 3) {
						tmp = '��������ָ��';
					} else if (val == 7) {
						tmp = '����پ�ָ��';
					} else {
						tmp = '';
					}
					return tmp;
				}
			},
			{
				header : 'ָ���������',
				width : 130,
				sortable : true,
				dataIndex : 'param',
				align : 'right'
			},
			{
				header : 'ʱ��',
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
					text : 'ָ�����'
				},
				'-',
				new Ext.form.ComboBox({
					id : 'structionsType',
					enableKeyEvents : true,
					grow : true,
					name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// �������ڱ���
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : [ [ '��������', '' ], [ '����Ƶ��ָ��', '0' ],
								[ '���Ͷϵ�ָ��', '2' ], [ '��������ָ��', '3' ],
								[ '���Э��ָ��', '7' ] ]
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
									title : '�����ļ�',
									closable : true,
									closeAction : 'close',
									autoWidth : false,
									width : 200,
									heigth : 150,
									items : [ new Ext.Panel(
											{
												html : "<a href='"
														+ excelhtml
														+ "' target='_blank' onclick='Ext.Msg.hide()'>������������ر���</a>",
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
		displayMsg : '��{0}����{1}������ ��{2}��',
		emptyMsg : "û������"
	})
};
