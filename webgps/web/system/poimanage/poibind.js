var bind_deviceId = [];
var bind_poiid = [];
var last_year = new Date();
var vselectGid;
var terminalGroupIds = '';
var tGroups = '';

last_year.setFullYear(last_year.getFullYear() - 1);
var startTime = (last_year).pattern("yyyy-M-d") + " 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d") + " 23:59:59";
var tmpDeviceIdField;
var locDescription;
var poiDescription;
var layer;
var bindingStatus;
var terminalName;
var terminalGroup;
var layer4Export;

var delbut = new Ext.Action({
	text : '��',
	id : 'delbut',
	// disabled : true,
	handler : function() {
		bind_deviceId = [];
		getTreeId(root, bind_deviceId);
		if (bind_deviceId.length <= 0) {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ն�!');
			return;
		}
//		if (bind_deviceId.toString().split(",").length>10) {
//			parent.Ext.MessageBox.alert('��ʾ', 'һ�����ѡ��10���ն�');
//			return;
//		}
		var sel_arr = poiGrid.selModel.getSelections();
		if (sel_arr.length <= 0) {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ע��!');
			return;
		}
		bind_poiid = [];
		for ( var i = 0; i < sel_arr.length; i++) {
			bind_poiid.push(sel_arr[i].get('id'));
		}
		parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ����?', bindConfirm);
	},
	iconCls : 'icon-modify'
});

function bindConfirm(btn) {
	if (btn == 'yes') {
		parent.Ext.Msg.show({
			msg : '���ڱ��� ���Ե�...',
			progressText : '����...',
			width : 300,
			wait : true,
			// waitConfig: {interval:200},
			icon : 'ext-mb-download'
		});
		bind();
	}
}

function bind() {
	Ext.Ajax.request({
		url : path + '/poi/poi.do?method=updatePoiRefTermForAdd',
		method : 'POST',
		params : {
			poiIds : bind_poiid.toString(),
			deviceIds : bind_deviceId.toString()
		},
		timeout : 600000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			// alert(request.responseText);
			if (res.result == 1) {

				parent.Ext.Msg.alert('��ʾ', '�󶨳ɹ�');
				loadPoiStore();
				return;
			} else {
				// store.reload();
				parent.Ext.Msg.alert('��ʾ', "��ʧ��!");
				return;
			}
		},
		failure : function(request) {
			parent.Ext.Msg.alert('��ʾ', "��ʧ��!");
			return;
		}
	});
}

var removeBinding = new Ext.Action({
	text : '�����',
	id : 'removeBinding',
	// disabled : true,
	handler : function() {
		bind_deviceId = [];
		getTreeId(root, bind_deviceId);
		if (bind_deviceId.length <= 0) {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ն�!');
			return;
		}

		var sel_arr = poiGrid.selModel.getSelections();
		if (sel_arr.length <= 0) {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ��󶨵ı�ע��!');
			return;
		}
		bind_poiid = [];
		for ( var i = 0; i < sel_arr.length; i++) {
			bind_poiid.push(sel_arr[i].get('id'));
		}
		parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�������?', function(btn) {
			if (btn == 'yes') {
				parent.Ext.Msg.show({
					msg : '���ڱ��� ���Ե�...',
					progressText : '����...',
					width : 300,
					wait : true,
					icon : 'ext-mb-download'
				});
				Ext.Ajax.request({
					url : path + '/poi/poi.do?method=deletePoiRefTerm',
					method : 'POST',
					params : {
						poiIds : bind_poiid.toString(),
						deviceIds : bind_deviceId.toString()
					},
					timeout : 600000,
					success : function(request) {
						var res = Ext.decode(request.responseText);
						if (res.result == 1) {

							parent.Ext.Msg.alert('��ʾ', '����󶨳ɹ�');
							loadPoiStore();
							return;
						} else {
							parent.Ext.Msg.alert('��ʾ', "�����ʧ��!");
							return;
						}
					},
					failure : function(request) {
						parent.Ext.Msg.alert('��ʾ', "�����ʧ��!");
						return;
					}
				});
			}
		});
	},
	iconCls : 'icon-del'
});

