//ѡ��Ҫ�޸ĵ�ͼ��,ҳ�水ť
var delbut = new Ext.Action({
	text : '�޸�',
	id : 'delbut',
	//disabled : true,
	handler : function() {
		var tmpselArr = layersm.getSelected();
		if (tmpselArr == undefined) {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ�޸ĵ�ͼ��!');
			return;
		}
		var tmpsel = layersm.getSelected();
		Ext.getCmp('layeridfrm').setValue(tmpsel.get('id'));
		Ext.getCmp('layerNamefrm').setValue(tmpsel.get('layerName'));
		Ext.getCmp('layerDescfrm').setValue(tmpsel.get('layerDesc'));
		Ext.getCmp('mapLevelfrm').setValue(tmpsel.get('mapLevel'));
		Ext.getCmp('visiblefrm').setValue(tmpsel.get('visible'));
		
		store.load({params : {start : 0,limit : 100}});
		viewport.layout.setActiveItem(modifyPanel);
		
		//parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�����ͼ����?', addConfirm);
	},
	iconCls : 'icon-modify'
});

//����ͼ��id��ѯ�ɼ��û�
function queryLayer(){
	Ext.Ajax.request({
		url : path + '/poi/poi.do?method=queryLayer',
		method : 'POST',
		params : {id : Ext.getCmp('layeridfrm').getValue()},
		//timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			if (res.visibleUserIds != undefined) {
				//��ѯ��ͼ��ɼ��û�id
				var tmpidArr = res.visibleUserIds.split(',');
				var tmpCount = store.getCount();
				for(var i=0;i<tmpidArr.length;i++){
					for(var j=0;j<tmpCount;j++){
						var tmprecord = store.getAt(j);
						var tmpuserid = tmprecord.get('id');
						if(tmpuserid == tmpidArr[i]){
							sm.selectRow(j,true);
						}
					}
				}
			}
		},
		failure : function(request) {
			//parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		}
	});
}

//�޸�ҳ�水ť
var toolbar = new Ext.Toolbar({
	id : 'frmtbar',
	items : [new Ext.Action({
				text : '�޸�',
				id : 'modifybut',
				//disabled : true,
				handler : function() {
					var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}
		        	
		        	//var tmpselArr = sm.getSelections();
		        	//if(tmpselArr.length<=0){
		        	//	parent.Ext.MessageBox.alert('��ʾ', '��ѡ��ɼ��û�!');
		        	//	return ;
		        	//}

		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸Ĵ�ͼ����?', addConfirm);
		        
				},
				iconCls : 'icon-modify'
			}), new Ext.Action({
				text : '����',
				id : 'backbut',
				//disabled : true,
				handler : function() {
					//layerstore.reload();
			 		var cursor = layerGrid.getBottomToolbar().cursor;
			 		layerstore.load({params:{start:cursor, limit:20 }});
					viewport.layout.setActiveItem(layerGrid);

				},
				iconCls : 'icon-back'
			})]
});

function addConfirm(btn) {
	if (btn == 'yes') {
		parent.Ext.Msg.show({
					msg : '���ڱ��� ���Ե�...',
					progressText : '����...',
					width : 300,
					wait : true,
					//waitConfig: {interval:200},
					icon : 'ext-mb-download'
				});
		add();
	}
}

//�޸�ͼ��
function add() {
	var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
	var tmpselArr = sm.getSelections();
	var ids = '';
	for (var i = 0; i < tmpselArr.length; i++) {
		ids += tmpselArr[i].get('id') + ',';
	}
	
	if(ids.length > 0){
		ids = ids.substring(0, ids.length - 1);
	}
	
	var layerDescfrm = Ext.getCmp('layerDescfrm').getValue();
	var visiblefrm = Ext.getCmp('visiblefrm').getValue().getGroupValue();
	var mapLevelfrm = Ext.getCmp('mapLevelfrm').getValue();
	layerNamefrm = encodeURI(layerNamefrm);
	layerDescfrm = encodeURI(layerDescfrm);
	var tmpid = Ext.getCmp('layeridfrm').getValue();
	
	Ext.Ajax.request({
				//url :url,
				url : path + '/poi/poi.do?method=updateLayer',
				method : 'POST',
				params : {
					layerName : layerNamefrm,
					layerDesc : layerDescfrm,
					visible : visiblefrm,
					mapLevel : mapLevelfrm,
					userIdss : ids,
					id : tmpid
				},
				//timeout : 10000,
				success : function(request) {
					var res = Ext.decode(request.responseText);
					//alert(request.responseText);
					if (res.result == 1) {
						//treeload.load(root);
						//store.reload();
						parent.Ext.Msg.alert('��ʾ', '�޸ĳɹ�');
						return;
					} else {
						//store.reload();
						parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
						return;
					}
				},
				failure : function(request) {
					parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
				}
			});
}

