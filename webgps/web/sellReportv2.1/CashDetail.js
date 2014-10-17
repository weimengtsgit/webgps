var cashDetailStartTime = tmpdate+' '+'00:00:00';
var cashDetailEndTime = tmpdate+' '+'23:59:59';
var cashDetailGpsDeviceids = '';
var cashDetailPoiName = '';
var cashDetailApproved = -1;

function initCashDetailGrid(deviceID_){	
	cashDetailGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('cashDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('cashDetailedf').getValue().format('Y-m-d');
	cashDetailPoiName = Ext.getCmp('cashDetaildif').getValue();
	cashDetailApproved = Ext.getCmp('cashDetailApproved').getValue();
	cashDetailStartTime = tmpstartdate+' '+'00:00:00';
	cashDetailEndTime = tmpenddate+' '+'23:59:59';

}

function searchCashDetailGrid(){
	storeLoad(CashDetailStore, 0, 15, cashDetailGpsDeviceids, cashDetailPoiName, cashDetailStartTime, cashDetailEndTime, cashDetailApproved, false);
}

var CashDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/cash/cash.do?method=listCashDetails'}),
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
		{name : 'cashAmount'},{name : 'cashAmount2'},{name : 'cashAmount3'},{name : 'cashAmount4'},{name : 'cashAmount5'},
		{name : 'cashAmount6'},{name : 'cashAmount7'},{name : 'cashAmount8'},{name : 'cashAmount9'},{name : 'cashAmount10'},
		{name : 'cashAmount11'},
		{name : 'approved'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
						searchValue: encodeURI(cashDetailPoiName), startTime: cashDetailStartTime, endTime: cashDetailEndTime, deviceIds: cashDetailGpsDeviceids
		    	};
			}
		}
	}
});


var cashDetailSm = new Ext.grid.CheckboxSelectionModel({});
var CashDetailGrid = {
	xtype: 'grid',
	id: 'CashDetailGrid',
	store: CashDetailStore,
	loadMask: {msg: main.loading},
	columns: [
	 	 cashDetailSm,
        {header: '����', width: 130, sortable: true,  dataIndex: 'createOn'},
        {header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: 'Ա������', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
        {header: '�ͻ�����', width: 100, sortable: true, dataIndex: 'poiName'},
        {header: cashAmount0Header, width: 100, sortable: true, dataIndex: 'cashAmount0', hidden: cashAmount0c, renderer: function (value, meta, record) {
        	var cashAmount_ = record.get('cashAmount');
        	var cashAmount2_ = record.get('cashAmount2');
        	var cashAmount4_ = record.get('cashAmount4');
        	var cashAmount5_ = record.get('cashAmount5');
        	var cashAmount6_ = record.get('cashAmount6');
        	var cashAmount7_ = record.get('cashAmount7');
        	var cashAmount8_ = record.get('cashAmount8');
        	var cashAmount9_ = record.get('cashAmount9');
        	var cashAmount10_ = record.get('cashAmount10');
        	var cashAmount11_ = record.get('cashAmount11');
        	var val_ = Number(cashAmount_) * Number(cashAmount2_) + Number(cashAmount4_) * Number(cashAmount5_) + Number(cashAmount6_) * Number(cashAmount7_) + Number(cashAmount8_) + Number(cashAmount9_) + Number(cashAmount10_) + Number(cashAmount11_);
        	return val_;
        	
        }},
        {header: cashAmountHeader, width: 100, sortable: true, dataIndex: 'cashAmount', hidden: cashAmountc},
        {header: cashAmount2Header, width: 100, sortable: true, dataIndex: 'cashAmount2', hidden: cashAmount2c},
        {header: cashAmount3Header, width: 100, sortable: true, dataIndex: 'cashAmount3', hidden: cashAmount3c, renderer: function (value, meta, record) {
        	if(value == '0'){
        		return "";
        	}else if(value == '1'){
        		return "��ǩ";
        	}else if(value == '2'){
        		return "����";
        	}else if(value == '3'){
        		return "����";
        	}
        }},
        {header: cashAmount4Header, width: 100, sortable: true, dataIndex: 'cashAmount4', hidden: cashAmount4c},
        {header: cashAmount5Header, width: 100, sortable: true, dataIndex: 'cashAmount5', hidden: cashAmount5c},
        {header: cashAmount6Header, width: 100, sortable: true, dataIndex: 'cashAmount6', hidden: cashAmount6c},
        {header: cashAmount7Header, width: 100, sortable: true, dataIndex: 'cashAmount7', hidden: cashAmount7c},
        {header: cashAmount8Header, width: 100, sortable: true, dataIndex: 'cashAmount8', hidden: cashAmount8c},
        {header: cashAmount9Header, width: 100, sortable: true, dataIndex: 'cashAmount9', hidden: cashAmount9c},
        {header: cashAmount10Header, width: 100, sortable: true, dataIndex: 'cashAmount10', hidden: cashAmount10c},
        {header: cashAmount11Header, width: 100, sortable: true, dataIndex: 'cashAmount11', hidden: cashAmount11c},
        {header: '״̬', width: 100, sortable: true, dataIndex: 'approved', renderer: function (value, meta, record) {
        	if(value == '0'){
        		return "δ���";
        	}else{
        		return "���";
        	}
        }}
	],
    sm : cashDetailSm,
	stripeRows: true,
	stateful: true,
	stateId: 'CashDetailGridId',
	//autoExpandColumn: 'vehicleNumber',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'cashDetailsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'cashDetailstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'cashDetailedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'cashDetailetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			{xtype: 'label', text: '�ͻ�����'},
			{id: 'cashDetaildif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {
					var deviceID_ = getDeviceId();
					if(deviceID_ == ''){
						Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
						return;
					}else{
						initCashDetailGrid(deviceID_);
						searchCashDetailGrid();
					}
				}
			}}},'-',
			{xtype: 'label', text: '״̬'},
			new Ext.form.ComboBox({
				width: 60,
				id : 'cashDetailApproved',
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
					initCashDetailGrid(deviceID_);
					searchCashDetailGrid();
				}
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					initCashDetailGrid(deviceID_);
					var excelhtml = path+'/cash/cash.do?method=listCashDetailsExpExcel&searchValue='+encodeURI(encodeURI(cashDetailPoiName))+'&startTime='+cashDetailStartTime+'&endTime='+cashDetailEndTime+'&deviceIds='+cashDetailGpsDeviceids+'&duration='+cashDetailApproved;
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
				var tmpRecArr = cashDetailSm.getSelections();
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
				approvedCashids = ids;
	        	Ext.MessageBox.confirm('��ʾ', 'ȷ��ͨ�������?', approvedCashConfirm);
	        	
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: CashDetailStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};


var approvedCashids;
function approvedCashConfirm(btn){
	if(btn=='yes'){
		Ext.Msg.show({
           msg: '�����ύ ���Ե�...',
           progressText: '�ύ...',
           width:300,
           wait:true,
           icon:'ext-mb-download'
       });
		Ext.Ajax.request({
			url:path+'/cash/cash.do?method=approved',
			method :'POST', 
			params: { ids: encodeURI(approvedCashids)},
			//timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if(res.result==1){
					CashDetailStore.reload();
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
