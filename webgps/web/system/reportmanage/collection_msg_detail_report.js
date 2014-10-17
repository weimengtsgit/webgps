var vehicleMsgStatDeviceids = '';
var vehicleMsgStatSearchValue = '';
var vehicleMsgStatStartTime = tmpdate;
var vehicleMsgStatEndTime = tmpdate;
var vehicleMsgStatCarTypeInfoId = '-1';

function getDeviceId(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var tmpgpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2) {
			tmpgpsDeviceid += idArr[0] + ',';
		}
	}
	if (tmpgpsDeviceid.length > 0) {
		tmpgpsDeviceid = tmpgpsDeviceid.substring(0, tmpgpsDeviceid.length - 1);
	}
	return tmpgpsDeviceid;
}

function getTreeId(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = node;
		}
	}
	node.eachChild(function(child) {
		getTreeId(child,treeArr);
	});
}

var car_type_info_combo = new Ext.form.ComboBox( {
	hiddenName : 'id',
	valueField : 'id',
	store : new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	}),
	displayField : 'name',
    mode: 'local',
	listeners: {
  	    expand : function(combo ) {
	  	  	var count = car_type_info_combo.getStore().getCount();
	  		if(count <=1){
	  	    	carTypeInfoComboexpand();
	  		}
    	}
    },
    //disabled : true,
    editable: false,
    triggerAction: 'all',
    emptyText:'ѡ��������'
});

function carTypeInfoComboexpand(){
	Ext.Ajax.request({
		url : path+'/carTypeInfo/carTypeInfo.do?method=carTypeInfoCombo',
		method :'POST',
		success : function(request) {
			var res = request.responseText;
			car_type_info_combo.getStore().loadData(eval(res));
		},
		failure : function(request) {
		}
	});
}

function searchVehicleMsgStatGrid(){
	var tmp = getDeviceId();
	if(tmp.length<=0){
		Ext.Msg.alert('��ʾ','��ѡ���ն�!');
		return;
	}
	vehicleMsgStatDeviceids = tmp;
	storeLoad(VehicleMsgStatStore, 0, 15, vehicleMsgStatDeviceids, '', '', '', '', false);
}

var VehicleMsgStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/locate/locate.do?method=listLocrecord'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, 
			[{name: 'termName'},{name: 'vehicleNumber'},{name: 'vehicleType'},{name: 'longitude'},{name: 'latitude'},
			 {name: 'gpstime'},{name: 'mqtime'},{name: 'speed'},{name: 'distance'},{name: 'groupName'},{name: 'locDesc'},{name: 'deviceId'},
			 {name: 'signalFlag'},{name: 'programVersion'},{name: 'lastDistance'},
			 {name: 'varExt1'},{name: 'varExt2'},{name: 'varExt3'},{name: 'varExt4'},{name: 'varExt5'},
			 {name: 'varExt6'},{name: 'varExt7'},{name: 'varExt8'},{name: 'varExt9'},{name: 'varExt10'},
			 {name: 'varExt11'},{name: 'varExt12'},{name: 'varExt13'},{name: 'varExt14'},{name: 'varExt15'},
			 {name: 'varExt16'},{name: 'varExt17'},{name: 'varExt18'},{name: 'varExt19'},{name: 'varExt20'},
			 {name: 'varExt21'},{name: 'varExt22'}
			 ]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
						startTime: vehicleMsgStatStartTime, endTime: vehicleMsgStatEndTime, deviceIds: vehicleMsgStatDeviceids
		    	};
			}
		}
	}
});

