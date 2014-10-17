var vehicleMsgStatDeviceids = '';
var vehicleMsgStatSearchValue = '';
var vehicleMsgStatStartTime = tmpdate;
var vehicleMsgStatEndTime = tmpdate;
var vehicleMsgStatCarTypeInfoId = '-1';

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
	var tmpstartdate = Ext.getCmp('vehicleMsgStatsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('vehicleMsgStatedf').getValue().format('Y-m-d');
	vehicleMsgStatStartTime = tmpstartdate;
	vehicleMsgStatEndTime = tmpenddate;
	vehicleMsgStatDeviceids = getDeviceId();
	vehicleMsgStatCarTypeInfoId = car_type_info_combo.getValue();
	storeLoad(VehicleMsgStatStore, 0, 15, vehicleMsgStatDeviceids, '', vehicleMsgStatStartTime, vehicleMsgStatEndTime, vehicleMsgStatCarTypeInfoId, false);
}

var VehicleMsgStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/vehicleExpense/vehicleExpense.do?method=listVehicleMsg'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, 
			[{name: 'vehicleDepreciation'},{name: 'personalExpenses'},{name: 'mileagenorm'},{name: 'expensenorm'},{name: 'returnnorm'},{name: 'dispatchamount'},{name: 'insurance'},{name: 'maintenance'},{name: 'toll'},
			 {name: 'annualCheck'},{name: 'oilLiter'},{name: 'oilCost'},{name: 'distance'},{name: 'deviceId'},{name: 'typeName'},
			 {name: 'oilWear'},{name: 'termName'},{name: 'vehicleNumber'},{name: 'simcard'},{name: 'groupName'},
			 {name: 'oilperkm'},{name: 'costperkm'}]),
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
			var colModel = new Ext.grid.ColumnModel({
//				listeners: {
//					configchange : function(cm, colIndex, hidden) {
////			            saveConfig(colIndex,);
//						var col = cm.getColumnHeader(colIndex);
//						alert(col);
//			        }
//			    },
				columns: [
						  	{header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
							{header: '����', width: 130, sortable: true,  dataIndex: 'termName'},
							{header: '���ƺ�', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
							{header: '�����ͺ�', width: 130, sortable: true,  dataIndex: 'typeName'},
							{header: '�ֻ�����', width: 130, sortable: true,  dataIndex: 'simcard'},
							//{header: '��ע', width: 130, sortable: true,  dataIndex: 'termDesc'},
							{header: '������(��)', width: 130, sortable: true,  dataIndex: 'oilLiter'},
							{header: '���ͽ��', width: 130, sortable: true,  dataIndex: 'oilCost'},
							{header: '�����۾�', width: 130, sortable: true,  dataIndex: 'vehicleDepreciation'},
							{header: '��Ա����', width: 130, sortable: true,  dataIndex: 'personalExpenses'},
							{header: '��̱�׼', width: 130, sortable: true,  dataIndex: 'mileagenorm'},
							{header: '���ñ�׼', width: 130, sortable: true,  dataIndex: 'expensenorm'},
							{header: '������׼', width: 130, sortable: true,  dataIndex: 'returnnorm'},
							{header: '���ͽ��', width: 130, sortable: true,  dataIndex: 'dispatchamount'},
							{header: '����̯��', width: 130, sortable: true,  dataIndex: 'insurance'},
							{header: 'ά�ޱ�������', width: 130, sortable: true,  dataIndex: 'maintenance'},
							{header: '��·���ŷ���', width: 130, sortable: true,  dataIndex: 'toll'},
							{header: '��켰��������', width: 130, sortable: true,  dataIndex: 'annualCheck'},
							//{header: '���úϼ�', width: 130, sortable: true,  dataIndex: 'tmp9'},
							{header: 'ÿ�����ͺ�', width: 130, sortable: true,  dataIndex: 'oilperkm'},
							{header: 'ÿ�������', width: 130, sortable: true,  dataIndex: 'costperkm'}
							//{header: '���ͷ�����', width: 130, sortable: true,  dataIndex: 'tmp12'}
						]
			});
	
			var btnExport2Excel = new Ext.Button({
				text : "����Excel",// ����
				iconCls: 'icon-excel',
				handler: function(){
					var cms = VehicleMsgStatGrid.getColumnModel(); // ����������ģ��
					var strColoumnNames = "";// �洢��ǰ�����Ѿ���ʾ������(����֮���� "," �ָ�)
					for (var i = 0; i < cms.getColumnCount(); i++) {// ����ǰ��ʾ����
						if (!cms.isHidden(i)) {
							strColoumnNames += cms.getDataIndex(i);// �������
						}
						if (i != cms.getColumnCount() - 1) {
							strColoumnNames += ",";
						}
					}
					//alert(strColoumnNames);
					//window.document.open('user.do?method=findUsers_&cms=' + strColoumnNames);
//					window.document.open(path+'/vehicleExpense/vehicleExpense.do?method=listVehicleMsg&expExcel=true&start=0&limit=65535&startTime='+vehicleMsgStatStartTime+'&endTime='+vehicleMsgStatEndTime+'&deviceIds='+vehicleMsgStatDeviceids+'&duration='+vehicleMsgStatCarTypeInfoId+'&cmd='+strColoumnNames,'', 'width=800,height=300,menubar=yes,scrollbars=yes,resizable=yes');
					Ext.Ajax.request({
						method : 'POST',
						url: path+'/vehicleExpense/vehicleExpense.do?method=listVehicleMsg&expExcel=true&start=0&limit=65535&startTime='+vehicleMsgStatStartTime+'&endTime='+vehicleMsgStatEndTime+'&deviceIds='+vehicleMsgStatDeviceids+'&duration='+vehicleMsgStatCarTypeInfoId+'&cms='+strColoumnNames,
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
				}
			});
var VehicleMsgStatGrid = new Ext.grid.GridPanel({
	xtype: 'grid',
	id: 'VehicleMsgStatGrid',
	store: VehicleMsgStatStore,
	loadMask: {msg: '��ѯ��...'},
	cm :colModel,
	stripeRows: true,
	stateful: true,
	frame:true,
	stateId: 'VehicleMsgStatGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: '��ʼʱ��'},
			{id: 'vehicleMsgStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label',text: '����ʱ��'},
			{id: 'vehicleMsgStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			car_type_info_combo,
			'-',
			{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',handler: function(){
				searchVehicleMsgStatGrid();
			}},'-',btnExport2Excel
			
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VehicleMsgStatStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
});
