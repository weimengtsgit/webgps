var CostReportGrid = new Ext.Panel({
	layout: 'fit',
	id: 'CostReportGrid',
	autoScroll: false,
	html: '<iframe id="CostReportPanelIfr" name="CostReportPanelIfr" src="'+path+'/sellReportv2.1/CostReportPanel.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[{xtype: 'label',text: '时间'},
		       new Ext.form.ComboBox({
					width: 60,
					id : 'CostReportYear',
					enableKeyEvents : true,
					grow : true,
					//name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// 数据是在本地
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : []
					})
				}),
				{xtype: 'label',text: '年'},
				new Ext.form.ComboBox({
					width: 60,
					id : 'CostReportNum',
					enableKeyEvents : true,
					grow : true,
					//name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// 数据是在本地
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : []
					})
				}),
				{xtype: 'label',text: targetTemplateType},'-',
				//{xtype: 'label', text: '客户'},
				//{id: 'costReportdif',xtype: 'textfield',width: 100,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				//	if (e.getKey() == e.ENTER) {}
				//}}},'-',
				{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
					costGpsDeviceidsReportChart = getDeviceId();
					if(costGpsDeviceidsReportChart == ''){
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}
					var reportYear = Ext.getCmp('CostReportYear').getValue();
					var reportNum = Ext.getCmp('CostReportNum').getValue();
					var ifrObject_ = document.getElementById('CostReportPanelIfr').contentWindow;
					if(ifrObject_.flag){
						ifrObject_.getLineChart('../cost/cost.do?method=getCostReportByTime&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+costGpsDeviceidsReportChart, 'costChartDiv', '#costUnauditedSpan');
					}else{
						ifrObject_.getLineChart('../cost/cost.do?method=getCostHisReportByTime&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+costGpsDeviceidsReportChart, 'costChartDiv', '#costUnauditedSpan');
					}
				}}
	    ]
	},
	listeners:{
		activate : function(p){
			initReportTimeScope(Ext.getCmp('CostReportYear'), Ext.getCmp('CostReportNum'));
		}
	}
});