var VehicleMsgStatGrid = {
    region: 'center',
	xtype: 'grid',
	id: 'VehicleMsgStatGrid',
	store: VehicleMsgStatStore,
	loadMask: {msg: '��ѯ��...'},
	columns: [
	  	{header: '�ն��豸��', width: 130, sortable: true,  dataIndex: 'termName'},
		{header: '�ͻ�����', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: '�����ͺ�', width: 130, sortable: true,  dataIndex: 'vehicleType'},
		{header: '����', width: 130, sortable: true,  dataIndex: 'longitude'},
		{header: 'γ��', width: 130, sortable: true,  dataIndex: 'latitude'},
		{header: '���ش�ʱ��', width: 130, sortable: true,  dataIndex: 'gpstime'},
		{header: 'ä������ʱ', width: 130, sortable: true,  dataIndex: 'mqtime'},
		{header: '�ٶ�', width: 130, sortable: true,  dataIndex: 'speed'},
		{header: '����', width: 130, sortable: true,  dataIndex: 'distance'},
		{header: '����GSM�ź�ǿ��', width: 130, sortable: true,  dataIndex: 'signalFlag'},
		{header: '����������ѹ', width: 130, sortable: true,  dataIndex: 'programVersion'},
		{header: 'ʣ�������ٷֱ�', width: 130, sortable: true,  dataIndex: 'lastDistance',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '%">' + val + '%</span>';
		}},
		{header: '������', width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: 'λ����Ϣ', width: 200, sortable: true,  dataIndex: 'locDesc',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
		}},
		{header: '�̵���2����״̬', width: 130, sortable: true,  dataIndex: 'varExt1',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = 'δ����';
			}else if(c == 1){
				val = '�ȴ�����';
			}else if(c == 2){
				val = '������';
			}
			return val;
		}},
		{header: '�̵���2����ԭ��', width: 130, sortable: true,  dataIndex: 'varExt2',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = 'δ����';
			}else if(c == 1){
				val = '����';
			}
			return val;
		}},
		{header: '�̵���1����״̬', width: 130, sortable: true,  dataIndex: 'varExt3',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = 'δ����';
			}else if(c == 1){
				val = '�ȴ�����';
			}else if(c == 2){
				val = '������';
			}
			return val;
		}},
		{header: '�̵���1����ԭ��', width: 130, sortable: true,  dataIndex: 'varExt4',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = 'δ����';
			}else if(c == 1){
				val = '����';
			}else if(c == 2){
				val = '����';
			}else if(c == 3){
				val = '����';
			}else if(c == 4){
				val = '���';
			}else if(c == 5){
				val = '��Ϊ';
			}
			return val;
		}},
		{header: '����������־', width: 130, sortable: true,  dataIndex: 'varExt5',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '���źź���������';
			}else if(c == 1){
				val = '���߲����������';
			}else if(c == 2){
				val = '����������������';
			}else if(c == 3){
				val = '��Ǵ���������';
			}
			return val;
		}},
		{header: '��ǰ�豸���״̬', width: 130, sortable: true,  dataIndex: 'varExt6',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '�ر�';
			}else if(c == 1){
				val = '��';
			}
			return val;
		}},
		{header: '��ǰ�豸����״̬', width: 130, sortable: true,  dataIndex: 'varExt7',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '����';
			}else if(c == 1){
				val = '�Ͽ�';
			}
			return val;
		}},
		{header: '��ǰ�豸����״̬', width: 130, sortable: true,  dataIndex: 'varExt8',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '����';
			}else if(c == 1){
				val = '�Ͽ�';
			}
			return val;
		}},
		{header: '��ǰ�豸����״̬', width: 130, sortable: true,  dataIndex: 'varExt9',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '����';
			}else if(c == 1){
				val = '��������';
			}
			return val;
		}},
		{header: '������Чλ', width: 130, sortable: true,  dataIndex: 'varExt10',renderer: function tips(val) {
			var c = Number(val);
			if(c == 0){
				val = '��ǰ������Ч';
			}else if(c == 1){
				val = '��ǰ������Ч';
			}
			return val;
		}},
		{header: '��ѹ', width: 130, sortable: true,  dataIndex: 'varExt13'},
		{header: 'ˮ��', width: 130, sortable: true,  dataIndex: 'varExt14'},
		{header: '��λ', width: 130, sortable: true,  dataIndex: 'varExt15'},
		{header: '����', width: 130, sortable: true,  dataIndex: 'varExt16'},
		{header: '��ѹ', width: 130, sortable: true,  dataIndex: 'varExt17'},
		{header: '����Сʱ', width: 130, sortable: true,  dataIndex: 'varExt18'},
		{header: 'ת��', width: 130, sortable: true,  dataIndex: 'varExt19'},
		{header: '����ֵ', width: 130, sortable: true,  dataIndex: 'varExt20',renderer: function tips(val) {
			if(val == 'D6'){
				val = '��ѹ�ͱ���';
			}else if(val == 'D5'){
				val = '�����ͱ���';
			}else if(val == 'D3'){
				val = 'ˮ�±���';
			}else if(val == 'D2'){
				val = '���±���';
			}else if(val == 'D1'){
				val = '���˱���';
			}else if(val == 'D0'){
				val = '���˱���';
			}
			return val;
		}}
		//{header: '��λ', width: 130, sortable: true,  dataIndex: 'varExt21'},
		//{header: '��λ', width: 130, sortable: true,  dataIndex: 'varExt22'}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VehicleMsgStatGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			/*{xtype: 'label',text: '��ʼʱ��'},
			{id: 'vehicleMsgStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label',text: '����ʱ��'},
			{id: 'vehicleMsgStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			car_type_info_combo,
			'-',*/
			{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',handler: function(){
				searchVehicleMsgStatGrid();
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/locate/locate.do?method=listLocrecord&expExcel=true&start=0&limit=65535&deviceIds='+vehicleMsgStatDeviceids,
					success:function(response, opts){ 
						//Ext.Msg.alert("��ʾ", "�����ɹ�");
					},
					form: Ext.fly('excelform'),
					isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert("��ʾ", "����ʧ��");
					},
					callback:function(response, opts){
						//Ext.Msg.alert("��ʾ", "�����ɹ�");
					},
					scope: this
				});
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VehicleMsgStatStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};

var loader = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'	
});

var root = new Ext.tree.AsyncTreeNode({
		text : '',
		id : '-100',
		draggable : false // ���ڵ㲻�����϶�
});

var api = new Ext.tree.TreePanel({
    region: 'west',
    rootVisible:false,
    lines:true,
    autoScroll:true,
    width:170,
    loader: loader,
    root:root
});
api.on('checkchange',function(node,checked){
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
    	//alert('2:'+child);
    	child.ui.toggleCheck(checked);
        child.attributes.checked = checked;
        child.fireEvent('checkchange',child,checked);
    });
},api);
Ext.onReady(function(){
    Ext.QuickTips.init();
    var viewport = new Ext.Viewport({layout: 'border',items: [ api, VehicleMsgStatGrid ]});
});