// ����poi id�������Ѱ��ն�
function queryPoi(id) {

	Ext.Ajax.request({
		// url :url,
		url : path + '/poi/poi.do?method=queryPoi',
		method : 'POST',
		params : {
			id : id
		},
		// timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			// alert(request.responseText);
			if (res.deviceIds != undefined) {

				var deviceIdfrm = res.deviceIds;
				if (deviceIdfrm.length > 0) {
					clearDevice(root);
					selDevice(root, ',' + deviceIdfrm + ',');
					Ext.Msg.hide();
				} else {
					Ext.Msg.alert('��ʾ', '�ñ�ע��δ����!');
				}
			} else {
				Ext.Msg.alert('��ʾ', '�ñ�ע��δ����!');
			}
		},
		failure : function(request) {
		}
	});
}

function clearDevice(node) {
	node.eachChild(function(child) {
		child.ui.toggleCheck(false);
		child.attributes.checked = false;
		clearDevice(child);
	});
}

function selDevice(node, deviceId) {
	node.eachChild(function(child) {
		var tmpidArr = child.id.split('@#');
		if (tmpidArr.length > 0) {
			var tmp = ',' + tmpidArr[0] + ',';
			/*
			 * alert(tmp); alert(deviceId); alert(deviceId.indexOf(tmp));
			 */
			if (deviceId.indexOf(tmp) != -1) {
				child.ui.toggleCheck(true);
				child.attributes.checked = true;
			} else {
				child.ui.toggleCheck(false);
				child.attributes.checked = false;
			}
		}
		selDevice(child, deviceId);
		// child.fireEvent('checkchange',child,checked);
	});
}

// ȡ�ù�ѡ�ն�
function getTreeId(node, treeArr) {
	// node.expand();
	// 15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if (idArr.length > 1) {
		if (node.isLeaf() && node.getUI().isChecked()) {
			// treeArr[treeArr.length] = node;
			treeArr.push(idArr[0]);
		}
	}
	node.eachChild(function(child) {
		getTreeId(child, treeArr);
	});
}

var loader = new Ext.tree.TreeLoader({
	dataUrl : path
			+ '/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'
});

var root = new Ext.tree.AsyncTreeNode({
	text : '��λƽ̨',
	id : '-100',
	draggable : false
// ���ڵ㲻�����϶�
});

var api = new Ext.tree.TreePanel({
	region : 'west',
	rootVisible : false,
	lines : true,
	autoScroll : true,
	width : 220,
	loader : loader,
	root : root,
	listeners : {
		click : function(node) {
			var id = node.id;
			var id_arr = id.split('@#');
			if (id_arr.length > 2) {
				Ext.getCmp('terminalName').setValue(node.text);
				// var binding = document.getElementById("bindingStatus");
				// if (binding.value == '����' || binding.value == '') {
				// binding.value = "�Ѱ�";
				// }

				// var deviceId = id_arr[0];
				// Ext.Msg.show({
				// msg : '���ڲ�ѯ ���Ե�...',
				// progressText : '��ѯ��...',
				// width : 300,
				// wait : true,
				// waitConfig: {interval:200},
				// icon : 'ext-mb-download'
				// });
				// queryTerminalPoi(deviceId);
			}
		}
	}
});

function queryTerminalPoi(deviceId) {
	poiGrid.selModel.clearSelections();
	Ext.Ajax.request({
		// url :url,
		// url:path+'/poi/poi.do?method=queryPoiByDeviceId',
		url : path + '/poi/poi.do?method=queryManagePoiByDeviceId',
		method : 'POST',
		params : {
			deviceId : deviceId
		},
		// timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			var poiids = '';
			if (res.data.length > 0) {

				for ( var i = 0; i < res.data.length; i++) {
					poiids += res.data[i].id + ',';
				}
				poiids = ',' + poiids;
				// alert(poiids);
				var count = poistore.getCount();
				var poiid_arr = [];
				for ( var i = 0; i < count; i++) {
					var tmp_record = poistore.getAt(i);
					var poiid = tmp_record.get('id');
					// alert(poiid);
					// alert(poiids.indexOf((','+poiid+',')));
					if (poiids.indexOf((',' + poiid + ',')) != -1) {
						poiid_arr.push(i);
					}
					// poistore.removeAt(i);
					// poistore.insert(0,tmp_record);
				}
				// alert(poiid_arr);

				poiGrid.selModel.selectRows(poiid_arr, false);
				if (poiid_arr.length <= 0) {
					Ext.Msg.alert('��ʾ', 'δ�󶨱�ע��!');
				} else {
					Ext.Msg.hide();
				}
			} else {

				Ext.Msg.alert('��ʾ', 'δ�󶨱�ע��!');
			}

		},
		failure : function(request) {
		}
	});
}

