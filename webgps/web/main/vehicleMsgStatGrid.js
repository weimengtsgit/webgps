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
    emptyText:'选择车辆类型'
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
						  	{header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
							{header: '姓名', width: 130, sortable: true,  dataIndex: 'termName'},
							{header: '车牌号', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
							{header: '车辆型号', width: 130, sortable: true,  dataIndex: 'typeName'},
							{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
							//{header: '备注', width: 130, sortable: true,  dataIndex: 'termDesc'},
							{header: '加油量(升)', width: 130, sortable: true,  dataIndex: 'oilLiter'},
							{header: '加油金额', width: 130, sortable: true,  dataIndex: 'oilCost'},
							{header: '车辆折旧', width: 130, sortable: true,  dataIndex: 'vehicleDepreciation'},
							{header: '人员费用', width: 130, sortable: true,  dataIndex: 'personalExpenses'},
							{header: '里程标准', width: 130, sortable: true,  dataIndex: 'mileagenorm'},
							{header: '费用标准', width: 130, sortable: true,  dataIndex: 'expensenorm'},
							{header: '返还标准', width: 130, sortable: true,  dataIndex: 'returnnorm'},
							{header: '配送金额', width: 130, sortable: true,  dataIndex: 'dispatchamount'},
							{header: '保险摊销', width: 130, sortable: true,  dataIndex: 'insurance'},
							{header: '维修保养费用', width: 130, sortable: true,  dataIndex: 'maintenance'},
							{header: '过路过桥费用', width: 130, sortable: true,  dataIndex: 'toll'},
							{header: '年检及其他费用', width: 130, sortable: true,  dataIndex: 'annualCheck'},
							//{header: '费用合计', width: 130, sortable: true,  dataIndex: 'tmp9'},
							{header: '每公里油耗', width: 130, sortable: true,  dataIndex: 'oilperkm'},
							{header: '每公里费用', width: 130, sortable: true,  dataIndex: 'costperkm'}
							//{header: '配送费用率', width: 130, sortable: true,  dataIndex: 'tmp12'}
						]
			});
	
			var btnExport2Excel = new Ext.Button({
				text : "导出Excel",// 标题
				iconCls: 'icon-excel',
				handler: function(){
					var cms = VehicleMsgStatGrid.getColumnModel(); // 获得网格的列模型
					var strColoumnNames = "";// 存储当前网格已经显示的列名(列名之间用 "," 分隔)
					for (var i = 0; i < cms.getColumnCount(); i++) {// 处理当前显示的列
						if (!cms.isHidden(i)) {
							strColoumnNames += cms.getDataIndex(i);// 获得列名
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
							//Ext.Msg.alert("提示", "操作成功");
						},
						form: Ext.fly('excelform'),
						isUpload: true,
						timeout: 300000,
						failure:function(response, opts){
							Ext.Msg.alert("提示", "操作失败");
						},
						callback:function(response, opts){
							//Ext.Msg.alert("提示", "操作成功");
						},
						scope: this
					});
				}
			});
var VehicleMsgStatGrid = new Ext.grid.GridPanel({
	xtype: 'grid',
	id: 'VehicleMsgStatGrid',
	store: VehicleMsgStatStore,
	loadMask: {msg: '查询中...'},
	cm :colModel,
	stripeRows: true,
	stateful: true,
	frame:true,
	stateId: 'VehicleMsgStatGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: '开始时间'},
			{id: 'vehicleMsgStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label',text: '结束时间'},
			{id: 'vehicleMsgStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			'-',
			car_type_info_combo,
			'-',
			{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
				searchVehicleMsgStatGrid();
			}},'-',btnExport2Excel
			
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VehicleMsgStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
});
