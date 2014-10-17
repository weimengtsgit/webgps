/**
 * 车辆定位信息
 */
//var doorReportGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
//var doorReportGpsDeviceids = '13512661512,15264369413,13716013548,13754314981,15992016977,13506710260,10666011,15810740687,13682340326';
var doorReportGpsDeviceids = '';
var doorReportDeviceId = '';
var doorReportSearchValue = '';
var doorReportStartTime = tmpdate+' '+'08:00:00';
var doorReportEndTime = tmpdate+' '+'18:00:00';

function initdoorReportGrid(){
	var tmpstartdate = Ext.getCmp('doorReportsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('doorReportstf').getValue();
	var tmpenddate = Ext.getCmp('doorReportedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('doorReportetf').getValue();
	doorReportSearchValue = Ext.getCmp('doorReportdif').getValue();
	doorReportStartTime = tmpstartdate+' '+tmpstarttime+':00';
	doorReportEndTime = tmpenddate+' '+tmpendtime+':00';
    doorReportGpsDeviceids = getDeviceId();
}

function searchdoorReportGrid(){
	var ids=doorReportGpsDeviceids.split(",");
	//if(ids.length>22){
	//	Ext.Msg.alert('提示','最多选择22个终端!');
	//}else{
	    if(doorReportStartTime>=doorReportEndTime){
	        Ext.Msg.alert('提示','开始时间必须小于结束时间!');
	   	}else{
	   		storeLoad(doorReportStore, 0, 15, doorReportGpsDeviceids, doorReportSearchValue, doorReportStartTime, doorReportEndTime, 0, false);
	   	}
	//}
}

var doorReportStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/visitStat.do?method=listVehicleGPS1'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, [{name: 'deviceId'},{name: 'vehicleNumber'},{name: 'simcard'},{name: 'gpsTime'},{name: 'pd'},{name: 'visitId'},{name: 'visitName'},{name: 'longitude'},{name: 'latitude'},{name: 'speed'},{name: 'direction'},{name: 'distance'},{name: 'accStatus'},{name: 'temperature'},{name: 'varExt1'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(doorReportSearchValue), startTime: doorReportStartTime, endTime: doorReportEndTime, deviceIds: doorReportGpsDeviceids
		    	};
			}
		}
	}
});

var doorReportGrid = {
	xtype: 'grid',
	id: 'doorReportGrid',
	store: doorReportStore,
	loadMask: {msg: '查询中...'},
	columns: [
		{header: '车牌号', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
		{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: '纬度', width: 130, sortable: true,  dataIndex: 'latitude'},
		{header: '经度', width: 130, sortable: true,  dataIndex: 'longitude'},
		{header: '门状态', width: 130, sortable: true,  dataIndex: 'varExt1'/*,renderer:function(r,d,v){
			if(r == '1'){
				return "<span style='color:red;font-weight:bold;'>开门</span>";
			}else if(r == '0'){
				return "<span style='color:green;font-weight:bold;'>关门</span>";
			}else{
				return '';
			}
		}*/},
		{header: '速度', width: 130, sortable: true,  dataIndex: 'speed'},
		{header: '方向', width: 130, sortable: true,  dataIndex: 'direction'},
		{header: '里程', width: 130, sortable: true,  dataIndex: 'distance'},
		{header: '行驶状态', width: 130, sortable: true,  dataIndex: 'accStatus'},
		{id: 'pd',header: '位置描述', width: 130, sortable: true,  dataIndex: 'pd',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: '到访客户', width: 130, sortable: true,  dataIndex: 'visitName',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		},
		{header: '时间', width: 130, sortable: true,  dataIndex: 'gpsTime'},
		{header: '温度', width: 130, sortable: true,  dataIndex: 'temperature',renderer:function(r,d,v){return r=="null"?"":r;}}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'doorReportGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: '开始时间'},
			{id: 'doorReportsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'doorReportstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: '结束时间'},
			{id: 'doorReportedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'doorReportetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text:'关键字'},
			{id: 'doorReportdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initdoorReportGrid();searchdoorReportGrid();}
			}}},'-',
			{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
				initdoorReportGrid();searchdoorReportGrid();
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				initdoorReportGrid();
				var excelhtml = excelpath+'/stat/visitStat.do?method=listVehicleGPS1&expExcel=true&searchValue='+encodeURI(encodeURI(doorReportSearchValue))+'&startTime='+doorReportStartTime+'&endTime='+doorReportEndTime+'&deviceIds='+doorReportGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
				var win = new Ext.Window({
					title : '下载文件',
					closable : true,
					closeAction : 'close',
					autoWidth : false,
					width : 200,
					heigth : 150,
					items : [new Ext.Panel({
						html : "<a href='"+excelhtml+"' target='_blank' onclick='Ext.Msg.hide()'>点击此链接下载报表</a>",
						frame : true
					})]
				});
				win.show();
				
				/*Ext.Msg.show({
			    	msg: '正在导出 请稍等...',
			        progressText: '导出中...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(doorReportSearchValue)+'&startTime='+doorReportStartTime+'&endTime='+doorReportEndTime+'&deviceId='+doorReportGpsDeviceids;
    			document.excelform.action = path+'/stat/visitStat.do?method=listVehicleGPS&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);*/
				/*if(Ext.getCmp('doorReportGrid').getStore().getCount()<=0){
					return;
				}
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/visitStat.do?method=listVehicleGPS&expExcel=true&searchValue='+encodeURI(encodeURI(doorReportSearchValue))+'&startTime='+doorReportStartTime+'&endTime='+doorReportEndTime+'&deviceIds='+doorReportGpsDeviceids,
					success:function(response, opts){
						Ext.Msg.alert("提示", "操作成功");
					},
					form: Ext.fly('excelform'),
					isUpload: true,
					timeout: 300000,
					failure:function(response, opts){
						Ext.Msg.alert("提示", "操作失败");
					},
					callback:function(response, opts){
						Ext.Msg.alert("提示", "操作成功");
					},
					scope: this
				});*/
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: doorReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
