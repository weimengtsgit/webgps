var signBillDetailStartTime = tmpdate+' '+'00:00:00';
var signBillDetailEndTime = tmpdate+' '+'23:59:59';
var signBillDetailGpsDeviceids = '';
var signBillDetailPoiName = '';
var signBillDetailApproved = -1;

function initSignBillDetailGrid(deviceID_){
	signBillDetailGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('signBillDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('signBillDetailedf').getValue().format('Y-m-d');
	signBillDetailPoiName = Ext.getCmp('signBillDetaildif').getValue();
	signBillDetailApproved = Ext.getCmp('signBillDetailApproved').getValue();
	signBillDetailStartTime = tmpstartdate+' '+'00:00:00';
	signBillDetailEndTime = tmpenddate+' '+'23:59:59';
}

function searchSignBillDetailGrid(){
	storeLoad(SignBillDetailStore, 0, 15, signBillDetailGpsDeviceids, signBillDetailPoiName, signBillDetailStartTime, signBillDetailEndTime, signBillDetailApproved, false);
}

var SignBillDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/signBill/signBill.do?method=listSignBillDetails'}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success', 
		idProperty: 'a', 
		root: 'data' 
	}, [{name : 'id'},
		{name : 'createOn'},
		{name : 'groupName'},
		{name : 'vehicleNumber'},
		{name : 'poiName'},
		{name : 'signBillAmount'},
		{name : 'approved'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(signBillDetailPoiName), startTime: signBillDetailStartTime, endTime: signBillDetailEndTime, deviceIds: signBillDetailGpsDeviceids
		    	};
			}
		}
	}
});


var signBillDetailSm = new Ext.grid.CheckboxSelectionModel({});
var SignBillDetailGrid = {
	xtype: 'grid',
	id: 'SignBillDetailGrid',
	store: SignBillDetailStore,
	loadMask: {msg: main.loading},
	columns: [
	     signBillDetailSm,
        {header: '����', width: 130, sortable: true,  dataIndex: 'createOn'},
        {header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: 'Ա������', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
        {header: '�ͻ�����', width: 100, sortable: true, dataIndex: 'poiName'},
        {header: 'ǩ�����', width: 100, sortable: true, dataIndex: 'signBillAmount'},
        {header: '״̬', width: 100, sortable: true, dataIndex: 'approved', renderer: function (value, meta, record) {
        	if(value == '0'){
        		return "δ���";
        	}else{
        		return "���";
        	}
        }}
	],
    sm : signBillDetailSm,
	stripeRows: true,
	stateful: true,
	stateId: 'SignBillDetailGridId',
	//autoExpandColumn: 'vehicleNumber',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'signBillDetailsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'signBillDetailstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'signBillDetailedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'signBillDetailetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			{xtype: 'label', text: '�ͻ�����'},
			{id: 'signBillDetaildif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {
					var deviceID_ = getDeviceId();
					if(deviceID_ == ''){
						Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
						return;
					}else{
						initSignBillDetailGrid(deviceID_);
						searchSignBillDetailGrid();
					}
				}
			}}},'-',
			{xtype: 'label', text: '״̬'},
			new Ext.form.ComboBox({
				width: 60,
				id : 'signBillDetailApproved',
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
					initSignBillDetailGrid(deviceID_);
					searchSignBillDetailGrid();
				}
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					initSignBillDetailGrid(deviceID_);
					var excelhtml = path+'/signBill/signBill.do?method=listSignBillDetailsExpExcel&searchValue='+encodeURI(encodeURI(signBillDetailPoiName))+'&startTime='+signBillDetailStartTime+'&endTime='+signBillDetailEndTime+'&deviceIds='+signBillDetailGpsDeviceids+'&duration='+signBillDetailApproved;
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
				var tmpRecArr = signBillDetailSm.getSelections();
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
				approvedSignBillids = ids;
	        	Ext.MessageBox.confirm('��ʾ', 'ȷ��ͨ�������?', approvedSignBillConfirm);
	        	
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: SignBillDetailStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};

var approvedSignBillids;
function approvedSignBillConfirm(btn){
	if(btn=='yes'){
		Ext.Msg.show({
           msg: '�����ύ ���Ե�...',
           progressText: '�ύ...',
           width:300,
           wait:true,
           icon:'ext-mb-download'
       });
		Ext.Ajax.request({
			url:path+'/signBill/signBill.do?method=approved',
			method :'POST', 
			params: { ids: encodeURI(approvedSignBillids)},
			//timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if(res.result==1){
					SignBillDetailStore.reload();
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
