var searchValue = '';

var delbut = new Ext.Action({
			text : '����',
			id : 'delbut',
			// disabled : true,
			handler : function() {
				var tmpfrmentCode = Ext.getCmp('frmentCode').getValue();
				if (tmpfrmentCode == '') {
					Ext.MessageBox.alert('��ʾ', '��������ҵ����!');
					return;
				}
				var tmpfrmentName = Ext.getCmp('frmentName').getValue();
				if (tmpfrmentName == '') {
					Ext.MessageBox.alert('��ʾ', '��������ҵ����!');
					return;
				}
				var tmpfrmsmsAccount = Ext.getCmp('frmsmsAccount').getValue();
				/*
				 * if(tmpfrmsmsAccount==''){ Ext.MessageBox.alert('��ʾ',
				 * '����������ʺ�!'); return ; }
				 */

				var tmpfrmsmsPwd = Ext.getCmp('frmsmsPwd').getValue();
				var tmpfrmConfirmsmsPwd = Ext.getCmp('frmConfirmsmsPwd')
						.getValue();
				/*
				 * if(tmpfrmsmsPwd==''){ Ext.MessageBox.alert('��ʾ', '�������������!');
				 * return ; }
				 */
				if (tmpfrmsmsPwd != tmpfrmConfirmsmsPwd) {
					Ext.MessageBox.alert('��ʾ', '���ζ����������벻һ��,����������!');
					return;
				}

				var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
				if (tmpfrmuserName == '') {
					Ext.MessageBox.alert('��ʾ', '��������ҵ����Ա����!');
					return;
				}
				var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
				if (tmpfrmuserAccount == '') {
					Ext.MessageBox.alert('��ʾ', '��������ҵ����Ա��¼�ʺ�!');
					return;
				}

				var tmpPassword = Ext.getCmp('frmPassword').getValue();
				var tmpConfirmPassword = Ext.getCmp('frmConfirmPassword')
						.getValue();
				if (tmpPassword == '') {
					Ext.MessageBox.alert('��ʾ', '��������ҵ����Ա��¼����!');
					return;
				}
				if (tmpPassword != tmpConfirmPassword) {
					Ext.MessageBox.alert('��ʾ', '������ҵ����Ա��¼�������벻һ��,����������!');
					return;
				}

				// var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
				var patrn = /["',]/im;
				var emailfieldset1 = Ext.getCmp('emailfieldset1').collapsed;
				if (!emailfieldset1) {
					var email1 = Ext.getCmp('email1');
					if (Ext.util.Format.trim(email1.getValue()) == '') {
						Ext.MessageBox.alert('��ʾ', '����������1�������ַ!');
						return;
					}
					if (!email1.isValid()) {
						Ext.MessageBox.alert('��ʾ', '����1�������ַ��ʽ��������,����������!');
						return;
					}

					var contactName1 = Ext.util.Format.trim(Ext
							.getCmp('contactName1').getValue());
					if (contactName1 == '') {
						Ext.MessageBox.alert('��ʾ', '����������1����ϵ������!');
						return;
					}
					if (patrn.test(contactName1)) {
						Ext.MessageBox.alert('��ʾ', '����1����ϵ�������в��ܺ���"\',�ȷǷ��ַ�!');
						return;
					}

					var position1 = Ext.util.Format.trim(Ext
							.getCmp('position1').getValue());
					if (position1 == '') {
						Ext.MessageBox.alert('��ʾ', '����������1��ְ��!');
						return;
					}
					if (patrn.test(position1)) {
						Ext.MessageBox.alert('��ʾ', '����1��ְ���в��ܺ���"\',�ȷǷ��ַ�!');
						return;
					}
				}

				var emailfieldset2 = Ext.getCmp('emailfieldset2').collapsed;
				if (!emailfieldset2) {
					var email2 = Ext.getCmp('email2');
					if (Ext.util.Format.trim(email2.getValue()) == '') {
						Ext.MessageBox.alert('��ʾ', '����������2�������ַ!');
						return;
					}
					if (!email2.isValid()) {
						Ext.MessageBox.alert('��ʾ', '����2�������ַ��ʽ��������,����������!');
						return;
					}

					var contactName2 = Ext.util.Format.trim(Ext
							.getCmp('contactName2').getValue());
					if (contactName2 == '') {
						Ext.MessageBox.alert('��ʾ', '����������2����ϵ������!');
						return;
					}
					if (patrn.test(contactName2)) {
						Ext.MessageBox.alert('��ʾ', '����2����ϵ�������в��ܺ���"\',�ȷǷ��ַ�!');
						return;
					}

					var position2 = Ext.util.Format.trim(Ext
							.getCmp('position2').getValue());
					if (position2 == '') {
						Ext.MessageBox.alert('��ʾ', '����������2��ְ��!');
						return;
					}
					if (patrn.test(position2)) {
						Ext.MessageBox.alert('��ʾ', '����2��ְ���в��ܺ���"\',�ȷǷ��ַ�!');
						return;
					}
				}

				Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ��������ҵ��?', addConfirm);

			},
			iconCls : 'icon-save'
		});

function addConfirm(btn) {
	if (btn == 'yes') {
		Ext.Msg.show({
					msg : '���ڱ��� ���Ե�...',
					progressText : '����...',
					width : 300,
					wait : true,
					// waitConfig: {interval:200},
					icon : 'ext-mb-download'
				});
		add();
	}
}

// ���Ӳ���
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
						Ext.Msg.alert('��ʾ', '����ɹ�');
						return;
					} else if (res.result == 3) {
						// store.reload();
						Ext.Msg.alert('��ʾ', "�Ѵ�����ҵ����!");
						return;
					} else {
						// store.reload();
						Ext.Msg.alert('��ʾ', "����ʧ��!");
						return;
					}
				},
				failure : function(request) {
					Ext.Msg.alert('��ʾ', "����ʧ��!");
				}
			});
}

