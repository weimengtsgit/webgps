var visitRankStartTime = tmpdate+' '+'00:00:00';
var visitRankEndTime = tmpdate+' '+'23:59:59';
var visitRankGpsDeviceids = '';
var visitRankPoiName = '';

function initVisitRankGrid(){
	var tmpstartdate = Ext.getCmp('visitRanksdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('visitRankedf').getValue().format('Y-m-d');
	visitRankStartTime = tmpstartdate+' '+'00:00:00';
	visitRankEndTime = tmpenddate+' '+'23:59:59';
	visitRankGpsDeviceids = getDeviceId();
}

function searchVisitRankGrid(){
	storeLoad(VisitRankStore, 0, 65535, visitRankGpsDeviceids, '', visitRankStartTime, visitRankEndTime, 0, false);
}

var VisitRankStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/visit/visit.do?method=listVisitRanks'}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success', 
		idProperty: 'a', 
		root: 'data' 
	}, [{name: 'vehicleNumber'},
	    {name: 'groupName'},
	    {name: 'visitCounts'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
						startTime: visitRankStartTime, endTime: visitRankEndTime, deviceIds: visitRankGpsDeviceids
		    	};
			}
		}
	}
});


var VisitRankGrid = {
	xtype: 'grid',
	id: 'VisitRankGrid',
	store: VisitRankStore,
	loadMask: {msg: main.loading},
	columns: [
        {header: '员工姓名', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
        {id: 'groupName', header: '部门', width: 250, sortable: true,  dataIndex: 'groupName'},
        {header: '客户拜访数(个)', width: 130, sortable: true, dataIndex: 'visitCounts'}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'VisitRankGridId',
	//autoExpandColumn: 'groupName',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'visitRanksdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'visitRankstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'visitRankedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'visitRanketf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				initVisitRankGrid();searchVisitRankGrid();
			}},'-',
			{xtype: 'button',text: '切换柱状图', iconCls: 'icon-search',handler: function(){
				Ext.getCmp('VisitRankPanel').layout.setActiveItem(1);
				var visitRankChartIfr_ = document.getElementById('visitRankChartIfr').contentWindow;
				visitRankChartIfr_.loadVisitRankChart(VisitRankStore);
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				initVisitRankGrid();
				var excelhtml = path+'/visit/visit.do?method=listVisitRanksExpExcel&startTime='+visitRankStartTime+'&endTime='+visitRankEndTime+'&deviceIds='+visitRankGpsDeviceids;
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
		pageSize: 65535,
		store: VisitRankStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};


var VisitRankChartPanel = new Ext.Panel({
	id: 'VisitRankChartPanel',
    //frame:true,
    autoScroll: true,
	html: '<iframe id="visitRankChartIfr" name="visitRankChartIfr" src="'+path+'/sellReportv2.1/visitRankChart.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[new Ext.Action({
			text: main.back,
			handler: function(){
				Ext.getCmp('VisitRankPanel').layout.setActiveItem(0);
			},
			iconCls: 'icon-search'
		})]
	}
});