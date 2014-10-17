var searchValue = '';

var delbut = new Ext.Action({
			text : '修改',
			id : 'delbut',
			// disabled : true,
			handler : function() {
				var tmpfrmentCode = Ext.getCmp('frmentCode').getValue();
				if (tmpfrmentCode == '') {
					parent.Ext.MessageBox.alert('提示', '请输入企业代码!');
					return;
				}
				var tmpfrmentName = Ext.getCmp('frmentName').getValue();
				if (tmpfrmentName == '') {
					parent.Ext.MessageBox.alert('提示', '请输入企业名称!');
					return;
				}
				var tmpfrmsmsAccount = Ext.getCmp('frmsmsAccount').getValue();
				if (tmpfrmsmsAccount == '') {
					// parent.Ext.MessageBox.alert('提示', '请输入短信帐号!');
					// return ;
				}

				var tmpfrmsmsPwd = Ext.getCmp('frmsmsPwd').getValue();
				var tmpfrmConfirmsmsPwd = Ext.getCmp('frmConfirmsmsPwd')
						.getValue();
				if (tmpfrmsmsPwd == '') {
					// parent.Ext.MessageBox.alert('提示', '请输入短信密码!');
					// return ;
				}
				if (tmpfrmsmsPwd != tmpfrmConfirmsmsPwd) {
					parent.Ext.MessageBox.alert('提示', '两次短信密码输入不一致,请重新输入!');
					return;
				}

				// var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
				var patrn = /["',]/im;
				var emailfieldset1 = Ext.getCmp('emailfieldset1').collapsed;
				if (!emailfieldset1) {
					var email1 = Ext.getCmp('email1');
					if (Ext.util.Format.trim(email1.getValue()) == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱1中邮箱地址!');
						return;
					}
					if (!email1.isValid()) {
						Ext.MessageBox.alert('提示', '邮箱1中邮箱地址格式输入有误,请重新输入!');
						return;
					}

					var contactName1 = Ext.util.Format.trim(Ext
							.getCmp('contactName1').getValue());
					if (contactName1 == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱1中联系人姓名!');
						return;
					}
					if (patrn.test(contactName1)) {
						Ext.MessageBox.alert('提示', '邮箱1中联系人姓名中不能含有"\',等非法字符!');
						return;
					}

					var position1 = Ext.util.Format.trim(Ext
							.getCmp('position1').getValue());
					if (position1 == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱1中职务!');
						return;
					}
					if (patrn.test(position1)) {
						Ext.MessageBox.alert('提示', '邮箱1中职务中不能含有"\',等非法字符!');
						return;
					}
				}

				var emailfieldset2 = Ext.getCmp('emailfieldset2').collapsed;
				if (!emailfieldset2) {
					var email2 = Ext.getCmp('email2');
					if (Ext.util.Format.trim(email2.getValue()) == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱2中邮箱地址!');
						return;
					}
					if (!email2.isValid()) {
						Ext.MessageBox.alert('提示', '邮箱2中邮箱地址格式输入有误,请重新输入!');
						return;
					}

					var contactName2 = Ext.util.Format.trim(Ext
							.getCmp('contactName2').getValue());
					if (contactName2 == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱2中联系人姓名!');
						return;
					}
					if (patrn.test(contactName2)) {
						Ext.MessageBox.alert('提示', '邮箱2中联系人姓名中不能含有"\',等非法字符!');
						return;
					}

					var position2 = Ext.util.Format.trim(Ext
							.getCmp('position2').getValue());
					if (position2 == '') {
						Ext.MessageBox.alert('提示', '请输入邮箱2中职务!');
						return;
					}
					if (patrn.test(position2)) {
						Ext.MessageBox.alert('提示', '邮箱2中职务中不能含有"\',等非法字符!');
						return;
					}
				}
				parent.Ext.MessageBox.confirm('提示', '您确定要修改企业吗?', addConfirm);

			},
			iconCls : 'icon-modify'
		});

function addConfirm(btn) {
	if (btn == 'yes') {
		parent.Ext.Msg.show({
					msg : '正在保存 请稍等...',
					progressText : '保存...',
					width : 300,
					wait : true,
					// waitConfig: {interval:200},
					icon : 'ext-mb-download'
				});
		add();
	}
}

// 添加部门
function add() {
	var tmpfrmentCode = Ext.getCmp('frmentCode').getValue();
	var tmpfrmentName = Ext.getCmp('frmentName').getValue();
	var tmpfrmsmsAccount = Ext.getCmp('frmsmsAccount').getValue();
	var tmpfrmsmsPwd = Ext.getCmp('frmsmsPwd').getValue();
	var tmpMaxUserNum = Ext.getCmp('frmMaxUserNum').getValue();
	var endTime = Ext.getCmp('endTime').getValue();
	var entStatus = Ext.getCmp('entStatus').getValue().getGroupValue();
	var smsType = Ext.getCmp('smsType').getValue();
	var visitTjStatus = Ext.getCmp('visitTjStatus').getValue();
	var reportStatus = Ext.getCmp('reportStatus').getValue();
	var templateTypeCombo_ = Ext.getCmp('templateTypeCombo').getValue();
	var tmpEmail1 = '';
	var tmpEmail2 = '';
	var tmpPersionGreyNum = Ext.getCmp('persionGreyNum').getValue();
	var tmpCarGreyNum = Ext.getCmp('carGreyNum').getValue();
	var emailfieldset1 = Ext.getCmp('emailfieldset1').collapsed;
	if (!emailfieldset1) {
		tmpEmail1 = Ext.getCmp('email1').getValue() + ','
				+ Ext.util.Format.trim(Ext.getCmp('contactName1').getValue())
				+ ','
				+ Ext.util.Format.trim(Ext.getCmp('position1').getValue())
	}
	var emailfieldset2 = Ext.getCmp('emailfieldset2').collapsed;
	if (!emailfieldset2) {
		tmpEmail2 = Ext.getCmp('email2').getValue() + ','
				+ Ext.util.Format.trim(Ext.getCmp('contactName2').getValue())
				+ ','
				+ Ext.util.Format.trim(Ext.getCmp('position2').getValue())
	}
	Ext.Ajax.request({
				// url :url,
				url : path + '/system/ent.do?method=updateEnt',
				method : 'POST',
				params : {
					entCode : encodeURI(tmpfrmentCode),
					entName : encodeURI(tmpfrmentName),
					maxUserNum : encodeURI(tmpMaxUserNum),
					smsAccount : encodeURI(tmpfrmsmsAccount),
					smsPwd : encodeURI(tmpfrmsmsPwd),
					endTime : endTime,
					entStatus : entStatus,
					smsType : smsType,
					visitTjStatus : visitTjStatus,
					reportStatus : reportStatus,
					templateType : templateTypeCombo_,
					email1 : encodeURI(tmpEmail1),
					email2 : encodeURI(tmpEmail2),
					persionGreyInterval : tmpPersionGreyNum,
					carGreyInterval : tmpCarGreyNum
				},
				// timeout : 10000,
				success : function(request) {
					var res = Ext.decode(request.responseText);
					// alert(request.responseText);
					if (res.result == 1) {
						// treeload.load(root);
						// store.reload();
						var cursor = userGrid.getBottomToolbar().cursor;
						store.load({
									params : {
										start : cursor,
										limit : 20,
										searchValue : encodeURI(searchValue)
									}
								});

						parent.Ext.Msg.alert('提示', '修改成功');
						return;
					} else if (res.result == 3) {
						// store.reload();
						parent.Ext.Msg.alert('提示', "不存在企业代码!");
						return;
					} else {
						// store.reload();
						parent.Ext.Msg.alert('提示', "修改失败!");
						return;
					}
				},
				failure : function(request) {
					parent.Ext.Msg.alert('提示', "修改失败!");
				}
			});
}

var simple = new Ext.FormPanel({
			region : 'center',
			labelWidth : 150, // label settings here cascade unless overridden
			// url:'save-form.php',
			frame : true,
			title : '基本信息',
			bodyStyle : 'padding:5px 5px 0',
			width : 200,
			defaults : {
				width : 330
			},
			defaultType : 'textfield',
			autoScroll : true,
			items : [{
						// xtype : 'textfield',
						fieldLabel : '企业代码',
						id : 'frmentCode',
						width : 150,
						readOnly : true,
						allowBlank : false
					}, {
						// xtype : 'textfield',
						fieldLabel : '企业名称',
						id : 'frmentName',
						width : 150,
						allowBlank : false
					}/*
						 * ,{ //xtype : 'textfield', fieldLabel: '最大用户数', id:
						 * 'frmmaxUserNum', width: 150, allowBlank:false }
						 */, {
						// xtype : 'textfield',
						fieldLabel : '短信帐号',
						id : 'frmsmsAccount',
						width : 150
					}, {
						fieldLabel : '短信密码',
						id : 'frmsmsPwd',
						inputType : 'password',
						width : 150
					}, {
						fieldLabel : '短信确认密码',
						id : 'frmConfirmsmsPwd',
						name : 'frmConfirmsmsPwd',
						inputType : 'password',
						width : 150
					}, {
						fieldLabel : '企业终端数量',
						id : 'frmMaxUserNum',
						width : 150
					}, {
						xtype : 'datefield',
						fieldLabel : '服务到期时间',
						id : 'endTime',
						format : 'Y-m-d',
						editable : false,
						width : 150
					}, {
						xtype : 'radiogroup',
						fieldLabel : '部门状态',
						width : 130,
						id : 'entStatus',
						items : [{
									boxLabel : '使用',
									id : 'useradio',
									name : 'entStatus',
									inputValue : 1,
									checked : true
								}, {
									boxLabel : '停用',
									id : 'stopradio',
									name : 'entStatus',
									inputValue : 0
								}]
					}, {
						id : 'smsType',
						xtype : 'combo',
						fieldLabel : '短信通道',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[1, '大汉三通'], [3, '国都']]
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						mode : 'local',
						forceSelection : true,
						triggerAction : 'all',
						value : 3,
						selectOnFocus : true
					}, {
						id : 'visitTjStatus',
						xtype : 'combo',
						fieldLabel : '是否需要拜访统计',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '否'], [1, '是']]
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						mode : 'local',
						forceSelection : true,
						triggerAction : 'all',
						value : 1,
						selectOnFocus : true
					}, {
						id : 'reportStatus',
						xtype : 'combo',
						fieldLabel : '是否需要导出报表日志',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '否'], [1, '是']]
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						mode : 'local',
						forceSelection : true,
						triggerAction : 'all',
						value : 1,
						selectOnFocus : true
					}, {
						id : 'templateTypeCombo',
						xtype : 'combo',
						fieldLabel : '指标模板',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '周'], [1, '旬'], [2, '月']]
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						mode : 'local',
						forceSelection : true,
						triggerAction : 'all',
						value : 2,
						selectOnFocus : true
					}, {
						xtype : 'fieldset',
						id : 'emailfieldset1',
						checkboxToggle : true,
						title : '邮箱1',
						autoHeight : true,
						defaults : {
							width : 150
						},
						defaultType : 'textfield',
						collapsed : true,
						items : [{
									fieldLabel : '邮箱地址',
									id : 'email1',
									vtype : 'email'
								}, {
									fieldLabel : '联系人',
									id : 'contactName1'
								}, {
									fieldLabel : '职务',
									id : 'position1'
								}]
					}, {
						xtype : 'fieldset',
						id : 'emailfieldset2',
						checkboxToggle : true,
						title : '邮箱2',
						autoHeight : true,
						defaults : {
							width : 150
						},
						defaultType : 'textfield',
						collapsed : true,
						items : [{
									fieldLabel : '邮箱地址',
									id : 'email2',
									vtype : 'email'
								}, {
									fieldLabel : '联系人',
									id : 'contactName2'
								}, {
									fieldLabel : '职务',
									id : 'position2'
								}]
					}, {
						fieldLabel : '人员蓝点时间(分钟)',
						id : 'persionGreyNum',
						width : 150,
						value: 15
					}, {
						fieldLabel : '车载灰点时间(分钟)',
						id : 'carGreyNum',
						width : 150,
						value: 1440
					}]
		});

