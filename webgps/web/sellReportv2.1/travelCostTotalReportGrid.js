
/**
 * 差旅信息汇总表 travelCostTotalReportGird 2012-11-08
 */
var travelCostTotalReportSearchValue = '';

//获取对象
function getObj(id){
	return document.getElementById(id);
}


var checkStateComBox = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	name : 'checkState',
	id : 'reviewStates',
	value:'-1',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data : [
				        ['全部','-1'],
				        ['未审核','0'],
				        ['已审核','1']
				        ]
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
});                                                                             
var proxy = new  Ext.data.HttpProxy({url: path+'/travelcost/travelcost.do?method=listTravelReports'});

var reader = new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'id', root: 'data' }, 
		[{name:'id'},
		 {name:'termName'},
		 {name:'groupName'},
		 {name:'simcard'},
		 {name:'startTravelTime'},
		 {name:'endTravelTime'},
		 {name:'leavePlace'},
		 {name:'arrivePlace'},
		 {name:'startTravelDesc'},
		 {name:'endTravelDesc'},
		 {name:'task'},
		 {name:'traffic'},
		 {name:'trafficCost'},
		 {name:'trafficBills'},
		 {name:'hotelCost'},
		 {name:'hotelBills'},
		 {name:'subsidyDay'},
		 {name:'subsidyCost'},
		 {name:'subsidyBills'},
		 {name:'cityTrafficCost'},
		 {name:'cityTrafficBills'},
		 {name:'otherIterms'},
		 {name:'otherCost'},
		 {name:'otherBills'},
		 {name:'countRowCost'},
		 {name:'reviewStates'},
		 {name:'total'},
		 {name:'totalTraCost'},
		 {name:'totalTraBills'},
		 {name:'totalHotelCost'},
		 {name:'totalHotelBills'},
		 {name:'totalSubsidyCost'},
		 {name:'totalSubsidyBills'},
		 {name:'totalCityTraCost'},
		 {name:'totalCityTraBills'},
		 {name:'totalOtherCost'},
		 {name:'totalOtherBills'}
		 ]);
var travelCostTotalReportStore = new Ext.data.Store({
	restful: true,
	proxy:proxy,
	reader:reader,
	 listeners:{
			beforeload:{
				fn: function(thiz,options){
					var startTime = Ext.getCmp('travsdf').getValue().format('Y-m-d');
					var endTime = Ext.getCmp('travedf').getValue().format('Y-m-d');
					var visitStatReportGpsDeviceids = getDeviceId();
					var reviewStates = Ext.getCmp('reviewStates').getValue();
					this.baseParams ={
							startTime : startTime,
							endTime:endTime,
							deviceIds:visitStatReportGpsDeviceids,
							reviewStates:reviewStates
			    	};
				}
			},load:{
				fn: function(thiz,options){
					
					//初始化汇总值
					getObj('v2').innerHTML = travelCostTotalReportStore.getAt(0).get('totalTraCost');
					getObj('v3').innerHTML = travelCostTotalReportStore.getAt(0).get('totalTraBills');
					getObj('v4').innerHTML = travelCostTotalReportStore.getAt(0).get('totalHotelCost');
					getObj('v5').innerHTML = travelCostTotalReportStore.getAt(0).get('totalHotelBills');
					getObj('v7').innerHTML = travelCostTotalReportStore.getAt(0).get('totalSubsidyCost');
					getObj('v8').innerHTML = travelCostTotalReportStore.getAt(0).get('totalSubsidyBills');
					getObj('v9').innerHTML = travelCostTotalReportStore.getAt(0).get('totalCityTraCost');
					getObj('v10').innerHTML = travelCostTotalReportStore.getAt(0).get('totalCityTraBills');
					getObj('v12').innerHTML = travelCostTotalReportStore.getAt(0).get('totalOtherCost');
					getObj('v13').innerHTML = travelCostTotalReportStore.getAt(0).get('totalOtherBills');
					getObj('v14').innerHTML = travelCostTotalReportStore.getAt(0).get('total');
				}
			}
		}
   });

var travelsm = new Ext.grid.CheckboxSelectionModel({});