/* ͼ���б� */
var poiproxy = new Ext.data.HttpProxy({
	// url : path + '/poi/poi.do?method=listPoi'
	url : path + '/poi/poi.do?method=listMatchingPois'
});

var poireader = new Ext.data.JsonReader({
	totalProperty : 'total',
	successProperty : 'success',
	idProperty : 'id',
	root : 'data'
}, [ {
	name : 'id'
}, {
	name : 'poiType'
},// poi����0��1��2��
{
	name : 'poiDatas'
},// poi����
{
	name : 'poiEncryptDatas'
},// poi��������
{
	name : 'iconpath'
}, {
	name : 'poiName'
}, {
	name : 'poiDesc'
}, {
	name : 'address'
}, {
	name : 'telephone'
}, {
	name : 'locDesc'
}, {
	name : 'termName'
}, {
	name : 'vehicleNumber'
}, {
	name : 'layerId'
}, {
	name : 'layerName'
}, {
	name : 'visitDistance'
}, {
	name : 'cDate'
} ]);

var poistore = new Ext.data.Store({
	id : 'poistore',
	restful : true, // <-- This Store is RESTful
	proxy : poiproxy,
	reader : poireader,
	listeners : {
		beforeload : {
			fn : function(thiz, options) {
				this.baseParams = {
					searchValue : tmpDeviceIdField,
					startTime : startTime,
					endTime : endTime,
					poiDescription : poiDescription,
					locDescription : locDescription,
					layer : layer,
					bindingStatus : bindingStatus,
					terminalName : terminalName,
					terminalGroupIds : tGroups
				};
			}
		}
	}
});

var poism = new Ext.grid.CheckboxSelectionModel({
	handleMouseDown : function(SelectionModel, rowIndex) {
		// Ext.Msg.show({
		// msg : '���ڲ�ѯ ���Ե�...',
		// progressText : '��ѯ��...',
		// width : 300,
		// wait : true,
		// waitConfig: {interval:200},
		// icon : 'ext-mb-download'
		// });
		poiGrid.selModel.selectRow(rowIndex, true);
		// var poiid = poistore.getAt(rowIndex).get('id');
		// queryPoi(poiid);
	}
// rowselect : function(SelectionModel, rowIndex){
// poiGrid.selModel.selectRow(rowIndex, true);
// Ext.emptyFn
// }
});