var proxy = new Ext.data.HttpProxy({
			url : path + '/system/ent.do?method=listEnt'
		});

var reader = new Ext.data.JsonReader({
			totalProperty : 'total',
			successProperty : 'success',
			idProperty : 'id',
			root : 'data'
		}, [{
					name : 'entCode'
				}, {
					name : 'entName'
				}, {
					name : 'maxUserNum'
				}, {
					name : 'smsAccount'
				}, {
					name : 'smsPwd'
				}, {
					name : 'endTime'
				}, {
					name : 'entStatus'
				}, {
					name : 'smsType'
				}, {
					name : 'visitTjStatus'
				}, {
					name : 'reportStatus'
				}, {
					name : 'templateType'
				}, {
					name : 'email1'
				}, {
					name : 'email2'
				}, {
					name : 'persionGreyInterval'
				}, {
					name : 'carGreyInterval'
				}]);

var store = new Ext.data.Store({
			autoLoad : {
				params : {
					start : 0,
					limit : 20,
					searchValue : encodeURI(searchValue)
				}
			},
			// id: 'userstore',
			restful : true, // <-- This Store is RESTful
			proxy : proxy,
			reader : reader,
			listeners : {
				beforeload : {
					fn : function(thiz, options) {
						this.baseParams = {
							searchValue : encodeURI(searchValue)
						};
					}
				}
			}
		});

