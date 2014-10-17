
/**
 * ������Ϣ���ܱ� travelCostTotalReportGird 2012-11-08
 */
var travelCostTotalReportSearchValue = '';

//��ȡ����
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
				        ['ȫ��','-1'],
				        ['δ���','0'],
				        ['�����','1']
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
					
					//��ʼ������ֵ
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

//��˾�����ڻ��ܱ���
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
		     {header: '���', width: 100, dataIndex:'id',hidden:true},              
	         {header: 'Ա������', width: 100, dataIndex:'termName'},        
		     {header: '����', width: 130, dataIndex:'groupName'},       
		     {header: '�ֻ�����', width: 120, dataIndex:'simcard'},         
		     {header: '��ʼʱ��', width: 100, dataIndex:'startTravelTime'}, 
		     {header: '����ʱ��', width: 100, dataIndex:'endTravelTime'},   
		     {header: '�ϱ�������', width: 100, dataIndex:'leavePlace'},      
		     {header: '�ϱ�Ŀ�ĵ�', width: 100, dataIndex:'arrivePlace'},     
		     {header: 'ʵ�ʳ�����', width: 100, dataIndex:'startTravelDesc',renderer: function (value, meta, record) {
		        	if(value == 'null'){
		        		return "����Чλ����Ϣ";
		        	}else{
		        		return value;
		        	}
		        }}, 
		     {header: 'ʵ��Ŀ�ĵ�', width: 100, dataIndex:'endTravelDesc',renderer: function (value, meta, record) {
		        	if(value == 'null'){
		        		return "����Чλ����Ϣ";
		        	}else{
		        		return value;
		        	}
		        }},   
		     {header: '��������', width: 120, dataIndex:'task'},            
		     {header: '����', width: 80, dataIndex:'traffic'},         
		     {header: '��Ԫ��', width: 80, dataIndex:'trafficCost',align: 'center'},     
		     {header: '���ݣ��ţ�', width: 80, dataIndex:'trafficBills',align: 'center'},    
		     {header: '��Ԫ��', width: 80, dataIndex:'hotelCost',align: 'center'},       
		     {header: '���ݣ��ţ�', width: 80, dataIndex:'hotelBills',align: 'center'},      
		     {header: '����', width: 80, dataIndex:'subsidyDay',align: 'center'},      
		     {header: '��Ԫ��', width: 80, dataIndex:'subsidyCost',align: 'center'},     
		     {header: '���ݣ��ţ�', width: 80, dataIndex:'subsidyBills',align: 'center'},    
		     {header: '��Ԫ��', width: 80, dataIndex:'cityTrafficCost',align: 'center'}, 
		     {header: '���ݣ��ţ�', width: 80, dataIndex:'cityTrafficBills',align: 'center'},
		     {header: '��Ŀ', width: 80, dataIndex:'otherIterms'},     
		     {header: '��Ԫ��', width: 80, dataIndex:'otherCost',align: 'center'},       
		     {header: '���ݣ��ţ�', width: 80, dataIndex:'otherBills',align: 'center'},      
		     {header: '���С��', width: 100, dataIndex:'countRowCost',align: 'center'},    
		     {header: '״̬', width: 120, dataIndex:'reviewStates'}    
		    ],
		    defaultSortable: true,
		    rows: [
		     [
		      {rowspan: 2}
		     ], [
			      {header: '�ϼ�', colspan: 11, align: 'center'},
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
		      {header: '��ͨ', colspan: 3, align: 'center'},
		      {header: 'ס��', colspan: 2, align: 'center'},
		      {header: '�����', colspan: 3, align: 'center'},
		      {header: '���ڽ�ͨ', colspan: 2, align: 'center'},
		      {header: '����', colspan: 3, align: 'center'}
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
				{xtype: 'label',text: ' ���״̬  '}, checkStateComBox, '-',
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
				
				//���store
				travelCostTotalReportStore.removeAll();
				//��ջ���ֵ
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
				
				//��ʼ������ֵ
				//alert(travelCostTotalReportStore.getAt(0).get('totalTraCost'));
							 
				//getObj('v2').innerHTML = travelCostTotalReportStore.getAt(0).get('totalTraCost');
				
			}}, '-',
			{xtype: 'button',text: '���',iconCls: 'icon-modify',handler: function(){
				var tmpRecArr = travelsm.getSelections();
				if(tmpRecArr.length<=0){
					Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ��˵�����!');
		    		return ;
				}
				Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ���������?', function (btn){
					if(btn=='yes'){
						var tmpRecArr = travelsm.getSelections();
						var ids = '';
						for(var i=0;i<tmpRecArr.length;i++){
							ids+=tmpRecArr[i].get('id')+',';
						}
						ids = ids.substring(0,ids.length-1);
						Ext.Msg.show({
							msg: '������� ���Ե�...',
							progressText: '���...',
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
							   	Ext.Msg.alert('��ʾ', '��˳ɹ�');
							   	return;
							 },
							failure : function(response) {
								Ext.Msg.alert('��ʾ', '���ʧ��');
							 }
					     });
					}
				});
				 
			}}, '-',
			{xtype: 'button',text: 'ȡ�����',iconCls: 'icon-modify',handler: function(){
				var tmpRecArr = travelsm.getSelections();
				if(tmpRecArr.length<=0){
					Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫȡ����˵�����!');
		    		return ;
				}
				Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫȡ�����������?', function (btn){
					if(btn=='yes'){
						var tmpRecArr = travelsm.getSelections();
						var ids = '';
						for(var i=0;i<tmpRecArr.length;i++){
							ids+=tmpRecArr[i].get('id')+',';
						}
						ids = ids.substring(0,ids.length-1);
						Ext.Msg.show({
							msg: '����ȡ����� ���Ե�...',
							progressText: 'ȡ�����...',
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
							   	Ext.Msg.alert('��ʾ', 'ȡ����˳ɹ�');
							   	return;
							 },
							failure : function(response) {
								Ext.Msg.alert('��ʾ', 'ȡ�����ʧ��');
							 }
					     });
					}
				});
				 
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				var deviceID_ = getDeviceId();
				var startTime = Ext.getCmp('travsdf').getValue().format('Y-m-d');
				var endTime = Ext.getCmp('travedf').getValue().format('Y-m-d');
				var travelcostDeviceids = getDeviceId();
				var reviewStates = Ext.getCmp('reviewStates').getValue();
				if(deviceID_ == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
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
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};

