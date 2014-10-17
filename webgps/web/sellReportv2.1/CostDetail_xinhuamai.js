var costDetailStartTime = tmpdate+' '+'00:00:00';
var costDetailEndTime = tmpdate+' '+'23:59:59';
var costDetailGpsDeviceids = '';
var costDetailApproved = -1;
function initCostDetailGrid_xinhuamai(deviceID_){
	costDetailGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('costDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('costDetailedf').getValue().format('Y-m-d');
	//costDetailPoiName = Ext.getCmp('costDetaildif').getValue();
	costDetailApproved = Ext.getCmp('costDetailApproved').getValue();
	costDetailStartTime = tmpstartdate+' '+'00:00:00';
	costDetailEndTime = tmpenddate+' '+'23:59:59';
}

function searchCostDetailGrid_xinhuamai(){
	storeLoad(CostDetailStore_xinhuamai, 0, 15, costDetailGpsDeviceids, '', costDetailStartTime, costDetailEndTime, costDetailApproved, false);
}

var CostDetailStore_xinhuamai = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/cost/cost.do?method=listCostDetailsForXinHuaMai'}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success', 
		idProperty: 'a', 
		root: 'data' 
	}, [{name : 'id'},
		{name : 'createOn'},
		{name : 'groupName'},
		{name : 'vehicleNumber'},
		{name : 'meal'},
		{name : 'transportation'},
		{name : 'accommodation'},
		{name : 'communication'},
		{name : 'gift'},
		{name : 'other'},
		{name : 'expand1'},
		{name : 'expand2'},
		{name : 'expand3'},
		{name : 'expand4'},
		{name : 'expand5'},
		{name : 'remarks'},
		{name : 'dateTime'},
		{name : 'costAmount'},
		{name : 'approved'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: costDetailStartTime, endTime: costDetailEndTime, deviceIds: costDetailGpsDeviceids
				};
			}
		}
	}
});


var costDetailSm_xinhuamai = new Ext.grid.CheckboxSelectionModel({});
var CostDetailGrid_xinhuamai = {
	xtype: 'grid',
	id: 'CostDetailGrid_xinhuamai',
	store: CostDetailStore_xinhuamai,
	loadMask: {msg: main.loading},
	columns: [
	 	 costDetailSm_xinhuamai,
        {header: '����', width: 130, sortable: true,  dataIndex: 'createOn'},
        {header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: 'Ա������', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
        {header: '��ͨ��', width: 100, sortable: true, dataIndex: 'meal'},
        {header: 'ס�޷�', width: 100, sortable: true, dataIndex: 'transportation'},
        {header: '������', width: 100, sortable: true, dataIndex: 'accommodation'},
        {header: 'Ͷ���', width: 100, sortable: true, dataIndex: 'communication'},
        {header: '�����', width: 100, sortable: true, dataIndex: 'gift'},
        {header: '�����˷�', width: 100, sortable: true, dataIndex: 'other'},
        {header: '��ݷ�', width: 100, sortable: true, dataIndex: 'expand1'},
        {header: '������', width: 100, sortable: true, dataIndex: 'expand2'},
        {header: '�д���', width: 100, sortable: true, dataIndex: 'expand3'},
        {header: '�����', width: 100, sortable: true, dataIndex: 'expand4'},
        {header: 'չ��չλ��', width: 100, sortable: true, dataIndex: 'expand5'},
        {header: '�ܽ��', width: 100, sortable: true, dataIndex: 'costAmount'},
        {header: coshRemarksHeader, width: 100, sortable: true, dataIndex: 'remarks', hidden: costRemarks},
        {header: costDateTiemHeader, width: 130, sortable: true, dataIndex: 'dateTime', hidden: costDateTime},
        {header: '״̬', width: 100, sortable: true, dataIndex: 'approved', renderer: function (value, meta, record) {
        	if(value == '0'){
        		return "δ���";
        	}else{
        		return "���";
        	}
        }}
	],
    sm : costDetailSm_xinhuamai,
	stripeRows: true,
	stateful: true,
	stateId: 'CostDetailGridId',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'costDetailsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'costDetailedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			{xtype: 'label', text: '״̬'},
			new Ext.form.ComboBox({
				width: 60,
				id : 'costDetailApproved',
				enableKeyEvents : true,
				grow : true,
				valueField : "id",
				displayField : "name",
				mode : 'local',// �������ڱ���
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				value: -1,
				store : new Ext.data.SimpleStore({
					fields : [ "name", "id" ],
					data : [[ '--ȫ��--', '-1' ], [ '���', '1' ], [ 'δ���', '0' ]]
				})
			}),'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					initCostDetailGrid_xinhuamai(deviceID_);
					searchCostDetailGrid_xinhuamai();
				}
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					initCostDetailGrid_xinhuamai(deviceID_);
					var excelhtml = path+'/cost/cost.do?method=listCostDetailsExpExcelForXinHuaMai&startTime='+costDetailStartTime+'&endTime='+costDetailEndTime+'&deviceIds='+costDetailGpsDeviceids+'&duration='+costDetailApproved;
					var win = new Ext.Window({
						title : '�����ļ�',
						closable : true,
						closeAction : 'close',
						autoWidth : false,
						width : 200,
						heigth : 150,
						items : [new Ext.Panel({
							html : "<a href='"+excelhtml+"' target='_blank' onclick='Ext.Msg.hide()'>������������ر���</a>",
							frame : true
						})]
					});
					win.show();
				}
				
			}},'-',
			{xtype: 'button',text: '���',iconCls: 'icon-saved',handler: function(){
				var tmpRecArr = costDetailSm_xinhuamai.getSelections();
				var ids = '';
				for(var i=0;i<tmpRecArr.length;i++){
					ids+=tmpRecArr[i].get('id')+',';
				}
				if(ids.length>0){
					ids = ids.substring(0,ids.length-1);
				}else{
					Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ��˵ļ�¼!');
	        		return ;
				}
				approvedCostids = ids;
	        	Ext.MessageBox.confirm('��ʾ', 'ȷ��ͨ�������?', approvedCostConfirm);
	        	
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: CostDetailStore_xinhuamai,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};


var approvedCostids;
function approvedCostConfirm(btn){
	if(btn=='yes'){
		Ext.Msg.show({
           msg: '�����ύ ���Ե�...',
           progressText: '�ύ...',
           width:300,
           wait:true,
           icon:'ext-mb-download'
       });
		Ext.Ajax.request({
			url:path+'/cost/cost.do?method=approved',
			method :'POST', 
			params: { ids: encodeURI(approvedCostids)},
			//timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if(res.result==1){
					CostDetailStore_xinhuamai.reload();
					Ext.Msg.alert('��ʾ', '�����ɹ�');
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
}