var sm = new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp('frmentCode').setValue(rec.get('entCode'));
					Ext.getCmp('frmentName').setValue(rec.get('entName'));
					Ext.getCmp('frmsmsAccount').setValue(rec.get('smsAccount'));
					Ext.getCmp('frmsmsPwd').setValue(rec.get('smsPwd'));
					Ext.getCmp('frmConfirmsmsPwd').setValue(rec.get('smsPwd'));
					Ext.getCmp('frmMaxUserNum').setValue(rec.get('maxUserNum'));;
					Ext.getCmp('endTime').setValue(rec.get('endTime'));

					Ext.getCmp('persionGreyNum').setValue(rec.get('persionGreyInterval'));
					Ext.getCmp('carGreyNum').setValue(rec.get('carGreyInterval'));

					var tmpentStatus = rec.get('entStatus');
					if (tmpentStatus == '1') {
						Ext.getCmp('entStatus').setValue(1);
					} else {
						Ext.getCmp('entStatus').setValue(0);
					}

					// 短信通道赋值 add by liuhongxiao 2011-12-05
					var smsType = rec.get('smsType');
					if (smsType == '3') {
						Ext.getCmp('smsType').setValue(3);
					} else {
						Ext.getCmp('smsType').setValue(1);
					}
					// 拜访统计赋值 add by liuhongxiao 2011-12-23
					var visitTjStatus = rec.get('visitTjStatus');
					if (visitTjStatus == '1') {
						Ext.getCmp('visitTjStatus').setValue(1);
					} else {
						Ext.getCmp('visitTjStatus').setValue(0);
					}
					// 报表日志赋值 add by liuhongxiao 2011-12-23
					var reportStatus = rec.get('reportStatus');
					if (reportStatus == '1') {
						Ext.getCmp('reportStatus').setValue(1);
					} else {
						Ext.getCmp('reportStatus').setValue(0);
					}
					// 目标维护模板 add by weimeng 2012-8-6
					var templateType = rec.get('templateType');
					if (templateType == '0') {
						Ext.getCmp('templateTypeCombo').setValue(0);
					} else if (templateType == '1') {
						Ext.getCmp('templateTypeCombo').setValue(1);
					} else if (templateType == '2') {
						Ext.getCmp('templateTypeCombo').setValue(2);
					} else {
						Ext.getCmp('templateTypeCombo').setValue(2);
					}
					// 目标维护模板 add by weimeng 2012-8-6

					var email1 = rec.get('email1');
					if (email1 != '') {
						var emailinfo = email1.split(",");
						Ext.getCmp('emailfieldset1').expand(false);
						Ext.getCmp('email1').setValue(emailinfo[0]);
						Ext.getCmp('contactName1').setValue(emailinfo[1]);
						Ext.getCmp('position1').setValue(emailinfo[2]);
					} else {
						Ext.getCmp('emailfieldset1').collapse(false);
						Ext.getCmp('email1').setValue('');
						Ext.getCmp('contactName1').setValue('');
						Ext.getCmp('position1').setValue('');
					}
					var email2 = rec.get('email2');
					if (email2 != '') {
						var emailinfo = email2.split(",");
						Ext.getCmp('emailfieldset2').expand(false);
						Ext.getCmp('email2').setValue(emailinfo[0]);
						Ext.getCmp('contactName2').setValue(emailinfo[1]);
						Ext.getCmp('position2').setValue(emailinfo[2]);
					} else {
						Ext.getCmp('emailfieldset2').collapse(false);
						Ext.getCmp('email2').setValue('');
						Ext.getCmp('contactName2').setValue('');
						Ext.getCmp('position2').setValue('');
					}
				}
			}
		});

