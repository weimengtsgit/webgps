
var diaryEditSearchValue = '';
var diaryEditStartTime = tmpdateyesterday+' '+'00:00:00';
var diaryEditEndTime = tmpdate+' '+'23:59:59';
var globalDiaryEditMarkRecord = null;
function ShowDiaryEditMarkGridF(record){
	var tmpx = record.get('lng');
	var tmpy = record.get('lat');
	globalDiaryEditMarkRecord = record;
	if(tmpx.length > 0 && tmpy.length > 0){
		Ext.Msg.show({
	           msg: '正在查询 请稍等...',
	           progressText: '查询...',
	           width:300,
	           wait:true,
	           icon:'ext-mb-download'
	       });
		Ext.Ajax.request({
			url:path+'/locate/locate.do?method=locDesc',
			method :'POST', 
			params: {x: tmpx, y: tmpy},
			success : function(request){
				var res= request.responseText;
				globalDiaryEditMarkRecord.set('desc', res);
				globalDiaryEditMarkRecord.commit();
				Ext.Msg.hide();
			},
			failure : function(request) {
			}
		});
	}
}

function searchDiaryEditGrid(){
	Ext.getCmp('modifyDEBtn').disable();
	var tmpstartdate = Ext.getCmp('DEsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('DEedf').getValue().format('Y-m-d');
	diaryEditSearchValue = Ext.getCmp('DEdif').getValue();
	diaryEditStartTime = tmpstartdate+' '+'00:00:00';
	diaryEditEndTime = tmpenddate+' '+'23:59:59';
	var treeArr = [];
	getTreeIdsDiary(DERoot,treeArr);
	storeLoad(DiaryEditStore, 0, 15, treeArr.toString(), diaryEditSearchValue, diaryEditStartTime, diaryEditEndTime, '', false);
}

var DiaryEditStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/diary/diary.do?method=listDiaryByDeviceIds'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'termName'},{name: 'title'},{name: 'content'},{name: 'remark'},{name: 'createDate'},{name: 'modifyDate'},{name: 'entCode'},{name: 'userId'},{name: 'diaryDate'},{name: 'remarkDate'},{name: 'deviceId'}]),
	//autoLoad: {params:{start: 0, limit: 15, searchValue: encodeURI(diaryEditSearchValue) , startTime: diaryEditStartTime, endTime: diaryEditEndTime}},
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(diaryEditSearchValue), startTime: diaryEditStartTime, endTime: diaryEditEndTime
		    	};
			}
		}
	}
});
var DiaryEditSm = new Ext.grid.RowSelectionModel( {
	singleSelect : true,
	listeners: {
		rowselect: function (selectionModel, rowIndex, record) {
			var record = DiaryEditSm.getSelected();
			var remark = record.get('remark');
			if(remark.length>0){
				Ext.getCmp('modifyDEBtn').disable();
			}else{
				Ext.getCmp('modifyDEBtn').enable();
			}
			
		}/*,
		rowdeselect :function (selectionModel, rowIndex, record) {
			Ext.getCmp('modifyDEBtn').disable();
		}*/
	}
});
var DiaryEditGrid = {
	xtype: 'grid',
    region:'center',
	id: 'DiaryEditGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: DiaryEditStore,
	loadMask: {msg:'查询中...'},
	sm: DiaryEditSm,
	columns: [
		{header: '名称', width: 130, sortable: false,  dataIndex: 'termName'},
		{header: '标题', width: 130, sortable: false,  dataIndex: 'title', renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '内容', width: 200, sortable: false,  dataIndex: 'content' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '批注', width: 200, sortable: false,  dataIndex: 'remark' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		//{header: '创建日期', width: 130, sortable: false,  dataIndex: 'createDate' },
		{header: '日志日期', width: 100, sortable: false,  dataIndex: 'diaryDate' },
		{header: '修改日期', width: 130, sortable: false,  dataIndex: 'modifyDate' },
		{header: '批注日期', width: 130, sortable: false,  dataIndex: 'remarkDate' }
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryEditGridId',
	//autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text:'开始时间'},
			{id: 'DEsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: yesterday},'-',
			{xtype: 'label',text:'结束时间'},
			{id: 'DEedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			{xtype: 'label', text:'关键字'},
			{id: 'DEdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchDiaryEditGrid();}
			}}},'-',
			{xtype: 'button',text: '查询',iconCls: 'icon-search',id:'queryDEBtn',handler: function(){
				searchDiaryEditGrid();
			}},'-',
			{xtype: 'button',text: '新增',iconCls: 'icon-add',id:'addDEBtn',handler: function(){
				var tmpArr = new Array();
				getTreeIdDiary(DERoot, tmpArr);
				if(tmpArr.length != 2){
					Ext.Msg.alert('提示','请选择一个终端!');
					return;
				}
				addOrUpdate = true;
				var deviceId = tmpArr[0];
				var termName = tmpArr[1];
				Ext.getCmp('diaryDeviceId').setValue(deviceId);
				Ext.getCmp('diaryTermName').setValue(termName);
				Ext.getCmp('diaryAddDF').setReadOnly(false);
				Ext.getCmp('diaryAddId').reset();
				Ext.getCmp('diaryAddTitle').reset();
				Ext.getCmp('diaryAddContent').reset();
				Ext.getCmp('diaryAddDF').setValue(new Date);
				Ext.getCmp('DiaryEditPanel').layout.setActiveItem(1);
				smswin.setWidth(450);
			}},'-',
			{xtype: 'button',text: '修改',iconCls: 'icon-modify',id:'modifyDEBtn', disabled: true, handler: function(){
				Ext.Msg.show({
			           msg: '正在查询 请稍等...',
			           progressText: '查询...',
			           width:300,
			           wait:true,
			           icon:'ext-mb-download'
			       });
				addOrUpdate = false;
				var r = DiaryEditSm.getSelected();
				var id = r.get('id');
				var deviceId = r.get('deviceId');
				var diaryDate = r.get('diaryDate');
				Ext.getCmp('diaryAddId').setValue(id);
				Ext.getCmp('diaryAddTitle').setValue(r.get('title'));
				Ext.getCmp('diaryAddContent').setValue(r.get('content'));
				Ext.getCmp('diaryAddDF').setValue(diaryDate);
				Ext.getCmp('diaryAddDF').setReadOnly(true);
				Ext.getCmp('DiaryEditPanel').layout.setActiveItem(1);
				Ext.getCmp('diaryTermName').setValue(r.get('termName'));
				Ext.getCmp('diaryDeviceId').setValue(deviceId);
				smswin.setWidth(450);
				Ext.Ajax.request({
					url:path+'/diarymark/diarymark.do?method=listDiaryMarkById',
					method :'POST', 
					params: {id: id},
					success : function(request){
						var res= request.responseText;
						DiaryMarkStore.loadData(eval(res));
						Ext.Msg.hide();
					},
					failure : function(request) {
					}
				});
			}}/*,'-',
			{xtype: 'button',text: '删除',iconCls: 'icon-search',id:'delDEBtn',handler: function(){
			}}*/
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: DiaryEditStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

var DELoader = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'
});

var DERoot = new Ext.tree.AsyncTreeNode({
	text : '',
	id : '-100',
	draggable : false
});

var DETreePanel = new Ext.tree.TreePanel({
	rootVisible: false,
    lines: true,
    autoScroll: true,
    width: 170,
    loader: DELoader,
    root: DERoot,
	split: true,
	//collapsible: true,
	region: 'west',
	//margins: '5 0 0 0',
	//cmargins: '5 5 0 0',
	minSize: 100,
	maxSize: 250,
	listeners : {
		checkchange: function(node,checked){
			node.expand();
			node.attributes.checked = checked;
			node.on('expand',function(node){
				node.eachChild(function(child){
					child.ui.toggleCheck(checked);
					child.attributes.checked = checked;
					child.fireEvent('checkchange',child,checked);
				});
			});
			node.eachChild(function(child){
				child.ui.toggleCheck(checked);
				child.attributes.checked = checked;
				child.fireEvent('checkchange',child,checked);
			});
		}
	}
});

function getTreeIdDiary(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = idArr[0];
			treeArr[treeArr.length] = node.text;
		}
	}
	node.eachChild(function(child) {
		getTreeIdDiary(child,treeArr);
	});
}