var simple = new Ext.FormPanel({
			region : 'center',
			labelWidth : 150, // label settings here cascade unless overridden
			// url:'save-form.php',
			frame : true,
			title : '������Ϣ',
			bodyStyle : 'padding:5px 5px 0',
			width : 200,
			defaults : {
				width : 330
			},
			defaultType : 'textfield',
			autoScroll : true,
			items : [{
						// xtype : 'textfield',
						fieldLabel : '��ҵ����',
						id : 'frmentCode',
						width : 150,
						allowBlank : false
					}, {
						// xtype : 'textfield',
						fieldLabel : '��ҵ����',
						id : 'frmentName',
						width : 150,
						allowBlank : false
					},/*
						 * ,{ //xtype : 'textfield', fieldLabel: '����û���', id:
						 * 'frmmaxUserNum', width: 150, allowBlank:false }
						 */{
						// xtype : 'textfield',
						fieldLabel : '�����ʺ�',
						id : 'frmsmsAccount',
						width : 150
					}, {
						fieldLabel : '��������',
						id : 'frmsmsPwd',
						inputType : 'password',
						width : 150
					}, {
						fieldLabel : '����ȷ������',
						id : 'frmConfirmsmsPwd',
						name : 'frmConfirmsmsPwd',
						inputType : 'password',
						width : 150
					}, {
						// xtype : 'textfield',
						fieldLabel : '��ҵ����Ա����',
						id : 'frmuserName',
						width : 150,
						allowBlank : false
					}, {
						// xtype : 'textfield',
						fieldLabel : '��ҵ����Ա��¼�ʺ�',
						id : 'frmuserAccount',
						width : 150,
						allowBlank : false
					}, {
						fieldLabel : '��ҵ����Ա��¼����',
						id : 'frmPassword',
						inputType : 'password',
						width : 150,
						allowBlank : false
					}, {
						fieldLabel : '��ҵ����Ա��¼ȷ������',
						id : 'frmConfirmPassword',
						inputType : 'password',
						width : 150,
						// vtype: 'password',
						// initialPassField: 'frmPassword', // id of the initial
						// password field
						allowBlank : false
					}, {
						fieldLabel : '��ҵ�ն�����',
						id : 'frmMaxUserNum',
						width : 150
					}, {
						xtype : 'datefield',
						fieldLabel : '������ʱ��',
						id : 'endTime',
						format : 'Y-m-d',
						editable : false,
						width : 150
					}, {
						id : 'smsType',
						xtype : 'combo',
						fieldLabel : '����ͨ��',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[1, '����ͨ'], [3, '����']]
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
						fieldLabel : '�Ƿ���Ҫ�ݷ�ͳ��',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '��'], [1, '��']]
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
						fieldLabel : '�Ƿ���Ҫ����������־',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '��'], [1, '��']]
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
						fieldLabel : 'ָ��ģ��',
						editable : false,
						width : 80,
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[0, '��'], [1, 'Ѯ'], [2, '��']]
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
						title : '����1',
						autoHeight : true,
						defaults : {
							width : 150
						},
						defaultType : 'textfield',
						collapsed : true,
						items : [{
									fieldLabel : '�����ַ',
									id : 'email1',
									vtype : 'email'
								}, {
									fieldLabel : '��ϵ��',
									id : 'contactName1'
								}, {
									fieldLabel : 'ְ��',
									id : 'position1'
								}]
					}, {
						xtype : 'fieldset',
						id : 'emailfieldset2',
						checkboxToggle : true,
						title : '����2',
						autoHeight : true,
						defaults : {
							width : 150
						},
						defaultType : 'textfield',
						collapsed : true,
						items : [{
									fieldLabel : '�����ַ',
									id : 'email2',
									vtype : 'email'
								}, {
									fieldLabel : '��ϵ��',
									id : 'contactName2'
								}, {
									fieldLabel : 'ְ��',
									id : 'position2'
								}]
					}, {
						fieldLabel : '��Ա����ʱ��(����)',
						id : 'persionGreyNum',
						width : 150,
						value: 15
					}, {
						fieldLabel : '���ػҵ�ʱ��(����)',
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
					header : '���',
					width : 40
				}), {
			header : "��ҵ����",
			width : 100,
			sortable : true,
			dataIndex : 'entCode'
		}, {
			header : "��ҵ����",
			width : 100,
			sortable : true,
			dataIndex : 'entName'
		}, {
			header : "����û���",
			width : 100,
			sortable : true,
			dataIndex : 'maxUserNum'
		}, {
			header : "�����ʺ�",
			width : 100,
			sortable : true,
			dataIndex : 'smsAccount'
		},
		// {header: "����ʱ��", width: 100, sortable: true, dataIndex: 'endTime'},
		// {header: "��������", width: 100, sortable: true, dataIndex:
		// 'smsPwd',hidden:true},
		{
			header : "������ʱ��",
			width : 100,
			sortable : true,
			dataIndex : 'endTime',
			renderer : function(val) {
				if (val == 'null')
					return '';
				return val;
			}
		}, {
			header : "״̬",
			width : 100,
			sortable : true,
			dataIndex : 'entStatus',
			renderer : function(val) {
				if (val == '0') {
					return 'ͣ��';
				} else if (val == '1') {
					return '����';
				} else if (val == '2') {
					return '�������';
				}
				return val;
			}
		}, {
			header : "����ͨ��",
			width : 100,
			sortable : true,
			dataIndex : 'smsType',
			renderer : function(val) {
				if (val == 'null') {
					return '';
				} else if (val == '1') {
					return '����ͨ';
				} else if (val == '3') {
					return '����';
				}
				return val;
			}
		}, {
			header : "�ݷ�ͳ��",
			width : 100,
			sortable : true,
			dataIndex : 'visitTjStatus',
			renderer : function(val) {
				if (val == 'null') {
					return '��';
				} else if (val == '0') {
					return '��';
				} else if (val == '1') {
					return '��';
				}
				return val;
			}
		}, {
			header : "������־",
			width : 100,
			sortable : true,
			dataIndex : 'reportStatus',
			renderer : function(val) {
				if (val == 'null') {
					return '��';
				} else if (val == '0') {
					return '��';
				} else if (val == '1') {
					return '��';
				}
				return val;
			}
		}, {
			header : "��Ա����ʱ��(����)",
			width : 100,
			sortable : true,
			dataIndex : 'persionGreyInterval'
		}, {
			header : "���ػҵ�ʱ��(����)",
			width : 100,
			sortable : true,
			dataIndex : 'carGreyInterval'
		}];

var userGrid = new Ext.grid.GridPanel({
	region : 'west',
	width : 450,
	loadMask : {
		msg : '��ѯ��...'
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
				displayMsg : '��{0}����{1}������ ��{2}��',
				emptyMsg : "û������"
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
				text : '��ѯ',
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