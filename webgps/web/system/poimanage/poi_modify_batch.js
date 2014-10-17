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
var IRecord = Ext.data.Record.create([{name: 'id'}, {name: 'name'}]);

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
			dataIndex : 'termName'
		}, {
			header : "����ʱ��",
			width : 130,
			sortable : true,
			dataIndex : 'cDate'
		} ];

var layerModifyStore = new Ext.data.SimpleStore({
  	fields:['id', 'name'],
  	data:[[]]
  });
 
//ͼ�������б�
function layerModifyComboexpand(){
	Ext.Ajax.request({
	 url : path+'/poi/poi.do?method=comboboxListLayer&modifyPoiBatch=false',
	 method :'POST',
	 success : function(request) {
	   var res = request.responseText;
	   layerModifyStore.loadData(eval(res));
	 },
	 failure : function(request) {
	 }
	});
}

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
	} ]))
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


function showPoiWin(){
PoiWindow= new Ext.Window({
title: '�����޸�',
width:310,
height:200,
shim:false,
closeAction: 'close',
animCollapse:false,
constrainHeader:true,
collapsible:true,
plain:true,
resizable:true,
closable:true,
animCollapse :true,
layout:'fit',
border:false,
items: [
Poiform=new Ext.FormPanel({
	   labelWidth:75,
       bodyStyle:'padding:5px 5px 0',
	   autoScroll : true,
	   defaults: {width: 150},
       items: [
    	   layerCombox=new Ext.form.ComboBox({   
	                   store : layerModifyStore,   
	                   editable:false, 
	                   mode: 'local',   
	                   triggerAction:'all',   
	                   fieldLabel: 'ͼ������',
	                   id : 'poiLayerfrm',
	                   displayField:'name',
	                   valueField :'id',
	                   emptyText:'ѡ��ͼ��',
	                   maxHeight: 200
	               }),{
    		id: 'range',
        	xtype: 'numberfield',
        	fieldLabel: '��Χ'
    	},{
    		id:'style',
			xtype:'fieldset',
			width:265,
			title: '��ע��ʽ',
			autoheight:true,
			layout:'anchor',
			defaultType: 'textfield',
			items:[{
				id:'poi0',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi0').dom.src = path+"/images/poi/poi0.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi0.gif');        						
					}
				}
			},{
				id:'poi1',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi1').dom.src = path+"/images/poi/poi1.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi1.gif');
					}
				}
			},{
				id:'poi2',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi2').dom.src = path+"/images/poi/poi2.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi2.gif');
					}
				}								            		
			},{
				id:'poi8',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi8').dom.src = path+"/images/poi/poi8.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi8.gif');
					}
				}								            		
			},{
				id:'poi9',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi9').dom.src = path+"/images/poi/poi9.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi9.gif');
					}
				}								            		
			},{
				id:'poi10',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi10').dom.src = path+"/images/poi/poi10.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi10.gif');
					}
				}								            		
			},{
				id:'poi11',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi11').dom.src = path+"/images/poi/poi11.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi11.gif');
					}
				}								            		
			},{
				id:'poi12',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi12').dom.src = path+"/images/poi/poi12.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi12.gif');
					}
				}								            		
			},{
				xtype: 'hidden',
				id: 'poiTypeHid'
			},{
				xtype: 'hidden',
				id: 'poiImgHid'
			},{
				xtype: 'hidden',
				id: 'poiCoordx'
			},{
				xtype: 'hidden',
				id: 'poiCoordy'
			}]
		},{
			xtype : 'hidden',
			id : 'idfrm'
		},{
			xtype : 'hidden',
			id : 'layerIdfrm'
		},{
			xtype : 'hidden',
			id : 'deviceIdfrm'
		},{
			xtype : 'hidden',
			id : 'iconpathfrm'
		}],
	 buttons:[{
 		text:'�޸�',
		xtype: 'button',
		columnWidth:.3,
		anchor:'50%',
		handler:function(){
			//��֤�Ƿ�ѡ��ͼ��
			if(!Ext.getCmp('poiLayerfrm').disabled){
				if(Ext.getCmp('poiLayerfrm').getValue()==""){
					Ext.Msg.alert('��ʾ', '��ѡ��ͼ��');
					return;
				}
			}
			//��֤�Ƿ����뷶Χ
			if(!Ext.getCmp('range').disabled){
				if(Ext.getCmp('range').getValue()==""){
					Ext.Msg.alert('��ʾ', '�����뷶Χֵ');
					return;
				}
			}
			//��֤�Ƿ�ѡ��ͼ��
			if(!Ext.getCmp('style').disabled){
				if(Ext.getCmp('poiImgHid').getValue()==""){
					Ext.Msg.alert('��ʾ', '��ѡ��ͼ��');
					return;
				}
			}
			var m=Ext.getCmp("poiGrid").getSelectionModel().getSelections();
			var poiIds="";
			var poiLayerModify = Ext.getCmp('poiLayerfrm').getValue();
			var range = Ext.getCmp('range').getValue();
			var iconPath=Ext.getCmp('poiImgHid').getValue();
    		for ( var i = 0; i < m.length; i++) {
    			if(i==m.length-1)
    				poiIds += m[i].get('id');
    			else
    				poiIds += m[i].get('id')+',';
				
			}
    		
    		Ext.Msg.show({
		           msg: '���ڱ��� ���Ե�...',
		           progressText: '����...',
		           width:300,
		           wait:true,
		           //waitConfig: {interval:200},
		           icon:'ext-mb-download'
		       });
    		
			Ext.Ajax.request({
				url:path+'/poi/poi.do?method=updatePoiBatch',
				params: { 
					poiIds: poiIds,
					poiLayer: poiLayerModify,
					range: range,
					iconpath: iconPath
				},
				method :'POST',
				success:function(request){
					 var res = Ext.decode(request.responseText);
					 	if(res.result==1){
					 		queryPois();
					   		Ext.Msg.alert('��ʾ', '�޸ĳɹ�');
					   		PoiWindow.close();
					   		btnWin.hide();
					   		return;
					   }else{
					   		Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
					   		return;
					   }
				 }
			});
		}
	},{text:'�ر�',handler:function(){
		 PoiWindow.close();
		 btnWin.hide();
	 }}] 
    })
    ]
});
PoiWindow.show();
};
var btnWin = new Ext.Window({
layout:'fit',
width:280,
height:70,
title:'�޸�ѡ��',
closeAction:'hide',
plain: true,
items : [
new Ext.Panel({
frame:true,
layout:'column',
items:[
new Ext.Button({
	columnWidth:.33,
    text: '�޸�ͼ��',
    handler: function(){
    	showPoiWin();
    	Ext.getCmp('poiLayerfrm').setDisabled(false);
    	Ext.getCmp('range').setDisabled(true);
    	Ext.getCmp('style').setDisabled(true);
    }
}),new Ext.Button({
	columnWidth:.33,
    text: '�޸ķ�Χ',
    handler: function(){
    	showPoiWin();
    	Ext.getCmp('range').setDisabled(false);
    	Ext.getCmp('poiLayerfrm').setDisabled(true);
    	Ext.getCmp('style').setDisabled(true);
    }
}),new Ext.Button({
	columnWidth:.33,
    text: '�޸���ʽ',
    handler: function(){
    	showPoiWin();
    	Ext.getCmp('style').setDisabled(false);
    	Ext.getCmp('poiLayerfrm').setDisabled(true);
    	Ext.getCmp('range').setDisabled(true);
    }
})]
})	
]
});