var userColumns = [new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
				}), {
			header : "企业代码",
			width : 100,
			sortable : true,
			dataIndex : 'entCode'
		}, {
			header : "企业名称",
			width : 100,
			sortable : true,
			dataIndex : 'entName'
		}, {
			header : "最大用户数",
			width : 100,
			sortable : true,
			dataIndex : 'maxUserNum'
		}, {
			header : "短信帐号",
			width : 100,
			sortable : true,
			dataIndex : 'smsAccount'
		},
		// {header: "短信密码", width: 100, sortable: true, dataIndex:
		// 'smsPwd',hidden:true}
		{
			header : "服务到期时间",
			width : 100,
			sortable : true,
			dataIndex : 'endTime',
			renderer : function(val) {
				if (val == 'null')
					return '';
				return val;
			}
		}, {
			header : "状态",
			width : 100,
			sortable : true,
			dataIndex : 'entStatus',
			renderer : function(val) {
				if (val == '0') {
					return '停用';
				} else if (val == '1') {
					return '正常';
				} else if (val == '2') {
					return '服务过期';
				}
				return val;
			}
		}, {
			header : "短信通道",
			width : 100,
			sortable : true,
			dataIndex : 'smsType',
			renderer : function(val) {
				if (val == 'null') {
					return '';
				} else if (val == '1') {
					return '大汉三通';
				} else if (val == '3') {
					return '国都';
				}
				return val;
			}
		}, {
			header : "拜访统计",
			width : 100,
			sortable : true,
			dataIndex : 'visitTjStatus',
			renderer : function(val) {
				if (val == 'null') {
					return '否';
				} else if (val == '0') {
					return '否';
				} else if (val == '1') {
					return '是';
				}
				return val;
			}
		}, {
			header : "报表日志",
			width : 100,
			sortable : true,
			dataIndex : 'reportStatus',
			renderer : function(val) {
				if (val == 'null') {
					return '否';
				} else if (val == '0') {
					return '否';
				} else if (val == '1') {
					return '是';
				}
				return val;
			}
		}, {
			header : "人员蓝点时间(分钟)",
			width : 100,
			sortable : true,
			dataIndex : 'persionGreyInterval'
		}, {
			header : "车载灰点时间(分钟)",
			width : 100,
			sortable : true,
			dataIndex : 'carGreyInterval'
		}];

