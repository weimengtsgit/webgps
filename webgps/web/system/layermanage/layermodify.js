//选择要修改的图层,页面按钮
var delbut = new Ext.Action({
	text : '修改',
	id : 'delbut',
	//disabled : true,
	handler : function() {
		var tmpselArr = layersm.getSelected();
		if (tmpselArr == undefined) {
			parent.Ext.MessageBox.alert('提示', '请选择要修改的图层!');
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
		
		//parent.Ext.MessageBox.confirm('提示', '您确定要添加新图层吗?', addConfirm);
	},
	iconCls : 'icon-modify'
});

//根据图层id查询可见用户
function queryLayer(){
	Ext.Ajax.request({
		url : path + '/poi/poi.do?method=queryLayer',
		method : 'POST',
		params : {id : Ext.getCmp('layeridfrm').getValue()},
		//timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			if (res.visibleUserIds != undefined) {
				//查询该图层可见用户id
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
			//parent.Ext.Msg.alert('提示', "保存失败!");
		}
	});
}

//修改页面按钮
var toolbar = new Ext.Toolbar({
	id : 'frmtbar',
	items : [new Ext.Action({
				text : '修改',
				id : 'modifybut',
				//disabled : true,
				handler : function() {
					var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入图层名称!');
		        		return ;
		        	}
		        	
		        	//var tmpselArr = sm.getSelections();
		        	//if(tmpselArr.length<=0){
		        	//	parent.Ext.MessageBox.alert('提示', '请选择可见用户!');
		        	//	return ;
		        	//}

		        	parent.Ext.MessageBox.confirm('提示', '您确定要修改此图层吗?', addConfirm);
		        
				},
				iconCls : 'icon-modify'
			}), new Ext.Action({
				text : '返回',
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
					msg : '正在保存 请稍等...',
					progressText : '保存...',
					width : 300,
					wait : true,
					//waitConfig: {interval:200},
					icon : 'ext-mb-download'
				});
		add();
	}
}

//修改图层
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
						parent.Ext.Msg.alert('提示', '修改成功');
						return;
					} else {
						//store.reload();
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
			region : 'west',
			labelWidth : 100, // label settings here cascade unless overridden
			//url:'save-form.php',
			frame : true,
			//title: '基本信息',
			bodyStyle : 'padding:5px 5px 0',
			width : 300,
			defaults : {
				width : 180
			},
			defaultType : 'textfield',
			items : [{
						//xtype : 'textfield',
						fieldLabel : '图层名称',
						id : 'layerNamefrm',
						width : 150,
						allowBlank : false
					}, {
						//xtype : 'textfield',
						fieldLabel : '图层描述',
						id : 'layerDescfrm',
						width : 300
						
					}, {
						xtype : 'radiogroup',
						fieldLabel : '是否可见',
						width : 80,
						id : 'visiblefrm',
						items : [{
									boxLabel : '是',
									name : 'isShow',
									inputValue : 1,
									checked : true
								}, {
									boxLabel : '否',
									name : 'isShow',
									inputValue : 0
								}]
					}, {
						id : 'mapLevelfrm',
						xtype : 'combo',
						fieldLabel : '图层地图级别',
						editable : false,
						width : 80,
						displayField : 'name',
						store : new Ext.data.ArrayStore({
									fields : ['id', 'name'],
									data : [[11,'市'],[13,'县'],[14,'镇'],[16,'街道']]
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
					header : '序号',
					width : 40
				}), {
			id : 'id',
			header : "id",
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		}, {
			header : "用户名称",
			width : 100,
			sortable : true,
			dataIndex : 'userName'
		}, {
			header : "登录帐号",
			width : 100,
			sortable : true,
			dataIndex : 'userAccount'
		}, {
			header : "省",
			width : 100,
			sortable : true,
			dataIndex : 'province'
		}, {
			header : "市",
			width : 100,
			sortable : true,
			dataIndex : 'city'
		}, {
			header : "创建时间",
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
	title : '帐号列表',
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
				displayMsg : '第{0}到第{1}条数据 共{2}条',
				emptyMsg : "没有数据"
			})
		/*tbar: [
	    delbut
	]*/
	});

var modifyPanel = new Ext.Panel({
	layout : 'border',
	items : [simple, userGrid]
});
/*图层列表*/
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
		return '不可见';
	} else if (val == '1') {
		return '可见';
	}
}

function layermaplevel(val){
	if (val == '11') {
		return '市';
	} else if (val == '13') {
		return '县';
	} else if (val == '14') {
		return '镇';
	}else if (val == '16') {
		return '街道';
	}
}

var layerColumns = [
//layersm,
		new Ext.grid.RowNumberer({header : '序号',width : 40
		}), {
			id : 'id',
			header : "id",
			width : 40,
			sortable : true,
			dataIndex : 'id',
			hidden : true
		}, {
			header : "图层名称",
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
			header : "图层描述",
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
			header : "图层地图级别",
			width : 100,
			sortable : true,
			dataIndex : 'mapLevel',
			renderer: layermaplevel
		}, {
			header : "是否可见",
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
			//title: '图层列表',
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
						displayMsg : '第{0}到第{1}条数据 共{2}条',
						emptyMsg : "没有数据"
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
