var delids='';
var delvehicleMsgIds = '';
var searchValue = '';

var delbut = new Ext.Action({
		        text: '删除',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	var tmpRecArr = sm.getSelections();
					var ids = '';
					var vehicleMsgIds = '';
					for(var i=0;i<tmpRecArr.length;i++){
						ids+=tmpRecArr[i].get('id')+',';
						vehicleMsgIds+=tmpRecArr[i].get('vehicleMsgId')+',';
						
					}
					if(ids.length>0){
						ids = ids.substring(0,ids.length-1);
					}else{
						parent.Ext.MessageBox.alert('提示', '请选择要删除的终端!');
		        		return ;
					}
					if(vehicleMsgIds.length>0){
						vehicleMsgIds = vehicleMsgIds.substring(0,vehicleMsgIds.length-1);
					}
					delids = ids;
					delvehicleMsgIds = vehicleMsgIds;
		        	parent.Ext.MessageBox.confirm('提示', '您确定要删除选择的终端吗?', addConfirm);
		        },
		        iconCls: 'icon-del'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '正在删除 请稍等...',
			           progressText: '删除...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    


//添加部门
function add(){

	Ext.Ajax.request({
		 //url :url,
		 url:path+'/terminal/terminal.do?method=deleteTerminals&ids='+delids+'&vehicleMsgIds='+delvehicleMsgIds,
		 method :'POST', 
		 //params: { ctl:'add', parentid : tmpfrmParendId ,starttime:tmpfrmstarttime,endtime:tmpfrmendtime},
		 timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		store.reload();
		   		parent.Ext.Msg.alert('提示', '删除成功');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "删除失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "删除失败!");
		 }
		});
}
function locateTypeRenderer(val){
	if(val=='0'){
		return '人员';
	}else if(val=='1'){
		return 'GPS';
	}else{
		return 'GPS';
	}
}

var sm = new Ext.grid.CheckboxSelectionModel({});

var proxy = new Ext.data.HttpProxy({
    url: path+'/terminal/terminal.do?method=listTerminal'
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
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'terminalstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			};
    		}
    	}
    }
});


Ext.onReady(function(){




var userColumns =  [
sm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "SIM卡号",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "终端序列号",fixed:true, width: 100, sortable: true, dataIndex: 'deviceId'},
    {header: "终端设备号",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "所属组名称",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "groupid",fixed:true, width: 100, sortable: true, dataIndex: 'groupId',hidden:true},
    {header: "省",fixed:true, width: 70, sortable: true, dataIndex: 'province'},
    {header: "市",fixed:true, width: 70, sortable: true, dataIndex: 'city'},
    {header: "上班时间",fixed:true, width: 100, sortable: true, dataIndex: 'startTime'},
    {header: "下班时间",fixed:true, width: 100, sortable: true, dataIndex: 'endTime'},
    {header: "周工作时间",fixed:true, width: 160, sortable: true, dataIndex: 'week', renderer:function (val){
		var tmpweek = '';
		if((Number(val) & 1)==1){ tmpweek += '周1,'; }
		if((Number(val) & 2)==2){ tmpweek += '周2,'; }
		if((Number(val) & 4)==4){ tmpweek += '周3,'; }
		if((Number(val) & 8)==8){ tmpweek += '周4,'; }
		if((Number(val) & 16)==16){ tmpweek += '周5,'; }
		if((Number(val) & 32)==32){ tmpweek += '周6,'; }
		if((Number(val) & 64)==64){ tmpweek += '周日,'; }
		if(tmpweek.length>0){ tmpweek = tmpweek.substring(0,tmpweek.length-1); }
		return tmpweek;
	}},
    {header: "采集间隔",fixed:true, width: 100, sortable: true, dataIndex: 'getherInterval'},
    {header: "员工备注",fixed:true, width: 100, sortable: true, dataIndex: 'termDesc'},
    {header: "终端类型",fixed:true, width: 100, sortable: true, dataIndex: 'locateType',renderer:locateTypeRenderer},
    {header: "GPS厂商",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    //{header: "GPS厂商",fixed:true, width: 100, sortable: true, dataIndex: 'typeCode'},
    {header: "客户姓名",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "车辆型号",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleType'},
    {header: "客户手机",fixed:true, width: 100, sortable: true, dataIndex: 'driverNumber'},
    //{header: "断油断电-速度限值",fixed:true, width: 100, sortable: true, dataIndex: 'oilSpeedLimit'},
    //{header: "劫警",fixed:true, width: 100, sortable: true, dataIndex: 'holdAlarmFlag'},
    //{header: "超速报警-速度限值",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLimit'},
    //{header: "超速报警-持续时间",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLast'}
    {header: "机械型号",fixed:true, width: 100, sortable: true, dataIndex: 'model'},
    {header: "机号(车架号)",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNum'},
    {header: "销售日期(提机日)",fixed:true, width: 100, sortable: true, dataIndex: 'saleDate'},
    {header: "销售方式",fixed:true, width: 100, sortable: true, dataIndex: 'saleMethods'},
    {header: "经销商",fixed:true, width: 100, sortable: true, dataIndex: 'dealer'},
    {header: "安装人员",fixed:true, width: 100, sortable: true, dataIndex: 'installers'},
    {header: "合同金额(元)",fixed:true, width: 100, sortable: true, dataIndex: 'contractValue'},
    {header: "贷款金额(元)",fixed:true, width: 100, sortable: true, dataIndex: 'loanAmount'},
    {header: "还款期限",fixed:true, width: 100, sortable: true, dataIndex: 'repaymentPeriod'},
    {header: "债权担当",fixed:true, width: 100, sortable: true, dataIndex: 'claimAct'}
];

var userGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 500,
        loadMask: {msg:'查询中...'},
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
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20, searchValue: searchValue }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '查询',
		        handler: function(){
		    		searchValue = Ext.getCmp('DeviceIdField').getValue();
	    			store.load({params:{start:0, limit:20, searchValue: searchValue }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });
	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    

    
});