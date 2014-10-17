var sm = new Ext.grid.CheckboxSelectionModel({});
var reader = new Ext.data.JsonReader({
			totalProperty : "total",
			successProperty : "success",
			idProperty : "id",
			root : "data"
		}, [{
					name : "id"
				}, {
					name : "sendTime"
				}, {
					name : "entCode"
				}, {
					name : "entName"
				}, {
					name : "userName"
				}, {
					name : "position"
				}, {
					name : "email"
				}, {
					name : "type"
				}, {
					name : "content"
				}, {
					name : "status"
				}]);
var store = new Ext.data.Store({
			id : "store",
			restful : true,
			proxy : new Ext.data.HttpProxy({
						url : path + "/emaillog/emailSendLog.do"
					}),
			reader : reader
		});
var columns = [sm, {
			id : 'id',
			header : 'id',
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		}, {
			header : '发送时间',
			width : 100,
			sortable : true,
			dataIndex : 'sendTime'
		}, {
			header : '企业代码',
			width : 100,
			sortable : true,
			dataIndex : 'entCode'
		}, {
			header : '企业名称',
			width : 100,
			sortable : true,
			dataIndex : 'entName'
		}, {
			header : '姓名',
			width : 100,
			sortable : true,
			dataIndex : 'userName'
		}, {
			header : '职务',
			width : 100,
			sortable : true,
			dataIndex : 'position'
		}, {
			header : '邮箱',
			width : 100,
			sortable : true,
			dataIndex : 'email'
		}, {
			header : '邮件类型',
			width : 100,
			sortable : true,
			dataIndex : 'type'
		}, {
			header : '发送内容',
			width : 100,
			sortable : true,
			dataIndex : 'content'
		}, {
			header : '状态',
			width : 100,
			sortable : true,
			dataIndex : 'status'
		}];
Ext.onReady(function() {
	var grid = new Ext.grid.GridPanel({
		region : 'west',
		width : 700,
		loadMask : {
			msg : '查询中...'
		},
		sm : sm,
		store : store,
		columns : columns,
		autoScroll : true,
		enableColumnHide : false,
		tbar : [new Ext.form.Label({
							text : '企业代码'
						}), '-', new Ext.form.TextField({
							id : 'entCode',
							width : 80,
							enableKeyEvents : true
						}), '-', new Ext.form.Label({
							text : '企业名称'
						}), '-', new Ext.form.TextField({
							id : 'entName',
							width : 80,
							enableKeyEvents : true
						}), '-', new Ext.form.Label({
							text : '姓名'
						}), '-', new Ext.form.TextField({
							id : 'userName',
							width : 80,
							enableKeyEvents : true
						}), '-', new Ext.form.Label({
							text : '开始时间'
						}), '-', new Ext.form.DateField({
							id : 'startDate',
							fieldLabel : 'Date',
							format : 'Y-m-d',
							width : 100,
							editable : false,
							value : new Date().add(Date.DAY, -1)
						}), '-', new Ext.form.Label({
							text : '结束时间'
						}), '-', new Ext.form.DateField({
							width : 100,
							fieldLabel : 'Date',
							format : 'Y-m-d',
							editable : false,
							value : new Date(),
							id : 'endDate'
						}), '-', new Ext.Action({
					text : '查询',
					iconCls : 'icon-search',
					handler : function() {
						var entCode = Ext.util.Format.trim(Ext
								.getCmp('entCode').getValue());
						var entName = Ext.util.Format.trim(Ext
								.getCmp('entName').getValue());
						var userName = Ext.util.Format.trim(Ext
								.getCmp('userName').getValue());
						var startDate = Ext.getCmp('startDate').getValue();
						var endDate = Ext.getCmp('endDate').getValue();
						if (startDate > endDate) {
							Ext.Msg.alert('提示', '开始时间要小于结束时间!');
							return;
						}
						var startTime = startDate.format('Y-m-d') + ' 00:00:00';
						var endTime = endDate.format('Y-m-d') + ' 23:59:59';
						store.baseParams = {
							method : 'listEmailLogs',
							start : 0,
							limit : 20,
							entCode : encodeURI(entCode),
							entName : encodeURI(entName),
							userName : encodeURI(userName),
							startDate : startTime,
							endDate : endTime
						};
						store.load({
									params : {
										method : 'listEmailLogs',
										start : 0,
										limit : 20,
										entCode : encodeURI(entCode),
										entName : encodeURI(entName),
										userName : encodeURI(userName),
										startDate : startTime,
										endDate : endTime
									}
								});
					}
				}), '-', new Ext.Action({
							text : '删除',
							iconCls : 'icon-del',
							handler : function() {
								var removes = sm.getSelections();
								if (removes.length <= 0) {
									Ext.MessageBox.alert('提示', '请选择要删除的数据!');
									return;
								}
								var ids = '';
								for (var i = 0; i < removes.length; i++) {
									ids += removes[i].get('id') + ',';
								}
								ids = ids.substring(0, ids.length - 1);
								Ext.Msg.show({
											msg : '正在删除 请稍等...',
											progressText : '删除...',
											width : 300,
											wait : true,
											icon : 'ext-mb-download'
										});
								Ext.Ajax.request({
											url : path
													+ '/emaillog/emailSendLog.do',
											method : 'POST',
											params : {
												ids : ids,
												method : 'deleteEmailLogs'
											},
											success : function(resp, opts) {
												var respText = Ext.util.JSON
														.decode(resp.responseText);

												Ext.Msg.alert('提示',
														respText.msg,
														function() {
															store.reload();
														});
											},
											failure : function(resp, opts) {
												var respText = Ext.util.JSON
														.decode(resp.responseText);
												Ext.Msg.alert('提示',
														respText.msg);
											}
										});
							}
						}), '-', new Ext.Action({
					text : '导出Excel',
					iconCls : 'icon-excel',
					handler : function() {
						var params = store.baseParams;
						if (params.method == null) {
							Ext.Msg.alert('提示', '请先进行查询操作!');
							return;
						}
						var excelform = document.excelform;
						excelform.action = encodeURI(path
								+ '/emaillog/emailSendLog.do?method=exportEmailLogs&entCode='
								+ params.entCode + '&entName=' + params.entName
								+ '&userName=' + params.userName
								+ '&startDate=' + params.startDate
								+ '&endDate=' + params.endDate);
						excelform.submit();
					}
				})],
		bbar : new Ext.PagingToolbar({
					pageSize : 20,
					store : store,
					displayInfo : true,
					displayMsg : '第{0}到第{1}条数据 共{2}条',
					emptyMsg : "没有数据"
				})
	});

	new Ext.Viewport({
				layout : 'border',
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							items : [grid]
						}]
			});
});