var simple = new Ext.FormPanel({
			region : 'west',
			labelWidth : 100, // label settings here cascade unless overridden
			//url:'save-form.php',
			frame : true,
			//title: '������Ϣ',
			bodyStyle : 'padding:5px 5px 0',
			width : 300,
			defaults : {
				width : 180
			},
			defaultType : 'textfield',
			items : [{
						//xtype : 'textfield',
						fieldLabel : 'ͼ������',
						id : 'layerNamefrm',
						width : 150,
						allowBlank : false
					}, {
						//xtype : 'textfield',
						fieldLabel : 'ͼ������',
						id : 'layerDescfrm',
						width : 300
						
					}, {
						xtype : 'radiogroup',
						fieldLabel : '�Ƿ�ɼ�',
						width : 80,
						id : 'visiblefrm',
						items : [{
									boxLabel : '��',
									name : 'isShow',
									inputValue : 1,
									checked : true
								}, {
									boxLabel : '��',
									name : 'isShow',
									inputValue : 0
								}]
					}, {
						id : 'mapLevelfrm',
						xtype : 'combo',
						fieldLabel : 'ͼ���ͼ����',
						editable : false,
						width : 80,
						displayField : 'name',
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[11,'��'],[13,'��'],[14,'��'],[16,'�ֵ�']]
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						mode : 'local',
						forceSelection : true,
						triggerAction : 'all',
						value : 16,
						selectOnFocus : true
					}, {
						xtype : 'hidden',
						id : 'layeridfrm'
					}],
			buttons : toolbar
		});

var proxy = new Ext.data.HttpProxy({
			url : path + '/system/user.do?method=listUserWithOutLoginUser'
		});

var reader = new Ext.data.JsonReader({
			totalProperty : 'total',
			successProperty : 'success',
			idProperty : 'id',
			root : 'data'
		}, [{
					name : 'id'
				}, {
					name : 'userName'
				}, {
					name : 'userAccount'
				}, {
					name : 'province'
				}, {
					name : 'city'
				}, {
					name : 'createDate'
				}, {
					name : 'password'
				}]);

var store = new Ext.data.Store({
			id : 'userstore',
			restful : true, // <-- This Store is RESTful
			proxy : proxy,
			reader : reader
		});

var sm = new Ext.grid.CheckboxSelectionModel({});

var userColumns = [sm, new Ext.grid.RowNumberer({
					header : '���',
					width : 40
				}), {
			id : 'id',
			header : "id",
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		}, {
			header : "�û�����",
			width : 100,
			sortable : true,
			dataIndex : 'userName'
		}, {
			header : "��¼�ʺ�",
			width : 100,
			sortable : true,
			dataIndex : 'userAccount'
		}, {
			header : "ʡ",
			width : 100,
			sortable : true,
			dataIndex : 'province'
		}, {
			header : "��",
			width : 100,
			sortable : true,
			dataIndex : 'city'
		}, {
			header : "����ʱ��",
			width : 100,
			sortable : true,
			dataIndex : 'createDate'
		}, {
			header : "password",
			width : 40,
			sortable : true,
			dataIndex : 'password',
			hidden : true
		}];