function getTreeIdsDiary(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = idArr[0];
		}
	}
	node.eachChild(function(child) {
		getTreeIdsDiary(child,treeArr);
	});
}

var DEpanel = new Ext.Panel({
	layout:'border',
	items: [DETreePanel, DiaryEditGrid]
});

var addOrUpdate = true;
var DiaryEditAddForm = {
	flex: 1,
	id: 'DiaryEditAddForm',
	xtype: 'form',
	buttonAlign: 'center',
	frame: true,
	labelWidth: 50,
    defaults: {width: 230},
    //defaultType: 'textfield',
    items: [{
		id: 'diaryAddDF',
		xtype: 'datefield',
		width: 160,
		fieldLabel: '日期',
		format: 'Y年m月d日',
		editable: false,
		value: new Date()
    },{
		id: 'diaryAddTitle',
		xtype: 'textfield',
		width: 350,
		fieldLabel: '标题',
		allowBlank: false,
		blankText : '不能为空',
		maxLength: 20
    },{
		id: 'diaryAddContent',
		xtype: 'textarea',
        fieldLabel: '内容',
		allowBlank: false,
		blankText : '不能为空',
		width: 350,
		height: 100,
		maxLength: 120
    },{
		xtype: 'textarea',
        fieldLabel: '批注',
		width: 350,
		height: 100,
		maxLength: 120,
		hidden: true,
		hideLabel: true
    },{
		xtype: 'hidden',
		hidden: true,
		id: 'diaryAddId'
    },{
		xtype: 'hidden',
		hidden: true,
		id: 'diaryDeviceId'
    },{
		xtype: 'hidden',
		hidden: true,
		id: 'diaryTermName'
    }],
    buttons: [{
        text: '确定',
		handler: function(){
			var DiaryEditAddForm = Ext.getCmp('DiaryEditAddForm').getForm();
			if(DiaryEditAddForm.isValid() != false){
				Ext.Msg.confirm('提示', '您确定要保存日志吗?',  function (btn){
					if(btn=='yes'){
						Ext.Msg.show({
						   msg: '操作中 请稍等...',
						   progressText: '操作中...',
						   width:300,
						   wait:true,
						   //waitConfig: {interval:200},
						   icon:'ext-mb-download'
					   });
					   addOrUpdateDiary();
				   	   //Ext.Msg.alert('提示', '操作成功');
					}
				});
			}
		}
    },{
        text: '返回',
		handler: function(){
			DiaryMarkStore.removeAll();
			Ext.getCmp('DiaryEditPanel').layout.setActiveItem(0);
			smswin.setWidth(900);
		}
    }/*,{
        text: '查询轨迹',
		handler: function(){
			Ext.Msg.show({
			           msg: '正在查询 请稍等...',
			           progressText: '查询中...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			var diaryDate = Ext.getCmp('diaryAddDF').getValue().format('Y-m-d');
			Ext.Ajax.request({
			 url:path+'/diary/diary.do?method=queryTrack',
			 method :'POST', 
			 params: { startTime : diaryDate },
			 timeout : 180000,
			 success : function(request) {
				Ext.Msg.hide();
				var res = Ext.decode(request.responseText);
				if(res==null || res.length==0){
				   Ext.Msg.alert('提示', "未查到数据!");
				   return;
			   }
				if(res.length>0){
					map.parseDiaryTrackData(res);
				}
			 },
			 failure : function(request) {
				 Ext.Msg.alert('提示', "未查到数据!");
			 }
			});
		}*/]
};

