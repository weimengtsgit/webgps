var searchValue = '';

var delbut = new Ext.Action({
			text : '保存',
			id : 'delbut',
			// disabled : true,
			handler : function() {
				var tmpfrmentCode = Ext.getCmp('frmentCode').getValue();
				if (tmpfrmentCode == '') {
					Ext.MessageBox.alert('提示', '请输入企业代码!');
					return;
				}
				var tmpfrmentName = Ext.getCmp('frmentName').getValue();
				if (tmpfrmentName == '') {
					Ext.MessageBox.alert('提示', '请输入企业名称!');
					return;
				}
				var tmpfrmsmsAccount = Ext.getCmp('frmsmsAccount').getValue();
				/*
				 * if(tmpfrmsmsAccount==''){ Ext.MessageBox.alert('提示',
				 * '请输入短信帐号!'); return ; }
				 */

				var tmpfrmsmsPwd = Ext.getCmp('frmsmsPwd').getValue();
				var tmpfrmConfirmsmsPwd = Ext.getCmp('frmConfirmsmsPwd')
						.getValue();
				/*
				 * if(tmpfrmsmsPwd==''){ Ext.MessageBox.alert('提示', '请输入短信密码!');
				 * return ; }
				 */
				if (tmpfrmsmsPwd != tmpfrmConfirmsmsPwd) {
					Ext.MessageBox.alert('提示', '两次短信密码输入不一致,请重新输入!');
					return;
				}

				var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
				if (tmpfrmuserName == '') {
					Ext.MessageBox.alert('提示', '请输入企业管理员名称!');
					return;
				}
				var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
				if (tmpfrmuserAccount == '') {
					Ext.MessageBox.alert('提示', '请输入企业管理员登录帐号!');
					return;
				}

				var tmpPassword = Ext.getCmp('frmPassword').getValue();
				var tmpConfirmPassword = Ext.getCmp('frmConfirmPassword')
						.getValue();
				if (tmpPassword == '') {
					Ext.MessageBox.alert('提示', '请输入企业管理员登录密码!');
					return;
				}
				if (tmpPassword != tmpConfirmPassword) {
					Ext.MessageBox.alert('提示', '两次企业管理员登录密码输入不一致,请重新输入!');
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

				Ext.MessageBox.confirm('提示', '您确定要添加新企业吗?', addConfirm);

			},
			iconCls : 'icon-save'
		});

function addConfirm(btn) {
	if (btn == 'yes') {
		Ext.Msg.show({
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
	var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
	var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
	var tmpPassword = Ext.getCmp('frmPassword').getValue();
	var tmpMaxUserNum = Ext.getCmp('frmMaxUserNum').getValue();
	var endTime = Ext.getCmp('endTime').getValue();
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
				url : path + '/system/ent.do?method=addEnt',
				method : 'POST',
				timeout : 600000,
				params : {
					entCode : encodeURI(tmpfrmentCode),
					entName : encodeURI(tmpfrmentName),
					maxUserNum : encodeURI(tmpMaxUserNum),
					smsAccount : encodeURI(tmpfrmsmsAccount),
					smsPwd : encodeURI(tmpfrmsmsPwd),
					userName : encodeURI(tmpfrmuserName),
					userAccount : encodeURI(tmpfrmuserAccount),
					password : encodeURI(tmpPassword),
					// insert entTime by zhaofeng
					endTime : endTime,
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
						store.reload();
						Ext.Msg.alert('提示', '保存成功');
						return;
					} else if (res.result == 3) {
						// store.reload();
						Ext.Msg.alert('提示', "已存在企业代码!");
						return;
					} else {
						// store.reload();
						Ext.Msg.alert('提示', "保存失败!");
						return;
					}
				},
				failure : function(request) {
					Ext.Msg.alert('提示', "保存失败!");
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
						allowBlank : false
					}, {
						// xtype : 'textfield',
						fieldLabel : '企业名称',
						id : 'frmentName',
						width : 150,
						allowBlank : false
					},/*
						 * ,{ //xtype : 'textfield', fieldLabel: '最大用户数', id:
						 * 'frmmaxUserNum', width: 150, allowBlank:false }
						 */{
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
						// xtype : 'textfield',
						fieldLabel : '企业管理员名称',
						id : 'frmuserName',
						width : 150,
						allowBlank : false
					}, {
						// xtype : 'textfield',
						fieldLabel : '企业管理员登录帐号',
						id : 'frmuserAccount',
						width : 150,
						allowBlank : false
					}, {
						fieldLabel : '企业管理员登录密码',
						id : 'frmPassword',
						inputType : 'password',
						width : 150,
						allowBlank : false
					}, {
						fieldLabel : '企业管理员登录确认密码',
						id : 'frmConfirmPassword',
						inputType : 'password',
						width : 150,
						// vtype: 'password',
						// initialPassField: 'frmPassword', // id of the initial
						// password field
						allowBlank : false
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
		// {header: "到期时间", width: 100, sortable: true, dataIndex: 'endTime'},
		// {header: "短信密码", width: 100, sortable: true, dataIndex:
		// 'smsPwd',hidden:true},
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
	// sm : sm,
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

function checkEmail(email) {
	var reyx = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	return (reyx.test(email));
}