var userGrid = new Ext.grid.GridPanel({

	region : 'center',
	width : 450,
	//iconCls: 'icon-grid',
	//frame: true,
	title : '�ʺ��б�',
	//autoExpandColumn: 'name',
	enableColumnHide : false,
	store : store,
	//plugins: [editor],
	columns : userColumns,
	sm : sm,
	//sm : smcheckbox,
	margins : '0 0 0 0',
	bbar : new Ext.PagingToolbar({
				pageSize : 100,
				store : store,
				displayInfo : true,
				displayMsg : '��{0}����{1}������ ��{2}��',
				emptyMsg : "û������"
			})
		/*tbar: [
	    delbut
	]*/
	});

var modifyPanel = new Ext.Panel({
	layout : 'border',
	items : [simple, userGrid]
});
/*ͼ���б�*/
var layerproxy = new Ext.data.HttpProxy({
	url : path + '/poi/poi.do?method=listLayer'
});

var layerreader = new Ext.data.JsonReader({
			totalProperty : 'total',
			successProperty : 'success',
			idProperty : 'id',
			root : 'data'
		}, [{name : 'id'}, 
			{name : 'layerName'}, 
			{name : 'layerDesc'}, 
			{name : 'mapLevel'}, 
			{name : 'visible'}
			]);

var layerstore = new Ext.data.Store({
			id : 'layerstore',
			restful : true, // <-- This Store is RESTful
			proxy : layerproxy,
			reader : layerreader
		});

var layersm = new Ext.grid.RowSelectionModel({singleSelect : true});

function visible(val) {
	if (val == '0') {
		return '���ɼ�';
	} else if (val == '1') {
		return '�ɼ�';
	}
}

function layermaplevel(val){
	if (val == '11') {
		return '��';
	} else if (val == '13') {
		return '��';
	} else if (val == '14') {
		return '��';
	}else if (val == '16') {
		return '�ֵ�';
	}
}

var layerColumns = [
//layersm,
		new Ext.grid.RowNumberer({header : '���',width : 40
		}), {
			id : 'id',
			header : "id",
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		}, {
			header : "ͼ������",
			width : 150,
			sortable : true,
			dataIndex : 'layerName',
			renderer: function tips(val) {
				      	var tmp = '';
				        if(val.indexOf('_recv')!=-1){
							val = val.substring(0,val.indexOf('_recv'));
							tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
						}else{
							tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
						}
							
						return tmp;
					}
		}, {
			header : "ͼ������",
			width : 300,
			sortable : true,
			dataIndex : 'layerDesc',
			renderer: function tips(val) {
				        	var tmp = '';
				            if(val.indexOf('_recv')!=-1){
								val = val.substring(0,val.indexOf('_recv'));
								tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
							}else{
								tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
							}
							
							return tmp;
            			}
		}, {
			header : "ͼ���ͼ����",
			width : 100,
			sortable : true,
			dataIndex : 'mapLevel',
			renderer: layermaplevel
		}, {
			header : "�Ƿ�ɼ�",
			width : 100,
			sortable : true,
			dataIndex : 'visible',
			renderer : visible
		}];

var layerGrid = new Ext.grid.GridPanel({
			//region: 'center',
			width : 450,
			//iconCls: 'icon-grid',
			//frame: true,
			//title: 'ͼ���б�',
			//autoExpandColumn: 'name',
			enableColumnHide : false,
			store : layerstore,
			//plugins: [editor],
			columns : layerColumns,
			sm : layersm,
			//sm : smcheckbox,
			margins : '0 0 0 0',
			bbar : new Ext.PagingToolbar({
						pageSize : 20,
						store : layerstore,
						displayInfo : true,
						displayMsg : '��{0}����{1}������ ��{2}��',
						emptyMsg : "û������"
					}),
			tbar : [delbut]
		});

var viewport;
Ext.onReady(function() {
	Ext.QuickTips.init();
	layerstore.load({params : {start : 0,limit : 20}});
	viewport = new Ext.Viewport({
		layout : 'card',
		activeItem : 0,
		items : [layerGrid, modifyPanel]
	});
	
	store.on('load',function(storethis,records,obj){
		sm.clearSelections();
		queryLayer();
	});
});
