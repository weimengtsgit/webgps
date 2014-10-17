var costDetailStartTime = tmpdate+' '+'00:00:00';
var costDetailEndTime = tmpdate+' '+'23:59:59';
var costDetailGpsDeviceids = '';
var costDetailApproved = -1;
function initCostDetailGrid(deviceID_){
	costDetailGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('costDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('costDetailedf').getValue().format('Y-m-d');
	//costDetailPoiName = Ext.getCmp('costDetaildif').getValue();
	costDetailApproved = Ext.getCmp('costDetailApproved').getValue();
	costDetailStartTime = tmpstartdate+' '+'00:00:00';
	costDetailEndTime = tmpenddate+' '+'23:59:59';
}

function searchCostDetailGrid(){
	storeLoad(CostDetailStore, 0, 15, costDetailGpsDeviceids, '', costDetailStartTime, costDetailEndTime, costDetailApproved, false);
}

var CostDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/cost/cost.do?method=listCostDetails'}),
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


var costDetailSm = new Ext.grid.CheckboxSelectionModel({});
var CostDetailGrid = {
	xtype: 'grid',
	id: 'CostDetailGrid',
	store: CostDetailStore,
	loadMask: {msg: main.loading},
	columns: [
	 	 costDetailSm,
        {header: '����', width: 130, sortable: true,  dataIndex: 'createOn'},
        {header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: 'Ա������', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
        {header: '�ͷ�', width: 100, sortable: true, dataIndex: 'meal'},
        {header: '��ͨ��', width: 100, sortable: true, dataIndex: 'transportation'},
        {header: 'ס�޷�', width: 100, sortable: true, dataIndex: 'accommodation'},
        {header: 'ͨ�ŷ�', width: 100, sortable: true, dataIndex: 'communication'},
        {header: '��Ʒ��', width: 100, sortable: true, dataIndex: 'gift'},
        {header: '����', width: 100, sortable: true, dataIndex: 'other'},
        {header: '�ܽ��', width: 100, sortable: true, dataIndex: 'costAmount', renderer: function (value, meta, record) {
        	var costAmount_ = 0;
        	costAmount_ = costAmount_ + Number(record.get('meal'));
        	costAmount_ = costAmount_ + Number(record.get('transportation'));
        	costAmount_ = costAmount_ + Number(record.get('accommodation'));
        	costAmount_ = costAmount_ + Number(record.get('communication'));
        	costAmount_ = costAmount_ + Number(record.get('gift'));
        	costAmount_ = costAmount_ + Number(record.get('other'));
        	return costAmount_;
        }},
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
    sm : costDetailSm,
	stripeRows: true,
	stateful: true,
	stateId: 'CostDetailGridId',
	//autoExpandColumn: 'vehicleNumber',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'costDetailsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'costDetailstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'costDetailedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'costDetailetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			//{xtype: 'label', text: '�ͻ�����'},
			//{id: 'costDetaildif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
			//	if (e.getKey() == e.ENTER) {initCostDetailGrid();searchCostDetailGrid();}
			//}}},'-',
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
					initCostDetailGrid(deviceID_);
					searchCostDetailGrid();
				}
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					initCostDetailGrid(deviceID_);
					var excelhtml = path+'/cost/cost.do?method=listCostDetailsExpExcel&startTime='+costDetailStartTime+'&endTime='+costDetailEndTime+'&deviceIds='+costDetailGpsDeviceids+'&duration='+costDetailApproved;
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
				var tmpRecArr = costDetailSm.getSelections();
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
		store: CostDetailStore,
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
					CostDetailStore.reload();
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
