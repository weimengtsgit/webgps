var delids='';
var delvehicleMsgIds = '';
var searchValue = '';
var vehicleMsgStatDeviceids = '';

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
function searchVehicleMsgStatGrid(){
	var tmp = getDeviceId();
	if(tmp.length<=0){
		Ext.Msg.alert('��ʾ','��ѡ���ն�!');
		return;
	}
	vehicleMsgStatDeviceids = tmp;
	searchValue = Ext.getCmp('DeviceIdField').getValue();
	storeLoad(store, 0, 15, vehicleMsgStatDeviceids, searchValue, '', '', '', false);
}

function locateTypeRenderer(val){
	if(val=='0'){
		return '��Ա';
	}else if(val=='1'){
		return 'GPS';
	}else{
		return 'GPS';
	}
}

var sm = new Ext.grid.CheckboxSelectionModel({});

var proxy = new Ext.data.HttpProxy({
    url: path+'/terminal/terminal.do?method=listTerminalByDeviceId'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'simcard'},
    {name: 'deviceId'},
    {name: 'termName'},
    {name: 'groupName'},
    {name: 'groupId'},
    {name: 'province'},
    {name: 'city'},
    {name: 'startTime'},
    {name: 'endTime'},
    {name: 'getherInterval'},
    {name: 'termDesc'},
    {name: 'locateType'},
    {name: 'typeCode'},
    {name: 'typeName'},
    {name: 'vehicleNumber'},
    {name: 'vehicleType'},
    {name: 'driverNumber'},
    {name: 'oilSpeedLimit'},
    {name: 'holdAlarmFlag'},
    {name: 'speedAlarmLimit'},
    {name: 'speedAlarmLast'},
    {name: 'week'},
    {name: 'vehicleMsgId'},
    {name: 'model'},
    {name: 'vehicleNum'},
    {name: 'saleDate'},
    {name: 'saleMethods'},
    {name: 'dealer'},
    {name: 'installers'},
    {name: 'contractValue'},
    {name: 'loanAmount'},
    {name: 'repaymentPeriod'},
    {name: 'claimAct'},
    {name: 'crtDate'}
    
]);

var store = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'terminalstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue),deviceIds : vehicleMsgStatDeviceids
    			};
    		}
    	}
    }
});



var userColumns =  [
sm,
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "SIM����",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "�ն����к�",fixed:true, width: 100, sortable: true, dataIndex: 'deviceId'},
    {header: "�ն��豸��",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "����������",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "groupid",fixed:true, width: 100, sortable: true, dataIndex: 'groupId',hidden:true},
    {header: "ʡ",fixed:true, width: 70, sortable: true, dataIndex: 'province'},
    {header: "��",fixed:true, width: 70, sortable: true, dataIndex: 'city'},
    //{header: "�ϰ�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'startTime'},
    //{header: "�°�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'endTime'},
    /*{header: "�ܹ���ʱ��",fixed:true, width: 160, sortable: true, dataIndex: 'week', renderer:function (val){
		var tmpweek = '';
		if((Number(val) & 1)==1){ tmpweek += '��1,'; }
		if((Number(val) & 2)==2){ tmpweek += '��2,'; }
		if((Number(val) & 4)==4){ tmpweek += '��3,'; }
		if((Number(val) & 8)==8){ tmpweek += '��4,'; }
		if((Number(val) & 16)==16){ tmpweek += '��5,'; }
		if((Number(val) & 32)==32){ tmpweek += '��6,'; }
		if((Number(val) & 64)==64){ tmpweek += '����,'; }
		if(tmpweek.length>0){ tmpweek = tmpweek.substring(0,tmpweek.length-1); }
		return tmpweek;
	}},*/
    //{header: "�ɼ����",fixed:true, width: 100, sortable: true, dataIndex: 'getherInterval'},
    {header: "Ա����ע",fixed:true, width: 100, sortable: true, dataIndex: 'termDesc'},
    {header: "�ն�����",fixed:true, width: 100, sortable: true, dataIndex: 'locateType',renderer:locateTypeRenderer},
    {header: "GPS����",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    //{header: "GPS����",fixed:true, width: 100, sortable: true, dataIndex: 'typeCode'},
    {header: "�ͻ�����",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "�����ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleType'},
    {header: "�ͻ��ֻ�",fixed:true, width: 100, sortable: true, dataIndex: 'driverNumber'},
    //{header: "���Ͷϵ�-�ٶ���ֵ",fixed:true, width: 100, sortable: true, dataIndex: 'oilSpeedLimit'},
    //{header: "�پ�",fixed:true, width: 100, sortable: true, dataIndex: 'holdAlarmFlag'},
    //{header: "���ٱ���-�ٶ���ֵ",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLimit'},
    //{header: "���ٱ���-����ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLast'}
    {header: "��е�ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'model'},
    {header: "����(���ܺ�)",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNum'},
    {header: "��������(�����)",fixed:true, width: 100, sortable: true, dataIndex: 'saleDate'},
    {header: "���۷�ʽ",fixed:true, width: 100, sortable: true, dataIndex: 'saleMethods'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'dealer'},
    {header: "��װ��Ա",fixed:true, width: 100, sortable: true, dataIndex: 'installers'},
    {header: "��ͬ���(Ԫ)",fixed:true, width: 100, sortable: true, dataIndex: 'contractValue'},
    {header: "������(Ԫ)",fixed:true, width: 100, sortable: true, dataIndex: 'loanAmount'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'repaymentPeriod'},
    {header: "ծȨ����",fixed:true, width: 100, sortable: true, dataIndex: 'claimAct'}
];

var userGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 500,
        loadMask: {msg:'��ѯ��...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: store,
         sm : sm,
        //plugins: [editor],
        columns : userColumns,
        //sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
		tbar: [
        	new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			//searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			//store.load({params:{start:0, limit:20, searchValue: searchValue }});
			    			searchVehicleMsgStatGrid();
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		    		//searchValue = Ext.getCmp('DeviceIdField').getValue();
	    			//store.load({params:{start:0, limit:20, searchValue: searchValue }});
	    			searchVehicleMsgStatGrid();
		        },
		        iconCls: 'icon-search'
		    }),'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				 
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/terminal/terminal.do?method=listTerminalByDeviceId&expExcel=true&start=0&limit=65535&deviceIds='+vehicleMsgStatDeviceids+'&searchValue='+encodeURI(searchValue),
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
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });
	
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
    var viewport = new Ext.Viewport({layout: 'border',items: [api, userGrid]});
    
});