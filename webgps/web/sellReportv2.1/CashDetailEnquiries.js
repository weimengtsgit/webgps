var enquiriesStartTime = tmpdate + ' ' + '00:00:00';
var enquiriesEndTime = tmpdate + ' ' + '23:59:59';
var enquiriesGpsDeviceids = '';
var enquiriesPoiName = '';
var enquiriesApproved = -1;

function initCashDetailEnquiriesGrid(deviceID_) {
	enquiriesGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('cashDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('cashDetailedf').getValue().format('Y-m-d');
	enquiriesPoiName = Ext.getCmp('cashDetaildif').getValue();
	enquiriesApproved = Ext.getCmp('enquiriesApproved').getValue();
	enquiriesStartTime = tmpstartdate + ' ' + '00:00:00';
	enquiriesEndTime = tmpenddate + ' ' + '23:59:59';
}

function searchCashDetailEnquiriesGrid() {
	storeLoad(EnquiriesStore, 0, 15, enquiriesGpsDeviceids, enquiriesPoiName,
			enquiriesStartTime, enquiriesEndTime, enquiriesApproved, false);
}

var EnquiriesStore = new Ext.data.Store({
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : path + '/cash/cash.do?method=listCashDetails' // 按条件查询信息方法
	}),
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		successProperty : 'success',
		idProperty : 'a',
		root : 'data'
	}, [ {
		name : 'id'
	}, {
		name : 'createOn'
	}, {
		name : 'groupName'
	}, {
		name : 'vehicleNumber'
	}, {
		name : 'poiName'
	}, {
		name : 'cashAmount'
	}, {
		name : 'cashAmount2'
	}, {
		name : 'cashAmount3'
	}, {
		name : 'cashAmount4'
	}, {
		name : 'cashAmount5'
	}, {
		name : 'cashAmount6'
	}, {
		name : 'cashAmount7'
	}, {
		name : 'cashAmount8'
	}, {
		name : 'cashAmount9'
	}, {
		name : 'cashAmount10'
	}, {
		name : 'approved'
	} ]),
	listeners : {
		beforeload : {
			fn : function(thiz, options) {
				this.baseParams = {
					searchValue : encodeURI(enquiriesPoiName),
					startTime : enquiriesStartTime,
					endTime : enquiriesEndTime,
					deviceIds : enquiriesGpsDeviceids
				};
			}
		}
	}
});