//公司级考勤汇总报表
var travelCostTotalReportGrid = {
	xtype: 'grid',
	id: 'travelCostTotalReportGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: travelCostTotalReportStore,
	loadMask: {msg: main.loading},
	sm : travelsm,
	   colModel: new Ext.grid.ColumnModel({
		    columns: [
		     travelsm,
		     {header: '序号', width: 100, dataIndex:'id',hidden:true},              
	         {header: '员工姓名', width: 100, dataIndex:'termName'},        
		     {header: '部门', width: 130, dataIndex:'groupName'},       
		     {header: '手机号码', width: 120, dataIndex:'simcard'},         
		     {header: '开始时间', width: 100, dataIndex:'startTravelTime'}, 
		     {header: '结束时间', width: 100, dataIndex:'endTravelTime'},   
		     {header: '上报出发地', width: 100, dataIndex:'leavePlace'},      
		     {header: '上报目的地', width: 100, dataIndex:'arrivePlace'},     
		     {header: '实际出发地', width: 100, dataIndex:'startTravelDesc',renderer: function (value, meta, record) {
		        	if(value == 'null'){
		        		return "无有效位置信息";
		        	}else{
		        		return value;
		        	}
		        }}, 
		     {header: '实际目的地', width: 100, dataIndex:'endTravelDesc',renderer: function (value, meta, record) {
		        	if(value == 'null'){
		        		return "无有效位置信息";
		        	}else{
		        		return value;
		        	}
		        }},   
		     {header: '出差事由', width: 120, dataIndex:'task'},            
		     {header: '工具', width: 80, dataIndex:'traffic'},         
		     {header: '金额（元）', width: 80, dataIndex:'trafficCost',align: 'center'},     
		     {header: '单据（张）', width: 80, dataIndex:'trafficBills',align: 'center'},    
		     {header: '金额（元）', width: 80, dataIndex:'hotelCost',align: 'center'},       
		     {header: '单据（张）', width: 80, dataIndex:'hotelBills',align: 'center'},      
		     {header: '天数', width: 80, dataIndex:'subsidyDay',align: 'center'},      
		     {header: '金额（元）', width: 80, dataIndex:'subsidyCost',align: 'center'},     
		     {header: '单据（张）', width: 80, dataIndex:'subsidyBills',align: 'center'},    
		     {header: '金额（元）', width: 80, dataIndex:'cityTrafficCost',align: 'center'}, 
		     {header: '单据（张）', width: 80, dataIndex:'cityTrafficBills',align: 'center'},
		     {header: '项目', width: 80, dataIndex:'otherIterms'},     
		     {header: '金额（元）', width: 80, dataIndex:'otherCost',align: 'center'},       
		     {header: '单据（张）', width: 80, dataIndex:'otherBills',align: 'center'},      
		     {header: '金额小计', width: 100, dataIndex:'countRowCost',align: 'center'},    
		     {header: '状态', width: 120, dataIndex:'reviewStates'}    
		    ],
		    defaultSortable: true,
		    rows: [
		     [
		      {rowspan: 2}
		     ], [
			      {header: '合计', colspan: 11, align: 'center'},
			      {header: '<div id="v1"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v2"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v3"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v4"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v5"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v6"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v7"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v8"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v9"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v10"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v11"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v12"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v13"></div>', colspan: 1, align: 'center'},
			      {header: '<div id="v14"></div>', colspan: 1, align: 'center'},
			      {header: ' ', colspan: 1, align: 'center'}
			     ],[
		      {},{},{},{},{},{},{},{},{},{},{},{},
		      {header: '交通', colspan: 3, align: 'center'},
		      {header: '住宿', colspan: 2, align: 'center'},
		      {header: '出差补助', colspan: 3, align: 'center'},
		      {header: '市内交通', colspan: 2, align: 'center'},
		      {header: '其他', colspan: 3, align: 'center'}
		     ]
		    ]
		   }),
		   enableColumnMove: false,
		   enableHdMenu: false,
		   enableColumnHide:false,
		   plugins: [new Ext.ux.plugins.GroupHeaderGrid()],
	stripeRows: true,
	stateful: true,
	stateId: 'travelCostTotalReportGridId',
	tbar : {
		xtype: 'toolbar',
		items:[ {xtype: 'label',text: main.start_time},
				{id: 'travsdf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},
				'-',
				{xtype: 'label',text: main.end_time},
				{id: 'travedf',xtype: 'datefield',fieldLabel: 'Date',format:'Y-m-d',width: 80,editable: false,value: new Date()},				  
				{xtype: 'label',text: ' 审核状态  '}, checkStateComBox, '-',
			    {xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
				var startTime = Ext.getCmp('travsdf').getValue().format('Y-m-d');
				var endTime = Ext.getCmp('travedf').getValue().format('Y-m-d');
				var reviewStates = Ext.getCmp('reviewStates').getValue();
				var travelcostDeviceids = getDeviceId();
				
				travelCostTotalReportStore.baseParams = {
					start : 0,
					limit : 20,
					startTime : startTime,
					endTime:endTime,
					deviceIds:travelcostDeviceids,
					reviewStates:reviewStates
					
				};
				
				//清空store
				travelCostTotalReportStore.removeAll();
				//清空汇总值
				for(var i=1; i <= 14; i++)
					getObj('v'+i).innerHTML = '';
				
				travelCostTotalReportStore.load({
					params : {
						start : 0,
						limit : 20,
						startTime : startTime,
						endTime:endTime,
						deviceIds:travelcostDeviceids,
						reviewStates:reviewStates
					}
				});
				
				//初始化汇总值
				//alert(travelCostTotalReportStore.getAt(0).get('totalTraCost'));
							 
				//getObj('v2').innerHTML = travelCostTotalReportStore.getAt(0).get('totalTraCost');
				
			}}, '-',
			{xtype: 'button',text: '审核',iconCls: 'icon-modify',handler: function(){
				var tmpRecArr = travelsm.getSelections();
				if(tmpRecArr.length<=0){
					Ext.MessageBox.alert('提示', '请选择要审核的数据!');
		    		return ;
				}
				Ext.MessageBox.confirm('提示', '您确定要审核数据吗?', function (btn){
					if(btn=='yes'){
						var tmpRecArr = travelsm.getSelections();
						var ids = '';
						for(var i=0;i<tmpRecArr.length;i++){
							ids+=tmpRecArr[i].get('id')+',';
						}
						ids = ids.substring(0,ids.length-1);
						Ext.Msg.show({
							msg: '正在审核 请稍等...',
							progressText: '审核...',
							width:300,
							wait:true,
							icon:'ext-mb-download'
						});
						Ext.Ajax.request({
							url:path+'/travelcost/travelcost.do?method=verifyTravelCost',
							method :'POST', 
							params:{
								ids: ids,
								flag:'1'
							 },
							success : function(response) {
								travelCostTotalReportStore.reload();
							   	Ext.Msg.alert('提示', '审核成功');
							   	return;
							 },
							failure : function(response) {
								Ext.Msg.alert('提示', '审核失败');
							 }
					     });
					}
				});
				 
			}}, '-',
			{xtype: 'button',text: '取消审核',iconCls: 'icon-modify',handler: function(){
				var tmpRecArr = travelsm.getSelections();
				if(tmpRecArr.length<=0){
					Ext.MessageBox.alert('提示', '请选择要取消审核的数据!');
		    		return ;
				}
				Ext.MessageBox.confirm('提示', '您确定要取消审核数据吗?', function (btn){
					if(btn=='yes'){
						var tmpRecArr = travelsm.getSelections();
						var ids = '';
						for(var i=0;i<tmpRecArr.length;i++){
							ids+=tmpRecArr[i].get('id')+',';
						}
						ids = ids.substring(0,ids.length-1);
						Ext.Msg.show({
							msg: '正在取消审核 请稍等...',
							progressText: '取消审核...',
							width:300,
							wait:true,
							icon:'ext-mb-download'
						});
						Ext.Ajax.request({
							url:path+'/travelcost/travelcost.do?method=verifyTravelCost',
							method :'POST', 
							params:{
								ids: ids,
								flag:'0'
							 },
							success : function(response) {
								travelCostTotalReportStore.reload();
							   	Ext.Msg.alert('提示', '取消审核成功');
							   	return;
							 },
							failure : function(response) {
								Ext.Msg.alert('提示', '取消审核失败');
							 }
					     });
					}
				});
				 
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				var startTime = Ext.getCmp('travsdf').getValue().format('Y-m-d');
				var endTime = Ext.getCmp('travedf').getValue().format('Y-m-d');
				var travelcostDeviceids = getDeviceId();
				var reviewStates = Ext.getCmp('reviewStates').getValue();
				if(deviceID_ == ''){
					Ext.Msg.alert('提示', '请选择终端!');
					return;
				}else{
					document.excelform.action = encodeURI(encodeURI(path + '/travelcost/travelcost.do?method=exportTravelCostInfo&startTime='+startTime+'&endTime='+endTime+'&deviceIds='+travelcostDeviceids+'&reviewStates='+reviewStates));
					document.excelform.submit();
				}
				
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 20,
		store: travelCostTotalReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

