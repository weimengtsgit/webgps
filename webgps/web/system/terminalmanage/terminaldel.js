var delids='';
var delvehicleMsgIds = '';
var searchValue = '';

var delbut = new Ext.Action({
		        text: 'ɾ��',
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
						parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ�����ն�!');
		        		return ;
					}
					if(vehicleMsgIds.length>0){
						vehicleMsgIds = vehicleMsgIds.substring(0,vehicleMsgIds.length-1);
					}
					delids = ids;
					delvehicleMsgIds = vehicleMsgIds;
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��ѡ����ն���?', addConfirm);
		        },
		        iconCls: 'icon-del'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '����ɾ�� ���Ե�...',
			           progressText: 'ɾ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    


//��Ӳ���
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
		   		parent.Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		 }
		});
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
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "SIM����",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "�ն����к�",fixed:true, width: 100, sortable: true, dataIndex: 'deviceId'},
    {header: "�ն��豸��",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "����������",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "groupid",fixed:true, width: 100, sortable: true, dataIndex: 'groupId',hidden:true},
    {header: "ʡ",fixed:true, width: 70, sortable: true, dataIndex: 'province'},
    {header: "��",fixed:true, width: 70, sortable: true, dataIndex: 'city'},
    {header: "�ϰ�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'startTime'},
    {header: "�°�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'endTime'},
    {header: "�ܹ���ʱ��",fixed:true, width: 160, sortable: true, dataIndex: 'week', renderer:function (val){
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
	}},
    {header: "�ɼ����",fixed:true, width: 100, sortable: true, dataIndex: 'getherInterval'},
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
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20, searchValue: searchValue }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
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
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });
	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
    

    
});