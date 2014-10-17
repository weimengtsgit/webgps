var diaryRemarkTermName = '';
var diaryRemarkSearchValue = '';
var diaryRemarkStartTime = tmpdateyesterday+' '+'00:00:00';
var diaryRemarkEndTime = tmpdate+' '+'23:59:59';
function getCheckedTreeText(node,textArr){
	if(node.isLeaf()&&node.getUI().isChecked()){
		textArr[textArr.length] = node.text;
	}
	node.eachChild(function(child) {
		getCheckedTreeText(child,textArr);
	});
}

function searchDiaryRemarkGrid(){
	Ext.getCmp('remarkDRBtn').disable();
	var tmpstartdate = Ext.getCmp('DRsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('DRedf').getValue().format('Y-m-d');
	diaryRemarkSearchValue = Ext.getCmp('DRdif').getValue();
	diaryRemarkStartTime = tmpstartdate+' '+'00:00:00';
	diaryRemarkEndTime = tmpenddate+' '+'23:59:59';
	var treeArr = [];
	getTreeIdsDiary(DRRoot,treeArr);
	storeLoad(DiaryRemarkStore, 0, 15, treeArr.toString(), diaryRemarkSearchValue, diaryRemarkStartTime, diaryRemarkEndTime, '', false);
}

var DiaryRemarkStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/diary/diary.do?method=listDiaryByDeviceIds'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'termName'},{name: 'title'},{name: 'content'},{name: 'remark'},{name: 'createDate'},{name: 'modifyDate'},{name: 'entCode'},{name: 'userId'},{name: 'diaryDate'},{name: 'remarkDate'},{name: 'deviceId'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					deviceIds: encodeURI(diaryRemarkTermName),searchValue: encodeURI(diaryRemarkSearchValue), startTime: diaryRemarkStartTime, endTime: diaryRemarkEndTime
		    	};
			}
		}
	}
});
var DiaryRemarkSm = new Ext.grid.RowSelectionModel( {
	singleSelect : true,
	listeners: {
		rowselect: function (selectionModel, rowIndex, record) {
			var record = DiaryRemarkSm.getSelected();
			Ext.getCmp('remarkDRBtn').enable();
		}
	}
});
var DiaryRemarkGrid = new Ext.grid.GridPanel({
	//xtype: 'grid',
	//collapsible: false,
    region:'center',
    //margins: '5 0 0 0',
	id: 'DiaryRemarkGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: DiaryRemarkStore,
	loadMask: {msg:'��ѯ��...'},
	sm: DiaryRemarkSm,
	columns: [
		{header: '����', width: 100, sortable: false,  dataIndex: 'termName', renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '����', width: 130, sortable: false,  dataIndex: 'title', renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '����', width: 200, sortable: false,  dataIndex: 'content' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '��ע', width: 200, sortable: false,  dataIndex: 'remark' , renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '��־����', width: 100, sortable: false,  dataIndex: 'diaryDate' },
		{header: '�޸�����', width: 130, sortable: false,  dataIndex: 'modifyDate' },
		{header: '��ע����', width: 130, sortable: false,  dataIndex: 'remarkDate' }
	],
	stripeRows: true,
	stateful: true,
	stateId: 'DiaryRemarkGridId',
	//autoExpandColumn: 'process',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text:'��ʼʱ��'},
			{id: 'DRsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: yesterday},'-',
			{xtype: 'label',text:'����ʱ��'},
			{id: 'DRedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},'-',
			{xtype: 'label', text:'�ؼ���'},
			{id: 'DRdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchDiaryRemarkGrid();}
			}}},'-',
			{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',id:'queryDRBtn',handler: function(){
				searchDiaryRemarkGrid();
			}},'-',
			{xtype: 'button',text: '��ע',iconCls: 'icon-modify',id:'remarkDRBtn', disabled: true, handler: function(){
				var r = DiaryRemarkSm.getSelected();
				Ext.getCmp('diaryRemarkId').setValue(r.get('id'));
				Ext.getCmp('diaryRemarkTitle').setValue(r.get('title'));
				Ext.getCmp('diaryRemarkContent').setValue(r.get('content'));
				Ext.getCmp('diaryRemarkRemark').setValue(r.get('remark'));
				Ext.getCmp('diaryRemarkDF').setValue(r.get('diaryDate'));
				Ext.getCmp('DiaryRemarkPanel').layout.setActiveItem(1);
				smswin.setWidth(450);
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: DiaryRemarkStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
});
var DRLoader = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'
});
var DRRoot = new Ext.tree.AsyncTreeNode({
	text : '',
	id : '-100',
	draggable : false
});
var DRTreePanel = new Ext.tree.TreePanel({
	rootVisible: false,
    lines: true,
    autoScroll: true,
    width: 170,
    loader: DRLoader,
    root: DRRoot,
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

var DRpanel = new Ext.Panel({
	layout:'border',
	items: [DRTreePanel, DiaryRemarkGrid]
});

var DiaryRemarkForm = {
	id: 'DiaryRemarkAddForm',
	xtype: 'form',
	buttonAlign: 'center',
	frame: true,
	labelWidth: 50,
    defaults: {width: 230},
    items: [{
		id: 'diaryRemarkDF',
		xtype: 'datefield',
		width: 160,
		fieldLabel: '����',
		format: 'Y��m��d��',
		readOnly: true
    },{
		id: 'diaryRemarkTitle',
		xtype: 'textfield',
		width: 350,
		fieldLabel: '����',
		allowBlank: false,
		blankText : '����Ϊ��',
		maxLength: 20,
		readOnly: true
    },{
		id: 'diaryRemarkContent',
		xtype: 'textarea',
        fieldLabel: '����',
		allowBlank: false,
		blankText : '����Ϊ��',
		width: 350,
		height: 100,
		maxLength: 120,
		readOnly: true
    },{
		id: 'diaryRemarkRemark',
		xtype: 'textarea',
        fieldLabel: '��ע',
		allowBlank: false,
		width: 350,
		height: 100,
		maxLength: 120
    },{
		xtype: 'hidden',
		hidden: true,
		id: 'diaryRemarkId'
    }],
    buttons: [{
        text: 'ȷ��',
		handler: function(){
			var DiaryRemarkAddForm = Ext.getCmp('DiaryRemarkAddForm').getForm();
			if(DiaryRemarkAddForm.isValid() != false){
				Ext.Msg.confirm('��ʾ', '��ȷ��Ҫ������ע��?',  function (btn){
					if(btn=='yes'){
						Ext.Msg.show({
						   msg: '������ ���Ե�...',
						   progressText: '������...',
						   width:300,
						   wait:true,
						   //waitConfig: {interval:200},
						   icon:'ext-mb-download'
					   });
					   remarkDiary();
					}
				});
			}
		}
    },{
        text: '����',
		handler: function(){
			Ext.getCmp('DiaryRemarkPanel').layout.setActiveItem(0);
			smswin.setWidth(900);
		}
    }/*,{
        text: '����',
		handler: function(){
			Ext.Msg.show({
			           msg: '���ڲ�ѯ ���Ե�...',
			           progressText: '��ѯ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });

			Ext.Ajax.request({
			 url:path+'/locate/locate.do?method=track',
			 method :'POST', 
			 params: { startTime : starttime },
			 timeout : 180000,
			 success : function(request) {
				Ext.Msg.hide();
				var res = Ext.decode(request.responseText);
				if(res==null || res.length==0){
					//Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
				Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
				   Ext.Msg.alert('��ʾ', "δ�鵽����!");
				   
				   return;
			   }
				if(res.length>0){
					//add magiejue 2010-11-23
					terminalType=res[0].locType;//����ն�����
					var tmpCardPanel = Ext.getCmp('trackquerypanel');
					tmpCardPanel.layout.setActiveItem(1);
					//���õ�ͼҳ�Ľ����켣���ݷ���,�����㻭��
					map.parseTrackData(res);
				}
				Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
				Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
			 },
			 failure : function(request) {
				 Ext.Msg.alert('��ʾ', "�켣�طŲ�ѯʧ��!");
				 Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
				Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
			 }
			});
		}
    }*/]
};

function remarkDiary(){
	var remark = Ext.getCmp('diaryRemarkRemark').getValue();
	var id = Ext.getCmp('diaryRemarkId').getValue();
	Ext.Ajax.request({
		 url: path+'/diary/diary.do?method=remarkDiary',
		 method :'POST', 
		 params: { id: id, remark: encodeURI(remark)},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		DiaryRemarkStore.reload();
				Ext.getCmp('remarkDRBtn').disable();
		   		Ext.Msg.alert('��ʾ', '�����ɹ�');
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

