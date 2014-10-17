/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var areaAlarmGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var areaAlarmGpsDeviceids = '';
var areaAlarmDeviceId = '';
var areaAlarmSearchValue = '';
var areaAlarmStartTime = tmpdate+' '+'08:00:00';
var areaAlarmEndTime = tmpdate+' '+'18:00:00';

function initAreaAlarmGrid(){
	var tmpstartdate = Ext.getCmp('areaAlarmsdf').getValue().format('Y-m-d');
	var tmpstarttime = Ext.getCmp('areaAlarmstf').getValue();
	var tmpenddate = Ext.getCmp('areaAlarmedf').getValue().format('Y-m-d');
	var tmpendtime = Ext.getCmp('areaAlarmetf').getValue();
	areaAlarmSearchValue = Ext.getCmp('areaAlarmdif').getValue();
	areaAlarmStartTime = tmpstartdate+' '+tmpstarttime+':00';
	areaAlarmEndTime = tmpenddate+' '+tmpendtime+':00';
    areaAlarmGpsDeviceids = getDeviceId();
}
//业务员出访统计查询
function searchAreaAlarmGrid(){
	storeLoad(AreaAlarmStore, 0, 15, areaAlarmGpsDeviceids, areaAlarmSearchValue, areaAlarmStartTime, areaAlarmEndTime, 0, false);
}
//业务员出访统计表
var AreaAlarmStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/alarmStat.do?method=listAreaAlarm'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'vehicleNumber'},{name: 'alarmTime'},{name: 'areaPoints'},{name: 'startTime'},{name: 'endTime'},{name: 'areaType'},{name: 'jmx'},{name: 'jmy'},{name: 'pd'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(areaAlarmSearchValue), startTime: areaAlarmStartTime, endTime: areaAlarmEndTime, deviceIds: areaAlarmGpsDeviceids
		    	};
			}
		}
	}
});
function areaTypeRender(val){
	if(val == '0'){
		return '出区域';
	}else{
		return '进区域';
	}
}
//业务员出访统计表
var AreaAlarmGrid = {
	xtype: 'grid',
	id: 'AreaAlarmGrid',
	store: AreaAlarmStore,
	loadMask: {msg: main.loading},
	columns: [
        {header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
        {header: '报警时间', width: 130, sortable: true,  dataIndex: 'alarmTime'},
        {header: '区域报警类型', width: 100, sortable: true, dataIndex: 'areaType', renderer: areaTypeRender},
        {id: 'pd',header: '位置描述', width: 350, sortable: true, dataIndex: 'pd',
	        renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
        }
	],
	stripeRows: true,
	stateful: true,
	stateId: 'AreaAlarmGridId',
	autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'areaAlarmsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'areaAlarmstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},'-',
			{xtype: 'label',text: main.end_time},
			{id: 'areaAlarmedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{id: 'areaAlarmetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},'-',
			{xtype: 'label', text: main.key_word},
			{id: 'areaAlarmdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initAreaAlarmGrid();searchAreaAlarmGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initAreaAlarmGrid();
				searchAreaAlarmGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				initAreaAlarmGrid();
				var excelhtml = excelpath+'/stat/alarmStat.do?method=listAreaAlarm&expExcel=true&searchValue='+encodeURI(encodeURI(areaAlarmSearchValue))+'&startTime='+areaAlarmStartTime+'&endTime='+areaAlarmEndTime+'&deviceIds='+areaAlarmGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
				
				/*if(Ext.getCmp('AreaAlarmGrid').getStore().getCount()<=0){
					return;
				}*/
				/*Ext.Msg.show({
			    	msg: '正在导出 请稍等...',
			        progressText: '导出中...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(areaAlarmSearchValue)+'&startTime='+areaAlarmStartTime+'&endTime='+areaAlarmEndTime+'&deviceIds='+areaAlarmGpsDeviceids;
    			document.excelform.action = path+'/stat/alarmStat.do?method=listAreaAlarm&'+tmpparam;
    			document.excelform.submit();
    			setTimeout(function(){Ext.MessageBox.hide()},3000);*/
				/*Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/alarmStat.do?method=listAreaAlarm&expExcel=true&searchValue='+encodeURI(encodeURI(areaAlarmSearchValue))+'&startTime='+areaAlarmStartTime+'&endTime='+areaAlarmEndTime+'&deviceIds='+areaAlarmGpsDeviceids,
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
				});*/
				
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: AreaAlarmStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

