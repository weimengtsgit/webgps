var costDetailStartTime = tmpdate+' '+'00:00:00';
var costDetailEndTime = tmpdate+' '+'23:59:59';
var costDetailGpsDeviceids = '';
var costDetailApproved = -1;
function initCostDetailGrid(deviceID_){
	costDetailGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('costDetailsdf').getValue().format('Y-m-d');
	var tmpenddate = Ext.getCmp('costDetailedf').getValue().format('Y-m-d');
	//costDetailPoiName = Ext.getCmp('costDetaildif').getValue();
	costDetailApproved = Ext.getCmp('costDetailApproved').getValue();
	costDetailStartTime = tmpstartdate+' '+'00:00:00';
	costDetailEndTime = tmpenddate+' '+'23:59:59';
}

function searchCostDetailGrid(){
	storeLoad(CostDetailStore, 0, 15, costDetailGpsDeviceids, '', costDetailStartTime, costDetailEndTime, costDetailApproved, false);
}

var CostDetailStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/cost/cost.do?method=listCostDetails'}),
	reader: new Ext.data.JsonReader({
		totalProperty: 'total',
		successProperty: 'success', 
		idProperty: 'a', 
		root: 'data' 
	}, [{name : 'id'},
		{name : 'createOn'},
		{name : 'groupName'},
		{name : 'vehicleNumber'},
		{name : 'meal'},
		{name : 'transportation'},
		{name : 'accommodation'},
		{name : 'communication'},
		{name : 'gift'},
		{name : 'other'},
		{name : 'remarks'},
		{name : 'dateTime'},
		{name : 'costAmount'},
		{name : 'approved'}
	]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					startTime: costDetailStartTime, endTime: costDetailEndTime, deviceIds: costDetailGpsDeviceids
				};
			}
		}
	}
});


var costDetailSm = new Ext.grid.CheckboxSelectionModel({});
var CostDetailGrid = {
	xtype: 'grid',
	id: 'CostDetailGrid',
	store: CostDetailStore,
	loadMask: {msg: main.loading},
	columns: [
	 	 costDetailSm,
        {header: '日期', width: 130, sortable: true,  dataIndex: 'createOn'},
        {header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
        {header: '员工姓名', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
        {header: '餐费', width: 100, sortable: true, dataIndex: 'meal'},
        {header: '交通费', width: 100, sortable: true, dataIndex: 'transportation'},
        {header: '住宿费', width: 100, sortable: true, dataIndex: 'accommodation'},
        {header: '通信费', width: 100, sortable: true, dataIndex: 'communication'},
        {header: '礼品费', width: 100, sortable: true, dataIndex: 'gift'},
        {header: '其他', width: 100, sortable: true, dataIndex: 'other'},
        {header: '总金额', width: 100, sortable: true, dataIndex: 'costAmount', renderer: function (value, meta, record) {
        	var costAmount_ = 0;
        	costAmount_ = costAmount_ + Number(record.get('meal'));
        	costAmount_ = costAmount_ + Number(record.get('transportation'));
        	costAmount_ = costAmount_ + Number(record.get('accommodation'));
        	costAmount_ = costAmount_ + Number(record.get('communication'));
        	costAmount_ = costAmount_ + Number(record.get('gift'));
        	costAmount_ = costAmount_ + Number(record.get('other'));
        	return costAmount_;
        }},
        {header: coshRemarksHeader, width: 100, sortable: true, dataIndex: 'remarks', hidden: costRemarks},
        {header: costDateTiemHeader, width: 130, sortable: true, dataIndex: 'dateTime', hidden: costDateTime},
        {header: '状态', width: 100, sortable: true, dataIndex: 'approved', renderer: function (value, meta, record) {
        	if(value == '0'){
        		return "未审核";
        	}else{
        		return "审核";
        	}
        }}
	],
    sm : costDetailSm,
	stripeRows: true,
	stateful: true,
	stateId: 'CostDetailGridId',
	//autoExpandColumn: 'vehicleNumber',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label',text: main.start_time},
			{id: 'costDetailsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'costDetailstf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '08:00'},
			'-',
			{xtype: 'label',text: main.end_time},
			{id: 'costDetailedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
			//{id: 'costDetailetf',xtype: 'timefield', fieldLabel: 'Time',width: 60,format :'H:i',editable: false,value: '18:00'},
			'-',
			//{xtype: 'label', text: '客户名称'},
			//{id: 'costDetaildif',xtype: 'textfield',width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
			//	if (e.getKey() == e.ENTER) {initCostDetailGrid();searchCostDetailGrid();}
			//}}},'-',
			{xtype: 'label', text: '状态'},
			new Ext.form.ComboBox({
				width: 60,
				id : 'costDetailApproved',
				enableKeyEvents : true,
				grow : true,
				valueField : "id",
				displayField : "name",
				mode : 'local',// 数据是在本地
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				value: -1,
				store : new Ext.data.SimpleStore({
					fields : [ "name", "id" ],
					data : [[ '--全部--', '-1' ], [ '审核', '1' ], [ '未审核', '0' ]]
				})
			}),'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initCostDetailGrid(deviceID_);
					searchCostDetailGrid();
				}
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					initCostDetailGrid(deviceID_);
					var excelhtml = path+'/cost/cost.do?method=listCostDetailsExpExcel&startTime='+costDetailStartTime+'&endTime='+costDetailEndTime+'&deviceIds='+costDetailGpsDeviceids+'&duration='+costDetailApproved;
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
				}
				
			}},'-',
			{xtype: 'button',text: '审核',iconCls: 'icon-saved',handler: function(){
				var tmpRecArr = costDetailSm.getSelections();
				var ids = '';
				for(var i=0;i<tmpRecArr.length;i++){
					ids+=tmpRecArr[i].get('id')+',';
				}
				if(ids.length>0){
					ids = ids.substring(0,ids.length-1);
				}else{
					Ext.MessageBox.alert('提示', '请选择要审核的记录!');
	        		return ;
				}
				approvedCostids = ids;
	        	Ext.MessageBox.confirm('提示', '确定通过审核吗?', approvedCostConfirm);
	        	
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: CostDetailStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};


var approvedCostids;
function approvedCostConfirm(btn){
	if(btn=='yes'){
		Ext.Msg.show({
           msg: '正在提交 请稍等...',
           progressText: '提交...',
           width:300,
           wait:true,
           icon:'ext-mb-download'
       });
		Ext.Ajax.request({
			url:path+'/cost/cost.do?method=approved',
			method :'POST', 
			params: { ids: encodeURI(approvedCostids)},
			//timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if(res.result==1){
					CostDetailStore.reload();
					Ext.Msg.alert('提示', '操作成功');
					return;
				}else{
					Ext.Msg.alert('提示', "操作失败!");
					return;
				}
			},
			failure : function(request) {
				Ext.Msg.alert('提示', "操作失败!");
			}
		});
    }
}