var userGrid = new Ext.grid.GridPanel({
	region : 'west',
	width : 450,
	loadMask : {
		msg : '查询中...'
	},
	// iconCls: 'icon-grid',
	// frame: true,
	// title: 'Users',
	// autoExpandColumn: 'name',
	enableColumnHide : false,
	store : store,
	// plugins: [editor],
	columns : userColumns,
	sm : sm,
	// sm : smcheckbox,
	margins : '0 0 0 0',
	bbar : new Ext.PagingToolbar({
				pageSize : 20,
				store : store,
				displayInfo : true,
				displayMsg : '第{0}到第{1}条数据 共{2}条',
				emptyMsg : "没有数据"
			}),
	tbar : [new Ext.form.TextField({
				id : 'DeviceIdField',
				width : 80,
				enableKeyEvents : true,
				listeners : {
					keypress : function(textField, e) {
						if (e.getKey() == e.ENTER) {
							searchValue = Ext.getCmp('DeviceIdField')
									.getValue();
							store.load({
										params : {
											start : 0,
											limit : 20,
											searchValue : encodeURI(searchValue)
										}
									});
						}
					}
				}
			}), '-', new Ext.Action({
				text : '查询',
				handler : function() {
					searchValue = Ext.getCmp('DeviceIdField').getValue();
					store.load({
								params : {
									start : 0,
									limit : 20,
									searchValue : encodeURI(searchValue)
								}
							});

				},
				iconCls : 'icon-search'
			}), '-', delbut]
});

Ext.onReady(function() {

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [simple, userGrid]
					});

		});
