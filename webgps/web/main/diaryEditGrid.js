
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
	           msg: '���ڲ�ѯ ���Ե�...',
	           progressText: '��ѯ...',
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
	loadMask: {msg:'��ѯ��...'},
	sm: DiaryEditSm,
	columns: [
		{header: '����', width: 130, sortable: false,  dataIndex: 'termName'},
		{header: '����', width: 130, sortable: false,  dataIndex: 'title', renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '����', width: 200, sortable: false,  dataIndex: 'content' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '��ע', width: 200, sortable: false,  dataIndex: 'remark' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		//{header: '��������', width: 130, sortable: false,  dataIndex: 'createDate' },
		{header: '��־����', width: 100, sortable: false,  dataIndex: 'diaryDate' },
		{header: '�޸�����', width: 130, sortable: false,  dataIndex: 'modifyDate' },
		{header: '��ע����', width: 130, sortable: false,  dataIndex: 'remarkDate' }
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryEditGridId',
	//autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text:'��ʼʱ��'},
			{id: 'DEsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: yesterday},'-',
			{xtype: 'label',text:'����ʱ��'},
			{id: 'DEedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			{xtype: 'label', text:'�ؼ���'},
			{id: 'DEdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchDiaryEditGrid();}
			}}},'-',
			{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',id:'queryDEBtn',handler: function(){
				searchDiaryEditGrid();
			}},'-',
			{xtype: 'button',text: '����',iconCls: 'icon-add',id:'addDEBtn',handler: function(){
				var tmpArr = new Array();
				getTreeIdDiary(DERoot, tmpArr);
				if(tmpArr.length != 2){
					Ext.Msg.alert('��ʾ','��ѡ��һ���ն�!');
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
			{xtype: 'button',text: '�޸�',iconCls: 'icon-modify',id:'modifyDEBtn', disabled: true, handler: function(){
				Ext.Msg.show({
			           msg: '���ڲ�ѯ ���Ե�...',
			           progressText: '��ѯ...',
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
			{xtype: 'button',text: 'ɾ��',iconCls: 'icon-search',id:'delDEBtn',handler: function(){
			}}*/
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: DiaryEditStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
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
		fieldLabel: '����',
		format: 'Y��m��d��',
		editable: false,
		value: new Date()
    },{
		id: 'diaryAddTitle',
		xtype: 'textfield',
		width: 350,
		fieldLabel: '����',
		allowBlank: false,
		blankText : '����Ϊ��',
		maxLength: 20
    },{
		id: 'diaryAddContent',
		xtype: 'textarea',
        fieldLabel: '����',
		allowBlank: false,
		blankText : '����Ϊ��',
		width: 350,
		height: 100,
		maxLength: 120
    },{
		xtype: 'textarea',
        fieldLabel: '��ע',
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
        text: 'ȷ��',
		handler: function(){
			var DiaryEditAddForm = Ext.getCmp('DiaryEditAddForm').getForm();
			if(DiaryEditAddForm.isValid() != false){
				Ext.Msg.confirm('��ʾ', '��ȷ��Ҫ������־��?',  function (btn){
					if(btn=='yes'){
						Ext.Msg.show({
						   msg: '������ ���Ե�...',
						   progressText: '������...',
						   width:300,
						   wait:true,
						   //waitConfig: {interval:200},
						   icon:'ext-mb-download'
					   });
					   addOrUpdateDiary();
				   	   //Ext.Msg.alert('��ʾ', '�����ɹ�');
					}
				});
			}
		}
    },{
        text: '����',
		handler: function(){
			DiaryMarkStore.removeAll();
			Ext.getCmp('DiaryEditPanel').layout.setActiveItem(0);
			smswin.setWidth(900);
		}
    }/*,{
        text: '��ѯ�켣',
		handler: function(){
			Ext.Msg.show({
			           msg: '���ڲ�ѯ ���Ե�...',
			           progressText: '��ѯ��...',
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
				   Ext.Msg.alert('��ʾ', "δ�鵽����!");
				   return;
			   }
				if(res.length>0){
					map.parseDiaryTrackData(res);
				}
			 },
			 failure : function(request) {
				 Ext.Msg.alert('��ʾ', "δ�鵽����!");
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
	loadMask: {msg:'��ѯ��...'},
	sm: DiaryMarkSm,
	columns: [
	    DiaryMarkSm,
		{header: '����', width: 130, sortable: false,  dataIndex: 'lng' },
		{header: 'γ��', width: 130, sortable: false,  dataIndex: 'lat' },
		{header: '����', width: 200, sortable: false,  dataIndex: 'desc' , renderer: function (value, meta, record) {
			if(value != '��ȡ'){
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
			{xtype: 'button',text: '��ע',iconCls: 'icon-search', id:'markDEBt', handler: function(){
				var count = DiaryMarkStore.getCount();
				if(count>=10){
					Ext.Msg.alert('��ʾ','ֻ�ܱ�ע10����!');
					return;
				}
				map.addEventListener(map.MOUSE_CLICK,MclickMouse_DiaryMark);
				Ext.getCmp('markDEBt').disable();
			}},'-',
			{xtype: 'button',text: 'ɾ��',iconCls: 'icon-del', id:'markdelDEBt', handler: function(){
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
		   		Ext.Msg.alert('��ʾ', '�����ɹ�');
		   }else if(res.result==3){
		   		Ext.Msg.alert('��ʾ', "�����ڵ���־��¼��,��ѡ����������!");
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "����ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "����ʧ��!");
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
		desc: '��ȡ'
	};
	var tmpRecord = new DiaryMarkStore.recordType(defaultData);
	DiaryMarkStore.insert(0,tmpRecord);
	
}