var DiaryMarkStore = new Ext.data.ArrayStore({
	fields: [
	 	{name: 'id'},
	 	{name: 'deviceId'},
	    {name: 'lng'},
	    {name: 'lat'},
	    {name: 'entCode'},
	    {name: 'desc'}
	]
});

var DiaryMarkSm = new Ext.grid.CheckboxSelectionModel({ 
	handleMouseDown : function(SelectionModel, rowIndex){
		var tmpRecord = DiaryMarkStore.getAt(rowIndex);
		if(drawDiaryMarkID.length>0){
			map.removeOverlayById('drawDiaryMarkID');
		}
		drawDiaryMarkID = 'drawDiaryMarkID';
		var poiCoordx = tmpRecord.get('lng');
		var poiCoordy = tmpRecord.get('lat');
		var imageUrl = '../images/poi/poi0.gif';
		map.addMarker('drawDiaryMarkID',poiCoordx,poiCoordy,'','',imageUrl,true);
	}
});

var DiaryMarkGrid = {
	flex: 1,
	xtype: 'grid',
	id: 'DiaryMarkGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: DiaryMarkStore,
	loadMask: {msg:'查询中...'},
	sm: DiaryMarkSm,
	columns: [
	    DiaryMarkSm,
		{header: '经度', width: 130, sortable: false,  dataIndex: 'lng' },
		{header: '纬度', width: 130, sortable: false,  dataIndex: 'lat' },
		{header: '描述', width: 200, sortable: false,  dataIndex: 'desc' , renderer: function (value, meta, record) {
			if(value != '获取'){
				return '<span style="display:table;width:100%;" qtip="' + value + '">' + value + '</span>';
			}
			var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='ShowDiaryEditMarkGrid'>"+value+"</a>";
			var resultStr = String.format(formatStr, record.get('id'));
				return "<div class='controlBtn'>" + resultStr + "</div>";
			}.createDelegate(this)
		}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryMarkGridId',
	//autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'button',text: '标注',iconCls: 'icon-search', id:'markDEBt', handler: function(){
				var count = DiaryMarkStore.getCount();
				if(count>=10){
					Ext.Msg.alert('提示','只能标注10个点!');
					return;
				}
				map.addEventListener(map.MOUSE_CLICK,MclickMouse_DiaryMark);
				Ext.getCmp('markDEBt').disable();
			}},'-',
			{xtype: 'button',text: '删除',iconCls: 'icon-del', id:'markdelDEBt', handler: function(){
				var tmpselArr = DiaryMarkSm.getSelections();
				for(var i=0;i<tmpselArr.length;i++){
					DiaryMarkStore.remove(tmpselArr[i]);
				}
			}}
		]
	},
	listeners: {
		cellclick: function (grid, rowIndex, columnIndex, e) {
			var btn = e.getTarget('.controlBtn');
			if (btn) {
				var t = e.getTarget();
				var record = grid.getStore().getAt(rowIndex);
				var control = t.className;
				switch (control) {
					case 'ShowDiaryEditMarkGrid':
						ShowDiaryEditMarkGridF(record, rowIndex);
						break;
				}
			}
		}
	}
};