var poiColumns = [
		poism,
		new Ext.grid.RowNumberer({
			header : '���',
			width : 40
		}),
		{
			id : 'id',
			header : "id",
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		},
		{
			header : "poiType",
			width : 100,
			sortable : true,
			dataIndex : 'poiType',
			hidden : true
		},
		{
			header : "poiDatas",
			width : 100,
			sortable : true,
			dataIndex : 'poiDatas',
			hidden : true
		},
		{
			header : "poiEncryptDatas",
			width : 100,
			sortable : true,
			dataIndex : 'poiEncryptDatas',
			hidden : true
		},
		{
			header : "iconpath",
			width : 100,
			sortable : true,
			dataIndex : 'iconpath',
			hidden : true
		},
		{
			header : "��ע����",
			width : 130,
			sortable : true,
			dataIndex : 'poiName',
			renderer : function tips(val) {
				var tmp = '';
				if (val.indexOf('_recv') != -1) {
					val = val.substring(0, val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				} else {
					tmp = '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
				return tmp;
			}
		},
		{
			id : 'poiDesc',
			header : "��ע����",
			width : 130,
			sortable : true,
			dataIndex : 'poiDesc',
			renderer : function tips(val) {
				var tmp = '';
				if (val.indexOf('_recv') != -1) {
					val = val.substring(0, val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				} else {
					tmp = '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
				return tmp;
			}
		},
		{
			header : "λ������",
			width : 160,
			sortable : true,
			dataIndex : 'locDesc'
		},
		{
			header : "��ע��ַ",
			width : 130,
			sortable : true,
			dataIndex : 'address',
			renderer : function tips(val) {
				var tmp = '';
				if (val.indexOf('_recv') != -1) {
					val = val.substring(0, val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				} else {
					tmp = '<span style="display:table;width:100%;" qtip="'
							+ val + '">' + val + '</span>';
				}
				return tmp;
			}
		}, {
			header : "��ע�绰",
			width : 100,
			sortable : true,
			dataIndex : 'telephone'
		}, {
			header : "vehicleNumber",
			width : 100,
			sortable : true,
			dataIndex : 'vehicleNumber',
			hidden : true
		}, {
			header : "layerId",
			width : 100,
			sortable : true,
			dataIndex : 'layerId',
			hidden : true
		}, {
			header : "����ͼ��",
			width : 90,
			sortable : true,
			dataIndex : 'layerName'
		}, {
			header : "��ע��Χ",
			width : 90,
			sortable : true,
			dataIndex : 'visitDistance'
		}, {
			header : "��ע��",
			width : 90,
			sortable : true,
			dataIndex : 'termName'
		}, {
			header : "����ʱ��",
			width : 130,
			sortable : true,
			dataIndex : 'cDate'
		} ];

/**
 * �û���������
 */
var comboxTreeRoot = new Ext.tree.AsyncTreeNode({
	text : '���в���',
	id : '-100',
	draggable : false
// ���ڵ㲻�����϶�
});

var treeLoader = new Ext.tree.TreeLoader({
//	dataUrl : path
//			+ '/manage/termGroupManage.do?actionMethod=groupListForUserCombox'
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForUserCombox&entCode='+encodeURI(entCode)+"&userId="+userId
});

var tree = new Ext.tree.TreePanel({
	loader : treeLoader,
	border : false,
	rootVisible : true,
	root : comboxTreeRoot
});

/**
 * �����б���
 */
var comboxWithTree = new Ext.form.ComboBox(
		{
			store : new Ext.data.SimpleStore({
				fields : [ 'abbr', 'state' ],
				data : [ [] ]
			}),
			editable : false,
			width : 135,
			mode : 'local',
			triggerAction : 'all',
			id : 'terminalGroup',
			displayField : 'abbr',
			emptyText : 'ѡ����',
			maxHeight : 200,
			tpl : "<tpl for='.'><div style='height:200px'><div id='tree'></div></div></tpl>",
			selectedClass : '',
			onSelect : Ext.emptyFn
		});

var layersStore = new Ext.data.Store({
	id : 'layersStore',
	proxy : new Ext.data.HttpProxy({
		url : path + '/poi/poi.do?method=listLayerIds'
	}),
	reader : new Ext.data.ArrayReader({}, Ext.data.Record.create([ {
		name : 'id',
		type : 'string'
	}, {
		name : 'name',
		type : 'string'
	} ])),
	listeners : {
		load : function(thiz, records, options) {
			thiz.insert(0, new Ext.data.Record({
				name : '&nbsp;',
				id : ''
			}));
		}
	}
});

var poiGrid = new Ext.grid.GridPanel({
	region : 'center',
	// width: 450,
	loadMask : {
		msg : '��ѯ��...'
	},
	enableColumnHide : false,
	store : poistore,
	// plugins: [editor],
	columns : poiColumns,
	sm : poism,
	// sm : smcheckbox,
	margins : '0 0 0 0',
	tbar : new Ext.Toolbar({
		items : [

		new Ext.form.Label({
			text : '��ע����'
		}), new Ext.form.TextField({
			id : 'DeviceIdField',
			width : 100

		}), '-', new Ext.form.Label({
			text : '��ע����'
		}), new Ext.form.TextField({
			id : 'poiDescription',
			width : 135
		// height:30
		}), '-', new Ext.form.Label({
			text : '����ͼ��'
		}), new Ext.form.ComboBox({
			id : 'layer',
			enableKeyEvents : true,
			// name : 'layer',
			valueField : "id",
			displayField : "name",
			mode : 'local',// �������ڱ���
			forceSelection : true,
			editable : true,
			triggerAction : 'all',
			width : 100,
			store : layersStore,
			listeners : {
				select : function(thiz, re, index) {
					layer = encodeURI(re.get('id'));
					if (re.get('name') == '&nbsp;') {
						thiz.setValue(null);
					}
				}
			}

		}), '-', new Ext.form.Label({
			text : 'λ������'
		}), new Ext.form.TextField({
			id : 'locDescription',
			width : 100
		// height:30
		}) ]

	}),
	listeners : { // ��tbar2��Ⱦ��tbar���棬ͨ��listeners�¼�
		'render' : function() {
			tbar2.render(this.tbar);
			tbar3.render(this.tbar);
		}
	},
	bbar : new Ext.PagingToolbar({
		pageSize : 200,
		store : poistore,
		displayInfo : true,
		displayMsg : '��ʾ�� {0} ���� {1} ����¼��һ�� {2} ��',
		emptyMsg : "û�м�¼"
	}),
	view : new Ext.ux.grid.BufferView({
		// custom row height
		// rowHeight: 34,
		// render rows as they come into viewable area.
		scrollDelay : false
	})
});

var tbar2 = new Ext.Toolbar({

	items : [ new Ext.form.Label({
		text : '�ն�����'
	}), new Ext.form.TextField({
		id : 'terminalName',
		width : 100,
		listeners : {
			change : function(thiz, nv, ov) {
				if (nv != '') {
					Ext.getCmp('bindingStatus').setValue(1);
				}
			}
		}
	// height:30
	}), '-', new Ext.form.Label({
		text : '��������'
	}), comboxWithTree, '-', new Ext.form.Label({
		text : '��ʼʱ��'
	}), new Ext.form.DateField({
		id : 'startDateField',
		fieldLabel : 'Date',
		format : 'Y-m-d',
		width : 100,
		editable : false,
		value : last_year
	}), '-', new Ext.form.Label({
		text : '��ֹʱ��'
	}), new Ext.form.DateField({
		id : 'endDateField',
		fieldLabel : 'Date',
		format : 'Y-m-d',
		width : 100,
		editable : false,
		value : new Date()
	}) ]
});

var bindingStatusStore = new Ext.data.SimpleStore({
	fields : [ "displayText", "returnValue" ],
	data : [ [ '����', '0' ], [ '�Ѱ�', '1' ], [ 'δ��', '-1' ] ]
});

var tbar3 = new Ext.Toolbar({
	items : [
			new Ext.form.Label({
				text : '�󶨹�ϵ'
			}),
			new Ext.form.ComboBox({
				id : 'bindingStatus',
				enableKeyEvents : true,
				name : 'bindingStatus',
				valueField : "returnValue",
				displayField : "displayText",
				mode : 'local',// �������ڱ���
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				width : 100,
				store : bindingStatusStore,
				listeners : {
					select : function(index) {
						bindingStatus = index.getValue();
					}
				}

			}),
			'-',
			new Ext.Action({
				text : '��ѯ',
				handler : function() {
					tGroups = terminalGroupIds;
					tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
					tmpDeviceIdField = encodeURI(tmpDeviceIdField);
					startTime = Ext.getCmp('startDateField').getValue().format(
							'Y-m-d')
							+ ' 00:00:00';
					endTime = Ext.getCmp('endDateField').getValue().format(
							'Y-m-d')
							+ ' 23:59:59';
					locDescription = encodeURI(Ext.getCmp('locDescription')
							.getValue());
					poiDescription = encodeURI(Ext.getCmp('poiDescription')
							.getValue());
					// alert(bindingStatus);
					terminalName = encodeURI(Ext.getCmp('terminalName')
							.getValue());
					layer4Export = layer;
					loadPoiStore();

				},
				iconCls : 'icon-search'
			}), '-', delbut, '-', removeBinding, '-', new Ext.Action({
				text : '����',
				handler : function() {

					exportExcel();
					// Ext.MessageBox.confirm('��ʾ', '������ѯ���?',
					// function(btn) {
					// if (btn == 'yes') {
					// }
					// });
				},
				iconCls : 'icon-excel'
			}) ]
});

function loadPoiStore() {
	poistore.load({
		params : {
			start : 0,
			limit : 200,
			searchValue : tmpDeviceIdField,
			poiDescription : poiDescription,
			locDescription : locDescription,
			layer : layer,
			bindingStatus : bindingStatus,
			terminalName : terminalName,
			terminalGroupIds : tGroups,
			startTime : startTime,
			endTime : endTime
		}
	});
}

var viewport;

function exportExcel() {

	var dlForm = Ext.get('dlForm');

	if (!dlForm) {
		dlForm = Ext.DomHelper.append(Ext.getBody(), {
			tag : 'form',
			method : 'post',
			id : 'dlForm',
			name : 'excelform',
			taget : '_self',
			action : path + '/poi/poi.do?method=listMatchingPois',
			cls : 'x-hidden',
			cn : [ {
				tag : 'input',
				id : 'expExcel',
				name : 'expExcel',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'searchValueIn',
				name : 'searchValue',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'poiDescriptionIn',
				name : 'poiDescription',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'locDescriptionIn',
				name : 'locDescription',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'layerIn',
				name : 'layer',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'bindingStatusIn',
				name : 'bindingStatus',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'terminalNameIn',
				name : 'terminalName',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'terminalGroupIdsIn',
				name : 'terminalGroupIds',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'startTimeIn',
				name : 'startTime',
				type : 'hidden'
			}, {
				tag : 'input',
				id : 'endTimeIn',
				name : 'endTime',
				type : 'hidden'
			} ]
		}, true);
		dlForm.child('#expExcel').set({
			value : true
		});
	}
	dlForm.child('#searchValueIn').set({
		value : tmpDeviceIdField
	});
	dlForm.child('#poiDescriptionIn').set({
		value : poiDescription
	});
	dlForm.child('#locDescriptionIn').set({
		value : locDescription
	});
	dlForm.child('#layerIn').set({
		value : layer4Export
	});
	dlForm.child('#bindingStatusIn').set({
		value : bindingStatus
	});
	dlForm.child('#terminalNameIn').set({
		value : terminalName
	});
	dlForm.child('#terminalGroupIdsIn').set({
		value : terminalGroupIds
	});
	dlForm.child('#startTimeIn').set({
		value : startTime
	});
	dlForm.child('#endTimeIn').set({
		value : endTime
	});
	dlForm.dom.submit();

}

function getChildTermGroups(node) {
	var tmpidArr = node.id.split('@');
	if (tmpidArr[0] == '-101' || tmpidArr[0] == '-100') {
		return;
	} else {
		terminalGroupIds = tmpidArr[0] + ';' + terminalGroupIds;
	}
	var roonodes = node.childNodes;
	// �ӽڵ���ȡ���ӽڵ����α���
	for ( var i = 0; i < roonodes.length; i++) {
		getChildTermGroups(roonodes[i]);
	}
}

Ext.onReady(function() {
	var loadMask = new Ext.LoadMask(Ext.getBody(), {
		msg : "�����У����Ժ� ..."
	});
	loadMask.show();
	Ext.QuickTips.init();

	tmpDeviceIdField = '';
	locDescription = '';
	poiDescription = '';
	layer = '';
	bindingStatus = '';
	terminalName = '';
	terminalGroup = '';
	layer4Export = '';

	layersStore.load({
		params : {
			start : 0,
			limit : 100
		}
	});

	// poistore.load({
	// params : {
	// start : 0,
	// limit : 200
	// }
	// });

	viewport = new Ext.Viewport({
		layout : 'border',
		items : [ api, poiGrid ]
	});
	root.expand(true, false, function() {
		loadMask.hide();
	});

	api.on('checkchange', function(node, checked) {

		node.expand();
		node.attributes.checked = checked;

		node.on('expand', function(node) {
			node.eachChild(function(child) {
				child.ui.toggleCheck(checked);
				child.attributes.checked = checked;
				child.fireEvent('checkchange', child, checked);
			});
		});

		node.eachChild(function(child) {
			// alert('2:'+child);
			child.ui.toggleCheck(checked);
			child.attributes.checked = checked;
			child.fireEvent('checkchange', child, checked);
		});
	}, api);

	tree.on('click', function(node) {
		terminalGroupIds = '';
		comboxWithTree.setValue(node.text);
		getChildTermGroups(node);

		var len = terminalGroupIds.length;
		if (len > 0) {
			terminalGroupIds = terminalGroupIds.substring(0, len - 1);
		}

		var tmpidArr = node.id.split('@');
		if (tmpidArr[0] == '-101') {
			vselectGid = '-1';
		} else {
			vselectGid = tmpidArr[0];
		}
		comboxWithTree.collapse();
	});

	comboxWithTree.on('expand', function() {
		tree.render('tree');
		comboxTreeRoot.expand(true, true);
		// �ı����overflow����ʽ����ʾ����Ĺ�����
		this.innerList.dom.style.overflowX = "auto";
	});

	comboxWithTree.render('comboxWithTree');

	Ext.getCmp('bindingStatus').setValue(1);
});
