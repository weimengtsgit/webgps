//var tmpdate = (new Date()).pattern("yyyy-M-d");
var visitReportStartTime = tmpdate+' '+'00:00:00';
var visitReportEndTime = tmpdate+' '+'23:59:59';
var visitReportGpsDeviceids = '';
var visitReportPoiName = '';

function initVisitReportGrid(deviceID_){
	var tmpPoiName_ = Ext.getCmp('visitReportdif').getValue();
	if(tmpPoiName_.length>100){
		Ext.Msg.alert('提示', '输入的客户名称超过了100字符,请重新输入!');
		return;
	}
		visitReportGpsDeviceids = deviceID_;
		var tmpstartdate = Ext.getCmp('visitReportsdf').getValue().format('Y-m-d');
		var tmpenddate = Ext.getCmp('visitReportedf').getValue().format('Y-m-d');
		visitReportPoiName = tmpPoiName_;
		visitReportStartTime = tmpstartdate+' '+'00:00:00';
		visitReportEndTime = tmpenddate+' '+'23:59:59';
}

function searchVisitReportGrid(){
	storeLoad(VisitReportStore, 0, 15, visitReportGpsDeviceids, visitReportPoiName, visitReportStartTime, visitReportEndTime, 0, false);
}

var VisitReportStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/visit/visit.do?method=listVisitReports'}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success', 
		idProperty: 'a', 
		root: 'data' 
	}, [{name: 'id'},
	    {name: 'vehicleNumber'},
	    {name: 'poiName'},
	    {name: 'signInTime'},
	    {name: 'signInRendertime'},
	    {name: 'signInLocType'},
	    {name: 'signInLng'},
	    {name: 'signInLat'},
	    {name: 'signInDistance'},
	    {name: 'signOutTime'},
	    {name: 'signOutRendertime'},
	    {name: 'signOutLocType'},
	    {name: 'signOutLng'},
	    {name: 'signOutLat'},
	    {name: 'signOutDistance'},
	    {name: 'signInDesc'},
	    {name: 'signOutDesc'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
						searchValue: encodeURI(visitReportPoiName), startTime: visitReportStartTime, endTime: visitReportEndTime, deviceIds: visitReportGpsDeviceids
		    	};
			}
		}
	}
});


var VisitReportGrid = {
	xtype: 'grid',
	id: 'VisitReportGrid',
	store: VisitReportStore,
	loadMask: {msg: main.loading},
	colModel: new Ext.ux.grid.LockingColumnModel([
    //columns: [
        {header: '序号', width: 30, sortable: true,  dataIndex: 'id', locked: true},
        {header: '员工姓名', width: 130, sortable: true,  dataIndex: 'vehicleNumber', locked: true},
        {header: '客户名称', width: 130, sortable: true,  dataIndex: 'poiName'},
        {header: '签到位置描述', width: 230, sortable: true, dataIndex: 'signInDesc',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
        },
        {header: '签到偏差(米)', width: 100, sortable: true, dataIndex: 'signInDistance'},
        {header: '签到时间', width: 130, sortable: true, dataIndex: 'signInRendertime'},
        {header: '签退位置描述', width: 230, sortable: true, dataIndex: 'signOutDesc',
			renderer: function tips(val) {
				return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
        },
        {header: '签退偏差(米)', width: 100, sortable: true, dataIndex: 'signOutDistance'},
        {header: '签退时间', width: 130, sortable: true, dataIndex: 'signOutRendertime'},
        {header: '签到数据上传时间', width: 130, sortable: true, dataIndex: 'signInTime'},
        {header: '签退数据上传时间', width: 130, sortable: true, dataIndex: 'signOutTime'},
        {header: '签到获取方式', width: 100, sortable: true, dataIndex: 'signInLocType',
			renderer: function tips(val) {
				//if(val == '0'){return 'GPS';}else if(val == '1'){return 'CELLID';}else{return '';}
				if(val == '0'){return 'GPS';}else if(val == '1'){return 'CELLID';}else if(val == '2'){return 'network';} else {return '';}
			}
        },
        {header: '签到经度', width: 100, sortable: true, dataIndex: 'signInLng'},
        {header: '签到纬度', width: 100, sortable: true, dataIndex: 'signInLat'},
        {header: '签退获取方式', width: 100, sortable: true, dataIndex: 'signOutLocType',
			renderer: function tips(val) {
				//if(val == '0'){return 'GPS';}else if(val == '1'){return 'CELLID';}else{return '';}
				if(val == '0'){return 'GPS';}else if(val == '1'){return 'CELLID';}else if(val == '2'){return 'network';} else {return '';}
			}
        },
        {header: '签退经度', width: 100, sortable: true, dataIndex: 'signOutLng'},
        {header: '签退纬度', width: 100, sortable: true, dataIndex: 'signOutLat'}
	]),
	view: new Ext.ux.grid.LockingGridView(),
	stripeRows: true,
	//stateful: true,
	stateId: 'VisitReportGridId',
	//autoExpandColumn: 'vehicleNumber',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'visitReportsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'visitReportstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'visitReportedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'visitReportetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			{xtype: 'label', text: '客户名称'},
			{id: 'visitReportdif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {
					var deviceID_ = getDeviceId();
					if(deviceID_ == ''){
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}else{
						initVisitReportGrid(deviceID_);
						searchVisitReportGrid();
					}
				}
			}}},'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initVisitReportGrid(deviceID_);
					searchVisitReportGrid();
				}
			}},'-',
			{xtype: 'button',text: '导出原始数据Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initVisitReportGrid(deviceID_);
					document.excelform.action = encodeURI(encodeURI(path + '/visit/visit.do?method=exportVisit&startTime='+visitReportStartTime+'&endTime='+visitReportEndTime+'&poiName='+visitReportPoiName+'&deviceIds='+visitReportGpsDeviceids));
					document.excelform.submit();
				}
				
			}},
			{xtype: 'button',text: '导出拜访考勤表Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initVisitReportGrid(deviceID_);
					document.excelform.action = encodeURI(encodeURI(path + '/visit/visit.do?method=exportVisitStat&startTime='+visitReportStartTime+'&endTime='+visitReportEndTime+'&poiName='+visitReportPoiName+'&deviceIds='+visitReportGpsDeviceids));
					document.excelform.submit();
				}
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: VisitReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