var DiaryMarkPanel = {
	layout:'vbox',
	layoutConfig: {
		align : 'stretch',
		pack  : 'start'
	},
	items: [
	    DiaryEditAddForm,
	    DiaryMarkGrid
	]
};

function addOrUpdateDiary(){
	var diaryDate = Ext.getCmp('diaryAddDF').getValue().format('Y-m-d');
	var startTime = diaryDate+' '+'00:00:00';
	var endTime = diaryDate+' '+'23:59:59';
	var title = Ext.getCmp('diaryAddTitle').getValue();
	var content = Ext.getCmp('diaryAddContent').getValue();;
	var deviceId = Ext.getCmp('diaryDeviceId').getValue();
	var termName = Ext.getCmp('diaryTermName').getValue();
	var id = Ext.getCmp('diaryAddId').getValue();
	var method = 'addDiary';
	if(addOrUpdate){
		method = 'addDiary';
	}else{
		method = 'updateDiary';
	}
	var x = [];
	var y = [];
	var len = DiaryMarkStore.getCount();
	for(var i=0;i<len;i++){
	   var tmprecord=DiaryMarkStore.getAt(i);
	   x.push(tmprecord.get('lng'));
	   y.push(tmprecord.get('lat'));
	}
	Ext.Ajax.request({
		 url: path+'/diary/diary.do?method='+method,
		 method :'POST', 
		 params: { id: id, termName: encodeURI(termName), deviceId: deviceId, startTime: startTime, endTime: endTime ,title: encodeURI(title), content: encodeURI(content), lng: x.toString(), lat: y.toString() },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		DiaryEditStore.reload();
				Ext.getCmp('modifyDEBtn').disable();
		   		Ext.Msg.alert('提示', '操作成功');
		   }else if(res.result==3){
		   		Ext.Msg.alert('提示', "该日期的日志已录入,请选择其它日期!");
		   		return;
		   }else{
		   		Ext.Msg.alert('提示', "操作失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "操作失败!");
		 }
	});
}

var drawDiaryMarkID = '';
function MclickMouse_DiaryMark(param){
	if(drawDiaryMarkID.length>0){
		map.removeOverlayById('drawDiaryMarkID');
	}
	drawDiaryMarkID = 'drawDiaryMarkID';
	var poiCoordx = param.eventX;
	var poiCoordy = param.eventY;
	var imageUrl = '../images/poi/poi0.gif';
	map.addMarker('drawDiaryMarkID',poiCoordx,poiCoordy,'','',imageUrl,false);
	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse_DiaryMark);
	Ext.getCmp('markDEBt').enable();
	
	var defaultData = {
		id: '',
		deviceId: '',
		lng: poiCoordx,
		lat: poiCoordy,
		entCode: '',
		desc: '获取'
	};
	var tmpRecord = new DiaryMarkStore.recordType(defaultData);
	DiaryMarkStore.insert(0,tmpRecord);
	
}