var EnquiriesSm = new Ext.grid.CheckboxSelectionModel({});
var CashDetailEnquiriesGrid = {
	xtype : 'grid',
	id : 'CashDetailEnquiriesGrid',
	store : EnquiriesStore,
	loadMask : {
		msg : main.loading
	},
	columns : [ EnquiriesSm, {
		header : '日期',
		width : 130,
		sortable : true,
		dataIndex : 'createOn'
	}, {
		header : '部门',
		width : 130,
		sortable : true,
		dataIndex : 'groupName'
	}, {
		header : '员工姓名',
		width : 100,
		sortable : true,
		dataIndex : 'vehicleNumber'
	}, {
		header : '客户名称',
		width : 100,
		sortable : true,
		dataIndex : 'poiName'
	}, {
		header : "满口香",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount'
	}, {
		header : "地之香",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount2'
	}, {
		header : "天之香",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount3'
	}, {
		header : "小青花窖藏",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount4'
	}, {
		header : "小青花典藏",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount5'
	}, {
		header : "大青花臻藏",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount6'
	}, {
		header : "大青花臻品",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount7'
	}, {
		header : "新五年原浆",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount8'
	}, {
		header : "新十年原浆",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount9'
	}, {
		header : "十五年原浆",
		width : 100,
		sortable : true,
		dataIndex : 'cashAmount10'
	}, {
		header : '状态',
		width : 100,
		sortable : true,
		dataIndex : 'approved',
		renderer : function(value, meta, record) {
			if (value == '0') {
				return "未审核";
			} else {
				return "审核";
			}
		}
	} ],
	sm : EnquiriesSm,
	stripeRows : true,
	stateful : true,
	stateId : 'CashDetailEnquiriesGridId',
	tbar : {
		xtype : 'toolbar',
		items : [
				{
					xtype : 'label',
					text : main.start_time
				},
				{
					id : 'cashDetailsdf',
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
					text : main.end_time
				},
				{
					id : 'cashDetailedf',
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
					text : '客户名称'
				},
				{
					id : 'cashDetaildif',
					xtype : 'textfield',
					width : 60,
					enableKeyEvents : true,
					listeners : {
						keypress : function(textField, e) {
							if (e.getKey() == e.ENTER) {
								var deviceID_ = getDeviceId();
								if (deviceID_ == '') {
									Ext.Msg.alert('提示', '请选择终端!');
									return;
								} else {
									initCashDetailEnquiriesGrid(deviceID_);
									searchCashDetailEnquiriesGrid();
								}
							}
						}
					}
				},
				'-',
				{
					xtype : 'label',
					text : '状态'
				},
				new Ext.form.ComboBox({
					width : 60,
					id : 'enquiriesApproved',
					enableKeyEvents : true,
					grow : true,
					valueField : "id",
					displayField : "name",
					mode : 'local',// 数据是在本地
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					value : -1,
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : [ [ '--全部--', '-1' ], [ '审核', '1' ],
								[ '未审核', '0' ] ]
					})
				}),
				'-',
				{
					xtype : 'button',
					text : main.search,
					iconCls : 'icon-search',
					handler : function() {
						var deviceID_ = getDeviceId();
						if (deviceID_ == '') {
							Ext.Msg.alert('提示', '请选择终端!');
							return;
						} else {
							initCashDetailEnquiriesGrid(deviceID_);
							searchCashDetailEnquiriesGrid();
						}
					}
				},
				'-',
				{
					xtype : 'button',
					text : '导出Excel',
					iconCls : 'icon-excel',
					handler : function() {
						var deviceID_ = getDeviceId();
						if (deviceID_ == '') {
							Ext.Msg.alert('提示', '请选择终端!');
							return;
						} else {
							initCashDetailEnquiriesGrid(deviceID_);
							var excelhtml = path
									+ '/cash/cash.do?method=listEnquiriesExpExcel&searchValue='
									+ encodeURI(encodeURI(enquiriesPoiName))
									+ '&startTime=' + enquiriesStartTime
									+ '&endTime=' + enquiriesEndTime
									+ '&deviceIds=' + enquiriesGpsDeviceids
									+ '&duration=' + enquiriesApproved;
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

					}
				},
				'-',
				{
					xtype : 'button',
					text : '审核',
					iconCls : 'icon-saved',
					handler : function() {
						var tmpRecArr = EnquiriesSm.getSelections();
						var ids = '';
						for ( var i = 0; i < tmpRecArr.length; i++) {
							ids += tmpRecArr[i].get('id') + ',';
						}
						if (ids.length > 0) {
							ids = ids.substring(0, ids.length - 1);
						} else {
							Ext.MessageBox.alert('提示', '请选择要审核的记录!');
							return;
						}
						approvedCashids = ids;
						Ext.MessageBox.confirm('提示', '确定通过审核吗?',
								approvedCashConfirm);

					}
				} ]
	},
	bbar : new Ext.PagingToolbar({
		pageSize : 15,
		store : EnquiriesStore,
		displayInfo : true,
		displayMsg : '第{0}到第{1}条数据 共{2}条',
		emptyMsg : "没有数据"
	})
};

var approvedCashids;
function approvedCashConfirm(btn) {
	if (btn == 'yes') {
		Ext.Msg.show({
			msg : '正在提交 请稍等...',
			progressText : '提交...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		Ext.Ajax.request({
			url : path + '/cash/cash.do?method=approved',
			method : 'POST',
			params : {
				ids : encodeURI(approvedCashids)
			},
			// timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if (res.result == 1) {
					EnquiriesStore.reload();
					Ext.Msg.alert('提示', '操作成功');
					return;
				} else {
					Ext.Msg.alert('提示', "操作失败!");
					return;
				}
			},
			failure : function(request) {
				Ext.Msg.alert('提示', "操作失败!");
			}
		});
	}
}