var poiGrid = new Ext.grid.GridPanel({
	id : 'poiGrid',
	region : 'center',
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
			emptyText:'ѡ��ͼ��',
			editable : true,
			triggerAction : 'all',
			width : 100,
			store : layersStore,
			listeners : {
				select : function(thiz, re, index) {
					layer = encodeURI(re.get('id'));
					if (re.get('name') == '����ͼ��') {
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

var modifybut = new Ext.Action({
    text: '�����޸�',
    id : 'modifybut',
    handler: function(){
    	var have = Ext.getCmp("poiGrid").getSelectionModel().getSelected();
    	if(!have){
    		Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ�޸ĵı�ע!');
    		return ;
    	}
    	btnWin.show();
    },
    iconCls: 'icon-modify'
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
					queryPois();
				},
				iconCls : 'icon-search'
			}),'-',modifybut ]
});

function queryPois(){
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
		},
		callback:function(r, options, success){
			if(success){
				 var myNewRecord = new IRecord({ 
				        id: '',
				        name: '����ͼ��'
				   });
				 layersStore.insert(0, myNewRecord);
			}
		}
	});
	

	viewport = new Ext.Viewport({
		layout : 'border',
		items : [ poiGrid ]
	});

	loadMask.hide();
	
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
	
	layerModifyComboexpand();

	Ext.getCmp('bindingStatus').setValue(1);
});
