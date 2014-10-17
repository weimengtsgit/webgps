/**
 * 业务员出访统计
 */
//var visitStatGpsDeviceids = '460020567820364,460028010255325,460029110118852,460006342120035,460001211628373,460010311400681,460022108900391,460001460210166,460028111134655,460020667112225,460001710831401,460003689042604,460020666916818,460028011838217,460028103313420,460014000395945,460020263531649,460001091931546,89860001011070001113,460001226025238';
//var distanceStatGpsDeviceids = '460000783103232,460028010255325,460029110000503,460017290619869,460001161238019,460006302136464,460022101208808,460001166028754,460022108900391,460001460210166,460028111134655,460020667112225,460016104928190,460001710831401,460020666916818,460011484980847,460028011838217,460028103313420,460011642961133,460003689042614,460029010737977,460001091931546,356827023621440,460020887703052,460001211628373,460020663711766,460003570213324,460010311400681,460003689042617,460020263531649,460020567820364,460023100030645,460022015235874,460023010413297,460003689042648,460000453118572,460003689042604,460028100005357';
var distanceStatGpsDeviceids = '';
var distanceStatDeviceId = '';
var distanceStatSearchValue = '';
var distanceStatStartTime = tmpdate;
var distanceStatEndTime = tmpdate;

function initDistanceStatGrid(){
	var tmpstartdate = Ext.getCmp('distanceStatsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('distanceStatedf').getValue().format('Y-m-d');
	distanceStatSearchValue = Ext.getCmp('distanceStatdif').getValue();
	distanceStatStartTime = tmpstartdate;
	distanceStatEndTime = tmpenddate;
    distanceStatGpsDeviceids = getDeviceId();
}
//业务员出访统计查询
function searchDistanceStatGrid(){
	storeLoad(DistanceStatStore, 0, 15, distanceStatGpsDeviceids, distanceStatSearchValue, distanceStatStartTime, distanceStatEndTime, 0, false);
}
//业务员出访统计表
var DistanceStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/distanceStat.do?method=listDistanceStatByCustom'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'vehicleNumber'},{name: 'distance'},{name: 'tjdate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					searchValue: encodeURI(distanceStatSearchValue), startTime: distanceStatStartTime, endTime: distanceStatEndTime, deviceIds: distanceStatGpsDeviceids
		    	};
			}
		}
	}
});
//业务员出访统计表
var DistanceStatGrid = {
	xtype: 'grid',
	id: 'DistanceStatGrid',
	store: DistanceStatStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
        {header: distanceStat.vehicleNumber, width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
        {header: distanceStat.distance, width: 130, sortable: true,  dataIndex: 'distance',align: 'right'},
        {id:'tjdate' ,header: distanceStat.tjdate, width: 130, sortable: true,  dataIndex: 'tjdate'}
    ],
	stripeRows: true,
	stateful: true,
	stateId: 'DistanceStatGridId',
	autoExpandColumn: 'tjdate',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'distanceStatsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label',text: main.end_time},
			{id: 'distanceStatedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			{xtype: 'label', text: main.key_word},
			{id: 'distanceStatdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {initDistanceStatGrid();searchDistanceStatGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initDistanceStatGrid();searchDistanceStatGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				
				initDistanceStatGrid();
				var excelhtml = excelpath+'/stat/distanceStat.do?method=listDistanceStatByCustom&expExcel=true&searchValue='+encodeURI(encodeURI(distanceStatSearchValue))+'&startTime='+distanceStatStartTime+'&endTime='+distanceStatEndTime+'&deviceIds='+distanceStatGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(distanceStatSearchValue)+'&startTime='+distanceStatStartTime+'&endTime='+distanceStatEndTime+'&deviceIds='+distanceStatGpsDeviceids;
    			document.excelform.action = path+'/stat/distanceStat.do?method=listDistanceStatByCustom&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);*/
				/*if(Ext.getCmp('DistanceStatGrid').getStore().getCount()<=0){
					return;
				}
				Ext.Ajax.request({
					method : 'POST',
					url: path+'/stat/distanceStat.do?method=listDistanceStatByCustom&expExcel=true&searchValue='+encodeURI(encodeURI(distanceStatSearchValue))+'&startTime='+distanceStatStartTime+'&endTime='+distanceStatEndTime+'&deviceIds='+distanceStatGpsDeviceids,
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
		store: DistanceStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

