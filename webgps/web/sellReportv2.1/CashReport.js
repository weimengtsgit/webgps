var CashReportGrid = new Ext.Panel({
	layout: 'fit',
	id: 'CashReportGrid',
	autoScroll: false,
	html: '<iframe id="CashReportPanelIfr" name="CashReportPanelIfr" src="'+path+'/sellReportv2.1/CashReportPanel.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[{xtype: 'label',text: '时间'},
		       new Ext.form.ComboBox({
					width: 60,
					id : 'CashReportYear',
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
					id : 'CashReportNum',
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
				{xtype: 'label', text: '客户'},
				{id: 'cashReportdif',xtype: 'textfield',width: 100,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
					if (e.getKey() == e.ENTER) {}
				}}},'-',
				{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
					cashGpsDeviceidsReportChart = getDeviceId();
					if(cashGpsDeviceidsReportChart == ''){
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}
					cashPoiNameReportChart = Ext.getCmp('cashReportdif').getValue();
					var reportYear = Ext.getCmp('CashReportYear').getValue();
					var reportNum = Ext.getCmp('CashReportNum').getValue();
					var ifrObject_ = document.getElementById('CashReportPanelIfr').contentWindow;
					if(ifrObject_.flag){
						ifrObject_.getLineChart('../cash/cash.do?method=getCashsByTime&poiName='+cashPoiNameReportChart+'&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+cashGpsDeviceidsReportChart, 'cashChartDiv', '#cashUnauditedSpan');
					}else{
						ifrObject_.getLineChart('../cash/cash.do?method=getCashHisByTime&poiName='+cashPoiNameReportChart+'&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+cashGpsDeviceidsReportChart, 'cashChartDiv', '#cashUnauditedSpan');
					}
				}}
	    ]
	},
	listeners:{
		activate : function(p){
			initReportTimeScope(Ext.getCmp('CashReportYear'), Ext.getCmp('CashReportNum'));
		}
	}
});

