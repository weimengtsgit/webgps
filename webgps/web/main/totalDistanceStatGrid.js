var totaldistanceStatGpsDeviceids = '';
var totaldistanceStatDeviceId = '';
var totaldistanceStatSearchValue = '';
var totaldistanceStatStartTime = tmpdate;
var totaldistanceStatEndTime = tmpdate;
function searchTotalDistanceStatGrid(){
	totaldistanceStatGpsDeviceids = getDeviceId();
	storeLoad(TotalDistanceStatStore, 0, 15, totaldistanceStatGpsDeviceids, totaldistanceStatSearchValue, totaldistanceStatStartTime, totaldistanceStatEndTime, 0, false);
}
var TotalDistanceStatStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/stat/distanceStat.do?method=listTotalDistanceStat'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'a', root: 'data' }, [{name: 'id'},{name: 'vehicleNumber'},{name: 'distance'},{name: 'tjdate'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					//searchValue: encodeURI(distanceStatSearchValue), startTime: distanceStatStartTime, endTime: distanceStatEndTime, deviceIds: distanceStatGpsDeviceids
		    	};
			}
		}
	}
});
var TotalDistanceStatGrid = {
	xtype: 'grid',
	id: 'TotalDistanceStatGrid',
	store: TotalDistanceStatStore,
	loadMask: {msg: main.loading},
	columns: [
		{header: 'id', width: 10, sortable: true, dataIndex: 'id',hidden:true},
        {header: '名称', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
        {header: '总公里数', width: 130, sortable: true,  dataIndex: 'distance',align: 'right'}
    ],
	stripeRows: true,
	stateful: true,
	//stateId: 'TotalDistanceStatGridId',
	//autoExpandColumn: 'tjdate',
	tbar : {
		xtype: 'toolbar',
		items:[
		    {xtype: 'label', text: main.key_word},
			{id: 'totaldistanceStatdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {searchTotalDistanceStatGrid();}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				searchTotalDistanceStatGrid();
			}},'-',
			{xtype: 'button',text: main.oupput_from_excel,iconCls: 'icon-excel',handler: function(){
				totaldistanceStatGpsDeviceids = getDeviceId();
				var excelhtml = excelpath+'/stat/distanceStat.do?method=listTotalDistanceStat&expExcel=true&searchValue='+encodeURI(encodeURI(totaldistanceStatSearchValue))+'&startTime='+totaldistanceStatStartTime+'&endTime='+totaldistanceStatEndTime+'&deviceIds='+totaldistanceStatGpsDeviceids+'&entCode='+empCode64+'&userAccount='+account64+'&password='+password64;
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
				
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: TotalDistanceStatStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

