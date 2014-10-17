/**
 * 车辆定位信息
 */
//var vehicleGPSInfoGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
//var vehicleGPSInfoGpsDeviceids = '13512661512,15264369413,13716013548,13754314981,15992016977,13506710260,10666011,15810740687,13682340326';
var vehicleGPSInfoGpsDeviceids = '';
var vehicleGPSInfoDeviceId = '';
var vehicleGPSInfoSearchValue = '';
var vehicleGPSInfoStartTime = tmpdate+' '+'08:00:00';
var vehicleGPSInfoEndTime = tmpdate+' '+'18:00:00';

function searchVehicleGPSInfoGrid(){
	var tmpstartdate = Ext.getCmp('vehicleGPSInfosdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('vehicleGPSInfostf').getValue();
	var tmpenddate = Ext.getCmp('vehicleGPSInfoedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('vehicleGPSInfoetf').getValue();
	vehicleGPSInfoSearchValue = Ext.getCmp('vehicleGPSInfodif').getValue();
	vehicleGPSInfoStartTime = tmpstartdate+' '+tmpstarttime+':00';
	vehicleGPSInfoEndTime = tmpenddate+' '+tmpendtime+':00';
    vehicleGPSInfoGpsDeviceids = getDeviceId();
	var ids=vehicleGPSInfoGpsDeviceids.split(",");
	if(ids.length>22){
		Ext.Msg.alert('提示','最多选择22个终端!');
	}else{
	    if(vehicleGPSInfoStartTime>=vehicleGPSInfoEndTime){
	        Ext.Msg.alert('提示','开始时间必须小于于结束时间!');
	   	}else{
	   		storeLoad(VehicleGPSInfoStore, 0, 15, vehicleGPSInfoGpsDeviceids, vehicleGPSInfoSearchValue, vehicleGPSInfoStartTime, vehicleGPSInfoEndTime, 0, false);
	   	}
	}
}

var VehicleGPSInfoStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/visitStat.do?method=listVehicleGPS'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, [{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'simcard'},{name: 'gpsTime'},{name: 'pd'},{name: 'visitId'},{name: 'visitName'},{name: 'longitude'},{name: 'latitude'},{name: 'speed'},{name: 'direction'},{name: 'distance'},{name: 'accStatus'},{name: 'temperature'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(vehicleGPSInfoSearchValue), startTime: vehicleGPSInfoStartTime, endTime: vehicleGPSInfoEndTime, deviceIds: vehicleGPSInfoGpsDeviceids
		    	};
			}
		}
	}
});

var VehicleGPSInfoGrid = {
	xtype: 'grid',
	id: 'VehicleGPSInfoGrid',
	store: VehicleGPSInfoStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: vehicleGPSInfo.vehicleNumber, width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: vehicleGPSInfo.simcard, width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: vehicleGPSInfo.latitude, width: 130, sortable: true,  dataIndex: 'latitude'},
		{header: vehicleGPSInfo.longitude, width: 130, sortable: true,  dataIndex: 'longitude'},
		{header: vehicleGPSInfo.speed, width: 130, sortable: true,  dataIndex: 'speed'},
		{header: vehicleGPSInfo.direction, width: 130, sortable: true,  dataIndex: 'direction'},
		{header: vehicleGPSInfo.distance, width: 130, sortable: true,  dataIndex: 'distance'},
		{header: vehicleGPSInfo.accStatus, width: 130, sortable: true,  dataIndex: 'accStatus'},
		{id: 'pd',header: vehicleGPSInfo.pd, width: 130, sortable: true,  dataIndex: 'pd',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: vehicleGPSInfo.visitName, width: 130, sortable: true,  dataIndex: 'visitName',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: vehicleGPSInfo.gpsTime, width: 130, sortable: true,  dataIndex: 'gpsTime'},
		{header: vehicleGPSInfo.temperature, width: 130, sortable: true,  dataIndex: 'temperature',renderer:function(r,d,v){return r=="null"?"":r;}}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VehicleGPSInfoGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'vehicleGPSInfosdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'vehicleGPSInfostf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'vehicleGPSInfoedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'vehicleGPSInfoetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text:'关键字'},
			{id: 'vehicleGPSInfodif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchVehicleGPSInfoGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				searchVehicleGPSInfoGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				/*Ext.Msg.show({
			    	msg: '正在导出 请稍等...',
			        progressText: '导出中...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(vehicleGPSInfoSearchValue)+'&startTime='+vehicleGPSInfoStartTime+'&endTime='+vehicleGPSInfoEndTime+'&deviceId='+vehicleGPSInfoGpsDeviceids;
    			document.excelform.action = path+'/stat/visitStat.do?method=listVehicleGPS&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);*/
				if(Ext.getCmp('VehicleGPSInfoGrid').getStore().getCount()<=0){
					return;
				}
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/visitStat.do?method=listVehicleGPS&expExcel=true&searchValue='+encodeURI(encodeURI(vehicleGPSInfoSearchValue))+'&startTime='+vehicleGPSInfoStartTime+'&endTime='+vehicleGPSInfoEndTime+'&deviceIds='+vehicleGPSInfoGpsDeviceids,
					success:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					form: Ext.fly('excelform'),
					isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_failure);
					},
					callback:function(response, opts){
						Ext.Msg.alert(main.tips, main.operation_is_successful);
					},
					scope: this
				});
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VehicleGPSInfoStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
